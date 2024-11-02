package com.project.ecommerce.repository;

import com.project.ecommerce.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    Users findByUsername(String username);
    Optional<Users> findById(Long id);
    Users findUserById(Long id);
}
