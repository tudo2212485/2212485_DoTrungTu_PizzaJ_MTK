# 🗄️ Hướng Dẫn Lưu Ảnh Dạng BLOB trong SQLite

## 📝 BLOB là gì?

**BLOB** (Binary Large Object) = Lưu file ảnh **trực tiếp** trong database dưới dạng binary.

### Ưu điểm:
- ✅ **Offline 100%** - Không cần Internet
- ✅ **Tự chứa** - Database chứa tất cả
- ✅ **Không lo link hết hạn**

### Nhược điểm:
- ❌ Database **rất nặng** (5 ảnh = ~1-2MB)
- ❌ Khó **xem/sửa** ảnh trong DB
- ❌ Performance **chậm hơn** với nhiều ảnh

---

## 🛠️ Cách triển khai (Nếu cần):

### Bước 1: Cập nhật schema database

```java
// Trong SQLiteConnection.java
String createPizzasTable = """
    CREATE TABLE IF NOT EXISTS pizzas (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL UNIQUE,
        description TEXT,
        base_price INTEGER NOT NULL,
        image_blob BLOB,           -- Thêm cột BLOB
        image_url TEXT,            -- Giữ URL backup
        is_active INTEGER DEFAULT 1
    )
""";
```

### Bước 2: Insert ảnh vào BLOB

```java
// Đọc file ảnh thành byte[]
public static byte[] readImageFile(String path) throws IOException {
    try (FileInputStream fis = new FileInputStream(path)) {
        return fis.readAllBytes();
    }
}

// Insert vào database
String insertPizza = """
    INSERT INTO pizzas (name, description, base_price, image_blob) 
    VALUES (?, ?, ?, ?)
""";

try (var pstmt = connection.prepareStatement(insertPizza)) {
    pstmt.setString(1, "Margherita");
    pstmt.setString(2, "Pizza cổ điển...");
    pstmt.setInt(3, 60_000);
    
    // Đọc ảnh từ file
    byte[] imageBytes = readImageFile("path/to/margherita.png");
    pstmt.setBytes(4, imageBytes);
    
    pstmt.executeUpdate();
}
```

### Bước 3: Load ảnh từ BLOB

```java
// Trong PizzaRepository.java
public byte[] getPizzaImageBlob(String pizzaName) throws SQLException {
    String sql = "SELECT image_blob FROM pizzas WHERE name = ?";
    try (Connection conn = SQLiteConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, pizzaName);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            return rs.getBytes("image_blob");
        }
    }
    return null;
}
```

### Bước 4: Hiển thị trong JavaFX

```java
// Trong MenuController.java
private void loadPizzaImageFromBlob(byte[] imageBytes) {
    if (imageBytes != null && imageBytes.length > 0) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
            Image image = new Image(bis);
            pizzaImageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

## 📦 Kích thước file so sánh:

| Phương pháp | Kích thước DB | Ví dụ |
|-------------|---------------|-------|
| **URL** | ~500 bytes | `pizza_orders.db` (8 KB) |
| **BLOB (5 ảnh 200KB)** | ~1 MB | `pizza_orders.db` (1.5 MB) |
| **BLOB (5 ảnh 50KB)** | ~250 KB | `pizza_orders.db` (300 KB) |

---

## 💡 Khuyến nghị:

### Dùng **URL** (hiện tại) khi:
- ✅ Có Internet ổn định
- ✅ Muốn database nhẹ
- ✅ Dễ thay đổi ảnh
- ✅ Demo/Prototype

### Dùng **BLOB** khi:
- ✅ Cần offline hoàn toàn
- ✅ Database size không quan trọng
- ✅ Ảnh không thay đổi nhiều
- ✅ Production app

### Dùng **File local** khi:
- ✅ Cân bằng giữa 2 cách trên
- ✅ Dễ quản lý ảnh
- ✅ Performance tốt
- ✅ Recommended cho most cases

---

## 🎯 Kết luận:

**Ứng dụng hiện tại** đang dùng **URL từ Unsplash** → Phù hợp cho demo!

Nếu cần offline, có thể:
1. Download 5 ảnh từ Unsplash
2. Lưu vào `src/main/resources/images/`
3. UPDATE database: `image_url = 'images/margherita.png'`
4. Code đã support cả 2 (URL + local)!

---

## 🚀 Tóm tắt cấu trúc hiện tại:

```
SQLite Database (pizza_orders.db)
├── pizzas table
│   ├── id
│   ├── name
│   ├── description  
│   ├── base_price
│   └── image_url → "https://images.unsplash.com/..."
│
└── Controller tự động:
    ✅ Detect URL → Load từ Internet
    ✅ Detect path → Load từ resources
    ✅ Không tìm thấy → Placeholder
```

**Perfect! Vừa linh hoạt, vừa dễ dùng!** 🎉


