package de.lewens_markisen.bc_reports;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class BcReportZeitNachweisKSaldo {
	private String kSaldo_Bezeichnung;
	private String kSaldo_Code;
	private String gtisBASaldoEndeAktPer;

}
