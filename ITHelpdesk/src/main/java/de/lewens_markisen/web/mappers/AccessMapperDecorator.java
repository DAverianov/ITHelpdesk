package de.lewens_markisen.web.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import de.lewens_markisen.domain.Access;
import de.lewens_markisen.web.model.AccessDto;

public abstract class AccessMapperDecorator implements AccessMapper{

    private AccessMapper accessMapper;

    @Autowired
    @Qualifier("delegate")
    public void setBeerMapper(AccessMapper accessMapper) {
        this.accessMapper = accessMapper;
    }

    @Override
    public AccessDto accessToAccessDto(Access access) {
    	AccessDto dto = accessMapper.accessToAccessDto(access);
        return dto;
    }
}
