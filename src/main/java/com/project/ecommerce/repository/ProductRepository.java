package com.project.ecommerce.repository;

import com.project.ecommerce.model.Categorys;
import com.project.ecommerce.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {
    @Query("SELECT p FROM Products p WHERE p.categorys.id = :categoryId")
    List<Products> findProductsByCategoryId(@Param("categoryId") Long categoryId);

    Products findProductById(Long id);
//    tình tông số hàng của sản phảm có cùng danh mục
//    @Query("SELECT SUM(p.quantity) FROM Products p WHERE p.categorys = :category")
//    long getTotalQuantityByCategory(@Param("category") Categorys category);
    //    Tính số sản phẩm có chung danh mục
    long countByCategorys(Categorys categorys);
}
