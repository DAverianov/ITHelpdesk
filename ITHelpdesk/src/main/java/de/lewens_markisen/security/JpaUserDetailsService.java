package de.lewens_markisen.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.lewens_markisen.domain.security.Authority;
import de.lewens_markisen.domain.security.User;
import de.lewens_markisen.repository.security.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
//public class JpaUserDetailsService implements UserDetailsService {
public class JpaUserDetailsService {

    private final UserRepository userRepository;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        User user = userRepository.findByUsername(username).orElseThrow(() -> {
//            return new UsernameNotFoundException("User name: " + username + " not found");
//        });
//
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
//                user.getEnabled(), user.getAccountNonExpired(), user.getCredentialsNonExpired(),
//                user.getAccountNonLocked(), convertToSpringAuthorities(user.getAuthorities()));
//    }

    private Collection<? extends GrantedAuthority> convertToSpringAuthorities(Set<Authority> authorities) {
        if (authorities != null && authorities.size() > 0){
            return authorities.stream()
                    .map(Authority::getRole)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        } else {
            return new HashSet<>();
        }
    }
}
