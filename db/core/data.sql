/*
 * Supermarket Membership Management System - Core Module Test Data
 *
 * Includes sample administrators, members, cards, products, orders, and transaction records.
 */

SET NAMES utf8mb4;

USE `supermarket`;

-- ==================== Administrator ====================
-- Default administrator account: admin / 123456 (BCrypt hashed)
INSERT INTO `manager`(`managerid`,`password`,`name`,`role`,`time`) VALUES 
('admin','$2a$10$WLYHCo.8L3lIfJQrYJwBdO1UpnZCdUt9cmeom.sKcTiWbKyLzHGGK','System Administrator','admin','2025-09-01 11:06:23');

-- ==================== Members ====================

INSERT INTO `member`(`memberid`,`password`,`name`,`sex`,`birthday`) VALUES 
('2525943c9c0f4f6a9530262628df56db','123123','Alice Lee','female','1990-06-01'),
('288bced48b0243bdaefb4d602fa30c62','123123','Ethan Chen','male','1995-03-05'),
('39d86e911df04588bc2c75407318465f','123123','Henry Parker','male','1988-04-02'),
('467e64ae0d1d440495be860179d5be78','123123','Mason Wu','male','1992-04-28'),
('4e0553653d27447896440c4a16eda042','123123','Logan Shaw','male','1985-04-03'),
('567ced95cf1541bc94ccb3dfa767b53f','123123','Kevin Zhang','male','1980-04-09'),
('569b67f5c340462f9c343a98ab6e64db','123123','Victor Sun','male','1975-04-23'),
('64bfda46ead04696978961f399c37e6d','123123','Sophia White','female','1993-04-30'),
('94ce3d7b848649edb01a1e2de6b7e143','123123','Daniel Yu','male','1991-04-04'),
('a15763ae3fd9431cb7c4542d2122d7a1','123123','Jason King','male','1989-04-01'),
('a58b8b60a698446292df3a79d16799c6','123123','Ryan Cole','male','1987-04-29'),
('edabd56c406449a0ab105bb7bfe92346','123123','Emma Violet','female','1994-04-01'),
('edc5c2070d154561b8d5aa94ded0ccd9','123123','Nathan Young','male','1996-04-30'),
('fcac671e85354bbc8086a94102d8c170','123123','Thomas Reed','male','1970-01-01');

-- ==================== Membership Cards ==================== 
('0161c880e711464c9bbb5f699f292e23','567ced95cf1541bc94ccb3dfa767b53f',100,0,1),
('0967a8dd2cc04fc887cb69f35ae831fd','567ced95cf1541bc94ccb3dfa767b53f',6501,0,0),
('09961c29e8494649b29851388ef440fb','569b67f5c340462f9c343a98ab6e64db',1035,10025,0),
('0b5705e0ed8f4b6fafc4126147b7d6fc','4e0553653d27447896440c4a16eda042',999,0,0),
('4f59de19d3534abfb948f4c9a38e53dc','569b67f5c340462f9c343a98ab6e64db',1231,0,0),
('8b06e3dcd4334c368d666081f666b500','567ced95cf1541bc94ccb3dfa767b53f',2551,0,0),
('8c0328f28b60400f983a806d67f705cc','288bced48b0243bdaefb4d602fa30c62',4910,3934,0),
('9a6a2bfb4e95421d92774444c229ddfd','567ced95cf1541bc94ccb3dfa767b53f',100,0,0),
('9c535625f7cc477eb69921849a40871d','edabd56c406449a0ab105bb7bfe92346',1314250,0,0),
('a81128bc8be54aec84b0dadf0b1d6fe1','4e0553653d27447896440c4a16eda042',0,0,0),
('aa20dce977254b599508979d2e667884','567ced95cf1541bc94ccb3dfa767b53f',100,0,1),
('aaa3dcc485144a44ae779d614a808ad0','4e0553653d27447896440c4a16eda042',0,0,0),
('b27a7b32a0c44561abe8104977814152','edabd56c406449a0ab105bb7bfe92346',0,0,0),
('ba0977e470ba4f6fb48b8c157da98b4d','edc5c2070d154561b8d5aa94ded0ccd9',9900,25,0),
('cb09d3ef48f24a14a58cde8ab4bc0a6f','569b67f5c340462f9c343a98ab6e64db',0,0,0),
('d5369d546dd94b68a1ae652b54f55ff7','567ced95cf1541bc94ccb3dfa767b53f',0,0,0),
('f0cc8a64e9744fac89ec05c99b9a13e1','569b67f5c340462f9c343a98ab6e64db',0,0,0),
('f226a06fed11438882f0d3cffd0ece4f','567ced95cf1541bc94ccb3dfa767b53f',180,302,0),
('fb0838a8d15e4306aff60bde281b2f27','94ce3d7b848649edb01a1e2de6b7e143',1314,0,0);

-- ==================== Products ====================

INSERT INTO `product`(`productid`,`name`,`price`,`integral`,`category`,`stock`,`unit`,`time`) VALUES 
(1,'Coca-Cola 330ml',300,5,'Beverage',200,'bottle','2025-09-10 22:31:07'),
(2,'Lay''s Classic Chips',850,15,'Snacks',150,'bag','2025-09-12 16:49:02'),
(3,'Nongfu Spring Water 500ml',200,3,'Beverage',300,'bottle','2025-09-15 16:53:36'),
(4,'Uni-President Beef Noodles',450,8,'Instant Food',500,'bag','2025-09-18 17:04:13'),
(5,'Dove Milk Chocolate',650,12,'Candy',100,'bar','2025-09-20 18:15:05'),
(6,'Oreo Original Cookies',800,15,'Snacks',120,'box','2025-09-25 20:55:11'),
(7,'Mengniu Pure Milk 250ml',350,6,'Dairy',200,'box','2025-10-01 20:56:11'),
(8,'Master Kong Iced Tea 500ml',300,5,'Beverage',180,'bottle','2025-10-05 22:22:20'),
(9,'Want Want Snow Cookies',550,10,'Snacks',160,'bag','2025-10-10 22:25:11'),
(10,'Colgate Toothpaste',1200,20,'Daily Goods',80,'tube','2025-10-15 22:30:33'),
(11,'Head & Shoulders Shampoo',2500,40,'Daily Goods',60,'bottle','2025-10-20 22:33:18'),
(12,'Cotton Towel',1500,25,'Daily Goods',100,'towel','2025-10-25 13:37:18');

-- ==================== Orders ====================

INSERT INTO `orders`(`orderid`,`cardid`,`total_amount`,`total_integral`,`payment_method`,`order_time`,`status`) VALUES 
('20251015001','f226a06fed11438882f0d3cffd0ece4f',900,100,0,'2025-10-15 15:30:00',1),
('20251018001','8b06e3dcd4334c368d666081f666b500',1700,200,0,'2025-10-18 16:45:30',1),
('20251022001','ba0977e470ba4f6fb48b8c157da98b4d',990,25,0,'2025-10-22 17:40:48',1),
('20251101001','09961c29e8494649b29851388ef440fb',1990,225,0,'2025-11-01 21:20:34',1),
('20251115001','8c0328f28b60400f983a806d67f705cc',16066,200,1,'2025-11-15 22:30:51',1);

-- ==================== Order Items ====================

INSERT INTO `order_items`(`item_id`,`orderid`,`productid`,`quantity`,`price`,`subtotal`) VALUES 
(1,'20251015001',1,3,300,900),
(2,'20251018001',1,2,300,600),
(3,'20251018001',3,2,200,400),
(4,'20251018001',6,1,800,800),
(5,'20251022001',1,1,300,300),
(6,'20251022001',2,1,850,850),
(7,'20251101001',1,1,300,300),
(8,'20251101001',2,2,850,1700),
(9,'20251101001',5,1,650,650),
(10,'20251115001',1,10,300,3000),
(11,'20251115001',2,5,850,4250),
(12,'20251115001',7,20,350,7000),
(13,'20251115001',11,2,2500,5000);

-- ==================== Transaction Records ====================

INSERT INTO `record`(`id`,`cardid`,`value`,`time`,`spendtype`,`orderid`,`description`) VALUES 
(1,'f226a06fed11438882f0d3cffd0ece4f',200,'2025-10-10 22:55:06',1,NULL,'Points redemption'),
(2,'f226a06fed11438882f0d3cffd0ece4f',1000,'2025-10-12 22:56:58',0,NULL,'Card top-up'),
(3,'f226a06fed11438882f0d3cffd0ece4f',-900,'2025-10-15 23:01:17',0,'20251015001','Product purchase order'),
(4,'f226a06fed11438882f0d3cffd0ece4f',200,'2025-10-16 23:01:17',1,NULL,'Points redemption'),
(5,'f226a06fed11438882f0d3cffd0ece4f',100,'2025-10-17 23:01:38',0,NULL,'Card top-up'),
(39,'8b06e3dcd4334c368d666081f666b500',500,'2025-10-18 15:00:10',0,NULL,'Card top-up'),
(40,'8b06e3dcd4334c368d666081f666b500',-1700,'2025-10-18 16:45:30',0,'20251018001','Product purchase order'),
(81,'ba0977e470ba4f6fb48b8c157da98b4d',9999,'2025-10-20 17:40:42',0,NULL,'Card top-up'),
(82,'ba0977e470ba4f6fb48b8c157da98b4d',-990,'2025-10-22 17:40:48',0,'20251022001','Product purchase order'),
(85,'09961c29e8494649b29851388ef440fb',1234,'2025-10-28 21:20:18',0,NULL,'Card top-up'),
(88,'09961c29e8494649b29851388ef440fb',-1990,'2025-11-01 21:20:34',0,'20251101001','Product purchase order'),
(110,'8c0328f28b60400f983a806d67f705cc',5000,'2025-11-10 22:30:33',0,NULL,'Card top-up'),
(113,'8c0328f28b60400f983a806d67f705cc',-16066,'2025-11-15 22:30:51',1,'20251115001','Points payment order');

SELECT 'Core module test data inserted successfully!' AS message;
