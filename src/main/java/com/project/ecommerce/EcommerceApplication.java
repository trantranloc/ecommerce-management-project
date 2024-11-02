package com.project.ecommerce;

import com.project.ecommerce.model.Roles;
import com.project.ecommerce.model.Users;
import com.project.ecommerce.repository.RoleRepository;
import com.project.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@SpringBootApplication
public class EcommerceApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public EcommerceApplication(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

    @Transactional
    @Override
    public void run(String... args) {
        Users admin = userRepository.findByUsername("admin");
        Users user = userRepository.findByUsername("user");

        // Create admin user if it doesn't exist
        if (admin == null) {
            admin = new Users();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@email.com");
            admin.setEnable(true);
            admin.setCreateAt(new Date());
            Roles adminRole = roleRepository.findByName("ADMIN");
            if (adminRole == null) {
                adminRole = new Roles();
                adminRole.setName("ADMIN");
                roleRepository.save(adminRole);
            }
            admin.setRoles(Collections.singleton(adminRole));
            userRepository.save(admin);
            System.out.println("Admin created "+ admin);
        } else {
            System.out.println("Admin already exists.");
        }

        // Phần code cho user thông thường (nếu cần)
        if (user == null) {
            user = new Users();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setEmail("user@email.com");
            user.setEnable(true);
            user.setCreateAt(new Date());
            Roles userRole = roleRepository.findByName("USER");
            if (userRole == null) {
                userRole = new Roles();
                userRole.setName("USER");
                roleRepository.save(userRole);
            }
            user.setRoles(Collections.singleton(userRole));
            userRepository.save(user);
        }else{
            System.out.println("User already exists.");
        }
    }}
