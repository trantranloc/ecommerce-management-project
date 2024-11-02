package com.project.ecommerce.controller.admin;

import com.project.ecommerce.model.Categorys;
import com.project.ecommerce.model.Products;
import com.project.ecommerce.repository.ProductRepository;
import com.project.ecommerce.service.ProductService;
import com.project.ecommerce.util.FileUploadUtil;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;

    public AdminProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    //  Quản lý  Danh sách sản phẩn admin
    @GetMapping("/products")
    public String product(@RequestParam(required = false) Long categoryId, Model model) {
//        Lọc sản phẩm theo danh mục
        List<Products> products;
        if (categoryId != null) {
//            nếu chọn danh mục thì hiển thị sản phẩm chứa mã danh mục đó
            products = productRepository.findProductsByCategoryId(categoryId);
        }else{
//            nếu không chọn danh mục thì hiển thị toàn bộ sản phẩm
            products = productService.getAllProducts();
        }
        List<Categorys> categoryAll = productService.getAllCategorys();
        model.addAttribute("products", products);
        model.addAttribute("categoryAll", categoryAll);
        model.addAttribute("selectCategory", categoryId);
        model.addAttribute("add_product", new Products());
        return "/admin/page/product";
    }
    //   Quản lý Form sửa thông tin người dùng admin
    @GetMapping("/update-product/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Products update_product = productService.findProductById(id);
        List<Categorys> categoryAll = productService.getAllCategorys();
        model.addAttribute("categoryAll", categoryAll);
        model.addAttribute("update_product", update_product);
        return "/admin/page/edit_product";
    }
    //   Quản lý Danh mục sản phẩm admin
    @GetMapping("/categorys")
    public String category(Model model ) {
        List<Categorys> categorys = productService.getAllCategorys();
        model.addAttribute("categorys", categorys);
        model.addAttribute("add_category", new Categorys());
        return "/admin/page/category";
    }

    //    Lưu sản phẩm
    @PostMapping("/save-product")
    public String saveProduct(@Valid @ModelAttribute("products") Products product,
                              @RequestParam(value = "fileImage", required = false) MultipartFile file) throws IOException {
//        Nếu ành khác null và ảnh không trống thì set file là ảnh , gắn thêm vị trí lưu file
        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            product.setThumbnail(fileName);
            // Lưu file
            String uploadDir = "src/main/resources/static/uploads/";
            FileUploadUtil.saveFile(uploadDir, fileName, file);
        }
        productService.saveProduct(product);
        return "redirect:/admin/products";
    }
//    Xóa sản phẩm theo id
    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
//    Update lại sản phẩm theo id
    @PostMapping("/update-product/{id}")
    public Object updateUser(@PathVariable("id") Long id , @ModelAttribute("update_user") Products update_product, @RequestParam(value = "fileImage") MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            update_product.setThumbnail(fileName);
            // Lưu file
            String uploadDir = "src/main/resources/static/uploads/";
            FileUploadUtil.saveFile(uploadDir, fileName, file);
//            Nếu thay ảnh mới thì xóa ảnh sản phẩm cũ
            if (update_product.getThumbnail() != null) {
                FileUploadUtil.deleteFile(uploadDir + update_product.getThumbnail());
            }
        }
        productService.updateProduct(id,update_product,file);
        return "redirect:/admin/products";
    }
//    Lưu dảnh mục sản phẩm
    @PostMapping("/save-category")
    public String saveCategory(Categorys categorys) {
        productService.saveCategory(categorys);
        return "redirect:/admin/categorys";
    }

}
