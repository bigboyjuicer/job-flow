package ru.rekklez.userservice.authentication.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.rekklez.userservice.authentication.service.AuthenticationService;
import ru.rekklez.userservice.authentication.service.JwtService;
import ru.rekklez.userservice.security.SecurityUser;
import ru.rekklez.userservice.authentication.controller.dto.LoginRequest;
import ru.rekklez.userservice.authentication.controller.dto.RegisterRequest;
import ru.rekklez.userservice.user.entity.UserEntity;
import ru.rekklez.userservice.authentication.mapper.RegisterRequestMapper;
import ru.rekklez.userservice.user.service.UserService;
import ru.rekklez.userservice.web.ApiResponse;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public AuthenticationController(UserService userService, AuthenticationService authenticationService, JwtService jwtService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid RegisterRequest registerRequest) {
        UserEntity userEntity = RegisterRequestMapper.INSTANCE.mapToUserEntity(registerRequest);
        userService.createUser(new SecurityUser(userEntity));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null, "Successfully registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authenticate = authenticationService.authenticate(loginRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(
                        Map.of("accessToken", jwtService.generateJwt(authenticate.getName())),
                        "Successfully logged in"
                ));
    }

}
