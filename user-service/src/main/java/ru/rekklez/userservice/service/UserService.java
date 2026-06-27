package ru.rekklez.userservice.service;

import ru.rekklez.userservice.util.dto.RegisterRequest;
import ru.rekklez.userservice.util.dto.UserDTO;

public interface UserService {
    UserDTO createUser(RegisterRequest registerDTO);
}
