package de.lewens_markisen.access;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import de.lewens_markisen.web.mappers.DateMapper;

@Mapper(uses = DateMapper.class)
@DecoratedWith(AccessMapperDecorator.class)
public interface AccessMapper {

    AccessDto accessToAccessDto(Access access);

    Access accessDtoToAccess(AccessDto accessDto);
}
