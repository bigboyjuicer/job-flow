package ru.rekklez.userservice.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.rekklez.userservice.authentication.controller.dto.request.RegisterRequest;
import ru.rekklez.userservice.security.SecurityUser;
import ru.rekklez.userservice.user.entity.Role;
import ru.rekklez.userservice.user.entity.UserEntity;
import ru.rekklez.userservice.user.repository.UserRepository;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceCreateTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void successfulCreateTest() {
        UserEntity newUser = new UserEntity(
                "1234@mail.ru",
                "12345678",
                Role.CANDIDATE,
                "Ivan",
                "Petrov",
                null
        );

        SecurityUser user = new SecurityUser(newUser);

        when(userService.userExists(newUser.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(newUser.getPasswordHash())).thenReturn(newUser.getPasswordHash());
        //when(userRepository.save(newUser)).thenReturn(newUser);

        userService.createUser(user);

        verify(userRepository).save(newUser);
    }


}
