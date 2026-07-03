package ru.rekklez.userservice.user.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.rekklez.userservice.user.service.UserService;

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
