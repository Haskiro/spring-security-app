package com.github.haskiro.FirstSecurityApp.security;

import com.github.haskiro.FirstSecurityApp.models.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class PersonDetails implements UserDetails {
    private final Person person;

    public PersonDetails(Person person) {
        this.person = person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ROLE_ADMIN, ROLE_USER
        return Collections.singletonList(new SimpleGrantedAuthority(person.getRole()));
    }

    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    @Override
    public String getUsername() {
        return this.person.getUsername();
    }

    @Override
    // аккаунт не просрочен
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    // аккаунт не заблокирован
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    // пароль не просрочен
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    // аккаунт включен, доступен
    public boolean isEnabled() {
        return true;
    }

    // Нужно чтобы получать данные аутентифицированного пользователя
    public Person getPerson() {
        return this.person;
    }
}
