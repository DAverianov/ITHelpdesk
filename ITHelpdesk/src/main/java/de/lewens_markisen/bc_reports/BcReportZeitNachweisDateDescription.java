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
public class BcReportZeitNachweisDateDescription implements Serializable  {
	private String arbZCaption;
	private String date_Period_Start;
	private String gcodTAZ;
	private String gtisIst;
	private String gtisSoll;
	private String gtxtText;

}
