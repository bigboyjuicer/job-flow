package ru.rekklez.userservice.user.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.rekklez.userservice.user.repository.UserRepository;
import ru.rekklez.userservice.user.dto.RegisterRequest;
import ru.rekklez.userservice.user.mapper.RegisterRequestMapper;
import ru.rekklez.userservice.user.dto.User;
import ru.rekklez.userservice.user.entity.UserEntity;
import ru.rekklez.userservice.user.mapper.UserMapper;
import ru.rekklez.userservice.user.service.UserService;

@Service
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User createUser(RegisterRequest userToCreate) {
        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));
        UserEntity user = RegisterRequestMapper.INSTANCE.registerRequestToUser(userToCreate);
        return UserMapper.INSTANCE.userEntityToUser(userRepository.save(user));
    }

    @Override
    public User getUser(String email) {
        return UserMapper
                .INSTANCE
                .userEntityToUser(
                        userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email))
                );

    }
}
