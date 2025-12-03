/*
 * Supermarket Membership Management System - Extension Module Test Data
 *
 * Includes suppliers, purchase orders, promotions, coupons, and customer behaviors.
 * Depends on core/data.sql being executed first.
 */

SET NAMES utf8mb4;

USE `supermarket`;

-- ==================== Suppliers ====================

INSERT INTO `supplier` (`supplier_id`, `supplier_name`, `contact_person`, `contact_phone`, `address`, `credit_rating`, `status`) VALUES
('SUP001', 'Coca-Cola Beverages China', 'Michael Zhang', '13800138001', 'Chaoyang, Beijing', 5, 1),
('SUP002', 'Pepsi Food Group', 'Linda Li', '13900139002', 'Pudong, Shanghai', 4, 1),
('SUP003', 'Uni-President Corp.', 'David Wang', '13700137003', 'Tianhe, Guangzhou', 4, 1),
('SUP004', 'Master Kong Holdings', 'Grace Zhao', '13600136004', 'Binhai, Tianjin', 3, 1),
('SUP005', 'Nestle China', 'Eric Chen', '13500135005', 'Nanshan, Shenzhen', 5, 1);

-- ==================== Purchase Orders ====================

INSERT INTO `purchase_order` (`purchase_id`, `supplier_id`, `order_date`, `expected_date`, `actual_date`, `total_amount`, `status`, `operator`, `remark`) VALUES
('PO20251201001', 'SUP001', '2025-11-28 10:00:00', '2025-12-01 18:00:00', NULL, 5800.00, 1, 'admin', 'Routine replenishment'),
('PO20251201002', 'SUP002', '2025-11-29 14:30:00', '2025-12-02 12:00:00', NULL, 3200.50, 2, 'admin', 'Holiday stocking'),
('PO20251201003', 'SUP003', '2025-11-30 09:15:00', '2025-12-03 10:00:00', '2025-11-30 16:00:00', 1500.00, 3, 'admin', 'Urgent order received');

-- ==================== Promotions ====================

INSERT INTO `promotion` (`promotion_id`, `promotion_name`, `promotion_type`, `description`, `start_time`, `end_time`, `min_amount`, `discount_rate`, `discount_amount`, `member_only`, `status`) VALUES
('PRO001', 'Summer Beverage Sale', 2, 'All drinks are 20% off.', '2025-06-01 00:00:00', '2025-08-31 23:59:59', 0.00, 0.80, 0.00, 0, 1),
('PRO002', 'Members Save 20 on 100', 1, 'Members get ¥20 off every ¥100 spent.', '2025-01-01 00:00:00', '2025-12-31 23:59:59', 100.00, 0.00, 20.00, 1, 1),
('PRO003', 'Snack Bundle BOGO', 3, 'Buy two selected snacks and get one free.', '2025-07-01 00:00:00', '2025-07-31 23:59:59', 0.00, 0.00, 0.00, 0, 1);

-- ==================== Coupons ====================

INSERT INTO `coupon` (`coupon_id`, `coupon_name`, `coupon_type`, `denomination`, `min_amount`, `valid_days`, `total_quantity`, `used_quantity`) VALUES
('COUP001', 'New User ¥50 Coupon', 1, 50.00, 200.00, 30, 1000, 120),
('COUP002', '20% Off Coupon', 2, 0.00, 100.00, 60, 500, 80),
('COUP003', 'Member Birthday ¥100 Coupon', 1, 100.00, 300.00, 15, 2000, 350);

-- ==================== Customer Behaviors ====================
-- behavior_type: 1=view, 2=add-to-cart, 3=purchase, 4=favorite

-- Kevin Zhang's journey
INSERT INTO `customer_behavior` (member_id, behavior_type, product_id, product_category, behavior_time, order_amount, session_id) VALUES
('567ced95cf1541bc94ccb3dfa767b53f', 1, 1, 'Beverage', DATE_SUB(NOW(), INTERVAL 5 DAY), NULL, 'S001'),
('567ced95cf1541bc94ccb3dfa767b53f', 2, 1, 'Beverage', DATE_SUB(NOW(), INTERVAL 5 DAY), NULL, 'S001'),
('567ced95cf1541bc94ccb3dfa767b53f', 3, 1, 'Beverage', DATE_SUB(NOW(), INTERVAL 5 DAY), 9.00, 'S001'),
('567ced95cf1541bc94ccb3dfa767b53f', 1, 2, 'Snacks', DATE_SUB(NOW(), INTERVAL 4 DAY), NULL, 'S002'),
('567ced95cf1541bc94ccb3dfa767b53f', 4, 2, 'Snacks', DATE_SUB(NOW(), INTERVAL 4 DAY), NULL, 'S002'),
('567ced95cf1541bc94ccb3dfa767b53f', 3, 2, 'Snacks', DATE_SUB(NOW(), INTERVAL 4 DAY), 8.50, 'S002');

-- Logan Shaw's journey
INSERT INTO `customer_behavior` (member_id, behavior_type, product_id, product_category, behavior_time, order_amount, session_id) VALUES
('4e0553653d27447896440c4a16eda042', 1, 3, 'Beverage', DATE_SUB(NOW(), INTERVAL 3 DAY), NULL, 'S003'),
('4e0553653d27447896440c4a16eda042', 1, 4, 'Instant Food', DATE_SUB(NOW(), INTERVAL 3 DAY), NULL, 'S003'),
('4e0553653d27447896440c4a16eda042', 2, 4, 'Instant Food', DATE_SUB(NOW(), INTERVAL 3 DAY), NULL, 'S003'),
('4e0553653d27447896440c4a16eda042', 3, 4, 'Instant Food', DATE_SUB(NOW(), INTERVAL 3 DAY), 13.50, 'S003'),
('4e0553653d27447896440c4a16eda042', 1, 7, 'Dairy', DATE_SUB(NOW(), INTERVAL 2 DAY), NULL, 'S004'),
('4e0553653d27447896440c4a16eda042', 3, 7, 'Dairy', DATE_SUB(NOW(), INTERVAL 2 DAY), 7.00, 'S004');

-- Nathan Young's journey
INSERT INTO `customer_behavior` (member_id, behavior_type, product_id, product_category, behavior_time, order_amount, session_id) VALUES
('edc5c2070d154561b8d5aa94ded0ccd9', 1, 5, 'Candy', DATE_SUB(NOW(), INTERVAL 6 DAY), NULL, 'S005'),
('edc5c2070d154561b8d5aa94ded0ccd9', 4, 5, 'Candy', DATE_SUB(NOW(), INTERVAL 6 DAY), NULL, 'S005'),
('edc5c2070d154561b8d5aa94ded0ccd9', 1, 6, 'Snacks', DATE_SUB(NOW(), INTERVAL 5 DAY), NULL, 'S006'),
('edc5c2070d154561b8d5aa94ded0ccd9', 2, 6, 'Snacks', DATE_SUB(NOW(), INTERVAL 5 DAY), NULL, 'S006'),
('edc5c2070d154561b8d5aa94ded0ccd9', 3, 6, 'Snacks', DATE_SUB(NOW(), INTERVAL 5 DAY), 8.00, 'S006');

-- Ethan Chen's journey
INSERT INTO `customer_behavior` (member_id, behavior_type, product_id, product_category, behavior_time, order_amount, session_id) VALUES
('288bced48b0243bdaefb4d602fa30c62', 1, 8, 'Beverage', DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, 'S007'),
('288bced48b0243bdaefb4d602fa30c62', 1, 9, 'Snacks', DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, 'S007'),
('288bced48b0243bdaefb4d602fa30c62', 2, 9, 'Snacks', DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, 'S007'),
('288bced48b0243bdaefb4d602fa30c62', 3, 8, 'Beverage', DATE_SUB(NOW(), INTERVAL 1 DAY), 3.00, 'S007'),
('288bced48b0243bdaefb4d602fa30c62', 3, 9, 'Snacks', DATE_SUB(NOW(), INTERVAL 1 DAY), 5.50, 'S007');

-- Victor Sun's journey
INSERT INTO `customer_behavior` (member_id, behavior_type, product_id, product_category, behavior_time, order_amount, session_id) VALUES
('569b67f5c340462f9c343a98ab6e64db', 1, 10, 'Daily Goods', NOW(), NULL, 'S008'),
('569b67f5c340462f9c343a98ab6e64db', 1, 11, 'Daily Goods', NOW(), NULL, 'S008'),
('569b67f5c340462f9c343a98ab6e64db', 2, 10, 'Daily Goods', NOW(), NULL, 'S008'),
('569b67f5c340462f9c343a98ab6e64db', 4, 11, 'Daily Goods', NOW(), NULL, 'S008'),
('569b67f5c340462f9c343a98ab6e64db', 3, 10, 'Daily Goods', NOW(), 12.00, 'S008');

SELECT 'Extension module test data inserted successfully!' AS message;
