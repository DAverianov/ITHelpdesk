package de.lewens_markisen.domain.local_db.security;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import de.lewens_markisen.domain.local_db.Person;
import jakarta.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@Table(name = "user_spring")
public class UserSpring implements UserDetails, CredentialsContainer, AuthoritieNames{

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String bcCode;
    private String email;
    @OneToOne(fetch = FetchType.EAGER)
    private Person person;

    @Singular
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_spring_role",
        joinColumns = {@JoinColumn(name = "USER_SPRING_ID", referencedColumnName = "ID")},
        inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    private Set<Role> roles;

    @Transient
    public Set<GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(Role::getAuthorities)
                .flatMap(Set::stream)
                .map(authority -> {
                    return new SimpleGrantedAuthority(authority.getPermission());
                })
                .collect(Collectors.toSet());
    }
 
    @Builder.Default
    @Column(name = "account_non_expired")
    private Boolean accountNonExpired = true;

    @Builder.Default
    @Column(name = "account_non_locked")
    private Boolean accountNonLocked = true;

    @Builder.Default
    @Column(name = "credentials_non_expired")
    private Boolean credentialsNonExpired = true;

    @Builder.Default
    private Boolean enabled = true;

	@Override
	public void eraseCredentials() {
        this.password = null;
	}
	
	public String getFullname() {
		return getFirstname()+" "+getLastname()+" /"+getUsername()+"/";
	}

	@Override
	public String toString() {
		return "UserSpring [id=" + id + ", username=" + username + ", password=" + password + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", bcCode=" + bcCode + ", person=" + person + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountNonExpired, accountNonLocked, bcCode, credentialsNonExpired, enabled, firstname,
				lastname, password, person, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSpring other = (UserSpring) obj;
		return Objects.equals(accountNonExpired, other.accountNonExpired)
				&& Objects.equals(accountNonLocked, other.accountNonLocked) && Objects.equals(bcCode, other.bcCode)
				&& Objects.equals(credentialsNonExpired, other.credentialsNonExpired)
				&& Objects.equals(enabled, other.enabled) && Objects.equals(firstname, other.firstname)
				&& Objects.equals(lastname, other.lastname) && Objects.equals(password, other.password)
				&& Objects.equals(person, other.person) && Objects.equals(username, other.username);
	}

	@Override
	public List<String> getAuthoritieNames() {
		List<String> authNames = new ArrayList<String>();
		authNames.add("user.create");
		authNames.add("user.read");
		authNames.add("user.update");
		authNames.add("user.delete");
		return authNames;
	}

}
