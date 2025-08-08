-- Tạo cơ sở dữ liệu
IF DB_ID('DATN3') IS NOT NULL
    DROP DATABASE DATN3;
GO

CREATE DATABASE DATN3;
GO

USE DATN3;
GO

-- Xóa bảng nếu tồn tại (theo thứ tự tránh lỗi khóa ngoại)
IF OBJECT_ID('OrderItems', 'U') IS NOT NULL DROP TABLE OrderItems;
IF OBJECT_ID('Orders', 'U') IS NOT NULL DROP TABLE Orders;
IF OBJECT_ID('CartItems', 'U') IS NOT NULL DROP TABLE CartItems;
IF OBJECT_ID('Reviews', 'U') IS NOT NULL DROP TABLE Reviews;
IF OBJECT_ID('ProductImages', 'U') IS NOT NULL DROP TABLE ProductImages;
IF OBJECT_ID('ProductSizes', 'U') IS NOT NULL DROP TABLE ProductSizes;
IF OBJECT_ID('Products', 'U') IS NOT NULL DROP TABLE Products;
IF OBJECT_ID('Categories', 'U') IS NOT NULL DROP TABLE Categories;
IF OBJECT_ID('PaymentMethods', 'U') IS NOT NULL DROP TABLE PaymentMethods;
IF OBJECT_ID('Users', 'U') IS NOT NULL DROP TABLE Users;
GO

-- Bảng người dùng
CREATE TABLE Users (
    UserID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(100),
    Email NVARCHAR(100) UNIQUE,
    Password NVARCHAR(255),
    Phone NVARCHAR(20),
    Address NVARCHAR(MAX),
    Role NVARCHAR(20) DEFAULT 'customer' CHECK (Role IN ('customer', 'admin')),
    CreatedAt DATETIME DEFAULT GETDATE()
);


-----------------------------------------------------------------------------
CREATE TABLE VerificationToken (
    id INT IDENTITY PRIMARY KEY,
    token NVARCHAR(255),
    user_id INT,
    expiryDate DATETIME,
    CONSTRAINT FK_VerificationToken_User FOREIGN KEY (user_id) REFERENCES Users(UserID)
);


-- Danh mục sản phẩm
CREATE TABLE Categories (
    CategoryID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(100) NOT NULL,
    Description NVARCHAR(MAX)
);

-- Sản phẩm
CREATE TABLE Products (
    ProductID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(150) NOT NULL,
    Description NVARCHAR(MAX),
    Price DECIMAL(10,2) NOT NULL,
    CategoryID INT,
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID)
);

-- Size sản phẩm
CREATE TABLE ProductSizes (
    ProductSizeID INT PRIMARY KEY IDENTITY(1,1),
    ProductID INT,
    Size NVARCHAR(50),
    Stock INT DEFAULT 0,
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
);

-- Ảnh sản phẩm
CREATE TABLE ProductImages (
    ImageID INT PRIMARY KEY IDENTITY(1,1),
    ProductID INT,
    ImageUrl NVARCHAR(255),
    IsMain BIT DEFAULT 0, -- 0 = ảnh phụ, 1 = ảnh chính
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
);

-- Phương thức thanh toán
CREATE TABLE PaymentMethods (
    PaymentMethodID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(100) NOT NULL,
    Description NVARCHAR(MAX)
);

-- Đơn hàng
CREATE TABLE Orders (
    OrderID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    RecipientName NVARCHAR(100),
    Phone NVARCHAR(20),
    ShippingAddress NVARCHAR(MAX),
    Note NVARCHAR(MAX),
    Total DECIMAL(10,2),
    Status NVARCHAR(20) DEFAULT 'Chờ xác nhận' CHECK (
        Status IN ('Chờ xác nhận', 'Đang xử lý', 'Đang giao', 'Đã giao', 'Đã hủy', 'Trả hàng')
    ),
    OrderDate DATETIME DEFAULT GETDATE(),
    PaymentMethodID INT,
    PaymentMethodName NVARCHAR(100),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (PaymentMethodID) REFERENCES PaymentMethods(PaymentMethodID)
);

-- Chi tiết đơn hàng
CREATE TABLE OrderItems (
    OrderItemID INT PRIMARY KEY IDENTITY(1,1),
    OrderID INT,
    ProductID INT,
    ProductSizeID INT,
    ProductName NVARCHAR(150),
    Size NVARCHAR(50),
    Quantity INT,
    Price DECIMAL(10,2),
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID),
    FOREIGN KEY (ProductSizeID) REFERENCES ProductSizes(ProductSizeID)
);

-- Giỏ hàng
CREATE TABLE CartItems (
    CartItemID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    ProductID INT,
    ProductSizeID INT,
    Quantity INT DEFAULT 1,
    AddedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID),
    FOREIGN KEY (ProductSizeID) REFERENCES ProductSizes(ProductSizeID)
);

-- Đánh giá
CREATE TABLE Reviews (
    ReviewID INT PRIMARY KEY IDENTITY(1,1),
    ProductID INT,
    UserID INT,
    Rating INT CHECK (Rating BETWEEN 1 AND 5),
    Comment NVARCHAR(MAX),
    ReviewDate DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Tắt tất cả khóa ngoại
EXEC sp_msforeachtable "ALTER TABLE ? NOCHECK CONSTRAINT ALL"

-- Xoá dữ liệu
EXEC sp_msforeachtable "DELETE FROM ?"

-- Bật lại tất cả khóa ngoại
EXEC sp_msforeachtable "ALTER TABLE ? WITH CHECK CHECK CONSTRAINT ALL"

-- Reset ID của bảng Categories
DBCC CHECKIDENT ('Categories', RESEED, 0);

INSERT INTO Categories (Name, Description)
VALUES
(N'Áo Polo', N'Các loại áo polo...'),
(N'Áo Sơ Mi', N'Các loại áo sơ mi...'),
(N'Áo Thun', N'Các loại áo thun'),
(N'Áo TankTop', N'Các loại áo tanktop'),
(N'Áo Khoác', N'Các loại áo khoác'),
(N'Quần Short', N'Các loại quần short'),
(N'Quần Jogger', N'Các loại quần jogger'),
(N'Quần Jean', N'Các loại quần jean');


INSERT INTO Products (Name, Description, Price, CategoryID)
VALUES
-- Áo Polo
(N'Áo Polo Premium Cotton', N'Chất liệu cotton 100%, form regular tôn dáng', 299000, 1),
(N'Áo Polo Pique Cotton', N'Chất liệu cotton 100%, form regular tôn dáng', 259000, 1),
(N'Áo Polo Ice Cooling', N'Chất liệu cotton 100%, form regular tôn dáng', 229000, 1),
(N'Áo Polo Thể Thao', N'Chất liệu cotton 100%, form regular tôn dáng', 199000, 1),

-- Áo Sơ Mi
(N'Áo Sơ Mi Flannel 100% Cotton', N'Chất liệu cotton 100%, thoải mái thoáng mát', 299000, 2),
(N'Áo Sơ Mi Overshirt 100% Cotton', N'Chất liệu cotton 100%, thoải mái thoáng mát', 359000, 2),
(N'Áo Sơ Mi Dài Tay Oxford', N'Chất liệu cotton, thoải mái thoáng mát', 399000, 2),
(N'Áo Sơ Mi Dài Tay Premium', N'Chất liệu cotton, thoải mái thoáng mát', 299000, 2),

-- Áo Thun
(N'Áo Thun 100% Cotton', N'Chất liệu cotton 100%, thoải mái thoáng mát', 199000, 3),
(N'Áo Thun Gym Thể Thao', N'Chất liệu cotton 100%, thoải mái thoáng mát', 159000, 3),
(N'Áo Thun T-Shirt', N'Chất liệu cotton 100%, thoải mái thoáng mát', 199000, 3),
(N'Áo Thun Premium Cotton', N'Chất liệu cotton 100%, thoải mái thoáng mát', 179000, 3),

-- Áo TankTop
(N'Áo TankTop Chạy Bộ', N'Chất liệu cotton 100%, thoải mái thoáng mát', 199000, 4),
(N'Áo TankTop AirRush', N'Chất liệu cotton 100%, thoải mái thoáng mát', 259000, 4),
(N'Áo TankTop Thoáng Mát Nhanh Khô', N'Chất liệu cotton 100%, thoải mái thoáng mát', 259000, 4),
(N'Áo TankTop Training Comfort', N'Chất liệu cotton 100%, thoải mái thoáng mát', 199000, 4),

-- Áo Khoác
(N'Áo Khoác Casual', N'Chất liệu cotton 100%, thoải mái thoáng mát', 359000, 5),
(N'Áo Khoác Thể Thao', N'Chất liệu cotton 100%, thoải mái thoáng mát', 399000, 5),
(N'Áo Khoác Nylon', N'Chất liệu cotton 100%, thoải mái thoáng mát', 299000, 5),
(N'Áo Khoác Đa Năng', N'Chất liệu cotton 100%, thoải mái thoáng mát', 299000, 5),

-- Quần Short
(N'Quần Short Casual', N'Chất liệu cotton 100%, thoải mái thoáng mát', 199000, 6),
(N'Quần Short Thể Thao', N'Chất liệu cotton 100%, thoải mái thoáng mát', 149000, 6),
(N'Quần Short Travel', N'Chất liệu cotton 100%, thoải mái thoáng mát', 159000, 6),
(N'Quần Short Chino', N'Chất liệu cotton 100%, thoải mái thoáng mát', 199000, 6),

-- Quần Jogger
(N'Quần Jogger Thể Thao Fleece Track', N'Chất liệu cotton 100%, thoải mái thoáng mát', 339000, 7),
(N'Quần Jogger Casual', N'Chất liệu cotton 100%, thoải mái thoáng mát', 359000, 7),
(N'Quần Jogger Thể Thao ExDry', N'Chất liệu cotton 100%, thoải mái thoáng mát', 299000, 7),
(N'Quần Jogger Daily Wear', N'Chất liệu cotton 100%, thoải mái thoáng mát', 259000, 7),

-- Quần Jean
(N'Quần Jean Basics Dáng Straight', N'Chất liệu cotton 100%, thoải mái thoáng mát', 339000, 8),
(N'Quần Jean Basics Dáng Slim', N'Chất liệu cotton 100%, thoải mái thoáng mát', 339000, 8),
(N'Quần Jean Basics Dáng Slim Fit', N'Chất liệu cotton 100%, thoải mái thoáng mát', 339000, 8),
(N'Quần Jean Basics Siêu Nhẹ', N'Chất liệu cotton 100%, thoải mái thoáng mát', 339000, 8);


INSERT INTO ProductImages (ProductID, ImageUrl, IsMain)
VALUES
(1, 'img/products/polo1.jpg', 1),
(1, 'img/products/polo11.jpg', 0),
(1, 'img/products/polo12.jpg', 0),
(1, 'img/products/polo13.jpg', 0),

(2, 'img/products/polo2.jpg', 1),
(2, 'img/products/polo21.jpg', 0),
(2, 'img/products/polo22.jpg', 0),
(2, 'img/products/polo23.jpg', 0),

(3, 'img/products/polo3.jpg', 1),
(3, 'img/products/polo31.jpg', 0),
(3, 'img/products/polo32.jpg', 0),
(3, 'img/products/polo33.jpg', 0),

(4, 'img/products/polo4.jpg', 1),
(4, 'img/products/polo41.jpg', 0),
(4, 'img/products/polo42.jpg', 0),
(4, 'img/products/polo43.jpg', 0),

(5, 'img/products/somi1.jpg', 1),
(5, 'img/products/somi11.jpg', 0),
(5, 'img/products/somi12.jpg', 0),
(5, 'img/products/somi13.jpg', 0),

(6, 'img/products/somi2.jpg', 1),
(6, 'img/products/somi21.jpg', 0),
(6, 'img/products/somi22.jpg', 0),
(6, 'img/products/somi23.jpg', 0),

(7, 'img/products/somi3.jpg', 1),
(7, 'img/products/somi31.jpg', 0),
(7, 'img/products/somi32.jpg', 0),
(7, 'img/products/somi33.jpg', 0),

(8, 'img/products/somi4.jpg', 1),
(8, 'img/products/somi41.jpg', 0),
(8, 'img/products/somi42.jpg', 0),
(8, 'img/products/somi43.jpg', 0),

(9, 'img/products/thun1.jpg',1),
(9, 'img/products/thun11jpg', 0),
(9, 'img/products/thun12.jpg', 0),
(9, 'img/products/thun13.jpg', 0),

(10, 'img/products/thun2.jpg',1),
(10, 'img/products/thun21.jpg', 0),
(10, 'img/products/thun22.jpg', 0),
(10, 'img/products/thun23.jpg', 0),

(11, 'img/products/thun3.jpg', 1),
(11, 'img/products/thun31.jpg', 0),
(11, 'img/products/thun32.jpg', 0),
(11, 'img/products/thun33.jpg', 0),

(12, 'img/products/thun4.jpg', 1),
(12, 'img/products/thun41.jpg', 0),
(12, 'img/products/thun42.jpg', 0),
(12, 'img/products/thun43.jpg', 0),

(13, 'img/products/tanktop1.jpg', 1),
(13, 'img/products/tanktop11.jpg', 0),
(13, 'img/products/tanktop12.jpg', 0),
(13, 'img/products/tanktop13.jpg', 0),

(14, 'img/products/tanktop2.jpg', 1),
(14, 'img/products/tanktop21.jpg', 0),
(14, 'img/products/tanktop22.jpg', 0),
(14, 'img/products/tanktop23.jpg', 0),

(15, 'img/products/tanktop3.jpg', 1),
(15, 'img/products/tanktop31.jpg', 0),
(15, 'img/products/tanktop32.jpg', 0),
(15, 'img/products/tanktop33.jpg', 0),

(16, 'img/products/tanktop4.jpg', 1),
(16, 'img/products/tanktop41.jpg', 0),
(16, 'img/products/tanktop42.jpg', 0),
(16, 'img/products/tanktop43.jpg', 0),

(17, 'img/products/khoac1.jpg', 1),
(17, 'img/products/khoac11.jpg', 0),
(17, 'img/products/khoac12.jpg', 0),
(17, 'img/products/khoac13.jpg', 0),

(18, 'img/products/khoac2.jpg', 1),
(18, 'img/products/khoac21.jpg', 0),
(18, 'img/products/khoac22.jpg', 0),
(18, 'img/products/khoac23.jpg', 0),

(19, 'img/products/khoac3.jpg', 1),
(19, 'img/products/khoac31.jpg', 0),
(19, 'img/products/khoac32.jpg', 0),
(19, 'img/products/khoac33.jpg', 0),

(20, 'img/products/khoac4.jpg', 1),
(20, 'img/products/khoac41.jpg', 0),
(20, 'img/products/khoac42.jpg', 0),
(20, 'img/products/khoac43.jpg', 0),

(21, 'img/products/short1.jpg', 1),
(21, 'img/products/short11.jpg', 0),
(21, 'img/products/short12.jpg', 0),
(21, 'img/products/short13.jpg', 0),

(22, 'img/products/sort2.jpg', 1),
(22, 'img/products/sort21.jpg', 0),
(22, 'img/products/sort22.jpg', 0),
(22, 'img/products/sort23.jpg', 0),

(23, 'img/products/sort3.jpg', 1),
(23, 'img/products/sort31.jpg', 0),
(23, 'img/products/sort32.jpg', 0),
(23, 'img/products/sort33.jpg', 0),

(24, 'img/products/sort4.jpg', 1),
(24, 'img/products/sort41.jpg', 0),
(24, 'img/products/sort42.jpg', 0),
(24, 'img/products/sort43.jpg', 0),

(25, 'img/products/jogger1.jpg', 1),
(25, 'img/products/jogger11.jpg', 0),
(25, 'img/products/jogger12.jpg', 0),
(25, 'img/products/jogger13.jpg', 0),

(26, 'img/products/jogger2.jpg', 1),
(26, 'img/products/jogger21.jpg', 0),
(26, 'img/products/jogger22.jpg', 0),
(26, 'img/products/jogger23.jpg', 0),

(27, 'img/products/jogger3.jpg', 1),
(27, 'img/products/jogger31.jpg', 0),
(27, 'img/products/jogger32.jpg', 0),
(27, 'img/products/jogger33.jpg', 0),

(28, 'img/products/jogger4.jpg', 1),
(28, 'img/products/jogger41.jpg', 0),
(28, 'img/products/jogger42.jpg', 0),
(28, 'img/products/jogger43.jpg', 0),

(29, 'img/products/jean1.jpg', 1),
(29, 'img/products/jean11.jpg', 0),
(29, 'img/products/jean12.jpg', 0),
(29, 'img/products/jean13.jpg', 0),


(30, 'img/products/jean2.jpg', 1),
(30, 'img/products/jean21.jpg', 0),
(30, 'img/products/jean22.jpg', 0),
(30, 'img/products/jean23.jpg', 0),

(31, 'img/products/jean3.jpg', 1),
(31, 'img/products/jean31.jpg', 0),
(31, 'img/products/jean32.jpg', 0),
(31, 'img/products/jean33.jpg', 0),


(32, 'img/products/jean4.jpg', 1),
(32, 'img/products/jean41.jpg', 0),
(32, 'img/products/jean42.jpg', 0),
(32, 'img/products/jean43.jpg', 0);


INSERT INTO ProductSizes (ProductID, Size, Stock)
VALUES
(1, 'S', 99), (1, 'M', 99), (1, 'L', 99), (1, 'XL', 99), (1, 'XXL', 99),
(2, 'S', 99), (2, 'M', 99), (2, 'L', 99), (2, 'XL', 99), (2, 'XXL', 99),
(3, 'S', 99), (3, 'M', 99), (3, 'L', 99), (3, 'XL', 99), (3, 'XXL', 99),
(4, 'S', 99), (4, 'M', 99), (4, 'L', 99), (4, 'XL', 99), (4, 'XXL', 99),
(5, 'S', 99), (5, 'M', 99), (5, 'L', 99), (5, 'XL', 99), (5, 'XXL', 99),
(6, 'S', 99), (6, 'M', 99), (6, 'L', 99), (6, 'XL', 99), (6, 'XXL', 99),
(7, 'S', 99), (7, 'M', 99), (7, 'L', 99), (7, 'XL', 99), (7, 'XXL', 99),
(8, 'S', 99), (8, 'M', 99), (8, 'L', 99), (8, 'XL', 99), (8, 'XXL', 99),
(9, 'S', 99), (9, 'M', 99), (9, 'L', 99), (9, 'XL', 99), (9, 'XXL', 99),
(10, 'S', 99), (10, 'M', 99), (10, 'L', 99), (10, 'XL', 99), (10, 'XXL', 99),
(11, 'S', 99), (11, 'M', 99), (11, 'L', 99), (11, 'XL', 99), (11, 'XXL', 99),
(12, 'S', 99), (12, 'M', 99), (12, 'L', 99), (12, 'XL', 99), (12, 'XXL', 99),
(13, 'S', 99), (13, 'M', 99), (13, 'L', 99), (13, 'XL', 99), (13, 'XXL', 99),
(14, 'S', 99), (14, 'M', 99), (14, 'L', 99), (14, 'XL', 99), (14, 'XXL', 99),
(15, 'S', 99), (15, 'M', 99), (15, 'L', 99), (15, 'XL', 99), (15, 'XXL', 99),
(16, 'S', 99), (16, 'M', 99), (16, 'L', 99), (16, 'XL', 99), (16, 'XXL', 99),
(17, 'S', 99), (17, 'M', 99), (17, 'L', 99), (17, 'XL', 99), (17, 'XXL', 99),
(18, 'S', 99), (18, 'M', 99), (18, 'L', 99), (18, 'XL', 99), (18, 'XXL', 99),
(19, 'S', 99), (19, 'M', 99), (19, 'L', 99), (19, 'XL', 99), (19, 'XXL', 99),
(20, 'S', 99), (20, 'M', 99), (20, 'L', 99), (20, 'XL', 99), (20, 'XXL', 99),
(21, 'S', 99), (21, 'M', 99), (21, 'L', 99), (21, 'XL', 99), (21, 'XXL', 99),
(22, 'S', 99), (22, 'M', 99), (22, 'L', 99), (22, 'XL', 99), (22, 'XXL', 99),
(23, 'S', 99), (23, 'M', 99), (23, 'L', 99), (23, 'XL', 99), (23, 'XXL', 99),
(24, 'S', 99), (24, 'M', 99), (24, 'L', 99), (24, 'XL', 99), (24, 'XXL', 99),
(25, 'S', 99), (25, 'M', 99), (25, 'L', 99), (25, 'XL', 99), (25, 'XXL', 99),
(26, 'S', 99), (26, 'M', 99), (26, 'L', 99), (26, 'XL', 99), (26, 'XXL', 99),
(27, 'S', 99), (27, 'M', 99), (27, 'L', 99), (27, 'XL', 99), (27, 'XXL', 99),
(28, 'S', 99), (28, 'M', 99), (28, 'L', 99), (28, 'XL', 99), (28, 'XXL', 99),
(29, 'S', 99), (29, 'M', 99), (29, 'L', 99), (29, 'XL', 99), (29, 'XXL', 99),
(30, 'S', 99), (30, 'M', 99), (30, 'L', 99), (30, 'XL', 99), (30, 'XXL', 99),
(31, 'S', 99), (31, 'M', 99), (31, 'L', 99), (31, 'XL', 99), (31, 'XXL', 99),
(32, 'S', 99), (32, 'M', 99), (32, 'L', 99), (32, 'XL', 99), (32, 'XXL', 99);


-- Phương thức thanh toán
INSERT INTO PaymentMethods (Name, Description) VALUES
(N'Thanh toán khi nhận hàng', N'Thanh toán trực tiếp khi giao hàng'),
(N'Chuyển khoản ngân hàng', N'Qua tài khoản ngân hàng'),
(N'Momo', N'Thanh toán ví Momo');

-- Cập nhật vai trò admin
UPDATE Users SET Role = 'admin' WHERE UserID = 1;

-- Thêm admin (nếu chưa có)
INSERT INTO Users (Name, Email, Password, Role)
VALUES (N'admin', 'admin@mail.com', '$2a$10$J0AT51XX/N00bUNw3K6BqekEWxn8xWCy5SlEaZI3bkfk4WGz7WZg', 'admin');



-- Kiểm tra dữ liệu (optional)
-- SELECT * FROM Users;
-- SELECT * FROM Products;
-- SELECT * FROM ProductImages;
-- SELECT * FROM CartItems;

-- Reset dữ liệu (nếu cần)
-- Tắt khóa ngoại
-- EXEC sp_msforeachtable "ALTER TABLE ? NOCHECK CONSTRAINT ALL"
-- Xóa dữ liệu
-- EXEC sp_msforeachtable "DELETE FROM ?"
-- Bật lại khóa ngoại
-- EXEC sp_msforeachtable "ALTER TABLE ? WITH CHECK CHECK CONSTRAINT ALL"






ALTER TABLE Products ADD oldPrice DECIMAL(10, 2);


ALTER TABLE Users ADD email_verified BIT NOT NULL DEFAULT 0;


ALTER TABLE Users
ADD password_changed_at DATETIME NULL;

-- ✅ Sử dụng N'Đã giao' với N đứng trước
ALTER TABLE Orders DROP CONSTRAINT CK__Orders__Status__4CA06362;


ALTER TABLE Orders ADD CONSTRAINT CK_Orders_Status
CHECK ([Status] IN (
    N'Chờ xác nhận',
    N'Đang xử lý',
    N'Đang giao',
    N'Đã giao',
    N'Đã hủy',
    N'Trả hàng'
));

INSERT INTO [Orders] (
    [UserID],
    [RecipientName],
    [Phone],
    [ShippingAddress],
    [Note],
    [Total],
    [Status],
    [OrderDate],
    [PaymentMethodID],
    [PaymentMethodName]
)
VALUES (
    2,
    N'Nguyễn Văn A',
    '0123456789',
    N'123 Main Street',
    N'Test đơn hàng completed',
    199.99,
    N'Đã giao',  -- ✅ Giờ đã hợp lệ
    GETDATE(),
    1,
    N'Thanh toán khi nhận hàng'
);

SELECT TOP 1 OrderID FROM Orders ORDER BY OrderDate DESC;

INSERT INTO [OrderItems] (
    [OrderID],
    [ProductID],
    [ProductSizeID],
    [ProductName],
    [Size],
    [Quantity],
    [Price]
)
VALUES (
    1,                -- Thay bằng OrderID đúng nếu khác
    1,
    1,
    N'Áo thun nam basic',
    N'S',
    1,
    199000.00
);

INSERT INTO Orders (UserID, RecipientName, Phone, ShippingAddress, Note, Total, Status, OrderDate, PaymentMethodID, PaymentMethodName)
VALUES (2, N'Trần Văn A', '0123456789', N'123 Đường ABC', N'Giao giờ hành chính', 500000, N'Đang giao', GETDATE(), 1, N'Thanh toán khi nhận hàng');

SELECT TOP 1 OrderID FROM Orders ORDER BY OrderID DESC;

INSERT INTO OrderItems (OrderID, ProductID, ProductSizeID, ProductName, Size, Quantity, Price)
VALUES (2, 2, 6, N'Giày sneaker trắng', '38', 2, 890000.00);

INSERT INTO Orders (
    UserID, RecipientName, Phone, ShippingAddress, Note, Total, Status, OrderDate, PaymentMethodID, PaymentMethodName
)
VALUES (
    2, N'Nguyễn Văn B', '0912345678', N'456 Đường XYZ', N'Hủy vì thay đổi ý định', 200000, N'Đã hủy', GETDATE(), 1, N'Thanh toán khi nhận hàng'
);

SELECT TOP 1 OrderID FROM Orders ORDER BY OrderID DESC;

INSERT INTO OrderItems (
    OrderID, ProductID, ProductSizeID, ProductName, Size, Quantity, Price
)
VALUES (
    3, 3, 21, N'Balo laptop chống sốc', 'S', 1, 459000.00
);

ALTER TABLE Reviews
ADD OrderID INT;

ALTER TABLE Reviews
ADD CONSTRAINT FK_Reviews_Orders
FOREIGN KEY (OrderID) REFERENCES Orders(OrderID);


DELETE FROM Reviews;