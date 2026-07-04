package ru.rekklez.userservice.user.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.rekklez.userservice.security.SecurityUser;
import ru.rekklez.userservice.user.controller.dto.request.UpdatePasswordRequest;
import ru.rekklez.userservice.user.controller.dto.request.UpdateUserProfileRequest;
import ru.rekklez.userservice.user.controller.dto.response.ProfileResponse;
import ru.rekklez.userservice.user.entity.UserEntity;
import ru.rekklez.userservice.user.mapper.ProfileResponseMapper;
import ru.rekklez.userservice.user.mapper.UpdateUserMapper;
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
    public ResponseEntity<ApiResponse<ProfileResponse>> getUser(Authentication authentication) {
        UserEntity userEntity = ((SecurityUser) userService.loadUserByUsername(authentication.getName())).user();
        ProfileResponse profileResponse = ProfileResponseMapper.INSTANCE.mapToProfileResponse(userEntity);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(profileResponse, "Successfully loaded user details"));
    }

    @PostMapping("/me")
    public ResponseEntity<ApiResponse<Void>> updateUserProfile(Authentication authentication, @RequestBody @Valid UpdateUserProfileRequest profile) {
        UserEntity userEntity = UpdateUserMapper.INSTANCE.mapToUserEntity(profile);
        userEntity.setEmail(authentication.getName());
        userService.updateUser(new SecurityUser(userEntity));
        return  ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null, "Successfully updated profile"));
    }

    @PostMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> updateUserPassword(@RequestBody @Valid UpdatePasswordRequest updatedPassword) {
        userService.changePassword(updatedPassword.oldPassword(), updatedPassword.newPassword());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null, "Successfully changed password"));
    }

    @GetMapping("/{email}")
    //TODO: Сделать ролевой доступ
    public ResponseEntity<ApiResponse<ProfileResponse>> getCandidate(@PathVariable String email) {
        UserEntity userEntity = ((SecurityUser) userService.loadUserByUsername(email)).user();
        ProfileResponse profileResponse = ProfileResponseMapper.INSTANCE.mapToProfileResponse(userEntity);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(profileResponse, "Successfully loaded candidate details"));
    }

}
