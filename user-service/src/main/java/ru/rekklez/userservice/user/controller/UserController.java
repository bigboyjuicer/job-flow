package ru.rekklez.userservice.user.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.rekklez.ApiResponse;
import ru.rekklez.userservice.security.SecurityUser;
import ru.rekklez.userservice.user.controller.dto.request.UpdatePasswordRequest;
import ru.rekklez.userservice.user.controller.dto.request.UpdateUserProfileRequest;
import ru.rekklez.userservice.user.controller.dto.response.ProfileResponse;
import ru.rekklez.userservice.user.entity.UserEntity;
import ru.rekklez.userservice.user.mapper.ProfileResponseMapper;
import ru.rekklez.userservice.user.mapper.UpdateUserMapper;
import ru.rekklez.userservice.user.service.DefaultUserService;
import ru.rekklez.userservice.user.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ProfileResponseMapper profileResponseMapper;

    public UserController(DefaultUserService userService, ProfileResponseMapper profileResponseMapper) {
        this.userService = userService;
        this.profileResponseMapper = profileResponseMapper;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<ProfileResponse>> getUser(Authentication authentication) {
        UserEntity userEntity = ((SecurityUser) userService.loadUserByUsername(authentication.getName())).user();
        ProfileResponse profileResponse = profileResponseMapper.mapToProfileResponse(userEntity);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(profileResponse, "Successfully loaded user details"));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<Void>> updateUserProfile(Authentication authentication, @RequestBody @Valid UpdateUserProfileRequest profile) {
        UserEntity userEntity = UpdateUserMapper.INSTANCE.mapToUserEntity(profile);
        userEntity.setEmail(authentication.getName());
        userService.updateUser(userEntity);
        return  ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null, "Successfully updated profile"));
    }

    @PutMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> updateUserPassword(@RequestBody @Valid UpdatePasswordRequest updatedPassword, Authentication authentication) {
        userService.updatePassword(updatedPassword.oldPassword(), updatedPassword.newPassword(), authentication);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null, "Successfully changed password"));
    }

    @GetMapping("/{id}")
    @Secured("ROLE_EMPLOYER")
    public ResponseEntity<ApiResponse<ProfileResponse>> getCandidate(@PathVariable long id) {
        UserEntity userEntity = ((SecurityUser) userService.loadUserById(id)).user();
        ProfileResponse profileResponse = profileResponseMapper.mapToProfileResponse(userEntity);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(profileResponse, "Successfully loaded candidate details"));
    }

}
