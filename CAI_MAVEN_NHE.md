# ⚡ CÀI MAVEN NHẸ - CHỈ 10MB!

Maven rất nhẹ, phù hợp khi máy hết bộ nhớ!

## Bước 1: Tải Maven (10MB)

1. Vào: https://maven.apache.org/download.cgi
2. Tìm: **Binary zip archive**
3. Tải: `apache-maven-3.9.6-bin.zip` (~10MB)

## Bước 2: Giải nén vào Ổ D (không chiếm ổ C!)

1. Giải nén file vừa tải
2. **Chép vào ổ D**: `D:\Maven`
   - (Đừng để ở C:\Program Files)

## Bước 3: Thêm vào PATH

### Cách NHANH - Dùng PowerShell:

1. **Mở PowerShell as Administrator**:
   - Nhấn `Windows + X`
   - Chọn "Windows PowerShell (Admin)" hoặc "Terminal (Admin)"

2. **Copy và paste lệnh này**:

```powershell
[Environment]::SetEnvironmentVariable("Path", $env:Path + ";D:\Maven\bin", "Machine")
```

3. Nhấn **Enter**

## Bước 4: Đóng và MỞ LẠI VS Code/Cursor

**QUAN TRỌNG**: Phải đóng và mở lại để PATH cập nhật!

1. Đóng VS Code/Cursor hoàn toàn
2. Mở lại
3. Mở Terminal mới

## Bước 5: Kiểm tra

```powershell
mvn --version
```

Nếu thấy:
```
Apache Maven 3.9.6
Maven home: D:\Maven
Java version: 19.0.2
```
→ THÀNH CÔNG! ✅

## Bước 6: Chạy ứng dụng

```bash
cd D:\2212485_DoTrungTu_PizzaJ_MTK
mvn clean javafx:run
```

Lần đầu chạy, Maven sẽ tải dependencies (~50MB) vào `C:\Users\<tên bạn>\.m2\repository`

Sau đó ứng dụng Pizza Order sẽ mở ra! 🍕

---

## ✅ Ưu điểm cách này:

- ✅ Chỉ 10MB (Maven)
- ✅ Không ảnh hưởng ổ C (cài vào D)
- ✅ Nhanh (5 phút)
- ✅ Sau này dùng được cho mọi project Java

## ⚠️ Lưu ý:

- Lần đầu chạy `mvn javafx:run` sẽ tải thêm ~50MB dependencies (JavaFX, SQLite...)
- Nhưng chỉ tải 1 lần, sau đó không tải nữa
- Tổng cộng: ~60MB (rất nhẹ so với IntelliJ ~2-3GB!)




