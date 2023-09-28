package com.scibetta.config;

import com.scibetta.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    CustomUserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        String[] permittedUrls = new String[]{
            "/",
            "/index",
            "/logout",
            "/register",
            "/register/execute",
            "/balance",
            "/balance/check",
            "/about-us",
            "/error",
            "/static/**",
            "/static/assets/**",
            "/static/css/**",
            "/static/js/**",
            "/favicon.ico"
        };
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((authorize) -> authorize
                    .requestMatchers(permittedUrls)
                    .permitAll()
                    .requestMatchers(
                        "/profile/admin",
                        "/profile/admin/manage-creditcards",
                        "/profile/admin/manage-creditcards/manage-status",
                        "/profile/admin/add-creditcard",
                        "/profile/admin/add-seller",
                        "/profile/admin/remove-seller"
                    ).hasRole("admin")
                    .requestMatchers(
                        "/profile/seller",
                        "/profile/seller/generate-report",
                        "/profile/seller/seller-transactions",
                        "/profile/seller/assign-creditcard"
                    ).hasRole("seller")
                    .requestMatchers(
                        "/profile/user-transactions"
                    ).hasRole("user")
                    .requestMatchers(
                            "/profile",
                            "/profile/change-password"
                    ).hasAnyRole("user", "seller", "admin")
                    .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/profile")
                    .permitAll()
            )
            .logout((logout) -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/index")
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
            );
        return http.build();

    }

}