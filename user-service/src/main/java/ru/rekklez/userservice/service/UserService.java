package ru.rekklez.userservice.service;

import ru.rekklez.userservice.util.dto.RegisterDTO;
import ru.rekklez.userservice.util.dto.UserDTO;

public interface UserService {
    UserDTO createUser(RegisterDTO registerDTO);
}
