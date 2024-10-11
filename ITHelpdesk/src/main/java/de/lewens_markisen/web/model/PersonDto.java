package de.lewens_markisen.web.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PersonDto extends BaseItem {

	@Builder
	public PersonDto(Long id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, String name,
			String nameForSearch, String bcCode) {
		super();
		this.name = name;
		this.bcCode = bcCode;
		this.nameForSearch = nameForSearch;
	}
	
	private String name;
	private String nameForSearch;
	private String bcCode;

}
