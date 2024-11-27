package de.lewens_markisen.bc_reports;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import de.lewens_markisen.utils.TimeUtils;
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
	private String soll;
	
	public String getSoll() {
		String soll = "";
		if (!StringUtils.startsWith(this.gtxtText, "Urlaub")) {
			soll = this.gtisSoll;
		}
		return soll;
	}

}
