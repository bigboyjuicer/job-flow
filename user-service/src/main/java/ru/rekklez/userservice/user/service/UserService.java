package ru.rekklez.userservice.user.service;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.rekklez.userservice.user.entity.UserEntity;

public interface UserService extends UserDetailsService {
    UserEntity loadUserById(long id);

    @Transactional
    UserEntity createUser(UserEntity user);

    @Transactional
    UserEntity updateUser(UserEntity user);

    @Transactional
    void updatePassword(String oldPassword, String newPassword, Authentication authentication);
}
