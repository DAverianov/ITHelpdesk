package de.lewens_markisen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import de.lewens_markisen.security.SfgPasswordEncoderFactories;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

//    @Bean
//    PasswordEncoder passwordEncoder(){
//        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }


	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authz) -> authz
                    .requestMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                                    .anyRequest().authenticated()
            )
            .httpBasic(withDefaults());
        return http.build();
    }

}
