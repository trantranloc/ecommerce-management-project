package com.project.ecommerce.service.admin;

import com.project.ecommerce.model.Roles;
import com.project.ecommerce.model.Users;
import com.project.ecommerce.repository.RoleRepository;
import com.project.ecommerce.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //    Lấy toàn bộ người dùng
    public List<Users> getAllUsers() {
        System.out.println(userRepository.findAll());
        return userRepository.findAll();
    }
//    Xóa người dùng theo id
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
//    Lưu người dùng
    public void saveUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateAt(new Date());
        user.setRoles(user.getRoles());
        userRepository.save(user);
    }
//    Lấy người dùng theo id
    public Users getUserById(Long id) {
        return userRepository.findUserById(id);
    }
//    Lấy toàn bộ cá quyền
    public List<Roles> getAllRole(){
        return roleRepository.findAll();
    }
//    Sửa ngươi dùng theo id
    public void updateUser(Long id, Users updateUser) {
//        tìm kiêm người dùndos bằng id và gán các giá trị
        Users existsUser = userRepository.findUserById(id);
        if (existsUser == null) {
            throw new RuntimeException("User not found");
        }

        existsUser.setUsername(updateUser.getUsername());
        existsUser.setEmail(updateUser.getEmail());
        existsUser.setAddress(updateUser.getAddress());

//        nếu sửa lại password thì gán password đó bằng chĩnh password điền vào khi mã hóa
        if (updateUser.getPassword() != null) {
            updateUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        } else {
//            ngược lại thì gán password cũ
            existsUser.setPassword(existsUser.getPassword());
        }
//        nếu ngày tạo không có thì gán ngày tạo đó bằng hiện tại
        if (existsUser.getCreateAt() == null) {
            updateUser.setCreateAt(new Date());
        } else {
//            nếu có thì giữ nguyên ngày tạo
            updateUser.setCreateAt(existsUser.getCreateAt());
        }
//        gán ngày sừa bằng ngày hiện tại
        updateUser.setUpdateAt(new Date());
        existsUser.setRoles(updateUser.getRoles());
        userRepository.save(updateUser);
    }

    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
