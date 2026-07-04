package ru.rekklez.userservice.user.service;

import ru.rekklez.userservice.user.dto.RegisterRequest;
import ru.rekklez.userservice.user.dto.User;

public interface UserService {
    User createUser(RegisterRequest registerDTO);
    User getUser(String email);
}
