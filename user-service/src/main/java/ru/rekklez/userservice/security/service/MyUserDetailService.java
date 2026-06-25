package ru.rekklez.userservice.security.service;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.rekklez.userservice.entity.User;
import ru.rekklez.userservice.repository.UserRepository;
import ru.rekklez.userservice.security.user.SecurityUser;

@Service
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return new SecurityUser(user);
    }

}
