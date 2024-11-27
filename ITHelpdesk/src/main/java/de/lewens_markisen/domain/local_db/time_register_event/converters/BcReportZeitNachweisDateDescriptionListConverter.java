package de.lewens_markisen.domain.local_db.time_register_event.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.lewens_markisen.bc_reports.BcReportZeitNachweisDateDescriptionList;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class BcReportZeitNachweisDateDescriptionListConverter implements AttributeConverter<BcReportZeitNachweisDateDescriptionList, String> {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public BcReportZeitNachweisDateDescriptionList convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		try {
			return objectMapper.readValue(value, BcReportZeitNachweisDateDescriptionList.class);
		} catch (JsonProcessingException e) {
			log.warn("Cannot convert JSON into BcReportZeitNachweisDateDescriptionList");
			return null;
		}
	}

	@Override
	public String convertToDatabaseColumn(BcReportZeitNachweisDateDescriptionList attribute) {
		if (attribute == null) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(attribute);
		} catch (JsonProcessingException jpe) {
			log.warn("Cannot convert BcReportZeitNachweisDateDescriptionList into JSON");
			return null;
		}
	}

}
