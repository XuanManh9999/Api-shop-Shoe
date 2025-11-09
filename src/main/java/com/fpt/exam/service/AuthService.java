package com.fpt.exam.service;

import com.fpt.exam.dto.AuthResponse;
import com.fpt.exam.dto.LoginRequest;
import com.fpt.exam.dto.RegisterRequest;
import com.fpt.exam.entity.Role;
import com.fpt.exam.entity.User;
import com.fpt.exam.exception.BadRequestException;
import com.fpt.exam.exception.UnauthorizedException;
import com.fpt.exam.repository.RoleRepository;
import com.fpt.exam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setStatus(true);

        // Set default role as USER if not provided
        Role defaultRole = roleRepository.findByRoleName("USER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setRoleName("USER");
                    return roleRepository.save(role);
                });
        user.setRole(defaultRole);

        User savedUser = userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        String token = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new AuthResponse(
                token,
                refreshToken,
                "Bearer",
                savedUser.getUserId(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole() != null ? savedUser.getRole().getRoleName() : "USER"
        );
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (user.getStatus() == null || !user.getStatus()) {
            throw new UnauthorizedException("Account is disabled");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new AuthResponse(
                token,
                refreshToken,
                "Bearer",
                user.getUserId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole() != null ? user.getRole().getRoleName() : "USER"
        );
    }

    public AuthResponse refreshToken(String refreshToken) {
        try {
            String email = jwtService.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtService.validateToken(refreshToken, userDetails)) {
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new UnauthorizedException("User not found"));

                String newToken = jwtService.generateToken(userDetails);
                String newRefreshToken = jwtService.generateRefreshToken(userDetails);

                return new AuthResponse(
                        newToken,
                        newRefreshToken,
                        "Bearer",
                        user.getUserId(),
                        user.getEmail(),
                        user.getFullName(),
                        user.getRole() != null ? user.getRole().getRoleName() : "USER"
                );
            } else {
                throw new UnauthorizedException("Invalid refresh token");
            }
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid refresh token");
        }
    }
}

