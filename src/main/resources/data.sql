-- CATEGORIES - Thời trang
INSERT INTO Categories (Name, Description) VALUES
('Áo nam', 'Các loại áo dành cho nam giới: áo sơ mi, áo thun, áo khoác'),
('Áo nữ', 'Các loại áo dành cho nữ giới: áo sơ mi, áo thun, áo kiểu, áo khoác'),
('Quần nam', 'Quần dài, quần short, quần jean dành cho nam'),
('Quần nữ', 'Quần dài, quần short, váy, chân váy dành cho nữ'),
('Phụ kiện', 'Túi xách, giày dép, thắt lưng, mũ nón');

-- PAYMENT METHODS
INSERT INTO PaymentMethods (Name, Description) VALUES
('Thanh toán khi nhận hàng (COD)', 'Thanh toán trực tiếp khi nhận hàng tại nhà'),
('Chuyển khoản ngân hàng', 'Thanh toán qua tài khoản ngân hàng Vietcombank, Techcombank, BIDV'),
('Ví điện tử', 'Thanh toán qua MoMo, ZaloPay, VNPay');

-- USERS
INSERT INTO Users (Name, Email, Password, Phone, Address, Role, CreatedAt) VALUES
('Nguyễn Minh Thơ', 'minhtho.002022@gmail.com', '$2a$10$wdiNfp58as8CFI9.kWD9IuPDYLpe836HZ0rVs8/2FIAFcK1ZyCpWO', '0703285661', 'Cao đẳng FPT', 'admin', CURRENT_TIMESTAMP),
('Nguyễn Thị Mai', 'mai@gmail.com', '123456', '0901234567', '123 Đường Nguyễn Huệ, Q1, TP.HCM', 'customer', CURRENT_TIMESTAMP),
('Lê Thị Hoa', 'hoa@gmail.com', '123456', '0923456789', '789 Đường Hai Bà Trưng, Q3, TP.HCM', 'customer', CURRENT_TIMESTAMP),
('Phạm Minh Tuấn', 'tuan@gmail.com', '123456', '0934567890', '321 Đường Pasteur, Q3, TP.HCM', 'customer', CURRENT_TIMESTAMP),
('Hoàng Thị Lan', 'lan@gmail.com', '123456', '0945678901', '654 Đường Nguyễn Trãi, Q5, TP.HCM', 'customer', CURRENT_TIMESTAMP);

-- PRODUCTS - Sản phẩm thời trang (mapped với ảnh có sẵn)
INSERT INTO Products (Name, Description, Price, OldPrice, Stock, Image, CategoryID, Size) VALUES
('Áo Sơ Mi Nam Trắng Oxford', 'Áo sơ mi nam chất liệu cotton cao cấp, thiết kế thanh lịch, phù hợp đi làm và dự tiệc', 450000, 550000, 25, 'img/product/product-1.jpg', 1, 'M, L, XL'),
('Áo Thun Nữ Oversize Basic', 'Áo thun nữ form rộng, chất liệu cotton mềm mại, phong cách trẻ trung', 280000, 350000, 30, 'img/product/product-2.jpg', 2, 'S, M, L'),
('Quần Jean Nam Slim Fit', 'Quần jean nam ôm vừa phải, chất liệu denim cao cấp, bền đẹp theo thời gian', 650000, 750000, 20, 'img/product/product-3.jpg', 3, '29, 30, 31, 32'),
('Chân Váy Nữ Xòe Midi', 'Chân váy xòe dài qua gối, chất liệu voan mềm mại, phù hợp đi làm và dạo phố', 380000, 450000, 15, 'img/product/product-4.jpg', 4, 'S, M, L'),
('Túi Xách Nữ Da Thật', 'Túi xách tay nữ da thật cao cấp, thiết kế sang trọng, nhiều ngăn tiện dụng', 1200000, 1500000, 12, 'img/product/product-5.jpg', 5, 'One Size'),
('Áo Khoác Bomber Nam', 'Áo khoác bomber nam phong cách streetwear, chất liệu polyester chống gió', 750000, 890000, 18, 'img/product/product-6.jpg', 1, 'M, L, XL'),
('Đầm Maxi Nữ Hoa Nhí', 'Đầm maxi dài tay, họa tiết hoa nhí nữ tính, phù hợp dự tiệc và đi chơi', 590000, 690000, 22, 'img/product/product-7.jpg', 2, 'S, M, L, XL'),
('Quần Short Nam Kaki', 'Quần short nam chất liệu kaki, phong cách casual, thoải mái cho mùa hè', 320000, 380000, 35, 'img/product/product-8.jpg', 3, '29, 30, 31, 32'),
('Giày Sneaker Nữ Trắng', 'Giày sneaker nữ màu trắng basic, đế cao su êm ái, phù hợp mọi outfit', 890000, 990000, 28, 'img/product/product-9.jpg', 5, '36, 37, 38, 39'),
('Áo Len Nữ Cổ Lọ', 'Áo len nữ cổ lọ ấm áp, chất liệu wool mềm mại, thích hợp mùa đông', 480000, 580000, 20, 'img/product/product-10.jpg', 2, 'S, M, L');

-- ORDERS
INSERT INTO Orders (UserID, Total, Status, OrderDate, ShippingAddress, PaymentMethodID) VALUES
(2, 730000, 'Chờ xác nhận', CURRENT_TIMESTAMP, '123 Đường Nguyễn Huệ, Q1, TP.HCM', 1),
(3, 1200000, 'Đang xử lý', CURRENT_TIMESTAMP, '789 Đường Hai Bà Trưng, Q3, TP.HCM', 2),
(4, 970000, 'Đang giao', CURRENT_TIMESTAMP, '321 Đường Pasteur, Q3, TP.HCM', 3),
(5, 480000, 'Đã giao', CURRENT_TIMESTAMP, '654 Đường Nguyễn Trãi, Q5, TP.HCM', 1),
(2, 650000, 'Đã hủy', CURRENT_TIMESTAMP, '123 Đường Nguyễn Huệ, Q1, TP.HCM', 2);

-- ORDER ITEMS
INSERT INTO OrderItems (OrderID, ProductID, Quantity, Price) VALUES
(1, 1, 1, 450000),
(1, 2, 1, 280000),
(2, 5, 1, 1200000),
(3, 6, 1, 750000),
(3, 8, 1, 320000),
(4, 10, 1, 480000),
(5, 3, 1, 650000);

-- CART ITEMS
INSERT INTO CartItems (UserID, ProductID, Quantity, AddedAt) VALUES
(2, 4, 1, CURRENT_TIMESTAMP),
(3, 7, 1, CURRENT_TIMESTAMP),
(4, 9, 1, CURRENT_TIMESTAMP),
(5, 1, 2, CURRENT_TIMESTAMP),
(2, 2, 1, CURRENT_TIMESTAMP);

-- REVIEWS
INSERT INTO Reviews (ProductID, UserID, Rating, Comment, ReviewDate) VALUES
(1, 2, 5, 'Áo sơ mi chất lượng tốt, vải mềm mại, form chuẩn. Rất hài lòng!', CURRENT_TIMESTAMP),
(2, 3, 4, 'Áo thun đẹp, form oversize vừa ý, màu sắc như hình. Sẽ mua thêm!', CURRENT_TIMESTAMP),
(5, 4, 5, 'Túi xách da thật, thiết kế sang trọng, đóng gói cẩn thận. Tuyệt vời!', CURRENT_TIMESTAMP),
(6, 5, 4, 'Áo khoác bomber đẹp, chất liệu tốt nhưng hơi nhỏ so với size chart.', CURRENT_TIMESTAMP),
(10, 2, 5, 'Áo len ấm, mềm mại, màu sắc đẹp. Phù hợp mùa đông Sài Gòn.', CURRENT_TIMESTAMP),
(3, 3, 3, 'Quần jean ổn, nhưng màu hơi khác so với hình. Chất lượng tạm được.', CURRENT_TIMESTAMP),
(7, 4, 5, 'Đầm maxi xinh xắn, họa tiết hoa nhí dễ thương, vải mát mẻ.', CURRENT_TIMESTAMP),
(9, 5, 4, 'Giày sneaker thoải mái, đi êm chân, màu trắng dễ phối đồ.', CURRENT_TIMESTAMP); 