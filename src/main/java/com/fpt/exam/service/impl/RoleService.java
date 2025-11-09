package com.fpt.exam.service.impl;

import com.fpt.exam.entity.Role;
import com.fpt.exam.exception.ResourceNotFoundException;
import com.fpt.exam.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    // Lấy danh sách role có phân trang và tìm kiếm
    public Page<Role> getAllRoles(String keyword, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            return roleRepository.searchRoles(keyword, pageable);
        }
        return roleRepository.findAll(pageable);
    }

    // Lấy role theo id
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
    }

    // Tạo role mới
    public Role createRole(Role role) {
        // Kiểm tra roleName không null và không rỗng
        if (role.getRoleName() == null || role.getRoleName().trim().isEmpty()) {
            throw new RuntimeException("Role name is required and cannot be empty");
        }
        
        // Kiểm tra trùng tên
        if (roleRepository.existsByRoleName(role.getRoleName().trim())) {
            throw new RuntimeException("Role name already exists");
        }
        
        // Trim và set lại roleName
        role.setRoleName(role.getRoleName().trim());
        return roleRepository.save(role);
    }

    // Cập nhật role
    public Role updateRole(Long id, Role newRole) {
        Role role = getRoleById(id);
        
        // Kiểm tra roleName không null và không rỗng
        if (newRole.getRoleName() == null || newRole.getRoleName().trim().isEmpty()) {
            throw new RuntimeException("Role name is required and cannot be empty");
        }
        
        String trimmedRoleName = newRole.getRoleName().trim();
        
        // Kiểm tra trùng tên (chỉ khi tên khác với tên hiện tại)
        if (!role.getRoleName().equals(trimmedRoleName) && 
            roleRepository.existsByRoleName(trimmedRoleName)) {
            throw new RuntimeException("Role name already exists");
        }
        
        role.setRoleName(trimmedRoleName);
        return roleRepository.save(role);
    }

    // Xóa role
    public void deleteRole(Long id) {
        Role role = getRoleById(id);
        roleRepository.delete(role);
    }

    // Tìm role theo tên
    public Optional<Role> getRoleByName(String name) {
        return roleRepository.findByRoleName(name);
    }
}
