package ru.rekklez.userservice.security;

import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.rekklez.userservice.user.entity.UserEntity;

import java.util.Collection;
import java.util.List;

public record SecurityUser(UserEntity user) implements UserDetails {

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    @NullMarked
    public String getUsername() {
        return user.getEmail();
    }
}
