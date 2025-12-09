-- Scalability Evaluation: 10k → 50k orders

USE `supermarket`;

DELIMITER $$

DROP PROCEDURE IF EXISTS `run_scalability_test`$$

CREATE PROCEDURE `run_scalability_test`(IN test_scale INT)
BEGIN
    DECLARE start_ts TIMESTAMP(6);
    DECLARE end_ts TIMESTAMP(6);
    DECLARE q1_time DECIMAL(10,3);
    DECLARE q2_time DECIMAL(10,3);
    DECLARE q3_time DECIMAL(10,3);
    DECLARE q4_time DECIMAL(10,3);
    DECLARE q5_time DECIMAL(10,3);
    DECLARE order_count INT;
    
    SELECT COUNT(*) INTO order_count FROM orders;
    
    -- Q1: Order Time Range
    SET start_ts = NOW(6);
    SELECT COUNT(*) INTO @dummy FROM (
        SELECT orderid, cardid, total_amount, order_time, status
        FROM orders
        WHERE order_time BETWEEN DATE_SUB(NOW(), INTERVAL 30 DAY) AND NOW()
        ORDER BY order_time DESC
        LIMIT 100
    ) t;
    SET end_ts = NOW(6);
    SET q1_time = TIMESTAMPDIFF(MICROSECOND, start_ts, end_ts) / 1000;
    
    -- Q2: Hot Products
    SET start_ts = NOW(6);
    SELECT COUNT(*) INTO @dummy FROM (
        SELECT p.productid, p.name, COUNT(*) AS sales, SUM(cb.order_amount) AS revenue
        FROM customer_behavior cb
        INNER JOIN product p ON cb.product_id = p.productid
        WHERE cb.behavior_type = 3 AND cb.behavior_time >= DATE_SUB(NOW(), INTERVAL 90 DAY)
        GROUP BY p.productid, p.name
        ORDER BY sales DESC
        LIMIT 10
    ) t;
    SET end_ts = NOW(6);
    SET q2_time = TIMESTAMPDIFF(MICROSECOND, start_ts, end_ts) / 1000;
    
    -- Q3: Member Consumption
    SET start_ts = NOW(6);
    SELECT COUNT(*) INTO @dummy FROM (
        SELECT m.memberid, m.name, COUNT(DISTINCT o.orderid) AS orders, SUM(o.total_amount) AS total
        FROM member m
        INNER JOIN card c ON c.memberid = m.memberid
        INNER JOIN orders o ON o.cardid = c.cardid
        WHERE o.order_time >= DATE_SUB(NOW(), INTERVAL 365 DAY)
        GROUP BY m.memberid, m.name
        ORDER BY total DESC
        LIMIT 20
    ) t;
    SET end_ts = NOW(6);
    SET q3_time = TIMESTAMPDIFF(MICROSECOND, start_ts, end_ts) / 1000;
    
    -- Q4: Transaction Records by Card
    SET start_ts = NOW(6);
    SELECT COUNT(*) INTO @dummy FROM (
        SELECT id, cardid, value, time, spendtype
        FROM record
        WHERE cardid = (SELECT cardid FROM card WHERE cardid LIKE 'TEST_C%' LIMIT 1)
        ORDER BY time DESC
        LIMIT 50
    ) t;
    SET end_ts = NOW(6);
    SET q4_time = TIMESTAMPDIFF(MICROSECOND, start_ts, end_ts) / 1000;
    
    -- Q5: Category Sales
    SET start_ts = NOW(6);
    SELECT COUNT(*) INTO @dummy FROM (
        SELECT p.category, COUNT(DISTINCT o.orderid) AS orders, SUM(oi.subtotal) AS revenue
        FROM order_items oi
        INNER JOIN product p ON oi.productid = p.productid
        INNER JOIN orders o ON oi.orderid = o.orderid
        WHERE o.order_time >= DATE_SUB(NOW(), INTERVAL 30 DAY)
        GROUP BY p.category
        ORDER BY revenue DESC
    ) t;
    SET end_ts = NOW(6);
    SET q5_time = TIMESTAMPDIFF(MICROSECOND, start_ts, end_ts) / 1000;
    
    SELECT test_scale AS scale_factor,
           order_count AS total_orders,
           q1_time AS Q1_ms,
           q2_time AS Q2_ms,
           q3_time AS Q3_ms,
           q4_time AS Q4_ms,
           q5_time AS Q5_ms,
           (q1_time + q2_time + q3_time + q4_time + q5_time) / 5 AS avg_ms;
END$$

DROP PROCEDURE IF EXISTS `run_full_scalability_evaluation`$$

-- Run 10k test only
DROP PROCEDURE IF EXISTS `run_10k_test`$$

CREATE PROCEDURE `run_10k_test`()
BEGIN
    CALL clear_test_data();
    CALL generate_all_test_data(1);
    CALL run_scalability_test(1);
END$$

-- Run 50k test only
DROP PROCEDURE IF EXISTS `run_50k_test`$$

CREATE PROCEDURE `run_50k_test`()
BEGIN
    CALL clear_test_data();
    CALL generate_all_test_data(5);
    CALL run_scalability_test(5);
END$$

DELIMITER ;
