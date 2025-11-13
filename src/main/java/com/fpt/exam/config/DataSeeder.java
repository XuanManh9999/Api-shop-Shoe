package com.fpt.exam.config;

import com.fpt.exam.entity.*;
import com.fpt.exam.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ReviewRepository reviewRepository;

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

        // Seed product variants nếu chưa có
        if (productVariantRepository.count() == 0) {
            seedProductVariants();
        } else {
            System.out.println("ℹ️  Product variants already exist. Skipping variant seed.");
        }

        // Seed product images nếu chưa có
        if (productImageRepository.count() == 0) {
            seedProductImages();
        } else {
            System.out.println("ℹ️  Product images already exist. Skipping image seed.");
        }

        // Seed discounts nếu chưa có
        if (discountRepository.count() == 0) {
            seedDiscounts();
        } else {
            System.out.println("ℹ️  Discounts already exist. Skipping discount seed.");
        }

        // Seed orders nếu chưa có
        if (orderRepository.count() == 0) {
            seedOrders();
        } else {
            System.out.println("ℹ️  Orders already exist. Skipping order seed.");
        }

        // Seed reviews nếu chưa có
        if (reviewRepository.count() == 0) {
            seedReviews();
        } else {
            System.out.println("ℹ️  Reviews already exist. Skipping review seed.");
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

        // Thêm một số users khác để có đủ dữ liệu demo
        String[] userNames = {
            "Nguyễn Văn An", "Trần Thị Bình", "Lê Văn Cường", 
            "Phạm Thị Dung", "Hoàng Văn Em", "Vũ Thị Phương"
        };
        String[] userEmails = {
            "nguyenvanan@example.com", "tranthibinh@example.com", "levancuong@example.com",
            "phamthidung@example.com", "hoangvanem@example.com", "vuthiphuong@example.com"
        };
        String[] userPhones = {
            "0912345678", "0923456789", "0934567890",
            "0945678901", "0956789012", "0967890123"
        };

        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < userNames.length; i++) {
            if (!userRepository.existsByEmail(userEmails[i])) {
                User user = new User();
                user.setFullName(userNames[i]);
                user.setEmail(userEmails[i]);
                user.setPassword(passwordEncoder.encode("password123"));
                user.setPhone(userPhones[i]);
                user.setAddress((i + 1) * 100 + " Đường Demo, Quận " + (i + 1) + ", TP.HCM");
                user.setRole(userRole);
                user.setStatus(true);
                // Tạo users với ngày khác nhau
                cal.setTime(new Date());
                cal.add(Calendar.DAY_OF_MONTH, -(30 - (i * 5)));
                user.setCreatedAt(cal.getTime());
                userRepository.save(user);
                System.out.println("   ✓ Created user: " + userNames[i] + " (" + userEmails[i] + ")");
            }
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

    private void seedProductVariants() {
        System.out.println("🌱 Seeding Product Variants...");

        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            System.out.println("   ⚠️  No products found. Please seed products first.");
            return;
        }

        String[] sizes = {"39", "40", "41", "42", "43", "44", "45"};
        String[] colors = {"Black", "White", "Red", "Blue", "Gray", "Navy", "Green"};
        double[] basePrices = {1500000.0, 1800000.0, 1200000.0, 2000000.0, 2500000.0, 1600000.0, 1700000.0, 1400000.0};

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            double basePrice = basePrices[i % basePrices.length];

            // Tạo 3-5 variants cho mỗi product
            int variantCount = 3 + (i % 3); // 3-5 variants
            for (int j = 0; j < variantCount; j++) {
                ProductVariant variant = new ProductVariant();
                variant.setProduct(product);
                variant.setSize(sizes[j % sizes.length]);
                variant.setColor(colors[j % colors.length]);
                variant.setStock(20 + (j * 10)); // Stock từ 20-60
                variant.setPrice(basePrice);
                
                // Có discount price cho một số variants
                if (j % 2 == 0) {
                    variant.setDiscountPrice(basePrice * 0.85); // Giảm 15%
                }
                
                productVariantRepository.save(variant);
            }
            System.out.println("   ✓ Created " + variantCount + " variants for: " + product.getName());
        }
    }

    private void seedProductImages() {
        System.out.println("🌱 Seeding Product Images...");

        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            System.out.println("   ⚠️  No products found. Please seed products first.");
            return;
        }

        // Sử dụng ảnh fake từ các service
        // Picsum Photos: https://picsum.photos/seed/{seed}/800/600
        // Unsplash Source: https://source.unsplash.com/800x600/?sneakers,shoes
        // Placeholder: https://via.placeholder.com/800x600
        
        String[] imageUrls = {
            "https://picsum.photos/seed/nike1/800/600",
            "https://picsum.photos/seed/nike2/800/600",
            "https://picsum.photos/seed/nike3/800/600",
            "https://picsum.photos/seed/adidas1/800/600",
            "https://picsum.photos/seed/adidas2/800/600",
            "https://picsum.photos/seed/puma1/800/600",
            "https://picsum.photos/seed/puma2/800/600",
            "https://picsum.photos/seed/converse1/800/600",
            "https://picsum.photos/seed/converse2/800/600",
            "https://picsum.photos/seed/newbalance1/800/600",
            "https://picsum.photos/seed/newbalance2/800/600",
            "https://picsum.photos/seed/shoes1/800/600",
            "https://picsum.photos/seed/shoes2/800/600",
            "https://picsum.photos/seed/shoes3/800/600",
            "https://picsum.photos/seed/shoes4/800/600"
        };

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            
            // Mỗi product có 2-4 ảnh
            int imageCount = 2 + (i % 3); // 2-4 images
            for (int j = 0; j < imageCount; j++) {
                ProductImage image = new ProductImage();
                image.setProduct(product);
                // Sử dụng seed khác nhau cho mỗi ảnh
                int imageIndex = (i * 10 + j) % imageUrls.length;
                image.setImageUrl(imageUrls[imageIndex]);
                image.setIsMain(j == 0); // Ảnh đầu tiên là ảnh chính
                image.setUploadedAt(new Date());
                productImageRepository.save(image);
            }
            System.out.println("   ✓ Created " + imageCount + " images for: " + product.getName());
        }
    }

    private void seedDiscounts() {
        System.out.println("🌱 Seeding Discounts...");

        Calendar cal = Calendar.getInstance();
        Date now = new Date();

        // Discount 1: Summer Sale
        if (!discountRepository.findByCode("SUMMER2024").isPresent()) {
            Discount discount1 = new Discount();
            discount1.setCode("SUMMER2024");
            discount1.setDescription("Giảm giá mùa hè 2024 - Áp dụng cho tất cả sản phẩm");
            discount1.setDiscountPercent(20);
            cal.setTime(now);
            cal.add(Calendar.MONTH, -1);
            discount1.setStartDate(cal.getTime());
            cal.add(Calendar.MONTH, 2);
            discount1.setEndDate(cal.getTime());
            discountRepository.save(discount1);
            System.out.println("   ✓ Created discount: SUMMER2024 (20% off)");
        }

        // Discount 2: New Customer
        if (!discountRepository.findByCode("NEWCUSTOMER").isPresent()) {
            Discount discount2 = new Discount();
            discount2.setCode("NEWCUSTOMER");
            discount2.setDescription("Giảm giá cho khách hàng mới - Giảm 15% cho đơn hàng đầu tiên");
            discount2.setDiscountPercent(15);
            cal.setTime(now);
            cal.add(Calendar.MONTH, -2);
            discount2.setStartDate(cal.getTime());
            cal.add(Calendar.YEAR, 1);
            discount2.setEndDate(cal.getTime());
            discountRepository.save(discount2);
            System.out.println("   ✓ Created discount: NEWCUSTOMER (15% off)");
        }

        // Discount 3: Flash Sale
        if (!discountRepository.findByCode("FLASH50").isPresent()) {
            Discount discount3 = new Discount();
            discount3.setCode("FLASH50");
            discount3.setDescription("Flash Sale - Giảm 50% cho các sản phẩm được chọn");
            discount3.setDiscountPercent(50);
            cal.setTime(now);
            cal.add(Calendar.DAY_OF_MONTH, -5);
            discount3.setStartDate(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 10);
            discount3.setEndDate(cal.getTime());
            discountRepository.save(discount3);
            System.out.println("   ✓ Created discount: FLASH50 (50% off)");
        }

        // Discount 4: Weekend Sale
        if (!discountRepository.findByCode("WEEKEND10").isPresent()) {
            Discount discount4 = new Discount();
            discount4.setCode("WEEKEND10");
            discount4.setDescription("Giảm giá cuối tuần - Giảm 10% cho tất cả đơn hàng");
            discount4.setDiscountPercent(10);
            cal.setTime(now);
            cal.add(Calendar.MONTH, -3);
            discount4.setStartDate(cal.getTime());
            cal.add(Calendar.YEAR, 1);
            discount4.setEndDate(cal.getTime());
            discountRepository.save(discount4);
            System.out.println("   ✓ Created discount: WEEKEND10 (10% off)");
        }
    }

    private void seedOrders() {
        System.out.println("🌱 Seeding Orders...");

        List<User> users = userRepository.findAll();
        List<ProductVariant> variants = productVariantRepository.findAll();
        List<Discount> discounts = discountRepository.findAll();

        if (users.isEmpty() || variants.isEmpty()) {
            System.out.println("   ⚠️  No users or variants found. Please seed users and variants first.");
            return;
        }

        String[] statuses = {"PENDING", "CONFIRMED", "DELIVERED", "CANCELLED"};
        String[] paymentMethods = {"CREDIT_CARD", "BANK_TRANSFER", "CASH", "E_WALLET"};
        String[] addresses = {
            "123 Nguyễn Huệ, Quận 1, TP.HCM",
            "456 Lê Lợi, Quận 1, TP.HCM",
            "789 Điện Biên Phủ, Quận Bình Thạnh, TP.HCM",
            "321 Võ Văn Tần, Quận 3, TP.HCM",
            "654 Nguyễn Trãi, Quận 5, TP.HCM"
        };

        Calendar cal = Calendar.getInstance();
        
        // Tạo 15-20 orders
        for (int i = 0; i < 18; i++) {
            User user = users.get(i % users.size());
            
            // Tạo order với ngày khác nhau (trong 3 tháng gần đây)
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, -(60 - (i * 3))); // Spread orders over 60 days
            Date orderDate = cal.getTime();

            Order order = new Order();
            order.setUser(user);
            order.setOrderDate(orderDate);
            order.setStatus(statuses[i % statuses.length]);
            order.setPaymentMethod(paymentMethods[i % paymentMethods.length]);
            order.setShippingAddress(addresses[i % addresses.length]);
            
            // Một số orders có discount
            if (i % 3 == 0 && !discounts.isEmpty()) {
                order.setDiscount(discounts.get(i % discounts.size()));
            }

            // Tạo order items (1-3 items per order)
            int itemCount = 1 + (i % 3);
            List<OrderItem> items = new ArrayList<>();
            double totalAmount = 0.0;

            for (int j = 0; j < itemCount; j++) {
                ProductVariant variant = variants.get((i * 3 + j) % variants.size());
                int quantity = 1 + (j % 2); // 1-2 items
                double unitPrice = variant.getDiscountPrice() != null && variant.getDiscountPrice() > 0 
                    ? variant.getDiscountPrice() 
                    : variant.getPrice();
                double itemTotal = unitPrice * quantity;

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setVariant(variant);
                orderItem.setQuantity(quantity);
                orderItem.setUnitPrice(unitPrice);
                orderItem.setTotalPrice(itemTotal);
                items.add(orderItem);
                totalAmount += itemTotal;
            }

            order.setTotalAmount(totalAmount);
            order.setItems(items);
            orderRepository.save(order);
            
            // Save order items
            for (OrderItem item : items) {
                orderItemRepository.save(item);
            }

            System.out.println("   ✓ Created order #" + (i + 1) + " for " + user.getFullName() + " - Total: " + totalAmount);
        }
    }

    private void seedReviews() {
        System.out.println("🌱 Seeding Reviews...");

        List<User> users = userRepository.findAll();
        List<Product> products = productRepository.findAll();

        if (users.isEmpty() || products.isEmpty()) {
            System.out.println("   ⚠️  No users or products found. Please seed users and products first.");
            return;
        }

        String[] comments = {
            "Sản phẩm rất tốt, chất lượng cao!",
            "Đẹp và thoải mái, rất hài lòng với sản phẩm này.",
            "Giao hàng nhanh, đóng gói cẩn thận. Sẽ mua lại!",
            "Chất lượng tốt nhưng giá hơi cao một chút.",
            "Sản phẩm đúng như mô tả, rất đẹp!",
            "Phù hợp với giá tiền, chất lượng ổn.",
            "Rất thích sản phẩm này, sẽ giới thiệu cho bạn bè.",
            "Màu sắc đẹp, size vừa vặn. Hài lòng!",
            "Sản phẩm tốt nhưng cần cải thiện về đóng gói.",
            "Tuyệt vời! Vượt quá mong đợi của tôi."
        };

        int[] ratings = {5, 5, 5, 4, 5, 4, 5, 5, 4, 5};

        // Tạo 2-3 reviews cho mỗi product
        for (Product product : products) {
            int reviewCount = 2 + (product.getProductId().intValue() % 2); // 2-3 reviews
            
            for (int i = 0; i < reviewCount; i++) {
                User user = users.get((product.getProductId().intValue() * 3 + i) % users.size());
                int commentIndex = (product.getProductId().intValue() * 3 + i) % comments.length;
                
                Review review = new Review();
                review.setProduct(product);
                review.setUser(user);
                review.setRating(ratings[commentIndex]);
                review.setComment(comments[commentIndex]);
                
                // Tạo review với ngày khác nhau
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.DAY_OF_MONTH, -(30 - (product.getProductId().intValue() * 3 + i)));
                review.setCreatedAt(cal.getTime());
                
                reviewRepository.save(review);
            }
            System.out.println("   ✓ Created " + reviewCount + " reviews for: " + product.getName());
        }
    }
}

