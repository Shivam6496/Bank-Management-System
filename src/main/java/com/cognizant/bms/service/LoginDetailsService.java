package com.cognizant.bms.service;

import com.cognizant.bms.entity.User;
import com.cognizant.bms.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class LoginDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private HttpSession session;


    // Load user from DB and convert to Spring Security User
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null || !user.isEnabled()) {
            throw new UsernameNotFoundException("User not found or disabled");
        }
        // Set username in session
        session.setAttribute("username", user.getUsername());
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}
