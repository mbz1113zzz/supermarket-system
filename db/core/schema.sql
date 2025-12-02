-- Supermarket Management System - Core Schema

SET NAMES utf8mb4;
SET SESSION sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO';

CREATE DATABASE IF NOT EXISTS `supermarket`
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `supermarket`;

-- Administrator table
CREATE TABLE IF NOT EXISTS `manager` (
  `managerid` varchar(10) NOT NULL,
  `password` varchar(100) NOT NULL,
  `name` varchar(50) NOT NULL,
  `role` varchar(10) NOT NULL DEFAULT 'operator',
  `time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`managerid`),
  CONSTRAINT `chk_manager_role` CHECK (`role` IN ('admin', 'operator', 'finance'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Member table
CREATE TABLE IF NOT EXISTS `member` (
  `memberid` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `name` varchar(50) NOT NULL,
  `sex` varchar(10) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`memberid`),
  KEY `idx_member_name` (`name`),
  CONSTRAINT `chk_member_sex` CHECK (`sex` IN ('male', 'female') OR `sex` IS NULL)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Membership card table
CREATE TABLE IF NOT EXISTS `card` (
  `cardid` varchar(50) NOT NULL,
  `memberid` varchar(50) NOT NULL,
  `balance` int(11) UNSIGNED DEFAULT 0,
  `integral` int(11) UNSIGNED DEFAULT 0,
  `lose` tinyint(1) DEFAULT 0,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`cardid`),
  KEY `idx_memberid` (`memberid`),
  CONSTRAINT `fk_card_member` FOREIGN KEY (`memberid`) REFERENCES `member` (`memberid`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_card_lose` CHECK (`lose` IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Product table
CREATE TABLE IF NOT EXISTS `product` (
  `productid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `price` int(11) UNSIGNED NOT NULL,
  `integral` int(11) UNSIGNED DEFAULT 0,
  `category` varchar(20) NOT NULL,
  `stock` int(11) UNSIGNED DEFAULT 0,
  `unit` varchar(10) NOT NULL DEFAULT 'pcs',
  `time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`productid`),
  KEY `idx_product_name` (`name`),
  KEY `idx_product_category` (`category`),
  CONSTRAINT `chk_product_price` CHECK (`price` > 0)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Orders table
CREATE TABLE IF NOT EXISTS `orders` (
  `orderid` varchar(32) NOT NULL,
  `cardid` varchar(50) NOT NULL,
  `total_amount` int(11) UNSIGNED NOT NULL DEFAULT 0,
  `total_integral` int(11) UNSIGNED DEFAULT 0,
  `payment_method` tinyint(1) NOT NULL DEFAULT 0,
  `order_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`orderid`),
  KEY `idx_cardid` (`cardid`),
  KEY `idx_order_time` (`order_time`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_orders_card` FOREIGN KEY (`cardid`) REFERENCES `card` (`cardid`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_orders_payment` CHECK (`payment_method` IN (0, 1)),
  CONSTRAINT `chk_orders_status` CHECK (`status` IN (1, 2))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Order items table
CREATE TABLE IF NOT EXISTS `order_items` (
  `item_id` int(11) NOT NULL AUTO_INCREMENT,
  `orderid` varchar(32) NOT NULL,
  `productid` int(11) NOT NULL,
  `quantity` int(11) UNSIGNED NOT NULL DEFAULT 1,
  `price` int(11) UNSIGNED NOT NULL,
  `subtotal` int(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`item_id`),
  KEY `idx_orderid` (`orderid`),
  KEY `idx_productid` (`productid`),
  CONSTRAINT `fk_order_items_order` FOREIGN KEY (`orderid`) REFERENCES `orders` (`orderid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_order_items_product` FOREIGN KEY (`productid`) REFERENCES `product` (`productid`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_order_items_quantity` CHECK (`quantity` > 0)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Transaction record table
CREATE TABLE IF NOT EXISTS `record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cardid` varchar(50) NOT NULL,
  `value` int(11) NOT NULL,
  `time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `spendtype` tinyint(1) NOT NULL DEFAULT 0,
  `orderid` varchar(32) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_cardid` (`cardid`),
  KEY `idx_time` (`time`),
  KEY `idx_orderid` (`orderid`),
  CONSTRAINT `fk_record_card` FOREIGN KEY (`cardid`) REFERENCES `card` (`cardid`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_record_order` FOREIGN KEY (`orderid`) REFERENCES `orders` (`orderid`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `chk_record_spendtype` CHECK (`spendtype` IN (0, 1))
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET SESSION sql_mode = DEFAULT;

