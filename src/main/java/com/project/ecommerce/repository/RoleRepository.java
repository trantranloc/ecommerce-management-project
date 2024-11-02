package com.project.ecommerce.repository;

import com.project.ecommerce.model.Roles;
import com.project.ecommerce.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    Roles findByName(String roleName);
    Roles findById(int id);


}
