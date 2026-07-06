package ru.rekklez.userservice.authentication.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.rekklez.userservice.authentication.service.AuthenticationService;

@Service
public class DefaultAuthenticationService implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    public DefaultAuthenticationService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication authenticate(String email, String password) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

}
