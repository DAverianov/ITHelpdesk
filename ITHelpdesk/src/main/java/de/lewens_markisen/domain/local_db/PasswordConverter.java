package de.lewens_markisen.domain.local_db;

import de.lewens_markisen.config.SpringContextHelper;
import de.lewens_markisen.services.EncryptionService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PasswordConverter implements AttributeConverter<String, String> {

	@Override
	public String convertToDatabaseColumn(String attribute) {
		return getEncryptionService().encrypt(attribute);
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
        return getEncryptionService().decrypt(dbData);
	}

    private EncryptionService getEncryptionService(){
     		
    	return SpringContextHelper.getApplicationContext().getBean(EncryptionService.class);
    }
}
