package com.project.ecommerce.controller.client;

import com.project.ecommerce.model.*;
import com.project.ecommerce.model.enums.Status;
import com.project.ecommerce.repository.ProductRepository;
import com.project.ecommerce.service.admin.MyUserDetails;
import com.project.ecommerce.service.OtherService;
import com.project.ecommerce.service.ProductService;
import com.project.ecommerce.service.admin.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/")
public class ClientController {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final OtherService otherService;

    public ClientController(ProductService productService, ProductRepository productRepository, UserService userService, OtherService otherService) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.userService = userService;
        this.otherService = otherService;
    }

    //    Số sản phẩm trong giỏ hàng
    @ModelAttribute("quantity")
    public int quantity() {
        List<Cart> cart = productService.getAllCarts();
        int quantity = 0 ;
        for (Cart item : cart){
            quantity += item.getQuantity();
        }
        return quantity;
    }
    public double calculatePrice(Long id) {
        Products products = productService.findProductById(id);
        if (id == null) {
            return 0; // hoặc giá trị mặc định khác
        }
        if (products != null){
            return products.getPrice()-(products.getPrice()*(products.getDiscount()/100));
        }else {
            return 0;
        }
    }
//    Trang layout

//    Đăng nhập
    @GetMapping("/login")
    public String login() {
        return "public/login";
    }
//    Hiện thị lỗi
    @GetMapping("/error")
    public String error() {
        return "public/error";
    }
//    @GetMapping("")
////    Trang chủ web
//    public String index(){
//        return "/public/pages/index";
//    }
//    Danh sách sản phẩm
    @GetMapping("/products")
    public String product(@RequestParam(required = false) Long categoryId, Model model) {
        List<Products> products;
//        Lọc sản phẩm theo Category
        if (categoryId != null) {
            products = productRepository.findProductsByCategoryId(categoryId);
        }else{
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);
        return "/public/pages/product";
    }
     @GetMapping("/product-details/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
        Products products = productService.findProductById(id);
         model.addAttribute("price", calculatePrice(id));
         model.addAttribute("products", products);
        return "/public/pages/product_details";
    }

//    Thêm vào giỏ hàng
    @GetMapping("/add-cart/{id}")
    public String addToCart(@PathVariable Long id) {
        productService.addToCart( id);
        return "redirect:/products";
    }
//    Thêm 1 sản phẩm
    @GetMapping("/add-o-cart/{id}")
    public String addOneCart(@PathVariable Long id) {
        productService.addToCart( id);
        return "redirect:/carts";
    }

//    Xóa khỏi giỏ hàng
    @GetMapping("/delete-cart/{id}")
    public String deleteToCart(@PathVariable Long id) {
        productService.deleteToCart( id);
        return "redirect:/carts";
    }
//    Xóa 1 sản phẩm
    @GetMapping("/delete-o-cart/{id}")
    public String deleteOneCart(@PathVariable("id") Long id) {
        productService.deleteOneCart(id);
        return "redirect:/carts";
    }
//    Giỏ hàng
    @GetMapping("/carts")
    public String cart(Model model) {
        calculateCartTotal(model);
        return "/public/pages/cart";
    }
//    Đơn hàng
    @GetMapping("/orders")
    public String order(Model model) {
        calculateCartTotal(model);
        return "/public/pages/order";
    }
//    Định dạng tiền
private String formatPrice(double price) {
    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    return formatter.format(price);
}
    private void calculateCartTotal(Model model) {
        List<Cart> cart = productService.getAllCarts();
        double total = 0;
        int quantity = 0;
        for (Cart item : cart) {
            double money = item.getProducts().getPrice() * item.getQuantity();
            quantity += item.getQuantity();
            total += money;
        }
        model.addAttribute("total", formatPrice(total));
        model.addAttribute("quantity", quantity);
        model.addAttribute("carts", cart);
    }
//    ĐẶT HÀNG
    @GetMapping("/order")
    private String order(){
        Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            productService.placeOrder();
            return "redirect:/products";
        } else {
            return "redirect:/login";
        }
    }
//    Đơn hàng cửa tôi
@GetMapping("/my-orders")
public String myOrder(Model model, @AuthenticationPrincipal MyUserDetails currentUser) {
    List<Orders> orders = otherService.getOrdersByUser(currentUser.getUsers());
    model.addAttribute("orders", orders);
    return "/public/pages/my_order";
}
//    Chí tiết đơn hàng
    @GetMapping("/orders-detail/{id}")
    private String orderDetails(@PathVariable Long id,Model model, Authentication authentication){
            Orders orders = otherService.getOrderById(id);
            // Kiểm tra xem đơn hàng có thuộc về người dùng hiện tại không
            if (orders != null && orders.getUsers().getUsername().equals(authentication.getName())) {
                List<OrderDetails> orderDetails = otherService.findByOrder(orders);
                model.addAttribute("orders", orders);
                model.addAttribute("orderDetails", orderDetails);
                model.addAttribute("status", Status.values());
            }else {
                return "redirect:/login";
        }
        return "/public/pages/order_detail";
    }
//    người dùng hùy đơn hàng
    @PostMapping("/orders/cancel/{id}")
    public String cancel(@PathVariable Long id){
        otherService.cancelOrder(id);
        return "redirect:/my-orders";
    }
//    Thông tin người dùng
    @GetMapping("/profile")
    public String Profile(Model model){
        Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users users = userService.getUserByUsername(username);
        model.addAttribute("users", users);
        return "/public/pages/profile";
    }
}
