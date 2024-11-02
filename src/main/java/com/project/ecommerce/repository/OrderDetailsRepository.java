package com.project.ecommerce.repository;

import com.project.ecommerce.model.OrderDetails;
import com.project.ecommerce.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    //    hiển thị chi tiết sản phẩm theo cùng 1 order
    @Query("Select od " +
            "From OrderDetails od " +
            "INNER JOIN Orders o on od.order.id = o.id " +
            "where o.id = :orderId")
    public List<OrderDetails> findByOrderId(@Param("orderId") Long orderId);
    List<OrderDetails> findByOrder(Orders order);

    OrderDetails getOrderDetailsById(long id);

}
