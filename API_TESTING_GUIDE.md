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

## 📝 Thứ tự test đề xuất

1. **Register** → Lấy token
2. **Login** → Lấy token (nếu đã có account)
3. **Tạo Role ADMIN** (nếu chưa có) → Cần tạo role ADMIN trước
4. **Gán role ADMIN cho user** → Qua User API update
5. **Login lại với user ADMIN** → Lấy token ADMIN
6. **Tạo Category** (nếu chưa có) → Để test Product
7. **Tạo Product** → Test với token
8. **Get Products** → Test pagination và search
9. **Update/Delete Product** → Test CRUD
10. **Get Users** → Cần ADMIN role
11. **Get/Create/Update/Delete Roles** → Cần ADMIN role
12. **Refresh Token** → Test khi token hết hạn
