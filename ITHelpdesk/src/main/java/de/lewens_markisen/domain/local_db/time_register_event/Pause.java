package de.lewens_markisen.domain.local_db.time_register_event;

import de.lewens_markisen.domain.local_db.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "pause")
public class Pause extends BaseEntity {

	@Column(name = "name")
	private String name;

	@Column(name = "minuten")
	private Integer minuten;

}
