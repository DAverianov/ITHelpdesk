package de.lewens_markisen.bc_reports;

import java.util.ArrayList;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BcReportZeitnachweisPerson {
	Map<String, String> attribute;
	ArrayList<BcReportZeitNachweisKSaldo> saldo;
	ArrayList<BcReportZeitNachweisDateDescription> dateDescription;
}
