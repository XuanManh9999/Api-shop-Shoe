package com.fpt.exam.config;

import com.fpt.exam.entity.Category;
import com.fpt.exam.entity.Product;
import com.fpt.exam.entity.Role;
import com.fpt.exam.entity.User;
import com.fpt.exam.repository.CategoryRepository;
import com.fpt.exam.repository.ProductRepository;
import com.fpt.exam.repository.RoleRepository;
import com.fpt.exam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🌱 Starting data seeding...");
        
        // Seed roles nếu chưa có
        if (roleRepository.count() == 0) {
            seedRoles();
        } else {
            System.out.println("ℹ️  Roles already exist. Skipping role seed.");
        }

        // Seed users nếu chưa có
        if (userRepository.count() == 0) {
            seedUsers();
        } else {
            System.out.println("ℹ️  Users already exist. Skipping user seed.");
        }

        // Seed categories nếu chưa có
        if (categoryRepository.count() == 0) {
            seedCategories();
        } else {
            System.out.println("ℹ️  Categories already exist. Skipping category seed.");
        }

        // Seed products nếu chưa có
        if (productRepository.count() == 0) {
            seedProducts();
        } else {
            System.out.println("ℹ️  Products already exist. Skipping product seed.");
        }

        System.out.println("✅ Data seeding completed!");
    }

    private void seedRoles() {
        System.out.println("🌱 Seeding Roles...");
        
        Role adminRole = new Role();
        adminRole.setRoleName("ADMIN");
        roleRepository.save(adminRole);
        System.out.println("   ✓ Created ADMIN role");

        Role userRole = new Role();
        userRole.setRoleName("USER");
        roleRepository.save(userRole);
        System.out.println("   ✓ Created USER role");
    }

    private void seedUsers() {
        System.out.println("🌱 Seeding Users...");

        Role adminRole = roleRepository.findByRoleName("ADMIN").orElseThrow();
        Role userRole = roleRepository.findByRoleName("USER").orElseThrow();

        // Admin User
        if (!userRepository.existsByEmail("admin@hyperstep.com")) {
            User admin = new User();
            admin.setFullName("Admin User");
            admin.setEmail("admin@hyperstep.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setPhone("0123456789");
            admin.setAddress("123 Admin Street, Ho Chi Minh City");
            admin.setRole(adminRole);
            admin.setStatus(true);
            admin.setCreatedAt(new Date());
            userRepository.save(admin);
            System.out.println("   ✓ Created Admin user (admin@hyperstep.com / admin123)");
        }

        // Client User
        if (!userRepository.existsByEmail("client@hyperstep.com")) {
            User client = new User();
            client.setFullName("Client User");
            client.setEmail("client@hyperstep.com");
            client.setPassword(passwordEncoder.encode("client123"));
            client.setPhone("0987654321");
            client.setAddress("456 Client Avenue, Ho Chi Minh City");
            client.setRole(userRole);
            client.setStatus(true);
            client.setCreatedAt(new Date());
            userRepository.save(client);
            System.out.println("   ✓ Created Client user (client@hyperstep.com / client123)");
        }
    }

    private void seedCategories() {
        System.out.println("🌱 Seeding Categories...");

        // Giày thể thao
        if (!categoryRepository.existsByName("Giày Thể Thao")) {
            Category category1 = new Category();
            category1.setName("Giày Thể Thao");
            category1.setDescription("Giày thể thao chuyên dụng cho các hoạt động thể thao, chạy bộ, tập gym");
            categoryRepository.save(category1);
            System.out.println("   ✓ Created category: Giày Thể Thao");
        }

        // Giày chạy bộ
        if (!categoryRepository.existsByName("Giày Chạy Bộ")) {
            Category category2 = new Category();
            category2.setName("Giày Chạy Bộ");
            category2.setDescription("Giày chạy bộ với công nghệ đệm êm, hỗ trợ tốt cho đôi chân");
            categoryRepository.save(category2);
            System.out.println("   ✓ Created category: Giày Chạy Bộ");
        }

        // Giày thời trang
        if (!categoryRepository.existsByName("Giày Thời Trang")) {
            Category category3 = new Category();
            category3.setName("Giày Thời Trang");
            category3.setDescription("Giày thời trang phong cách, phù hợp cho mọi dịp");
            categoryRepository.save(category3);
            System.out.println("   ✓ Created category: Giày Thời Trang");
        }

        // Giày bóng đá
        if (!categoryRepository.existsByName("Giày Bóng Đá")) {
            Category category4 = new Category();
            category4.setName("Giày Bóng Đá");
            category4.setDescription("Giày bóng đá chuyên nghiệp với đế đinh cao su");
            categoryRepository.save(category4);
            System.out.println("   ✓ Created category: Giày Bóng Đá");
        }
    }

    private void seedProducts() {
        System.out.println("🌱 Seeding Products...");

        Category theThao = categoryRepository.findByName("Giày Thể Thao")
                .orElse(categoryRepository.findAll().get(0));
        Category chayBo = categoryRepository.findByName("Giày Chạy Bộ")
                .orElse(categoryRepository.findAll().get(0));
        Category thoiTrang = categoryRepository.findByName("Giày Thời Trang")
                .orElse(categoryRepository.findAll().get(0));
        Category bongDa = categoryRepository.findByName("Giày Bóng Đá")
                .orElse(categoryRepository.findAll().get(0));

        // Nike Air Max 90
        if (productRepository.findByNameContainingIgnoreCase("Nike Air Max 90").isEmpty()) {
            Product product1 = new Product();
            product1.setName("Nike Air Max 90");
            product1.setDescription("Giày thể thao Nike Air Max 90 với công nghệ Air cushioning, thiết kế cổ điển nhưng hiện đại. Phù hợp cho mọi hoạt động thể thao và thời trang.");
            product1.setBrand("Nike");
            product1.setCategory(theThao);
            product1.setCreatedAt(new Date());
            productRepository.save(product1);
            System.out.println("   ✓ Created product: Nike Air Max 90");
        }

        // Adidas Ultraboost 22
        if (productRepository.findByNameContainingIgnoreCase("Adidas Ultraboost 22").isEmpty()) {
            Product product2 = new Product();
            product2.setName("Adidas Ultraboost 22");
            product2.setDescription("Giày chạy bộ Adidas Ultraboost 22 với công nghệ Boost đệm êm, đế Continental cao su bền bỉ. Hoàn hảo cho các runner chuyên nghiệp.");
            product2.setBrand("Adidas");
            product2.setCategory(chayBo);
            product2.setCreatedAt(new Date());
            productRepository.save(product2);
            System.out.println("   ✓ Created product: Adidas Ultraboost 22");
        }

        // Puma RS-X
        if (productRepository.findByNameContainingIgnoreCase("Puma RS-X").isEmpty()) {
            Product product3 = new Product();
            product3.setName("Puma RS-X");
            product3.setDescription("Giày thời trang Puma RS-X với thiết kế retro futurism, phong cách độc đáo. Phù hợp cho giới trẻ năng động.");
            product3.setBrand("Puma");
            product3.setCategory(thoiTrang);
            product3.setCreatedAt(new Date());
            productRepository.save(product3);
            System.out.println("   ✓ Created product: Puma RS-X");
        }

        // Nike Mercurial Vapor 15
        if (productRepository.findByNameContainingIgnoreCase("Nike Mercurial Vapor 15").isEmpty()) {
            Product product4 = new Product();
            product4.setName("Nike Mercurial Vapor 15");
            product4.setDescription("Giày bóng đá Nike Mercurial Vapor 15 với công nghệ Flyknit, nhẹ và bền. Được các cầu thủ chuyên nghiệp tin dùng.");
            product4.setBrand("Nike");
            product4.setCategory(bongDa);
            product4.setCreatedAt(new Date());
            productRepository.save(product4);
            System.out.println("   ✓ Created product: Nike Mercurial Vapor 15");
        }

        // Adidas Predator Edge
        if (productRepository.findByNameContainingIgnoreCase("Adidas Predator Edge").isEmpty()) {
            Product product5 = new Product();
            product5.setName("Adidas Predator Edge");
            product5.setDescription("Giày bóng đá Adidas Predator Edge với công nghệ Demonskin, tăng độ bám và kiểm soát bóng. Thiết kế hiện đại, hiệu năng cao.");
            product5.setBrand("Adidas");
            product5.setCategory(bongDa);
            product5.setCreatedAt(new Date());
            productRepository.save(product5);
            System.out.println("   ✓ Created product: Adidas Predator Edge");
        }

        // New Balance 550
        if (productRepository.findByNameContainingIgnoreCase("New Balance 550").isEmpty()) {
            Product product6 = new Product();
            product6.setName("New Balance 550");
            product6.setDescription("Giày thời trang New Balance 550 với thiết kế cổ điển, chất liệu da cao cấp. Phong cách retro đầy cá tính.");
            product6.setBrand("New Balance");
            product6.setCategory(thoiTrang);
            product6.setCreatedAt(new Date());
            productRepository.save(product6);
            System.out.println("   ✓ Created product: New Balance 550");
        }

        // Nike React Infinity Run
        if (productRepository.findByNameContainingIgnoreCase("Nike React Infinity Run").isEmpty()) {
            Product product7 = new Product();
            product7.setName("Nike React Infinity Run");
            product7.setDescription("Giày chạy bộ Nike React Infinity Run với công nghệ React foam, giảm chấn thương. Hoàn hảo cho các runner muốn chạy xa hơn.");
            product7.setBrand("Nike");
            product7.setCategory(chayBo);
            product7.setCreatedAt(new Date());
            productRepository.save(product7);
            System.out.println("   ✓ Created product: Nike React Infinity Run");
        }

        // Converse Chuck Taylor All Star
        if (productRepository.findByNameContainingIgnoreCase("Converse Chuck Taylor").isEmpty()) {
            Product product8 = new Product();
            product8.setName("Converse Chuck Taylor All Star");
            product8.setDescription("Giày thời trang Converse Chuck Taylor All Star - biểu tượng của phong cách streetwear. Thiết kế đơn giản nhưng không bao giờ lỗi mốt.");
            product8.setBrand("Converse");
            product8.setCategory(thoiTrang);
            product8.setCreatedAt(new Date());
            productRepository.save(product8);
            System.out.println("   ✓ Created product: Converse Chuck Taylor All Star");
        }
    }
}

