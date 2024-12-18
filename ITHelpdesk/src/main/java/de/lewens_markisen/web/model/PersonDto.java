package de.lewens_markisen.web.model;

import de.lewens_markisen.domain.local_db.person.Sex;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PersonDto extends BaseItem {
	
	@Builder
	public PersonDto(String name, String nameForSearch, String bcCode,
			String idCard, Sex sex) {
		super();
		this.name = name;
		this.nameForSearch = nameForSearch;
		this.bcCode = bcCode;
		this.idCard = idCard;
		this.sex = sex;
	}
	private String name;
	private String nameForSearch;
	private String bcCode;
	private String idCard;
	private Sex sex;

}
