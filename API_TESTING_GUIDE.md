# 📋 Hướng dẫn Test API trên Postman

## 🔐 AUTH APIs (Không cần token)

### 1. Đăng ký (Register)

**POST** `http://localhost:8080/api/auth/register`

**Headers:**

```
Content-Type: application/json
```

**Body (JSON):**

```json
{
  "fullName": "Nguyễn Văn A",
  "email": "user@example.com",
  "password": "password123",
  "phone": "0123456789",
  "address": "123 Đường ABC, Quận 1, TP.HCM"
}
```

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Registration successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
    "type": "Bearer",
    "userId": 1,
    "email": "user@example.com",
    "fullName": "Nguyễn Văn A",
    "role": "USER"
  }
}
```

---

### 2. Đăng nhập (Login)

**POST** `http://localhost:8080/api/auth/login`

**Headers:**

```
Content-Type: application/json
```

**Body (JSON):**

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
    "type": "Bearer",
    "userId": 1,
    "email": "user@example.com",
    "fullName": "Nguyễn Văn A",
    "role": "USER"
  }
}
```

**Lưu ý:** Copy `token` từ response để dùng cho các API khác!

---

### 3. Refresh Token

**POST** `http://localhost:8080/api/auth/refresh`

**Headers:**

```
Content-Type: application/json
```

**Body (JSON):**

```json
{
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9..."
}
```

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Token refreshed successfully",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
    "type": "Bearer",
    "userId": 1,
    "email": "user@example.com",
    "fullName": "Nguyễn Văn A",
    "role": "USER"
  }
}
```

---

## 👥 USER APIs (Cần ADMIN role + Token)

**Headers bắt buộc:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

### 4. Lấy danh sách Users (có phân trang, tìm kiếm)

**GET** `http://localhost:8080/api/users`

**Query Parameters:**

- `keyword` (optional): Tìm kiếm theo tên, email, phone
- `page` (default: 0): Số trang (bắt đầu từ 0)
- `size` (default: 10): Số lượng items mỗi trang
- `sortBy` (default: userId): Trường để sort (userId, fullName, email, createdAt)
- `sortDir` (default: asc): Hướng sort (asc hoặc desc)

**Ví dụ:**

```
GET http://localhost:8080/api/users?page=0&size=10&keyword=nguyen&sortBy=fullName&sortDir=asc
```

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Users retrieved successfully",
  "data": {
    "content": [
      {
        "userId": 1,
        "fullName": "Nguyễn Văn A",
        "email": "user@example.com",
        "phone": "0123456789",
        "address": "123 Đường ABC",
        "status": true,
        "createdAt": "2025-11-09T10:00:00",
        "role": {
          "roleId": 1,
          "roleName": "USER"
        }
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 1,
    "totalPages": 1,
    "first": true,
    "last": true
  }
}
```

---

### 5. Lấy User theo ID

**GET** `http://localhost:8080/api/users/{id}`

**Ví dụ:**

```
GET http://localhost:8080/api/users/1
```

**Response mẫu:**

```json
{
  "status": "success",
  "message": "User retrieved successfully",
  "data": {
    "userId": 1,
    "fullName": "Nguyễn Văn A",
    "email": "user@example.com",
    "phone": "0123456789",
    "address": "123 Đường ABC",
    "status": true,
    "createdAt": "2025-11-09T10:00:00",
    "role": {
      "roleId": 1,
      "roleName": "USER"
    }
  }
}
```

---

### 6. Tạo User mới

**POST** `http://localhost:8080/api/users`

**Body (JSON):**

```json
{
  "fullName": "Trần Thị B",
  "email": "admin@example.com",
  "password": "password123",
  "phone": "0987654321",
  "address": "456 Đường XYZ",
  "status": true,
  "role": {
    "roleId": 2,
    "roleName": "ADMIN"
  }
}
```

**Lưu ý:**

- `password` sẽ được mã hóa tự động
- `role` cần có `roleId` hợp lệ trong database
- `status` mặc định là `true` nếu không gửi

**Response mẫu:**

```json
{
  "status": "success",
  "message": "User created successfully",
  "data": {
    "userId": 2,
    "fullName": "Trần Thị B",
    "email": "admin@example.com",
    "phone": "0987654321",
    "address": "456 Đường XYZ",
    "status": true,
    "createdAt": "2025-11-09T10:30:00",
    "role": {
      "roleId": 2,
      "roleName": "ADMIN"
    }
  }
}
```

---

### 7. Cập nhật User

**PUT** `http://localhost:8080/api/users/{id}`

**Ví dụ:**

```
PUT http://localhost:8080/api/users/1
```

**Body (JSON):**

```json
{
  "fullName": "Nguyễn Văn A Updated",
  "email": "user@example.com",
  "phone": "0999999999",
  "address": "789 Đường Mới",
  "status": true,
  "role": {
    "roleId": 1,
    "roleName": "USER"
  }
}
```

**Lưu ý:** Không gửi `password` trong body update (cần API riêng để đổi mật khẩu)

**Response mẫu:**

```json
{
  "status": "success",
  "message": "User updated successfully",
  "data": {
    "userId": 1,
    "fullName": "Nguyễn Văn A Updated",
    "email": "user@example.com",
    "phone": "0999999999",
    "address": "789 Đường Mới",
    "status": true,
    "role": {
      "roleId": 1,
      "roleName": "USER"
    }
  }
}
```

---

### 8. Xóa User

**DELETE** `http://localhost:8080/api/users/{id}`

**Ví dụ:**

```
DELETE http://localhost:8080/api/users/1
```

**Response mẫu:**

```json
{
  "status": "success",
  "message": "User deleted successfully",
  "data": null
}
```

---

## 📦 PRODUCT APIs (Cần ADMIN hoặc USER role + Token)

**Headers bắt buộc:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

### 9. Lấy danh sách Products (có phân trang, tìm kiếm)

**GET** `http://localhost:8080/api/products`

**Query Parameters:**

- `keyword` (optional): Tìm kiếm theo tên, mô tả, brand
- `categoryId` (optional): Lọc theo category ID
- `page` (default: 0): Số trang (bắt đầu từ 0)
- `size` (default: 10): Số lượng items mỗi trang
- `sortBy` (default: productId): Trường để sort (productId, name, brand, createdAt)
- `sortDir` (default: asc): Hướng sort (asc hoặc desc)

**Ví dụ:**

```
GET http://localhost:8080/api/products?page=0&size=10&keyword=nike&categoryId=1&sortBy=name&sortDir=asc
```

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Products retrieved successfully",
  "data": {
    "content": [
      {
        "productId": 1,
        "name": "Nike Air Max 90",
        "description": "Giày thể thao cao cấp",
        "brand": "Nike",
        "createdAt": "2025-11-09T10:00:00",
        "category": {
          "categoryId": 1,
          "name": "Giày thể thao",
          "description": "Danh mục giày thể thao"
        },
        "images": [],
        "variants": []
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 1,
    "totalPages": 1,
    "first": true,
    "last": true
  }
}
```

---

### 10. Lấy Product theo ID

**GET** `http://localhost:8080/api/products/{id}`

**Ví dụ:**

```
GET http://localhost:8080/api/products/1
```

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Product retrieved successfully",
  "data": {
    "productId": 1,
    "name": "Nike Air Max 90",
    "description": "Giày thể thao cao cấp",
    "brand": "Nike",
    "createdAt": "2025-11-09T10:00:00",
    "category": {
      "categoryId": 1,
      "name": "Giày thể thao",
      "description": "Danh mục giày thể thao"
    },
    "images": [],
    "variants": []
  }
}
```

---

### 11. Tạo Product mới

**POST** `http://localhost:8080/api/products`

**Body (JSON):**

```json
{
  "name": "Adidas Ultraboost 22",
  "description": "Giày chạy bộ công nghệ cao với đế Boost",
  "brand": "Adidas",
  "category": {
    "categoryId": 1,
    "name": "Giày thể thao",
    "description": "Danh mục giày thể thao"
  }
}
```

**Lưu ý:**

- `category` cần có `categoryId` hợp lệ trong database
- `images` và `variants` sẽ được tạo riêng (không gửi trong body này)

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Product created successfully",
  "data": {
    "productId": 2,
    "name": "Adidas Ultraboost 22",
    "description": "Giày chạy bộ công nghệ cao với đế Boost",
    "brand": "Adidas",
    "createdAt": "2025-11-09T11:00:00",
    "category": {
      "categoryId": 1,
      "name": "Giày thể thao",
      "description": "Danh mục giày thể thao"
    },
    "images": [],
    "variants": []
  }
}
```

---

### 12. Cập nhật Product

**PUT** `http://localhost:8080/api/products/{id}`

**Ví dụ:**

```
PUT http://localhost:8080/api/products/1
```

**Body (JSON):**

```json
{
  "name": "Nike Air Max 90 Updated",
  "description": "Mô tả mới cho sản phẩm",
  "brand": "Nike",
  "category": {
    "categoryId": 1,
    "name": "Giày thể thao",
    "description": "Danh mục giày thể thao"
  }
}
```

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Product updated successfully",
  "data": {
    "productId": 1,
    "name": "Nike Air Max 90 Updated",
    "description": "Mô tả mới cho sản phẩm",
    "brand": "Nike",
    "createdAt": "2025-11-09T10:00:00",
    "category": {
      "categoryId": 1,
      "name": "Giày thể thao",
      "description": "Danh mục giày thể thao"
    }
  }
}
```

---

### 13. Xóa Product

**DELETE** `http://localhost:8080/api/products/{id}`

**Ví dụ:**

```
DELETE http://localhost:8080/api/products/1
```

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Product deleted successfully",
  "data": null
}
```

---

## 🔑 Cách sử dụng Token trong Postman

1. **Sau khi login/register**, copy `token` từ response
2. Vào tab **Authorization** trong Postman
3. Chọn type: **Bearer Token**
4. Paste token vào ô **Token**
5. Hoặc thêm vào **Headers**:
   ```
   Authorization: Bearer {paste_token_here}
   ```

## ⚠️ Lưu ý quan trọng

1. **Role ADMIN:** Để test User APIs, cần đăng nhập với tài khoản có role ADMIN
2. **Tạo Role ADMIN:**

   - Đăng ký user đầu tiên sẽ tự động có role USER
   - Cần tạo role ADMIN trong database và gán cho user
   - Hoặc tạo user mới với role ADMIN qua API (nếu đã có role ADMIN trong DB)

3. **Password:** Tối thiểu 6 ký tự
4. **Email:** Phải đúng format email
5. **Token expiration:** Token hết hạn sau 1 ngày, dùng refresh token để lấy token mới

## 🔐 ROLE APIs (Cần ADMIN role + Token)

**Headers bắt buộc:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

### 14. Lấy danh sách Roles (có phân trang, tìm kiếm)

**GET** `http://localhost:8080/api/roles`

**Query Parameters:**

- `keyword` (optional): Tìm kiếm theo tên role
- `page` (default: 0): Số trang (bắt đầu từ 0)
- `size` (default: 10): Số lượng items mỗi trang
- `sortBy` (default: roleId): Trường để sort (roleId, roleName)
- `sortDir` (default: asc): Hướng sort (asc hoặc desc)

**Ví dụ:**

```
GET http://localhost:8080/api/roles?page=0&size=10&keyword=admin&sortBy=roleName&sortDir=asc
```

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Roles retrieved successfully",
  "data": {
    "content": [
      {
        "roleId": 1,
        "roleName": "USER"
      },
      {
        "roleId": 2,
        "roleName": "ADMIN"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 2,
    "totalPages": 1,
    "first": true,
    "last": true
  }
}
```

---

### 15. Lấy Role theo ID

**GET** `http://localhost:8080/api/roles/{id}`

**Ví dụ:**

```
GET http://localhost:8080/api/roles/1
```

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Role retrieved successfully",
  "data": {
    "roleId": 1,
    "roleName": "USER"
  }
}
```

---

### 16. Tạo Role mới

**POST** `http://localhost:8080/api/roles`

**Body (JSON):**

```json
{
  "roleName": "MANAGER"
}
```

**Lưu ý:**

- `roleName` phải unique (không trùng với role đã có)
- Thường dùng: USER, ADMIN, MANAGER, etc.

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Role created successfully",
  "data": {
    "roleId": 3,
    "roleName": "MANAGER"
  }
}
```

---

### 17. Cập nhật Role

**PUT** `http://localhost:8080/api/roles/{id}`

**Ví dụ:**

```
PUT http://localhost:8080/api/roles/1
```

**Body (JSON):**

```json
{
  "roleName": "CUSTOMER"
}
```

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Role updated successfully",
  "data": {
    "roleId": 1,
    "roleName": "CUSTOMER"
  }
}
```

---

### 18. Xóa Role

**DELETE** `http://localhost:8080/api/roles/{id}`

**Ví dụ:**

```
DELETE http://localhost:8080/api/roles/3
```

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Role deleted successfully",
  "data": null
}
```

**Lưu ý:** Không nên xóa role đang được sử dụng bởi users (có thể gây lỗi foreign key)

---

## 📁 CATEGORY APIs (Cần ADMIN role + Token)

**Headers bắt buộc:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

### 19. Lấy danh sách Categories (có phân trang, tìm kiếm)

**GET** `http://localhost:8080/api/categories`

**Query Parameters:**

- `keyword` (optional): Tìm kiếm theo tên, mô tả
- `page` (default: 0): Số trang (bắt đầu từ 0)
- `size` (default: 10): Số lượng items mỗi trang
- `sortBy` (default: categoryId): Trường để sort (categoryId, name)
- `sortDir` (default: asc): Hướng sort (asc hoặc desc)

**Ví dụ:**

```
GET http://localhost:8080/api/categories?page=0&size=10&keyword=giay&sortBy=name&sortDir=asc
```

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Categories retrieved successfully",
  "data": {
    "content": [
      {
        "categoryId": 1,
        "name": "Giày thể thao",
        "description": "Danh mục giày thể thao"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 1,
    "totalPages": 1,
    "first": true,
    "last": true
  }
}
```

---

### 20. Lấy Category theo ID

**GET** `http://localhost:8080/api/categories/{id}`

**Ví dụ:**

```
GET http://localhost:8080/api/categories/1
```

---

### 21. Tạo Category mới

**POST** `http://localhost:8080/api/categories`

**Body (JSON):**

```json
{
  "name": "Giày chạy bộ",
  "description": "Danh mục giày chạy bộ"
}
```

---

### 22. Cập nhật Category

**PUT** `http://localhost:8080/api/categories/{id}`

**Body (JSON):**

```json
{
  "name": "Giày thể thao Updated",
  "description": "Mô tả mới"
}
```

---

### 23. Xóa Category

**DELETE** `http://localhost:8080/api/categories/{id}`

---

## 🛒 ORDER APIs (Cần ADMIN hoặc USER role + Token)

**Headers bắt buộc:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

### 24. Lấy danh sách Orders (có phân trang, tìm kiếm)

**GET** `http://localhost:8080/api/orders`

**Query Parameters:**

- `keyword` (optional): Tìm kiếm theo status, payment method, shipping address, user name/email
- `status` (optional): Lọc theo status (PENDING, CONFIRMED, DELIVERED, CANCELLED)
- `userId` (optional): Lọc theo user ID
- `page` (default: 0): Số trang
- `size` (default: 10): Số lượng items mỗi trang
- `sortBy` (default: orderId): Trường để sort
- `sortDir` (default: desc): Hướng sort

**Ví dụ:**

```
GET http://localhost:8080/api/orders?page=0&size=10&status=DELIVERED&sortBy=orderDate&sortDir=desc
```

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Orders retrieved successfully",
  "data": {
    "content": [
      {
        "orderId": 1,
        "orderDate": "2024-01-15T10:00:00",
        "totalAmount": 1500000.0,
        "status": "DELIVERED",
        "paymentMethod": "CREDIT_CARD",
        "shippingAddress": "123 Đường ABC",
        "user": {
          "userId": 1,
          "fullName": "Nguyễn Văn A",
          "email": "user@example.com"
        },
        "items": []
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

---

### 25. Lấy Order theo ID

**GET** `http://localhost:8080/api/orders/{id}`

---

### 26. Tạo Order mới

**POST** `http://localhost:8080/api/orders`

**Body (JSON):**

```json
{
  "totalAmount": 1500000.0,
  "status": "PENDING",
  "paymentMethod": "CREDIT_CARD",
  "shippingAddress": "123 Đường ABC, Quận 1, TP.HCM",
  "user": {
    "userId": 1
  },
  "items": [
    {
      "quantity": 2,
      "unitPrice": 750000.0,
      "totalPrice": 1500000.0,
      "variant": {
        "variantId": 1
      }
    }
  ]
}
```

---

### 27. Cập nhật Order

**PUT** `http://localhost:8080/api/orders/{id}`

**Body (JSON):**

```json
{
  "status": "CONFIRMED",
  "paymentMethod": "CREDIT_CARD",
  "shippingAddress": "123 Đường ABC",
  "totalAmount": 1500000.0
}
```

---

### 28. Xóa Order

**DELETE** `http://localhost:8080/api/orders/{id}`

---

## 🎟️ DISCOUNT APIs (Cần ADMIN role + Token)

**Headers bắt buộc:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

### 29. Lấy danh sách Discounts (có phân trang, tìm kiếm)

**GET** `http://localhost:8080/api/discounts`

**Query Parameters:**

- `keyword` (optional): Tìm kiếm theo code, description
- `page` (default: 0): Số trang
- `size` (default: 10): Số lượng items mỗi trang
- `sortBy` (default: discountId): Trường để sort
- `sortDir` (default: asc): Hướng sort

---

### 30. Lấy Discount theo ID

**GET** `http://localhost:8080/api/discounts/{id}`

---

### 31. Lấy Discount theo Code

**GET** `http://localhost:8080/api/discounts/code/{code}`

**Ví dụ:**

```
GET http://localhost:8080/api/discounts/code/SUMMER2024
```

---

### 32. Tạo Discount mới

**POST** `http://localhost:8080/api/discounts`

**Body (JSON):**

```json
{
  "code": "SUMMER2024",
  "description": "Giảm giá mùa hè",
  "discountPercent": 20.0,
  "startDate": "2024-06-01T00:00:00",
  "endDate": "2024-08-31T23:59:59",
  "isActive": true
}
```

---

### 33. Cập nhật Discount

**PUT** `http://localhost:8080/api/discounts/{id}`

---

### 34. Xóa Discount

**DELETE** `http://localhost:8080/api/discounts/{id}`

---

## ⭐ REVIEW APIs (Cần ADMIN hoặc USER role + Token)

**Headers bắt buộc:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

### 35. Lấy danh sách Reviews (có phân trang, tìm kiếm)

**GET** `http://localhost:8080/api/reviews`

**Query Parameters:**

- `keyword` (optional): Tìm kiếm theo comment
- `productId` (optional): Lọc theo product ID
- `userId` (optional): Lọc theo user ID
- `rating` (optional): Lọc theo rating (1-5)
- `page` (default: 0): Số trang
- `size` (default: 10): Số lượng items mỗi trang
- `sortBy` (default: reviewId): Trường để sort
- `sortDir` (default: desc): Hướng sort

**Ví dụ:**

```
GET http://localhost:8080/api/reviews?productId=1&rating=5&page=0&size=10
```

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Reviews retrieved successfully",
  "data": {
    "content": [
      {
        "reviewId": 1,
        "rating": 5,
        "comment": "Sản phẩm rất tốt!",
        "createdAt": "2024-01-15T10:00:00",
        "product": {
          "productId": 1,
          "name": "Nike Air Max 90"
        },
        "user": {
          "userId": 1,
          "fullName": "Nguyễn Văn A"
        }
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 1
  }
}
```

---

### 36. Lấy Review theo ID

**GET** `http://localhost:8080/api/reviews/{id}`

---

### 37. Tạo Review mới

**POST** `http://localhost:8080/api/reviews`

**Body (JSON):**

```json
{
  "rating": 5,
  "comment": "Sản phẩm rất tốt, chất lượng cao!",
  "product": {
    "productId": 1
  },
  "user": {
    "userId": 1
  }
}
```

---

### 38. Cập nhật Review

**PUT** `http://localhost:8080/api/reviews/{id}`

---

### 39. Xóa Review

**DELETE** `http://localhost:8080/api/reviews/{id}`

---

## 🎨 PRODUCT VARIANT APIs (Cần ADMIN hoặc USER role + Token)

**Headers bắt buộc:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

### 40. Lấy danh sách Product Variants (có phân trang, tìm kiếm)

**GET** `http://localhost:8080/api/variants`

**Query Parameters:**

- `keyword` (optional): Tìm kiếm theo size, color, product name
- `productId` (optional): Lọc theo product ID
- `page` (default: 0): Số trang
- `size` (default: 10): Số lượng items mỗi trang
- `sortBy` (default: variantId): Trường để sort
- `sortDir` (default: asc): Hướng sort

**Ví dụ:**

```
GET http://localhost:8080/api/variants?productId=1&page=0&size=10
```

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Product variants retrieved successfully",
  "data": {
    "content": [
      {
        "variantId": 1,
        "size": "42",
        "color": "Black",
        "stock": 50,
        "price": 1500000.0,
        "discountPrice": 1200000.0,
        "product": {
          "productId": 1,
          "name": "Nike Air Max 90"
        }
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 1
  }
}
```

---

### 41. Lấy Product Variant theo ID

**GET** `http://localhost:8080/api/variants/{id}`

---

### 42. Tạo Product Variant mới

**POST** `http://localhost:8080/api/variants`

**Body (JSON):**

```json
{
  "size": "42",
  "color": "Black",
  "stock": 50,
  "price": 1500000.0,
  "discountPrice": 1200000.0,
  "product": {
    "productId": 1
  }
}
```

---

### 43. Cập nhật Product Variant

**PUT** `http://localhost:8080/api/variants/{id}`

---

### 44. Xóa Product Variant

**DELETE** `http://localhost:8080/api/variants/{id}`

---

## 🖼️ PRODUCT IMAGE APIs (Cần ADMIN hoặc USER role + Token)

**Headers bắt buộc:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

### 45. Lấy danh sách Product Images (có phân trang, tìm kiếm)

**GET** `http://localhost:8080/api/product-images`

**Query Parameters:**

- `keyword` (optional): Tìm kiếm theo image URL
- `productId` (optional): Lọc theo product ID
- `isMain` (optional): Lọc theo ảnh chính (true/false)
- `page` (default: 0): Số trang
- `size` (default: 10): Số lượng items mỗi trang
- `sortBy` (default: imageId): Trường để sort
- `sortDir` (default: asc): Hướng sort

**Ví dụ:**

```
GET http://localhost:8080/api/product-images?productId=1&isMain=true
```

---

### 46. Lấy Product Image theo ID

**GET** `http://localhost:8080/api/product-images/{id}`

---

### 47. Tạo Product Image mới

**POST** `http://localhost:8080/api/product-images`

**Body (JSON):**

```json
{
  "imageUrl": "https://example.com/images/nike-air-max-90.jpg",
  "isMain": true,
  "product": {
    "productId": 1
  }
}
```

---

### 48. Cập nhật Product Image

**PUT** `http://localhost:8080/api/product-images/{id}`

---

### 49. Xóa Product Image

**DELETE** `http://localhost:8080/api/product-images/{id}`

---

## 📊 STATISTICS APIs (Cần ADMIN role + Token)

**Headers bắt buộc:**

```
Content-Type: application/json
Authorization: Bearer {token}
```

### 50. Lấy Dashboard Overview

**GET** `http://localhost:8080/api/statistics/dashboard`

**Response mẫu:**

```json
{
  "status": "success",
  "message": "Dashboard data retrieved successfully",
  "data": {
    "revenueSummary": {
      "todayRevenue": 1500000.0,
      "thisMonthRevenue": 45000000.0,
      "thisYearRevenue": 500000000.0,
      "totalRevenue": 1200000000.0
    },
    "orderSummary": {
      "todayOrders": 25,
      "thisMonthOrders": 750,
      "thisYearOrders": 8500,
      "totalOrders": 20000
    },
    "totalUsers": 1500,
    "totalProducts": 500,
    "ordersByStatus": {
      "PENDING": 10,
      "CONFIRMED": 5,
      "DELIVERED": 8,
      "CANCELLED": 2
    },
    "topSellingProducts": [
      {
        "productId": 1,
        "productName": "Nike Air Max 90",
        "totalSold": 150,
        "totalRevenue": 22500000.0
      }
    ],
    "monthlyRevenue": [
      {
        "month": "2024-01",
        "revenue": 40000000.0,
        "orderCount": 650
      }
    ]
  }
}
```

---

### 51. Lấy Revenue Statistics

**GET** `http://localhost:8080/api/statistics/revenue`

**Query Parameters:**

- `period` (default: month): `day`, `month`, hoặc `year`
- `startDate` (optional): Format `yyyy-MM-dd`
- `endDate` (optional): Format `yyyy-MM-dd`

**Ví dụ:**

```
GET http://localhost:8080/api/statistics/revenue?period=month
GET http://localhost:8080/api/statistics/revenue?period=day&startDate=2024-01-01&endDate=2024-01-31
```

**Response mẫu:**

```json
{
  "status": "success",
  "data": {
    "period": "month",
    "startDate": "2024-01-01",
    "endDate": "2024-12-31",
    "totalRevenue": 500000000.0,
    "totalOrders": 8500,
    "averageOrderValue": 58823.53,
    "dataPoints": [
      {
        "date": "2024-01",
        "revenue": 40000000.0,
        "orderCount": 650
      }
    ]
  }
}
```

---

### 52. Lấy Order Statistics

**GET** `http://localhost:8080/api/statistics/orders`

**Query Parameters:**

- `period` (default: month): `day`, `month`, hoặc `year`
- `startDate` (optional): Format `yyyy-MM-dd`
- `endDate` (optional): Format `yyyy-MM-dd`

**Ví dụ:**

```
GET http://localhost:8080/api/statistics/orders?period=month
```

**Response mẫu:**

```json
{
  "status": "success",
  "data": {
    "period": "month",
    "totalOrders": 8500,
    "growthRate": 15.5,
    "dataPoints": [...],
    "ordersByStatus": {
      "PENDING": 200,
      "CONFIRMED": 1500,
      "DELIVERED": 6500,
      "CANCELLED": 300
    },
    "ordersByPaymentMethod": {
      "CASH": 3000,
      "CREDIT_CARD": 4000,
      "BANK_TRANSFER": 1500
    }
  }
}
```

---

### 53. Lấy Product Statistics

**GET** `http://localhost:8080/api/statistics/products`

**Query Parameters:**

- `period` (default: month): `day`, `month`, hoặc `year`

**Ví dụ:**

```
GET http://localhost:8080/api/statistics/products?period=month
```

**Response mẫu:**

```json
{
  "status": "success",
  "data": {
    "period": "month",
    "totalProducts": 500,
    "topSellingProducts": [
      {
        "productId": 1,
        "productName": "Nike Air Max 90",
        "categoryName": "Giày thể thao",
        "totalSold": 150,
        "totalRevenue": 22500000.0
      }
    ],
    "lowStockProducts": [
      {
        "productId": 5,
        "productName": "Adidas Ultraboost",
        "variantInfo": "Size: 42, Color: Black",
        "currentStock": 3,
        "minStockThreshold": 10
      }
    ],
    "productsByCategory": {
      "Giày thể thao": 200,
      "Giày chạy bộ": 150
    }
  }
}
```

---

### 54. Lấy User Statistics

**GET** `http://localhost:8080/api/statistics/users`

**Query Parameters:**

- `period` (default: month): `day`, `month`, hoặc `year`

**Ví dụ:**

```
GET http://localhost:8080/api/statistics/users?period=month
```

**Response mẫu:**

```json
{
  "status": "success",
  "data": {
    "period": "month",
    "totalUsers": 1500,
    "activeUsers": 850,
    "newUsersByPeriod": [
      {
        "date": "2024-01",
        "newUserCount": 120
      }
    ],
    "usersByRole": {
      "USER": 1400,
      "ADMIN": 100
    }
  }
}
```

---

### 55. Lấy Category Statistics

**GET** `http://localhost:8080/api/statistics/categories`

**Response mẫu:**

```json
{
  "status": "success",
  "data": {
    "categoryData": [
      {
        "categoryId": 1,
        "categoryName": "Giày thể thao",
        "productCount": 200,
        "orderCount": 5000,
        "totalRevenue": 300000000.0,
        "averageOrderValue": 60000.0
      }
    ]
  }
}
```

---

### 56. Lấy Top Selling Products

**GET** `http://localhost:8080/api/statistics/top-products`

**Query Parameters:**

- `limit` (default: 10): Số lượng sản phẩm (1-100)
- `period` (default: month): `day`, `month`, hoặc `year`

**Ví dụ:**

```
GET http://localhost:8080/api/statistics/top-products?limit=10&period=month
GET http://localhost:8080/api/statistics/top-products?limit=5&period=day
```

**Response mẫu:**

```json
{
  "status": "success",
  "data": [
    {
      "productId": 1,
      "productName": "Nike Air Max 90",
      "categoryName": "Giày thể thao",
      "totalSold": 150,
      "totalRevenue": 22500000.0
    }
  ]
}
```

---

## 📝 Thứ tự test đề xuất

1. **Register** → Lấy token
2. **Login** → Lấy token (nếu đã có account)
3. **Tạo Role ADMIN** (nếu chưa có) → Cần tạo role ADMIN trước
4. **Gán role ADMIN cho user** → Qua User API update
5. **Login lại với user ADMIN** → Lấy token ADMIN
6. **Tạo Category** → Để test Product
7. **Tạo Product** → Test với token
8. **Tạo Product Variant** → Thêm size, color, price cho product
9. **Tạo Product Image** → Thêm ảnh cho product
10. **Tạo Discount** → Tạo mã giảm giá
11. **Tạo Order** → Test đặt hàng
12. **Tạo Review** → Test đánh giá sản phẩm
13. **Get Products** → Test pagination và search
14. **Get Orders** → Test filter theo status, user
15. **Get Users** → Cần ADMIN role
16. **Get/Create/Update/Delete Roles** → Cần ADMIN role
17. **Get Statistics/Dashboard** → Xem thống kê (cần ADMIN)
18. **Refresh Token** → Test khi token hết hạn
