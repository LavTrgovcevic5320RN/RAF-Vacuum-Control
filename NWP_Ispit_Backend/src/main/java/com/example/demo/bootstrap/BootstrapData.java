package com.example.demo.bootstrap;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Component
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BootstrapData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Loading Data...");

        User user = new User();

        user.setFirstName("Lav");
        user.setLastName("Trgovcevic");
        user.setEmail("admin@gmail.com");
        user.setPassword(passwordEncoder.encode("admin"));
        Set<String> perms = new HashSet<>();
        perms.add("can_read_users");
        perms.add("can_update_users");
        perms.add("can_create_users");
        perms.add("can_delete_users");
        perms.add("can_remove_vacuums");
        perms.add("can_start_vacuums");
        perms.add("can_add_vacuums");
        perms.add("can_discharge_vacuums");
        perms.add("can_search_vacuums");
        perms.add("can_stop_vacuums");

        user.setPermissions(perms);
        this.userRepository.save(user);

        System.out.println("Data loaded!");
    }
}
