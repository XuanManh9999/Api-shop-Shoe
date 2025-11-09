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

    @Autowired
    private EmailService emailService;

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

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Email not found in the system"));

        // Tạo mật khẩu mới ngẫu nhiên (8-12 ký tự)
        String newPassword = generateRandomPassword();
        
        // Mã hóa và lưu mật khẩu mới vào database
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Gửi mật khẩu mới (chưa mã hóa) về email
        emailService.sendPasswordResetEmail(user.getEmail(), newPassword);
    }

    private String generateRandomPassword() {
        // Tạo mật khẩu ngẫu nhiên 10 ký tự: chữ hoa, chữ thường, số
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String allChars = upperCase + lowerCase + numbers;
        
        StringBuilder password = new StringBuilder();
        java.util.Random random = new java.util.Random();
        
        // Đảm bảo có ít nhất 1 chữ hoa, 1 chữ thường, 1 số
        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));
        
        // Thêm các ký tự ngẫu nhiên còn lại
        for (int i = 3; i < 10; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        // Xáo trộn các ký tự
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        
        return new String(passwordArray);
    }
}

