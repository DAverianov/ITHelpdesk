package de.lewens_markisen.domain.local_db.time_register_event;

import de.lewens_markisen.domain.local_db.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "day_art")
public class DayArt extends BaseEntity {

	@Column(name = "name")
	private String name;

	@Column(name = "minuten")
	@Min(value = 0)
	@Max(value = 60)
	@Builder.Default
	private Integer minuten = 0;

}
