package com.livk.auth.server.config;

import com.livk.auth.common.core.FormIdentityLoginConfigurer;
import com.livk.auth.common.core.UserDetailsAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * <p>
 * DefaultSecurityConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/7/18
 */
@EnableWebSecurity
public class DefaultSecurityConfiguration {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests(authorizeRequests ->
                        authorizeRequests.antMatchers("/token/*")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .headers()
                .frameOptions()
                .sameOrigin()
                .and()
                .apply(new FormIdentityLoginConfigurer())
                .and()
                .authenticationProvider(new UserDetailsAuthenticationProvider())
                .build();
    }

    @Bean
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
        return http.requestMatchers((matchers) -> matchers.antMatchers("/actuator/**", "/css/**", "/error"))
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
                .requestCache()
                .disable()
                .securityContext()
                .disable()
                .sessionManagement()
                .disable()
                .build();
    }
}
