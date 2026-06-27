package ru.rekklez.userservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.rekklez.userservice.entity.User;
import ru.rekklez.userservice.security.user.SecurityUser;
import ru.rekklez.userservice.service.AuthenticationService;
import ru.rekklez.userservice.service.UserService;
import ru.rekklez.userservice.util.dto.LoginDTO;
import ru.rekklez.userservice.util.dto.RegisterRequest;
import ru.rekklez.userservice.util.dto.UserDTO;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(UserDetailsService userDetailsService, UserService userService, AuthenticationService authenticationService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid RegisterRequest registerRequest) {
        return ResponseEntity.status(201).body(userService.createUser(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO user) {
        Authentication authenticate = authenticationService.authenticate(user);
        if(authenticate.isAuthenticated()) {
            return ResponseEntity.ok().body("Successfully logged in");
        }
        return ResponseEntity.status(500).body("Something went wrong");
    }

    @GetMapping("/me")
    public ResponseEntity<User> me(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) userDetailsService.loadUserByUsername(authentication.getName());
        return ResponseEntity.ok(securityUser.getUser());
    }

}
