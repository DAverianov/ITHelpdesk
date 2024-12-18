package de.lewens_markisen.access;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

import de.lewens_markisen.web.model.BaseItem;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccessDto extends BaseItem {

    @Builder
    public AccessDto(Long id, Integer version, OffsetDateTime createdDate, OffsetDateTime lastModifiedDate, String name,
    		String url, String domain, String user, String password, String description) {
        super();
        this.name = name;
        this.url = url;
        this.domain = domain;
        this.user = user;
        this.password = password;
        this.description = description;
    }

    private String name;
    private String url;
    private String domain;
    private String user;
    private String password;
    private String description;

}
