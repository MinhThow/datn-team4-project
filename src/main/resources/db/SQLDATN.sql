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

-- Dữ liệu mẫu: Danh mục
INSERT INTO Categories (Name, Description) VALUES
(N'Áo', N'Các loại áo thun, áo sơ mi, áo khoác...'),
(N'Giày', N'Giày sneaker, giày thể thao, giày boot...'),
(N'Balo', N'Balo thời trang, balo laptop, balo du lịch...'),
(N'Phụ kiện', N'Nón, túi xách, thắt lưng, kính mát...');

-- Dữ liệu mẫu: Sản phẩm
INSERT INTO Products (Name, Description, Price, CategoryID) VALUES
(N'Áo thun nam basic', N'Chất liệu cotton 100%, form rộng dễ mặc.', 199000, 1),
(N'Giày sneaker trắng', N'Mẫu giày hot trend, phối đồ dễ dàng.', 890000, 2),
(N'Balo laptop chống sốc', N'Chứa được laptop 15.6 inch, chống nước.', 459000, 3),
(N'Nước hoa', N'Lưu hương , quyến rũ', 299000, 4);

-- Size sản phẩm
INSERT INTO ProductSizes (ProductID, Size, Stock) VALUES
(1, 'S', 99), (1, 'M', 99), (1, 'L', 99), (1, 'XL', 99), (1, 'XXL', 99),
(2, '38', 5), (2, '39', 8), (2, '40', 10), (2, '42', 6), (2, '43', 4);

-- Ảnh sản phẩm
INSERT INTO ProductImages (ProductID, ImageUrl, IsMain) VALUES
(1, 'img/product/product-8.jpg', 1),
(1, 'img/product/product-8.jpg', 0),
(2, 'img/product/product-1.jpg', 1),
(2, 'img/product/product-1.jpg', 0),
(3, 'img/product/product-3.jpg', 1),
(3, 'img/product/product-3.jpg', 0),
(4, 'img/product/polo.jpg', 0);

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
CREATE TABLE [dbo].[bank_accounts] (
    [id] INT IDENTITY(1,1) PRIMARY KEY,
    [accountHolder] NVARCHAR(100) NOT NULL,
    [accountNumber] NVARCHAR(50) NOT NULL,
    [bankName] NVARCHAR(100) NOT NULL,
    [branch] NVARCHAR(100),
    [cardType] NVARCHAR(50),
    [cvv] NVARCHAR(10),
    [expiryDate] VARCHAR(10),
    [user_id] INT NOT NULL,
    CONSTRAINT FK_BankAccounts_Users FOREIGN KEY ([user_id])
        REFERENCES [dbo].[users]([UserID])
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

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
    6,
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
    9,                -- Thay bằng OrderID đúng nếu khác
    1,
    1,
    N'Áo thun nam basic',
    N'S',
    1,
    199000.00
);

INSERT INTO Orders (UserID, RecipientName, Phone, ShippingAddress, Note, Total, Status, OrderDate, PaymentMethodID, PaymentMethodName)
VALUES (6, N'Trần Văn A', '0123456789', N'123 Đường ABC', N'Giao giờ hành chính', 500000, N'Đang giao', GETDATE(), 1, N'Thanh toán khi nhận hàng');

SELECT TOP 1 OrderID FROM Orders ORDER BY OrderID DESC;

INSERT INTO OrderItems (OrderID, ProductID, ProductSizeID, ProductName, Size, Quantity, Price)
VALUES (11, 2, 6, N'Giày sneaker trắng', '38', 2, 890000.00);

INSERT INTO Orders (
    UserID, RecipientName, Phone, ShippingAddress, Note, Total, Status, OrderDate, PaymentMethodID, PaymentMethodName
)
VALUES (
    6, N'Nguyễn Văn B', '0912345678', N'456 Đường XYZ', N'Hủy vì thay đổi ý định', 200000, N'Đã hủy', GETDATE(), 1, N'Thanh toán khi nhận hàng'
);

SELECT TOP 1 OrderID FROM Orders ORDER BY OrderID DESC;

INSERT INTO OrderItems (
    OrderID, ProductID, ProductSizeID, ProductName, Size, Quantity, Price
)
VALUES (
    12, 3, 21, N'Balo laptop chống sốc', 'S', 1, 459000.00
);


CREATE TABLE VerificationToken (
    id INT IDENTITY PRIMARY KEY,
    token NVARCHAR(255),
    user_id INT,
    expiryDate DATETIME,
    CONSTRAINT FK_VerificationToken_User FOREIGN KEY (user_id) REFERENCES Users(UserID)
);

ALTER TABLE Users ADD email_verified BIT NOT NULL DEFAULT 0;

ALTER TABLE Users
ADD password_changed_at DATETIME NULL;

CREATE TABLE LoginHistory (
    LID INT PRIMARY KEY IDENTITY(1,1),             -- LID = Login ID, rõ nghĩa
    UserID INT NOT NULL,                           -- khóa ngoại đến Users.UserID
    login_time DATETIME NOT NULL DEFAULT GETDATE(),
    ip_address NVARCHAR(100),
    user_agent NVARCHAR(512),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

