package ru.rekklez.userservice.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rekklez.userservice.service.UserService;
import ru.rekklez.userservice.util.dto.UserDTO;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*@GetMapping("/me")
    public UserDTO findUser(Authentication authentication) {
        return userService.findUser(authentication.getName(), authentication.getCredentials().toString());
    }*/

}
