package ru.rekklez.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.rekklez.userservice.service.AuthenticationService;
import ru.rekklez.userservice.service.UserService;
import ru.rekklez.userservice.util.dto.LoginDTO;
import ru.rekklez.userservice.util.dto.RegisterDTO;
import ru.rekklez.userservice.util.dto.UserDTO;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody RegisterDTO newUser) {
        return ResponseEntity.status(201).body(userService.createUser(newUser));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO user) {
        Authentication authenticate = authenticationService.authenticate(user);
        if(authenticate.isAuthenticated()) {
            return ResponseEntity.ok().body("Successfully logged in");
        }
        return ResponseEntity.status(500).body("Something went wrong");
    }

}
