-- Test Data Generator
-- scale_factor: 1=10k, 5=50k orders

SET NAMES utf8mb4;
USE `supermarket`;

-- PART 1: Test Data Generation Procedures

DELIMITER $$

DROP PROCEDURE IF EXISTS `generate_test_members`$$
DROP PROCEDURE IF EXISTS `generate_test_cards`$$
DROP PROCEDURE IF EXISTS `generate_test_products`$$
DROP PROCEDURE IF EXISTS `generate_test_orders`$$
DROP PROCEDURE IF EXISTS `generate_test_order_items`$$
DROP PROCEDURE IF EXISTS `generate_test_records`$$
DROP PROCEDURE IF EXISTS `generate_test_customer_behaviors`$$
DROP PROCEDURE IF EXISTS `generate_all_test_data`$$
DROP PROCEDURE IF EXISTS `clear_test_data`$$
CREATE PROCEDURE `generate_test_members`(IN num_rows INT)
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE batch_size INT DEFAULT 1000;
    DECLARE current_batch INT DEFAULT 0;
    
    SET @start_time = NOW();
    
    -- Disable foreign key checks for faster inserts
    SET FOREIGN_KEY_CHECKS = 0;
    SET autocommit = 0;
    
    WHILE i <= num_rows DO
        INSERT INTO `member` (`memberid`, `password`, `name`, `sex`, `birthday`, `create_time`)
        VALUES (
            CONCAT('TEST_M', LPAD(i, 8, '0')),
            '$2a$10$testpasswordhash',
            CONCAT('TestMember', i),
            IF(i % 2 = 0, 'male', 'female'),
            DATE_SUB(CURRENT_DATE, INTERVAL FLOOR(RAND() * 15000) DAY),
            DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY)
        );
        
        SET current_batch = current_batch + 1;
        IF current_batch >= batch_size THEN
            COMMIT;
            SET current_batch = 0;
        END IF;
        
        SET i = i + 1;
    END WHILE;
    
    COMMIT;
    SET FOREIGN_KEY_CHECKS = 1;
    SET autocommit = 1;
    
    SELECT CONCAT('Generated ', num_rows, ' members in ', TIMEDIFF(NOW(), @start_time)) AS result;
END$$

CREATE PROCEDURE `generate_test_cards`(IN num_rows INT)
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE batch_size INT DEFAULT 1000;
    DECLARE current_batch INT DEFAULT 0;
    
    SET @start_time = NOW();
    SET FOREIGN_KEY_CHECKS = 0;
    SET autocommit = 0;
    
    WHILE i <= num_rows DO
        INSERT INTO `card` (`cardid`, `memberid`, `balance`, `integral`, `lose`, `create_time`)
        VALUES (
            CONCAT('TEST_C', LPAD(i, 8, '0')),
            CONCAT('TEST_M', LPAD(i, 8, '0')),
            FLOOR(RAND() * 100000),
            FLOOR(RAND() * 50000),
            0,
            DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY)
        );
        
        SET current_batch = current_batch + 1;
        IF current_batch >= batch_size THEN
            COMMIT;
            SET current_batch = 0;
        END IF;
        
        SET i = i + 1;
    END WHILE;
    
    COMMIT;
    SET FOREIGN_KEY_CHECKS = 1;
    SET autocommit = 1;
    
    SELECT CONCAT('Generated ', num_rows, ' cards in ', TIMEDIFF(NOW(), @start_time)) AS result;
END$$

CREATE PROCEDURE `generate_test_products`(IN num_rows INT)
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE batch_size INT DEFAULT 1000;
    DECLARE current_batch INT DEFAULT 0;
    DECLARE categories VARCHAR(200) DEFAULT 'food,beverage,dairy,snacks,household,personal_care,electronics,stationery';
    
    SET @start_time = NOW();
    SET FOREIGN_KEY_CHECKS = 0;
    SET autocommit = 0;
    
    WHILE i <= num_rows DO
        INSERT INTO `product` (`name`, `price`, `integral`, `category`, `stock`, `unit`, `time`)
        VALUES (
            CONCAT('TestProduct', i),
            FLOOR(100 + RAND() * 99900),
            FLOOR(RAND() * 500),
            ELT(FLOOR(1 + RAND() * 8), 'food', 'beverage', 'dairy', 'snacks', 'household', 'personal_care', 'electronics', 'stationery'),
            FLOOR(RAND() * 1000),
            ELT(FLOOR(1 + RAND() * 4), 'pcs', 'kg', 'box', 'bottle'),
            DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY)
        );
        
        SET current_batch = current_batch + 1;
        IF current_batch >= batch_size THEN
            COMMIT;
            SET current_batch = 0;
        END IF;
        
        SET i = i + 1;
    END WHILE;
    
    COMMIT;
    SET FOREIGN_KEY_CHECKS = 1;
    SET autocommit = 1;
    
    SELECT CONCAT('Generated ', num_rows, ' products in ', TIMEDIFF(NOW(), @start_time)) AS result;
END$$

CREATE PROCEDURE `generate_test_orders`(IN num_rows INT)
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE batch_size INT DEFAULT 1000;
    DECLARE current_batch INT DEFAULT 0;
    DECLARE total_cards INT;
    
    SELECT COUNT(*) INTO total_cards FROM card WHERE cardid LIKE 'TEST_C%';
    
    SET @start_time = NOW();
    SET FOREIGN_KEY_CHECKS = 0;
    SET autocommit = 0;
    
    WHILE i <= num_rows DO
        INSERT INTO `orders` (`orderid`, `cardid`, `total_amount`, `total_integral`, `payment_method`, `order_time`, `status`)
        VALUES (
            CONCAT('TEST_O', LPAD(i, 12, '0'), FLOOR(RAND() * 1000)),
            CONCAT('TEST_C', LPAD(FLOOR(1 + RAND() * total_cards), 8, '0')),
            FLOOR(500 + RAND() * 499500),
            FLOOR(RAND() * 1000),
            FLOOR(RAND() * 2),
            DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY),
            IF(RAND() > 0.1, 1, 2)
        );
        
        SET current_batch = current_batch + 1;
        IF current_batch >= batch_size THEN
            COMMIT;
            SET current_batch = 0;
        END IF;
        
        SET i = i + 1;
    END WHILE;
    
    COMMIT;
    SET FOREIGN_KEY_CHECKS = 1;
    SET autocommit = 1;
    
    SELECT CONCAT('Generated ', num_rows, ' orders in ', TIMEDIFF(NOW(), @start_time)) AS result;
END$$

CREATE PROCEDURE `generate_test_order_items`(IN num_rows INT)
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE batch_size INT DEFAULT 1000;
    DECLARE current_batch INT DEFAULT 0;
    DECLARE total_orders INT;
    DECLARE total_products INT;
    DECLARE rand_qty INT;
    DECLARE rand_price INT;
    
    SELECT COUNT(*) INTO total_orders FROM orders WHERE orderid LIKE 'TEST_O%';
    SELECT MAX(productid) INTO total_products FROM product;
    
    SET @start_time = NOW();
    SET FOREIGN_KEY_CHECKS = 0;
    SET autocommit = 0;
    
    WHILE i <= num_rows DO
        SET rand_qty = FLOOR(1 + RAND() * 10);
        SET rand_price = FLOOR(100 + RAND() * 9900);
        
        INSERT INTO `order_items` (`orderid`, `productid`, `quantity`, `price`, `subtotal`)
        VALUES (
            CONCAT('TEST_O', LPAD(FLOOR(1 + RAND() * total_orders), 12, '0'), FLOOR(RAND() * 1000)),
            FLOOR(1 + RAND() * total_products),
            rand_qty,
            rand_price,
            rand_qty * rand_price
        )
        ON DUPLICATE KEY UPDATE quantity = quantity;
        
        SET current_batch = current_batch + 1;
        IF current_batch >= batch_size THEN
            COMMIT;
            SET current_batch = 0;
        END IF;
        
        SET i = i + 1;
    END WHILE;
    
    COMMIT;
    SET FOREIGN_KEY_CHECKS = 1;
    SET autocommit = 1;
    
    SELECT CONCAT('Generated ', num_rows, ' order items in ', TIMEDIFF(NOW(), @start_time)) AS result;
END$$

CREATE PROCEDURE `generate_test_records`(IN num_rows INT)
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE batch_size INT DEFAULT 1000;
    DECLARE current_batch INT DEFAULT 0;
    DECLARE total_cards INT;
    
    SELECT COUNT(*) INTO total_cards FROM card WHERE cardid LIKE 'TEST_C%';
    
    SET @start_time = NOW();
    SET FOREIGN_KEY_CHECKS = 0;
    SET autocommit = 0;
    
    WHILE i <= num_rows DO
        INSERT INTO `record` (`cardid`, `value`, `time`, `spendtype`, `description`)
        VALUES (
            CONCAT('TEST_C', LPAD(FLOOR(1 + RAND() * total_cards), 8, '0')),
            IF(RAND() > 0.7, FLOOR(RAND() * 10000), -FLOOR(RAND() * 5000)),
            DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY),
            FLOOR(RAND() * 2),
            CONCAT('Test transaction ', i)
        );
        
        SET current_batch = current_batch + 1;
        IF current_batch >= batch_size THEN
            COMMIT;
            SET current_batch = 0;
        END IF;
        
        SET i = i + 1;
    END WHILE;
    
    COMMIT;
    SET FOREIGN_KEY_CHECKS = 1;
    SET autocommit = 1;
    
    SELECT CONCAT('Generated ', num_rows, ' records in ', TIMEDIFF(NOW(), @start_time)) AS result;
END$$

CREATE PROCEDURE `generate_test_customer_behaviors`(IN num_rows INT)
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE batch_size INT DEFAULT 1000;
    DECLARE current_batch INT DEFAULT 0;
    DECLARE total_members INT;
    DECLARE total_products INT;
    
    SELECT COUNT(*) INTO total_members FROM member WHERE memberid LIKE 'TEST_M%';
    SELECT MAX(productid) INTO total_products FROM product;
    
    SET @start_time = NOW();
    SET FOREIGN_KEY_CHECKS = 0;
    SET autocommit = 0;
    
    WHILE i <= num_rows DO
        INSERT INTO `customer_behavior` (`member_id`, `behavior_type`, `product_id`, `order_amount`, `behavior_time`)
        VALUES (
            CONCAT('TEST_M', LPAD(FLOOR(1 + RAND() * total_members), 8, '0')),
            FLOOR(1 + RAND() * 4),
            FLOOR(1 + RAND() * total_products),
            FLOOR(RAND() * 50000) / 100,
            DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY)
        );
        
        SET current_batch = current_batch + 1;
        IF current_batch >= batch_size THEN
            COMMIT;
            SET current_batch = 0;
        END IF;
        
        SET i = i + 1;
    END WHILE;
    
    COMMIT;
    SET FOREIGN_KEY_CHECKS = 1;
    SET autocommit = 1;
    
    SELECT CONCAT('Generated ', num_rows, ' customer behaviors in ', TIMEDIFF(NOW(), @start_time)) AS result;
END$$

-- scale_factor: 1=10k, 5=50k orders
CREATE PROCEDURE `generate_all_test_data`(IN scale_factor INT)
proc_label: BEGIN
    DECLARE base_members INT DEFAULT 10000;
    DECLARE base_products INT DEFAULT 1000;
    DECLARE base_orders INT DEFAULT 10000;
    DECLARE base_behaviors INT DEFAULT 10000;
    
    IF scale_factor < 1 OR scale_factor > 5 THEN
        SELECT 'Error: scale_factor must be 1 (10k) or 5 (50k)' AS error;
        LEAVE proc_label;
    END IF;
    
    SELECT CONCAT('Starting test data generation with scale factor: ', scale_factor) AS status;
    SELECT CONCAT('Target rows - Members: ', base_members, 
                  ', Products: ', base_products,
                  ', Orders: ', base_orders * scale_factor,
                  ', Behaviors: ', base_behaviors * scale_factor) AS targets;
    
    CALL generate_test_members(base_members);
    CALL generate_test_cards(base_members);
    CALL generate_test_products(base_products);
    CALL generate_test_orders(base_orders * scale_factor);
    CALL generate_test_order_items(base_orders * scale_factor * 3);
    CALL generate_test_records(base_orders * scale_factor * 2);
    CALL generate_test_customer_behaviors(base_behaviors * scale_factor);
    
    SELECT 'Test data generation completed!' AS status;
END$$

CREATE PROCEDURE `clear_test_data`()
BEGIN
    SET @start_time = NOW();
    SET FOREIGN_KEY_CHECKS = 0;
    
    DELETE FROM customer_behavior WHERE member_id LIKE 'TEST_M%';
    DELETE FROM record WHERE cardid LIKE 'TEST_C%';
    DELETE FROM order_items WHERE orderid LIKE 'TEST_O%';
    DELETE FROM orders WHERE orderid LIKE 'TEST_O%';
    DELETE FROM card WHERE cardid LIKE 'TEST_C%';
    DELETE FROM member WHERE memberid LIKE 'TEST_M%';
    DELETE FROM product WHERE name LIKE 'TestProduct%';
    
    SET FOREIGN_KEY_CHECKS = 1;
    
    SELECT CONCAT('Cleared all test data in ', TIMEDIFF(NOW(), @start_time)) AS result;
END$$

DELIMITER ;

