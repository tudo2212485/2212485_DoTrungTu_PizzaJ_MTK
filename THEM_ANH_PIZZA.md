# 🖼️ Hướng Dẫn Thêm Ảnh Pizza - Nhanh!

## 📥 Cách 1: Download ảnh từ Internet (NHANH NHẤT)

### Bước 1: Tìm ảnh pizza miễn phí

Truy cập các trang web ảnh miễn phí:
- **Unsplash**: https://unsplash.com/s/photos/pizza
- **Pexels**: https://www.pexels.com/search/pizza/
- **Pixabay**: https://pixabay.com/images/search/pizza/

### Bước 2: Download 5 ảnh pizza

Tìm và download ảnh cho 5 loại:
1. Margherita (pizza cơ bản với phô mai)
2. Pepperoni (pizza với xúc xích)
3. Hawaiian (pizza với dứa)
4. Seafood (pizza hải sản)
5. Veggie/Rau (pizza rau củ)

### Bước 3: Đổi tên file

Sau khi download, đổi tên file thành:
```
margherita.png
pepperoni.png
hawaiian.png
seafood.png
veggie-supreme.png
```

### Bước 4: Copy vào thư mục

Copy 5 file ảnh vào thư mục:
```
D:\2212485_DoTrungTu_PizzaJ_MTK\src\main\resources\images\
```

---

## 📥 Cách 2: Sử dụng ảnh AI (Nếu có)

Nếu bạn có ChatGPT Plus, DALL-E, hoặc Midjourney:
1. Tạo ảnh pizza với prompt: "realistic photo of [loại pizza], top view, white background"
2. Download và đổi tên như Cách 1
3. Copy vào thư mục images

---

## 📥 Cách 3: Sử dụng ảnh tạm (DEMO)

Nếu chưa có ảnh, ứng dụng vẫn chạy được nhưng sẽ:
- Hiển thị vùng trống thay vì ảnh
- In thông báo "⚠️ Không tìm thấy ảnh pizza" trong console
- Vẫn hoạt động đầy đủ các chức năng khác

---

## ✅ Kiểm tra

Sau khi thêm ảnh, cấu trúc thư mục sẽ như sau:

```
D:\2212485_DoTrungTu_PizzaJ_MTK\
└── src\
    └── main\
        └── resources\
            └── images\
                ├── margherita.png
                ├── pepperoni.png
                ├── hawaiian.png
                ├── seafood.png
                └── veggie-supreme.png
```

---

## 🚀 Chạy lại ứng dụng

Sau khi thêm ảnh, xóa database cũ để load lại:
```powershell
# Xóa database cũ
Remove-Item pizza_orders.db -ErrorAction SilentlyContinue

# Chạy lại
mvn clean javafx:run
```

Hoặc đơn giản:
```powershell
mvn clean javafx:run
```

---

## 📐 Lưu ý về kích thước ảnh

- **Kích thước tối ưu**: 400x400 pixels (hình vuông)
- **Format**: PNG (trong suốt tốt nhất) hoặc JPG
- **Dung lượng**: < 200KB mỗi ảnh
- **Background**: Nên dùng nền trắng hoặc trong suốt

---

## 🎯 Ví dụ tìm ảnh nhanh trên Unsplash

1. Vào https://unsplash.com
2. Search: "margherita pizza"
3. Chọn ảnh đẹp, click Download
4. Đổi tên file thành `margherita.png`
5. Lặp lại với các loại pizza khác

**Mẹo**: Thêm từ khóa "top view" hoặc "overhead" để có góc chụp từ trên đẹp hơn!


