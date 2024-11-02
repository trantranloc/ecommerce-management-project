package com.project.ecommerce.controller.admin;

import com.project.ecommerce.model.*;
import com.project.ecommerce.service.OtherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final OtherService otherService;

    public AdminDashboardController(OtherService otherService) {
        this.otherService = otherService;}

    //  Quản lý  Trang chủ
    @GetMapping("/")
    public String index() {

        return "/admin/page/index";
    }
    @GetMapping("/customers")
    public String customer() {
        return "/admin/page/customer";
    }
    @GetMapping("/over-view")
    public String overview() {
        return "/admin/page/overview";
    }
//    Quản lý khuyến mại
    @GetMapping("/sales")
    public String sale(Model model) {
        List<Sales> sales = otherService.getAllSales();
        model.addAttribute("sales", sales);
        return "/admin/page/sale";
    }
    @GetMapping("/reviews")
    public String reviews() {
        return "/admin/page/reviews";
    }
    @GetMapping("/inventorys")
    public String inventory() {
        return "/admin/page/inventory";
    }
    @GetMapping("/blogs")
    public String blog() {
        return "/admin/page/blog";
    }

}
