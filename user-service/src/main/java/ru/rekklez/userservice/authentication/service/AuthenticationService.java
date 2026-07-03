package ru.rekklez.userservice.authentication.service;

import org.springframework.security.core.Authentication;
import ru.rekklez.userservice.user.dto.LoginRequest;

public interface AuthenticationService {

    Authentication authenticate(LoginRequest user);

}
