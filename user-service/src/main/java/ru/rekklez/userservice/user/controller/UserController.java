package ru.rekklez.userservice.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rekklez.userservice.user.dto.User;
import ru.rekklez.userservice.user.service.UserService;
import ru.rekklez.userservice.web.ApiResponse;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getUserDetails(Authentication authentication) {
        User user = userService.getUser(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(user, "Successfully loaded user details"));
    }

}
