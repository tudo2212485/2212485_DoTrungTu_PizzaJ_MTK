# 🔧 HƯỚNG DẪN CÀI MAVEN TRÊN WINDOWS

## Bước 1: Tải Apache Maven

1. Vào: https://maven.apache.org/download.cgi
2. Tìm phần **"Files"**
3. Tải file: **`apache-maven-3.9.6-bin.zip`** (Binary zip archive)

## Bước 2: Giải nén Maven

1. **Giải nén** file vừa tải
2. **Chép** thư mục `apache-maven-3.9.6` vào: `C:\Program Files\`
3. Đổi tên thành: `C:\Program Files\Maven` (cho dễ nhớ)

## Bước 3: Thêm Maven vào PATH

### Cách 1: Qua Settings

1. Nhấn `Windows + R`
2. Gõ: `sysdm.cpl`
3. Enter
4. Tab **"Advanced"** → Nhấn **"Environment Variables"**
5. Trong phần **"System variables"** (bảng dưới):
   - Tìm biến **"Path"**
   - Nhấn **"Edit"**
   - Nhấn **"New"**
   - Thêm: `C:\Program Files\Maven\bin`
   - OK → OK → OK

### Cách 2: Qua PowerShell (Admin)

```powershell
# Mở PowerShell as Administrator
# Copy và paste lệnh này:
[Environment]::SetEnvironmentVariable("Path", $env:Path + ";C:\Program Files\Maven\bin", "Machine")
```

## Bước 4: Tạo biến MAVEN_HOME (Tùy chọn nhưng khuyến nghị)

1. Trong **Environment Variables**
2. Phần **System variables** → Nhấn **"New"**
3. Variable name: `MAVEN_HOME`
4. Variable value: `C:\Program Files\Maven`
5. OK

## Bước 5: Kiểm tra Java

Maven cần Java. Kiểm tra:

```powershell
java -version
```

Nếu thấy:
```
java version "19.0.2"
```
→ OK! ✅

## Bước 6: KHỞI ĐỘNG LẠI PowerShell

**QUAN TRỌNG**: 
- Đóng TẤT CẢ cửa sổ PowerShell/Terminal
- Đóng VS Code/Cursor
- Mở lại VS Code/Cursor
- Mở Terminal mới

## Bước 7: Kiểm tra Maven

```powershell
mvn --version
```

Nếu thấy:
```
Apache Maven 3.9.6
Maven home: C:\Program Files\Maven
Java version: 19.0.2
```
→ THÀNH CÔNG! ✅

## Bước 8: Chạy ứng dụng

```bash
cd D:\2212485_DoTrungTu_PizzaJ_MTK
mvn clean javafx:run
```

---

## ⚠️ Xử Lý Lỗi

### Lỗi: "mvn is not recognized" (sau khi cài)

**Nguyên nhân**: Chưa restart terminal

**Giải pháp**:
1. Đóng HẾT terminal/VS Code
2. Mở lại
3. Thử lại

### Lỗi: "JAVA_HOME is not set"

**Giải pháp**:
1. Environment Variables
2. System variables → New
3. Variable name: `JAVA_HOME`
4. Variable value: `C:\Program Files\Java\jdk-19` (tùy đường dẫn Java của bạn)
5. OK

Để tìm đường dẫn Java:
```powershell
where java
```

### Lỗi: "The JAVA_HOME environment variable is not defined correctly"

**Giải pháp**:
- Đảm bảo JAVA_HOME trỏ đến thư mục JDK, KHÔNG phải JRE
- Ví dụ đúng: `C:\Program Files\Java\jdk-19`
- Ví dụ sai: `C:\Program Files\Java\jdk-19\bin`

---

## ✅ Checklist

- [ ] Tải Maven .zip
- [ ] Giải nén vào C:\Program Files\Maven
- [ ] Thêm C:\Program Files\Maven\bin vào PATH
- [ ] (Tùy chọn) Tạo biến MAVEN_HOME
- [ ] Đóng và mở lại Terminal
- [ ] Chạy: mvn --version
- [ ] Thấy thông tin Maven → OK!
- [ ] cd đến project
- [ ] Chạy: mvn clean javafx:run
- [ ] Thấy ứng dụng Pizza Order App 🍕

---

⏱️ **Thời gian**: 10-15 phút (nếu làm lần đầu)

**💡 Lưu ý**: Cách này phức tạp hơn IntelliJ IDEA, nhưng sau khi cài xong, bạn có Maven để dùng cho mọi project Java sau này!




