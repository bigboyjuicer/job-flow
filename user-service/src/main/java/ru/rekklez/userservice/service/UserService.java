package ru.rekklez.userservice.service;

import ru.rekklez.userservice.util.dto.UserDTO;

public interface UserService {
    UserDTO findUser(String username, String password);
}
