package com.project.ecommerce.controller.admin;

import com.project.ecommerce.model.OrderDetails;
import com.project.ecommerce.model.Orders;
import com.project.ecommerce.model.enums.Status;
import com.project.ecommerce.service.OtherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//@RestController
@Controller
@RequestMapping("/admin")
public class AdminOrderController {

    private final OtherService otherService;

    public AdminOrderController(OtherService otherService) {
        this.otherService = otherService;
    }

    //    Quản lý đơn hàng
    @GetMapping("/orders")
    public String order(Model model) {
        List<Orders> orders = otherService.getAllOrder();
        orders.sort((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()) );
        model.addAttribute("orders", orders);
        return "/admin/page/order";
    }
    //    Chi tiết đơn hànd
    @GetMapping("/order-details/{id}")
    public String orderDetails(@PathVariable("id")Long id, Model model) {
        Orders orders = otherService.getOrderById(id);
        List<OrderDetails> orderDetails = orders.getOrderDetails();
        model.addAttribute("orders", orders);
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("status", Status.values());
        return "/admin/page/order_detail";
    }

    //    Update trạng thái  chi tiết đơn hàng
    @PostMapping("/update-order-details/{id}")
    public String updateOrderDetails(@PathVariable("id") Long id, @RequestParam("newStatus") Status newStatus) {
        otherService.updateOrderDetails(id, newStatus);
        return "redirect:/admin/orders";
    }

}
