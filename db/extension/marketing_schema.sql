-- Marketing Module Schema
-- Dependency: run core/schema.sql first

SET NAMES utf8mb4;
USE `supermarket`;

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
  KEY `idx_time_range` (`start_time`, `end_time`)
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
  CONSTRAINT `fk_promotion_product_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`productid`) ON DELETE CASCADE ON UPDATE CASCADE
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
  KEY `idx_coupon_type_status` (`coupon_type`, `status`)
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
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_user_coupon_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`coupon_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_user_coupon_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`memberid`) ON DELETE CASCADE ON UPDATE CASCADE
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
  KEY `idx_behavior_time` (`behavior_time`),
  CONSTRAINT `fk_behavior_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`memberid`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
