package ru.rekklez.userservice.service;

import org.springframework.security.core.Authentication;
import ru.rekklez.userservice.util.dto.LoginDTO;

public interface AuthenticationService {

    Authentication authenticate(LoginDTO user);

}
