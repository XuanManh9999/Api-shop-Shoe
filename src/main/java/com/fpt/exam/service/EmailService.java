package com.fpt.exam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${app.mail.from:no-reply@hyperstep.com}")
    private String fromEmail;

    @Value("${app.mail.subject:HyperStep - Mat khau moi cua ban}")
    private String subjectTemplate;

    public void sendPasswordResetEmail(String toEmail, String newPassword) {
        if (mailSender == null) {
            System.out.println("⚠️  Mail sender not configured. Email would be sent to: " + toEmail);
            System.out.println("    New Password: " + newPassword);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    true, // multipart = true
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            // Set subject với tiếng Việt và encode UTF-8
            String subject = "HyperStep - Mật khẩu mới của bạn";
            try {
                // Encode subject với Base64 encoding để hiển thị đúng tiếng Việt
                message.setSubject(MimeUtility.encodeText(subject, StandardCharsets.UTF_8.name(), "B"));
            } catch (Exception e) {
                // Fallback: set trực tiếp
                message.setSubject(subject, StandardCharsets.UTF_8.name());
            }
            helper.setText(buildEmailContent(newPassword), false); // false = plain text
            
            mailSender.send(message);
            System.out.println("✅ Password reset email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    private String buildEmailContent(String newPassword) {
        return "Xin chào,\n\n" +
               "Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản HyperStep.\n\n" +
               "Mật khẩu mới của bạn là: " + newPassword + "\n\n" +
               "Vui lòng đăng nhập và đổi mật khẩu ngay sau khi nhận được email này để đảm bảo an toàn.\n\n" +
               "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.\n\n" +
               "Trân trọng,\n" +
               "Đội ngũ HyperStep";
    }
}

