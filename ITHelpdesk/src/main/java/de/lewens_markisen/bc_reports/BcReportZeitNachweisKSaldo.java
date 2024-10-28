package de.lewens_markisen.bc_reports;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BcReportZeitNachweisKSaldo implements Serializable  {
	private String kSaldo_Bezeichnung;
	private String kSaldo_Code;
	private String gtisBASaldoEndeAktPer;
	private String gtisBASaldoEndeVorPer;
}
