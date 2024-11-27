package de.lewens_markisen.bc_reports;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BcReportZeitNachweisDateDescriptionList implements Serializable{
	private List<BcReportZeitNachweisDateDescription> dateTable;
}
