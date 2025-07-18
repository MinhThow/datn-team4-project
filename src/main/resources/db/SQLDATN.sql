CREATE DATABASE DATN3;
GO

USE DATN3;
GO



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


CREATE TABLE Categories (
    CategoryID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(100) NOT NULL,
    Description NVARCHAR(MAX)
);


CREATE TABLE Products (
    ProductID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(150) NOT NULL,
    Description NVARCHAR(MAX),
    Price DECIMAL(10,2) NOT NULL,
    CategoryID INT,
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID)
);


CREATE TABLE ProductSizes (
    ProductSizeID INT PRIMARY KEY IDENTITY(1,1),
    ProductID INT,
    Size NVARCHAR(50),
    Stock INT DEFAULT 0,
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
);


CREATE TABLE ProductImages (
    ImageID INT PRIMARY KEY IDENTITY(1,1),
    ProductID INT,
    ImageUrl NVARCHAR(255),
	IsMain BIT DEFAULT 0, -- 0 = ảnh phụ, 1 = ảnh chính
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
);


CREATE TABLE PaymentMethods (
    PaymentMethodID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(100) NOT NULL,
    Description NVARCHAR(MAX)
);


CREATE TABLE Orders (
    OrderID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    Total DECIMAL(10,2),
    Status NVARCHAR(20) DEFAULT 'Chờ xác nhận' CHECK (Status IN ('Chờ xác nhận', 'Đang xử lý', 'Đang giao', 'Đã giao', 'Đã hủy', 'Trả hàng')),
    OrderDate DATETIME DEFAULT GETDATE(),
    ShippingAddress NVARCHAR(MAX),
    PaymentMethodID INT,
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (PaymentMethodID) REFERENCES PaymentMethods(PaymentMethodID)
);


CREATE TABLE OrderItems (
    OrderItemID INT PRIMARY KEY IDENTITY(1,1),
    OrderID INT,
    ProductID INT,
    ProductSizeID INT,
    Quantity INT,
    Price DECIMAL(10,2),
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID),
    FOREIGN KEY (ProductSizeID) REFERENCES ProductSizes(ProductSizeID)
);


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


INSERT INTO Categories (Name, Description)
VALUES
(N'Áo', N'Các loại áo thun, áo sơ mi, áo khoác...'),
(N'Giày', N'Giày sneaker, giày thể thao, giày boot...'),
(N'Balo', N'Balo thời trang, balo laptop, balo du lịch...'),
(N'Phụ kiện', N'Nón, túi xách, thắt lưng, kính mát...');

INSERT INTO Products (Name, Description, Price, CategoryID)
VALUES
(N'Áo thun nam basic', N'Chất liệu cotton 100%, form rộng dễ mặc.', 199000, 1),  -- Áo
(N'Giày sneaker trắng', N'Mẫu giày hot trend, phối đồ dễ dàng.', 890000, 2),     -- Giày
(N'Balo laptop chống sốc', N'Chứa được laptop 15.6 inch, chống nước.', 459000, 3), -- Balo
(N'Nước hoa', N'Lưu hương , quyến rũ', 299000, 4);         -- Phụ kiện

(1, 'S', 10),
(1, 'M', 12),
(1, 'L', 15),
(1, 'XL', 10),
(1, 'XXL', 8);


INSERT INTO ProductSizes (ProductID, Size, Stock)
VALUES
(2, '38', 5),
(2, '39', 8),
(2, '40', 10),
(2, '42', 6),
(2, '43', 4);



-- Áo thun nam basic (ProductID = 1)
INSERT INTO ProductImages (ProductID, ImageUrl, IsMain)
VALUES
(1, 'img/product/product-8.jpg', 1),
(1, 'img/product/product-8.jpg', 0),
(1, 'img/product/product-8.jpg', 0),
(1, 'img/product/product-8.jpg', 0);

-- Giày sneaker trắng (ProductID = 2)
INSERT INTO ProductImages (ProductID, ImageUrl, IsMain)
VALUES
(2, 'img/product/product-1.jpg', 1),
(2, 'img/product/product-1.jpg', 0),
(2, 'img/product/product-1.jpg', 0),
(2, 'img/product/product-1.jpg', 0);

-- Balo laptop chống sốc (ProductID = 3)
INSERT INTO ProductImages (ProductID, ImageUrl, IsMain)
VALUES
(3, 'img/product/product-3.jpg', 1),
(3, 'img/product/product-3.jpg', 0),
(3,'img/product/product-3.jpg', 0),
(3, 'img/product/product-3.jpg', 0);

-- Kính mát thời trang (ProductID = 4)
INSERT INTO ProductImages (ProductID, ImageUrl, IsMain)
VALUES
(4, 'img/product/product-4.jpg', 1),
(4, 'img/product/product-4.jpg', 0),
(4, 'img/product/product-4.jpg', 0),
(4, 'img/product/product-4.jpg', 0);

-- Áo sơ mi caro (ProductID = 5)
INSERT INTO ProductImages (ProductID, ImageUrl, IsMain)
VALUES
(N'Thanh toán khi nhận hàng', N'Thanh toán trực tiếp khi giao hàng'),
(N'Chuyển khoản ngân hàng', N'Qua tài khoản ngân hàng'),
(N'Momo', N'Thanh toán ví Momo');

select * from CartItems


select * from ProductImages
select * from Products
select * from ProductSizes