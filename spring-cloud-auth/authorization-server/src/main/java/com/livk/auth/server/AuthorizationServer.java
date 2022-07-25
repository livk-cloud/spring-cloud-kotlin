package com.livk.auth.server;


import com.livk.auth.common.core.principal.Oauth2User;
import com.livk.auth.common.service.Oauth2UserDetailsService;
import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <p>
 * AuthorizationServer
 * </p>
 *
 * @author livk
 * @date 2022/7/15
 */
@SpringBootApplication
public class AuthorizationServer {
    public static void main(String[] args) {
        LivkSpring.run(AuthorizationServer.class, args);
    }


    Oauth2User oauth2User() {
        return new Oauth2User(123L, "livk", "123456", "18664967020",
                true, true, true, true,
                AuthorityUtils.createAuthorityList("USER"));
    }

    @Bean
    public Oauth2UserDetailsService users() {
        return new Oauth2UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                if ("livk".equals(username)) {
                    return oauth2User();
                }
                return null;
            }
        };
    }
}
