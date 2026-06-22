package ru.rekklez.userservice.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.rekklez.userservice.entity.User;
import ru.rekklez.userservice.repository.UserRepository;
import ru.rekklez.userservice.util.dto.UserDTO;
import ru.rekklez.userservice.util.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDTO findUser(String email, String password) {
        User user = userRepository.findUser(email, password).orElseThrow(RuntimeException::new);
        return UserMapper.INSTANCE.userToUserDT0(user);
    }

}
