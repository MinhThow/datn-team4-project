Create database DATN;
Go

Use DATN;
Go

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
    Stock INT DEFAULT 0,
    Image NVARCHAR(255),
    CategoryID INT,
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID),
    Size NVARCHAR(50)
);

CREATE TABLE Orders (
    OrderID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    Total DECIMAL(10,2),
    Status NVARCHAR(20) DEFAULT 'Chờ xác nhận' CHECK (Status IN ('Chờ xác nhận', 'Đang xử lý', 'Đang giao', 'Đã giao', 'Đã hủy','Trả hàng')),
    OrderDate DATETIME DEFAULT GETDATE(),
    ShippingAddress NVARCHAR(MAX),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
ALTER TABLE Orders
ADD PaymentMethodID INT FOREIGN KEY REFERENCES PaymentMethods(PaymentMethodID);

CREATE TABLE OrderItems (
    OrderItemID INT PRIMARY KEY IDENTITY(1,1),
    OrderID INT,
    ProductID INT,
    Quantity INT,
    Price DECIMAL(10,2),
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
);

CREATE TABLE CartItems (
    CartItemID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    ProductID INT,
    Quantity INT DEFAULT 1,
    AddedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
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

-- Phương thức thanh toán
CREATE TABLE PaymentMethods (
    PaymentMethodID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(100) NOT NULL,        
    Description NVARCHAR(MAX)
);

CREATE TABLE Sales (
    SaleID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(255),                -- Tên chương trình sale
    Description NVARCHAR(MAX),         -- Mô tả
    StartDate DATETIME,                -- Thời gian bắt đầu
    EndDate DATETIME                   -- Thời gian kết thúc
);

CREATE TABLE SaleDetails (
    SaleDetailID INT PRIMARY KEY IDENTITY(1,1),
    SaleID INT,
    ProductID INT,
    SalePrice DECIMAL(10,2) NOT NULL,  -- Giá sale cho sản phẩm này
    FOREIGN KEY (SaleID) REFERENCES Sales(SaleID),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
);

-- Dữ liệu mẫu cho Users
INSERT INTO Users (Name, Email, Password, Phone, Address, Role) VALUES
(N'Nguyễn Văn A', N'a@gmail.com', N'123456', N'0123456789', N'Hà Nội', N'customer'),
(N'Trần Thị B', N'b@gmail.com', N'123456', N'0987654321', N'Hồ Chí Minh', N'customer'),
(N'Admin', N'admin@gmail.com', N'admin123', N'0123123123', N'Hà Nội', N'admin');

-- Dữ liệu mẫu cho Categories
INSERT INTO Categories (Name, Description) VALUES
(N'Áo', N'Các loại áo thời trang nam'),
(N'Quần', N'Các loại quần thời trang nam'),
(N'Giày', N'Các loại giày nam'),
(N'Phụ kiện', N'Phụ kiện thời trang nam');

-- Dữ liệu mẫu cho Products
INSERT INTO Products (Name, Description, Price, Stock, Image, CategoryID, Size)
VALUES
(N'Áo thun basic', N'Áo thun cotton co giãn', 199000, 50, N'product-1.jpg', 1, N'M'),
(N'Áo khoác bomber', N'Áo khoác thời trang nam', 499000, 30, N'product-2.jpg', 1, N'L'),
(N'Quần jeans slimfit', N'Quần jeans co giãn', 399000, 40, N'product-3.jpg', 2, N'32'),
(N'Giày sneaker trắng', N'Giày sneaker nam trẻ trung', 599000, 20, N'product-4.jpg', 3, N'42'),
(N'Balo thời trang', N'Balo vải canvas', 299000, 25, N'product-5.jpg', 4, N'Free');

-- Dữ liệu mẫu cho PaymentMethods
INSERT INTO PaymentMethods (Name, Description) VALUES
(N'Thanh toán khi nhận hàng', N'Thanh toán trực tiếp khi nhận hàng'),
(N'Chuyển khoản ngân hàng', N'Thanh toán qua tài khoản ngân hàng');

-- Dữ liệu mẫu cho Sales
INSERT INTO Sales (Name, Description, StartDate, EndDate) VALUES
(N'Sale Hè 2024', N'Giảm giá đặc biệt cho mùa hè', '2024-06-01', '2024-06-30'),
(N'Flash Sale 7.7', N'Giảm giá sốc trong ngày 7/7', '2024-07-07', '2024-07-07');

-- Dữ liệu mẫu cho SaleDetails
INSERT INTO SaleDetails (SaleID, ProductID, SalePrice) VALUES
(1, 1, 159000), -- Áo thun basic giảm còn 159k trong Sale Hè
(1, 3, 349000), -- Quần jeans slimfit giảm còn 349k trong Sale Hè
(2, 4, 499000); -- Giày sneaker trắng giảm còn 499k trong Flash Sale




