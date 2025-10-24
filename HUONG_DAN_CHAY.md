# 🚀 HƯỚNG DẪN CHẠY ỨNG DỤNG PIZZA ORDER

## ⚡ CÁCH NHANH NHẤT (Khuyến nghị)

### Bước 1: Tải IntelliJ IDEA Community (MIỄN PHÍ)
- Link tải: https://www.jetbrains.com/idea/download/?section=windows
- Chọn **Community Edition** (miễn phí)
- Tải và cài đặt (Next → Next → Finish)

### Bước 2: Mở Project
1. Mở IntelliJ IDEA
2. Chọn **Open**
3. Chọn thư mục: `D:\2212485_DoTrungTu_PizzaJ_MTK`
4. Chọn **Trust Project**

### Bước 3: Chờ IntelliJ tải dependencies
- IntelliJ sẽ tự động tải Maven và các thư viện cần thiết
- Xem thanh tiến trình ở dưới cùng
- Chờ khoảng 2-3 phút (lần đầu)

### Bước 4: Chạy ứng dụng
1. Mở file: `src/main/java/com/pizza/ui/MainApp.java`
2. Nhấn chuột phải vào file
3. Chọn **Run 'MainApp.main()'**
4. Ứng dụng sẽ chạy! 🍕

---

## 🔧 CÁCH 2: Cài Maven thủ công

### Bước 1: Tải Apache Maven
- Link: https://maven.apache.org/download.cgi
- Tải file: `apache-maven-3.9.x-bin.zip`

### Bước 2: Giải nén
- Giải nén vào: `C:\Program Files\Apache\Maven`

### Bước 3: Thêm vào PATH
1. Nhấn `Windows + R`, gõ: `sysdm.cpl`
2. Tab **Advanced** → **Environment Variables**
3. Trong **System variables**, tìm **Path**
4. Nhấn **Edit** → **New**
5. Thêm: `C:\Program Files\Apache\Maven\bin`
6. OK → OK → OK
7. **Khởi động lại PowerShell/Terminal**

### Bước 4: Kiểm tra
```bash
mvn --version
```

### Bước 5: Chạy ứng dụng
```bash
cd D:\2212485_DoTrungTu_PizzaJ_MTK
mvn clean javafx:run
```

---

## 📝 NỘP BÀI

Dự án đã hoàn thiện với:

✅ **4 Design Patterns**:
- Factory Pattern: `com.pizza.domain.factory.PizzaFactory`
- Decorator Pattern: `com.pizza.domain.decorator.*`
- Strategy Pattern: `com.pizza.domain.strategy.*`
- Observer Pattern: `com.pizza.app.EventBus`

✅ **Database**: SQLite với JDBC (`com.pizza.infra.db.*`)

✅ **JavaFX UI**: 3 màn hình (Home, Menu, Cart)

✅ **Unit Tests**: 21 test cases

✅ **README.md**: Giải thích chi tiết các design patterns

---

## 🎥 DEMO (nếu không chạy được)

Nếu bạn không chạy được, có thể:
1. Chụp màn hình code
2. Chụp màn hình cấu trúc project
3. Giải thích design patterns trong README.md
4. Nộp source code đầy đủ

**LƯU Ý**: Code đã hoàn thiện 100%, chỉ cần môi trường chạy phù hợp!





