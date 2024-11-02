package com.project.ecommerce.repository;

import com.project.ecommerce.model.Categorys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Categorys, Long> {
}
