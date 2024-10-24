package de.lewens_markisen.bc_reports;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class BcReportZeitNachweisDateDescription {
	private String arbZCaption;
	private String date_Period_Start;
	private String gcodTAZ;
	private String gtisIst;
	private String gtisSoll;
	private String gtxtText;

}
