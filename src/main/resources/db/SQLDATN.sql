-- Tạo database nếu chưa tồn tại
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'DATN')
BEGIN
    CREATE DATABASE DATN;
END
GO

USE DATN;
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
GO

-- Bảng danh mục
CREATE TABLE Categories (
    CategoryID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(100) NOT NULL,
    Description NVARCHAR(MAX)
);
GO

-- Bảng sản phẩm
CREATE TABLE Products (
    ProductID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(150) NOT NULL,
    Description NVARCHAR(MAX),
    Price DECIMAL(10,2) NOT NULL,
    Stock INT DEFAULT 0,
    Image NVARCHAR(255),
    CategoryID INT,
    Size NVARCHAR(50),
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID)
);
GO

-- Bảng đơn hàng
CREATE TABLE Orders (
    OrderID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    Total DECIMAL(10,2),
    Status NVARCHAR(20) DEFAULT N'Chờ xác nhận' CHECK (Status IN (N'Chờ xác nhận', N'Đang xử lý', N'Đang giao', N'Đã giao', N'Đã hủy',N'Trả hàng')),
    OrderDate DATETIME DEFAULT GETDATE(),
    ShippingAddress NVARCHAR(MAX),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
GO

-- Bảng chi tiết đơn hàng
CREATE TABLE OrderDetails (
    OrderDetailID INT PRIMARY KEY IDENTITY(1,1),
    OrderID INT,
    ProductID INT,
    Quantity INT NOT NULL,
    Price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
);
GO

-- Bảng đánh giá sản phẩm
CREATE TABLE Reviews (
    ReviewID INT PRIMARY KEY IDENTITY(1,1),
    ProductID INT,
    UserID INT,
    Rating INT CHECK (Rating BETWEEN 1 AND 5),
    Comment NVARCHAR(MAX),
    CreatedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
GO

-- Bảng giỏ hàng
CREATE TABLE Carts (
    CartID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    CreatedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
GO

CREATE TABLE CartItems (
    CartItemID INT PRIMARY KEY IDENTITY(1,1),
    CartID INT,
    ProductID INT,
    Quantity INT NOT NULL,
    FOREIGN KEY (CartID) REFERENCES Carts(CartID),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
);
GO

-- Thêm dữ liệu mẫu cho bảng Users
INSERT INTO Users (Name, Email, Password, Phone, Address, Role) VALUES
(N'Admin', 'admin@gmail.com', '$2a$10$N8JUm.3Z4CQxpTEmp.T6pOllxUHEbCMfzUWBv8QyZYn3wI8GK4Fby', '0987654321', N'Hà Nội', 'admin'),
GO

-- Thêm dữ liệu mẫu cho bảng Categories
INSERT INTO Categories (Name, Description) VALUES
(N'Áo', N'Các loại áo thời trang nam nữ'),
(N'Quần', N'Các loại quần thời trang nam nữ'),
(N'Váy', N'Các loại váy thời trang nữ'),
(N'Phụ kiện', N'Các loại phụ kiện thời trang');
GO

-- Thêm dữ liệu mẫu cho bảng Products
INSERT INTO Products (Name, Description, Price, Stock, Image, CategoryID, Size) VALUES
(N'Áo thun basic', N'Áo thun trơn form rộng', 150000, 100, 'aothun1.jpg', 1, 'M'),
(N'Áo sơ mi trắng', N'Áo sơ mi công sở', 250000, 80, 'aosomi1.jpg', 1, 'L'),
(N'Quần jean nam', N'Quần jean form regular', 450000, 50, 'quanjean1.jpg', 2, '32'),
(N'Váy hoa nhí', N'Váy hoa nhí dáng xòe', 350000, 40, 'vayhoa1.jpg', 3, 'S'),
(N'Thắt lưng da', N'Thắt lưng da bò thật', 200000, 30, 'thatlung1.jpg', 4, 'FREE');
GO

-- Thêm dữ liệu mẫu cho bảng Orders
INSERT INTO Orders (UserID, Total, Status, ShippingAddress) VALUES
(2, 400000, N'Đã giao', N'123 Nguyễn Văn Cừ, Q.5, TP.HCM'),
(3, 350000, N'Đang giao', N'456 Lê Lợi, Hải Châu, Đà Nẵng'),
(2, 650000, N'Chờ xác nhận', N'789 Trần Hưng Đạo, Q.1, TP.HCM');
GO

-- Thêm dữ liệu mẫu cho bảng OrderDetails
INSERT INTO OrderDetails (OrderID, ProductID, Quantity, Price) VALUES
(1, 1, 2, 150000),
(1, 2, 1, 250000),
(2, 4, 1, 350000),
(3, 3, 1, 450000),
(3, 5, 1, 200000);
GO

-- Thêm dữ liệu mẫu cho bảng Reviews
INSERT INTO Reviews (ProductID, UserID, Rating, Comment) VALUES
(1, 2, 5, N'Sản phẩm rất tốt, đúng như mô tả'),
(2, 3, 4, N'Chất lượng ổn, giao hàng nhanh'),
(4, 3, 5, N'Váy đẹp, form chuẩn');
GO

-- Thêm dữ liệu mẫu cho bảng Carts
INSERT INTO Carts (UserID) VALUES
(2),
(3);
GO

-- Thêm dữ liệu mẫu cho bảng CartItems
INSERT INTO CartItems (CartID, ProductID, Quantity) VALUES
(1, 1, 1),
(1, 3, 1),
(2, 4, 2);
GO
