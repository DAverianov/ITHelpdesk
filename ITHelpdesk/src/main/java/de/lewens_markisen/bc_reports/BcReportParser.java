package de.lewens_markisen.bc_reports;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Component
public class BcReportParser {
	
	public List<BcReportZeitnachweisPerson> parse(String fileName) {

		List<BcReportZeitnachweisPerson> persons = new ArrayList<BcReportZeitnachweisPerson>();

		DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return persons;
		}
//		Document doc = builder.parse(new File("src/main/resources/Zeitnachweis Mitarbeiter.xml"));
		Document doc;
		try {
			doc = builder.parse(new File(fileName));
		} catch (SAXException e) {
			e.printStackTrace();
			return persons;
		} catch (IOException e) {
			e.printStackTrace();
			return persons;
		}
		doc.getDocumentElement().normalize();

		NodeList nodeList = doc.getElementsByTagName("ReportDataSet");
		Node first = nodeList.item(0);

		Node nodeDataItems = getNodeWithName(first, "DataItems");
		List<Node> nodeListPersons = getNodesWithAttrName(nodeDataItems, "DataItem", "AZ_Person");

		for (Node nodePerson : nodeListPersons) {
			List<Node> dataItemNodes = getDataItems(nodePerson, "Integer");
			Map<String, String> columns = getMapFromColumns(dataItemNodes);
			ArrayList<BcReportZeitNachweisKSaldo> saldo = getKSaldo(dataItemNodes);
			ArrayList<BcReportZeitNachweisDateDescription> dateDescription = getDateDescription(dataItemNodes);
			
			BcReportZeitnachweisPerson personDaten = new BcReportZeitnachweisPerson();
			personDaten.setAttribute(columns);
			personDaten.setSaldo(saldo);
			personDaten.setDateDescription(dateDescription);
			persons.add(personDaten);
		}
		return persons;
	}

	private ArrayList<BcReportZeitNachweisDateDescription> getDateDescription(List<Node> dataItemNodes) {
		ArrayList<BcReportZeitNachweisDateDescription> dateDescr = new ArrayList<BcReportZeitNachweisDateDescription>();
		for (Node nodeDataItem : dataItemNodes) {
			List<Node> dSaldoNodes = getDataItems(nodeDataItem, "Date");

			for (Node saldoNode : dSaldoNodes) {
				Map<String, String> columnsSaldo = getMapFromColumns(saldoNode);
				//@formatter:on
				dateDescr.add(BcReportZeitNachweisDateDescription.builder()
						.arbZCaption(columnsSaldo.get("ArbZCaption"))
						.date_Period_Start(columnsSaldo.get("Date_Period_Start"))
						.gcodTAZ(columnsSaldo.get("gcodTAZ"))
						.gtisIst(columnsSaldo.get("gtisIst"))
						.gtisSoll(columnsSaldo.get("gtisSoll"))
						.gtxtText(columnsSaldo.get("gtxtText"))
						.build());
				//@formatter:off
			}
		}
		return dateDescr;
	}

	private ArrayList<BcReportZeitNachweisKSaldo> getKSaldo(List<Node> dataItemNodes) {
		ArrayList<BcReportZeitNachweisKSaldo> saldo = new ArrayList<BcReportZeitNachweisKSaldo>();
		for (Node nodeDataItem : dataItemNodes) {
			List<Node> dSaldoNodes = getDataItems(nodeDataItem, "KSaldo");

			for (Node saldoNode : dSaldoNodes) {
				Map<String, String> columnsSaldo = getMapFromColumns(saldoNode);
				//@formatter:on
				saldo.add(BcReportZeitNachweisKSaldo.builder()
						.kSaldo_Bezeichnung(columnsSaldo.get("KSaldo_Bezeichnung"))
						.kSaldo_Code(columnsSaldo.get("KSaldo_Code"))
						.gtisBASaldoEndeAktPer(columnsSaldo.get("gtisBASaldoEndeAktPer"))
						.build());
				//@formatter:off
			}
		}
		return saldo;
	}

	private Map<String, String> getMapFromColumns(List<Node> parents) {
		Map<String, String> elements = new HashMap<String, String>();
		for (Node p : parents) {
			Map<String, String> elementsCol = getMapFromColumns(p);
			elementsCol.forEach((k,v) -> elements.put(k, v));
		}
		return elements;
	}

	private Map<String, String> getMapFromColumns(Node parent) {
		Map<String, String> elements = new HashMap<String, String>();
		Node columnsNode = getNodeWithName(parent, "Columns");
		if (columnsNode != null) {
			List<Node> columns = getNodesWithName(columnsNode, "Column");
			for (Node c : columns) {
				elements.put(getAttribute(c, "name"), c.getTextContent());
			}
		}
		return elements;
	}

	private List<Node> getDataItems(Node parent, String attrName) {
		Node nodeDataItems = getNodeWithName(parent, "DataItems");
		return getNodesWithAttrName(nodeDataItems, "DataItem", attrName);
	}

	private Map<String, Map<String, String>> getColumns(List<Node> nodes) {
		Map<String, Map<String, String>> res = new HashMap<String, Map<String, String>>();
		Map<String, String> attr = new HashMap<String, String>();
		List<Node> columns;
		for (Node node : nodes) {
			columns = getNodesWithName(node, "Column");
			attr.clear();
			columns.stream().forEach(c -> {
				attr.put(getAttribute(c, "name"), getTextContent(c));
			});
		}
		return null;
	}

	private String getTextContent(Node node) {
		String res = "";
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i).getNodeType() == 3) {
				return children.item(i).getTextContent();
			}
		}
		return res;
	}

	private String getAttribute(Node node, String attrName) {
		String res = "";
		NamedNodeMap attributes = node.getAttributes();
		Node nameAttrib = attributes.getNamedItem(attrName);
		if (nameAttrib == null) {
			return "";
		} else {
			NodeList children = nameAttrib.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				if (children.item(i).getNodeType() == 3) {
					res = children.item(i).getTextContent();
				}
			}
		}
		return res;
	}

	private Node getNodeWithName(Node node, String nodeName) {
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if (nodeName.equals(children.item(i).getNodeName())) {
				return children.item(i);
			}
		}
		return null;
	}

	private List<Node> getNodesWithName(Node node, String nodeName) {
		List<Node> nodes = new ArrayList<Node>();

		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if (nodeName.equals(children.item(i).getNodeName())) {
				nodes.add(children.item(i));
			}
		}
		return nodes;
	}

	private List<Node> getNodesWithAttrName(Node node, String nodeName, String attributeValue) {
		List<Node> res = new ArrayList<Node>();
		NodeList children = node.getChildNodes();
		Node currNode;
		for (int i = 0; i < children.getLength(); i++) {
			currNode = children.item(i);
			if (nodeName.equals(currNode.getNodeName())) {
				NamedNodeMap attributes = currNode.getAttributes();
				Node nameAttrib = attributes.getNamedItem("name");
				if (nameAttrib == null) {
					continue;
				} else {
					if (attributeValue.equals(nameAttrib.getNodeValue())) {
						res.add(currNode);
					}
				}
			}
		}
		return res;
	}
}
