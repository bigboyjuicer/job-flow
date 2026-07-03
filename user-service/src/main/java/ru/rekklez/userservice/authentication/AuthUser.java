package ru.rekklez.userservice.authentication;

import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.rekklez.userservice.user.entity.UserEntity;

import java.util.Collection;
import java.util.List;

public record AuthUser(UserEntity user) implements UserDetails {

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> user.getRole().name());
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
