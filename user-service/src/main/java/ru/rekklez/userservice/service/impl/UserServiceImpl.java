package ru.rekklez.userservice.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.rekklez.userservice.entity.User;
import ru.rekklez.userservice.repository.UserRepository;
import ru.rekklez.userservice.service.UserService;
import ru.rekklez.userservice.util.dto.RegisterDTO;
import ru.rekklez.userservice.util.dto.UserDTO;
import ru.rekklez.userservice.util.mapper.RegisterDTOMapper;
import ru.rekklez.userservice.util.mapper.UserDTOMapper;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public UserDTO createUser(RegisterDTO newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User user = RegisterDTOMapper.INSTANCE.registerDTOToUser(newUser);
        return UserDTOMapper.INSTANCE.userToUserDT0(userRepository.save(user));
    }
}
