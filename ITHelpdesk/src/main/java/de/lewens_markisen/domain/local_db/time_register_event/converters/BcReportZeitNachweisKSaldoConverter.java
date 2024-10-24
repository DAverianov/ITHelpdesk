package de.lewens_markisen.domain.local_db.time_register_event.converters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.lewens_markisen.bc_reports.BcReportZeitNachweisKSaldo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class BcReportZeitNachweisKSaldoConverter implements AttributeConverter<List<BcReportZeitNachweisKSaldo>, String> {

	@Override
	public List<BcReportZeitNachweisKSaldo> convertToEntityAttribute(String value) {
		try {
		    TypeReference<List<BcReportZeitNachweisKSaldo>> typeRef = new TypeReference<List<BcReportZeitNachweisKSaldo>>() {};
			return objectMapper.readValue(value, typeRef);
		} catch (JsonProcessingException e) {
			log.warn("Cannot convert JSON into Address");
			return null;
		}
	}

	@Override
	public String convertToDatabaseColumn(List<BcReportZeitNachweisKSaldo> attribute) {
		try {
			return objectMapper.writeValueAsString(attr);
		} catch (JsonProcessingException jpe) {
			log.warn("Cannot convert Address into JSON");
			return null;
		}
	}

}
