package com.scibetta.service;

import com.scibetta.model.User;
import com.scibetta.repository.UserDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired UserDatabase userDatabase;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, LockedException {
        Optional<User> user = userDatabase.selectByUsername(username);
        if (user.isEmpty()) {
            System.out.println("(CustomUserDetailsService) Utente non trovato.");
            throw new UsernameNotFoundException("Utente non presente nel database.");
        }
        System.out.println("(CustomUserDetailsService) E-mail: " + user.get());
        return new CustomUserDetails(user.get());
    }

}
