package com.fpt.exam.repository;

import com.fpt.exam.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
    boolean existsByRoleName(String roleName);
    
    @Query("SELECT r FROM Role r WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(r.roleName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Role> searchRoles(@Param("keyword") String keyword, Pageable pageable);
}

