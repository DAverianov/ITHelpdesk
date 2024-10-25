package de.lewens_markisen.domain.local_db.time_register_event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.lewens_markisen.bc_reports.BcReportZeitNachweisDateDescription;
import de.lewens_markisen.bc_reports.BcReportZeitNachweisDateDescriptionList;
import de.lewens_markisen.bc_reports.BcReportZeitNachweisKSaldo;
import de.lewens_markisen.bc_reports.BcReportZeitNachweisKSaldoList;
import de.lewens_markisen.domain.local_db.BaseEntity;
import de.lewens_markisen.domain.local_db.Person;
import de.lewens_markisen.domain.local_db.time_register_event.converters.AttributeMapStringConverter;
import de.lewens_markisen.domain.local_db.time_register_event.converters.BcReportZeitNachweisDateDescriptionListConverter;
import de.lewens_markisen.domain.local_db.time_register_event.converters.BcReportZeitNachweisKSaldoListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "person_in_bc_report_month")
public class PersonInBcReport extends BaseEntity {

	@ManyToOne
	private Person person;

	@Column(name = "month", columnDefinition = "DATE")
	private LocalDate month;

	@Convert(converter = AttributeMapStringConverter.class)
	@Column(name = "attribute")
	private Map<String, String> attribute;

	@Convert(converter = BcReportZeitNachweisDateDescriptionListConverter.class)
	@Column(name = "date_table")
	private BcReportZeitNachweisDateDescriptionList dateTable;

	@Convert(converter = BcReportZeitNachweisKSaldoListConverter.class)
	@Column(name = "saldo")
	private BcReportZeitNachweisKSaldoList saldo;

}
