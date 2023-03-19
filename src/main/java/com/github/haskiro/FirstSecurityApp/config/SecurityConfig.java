package com.github.haskiro.FirstSecurityApp.config;

import com.github.haskiro.FirstSecurityApp.security.AuthProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final AuthProviderImpl authProvider;

    @Autowired
    public SecurityConfig(AuthProviderImpl authProvider) {
        this.authProvider = authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests().requestMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                 .usernameParameter("username")
                .permitAll()
                .and()
                .logout().permitAll()
                .and().authenticationProvider(authProvider);

        return http.build();
    }

}
