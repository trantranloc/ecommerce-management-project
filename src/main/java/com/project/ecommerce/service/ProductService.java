package com.project.ecommerce.service;

import com.project.ecommerce.model.*;
import com.project.ecommerce.model.enums.Status;
import com.project.ecommerce.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final UserRepository userRepository;

    public ProductService(CartRepository cartRepository, ProductRepository productRepository, CategoryRepository categoryRepository, OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.userRepository = userRepository;
    }

    // Lấy toàn bộ sản phẩm
    public List<Products> getAllProducts() {
        return productRepository.findAll();
    }

    // Lấy sản phẩm theo id sản phẩm
    public Products findProductById(Long id) {
        return productRepository.findProductById(id);
    }

    // Lưu sản phẩm
    public void saveProduct(Products product) {
        product.setCreateAt(new Date());
        productRepository.save(product);
    }

    // Sửa sản phẩm theo id và lưu lại file ảnh mới
    public void updateProduct(Long id, Products updateProduct, MultipartFile file) {
        // Tìm sản phẩm đó bằng id gán các giá trị
        Products product = productRepository.findProductById(id);
        product.setTitle(updateProduct.getTitle());
        product.setDescription(updateProduct.getDescription());
        product.setPrice(updateProduct.getPrice());
        product.setDiscount(updateProduct.getDiscount());
        product.setUpdateAt(new Date());
        // Nếu sản phẩm cũ có ảnh và không có file ảnh mới thì sản phẩm sửa sẽ gán ảnh cũ sản phẩm ban đầu
        if (product.getThumbnail() != null && file.isEmpty()) {
            updateProduct.setThumbnail(product.getThumbnail());
        }
        productRepository.save(updateProduct);
    }

    // Xóa sản phẩm theo id
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Lấy toàn bộ sản phẩm trong giỏ hàng
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    // Thêm sản phẩm vào giỏ hàng
    public void addToCart(Long productId) {
        // Tìm kiếm sản phẩm trong giỏ hàng dựa trên productId
        Products product = productRepository.findProductById(productId);
        Cart cart = cartRepository.findProductById(productId);

        if (cart != null && cart.getProducts() != null) {
            // Nếu sản phẩm đã tồn tại trong giỏ hàng, tăng số lượng lên 1
            cart.setQuantity(cart.getQuantity() + 1);
        } else {
            // Nếu sản phẩm chưa tồn tại trong giỏ hàng, tạo một giỏ hàng mới
            cart = new Cart();
            cart.setProducts(product);
            cart.setCreateAt(new Date());
            cart.setQuantity(1);
        }

        cartRepository.save(cart);
    }

    // Lấy toàn bộ danh mục sản phẩm
    public List<Categorys> getAllCategorys() {
        return categoryRepository.findAll();
    }

    // Xóa sản phẩm đó ra giỏ hàng
    public void deleteToCart(Long cartId) {
        Cart cart = cartRepository.findCartById(cartId);
        if (cart != null) {
            cartRepository.deleteById(cartId);
        }
    }

    // Xóa 1 sản phẩm ra giỏ hàng
    public void deleteOneCart(Long cartId) {
        // Tìm id của ô giỏ hàng bằng id nếu số lượng sản phẩm đó lơn hơn 1 thì khi ấn trừ đi 1,
        Cart cart = cartRepository.findCartById(cartId);
        if (cart.getQuantity() > 1) {
            cart.setQuantity(cart.getQuantity() - 1);
            cartRepository.save(cart);
        } else {
            // Nếu nhỏ hơn thì xóa sản phẩm đó khỏi giỏ hàng
            cartRepository.deleteById(cartId);
        }
    }

    // Lưu danh mục sản phẩm
    public void saveCategory(Categorys category) {
        categoryRepository.save(category);
    }

    public void placeOrder() {
        // Tạo đơn hàng mới
        Orders order = new Orders();
        order.setOrderDate(new Date()); // Đặt ngày đặt hàng
        order.setStatus(Status.PENDING); // Đặt trạng thái đơn hàng

        // Khởi tạo biến theo dõi tổng tiền và tổng số lượng
        double total = 0;
        int quantity = 0;
        // Lấy thông tin người dùng đã đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Kiểm tra nếu đối tượng là UserDetails để lấy thông tin user
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                String username = userDetails.getUsername();

                // Tìm kiếm user trong database theo username
                Users user = userRepository.findByUsername(username);
                // Gán user vào đơn hàng
                order.setUsers(user);
            }
        }
        // Tạo và lưu các đối tượng OrderDetails
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        List<Cart> carts = getAllCarts(); // Lấy danh sách các sản phẩm trong giỏ hàng
        for (Cart item : carts) {
            // Tạo một OrderDetails mới
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setProducts(item.getProducts()); // Gán sản phẩm
            orderDetails.setQuantity(item.getQuantity()); // Gán số lượng
            double money = item.getProducts().getPrice() * item.getQuantity(); // Tính tổng tiền
            orderDetails.setTotalMoney(money); // Gán tổng tiền
            orderDetails.setOrder(order); // Liên kết với Orders
            orderDetailsList.add(orderDetails); // Thêm vào danh sách

            // Cập nhật tổng tiền và tổng số lượng
            total += money;
            quantity += item.getQuantity();

//            Giảm số lượng sản phẩm trong kho
            Products products = item.getProducts();
            products.setQuantity(products.getQuantity() - item.getQuantity());
            productRepository.save(products);
        }

        // Gán tổng tiền và tổng số lượng cho Orders
        order.setTotalMoney(total);
        order.setQuantity(quantity);

        // Xóa sản phẩm trong giỏ hàng
        cartRepository.deleteAll();
        // Lưu Orders và OrderDetails
        orderRepository.save(order); // Lưu Orders
        orderDetailsRepository.saveAll(orderDetailsList); // Lưu OrderDetails
    }
}