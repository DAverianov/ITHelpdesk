package de.lewens_markisen.web.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccessDto extends BaseItem {

    @Builder
    public AccessDto(Long id, Integer version, OffsetDateTime createdDate, OffsetDateTime lastModifiedDate, String name,
    		String domain, String user, String password, String description) {
        super(id, version, createdDate, lastModifiedDate);
        this.name = name;
        this.domain = domain;
        this.user = user;
        this.password = password;
        this.description = description;
    }

    private String name;
    private String domain;
    private String user;
    private String password;
    private String description;

}
