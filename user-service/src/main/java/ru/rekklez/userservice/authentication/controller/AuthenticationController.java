package ru.rekklez.userservice.authentication.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.rekklez.userservice.authentication.service.AuthenticationService;
import ru.rekklez.userservice.authentication.service.JwtService;
import ru.rekklez.userservice.user.dto.LoginRequest;
import ru.rekklez.userservice.user.dto.RegisterRequest;
import ru.rekklez.userservice.user.dto.User;
import ru.rekklez.userservice.user.service.UserService;
import ru.rekklez.userservice.web.ApiResponse;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public AuthenticationController(UserService userService, AuthenticationService authenticationService, JwtService jwtService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody @Valid RegisterRequest registerRequest) {
        User user = userService.createUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(user, "Successfully registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody LoginRequest user) {
        Authentication authenticate = authenticationService.authenticate(user);
        if(authenticate.isAuthenticated()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ApiResponse.success(
                            Map.of("accessToken", jwtService.generateJwt(authenticate.getName())),
                            "Successfully logged in"
                    ));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Something went wrong", null));
    }

}
