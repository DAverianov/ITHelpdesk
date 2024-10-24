package de.lewens_markisen.bc_reports;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BcReportZeitnachweisPerson {
	Map<String, String> attribute;
	List<BcReportZeitNachweisKSaldo> saldo;
	List<BcReportZeitNachweisDateDescription> dateDescription;
}
