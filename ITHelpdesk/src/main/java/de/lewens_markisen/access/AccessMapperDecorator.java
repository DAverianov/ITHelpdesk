package de.lewens_markisen.access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import de.lewens_markisen.domain.local_db.Access;

public abstract class AccessMapperDecorator implements AccessMapper{

    private AccessMapper accessMapper;

    @Autowired
    @Qualifier("delegate")
    public void setAccessMapper(AccessMapper accessMapper) {
        this.accessMapper = accessMapper;
    }

    @Override
    public AccessDto accessToAccessDto(Access access) {
    	AccessDto dto = accessMapper.accessToAccessDto(access);
        return dto;
    }
}
