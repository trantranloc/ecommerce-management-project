package com.project.ecommerce.service;

import com.project.ecommerce.model.OrderDetails;
import com.project.ecommerce.model.Orders;
import com.project.ecommerce.model.Sales;
import com.project.ecommerce.model.Users;
import com.project.ecommerce.model.enums.Status;
import com.project.ecommerce.repository.OrderDetailsRepository;
import com.project.ecommerce.repository.OrderRepository;
import com.project.ecommerce.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OtherService {
    private final SaleRepository saleRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    public OtherService(SaleRepository saleRepository, OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository) {
        this.saleRepository = saleRepository;
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
    }

    //    Danh sach khuyến mại giảm giá
    public List<Sales> getAllSales(){
        return saleRepository.findAll();
    }
//    Danh sách đơn hàng
    public List<Orders> getAllOrder(){
        return orderRepository.findAll();
}
    public Orders getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
    //    Update trạng thái  chi tiết đơn hàng
    public void updateOrderDetails(Long id, Status status) {
        Orders orders = orderRepository.getOrderById(id);
        if (orders != null) {
            orders.setStatus(status);
            orderRepository.save(orders);
        } else {
            throw new RuntimeException("Không tìm thấy đơn hàng với ID: " + id);
        }
    }
//    được sử dụng để lấy tất cả đơn hàng của một người dùng cụ thể
    public List<Orders> getOrdersByUser(Users users) {
        return orderRepository.findByUsers(users);
    }
//    Lấy chi tiết đơn hàng
    public List<OrderDetails> findByOrder(Orders order){
        return orderDetailsRepository.findByOrder(order);
    }


//    Hủy đơn hàng bởi người dùng
    public void cancelOrder(Long id) {
    Orders orders = orderRepository.getOrderById(id);
    orders.setStatus(Status.CANCELLED);
    orderRepository.save(orders);
    }

}
