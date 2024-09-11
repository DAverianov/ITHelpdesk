package de.lewens_markisen.web.mappers;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import de.lewens_markisen.domain.Access;
import de.lewens_markisen.web.model.AccessDto;

@Mapper(uses = DateMapper.class)
@DecoratedWith(AccessMapperDecorator.class)
public interface AccessMapper {

    AccessDto accessToAccessDto(Access access);

    Access accessDtoToAccess(AccessDto accessDto);
}
