
USE DATN3;
GO


-- T?t t?t c? khóa ngo?i
EXEC sp_msforeachtable "ALTER TABLE ? NOCHECK CONSTRAINT ALL"

-- Xoá d? li?u
EXEC sp_msforeachtable "DELETE FROM ?"

-- B?t l?i t?t c? khóa ngo?i
EXEC sp_msforeachtable "ALTER TABLE ? WITH CHECK CHECK CONSTRAINT ALL"

select * from Categories
-- reset id 
DBCC CHECKIDENT ('Categories', RESEED, 0);


--Danh mu?c
INSERT INTO Categories (Name, Description)
VALUES
(N'Áo Polo', N'Các lo?i áo polo...'),
(N'Áo S? Mi', N'Các lo?i áo s? mi...'),
(N'Áo Thun', N'Các lo?i áo thun'),
(N'Áo TankTop', N'Các lo?i áo tanktop'),
(N'Áo Khoác', N'Các lo?i áo khoác'),
(N'Qu?n Short', N'Các lo?i qu?n short'),
(N'Qu?n Jogger', N'Các lo?i qu?n jogger'),
(N'Qu?n Jean', N'Các lo?i qu?n jean');


-- Sa?n phâ?m 
INSERT INTO Products (Name, Description, Price, CategoryID)
VALUES
-- a?o polo
(N'A?o Polo Premium Cotton', N'Ch?t li?u cotton 100%, form regular tôn da?ng', 299000, 1),
(N'A?o Polo Pique Cotton', N'Ch?t li?u cotton 100%, form regular tôn da?ng', 259000, 1),
(N'A?o Polo Ice Cooling', N'Ch?t li?u cotton 100%, form regular tôn da?ng', 229000, 1),
(N'A?o Polo thê? thao', N'Ch?t li?u cotton 100%, form regular tôn da?ng', 199000, 1),
-- A?o s? mi
(N'A?o S? Mi Flannel 100% Cotton', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 299000, 2),
(N'A?o S? Mi Overshirt 100% Cotton', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 359000, 2),
(N'A?o S? Mi Da?i Tay Oxford ', N'Ch?t li?u cotton, thoa?i ma?i thoa?ng ma?t', 399000, 2),
(N'A?o S? Mi Da?i Tay Premium', N'Ch?t li?u cotton , thoa?i ma?i thoa?ng ma?t', 299000, 2),
--a?o thun
(N'A?o Thun 100% Cotton', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 199000, 3),
(N'A?o Thun Gym Thê? Thao', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 159000, 3),
(N'A?o Thun Tshirt', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 199000, 3),
(N'A?o Thun Premium Cotton', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 179000, 3),
--a?o tanktop
(N'A?o TankTop Cha?y Bô?', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 199000, 4),
(N'A?o TankTop AirRush', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 259000, 4),
(N'A?o TankTop Thoa?ng Ma?t Nhanh Khô', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 259000, 4),
(N'A?o TankTop Traning Comfort', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 199000, 4),
--A?o khoa?c
(N'A?o Khoa?c Casual', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 359000, 5),
(N'A?o Khoa?c Thê? Thao', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 399000, 5),
(N'A?o Khoa?c Nylon', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 299000, 5),
(N'A?o Khoa?c ?a N?ng', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 299000, 5),
-- quâ?n short
(N'Quâ?n Short Casual', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 199000, 6),
(N'Quâ?n Short Thê? Thao', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 149000, 6),
(N'Quâ?n Short Travel', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 159000, 6),
(N'Quâ?n Short Chino', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 199000, 6),

-- Quâ?n Jogger
(N'Quâ?n Jogger Th? Thao Fleece Track', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 339000, 7),
(N'Quâ?n Jogger Casual', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 359000, 7),
(N'Quâ?n Jogger Th? Thao ExDry', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 299000, 7),
(N'Quâ?n Jogger Daily Wear', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 259000, 7),

-- quâ?n jean
(N'Quâ?n Jeans Basics Da?ng Straight', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 339000, 8),
(N'Quâ?n Jeans Basics Da?ng Slim', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 339000, 8),
(N'Quâ?n Jeans Basics Da?ng Slim Fit', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 339000, 8),
(N'Quâ?n Jeans Basics Siêu Nhe?', N'Ch?t li?u cotton 100%, thoa?i ma?i thoa?ng ma?t', 339000, 8);


select * from Products
-- a?nh sa?n phâ?m 
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

(22, 'img/products/short2.jpg', 1),
(22, 'img/products/short21.jpg', 0),
(22, 'img/products/short22.jpg', 0),
(22, 'img/products/short23.jpg', 0),

(23, 'img/products/short3.jpg', 1),
(23, 'img/products/short31.jpg', 0),
(23, 'img/products/short32.jpg', 0),
(23, 'img/products/short33.jpg', 0),

(24, 'img/products/short4.jpg', 1),
(24, 'img/products/short41.jpg', 0),
(24, 'img/products/short42.jpg', 0),
(24, 'img/products/short43.jpg', 0),

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
