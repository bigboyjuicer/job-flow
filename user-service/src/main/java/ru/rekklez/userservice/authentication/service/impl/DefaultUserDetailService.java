package ru.rekklez.userservice.authentication.service.impl;

import jakarta.transaction.Transactional;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.rekklez.userservice.user.entity.UserEntity;
import ru.rekklez.userservice.user.repository.UserRepository;
import ru.rekklez.userservice.authentication.AuthUser;

@Service
public class DefaultUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public DefaultUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @NullMarked
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return new AuthUser(user);
    }

}
