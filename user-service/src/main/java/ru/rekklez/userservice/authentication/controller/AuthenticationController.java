package ru.rekklez.userservice.authentication.controller;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rekklez.userservice.authentication.controller.dto.request.LoginRequest;
import ru.rekklez.userservice.authentication.controller.dto.request.RefreshRequest;
import ru.rekklez.userservice.authentication.controller.dto.request.RegisterRequest;
import ru.rekklez.userservice.authentication.controller.dto.response.TokenResponse;
import ru.rekklez.userservice.authentication.mapper.RegisterRequestMapper;
import ru.rekklez.userservice.authentication.service.AuthenticationService;
import ru.rekklez.userservice.authentication.service.TokenService;
import ru.rekklez.userservice.security.SecurityUser;
import ru.rekklez.userservice.user.controller.dto.response.ProfileResponse;
import ru.rekklez.userservice.user.entity.UserEntity;
import ru.rekklez.userservice.user.mapper.ProfileResponseMapper;
import ru.rekklez.userservice.user.service.DefaultUserService;
import ru.rekklez.ApiResponse;
import ru.rekklez.userservice.web.exception.NotValidRefreshTokenException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private final DefaultUserService defaultUserService;
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;
    private final RegisterRequestMapper registerRequestMapper;
    private final ProfileResponseMapper profileResponseMapper;

    public AuthenticationController(DefaultUserService defaultUserService, AuthenticationService authenticationService, TokenService tokenService, RegisterRequestMapper registerRequestMapper, ProfileResponseMapper profileResponseMapper) {
        this.defaultUserService = defaultUserService;
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
        this.registerRequestMapper = registerRequestMapper;
        this.profileResponseMapper = profileResponseMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<ProfileResponse>> register(@RequestBody @Valid RegisterRequest registerRequest) {
        UserEntity userEntity = registerRequestMapper.mapToUserEntity(registerRequest);
        ProfileResponse profile = profileResponseMapper.mapToProfileResponse(defaultUserService.createUser(userEntity));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(profile, "Successfully registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationService.authenticate(loginRequest.email(), loginRequest.password());
        Long id = ((SecurityUser) authentication.getPrincipal()).user().getId();
        String email = authentication.getName();
        String role = authentication.getAuthorities().stream().findFirst().get().toString();

        String accessToken = tokenService.generateAccessToken(email, role, id);
        String refreshToken = tokenService.generateRefreshToken(email, role, id);

        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(tokenResponse, "Successfully logged in"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(@RequestBody RefreshRequest refreshRequest) {
        String refreshToken = refreshRequest.refreshToken();
        if(tokenService.refreshTokenIsValid(refreshToken)) {
            Claims claims = tokenService.extractClaimsFromRefreshToken(refreshToken);
            String email = claims.getSubject();
            String role = (String) claims.get("role");
            Long id = (Long) claims.get("id");
            String newAccessToken = tokenService.generateAccessToken(email, role, id);
            String newRefreshToken = tokenService.generateRefreshToken(email, role, id);

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
