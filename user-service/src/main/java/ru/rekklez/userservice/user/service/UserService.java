package ru.rekklez.userservice.user.service;

import ru.rekklez.userservice.user.dto.RegisterRequest;
import ru.rekklez.userservice.user.dto.User;
import ru.rekklez.userservice.user.dto.UpdateUserProfileRequest;

public interface UserService {
    User createUser(RegisterRequest registerDTO);
    User getUser(String email);
    void updateUserProfile(String email, UpdateUserProfileRequest profile);
    void updateUserPassword(String email, String newPassword);
}
