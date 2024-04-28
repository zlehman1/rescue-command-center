package org.rescue.command.center.authentication.security;

import org.rescue.command.center.authentication.model.User;
import org.rescue.command.center.authentication.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> userList = userRepository.findByUsername(username);

        User user;

        if (userList.isEmpty()) {
            return null;
        } else if (userList.size() > 1) {
            throw new IllegalStateException("More than one user found with username: " + username);
        } else {
            user = userList.get(0);
        }

        if (user != null) {
            UserDetails authUser = new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.toString()))
                            .collect(Collectors.toList()));

            return authUser;
        }
            else {
            throw new UsernameNotFoundException("Invalid Benutzername oder Passwort");
        }
    }
}
