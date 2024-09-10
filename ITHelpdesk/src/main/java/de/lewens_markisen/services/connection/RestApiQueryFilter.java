package de.lewens_markisen.services.connection;

import lombok.Getter;
import lombok.Builder;

@Getter
public class RestApiQueryFilter {
	@Builder
	public RestApiQueryFilter(String attribute, String value, String comparisonType) {
		super();
		this.attribute = attribute;
		this.value = value;
		this.comparisonType = comparisonType;
	}
	private String attribute;
	private String value;
	private String comparisonType;
}
