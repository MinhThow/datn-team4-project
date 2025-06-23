USE DATN;
GO

-- 1. Tạo bảng Phương thức thanh toán TRƯỚC
CREATE TABLE datn.PaymentMethods (
                                     PaymentMethodID INT PRIMARY KEY IDENTITY(1,1),
                                     Name NVARCHAR(100) NOT NULL,
                                     Description NVARCHAR(MAX)
);

-- 2. Tạo bảng Users
CREATE TABLE datn.Users (
                            UserID INT PRIMARY KEY IDENTITY(1,1),
                            Name NVARCHAR(100),
                            Email NVARCHAR(100) UNIQUE,
                            Password NVARCHAR(255),
                            Phone NVARCHAR(20),
                            Address NVARCHAR(MAX),
                            Role NVARCHAR(20) DEFAULT 'customer' CHECK (Role IN ('customer', 'admin')),
                            CreatedAt DATETIME DEFAULT GETDATE()
);

-- 3. Tạo bảng Categories
CREATE TABLE datn.Categories (
                                 CategoryID INT PRIMARY KEY IDENTITY(1,1),
                                 Name NVARCHAR(100) NOT NULL,
                                 Description NVARCHAR(MAX)
);

-- 4. Tạo bảng Products
CREATE TABLE datn.Products (
                               ProductID INT PRIMARY KEY IDENTITY(1,1),
                               Name NVARCHAR(150) NOT NULL,
                               Description NVARCHAR(MAX),
                               Price DECIMAL(10,2) NOT NULL,
                               Stock INT DEFAULT 0,
                               Image NVARCHAR(255),
                               CategoryID INT,
                               FOREIGN KEY (CategoryID) REFERENCES datn.Categories(CategoryID)
);
-- Thêm Size cho bảng Products
ALTER TABLE datn.Products ADD Size NVARCHAR(50);

-- 5. Tạo bảng Orders
CREATE TABLE datn.Orders (
                             OrderID INT PRIMARY KEY IDENTITY(1,1),
                             UserID INT,
                             Total DECIMAL(10,2),
                             Status NVARCHAR(20) DEFAULT 'Cho xac nhan' CHECK (Status IN ('Cho xac nhan', 'Dang xy ly', 'Dang giao', 'Da giao', 'Da huy','Tra Hang')),
                             OrderDate DATETIME DEFAULT GETDATE(),
                             ShippingAddress NVARCHAR(MAX),
                             PaymentMethodID INT, -- thêm trực tiếp cột vào đây luôn
                             FOREIGN KEY (UserID) REFERENCES datn.Users(UserID),
                             FOREIGN KEY (PaymentMethodID) REFERENCES datn.PaymentMethods(PaymentMethodID)
);

-- 6. Tạo OrderItems
CREATE TABLE datn.OrderItems (
                                 OrderItemID INT PRIMARY KEY IDENTITY(1,1),
                                 OrderID INT,
                                 ProductID INT,
                                 Quantity INT,
                                 Price DECIMAL(10,2),
                                 FOREIGN KEY (OrderID) REFERENCES datn.Orders(OrderID),
                                 FOREIGN KEY (ProductID) REFERENCES datn.Products(ProductID)
);

-- 7. Tạo CartItems
CREATE TABLE datn.CartItems (
                                CartItemID INT PRIMARY KEY IDENTITY(1,1),
                                UserID INT,
                                ProductID INT,
                                Quantity INT DEFAULT 1,
                                AddedAt DATETIME DEFAULT GETDATE(),
                                FOREIGN KEY (UserID) REFERENCES datn.Users(UserID),
                                FOREIGN KEY (ProductID) REFERENCES datn.Products(ProductID)
);

-- 8. Tạo Reviews
CREATE TABLE datn.Reviews (
                              ReviewID INT PRIMARY KEY IDENTITY(1,1),
                              ProductID INT,
                              UserID INT,
                              Rating INT CHECK (Rating BETWEEN 1 AND 5),
                              Comment NVARCHAR(MAX),
                              ReviewDate DATETIME DEFAULT GETDATE(),
                              FOREIGN KEY (ProductID) REFERENCES datn.Products(ProductID),
                              FOREIGN KEY (UserID) REFERENCES datn.Users(UserID)
);




-----------------


USE datn;

-- USERS
INSERT INTO datn.Users (Name, Email, Password, Phone, Address, Role)
VALUES
(N'Nguyễn Văn A', N'a@gmail.com', N'123456', N'0901234567', N'Hà Nội', N'customer'),
(N'Trần Thị B', N'b@gmail.com', N'123456', N'0912345678', N'Hồ Chí Minh', N'admin'),
(N'Lê Văn C', N'c@gmail.com', N'123456', N'0923456789', N'Đà Nẵng', N'customer'),
(N'Phạm Thị D', N'd@gmail.com', N'123456', N'0934567890', N'Hải Phòng', N'customer'),
(N'Hoàng Văn E', N'e@gmail.com', N'123456', N'0945678901', N'Cần Thơ', N'customer');

-- CATEGORIES
INSERT INTO datn.Categories (Name, Description)
VALUES
(N'Điện thoại', N'Các loại điện thoại thông minh'),
(N'Laptop', N'Các loại máy tính xách tay'),
(N'Phụ kiện', N'Phụ kiện điện tử'),
(N'Đồng hồ', N'Đồng hồ thông minh'),
(N'Máy tính bảng', N'Các loại tablet');

-- PRODUCTS
INSERT INTO datn.Products (Name, Description, Price, Stock, Image, CategoryID, Size)
VALUES
(N'iPhone 15', N'Điện thoại Apple mới nhất', 25000000, 10, N'iphone15.jpg', 1, N'128GB'),
(N'Samsung Galaxy S24', N'Điện thoại Samsung cao cấp', 22000000, 15, N's24.jpg', 1, N'256GB'),
(N'MacBook Pro 2023', N'Máy tính xách tay Apple', 45000000, 5, N'macbookpro.jpg', 2, N'16-inch'),
(N'Logitech Mouse', N'Chuột không dây', 500000, 50, N'logitech.jpg', 3, N''),
(N'Apple Watch Series 9', N'Đồng hồ thông minh', 12000000, 8, N'aw9.jpg', 4, N'44mm'),
(N'iPad Air', N'Máy tính bảng Apple', 18000000, 12, N'ipadair.jpg', 5, N'10.9-inch');

-- PAYMENT METHODS
INSERT INTO datn.PaymentMethods (Name, Description)
VALUES
(N'Thanh toán khi nhận hàng', N'Thanh toán trực tiếp khi nhận hàng'),
(N'Chuyển khoản ngân hàng', N'Thanh toán qua tài khoản ngân hàng'),
(N'Ví điện tử', N'Thanh toán qua ví điện tử như Momo, ZaloPay');

-- ORDERS
INSERT INTO datn.Orders (UserID, Total, Status, OrderDate, ShippingAddress, PaymentMethodID)
VALUES
(1, 25000000, N'Cho xac nhan', GETDATE(), N'Hà Nội', 1),
(2, 22000000, N'Dang xu ly', GETDATE(), N'Hồ Chí Minh', 2),
(3, 45000000, N'Dang giao', GETDATE(), N'Đà Nẵng', 3),
(4, 500000, N'Da giao', GETDATE(), N'Hải Phòng', 1),
(5, 12000000, N'Da huy', GETDATE(), N'Cần Thơ', 2);

-- ORDER ITEMS
INSERT INTO datn.OrderItems (OrderID, ProductID, Quantity, Price)
VALUES
(1, 1, 1, 25000000),
(2, 2, 1, 22000000),
(3, 3, 1, 45000000),
(4, 4, 2, 1000000),
(5, 5, 1, 12000000);

-- CART ITEMS
INSERT INTO datn.CartItems (UserID, ProductID, Quantity)
VALUES
(1, 2, 1),
(2, 3, 2),
(3, 4, 1),
(4, 5, 1),
(5, 6, 3);

-- REVIEWS
INSERT INTO datn.Reviews (ProductID, UserID, Rating, Comment)
VALUES
(1, 1, 5, N'Rất hài lòng với sản phẩm!'),
(2, 2, 4, N'Sản phẩm tốt, giao hàng nhanh.'),
(3, 3, 3, N'Chất lượng ổn, giá hơi cao.'),
(4, 4, 5, N'Phụ kiện rất hữu ích.'),
(5, 5, 2, N'Không hài lòng lắm về sản phẩm này.'); 
