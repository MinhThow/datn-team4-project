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

select *From Categories


CREATE TABLE Products (
    ProductID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(150) NOT NULL,
    Description NVARCHAR(MAX),
    Price DECIMAL(10,2) NOT NULL,
    Stock INT DEFAULT 0,
    Image NVARCHAR(255),
    CategoryID INT,
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID)
);
ALTER TABLE Products
ADD Size NVARCHAR(50);

CREATE TABLE Orders (
    OrderID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT,
    Total DECIMAL(10,2),
    Status NVARCHAR(20) DEFAULT 'Chờ xác nhận' CHECK (Status IN ('Chờ xác nhận', 'Đang xử lý', 'Đang giao', 'Đã giao', 'Đã hủy','Trả hàng')),
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


-- Phương thức thanh toán
CREATE TABLE PaymentMethods (
    PaymentMethodID INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(100) NOT NULL,        
    Description NVARCHAR(MAX)
);

select * from Products
select * From Categories


-- Trigger sau INSERT
CREATE TRIGGER trg_OrderItems_AfterInsert
ON OrderItems
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE Orders
    SET Total = (
        SELECT SUM(Quantity * Price)
        FROM OrderItems
        WHERE OrderItems.OrderID = inserted.OrderID
    )
    FROM Orders
    INNER JOIN inserted
    ON Orders.OrderID = inserted.OrderID;
END;
GO

-- Trigger sau UPDATE
CREATE TRIGGER trg_OrderItems_AfterUpdate
ON OrderItems
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE Orders
    SET Total = (
        SELECT SUM(Quantity * Price)
        FROM OrderItems
        WHERE OrderItems.OrderID = inserted.OrderID
    )
    FROM Orders
    INNER JOIN inserted
    ON Orders.OrderID = inserted.OrderID;
END;
GO

-- Trigger sau DELETE
CREATE TRIGGER trg_OrderItems_AfterDelete
ON OrderItems
AFTER DELETE
AS
BEGIN
    SET NOCOUNT ON;

    UPDATE Orders
    SET Total = (
        SELECT COALESCE(SUM(Quantity * Price), 0)
        FROM OrderItems
        WHERE OrderItems.OrderID = deleted.OrderID
    )
    FROM Orders
    INNER JOIN deleted
    ON Orders.OrderID = deleted.OrderID;
END;
GO

-- DỮ LIỆU MẪU CHO WEBSITE THỜI TRANG

-- USERS
INSERT INTO Users (Name, Email, Password, Phone, Address, Role)
VALUES
-- Admin
(N'Nguyễn Minh Thơ', N'minhtho.002022@gmail.com', N'$2a$10$wdiNfp58as8CFI9.kWD9IuPDYLpe836HZ0rVs8/2FIAFcK1ZyCpWO', N'0703285661', N'Cao đẳng FPT', N'admin'),
-- Customers
(N'Nguyễn Thị Mai', N'mai@gmail.com', N'123456', N'0901234567', N'123 Đường Nguyễn Huệ, Q1, TP.HCM', N'customer'),
(N'Lê Thị Hoa', N'hoa@gmail.com', N'123456', N'0923456789', N'789 Đường Hai Bà Trưng, Q3, TP.HCM', N'customer'),
(N'Phạm Minh Tuấn', N'tuan@gmail.com', N'123456', N'0934567890', N'321 Đường Pasteur, Q3, TP.HCM', N'customer'),
(N'Hoàng Thị Lan', N'lan@gmail.com', N'123456', N'0945678901', N'654 Đường Nguyễn Trãi, Q5, TP.HCM', N'customer');

-- CATEGORIES - Thời trang
INSERT INTO Categories (Name, Description)
VALUES
(N'Áo nam', N'Các loại áo dành cho nam giới: áo sơ mi, áo thun, áo khoác'),
(N'Áo nữ', N'Các loại áo dành cho nữ giới: áo sơ mi, áo thun, áo kiểu, áo khoác'),
(N'Quần nam', N'Quần dài, quần short, quần jean dành cho nam'),
(N'Quần nữ', N'Quần dài, quần short, váy, chân váy dành cho nữ'),
(N'Phụ kiện', N'Túi xách, giày dép, thắt lưng, mũ nón');

-- PAYMENT METHODS
INSERT INTO PaymentMethods (Name, Description)
VALUES
(N'Thanh toán khi nhận hàng (COD)', N'Thanh toán trực tiếp khi nhận hàng tại nhà'),
(N'Chuyển khoản ngân hàng', N'Thanh toán qua tài khoản ngân hàng Vietcombank, Techcombank, BIDV'),
(N'Ví điện tử', N'Thanh toán qua MoMo, ZaloPay, VNPay');

-- PRODUCTS - Sản phẩm thời trang
INSERT INTO Products (Name, Description, Price, Stock, Image, CategoryID, Size)
VALUES
(N'Áo Sơ Mi Nam Trắng Oxford', N'Áo sơ mi nam chất liệu cotton cao cấp, thiết kế thanh lịch, phù hợp đi làm và dự tiệc', 450000, 25, N'ao-so-mi-nam-trang.jpg', 1, N'M, L, XL'),
(N'Áo Thun Nữ Oversize Basic', N'Áo thun nữ form rộng, chất liệu cotton mềm mại, phong cách trẻ trung', 280000, 30, N'ao-thun-nu-oversize.jpg', 2, N'S, M, L'),
(N'Quần Jean Nam Slim Fit', N'Quần jean nam ôm vừa phải, chất liệu denim cao cấp, bền đẹp theo thời gian', 650000, 20, N'quan-jean-nam-slim.jpg', 3, N'29, 30, 31, 32'),
(N'Chân Váy Nữ Xòe Midi', N'Chân váy xòe dài qua gối, chất liệu voan mềm mại, phù hợp đi làm và dạo phố', 380000, 15, N'chan-vay-nu-xoe.jpg', 4, N'S, M, L'),
(N'Túi Xách Nữ Da Thật', N'Túi xách tay nữ da thật cao cấp, thiết kế sang trọng, nhiều ngăn tiện dụng', 1200000, 12, N'tui-xach-nu-da.jpg', 5, N'One Size'),
(N'Áo Khoác Bomber Nam', N'Áo khoác bomber nam phong cách streetwear, chất liệu polyester chống gió', 750000, 18, N'ao-khoac-bomber-nam.jpg', 1, N'M, L, XL'),
(N'Đầm Maxi Nữ Hoa Nhí', N'Đầm maxi dài tay, họa tiết hoa nhí nữ tính, phù hợp dự tiệc và đi chơi', 590000, 22, N'dam-maxi-nu-hoa.jpg', 2, N'S, M, L, XL'),
(N'Quần Short Nam Kaki', N'Quần short nam chất liệu kaki, phong cách casual, thoải mái cho mùa hè', 320000, 35, N'quan-short-nam-kaki.jpg', 3, N'29, 30, 31, 32'),
(N'Giày Sneaker Nữ Trắng', N'Giày sneaker nữ màu trắng basic, đế cao su êm ái, phù hợp mọi outfit', 890000, 28, N'giay-sneaker-nu-trang.jpg', 5, N'36, 37, 38, 39'),
(N'Áo Len Nữ Cổ Lọ', N'Áo len nữ cổ lọ ấm áp, chất liệu wool mềm mại, thích hợp mùa đông', 480000, 20, N'ao-len-nu-co-lo.jpg', 2, N'S, M, L');

-- ORDERS
INSERT INTO Orders (UserID, Total, Status, OrderDate, ShippingAddress, PaymentMethodID)
VALUES
(2, 730000, N'Chờ xác nhận', GETDATE(), N'123 Đường Nguyễn Huệ, Q1, TP.HCM', 1),
(3, 1200000, N'Đang xử lý', GETDATE(), N'789 Đường Hai Bà Trưng, Q3, TP.HCM', 2),
(4, 970000, N'Đang giao', GETDATE(), N'321 Đường Pasteur, Q3, TP.HCM', 3),
(5, 480000, N'Đã giao', GETDATE(), N'654 Đường Nguyễn Trãi, Q5, TP.HCM', 1),
(2, 650000, N'Đã hủy', GETDATE(), N'123 Đường Nguyễn Huệ, Q1, TP.HCM', 2);

-- ORDER ITEMS
INSERT INTO OrderItems (OrderID, ProductID, Quantity, Price)
VALUES
(1, 1, 1, 450000), -- Áo sơ mi nam
(1, 2, 1, 280000), -- Áo thun nữ
(2, 5, 1, 1200000), -- Túi xách da
(3, 6, 1, 750000), -- Áo khoác bomber
(3, 8, 1, 320000), -- Quần short kaki
(4, 10, 1, 480000), -- Áo len nữ
(5, 3, 1, 650000); -- Quần jean nam

-- CART ITEMS
INSERT INTO CartItems (UserID, ProductID, Quantity)
VALUES
(2, 4, 1), -- Chân váy nữ
(3, 7, 1), -- Đầm maxi
(4, 9, 1), -- Giày sneaker
(5, 1, 2), -- Áo sơ mi nam x2
(2, 2, 1); -- Áo thun nữ

-- REVIEWS
INSERT INTO Reviews (ProductID, UserID, Rating, Comment)
VALUES
(1, 2, 5, N'Áo sơ mi chất lượng tốt, vải mềm mại, form chuẩn. Rất hài lòng!'),
(2, 3, 4, N'Áo thun đẹp, form oversize vừa ý, màu sắc như hình. Sẽ mua thêm!'),
(5, 4, 5, N'Túi xách da thật, thiết kế sang trọng, đóng gói cẩn thận. Tuyệt vời!'),
(6, 5, 4, N'Áo khoác bomber đẹp, chất liệu tốt nhưng hơi nhỏ so với size chart.'),
(10, 2, 5, N'Áo len ấm, mềm mại, màu sắc đẹp. Phù hợp mùa đông Sài Gòn.'),
(3, 3, 3, N'Quần jean ổn, nhưng màu hơi khác so với hình. Chất lượng tạm được.'),
(7, 4, 5, N'Đầm maxi xinh xắn, họa tiết hoa nhí dễ thương, vải mát mẻ.'),
(9, 5, 4, N'Giày sneaker thoải mái, đi êm chân, màu trắng dễ phối đồ.');

select * from users
go

INSERT INTO Products (Name, Description, Price, Stock, Image, CategoryID, Size)
VALUES
(N'Áo thun nam cổ tròn', N'Áo thun cotton 100%, thoáng mát', 199000, 50, N'/images/ao-thun-nam-1.jpg', 1, N'M'),
(N'Giày thể thao trắng', N'Giày thể thao chạy bộ, đế mềm', 899000, 30, N'/images/giay-the-thao-1.jpg', 1, N'42'),
(N'Túi xách da cao cấp', N'Túi da thật, màu nâu sang trọng', 1299000, 10, N'/images/tui-xach-da-1.jpg', 1, N'Free'),
(N'Quần jean nam', N'Quần jean ống suông, màu xanh đậm', 450000, 40, N'/images/quan-jean-1.jpg', 1, N'32'),
(N'Balo laptop', N'Balo chống sốc, chống nước, đựng laptop 15.6 inch', 599000, 25, N'/images/balo-laptop-1.jpg', 1, N'Free'),
(N'Giày cao gót nữ', N'Giày cao gót màu đen, da mềm', 699000, 20, N'/images/giay-cao-got-1.jpg', 1, N'38');



select * from Products
select * from Users