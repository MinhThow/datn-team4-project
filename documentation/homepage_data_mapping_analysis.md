# Phân tích & Mapping Dữ liệu Động cho Homepage (UPDATED)

## Tổng quan
Tài liệu này phân tích template `index.html` để xác định các block dữ liệu động và mapping với database schema **THỰC TẾ**.

## ⚠️ Quan trọng: Cập nhật theo SQL Schema
Sau khi đọc kỹ `SQLDATN.sql`, tôi đã cập nhật lại các cấu trúc dữ liệu để khớp với schema thực tế.

## 1. Hero Section (Banner Slider) - trước mắt hardcode
**Vị trí**: Lines 132-178 trong `index.html`  
**Mô tả**: Slider banner chính ở đầu trang

### ⚠️ Vấn đề phát hiện:
- **Bảng `Sales` KHÔNG có field `Banner`** trong schema thực tế!
- Chỉ có: `SaleID`, `Name`, `Description`, `StartDate`, `EndDate`

### Giải pháp đề xuất:
**Option 1**: Thêm field `Banner` vào bảng `Sales`:
```sql
ALTER TABLE Sales ADD Banner NVARCHAR(255);
```

**Option 2**: Hardcode banner images trong code, mapping theo `SaleID`:
```java
// Trong service layer
Map<Integer, String> saleBanners = Map.of(
    1, "img/hero/hero-1.jpg",
    2, "img/hero/hero-2.jpg"
);
```

### Cấu trúc dữ liệu đề xuất (Updated):
```json
{
  "heroBanners": [
    {
      "saleID": 1,
      "name": "Sale Hè 2024",
      "description": "Giảm giá đặc biệt cho mùa hè", 
      "bannerImage": "img/hero/hero-1.jpg", // Cần thêm field hoặc hardcode
      "startDate": "2024-06-01T00:00:00",
      "endDate": "2024-06-30T23:59:59",
      "ctaText": "Shop now",
      "ctaLink": "/shop?sale=1"
    }
  ]
}
```

## 2. Banner Section (Category Banners) - trước mắt hardcode
**Vị trí**: Lines 181-217 trong `index.html`  
**Mô tả**: 3 banner nhỏ quảng cáo các danh mục sản phẩm

### Database Mapping (Updated):
- **Bảng chính**: `Categories` (chỉ có `CategoryID`, `Name`, `Description`)
- **Vấn đề**: Không có field cho banner image

### Giải pháp:
Hardcode mapping trong code:
```java
Map<Integer, CategoryBanner> categoryBanners = Map.of(
    1, new CategoryBanner("Clothing Collections 2030", "img/banner/banner-1.jpg"),
    3, new CategoryBanner("Shoes Spring 2030", "img/banner/banner-3.jpg"),
    4, new CategoryBanner("Accessories", "img/banner/banner-2.jpg")
);
```

### Cấu trúc dữ liệu đề xuất (Updated):
```json
{
  "categoryBanners": [
    {
      "categoryID": 1,
      "name": "Áo", // Từ Categories.Name
      "displayName": "Clothing Collections 2030", // Hardcode
      "description": "Các loại áo thời trang nam", // Từ Categories.Description
      "bannerImage": "img/banner/banner-1.jpg", // Hardcode
      "ctaLink": "/shop?category=1"
    }
  ]
}
```

## 3. Product Section (Main Products) - CORRECTED
**Vị trí**: Lines 220-520 trong `index.html`  
**Mô tả**: Danh sách sản phẩm chính với filter (Best Sellers, New Arrivals, Hot Sales)

### Database Mapping (Corrected):
- **Bảng chính**: `Products` (`ProductID`, `Name`, `Description`, `Price`, `Stock`, `Image`, `CategoryID`, `Size`)
- **Bảng join**: `SaleDetails` (để lấy `SalePrice`)
- **Bảng join**: `Reviews` (để tính rating trung bình)
- **Bảng join**: `OrderItems` (để tính best sellers)

### ⚠️ Vấn đề phát hiện:
1. **Không có field `colors`** trong `Products` table
2. **`SaleDetails` có `SalePrice`** (không phải discount percentage)
3. **Cần tính toán `discountPercent`** = `(Price - SalePrice) / Price * 100`

### Cấu trúc dữ liệu đề xuất (Corrected):
```json
{
  "products": {
    "bestSellers": [
      {
        "productID": 1, // Khớp với SQL
        "name": "Áo thun basic", // Products.Name
        "description": "Áo thun cotton co giãn", // Products.Description
        "price": 199000, // Products.Price (original)
        "salePrice": 159000, // SaleDetails.SalePrice (nếu có)
        "hasDiscount": true, // Calculated
        "discountPercent": 20, // Calculated: (199000-159000)/199000*100
        "stock": 50, // Products.Stock
        "image": "product-1.jpg", // Products.Image
        "categoryID": 1, // Products.CategoryID
        "size": "M", // Products.Size
        "rating": 4.5, // Calculated từ Reviews
        "totalReviews": 25, // Count từ Reviews
        "isNew": false, // Logic: ProductID trong top 10 mới nhất
        "isSale": true // Logic: Có trong SaleDetails và sale active
      }
    ],
    "newArrivals": [...],
    "hotSales": [...]
  }
}
```

## 4. Categories Section (Deal of the Week) - CORRECTED - trước mắt hardcode
**Vị trí**: Lines 523-560 trong `index.html`  
**Mô tả**: Sản phẩm deal đặc biệt với countdown timer

### Database Mapping (Corrected):
```sql
SELECT s.SaleID, s.Name, s.Description, s.EndDate,
       p.ProductID, p.Name as ProductName, p.Price, p.Image,
       sd.SalePrice,
       (p.Price - sd.SalePrice) as Discount,
       ((p.Price - sd.SalePrice) / p.Price * 100) as DiscountPercent
FROM Sales s
JOIN SaleDetails sd ON s.SaleID = sd.SaleID  
JOIN Products p ON sd.ProductID = p.ProductID
WHERE s.EndDate > GETDATE()
ORDER BY DiscountPercent DESC
```

### Cấu trúc dữ liệu đề xuất (Corrected):
```json
{
  "dealOfWeek": {
    "saleID": 2, // Sales.SaleID
    "saleName": "Flash Sale 7.7", // Sales.Name
    "productID": 4, // Products.ProductID
    "productName": "Giày sneaker trắng", // Products.Name
    "originalPrice": 599000, // Products.Price
    "salePrice": 499000, // SaleDetails.SalePrice
    "discount": 100000, // Calculated
    "discountPercent": 17, // Calculated
    "productImage": "product-4.jpg", // Products.Image (không phải product-sale.png)
    "endDate": "2024-07-07T23:59:59", // Sales.EndDate
    "timeRemaining": {
      "days": 3,
      "hours": 1, 
      "minutes": 50,
      "seconds": 18
    }
  }
}
```

## 5. Latest Blog Section - CHƯA CÓ TRONG SCHEMA - trước mắt hardcode
**Vị trí**: Lines 586-629 trong `index.html`  
**Mô tả**: 3 bài blog mới nhất

### ⚠️ Vấn đề:
Schema hiện tại **KHÔNG có bảng Blog**!

### Giải pháp:
**Option 1**: Thêm bảng Blogs vào schema:
```sql
CREATE TABLE Blogs (
    BlogID INT PRIMARY KEY IDENTITY(1,1),
    Title NVARCHAR(255) NOT NULL,
    Content NVARCHAR(MAX),
    Image NVARCHAR(255),
    CreatedAt DATETIME DEFAULT GETDATE(),
    Status NVARCHAR(20) DEFAULT 'published'
);
```

**Option 2**: Bỏ qua section này trong Phase 1, hardcode data tạm.

## 6. Search Functionality - OK
**Đã đúng** - sử dụng `Products.Name` và `Products.Description`

## 7. Shopping Cart Info (Header) - CORRECTED - trước mắt hardcode
### Database Mapping (Corrected):
```sql
SELECT COUNT(*) as TotalItems,
       SUM(CASE 
           WHEN sd.SalePrice IS NOT NULL THEN sd.SalePrice * ci.Quantity
           ELSE p.Price * ci.Quantity 
       END) as TotalAmount
FROM CartItems ci
JOIN Products p ON ci.ProductID = p.ProductID
LEFT JOIN SaleDetails sd ON p.ProductID = sd.ProductID
LEFT JOIN Sales s ON sd.SaleID = s.SaleID 
    AND s.StartDate <= GETDATE() 
    AND s.EndDate >= GETDATE()
WHERE ci.UserID = ?
```

## Tổng hợp Vấn đề và Giải pháp

### Vấn đề phát hiện:
1. ❌ `Sales` table thiếu field `Banner`
2. ❌ `Categories` table thiếu field banner image
3. ❌ `Products` table thiếu field `colors`
4. ❌ Chưa có `Blogs` table
5. ✅ `SaleDetails` có `SalePrice` (đúng)
6. ✅ Các bảng khác đã đúng

### Giải pháp ngắn hạn (Phase 1):
1. **Hardcode banner images** trong service layer
2. **Bỏ qua colors** (không hiển thị)
3. **Bỏ qua blog section** 
4. **Tính toán discount** từ `Price` và `SalePrice`

### Giải pháp dài hạn:
1. **ALTER TABLE** để thêm các field cần thiết
2. **CREATE TABLE Blogs**
3. **Thêm bảng ProductColors** nếu cần

## SQL Queries cần thiết

### 1. Get Products with Sale Info:
```sql
SELECT p.ProductID, p.Name, p.Description, p.Price, p.Stock, p.Image, p.CategoryID, p.Size,
       sd.SalePrice,
       CASE WHEN sd.SalePrice IS NOT NULL THEN 
           CAST((p.Price - sd.SalePrice) * 100.0 / p.Price AS INT)
       ELSE 0 END as DiscountPercent,
       AVG(r.Rating) as AverageRating,
       COUNT(r.ReviewID) as TotalReviews
FROM Products p
LEFT JOIN SaleDetails sd ON p.ProductID = sd.ProductID
LEFT JOIN Sales s ON sd.SaleID = s.SaleID 
    AND s.StartDate <= GETDATE() 
    AND s.EndDate >= GETDATE()
LEFT JOIN Reviews r ON p.ProductID = r.ProductID
WHERE p.Stock > 0
GROUP BY p.ProductID, p.Name, p.Description, p.Price, p.Stock, p.Image, p.CategoryID, p.Size, sd.SalePrice
```

### 2. Get Best Sellers:
```sql
SELECT TOP 8 p.*, SUM(oi.Quantity) as TotalSold
FROM Products p
JOIN OrderItems oi ON p.ProductID = oi.ProductID
JOIN Orders o ON oi.OrderID = o.OrderID
WHERE o.Status IN ('Đã giao')
GROUP BY p.ProductID, p.Name, p.Description, p.Price, p.Stock, p.Image, p.CategoryID, p.Size
ORDER BY TotalSold DESC
```

### 3. Get Active Sales for Banners:
```sql
SELECT SaleID, Name, Description, StartDate, EndDate
FROM Sales
WHERE StartDate <= GETDATE() AND EndDate >= GETDATE()
ORDER BY SaleID
```

## Tổng hợp API Endpoints cần thiết

### 1. Homepage Data API
```
GET /api/homepage
Response: {
  "heroBanners": [...],
  "categoryBanners": [...], 
  "products": {
    "bestSellers": [...],
    "newArrivals": [...],
    "hotSales": [...]
  },
  "dealOfWeek": {...},
  "latestBlogs": [...]
}
```

### 2. Product Search API
```
GET /api/products/search?keyword={keyword}&category={categoryId}&page={page}&size={size}
Response: {
  "content": [...],
  "totalElements": 150,
  "totalPages": 15,
  "currentPage": 0
}
```

### 3. Cart Info API  
```
GET /api/cart/summary
Response: {
  "totalItems": 3,
  "totalAmount": 597000
}
```

## Ưu tiên Implementation

### Phase 1 (Cao):
1. **Product API** - Cần thiết cho main content
2. **Search API** - Tính năng cốt lõi
3. **Cart Summary API** - UX quan trọng

### Phase 2 (Trung bình):
1. **Banner/Sale API** - Tính năng marketing
2. **Deal of Week API** - Tính năng đặc biệt

### Phase 3 (Thấp):
1. **Blog API** - Cần tạo bảng mới
2. **Category Banner API** - Có thể hardcode tạm

## Ghi chú đặc biệt

1. **Sale Price Logic**: 
   - Nếu sản phẩm có trong `SaleDetails` và sale đang active → hiển thị `SalePrice`
   - Ngược lại → hiển thị `Price` gốc

2. **Rating Calculation**:
   - Tính từ bảng `Reviews`: `AVG(Rating)` 
   - Hiển thị số sao và tổng số review

3. **Stock Management**:
   - Kiểm tra `Stock > 0` trước khi hiển thị sản phẩm
   - Hiển thị "Out of Stock" nếu hết hàng

4. **Image Handling**:
   - Đường dẫn ảnh trong DB chỉ lưu tên file
   - Frontend cần prefix với `/img/product/` hoặc CDN URL

5. **Responsive Design**:
   - Template đã responsive
   - API cần support pagination cho mobile (size nhỏ hơn)