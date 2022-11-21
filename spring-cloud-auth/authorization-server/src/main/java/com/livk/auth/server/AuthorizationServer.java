package com.livk.auth.server;

import com.livk.auth.common.core.principal.Oauth2User;
import com.livk.auth.common.service.Oauth2UserDetailsService;
import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * <p>
 * AuthorizationServer
 * </p>
 *
 * @author livk
 * @date 2022/7/15
 * @see com.livk.auth.common.core.UserDetailsAuthenticationProvider
 */
@SpringBootApplication
public class AuthorizationServer {
    public static void main(String[] args) {
        LivkSpring.run(AuthorizationServer.class, args);
    }


    Oauth2User oauth2User() {
        return new Oauth2User(123L, "livk", new BCryptPasswordEncoder().encode("123456"),
                "18664960000", true, true, true,
                true, AuthorityUtils.createAuthorityList("USER"));
    }

    @Bean
    public Oauth2UserDetailsService users() {

        return username -> {
            if ("livk".equals(username)) {
                return oauth2User();
            }
            if ("18664960000".equals(username)) {
                return oauth2User();
            }
            return null;
        };
    }
}
