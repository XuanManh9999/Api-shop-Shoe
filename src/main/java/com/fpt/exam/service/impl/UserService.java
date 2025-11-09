package com.fpt.exam.service.impl;

import com.fpt.exam.entity.User;
import com.fpt.exam.exception.ResourceNotFoundException;
import com.fpt.exam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Lấy danh sách user có phân trang và tìm kiếm
    public Page<User> getAllUsers(String keyword, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            return userRepository.searchUsers(keyword, pageable);
        }
        return userRepository.findAll(pageable);
    }

    // Lấy user theo id
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    // Thêm mới user
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        // Mã hóa password trước khi lưu
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    // Cập nhật user
    public User updateUser(Long id, User newUser) {
        User user = getUserById(id);
        user.setFullName(newUser.getFullName());
        if (!user.getEmail().equals(newUser.getEmail()) && userRepository.existsByEmail(newUser.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        user.setEmail(newUser.getEmail());
        user.setAddress(newUser.getAddress());
        user.setPhone(newUser.getPhone());
        if (newUser.getStatus() != null) {
            user.setStatus(newUser.getStatus());
        }
        if (newUser.getRole() != null) {
            user.setRole(newUser.getRole());
        }
        // Chỉ mã hóa password nếu user gửi password mới (không null và không rỗng)
        if (newUser.getPassword() != null && !newUser.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }
        return userRepository.save(user);
    }

    // Xóa user
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    // Tìm theo email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

