### **TASK-HP-001: Thiết kế và Triển khai API & Luồng Dữ liệu Trang chủ (chuẩn hóa theo SQL)**

- **Mô tả**: Xây dựng các API và controller để cung cấp dữ liệu động cho trang chủ website bán thời trang. Trang chủ sẽ hiển thị sản phẩm nổi bật/mới nhất (có phân trang, giá sale nếu có), danh mục sản phẩm, banner/slider (lấy từ bảng Sales), và hỗ trợ tìm kiếm nhanh. Kết nối dữ liệu này với template `index.html` bằng Thymeleaf.

- **Mục tiêu**:
    - Hiển thị danh sách sản phẩm nổi bật hoặc mới nhất (có phân trang, có trường giá sale nếu có).
    - Hiển thị danh mục sản phẩm để người dùng lọc nhanh.
    - Hiển thị banner/slider quảng cáo (lấy từ bảng Sales).
    - Hỗ trợ tìm kiếm sản phẩm theo tên/mô tả ngay trên trang chủ.
    - Kết nối dữ liệu động vào template `index.html`.
    - Đảm bảo API backend rõ ràng, dễ mở rộng, đúng với database thực tế.

- **Tiêu chuẩn Hoàn thành (Definition of Done)**:
    - **API Documentation**: Có tài liệu mô tả rõ ràng endpoint, tham số, cấu trúc JSON đầu ra.
    - **Controller Implementation**: Controller trả về dữ liệu động cho template `index.html`.
    - **Service Layer Logic**: Service xử lý logic lấy sản phẩm mới nhất/nổi bật, danh mục, banner, giá sale.
    - **Repository Layer**: Repository truy vấn dữ liệu phù hợp, join SaleDetails nếu cần.
    - **Thymeleaf Integration**: Dữ liệu được render động vào template.
    - **Testing**: Kiểm thử hiển thị dữ liệu trên giao diện.
*   **Java:**
    * Tuân theo Quy ước Mã Java của Oracle.
    * Sử dụng PascalCase cho **tên lớp**.
    * Sử dụng camelCase cho **tên phương thức**, **tên trường** và **biến cục bộ**.
    * Sử dụng tên rõ ràng, mô tả và nhất quán.
    * Sử dụng `CompletableFuture` cho các thao tác bất đồng bộ.

*   **Quy ước Đặt tên:**
    *   Tên lớp: PascalCase
    *   Tên phương thức/trường/biến: camelCase
    *   Sử dụng tên rõ ràng, mô tả và nhất quán

### **Thiết kế chi tiết API & Dữ liệu (chuẩn hóa theo SQL)**

1.  **API lấy sản phẩm trang chủ (có phân trang, giá sale nếu có)**
    -   **Method**: `GET`
    -   **URL**: `/api/v1/products/home`
    -   **Request Parameters**:
        | Tên tham số | Kiểu dữ liệu | Bắt buộc | Mặc định | Mô tả |
        |-------------|--------------|----------|----------|------------------------------------------|
        | `page`      | `int`        | Không    | `0`      | Số trang (bắt đầu từ 0)                  |
        | `size`      | `int`        | Không    | `8`      | Số lượng sản phẩm trên mỗi trang         |
    -   **Response**:
        ```json
        {
          "products": [
            {
              "productID": 1,
              "name": "Áo thun basic",
              "description": "Áo thun cotton co giãn",
              "price": 199000,
              "salePrice": 159000,   // null nếu không có sale
              "stock": 50,
              "image": "product-1.jpg",
              "categoryID": 1,
              "size": "M"
            }
          ],
          "page": 0,
          "size": 8,
          "totalPages": 2,
          "totalElements": 16
        }
        ```
    -   **Ghi chú**: Nếu sản phẩm có sale (join với SaleDetails), trả về cả `salePrice`.

2.  **API lấy danh mục sản phẩm**
    -   **Method**: `GET`
    -   **URL**: `/api/v1/categories`
    -   **Response**:
        ```json
        {
          "categories": [
            { "categoryID": 1, "name": "Áo", "description": "Các loại áo thời trang nam" },
            { "categoryID": 2, "name": "Quần", "description": "Các loại quần thời trang nam" }
          ]
        }
        ```

3.  **API lấy banner/slider (lấy từ bảng Sales)**
    -   **Method**: `GET`
    -   **URL**: `/api/v1/banners`
    -   **Response**:
        ```json
        {
          "banners": [
            { "image": "banner-1.jpg", "title": "Sale Hè 2024", "description": "Giảm giá đặc biệt cho mùa hè", "link": "/sale/1" },
            { "image": "banner-2.jpg", "title": "Flash Sale 7.7", "description": "Giảm giá sốc trong ngày 7/7", "link": "/sale/2" }
          ]
        }
        ```
    -   **Ghi chú**: Có thể lấy trường `Name`, `Description`, `SaleID` từ bảng `Sales` để làm banner động.

4.  **API tìm kiếm sản phẩm**
    -   **Method**: `GET`
    -   **URL**: `/api/v1/products/search`
    -   **Request Parameters**:
        | Tên tham số | Kiểu dữ liệu | Bắt buộc | Mặc định | Mô tả |
        |-------------|--------------|----------|----------|------------------------------------------|
        | `keyword`   | `String`     | Có       |          | Từ khóa tìm kiếm theo tên/mô tả          |
        | `page`      | `int`        | Không    | `0`      | Số trang                                |
        | `size`      | `int`        | Không    | `8`      | Số lượng sản phẩm trên mỗi trang         |
    -   **Response**: giống API sản phẩm trang chủ

### **Các bước triển khai Backend & Frontend**

1. **Phân tích template `index.html`** để xác định các block dữ liệu động cần render.
2. **Tạo các API trên** (có thể dùng chung service/repository với các API khác, join SaleDetails nếu cần).
3. **Tạo controller trả về dữ liệu động cho template `index.html`** (Spring Controller, không phải RestController).
4. **Sử dụng Thymeleaf để render dữ liệu vào template**.
5. **Kiểm thử hiển thị dữ liệu trên giao diện**.

### **Ghi chú**
- Đảm bảo các trường dữ liệu trả về đúng với entity và database thực tế.
- Nếu sản phẩm có sale, trả về cả `price` và `salePrice`.
- Banner có thể lấy từ bảng `Sales`.
- Phân trang sản phẩm.
- Tìm kiếm theo tên/mô tả.
- Ưu tiên code rõ ràng, dễ mở rộng, tách biệt từng chức năng. 