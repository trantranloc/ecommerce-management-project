package com.project.ecommerce.controller.admin;

import com.project.ecommerce.model.Roles;
import com.project.ecommerce.model.Users;
import com.project.ecommerce.service.admin.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller

@RequestMapping("/admin")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    //  Quản lý  Danh sách Người dùng
    @GetMapping("/users")
    public String user(Model model) {
        List<Users> users = userService.getAllUsers();
        List<Roles> roles = userService.getAllRole();
        model.addAttribute("roles", roles);
        model.addAttribute("users", users);
        model.addAttribute("add_user", new Users());
        return "/admin/page/user";
    }
    //  Quản lý  Form sửa thông tin người dùng
    @GetMapping("/edit-user/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        Users update_user = userService.getUserById(id);
        List<Roles> roles = userService.getAllRole();
        model.addAttribute("roles", roles);
        model.addAttribute("update_user", update_user);
        return "/admin/page/edit_user";
    }
//    Lưu người dùng
    @PostMapping("/save-user")
    public String saveUser(Users users) {
        userService.saveUser(users);
        return "redirect:/admin/users";
    }
//    Update người dùng theo id
    @PostMapping("/update-user/{id}")
    public Object updateUser(@PathVariable("id") Long id , @ModelAttribute("update_user") Users update_user) {
        userService.updateUser(id,update_user);
        return "redirect:/admin/users";
    }
//    Xóa người dùng theo id
    @GetMapping("/delete-user/{id}")
    public  String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
