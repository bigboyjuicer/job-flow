package ru.rekklez.userservice.authentication.service;

import org.springframework.security.core.Authentication;
import ru.rekklez.userservice.authentication.controller.dto.request.LoginRequest;

public interface AuthenticationService {

    Authentication authenticate(String email, String password);

}
