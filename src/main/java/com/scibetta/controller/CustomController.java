package com.scibetta.controller;

import com.scibetta.repository.UserDatabase;
import com.scibetta.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class CustomController {

    private final UserDatabase userDatabase;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public CustomController(UserDatabase userDatabase, PasswordEncoder passwordEncoder) {
        this.userDatabase = userDatabase;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null  && auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ANONYMOUS"))) {
            return "redirect:/profile";
        }
        return "login";
    }

    @GetMapping("/logout")
    String logout() {
        return "redirect:/";
    }

    @GetMapping("/profile")
    String profile() {
        return "profile";
    }

    @PostMapping("/profile/change-password")
    public String profileChangePassword(String newPassword, Authentication authentication, Model model) {
        String username = authentication.getName();
        try {
            User user = userDatabase.selectByUsername(username).get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userDatabase.updateById(user.getId(), user);
            model.addAttribute("res", "Cambio password avvenuto con successo");
        } catch (NoSuchElementException e) {
            model.addAttribute("res", "Errore interno nel cambio password");
        }
        return "profile";
    }

    @PostMapping("/register/execute")
    public String registerExecute(@ModelAttribute("user") User user) {
        Optional<User> possibleUser = userDatabase.selectByUsername(user.getUsername());
        if (possibleUser.isPresent()) {
            System.out.println("(CustomController) Errore: indirizzo e-mail già utilizzato.");
            return "redirect:/register?error";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("user");
        userDatabase.insert(user);
        System.out.println("(CustomController) " + user);
        return "redirect:/login";
    }

}