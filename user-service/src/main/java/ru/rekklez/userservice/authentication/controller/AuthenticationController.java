package ru.rekklez.userservice.authentication.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.rekklez.userservice.authentication.controller.dto.request.RefreshRequest;
import ru.rekklez.userservice.authentication.controller.dto.response.TokenResponse;
import ru.rekklez.userservice.authentication.service.AuthenticationService;
import ru.rekklez.userservice.authentication.service.TokenService;
import ru.rekklez.userservice.security.SecurityUser;
import ru.rekklez.userservice.authentication.controller.dto.request.LoginRequest;
import ru.rekklez.userservice.authentication.controller.dto.request.RegisterRequest;
import ru.rekklez.userservice.user.entity.UserEntity;
import ru.rekklez.userservice.authentication.mapper.RegisterRequestMapper;
import ru.rekklez.userservice.user.service.UserService;
import ru.rekklez.userservice.web.ApiResponse;
import ru.rekklez.userservice.web.exception.NotValidRefreshTokenException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;

    public AuthenticationController(UserService userService, AuthenticationService authenticationService, TokenService tokenService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid RegisterRequest registerRequest) {
        UserEntity userEntity = RegisterRequestMapper.INSTANCE.mapToUserEntity(registerRequest);
        userService.createUser(new SecurityUser(userEntity));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null, "Successfully registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        authenticationService.authenticate(loginRequest.email(), loginRequest.password());
        String email = loginRequest.email();

        String accessToken = tokenService.generateAccessToken(email);
        String refreshToken = tokenService.generateRefreshToken(email);

        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(tokenResponse, "Successfully logged in"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(@RequestBody RefreshRequest refreshRequest) {
        String refreshToken = refreshRequest.refreshToken();
        if(tokenService.refreshTokenIsValid(refreshToken)) {
            String email = tokenService.extractEmailFromRefreshToken(refreshToken);
            String newAccessToken = tokenService.generateAccessToken(email);
            String newRefreshToken = tokenService.generateRefreshToken(email);

            TokenResponse tokenResponse = new TokenResponse(newAccessToken, newRefreshToken);

            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(tokenResponse, "Successfully refreshed token"));
        }
        throw new NotValidRefreshTokenException("Not valid refresh token");
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(Authentication authentication) {
        String email = authentication.getName();
        tokenService.evictRefreshToken(email);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null, "Successfully logged out"));
    }

}
