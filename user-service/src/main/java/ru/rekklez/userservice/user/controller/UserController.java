package ru.rekklez.userservice.user.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.rekklez.userservice.user.dto.UpdatePasswordRequest;
import ru.rekklez.userservice.user.dto.User;
import ru.rekklez.userservice.user.dto.UpdateUserProfileRequest;
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
    public ResponseEntity<ApiResponse<User>> getUser(Authentication authentication) {
        User user = userService.getUser(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(user, "Successfully loaded user details"));
    }

    @PostMapping("/me")
    public ResponseEntity<ApiResponse<Void>> updateUserProfile(Authentication authentication, @RequestBody @Valid UpdateUserProfileRequest profile) {
        userService.updateUserProfile(authentication.getName(), profile);
        return  ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null, "Successfully updated profile"));
    }

    @PostMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> updateUserPassword(Authentication authentication, @RequestBody @Valid UpdatePasswordRequest updatedPassword) {
        userService.updateUserPassword(authentication.getName(), updatedPassword.password());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null, "Successfully changed password"));
    }

}
