-- H2 Database compatible SQL script for DATN project

CREATE TABLE Users (
    UserID INT IDENTITY PRIMARY KEY,
    Name VARCHAR(100),
    Email VARCHAR(100) UNIQUE,
    Password VARCHAR(255),
    Phone VARCHAR(20),
    Address TEXT,
    Role VARCHAR(20) DEFAULT 'customer' CHECK (Role IN ('customer', 'admin')),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Categories (
    CategoryID INT IDENTITY PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Description TEXT
);

CREATE TABLE Products (
    ProductID INT IDENTITY PRIMARY KEY,
    Name VARCHAR(150) NOT NULL,
    Description TEXT,
    Price DECIMAL(10,2) NOT NULL,
    Stock INT DEFAULT 0,
    Image VARCHAR(255),
    CategoryID INT,
    Size VARCHAR(50),
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID)
);

-- Phương thức thanh toán (moved up to be referenced by Orders)
CREATE TABLE PaymentMethods (
    PaymentMethodID INT IDENTITY PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,        
    Description TEXT
);

CREATE TABLE Orders (
    OrderID INT IDENTITY PRIMARY KEY,
    UserID INT,
    Total DECIMAL(10,2),
    Status VARCHAR(20) DEFAULT 'Chờ xác nhận' CHECK (Status IN ('Chờ xác nhận', 'Đang xử lý', 'Đang giao', 'Đã giao', 'Đã hủy','Trả hàng')),
    OrderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ShippingAddress TEXT,
    PaymentMethodID INT,
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (PaymentMethodID) REFERENCES PaymentMethods(PaymentMethodID)
);

CREATE TABLE OrderItems (
    OrderItemID INT IDENTITY PRIMARY KEY,
    OrderID INT,
    ProductID INT,
    Quantity INT,
    Price DECIMAL(10,2),
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
);

CREATE TABLE CartItems (
    CartItemID INT IDENTITY PRIMARY KEY,
    UserID INT,
    ProductID INT,
    Quantity INT DEFAULT 1,
    AddedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
);

CREATE TABLE Reviews (
    ReviewID INT IDENTITY PRIMARY KEY,
    ProductID INT,
    UserID INT,
    Rating INT CHECK (Rating BETWEEN 1 AND 5),
    Comment TEXT,
    ReviewDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE Sales (
    SaleID INT IDENTITY PRIMARY KEY,
    Name VARCHAR(255),                -- Tên chương trình sale
    Description TEXT,                 -- Mô tả
    StartDate TIMESTAMP,              -- Thời gian bắt đầu
    EndDate TIMESTAMP                 -- Thời gian kết thúc
);

CREATE TABLE SaleDetails (
    SaleDetailID INT IDENTITY PRIMARY KEY,
    SaleID INT,
    ProductID INT,
    SalePrice DECIMAL(10,2) NOT NULL,  -- Giá sale cho sản phẩm này
    FOREIGN KEY (SaleID) REFERENCES Sales(SaleID),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
);

-- Dữ liệu mẫu cho Users
INSERT INTO Users (Name, Email, Password, Phone, Address, Role) VALUES
('Nguyễn Văn A', 'a@gmail.com', '123456', '0123456789', 'Hà Nội', 'customer'),
('Trần Thị B', 'b@gmail.com', '123456', '0987654321', 'Hồ Chí Minh', 'customer'),
('Admin', 'admin@gmail.com', 'admin123', '0123123123', 'Hà Nội', 'admin');

-- Dữ liệu mẫu cho Categories
INSERT INTO Categories (Name, Description) VALUES
('Áo', 'Các loại áo thời trang nam'),
('Quần', 'Các loại quần thời trang nam'),
('Giày', 'Các loại giày nam'),
('Phụ kiện', 'Phụ kiện thời trang nam');

-- Dữ liệu mẫu cho Products
INSERT INTO Products (Name, Description, Price, Stock, Image, CategoryID, Size)
VALUES
('Áo thun basic', 'Áo thun cotton co giãn', 199000, 50, 'product-1.jpg', 1, 'M'),
('Áo khoác bomber', 'Áo khoác thời trang nam', 499000, 30, 'product-2.jpg', 1, 'L'),
('Quần jeans slimfit', 'Quần jeans co giãn', 399000, 40, 'product-3.jpg', 2, '32'),
('Giày sneaker trắng', 'Giày sneaker nam trẻ trung', 599000, 20, 'product-4.jpg', 3, '42'),
('Balo thời trang', 'Balo vải canvas', 299000, 25, 'product-5.jpg', 4, 'Free');

-- Dữ liệu mẫu cho PaymentMethods
INSERT INTO PaymentMethods (Name, Description) VALUES
('Thanh toán khi nhận hàng', 'Thanh toán trực tiếp khi nhận hàng'),
('Chuyển khoản ngân hàng', 'Thanh toán qua tài khoản ngân hàng');

-- Dữ liệu mẫu cho Sales
INSERT INTO Sales (Name, Description, StartDate, EndDate) VALUES
('Sale Hè 2024', 'Giảm giá đặc biệt cho mùa hè', '2024-06-01 00:00:00', '2024-06-30 23:59:59'),
('Flash Sale 7.7', 'Giảm giá sốc trong ngày 7/7', '2024-07-07 00:00:00', '2024-07-07 23:59:59');

-- Dữ liệu mẫu cho SaleDetails
INSERT INTO SaleDetails (SaleID, ProductID, SalePrice) VALUES
(1, 1, 159000), -- Áo thun basic giảm còn 159k trong Sale Hè
(1, 3, 349000), -- Quần jeans slimfit giảm còn 349k trong Sale Hè
(2, 4, 499000); -- Giày sneaker trắng giảm còn 499k trong Flash Sale




