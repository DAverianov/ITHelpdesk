package de.lewens_markisen.domain.local_db.time_register_event.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.lewens_markisen.bc_reports.BcReportZeitNachweisKSaldoList;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class BcReportZeitNachweisKSaldoListConverter
		implements AttributeConverter<BcReportZeitNachweisKSaldoList, String> {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public BcReportZeitNachweisKSaldoList convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		try {
			return objectMapper.readValue(value, BcReportZeitNachweisKSaldoList.class);

		} catch (JsonProcessingException e) {
			log.warn("Cannot convert JSON into BcReportZeitNachweisKSaldoList");
			return null;
		}
	}

	@Override
	public String convertToDatabaseColumn(BcReportZeitNachweisKSaldoList attribute) {
		if (attribute == null) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(attribute);
		} catch (JsonProcessingException jpe) {
			log.warn("Cannot convert BcReportZeitNachweisKSaldoList into JSON");
			return null;
		}
	}

}
