package de.lewens_markisen.email.model;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import de.lewens_markisen.domain.local_db.email.EmailAcсount;
import de.lewens_markisen.web.mappers.DateMapper;

@Mapper(uses = DateMapper.class)
@DecoratedWith(EmailAccountMapperDecorator.class)
public interface EmailAccountMapper {

    EmailAccountDto emailAccountToEmailAccountDto(EmailAcсount emailAccount);

    EmailAcсount emailAccountDtoToEmailAccount(EmailAccountDto emailAccountDto);
}
