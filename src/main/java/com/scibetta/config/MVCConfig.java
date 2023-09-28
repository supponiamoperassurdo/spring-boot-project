package com.scibetta.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error").setViewName("error");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/balance").setViewName("balance");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/profile").setViewName("profile");
        registry.addViewController("/profile/user-transactions").setViewName("user-transactions");
        registry.addViewController("/profile/seller").setViewName("seller");
        registry.addViewController("/profile/seller/seller-transactions").setViewName("seller-transactions");
        registry.addViewController("/profile/admin").setViewName("admin");
        registry.addViewController("/profile/admin/manage-creditcards").setViewName("manage-creditcards");
        registry.addViewController("/about-us").setViewName("about-us");
    }
}
