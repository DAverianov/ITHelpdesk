package de.lewens_markisen.domain.local_db.time_register_event.converters;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class AttributeMapStringConverter implements AttributeConverter<Map<String, String>, String>  {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(Map<String, String> attr) {
		if (attr == null) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(attr);
		} catch (JsonProcessingException jpe) {
			log.warn("Cannot convert Map<String, String> into JSON");
			return null;
		}
	}

	@Override
	public Map<String, String> convertToEntityAttribute(String value) {
		if (value == null) {
			return null;
		}
		try {
		    TypeReference<HashMap<String,String>> typeRef = new TypeReference<HashMap<String,String>>() {};
			return objectMapper.readValue(value, typeRef);
		} catch (JsonProcessingException e) {
			log.warn("Cannot convert JSON into Map<String, String>");
			return null;
		}
	}
}
