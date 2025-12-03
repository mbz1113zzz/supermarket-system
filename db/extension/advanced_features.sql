-- Supermarket System - Triggers and Stored Procedures

SET NAMES utf8mb4;
SET SESSION sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO';

USE `supermarket`;

DELIMITER $$

-- Stock increment on receipt
DROP TRIGGER IF EXISTS `trg_receipt_item_stock_increment`$$
CREATE TRIGGER `trg_receipt_item_stock_increment`
AFTER INSERT ON `purchase_receipt_item`
FOR EACH ROW
BEGIN
    DECLARE effective_quantity INT DEFAULT COALESCE(NEW.actual_quantity, NEW.quantity, 0);

    IF effective_quantity < 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Receipt quantity cannot be negative';
    END IF;

    UPDATE `product`
    SET `stock` = `stock` + effective_quantity
    WHERE `productid` = NEW.product_id;

    UPDATE `purchase_receipt`
    SET `total_quantity` = COALESCE(`total_quantity`, 0) + effective_quantity
    WHERE `receipt_id` = NEW.receipt_id;
END$$

-- Auto-calculate return item total price
DROP TRIGGER IF EXISTS `trg_purchase_return_item_before`$$
CREATE TRIGGER `trg_purchase_return_item_before`
BEFORE INSERT ON `purchase_return_item`
FOR EACH ROW
BEGIN
    IF NEW.total_price IS NULL OR NEW.total_price = 0 THEN
        SET NEW.total_price = ROUND(COALESCE(NEW.quantity, 0) * COALESCE(NEW.unit_price, 0), 2);
    END IF;
END$$

DROP TRIGGER IF EXISTS `trg_purchase_return_item_stock_guard`$$
CREATE TRIGGER `trg_purchase_return_item_stock_guard`
AFTER INSERT ON `purchase_return_item`
FOR EACH ROW
BEGIN
    DECLARE current_stock INT;

    SELECT `stock`
    INTO current_stock
    FROM `product`
    WHERE `productid` = NEW.product_id
    FOR UPDATE;

    IF current_stock IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Product not found; cannot perform return deduction';
    END IF;

    IF current_stock < NEW.quantity THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Insufficient stock; cannot perform return deduction';
    END IF;

    UPDATE `product`
    SET `stock` = current_stock - NEW.quantity
    WHERE `productid` = NEW.product_id;

    UPDATE `purchase_return`
    SET `total_amount` = COALESCE(`total_amount`, 0.00) + COALESCE(NEW.total_price, 0.00)
    WHERE `return_id` = NEW.return_id;
END$$

-- Member consumption report
DROP PROCEDURE IF EXISTS `sp_member_consumption_report`$$
CREATE PROCEDURE `sp_member_consumption_report`(
    IN p_member_id VARCHAR(50),
    IN p_start_time DATETIME,
    IN p_end_time DATETIME
)
BEGIN
    DECLARE v_start DATETIME DEFAULT DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 30 DAY);
    DECLARE v_end DATETIME DEFAULT CURRENT_TIMESTAMP;

    IF p_start_time IS NOT NULL THEN
        SET v_start = p_start_time;
    END IF;

    IF p_end_time IS NOT NULL THEN
        SET v_end = p_end_time;
    END IF;

    SELECT
        m.memberid AS member_id,
        m.name AS member_name,
        COUNT(DISTINCT o.orderid) AS order_count,
        COALESCE(SUM(o.total_amount), 0) AS total_amount_cent,
        ROUND(COALESCE(SUM(o.total_amount), 0) / 100, 2) AS total_amount_yuan,
        COALESCE(SUM(o.total_integral), 0) AS earned_integral,
        MAX(o.order_time) AS last_order_time,
        COALESCE(COUNT(DISTINCT pe.promotion_id), 0) AS promotion_hits,
        COALESCE(COUNT(DISTINCT CASE WHEN uc.status = 2 THEN uc.id END), 0) AS coupons_used
    FROM `member` m
    LEFT JOIN `card` c ON c.memberid = m.memberid
    LEFT JOIN `orders` o ON o.cardid = c.cardid AND o.order_time BETWEEN v_start AND v_end
    LEFT JOIN `promotion_effect` pe ON pe.member_id = m.memberid AND pe.use_time BETWEEN v_start AND v_end
    LEFT JOIN `user_coupon` uc ON uc.member_id = m.memberid AND uc.use_time BETWEEN v_start AND v_end
    WHERE m.memberid = p_member_id
    GROUP BY m.memberid, m.name;
END$$

-- Daily inventory snapshot
DROP PROCEDURE IF EXISTS `sp_daily_inventory_snapshot`$$
CREATE PROCEDURE `sp_daily_inventory_snapshot`(
    IN p_target_date DATE
)
BEGIN
    DECLARE v_target DATE DEFAULT CURRENT_DATE;

    IF p_target_date IS NOT NULL THEN
        SET v_target = p_target_date;
    END IF;

    SELECT
        p.productid,
        p.name AS product_name,
        COALESCE(inbound.in_qty, 0) AS inbound_quantity,
        COALESCE(outbound.out_qty, 0) AS return_quantity,
        COALESCE(inbound.in_qty, 0) - COALESCE(outbound.out_qty, 0) AS net_quantity,
        p.stock AS current_stock,
        v_target AS snapshot_date
    FROM `product` p
    LEFT JOIN (
        SELECT
            pri.product_id,
            SUM(COALESCE(pri.actual_quantity, pri.quantity)) AS in_qty
        FROM `purchase_receipt_item` pri
        JOIN `purchase_receipt` pr ON pr.receipt_id = pri.receipt_id
        WHERE DATE(pr.receipt_date) = v_target
        GROUP BY pri.product_id
    ) AS inbound ON inbound.product_id = p.productid
    LEFT JOIN (
        SELECT
            pri.product_id,
            SUM(pri.quantity) AS out_qty
        FROM `purchase_return_item` pri
        JOIN `purchase_return` pr ON pr.return_id = pri.return_id
        WHERE DATE(pr.return_date) = v_target
        GROUP BY pri.product_id
    ) AS outbound ON outbound.product_id = p.productid
    WHERE COALESCE(inbound.in_qty, 0) > 0 OR COALESCE(outbound.out_qty, 0) > 0
    ORDER BY p.productid;
END$$

DELIMITER ;

SET SESSION sql_mode = DEFAULT;
