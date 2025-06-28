---
title: "Phân Tích Chi Tiết Mapping Dữ Liệu Homepage - Dựa Trên Source Code Thực Tế"
type: analysis
created: 2025-01-27T16:30:00
updated: 2025-01-27T16:30:00
priority: high
---

# Phân Tích Chi Tiết Mapping Dữ Liệu Homepage

## 1. Cấu Trúc Database Thực Tế (Từ SQLDATN.sql)

### Products Table:
```sql
CREATE TABLE Products (
    ProductID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(150) NOT NULL,
    Description NVARCHAR(MAX),
    Price DECIMAL(10,2) NOT NULL,
    Stock INT DEFAULT 0,
    Image NVARCHAR(255),
    CategoryID INT,
    Size NVARCHAR(50),  -- Đã được thêm
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID)
);
```

### Categories Table:
```sql
CREATE TABLE Categories (
    CategoryID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(100) NOT NULL,
    Description NVARCHAR(MAX)
);
```

### Dữ Liệu Mẫu Hiện Tại:
**Categories (Thời Trang):**
- Áo nam
- Áo nữ  
- Quần nam
- Quần nữ
- Phụ kiện

**Products (10 sản phẩm thời trang):**
- Áo Sơ Mi Nam Trắng Oxford (450,000 VND)
- Áo Thun Nữ Oversize Basic (280,000 VND)
- Quần Jean Nam Slim Fit (650,000 VND)
- Chân Váy Nữ Xòe Midi (380,000 VND)
- Túi Xách Nữ Da Thật (1,200,000 VND)
- Áo Khoác Bomber Nam (750,000 VND)
- Đầm Maxi Nữ Hoa Nhí (590,000 VND)
- Quần Short Nam Kaki (320,000 VND)
- Giày Sneaker Nữ Trắng (890,000 VND)
- Áo Len Nữ Cổ Lọ (480,000 VND)

## 2. Cấu Trúc API Hiện Tại

### ProductController (/api/products):
```java
@GetMapping                              // GET /api/products - Lấy tất cả sản phẩm
@GetMapping("/{id}")                     // GET /api/products/{id} - Lấy 1 sản phẩm
@PostMapping                             // POST /api/products - Tạo sản phẩm
@PutMapping("/{id}")                     // PUT /api/products/{id} - Cập nhật
@DeleteMapping("/{id}")                  // DELETE /api/products/{id} - Xóa
```

### ProductDTO (Dữ liệu trả về):
```java
public class ProductDTO {
    private Integer productID;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String image;
    private String size;
    private Integer categoryID;
    private String categoryName;  // Được map từ Category entity
}
```

### ProductService Hiện Tại:
```java
public interface ProductService {
    List<ProductDTO> getAllProducts();        // ✅ Có sẵn
    ProductDTO getProductById(Integer id);    // ✅ Có sẵn
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(Integer id, ProductDTO productDTO);
    void deleteProduct(Integer id);
}
```

### ProductRepository:
```java
@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    // Chỉ có basic CRUD, CHƯA CÓ custom methods
}
```

## 3. Phân Tích Thiếu Hụt Cho Homepage

### 3.1 API Endpoints Cần Thêm:

**ProductController cần bổ sung:**
```java
@GetMapping("/best-sellers")          // ❌ THIẾU - Sản phẩm bán chạy
@GetMapping("/new-arrivals")          // ❌ THIẾU - Sản phẩm mới
@GetMapping("/hot-sales")             // ❌ THIẾU - Sản phẩm giảm giá
@GetMapping("/featured")              // ❌ THIẾU - Sản phẩm nổi bật
@GetMapping("/by-category/{categoryId}") // ❌ THIẾU - Sản phẩm theo danh mục
```

### 3.2 Service Methods Cần Thêm:

**ProductService cần bổ sung:**
```java
List<ProductDTO> getBestSellers(int limit);     // ❌ THIẾU
List<ProductDTO> getNewArrivals(int limit);     // ❌ THIẾU  
List<ProductDTO> getHotSales(int limit);        // ❌ THIẾU
List<ProductDTO> getFeaturedProducts(int limit); // ❌ THIẾU
List<ProductDTO> getProductsByCategory(Integer categoryId); // ❌ THIẾU
```

### 3.3 Repository Methods Cần Thêm:

**ProductRepository cần bổ sung:**
```java
// Sản phẩm bán chạy (dựa vào OrderItems)
@Query("SELECT p FROM Product p JOIN OrderItem oi ON p.productID = oi.product.productID " +
       "GROUP BY p ORDER BY SUM(oi.quantity) DESC")
List<Product> findBestSellers(Pageable pageable);

// Sản phẩm mới (dựa vào thời gian tạo - cần thêm createdAt vào Product)
List<Product> findByOrderByCreatedAtDesc(Pageable pageable);

// Sản phẩm theo danh mục
List<Product> findByCategoryCategoryID(Integer categoryId);

// Sản phẩm có stock > 0
List<Product> findByStockGreaterThan(Integer stock);
```

### 3.4 Database Schema Cần Bổ Sung:

**Products table thiếu:**
```sql
ALTER TABLE Products ADD CreatedAt DATETIME DEFAULT GETDATE();
ALTER TABLE Products ADD IsHotSale BIT DEFAULT 0;
ALTER TABLE Products ADD IsFeatured BIT DEFAULT 0;
ALTER TABLE Products ADD SortOrder INT DEFAULT 0;
```

## 4. Mapping Dữ Liệu Lên Template

### 4.1 Navigation Menu (Lines 99-113 index.html):
```html
<ul class="header__menu__dropdown">
    <!-- Cần categories từ CategoryService.getAllCategories() -->
    <li th:each="category : ${categories}">
        <a th:href="@{/shop(category=${category.categoryID})}" 
           th:text="${category.name}"></a>
    </li>
</ul>
```

### 4.2 Cart Info (Lines 52, 121):
```html
<span th:text="${cartItemCount ?: 0}">0</span>
<span th:text="${#numbers.formatDecimal(cartTotalPrice ?: 0, 0, 'COMMA', 0, 'POINT')}">$0.00</span>
```
**❌ THIẾU:** CartService methods để tính cartItemCount và cartTotalPrice

### 4.3 Product Showcase (Lines 233-512):
```html
<!-- Best Sellers -->
<div th:if="${activeFilter == 'best-sellers' or activeFilter == null}">
    <div th:each="product : ${bestSellers}" class="col-lg-3 col-md-6 col-sm-6">
        <!-- Product item template -->
    </div>
</div>

<!-- New Arrivals -->  
<div th:if="${activeFilter == 'new-arrivals'}">
    <div th:each="product : ${newArrivals}" class="col-lg-3 col-md-6 col-sm-6">
        <!-- Product item template -->
    </div>
</div>

<!-- Hot Sales -->
<div th:if="${activeFilter == 'hot-sales'}">
    <div th:each="product : ${hotSales}" class="col-lg-3 col-md-6 col-sm-6">
        <!-- Product item template -->
    </div>
</div>
```

### 4.4 Deal of the Week (Lines 534-556):
```html
<div class="product__discount__item">
    <div th:if="${dealProduct != null}">
        <img th:src="@{'/img/product/' + ${dealProduct.image}}" alt="">
        <h5 th:text="${dealProduct.name}">Product Name</h5>
        <div class="product__item__price">
            <span th:text="${#numbers.formatDecimal(dealProduct.price, 0, 'COMMA', 0, 'POINT')} + ' VND'">$0</span>
        </div>
    </div>
</div>
```

## 5. HomeController Model Attributes Cần Thiết

```java
@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        // Products
        model.addAttribute("bestSellers", productService.getBestSellers(8));      // ❌ THIẾU method
        model.addAttribute("newArrivals", productService.getNewArrivals(8));      // ❌ THIẾU method  
        model.addAttribute("hotSales", productService.getHotSales(8));            // ❌ THIẾU method
        model.addAttribute("dealProduct", productService.getFeaturedProducts(1)); // ❌ THIẾU method
        
        // Categories
        model.addAttribute("categories", categoryService.getAllCategories());     // ✅ Có sẵn
        
        // Cart info (nếu user đã login)
        User currentUser = getCurrentUser(session);
        if (currentUser != null) {
            model.addAttribute("cartItemCount", cartService.getCartItemCount(currentUser.getUserID())); // ❌ THIẾU service
            model.addAttribute("cartTotalPrice", cartService.getCartTotal(currentUser.getUserID()));    // ❌ THIẾU service
        } else {
            model.addAttribute("cartItemCount", 0);
            model.addAttribute("cartTotalPrice", BigDecimal.ZERO);
        }
        
        return "index";
    }
}
```

## 6. Tóm Tắt Công Việc Cần Làm

### Ưu tiên CAO:
1. **Tạo HomeController** - Cần ngay để hiển thị trang chủ
2. **Bổ sung ProductService methods** - getBestSellers, getNewArrivals, getHotSales
3. **Bổ sung ProductRepository queries** - Custom queries cho filtering
4. **Tạo CartService** - Tính toán cart info

### Ưu tiên TRUNG BÌNH:
1. **Bổ sung database schema** - CreatedAt, IsHotSale, IsFeatured columns
2. **Cập nhật template** - Thymeleaf integration
3. **Product filtering logic** - JavaScript/Ajax cho filter buttons

### Ưu tiên THẤP:
1. **Deal countdown timer** - JavaScript countdown
2. **Blog integration** - Nếu có blog system
3. **Search functionality** - Autocomplete search

## 7. Dữ Liệu Test Hiện Tại

Với 10 sản phẩm thời trang hiện có, có thể tạo logic phân loại tạm thời:
- **Best Sellers**: Áo Sơ Mi Nam Trắng Oxford, Áo Thun Nữ Oversize Basic, Quần Jean Nam Slim Fit (sản phẩm cơ bản)
- **New Arrivals**: Áo Khoác Bomber Nam, Đầm Maxi Nữ Hoa Nhí, Áo Len Nữ Cổ Lọ (sản phẩm thời trang mới)
- **Hot Sales**: Chân Váy Nữ Xòe Midi, Quần Short Nam Kaki (giá phải chăng)
- **Featured Deal**: Túi Xách Nữ Da Thật (sản phẩm cao cấp nhất)

## 8. Next Steps

1. Tạo HomeController với các model attributes cơ bản
2. Implement các service methods cần thiết
3. Test với dữ liệu hiện có
4. Cập nhật template với Thymeleaf
5. Bổ sung database schema nếu cần 