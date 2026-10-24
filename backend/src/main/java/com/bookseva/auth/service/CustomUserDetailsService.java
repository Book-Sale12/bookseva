package com.bookseva.auth.service;
import com.bookseva.user.entity.UserStatus;


import com.bookseva.user.entity.User;
import com.bookseva.user.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        boolean isActive = user.getStatus() == com.bookseva.user.entity.UserStatus.ACTIVE;
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(), isActive,  // enabled
        true,  // accountNonExpired
        true,  // credentialsNonExpired
        isActive,  // accountNonLocked
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
    }

    public CustomUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
