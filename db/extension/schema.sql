-- Supermarket Management System - Extension Schema
-- Dependency: run core/schema.sql first

SET NAMES utf8mb4;
SET SESSION sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO';

USE `supermarket`;

-- Supplier table
CREATE TABLE IF NOT EXISTS `supplier` (
  `supplier_id` varchar(32) NOT NULL,
  `supplier_name` varchar(100) NOT NULL,
  `contact_person` varchar(50) DEFAULT NULL,
  `contact_phone` varchar(20) DEFAULT NULL,
  `contact_email` varchar(100) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `credit_rating` tinyint(1) NOT NULL DEFAULT 3,
  `status` tinyint(1) NOT NULL DEFAULT 1,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`supplier_id`),
  KEY `idx_supplier_name` (`supplier_name`),
  KEY `idx_supplier_status_rating` (`status`, `credit_rating`),
  CONSTRAINT `chk_supplier_rating` CHECK (`credit_rating` BETWEEN 1 AND 5),
  CONSTRAINT `chk_supplier_status` CHECK (`status` IN (1, 2))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Purchase order table
CREATE TABLE IF NOT EXISTS `purchase_order` (
  `purchase_id` varchar(32) NOT NULL,
  `supplier_id` varchar(32) NOT NULL,
  `order_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expected_date` datetime DEFAULT NULL,
  `actual_date` datetime DEFAULT NULL,
  `total_amount` decimal(10,2) UNSIGNED NOT NULL DEFAULT 0.00,
  `status` tinyint(1) NOT NULL DEFAULT 1,
  `operator` varchar(50) DEFAULT NULL,
  `remark` text,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`purchase_id`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_status` (`status`),
  KEY `idx_order_date` (`order_date`),
  CONSTRAINT `fk_purchase_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`supplier_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_purchase_status` CHECK (`status` IN (1, 2, 3, 4))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Purchase order item table
CREATE TABLE IF NOT EXISTS `purchase_order_item` (
  `item_id` int(11) NOT NULL AUTO_INCREMENT,
  `purchase_id` varchar(32) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) UNSIGNED NOT NULL DEFAULT 0,
  `unit_price` decimal(8,2) UNSIGNED NOT NULL DEFAULT 0.00,
  `total_price` decimal(10,2) UNSIGNED NOT NULL DEFAULT 0.00,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`item_id`),
  KEY `idx_purchase_id` (`purchase_id`),
  KEY `idx_product_id` (`product_id`),
  CONSTRAINT `fk_purchase_item_order` FOREIGN KEY (`purchase_id`) REFERENCES `purchase_order` (`purchase_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_purchase_item_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`productid`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_purchase_item_quantity` CHECK (`quantity` > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Purchase receipt table
CREATE TABLE IF NOT EXISTS `purchase_receipt` (
  `receipt_id` varchar(32) NOT NULL,
  `purchase_id` varchar(32) NOT NULL,
  `receipt_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `operator` varchar(50) DEFAULT NULL,
  `total_quantity` int(11) DEFAULT 0,
  `remark` text,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`receipt_id`),
  KEY `idx_purchase_id` (`purchase_id`),
  KEY `idx_receipt_date` (`receipt_date`),
  CONSTRAINT `fk_receipt_purchase` FOREIGN KEY (`purchase_id`) REFERENCES `purchase_order` (`purchase_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Purchase receipt item table
CREATE TABLE IF NOT EXISTS `purchase_receipt_item` (
  `item_id` int(11) NOT NULL AUTO_INCREMENT,
  `receipt_id` varchar(32) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) DEFAULT 0,
  `actual_quantity` int(11) DEFAULT 0,
  `batch_number` varchar(50) DEFAULT NULL,
  `production_date` date DEFAULT NULL,
  `expiry_date` date DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`item_id`),
  KEY `idx_receipt_id` (`receipt_id`),
  KEY `idx_product_id` (`product_id`),
  CONSTRAINT `fk_receipt_item_receipt` FOREIGN KEY (`receipt_id`) REFERENCES `purchase_receipt` (`receipt_id`),
  CONSTRAINT `fk_receipt_item_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`productid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Purchase return table
CREATE TABLE IF NOT EXISTS `purchase_return` (
  `return_id` varchar(32) NOT NULL,
  `purchase_id` varchar(32) NOT NULL,
  `supplier_id` varchar(32) NOT NULL,
  `return_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `total_amount` decimal(10,2) DEFAULT 0.00,
  `reason` varchar(200) DEFAULT NULL,
  `status` int(1) DEFAULT 1,
  `operator` varchar(50) DEFAULT NULL,
  `remark` text,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`return_id`),
  KEY `idx_purchase_id` (`purchase_id`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_return_date` (`return_date`),
  CONSTRAINT `fk_return_purchase` FOREIGN KEY (`purchase_id`) REFERENCES `purchase_order` (`purchase_id`),
  CONSTRAINT `fk_return_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`supplier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Purchase return item table
CREATE TABLE IF NOT EXISTS `purchase_return_item` (
  `item_id` int(11) NOT NULL AUTO_INCREMENT,
  `return_id` varchar(32) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) DEFAULT 0,
  `unit_price` decimal(8,2) DEFAULT 0.00,
  `total_price` decimal(10,2) DEFAULT 0.00,
  `reason` varchar(200) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`item_id`),
  KEY `idx_return_id` (`return_id`),
  KEY `idx_product_id` (`product_id`),
  CONSTRAINT `fk_return_item_return` FOREIGN KEY (`return_id`) REFERENCES `purchase_return` (`return_id`),
  CONSTRAINT `fk_return_item_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`productid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Promotion table
CREATE TABLE IF NOT EXISTS `promotion` (
  `promotion_id` varchar(32) NOT NULL,
  `promotion_name` varchar(100) NOT NULL,
  `promotion_type` tinyint(1) NOT NULL,
  `description` text,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `min_amount` decimal(8,2) UNSIGNED DEFAULT 0.00,
  `discount_rate` decimal(3,2) UNSIGNED DEFAULT 0.00,
  `discount_amount` decimal(8,2) UNSIGNED DEFAULT 0.00,
  `member_only` tinyint(1) NOT NULL DEFAULT 0,
  `status` tinyint(1) NOT NULL DEFAULT 1,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`promotion_id`),
  KEY `idx_promotion_type` (`promotion_type`),
  KEY `idx_status_type` (`status`, `promotion_type`),
  KEY `idx_time_range` (`start_time`, `end_time`),
  CONSTRAINT `chk_promotion_type` CHECK (`promotion_type` IN (1, 2, 3, 4)),
  CONSTRAINT `chk_promotion_status` CHECK (`status` IN (0, 1, 2, 3)),
  CONSTRAINT `chk_promotion_time` CHECK (`end_time` > `start_time`),
  CONSTRAINT `chk_promotion_discount_rate` CHECK (`discount_rate` BETWEEN 0.00 AND 1.00),
  CONSTRAINT `chk_promotion_member_only` CHECK (`member_only` IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Promotion product table
CREATE TABLE IF NOT EXISTS `promotion_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `promotion_id` varchar(32) NOT NULL,
  `product_id` int(11) NOT NULL,
  `special_price` decimal(8,2) UNSIGNED DEFAULT NULL,
  `buy_quantity` int(11) UNSIGNED NOT NULL DEFAULT 0,
  `free_quantity` int(11) UNSIGNED NOT NULL DEFAULT 0,
  `discount_rate` decimal(3,2) UNSIGNED DEFAULT 0.00,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_promotion_product` (`promotion_id`, `product_id`),
  KEY `idx_product_id` (`product_id`),
  CONSTRAINT `fk_promotion_product_promotion` FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`promotion_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_promotion_product_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`productid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `chk_promo_product_discount_rate` CHECK (`discount_rate` BETWEEN 0.00 AND 1.00)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Coupon table
CREATE TABLE IF NOT EXISTS `coupon` (
  `coupon_id` varchar(32) NOT NULL,
  `coupon_name` varchar(100) NOT NULL,
  `coupon_type` tinyint(1) NOT NULL,
  `denomination` decimal(8,2) UNSIGNED DEFAULT 0.00,
  `discount_rate` decimal(3,2) UNSIGNED DEFAULT 0.00,
  `min_amount` decimal(8,2) UNSIGNED DEFAULT 0.00,
  `valid_days` int(11) UNSIGNED NOT NULL DEFAULT 30,
  `total_quantity` int(11) UNSIGNED NOT NULL DEFAULT 0,
  `used_quantity` int(11) UNSIGNED NOT NULL DEFAULT 0,
  `status` tinyint(1) NOT NULL DEFAULT 1,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`coupon_id`),
  KEY `idx_coupon_type_status` (`coupon_type`, `status`),
  CONSTRAINT `chk_coupon_type` CHECK (`coupon_type` IN (1, 2, 3)),
  CONSTRAINT `chk_coupon_status` CHECK (`status` IN (0, 1)),
  CONSTRAINT `chk_coupon_discount_rate` CHECK (`discount_rate` BETWEEN 0.00 AND 1.00),
  CONSTRAINT `chk_coupon_used_quantity` CHECK (`used_quantity` <= `total_quantity`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- User coupon table
CREATE TABLE IF NOT EXISTS `user_coupon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `coupon_id` varchar(32) NOT NULL,
  `member_id` varchar(50) NOT NULL,
  `card_id` varchar(50) DEFAULT NULL,
  `receive_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `use_time` datetime DEFAULT NULL,
  `order_id` varchar(32) DEFAULT NULL,
  `expire_time` datetime NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  KEY `idx_coupon_id` (`coupon_id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_card_id` (`card_id`),
  KEY `idx_status` (`status`),
  KEY `idx_expire_time` (`expire_time`),
  CONSTRAINT `fk_user_coupon_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`coupon_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_user_coupon_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`memberid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_coupon_card` FOREIGN KEY (`card_id`) REFERENCES `card` (`cardid`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `chk_user_coupon_status` CHECK (`status` IN (1, 2, 3)),
  CONSTRAINT `chk_user_coupon_expire` CHECK (`expire_time` > `receive_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Customer behavior table
CREATE TABLE IF NOT EXISTS `customer_behavior` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `member_id` varchar(50) DEFAULT NULL,
  `card_id` varchar(50) DEFAULT NULL,
  `behavior_type` tinyint(1) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `product_category` varchar(20) DEFAULT NULL,
  `behavior_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `order_amount` decimal(10,2) UNSIGNED DEFAULT 0.00,
  `session_id` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_behavior_type` (`behavior_type`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_behavior_time` (`behavior_time`),
  KEY `idx_product_category` (`product_category`),
  CONSTRAINT `fk_behavior_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`memberid`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_behavior_card` FOREIGN KEY (`card_id`) REFERENCES `card` (`cardid`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_behavior_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`productid`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `chk_behavior_type` CHECK (`behavior_type` IN (1, 2, 3, 4))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Promotion effect table
CREATE TABLE IF NOT EXISTS `promotion_effect` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `promotion_id` varchar(32) NOT NULL,
  `member_id` varchar(50) DEFAULT NULL,
  `card_id` varchar(50) DEFAULT NULL,
  `order_id` varchar(32) DEFAULT NULL,
  `original_amount` decimal(10,2) UNSIGNED NOT NULL DEFAULT 0.00,
  `discount_amount` decimal(10,2) UNSIGNED NOT NULL DEFAULT 0.00,
  `final_amount` decimal(10,2) UNSIGNED NOT NULL DEFAULT 0.00,
  `use_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_promotion_id` (`promotion_id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_use_time` (`use_time`),
  CONSTRAINT `fk_promotion_effect_promotion` FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`promotion_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_promotion_effect_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`memberid`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_promotion_effect_card` FOREIGN KEY (`card_id`) REFERENCES `card` (`cardid`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `chk_promo_effect_amounts` CHECK (`final_amount` <= `original_amount`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET SESSION sql_mode = DEFAULT;

