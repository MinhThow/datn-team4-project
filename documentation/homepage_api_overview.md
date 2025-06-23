# Tổng hợp chức năng/API động cho trang chủ (Homepage)

Dưới đây là các chức năng động cần thiết để xây dựng trang chủ web bán thời trang, tập trung vào các phần sẽ làm bằng database/API (không liệt kê các phần hard code):

## 1. Thông tin user
- Đăng nhập, đăng ký, lấy thông tin user
- **API:**
  - `GET /api/user/info` — Lấy thông tin user (nếu đã đăng nhập)

## 2. Danh mục sản phẩm
- Hiển thị danh mục sản phẩm cho menu, filter
- **API:**
  - `GET /api/categories` — Danh mục sản phẩm

## 3. Sản phẩm
- Hiển thị tất cả sản phẩm, phân trang, filter theo danh mục
- **API:**
  - `GET /api/products?page=1&size=12` — Danh sách sản phẩm, có phân trang
  - `GET /api/products?category={id}` — Lọc sản phẩm theo danh mục
  - `GET /api/products/{id}` — Chi tiết sản phẩm

## 4. Tìm kiếm sản phẩm
- Tìm kiếm theo tên, mô tả
- **API:**
  - `GET /api/products?search=keyword` — Tìm kiếm sản phẩm

## 5. Giỏ hàng
- Thêm, sửa, xóa sản phẩm trong giỏ, xem tổng số sản phẩm
- **API:**
  - `GET /api/cart/summary` — Tổng số sản phẩm trong giỏ
  - `POST /api/cart/add` — Thêm sản phẩm vào giỏ
  - `POST /api/cart/update` — Cập nhật số lượng
  - `POST /api/cart/remove` — Xóa sản phẩm khỏi giỏ

## 6. Sản phẩm sale
- Hiển thị sản phẩm đang khuyến mãi, nhiều chương trình sale động
- **API:**
  - `GET /api/products/sale` — Danh sách sản phẩm đang sale (JOIN bảng Sales/SaleDetails)

---

**Lưu ý:**
- Các chức năng như banner, blog, instagram, newsletter, settings... sẽ hard code hoặc không làm ở giai đoạn này nên không liệt kê ở đây.
- Nếu cần bổ sung API động nào khác, hãy cập nhật thêm vào danh sách này. 