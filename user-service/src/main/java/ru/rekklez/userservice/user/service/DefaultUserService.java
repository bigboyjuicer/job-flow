package ru.rekklez.userservice.user.service;

import jakarta.transaction.Transactional;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.rekklez.userservice.security.SecurityUser;
import ru.rekklez.userservice.user.entity.UserEntity;
import ru.rekklez.userservice.user.repository.UserRepository;
import ru.rekklez.userservice.web.exception.UserAlreadyExistsException;
import ru.rekklez.userservice.web.exception.WrongPasswordException;

@Service
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if(email == null || email.isEmpty()) throw new IllegalArgumentException("Email cannot be null or empty");
        UserEntity user = userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with this email not found"));
        return new SecurityUser(user);
    }

    @Override
    public UserDetails loadUserById(long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with this id not found"));
        return new SecurityUser(user);
    }

    @Transactional
    @Override
    public UserEntity createUser(UserEntity user) {
        if(user == null) throw new IllegalArgumentException("User cannot be null");
        if(userRepository.existsByEmail(user.getEmail())) throw new UserAlreadyExistsException("User with this email already exists");
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public UserEntity updateUser(UserEntity user) {
        if(user == null) throw new IllegalArgumentException("User cannot be null");
        if(userRepository.existsByEmail(user.getEmail())) {
            return userRepository.updateUserProfile(user.getEmail(), user.getFirstName(), user.getLastName(), user.getCompanyName());
        } else {
            throw new UsernameNotFoundException("User with this email not found");
        }
    }

    @Transactional
    @Override
    public UserEntity updatePassword(String oldPassword, String newPassword, Authentication authentication) {
        if(oldPassword == null || newPassword == null || oldPassword.isEmpty() || newPassword.isEmpty())
            throw new IllegalArgumentException("Password cannot be null or empty");
        if(oldPassword.equals(newPassword))
            throw new IllegalArgumentException("You cannot change password to a similar one");
        if(authentication != null) {
            UserEntity user = userRepository.findUserByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User with this email not found"));
            if(passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
                String passwordHash = passwordEncoder.encode(newPassword);
                return userRepository.updateUserPassword(user.getEmail(), passwordHash);
            } else {
                throw new WrongPasswordException("Wrong password");
            }
        } else {
            throw new AuthenticationCredentialsNotFoundException("Authentication credentials not found");
        }
    }
}
