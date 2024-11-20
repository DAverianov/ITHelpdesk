package de.lewens_markisen.email.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import de.lewens_markisen.domain.local_db.email.EmailAccountLss;

public abstract class EmailAccountMapperDecorator implements EmailAccountMapper{

    private EmailAccountMapper emailAccountMapper;

    @Autowired
    @Qualifier("delegate")
    public void setAccessMapper(EmailAccountMapper emailAccountMapper) {
        this.emailAccountMapper = emailAccountMapper;
    }

    @Override
    public EmailAccountDto emailAccountToEmailAccountDto(EmailAccountLss emailAccount) {
    	EmailAccountDto dto = emailAccountMapper.emailAccountToEmailAccountDto(emailAccount);
        return dto;
    }
}
