package de.lewens_markisen.email.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import de.lewens_markisen.domain.local_db.Access;
import de.lewens_markisen.web.model.BaseItem;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class EmailAccountDto extends BaseItem {

    private String email;
    private String host;
    private Integer port;
    private String username;
    private String outgoingProtocol;
    private String smtpAuth;
    private String smtpStarttlsEnable;
    private String description;
    private String access;

}
