package ru.rekklez.userservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.rekklez.userservice.service.UserService;
import ru.rekklez.userservice.util.dto.LoginDto;
import ru.rekklez.userservice.util.dto.UserDTO;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public UserDTO findUser(@RequestBody LoginDto login) {
        return userService.findUser(login.getEmail(), login.getPassword());
    }

}
