package ru.rekklez.userservice.user.service;

import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.rekklez.userservice.user.entity.UserEntity;
import ru.rekklez.userservice.user.repository.UserRepository;
import ru.rekklez.userservice.security.SecurityUser;
import ru.rekklez.userservice.web.exception.UserAlreadyExistsException;
import ru.rekklez.userservice.web.exception.WrongPasswordException;

@Service
public class UserService implements UserDetailsManager {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found (email = " + email + " )"));
        return new SecurityUser(user);
    }

    @Override
    @Transactional
    public void createUser(@NonNull UserDetails user) {
        UserEntity userEntity = ((SecurityUser) user).user();
        if(userExists(userEntity.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
        userEntity.setPasswordHash(passwordEncoder.encode(userEntity.getPasswordHash()));
        userRepository.save(userEntity);
        log.info("New user registered");
    }

    @Override
    @Transactional
    public void updateUser(@NonNull UserDetails user) {
        UserEntity userEntity = ((SecurityUser) user).user();
        if(userExists(userEntity.getEmail())) {
            userRepository.updateUserProfile(userEntity.getEmail(), userEntity.getFirstName(), userEntity.getLastName(), userEntity.getCompanyName());
        } else {
            throw new UsernameNotFoundException("User not found (email = " + userEntity.getEmail() + " )");
        }
    }

    @Override
    public void deleteUser(@NonNull String email) {

    }

    @Override
    @Transactional
    public void changePassword(@Nullable String oldPassword, @Nullable String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            UserEntity user = ((SecurityUser) loadUserByUsername(authentication.getName())).user();
            if(passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
                String passwordHash = passwordEncoder.encode(newPassword);
                userRepository.updateUserPassword(authentication.getName(), passwordHash);
            } else {
                throw new WrongPasswordException("Wrong password");
            }
        } else {
            throw new AuthenticationCredentialsNotFoundException("Authentication credentials not found");
        }
    }

    @Override
    public boolean userExists(@NonNull String email) {
        return userRepository.existsByEmail(email);
    }
}
