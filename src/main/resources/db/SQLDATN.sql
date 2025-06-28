
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


INSERT INTO Users (Name, Email, Password, Phone, Address, Role)
VALUES
-- 2 admin
(N'Nguyễn Minh Thơ', N'minhtho.002022@gmail.com', N'123456', N'0703285661', N'Cao đẳng FPT', N'admin'),

-- 2 customer
(N'Phạm Văn C', N'user1@example.com', N'user123', N'0909123456', N'789 Đường Pasteur, Q3, TP.HCM', N'customer'),
(N'Lê Thị D', N'user2@example.com', N'qwerty', N'0933445566', N'321 Đường Hai Bà Trưng, Q1, TP.HCM', N'customer');


select * from users

UPDATE Users
SET Password = N'$2a$10$wdiNfp58as8CFI9.kWD9IuPDYLpe836HZ0rVs8/2FIAFcK1ZyCpWO'
WHERE UserID = 1;
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