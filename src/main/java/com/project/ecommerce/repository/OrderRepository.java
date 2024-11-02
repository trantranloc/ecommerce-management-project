package com.project.ecommerce.repository;

import com.project.ecommerce.model.OrderDetails;
import com.project.ecommerce.model.Orders;
import com.project.ecommerce.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUsers(Users users);
    Orders getOrderById(long id);

}
