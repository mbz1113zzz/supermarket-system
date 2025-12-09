-- Query Efficiency Test - EXPLAIN ANALYZE

USE `supermarket`;

-- Q1: Order Time Range (idx_order_time)

EXPLAIN ANALYZE
SELECT orderid, cardid, total_amount, payment_method, order_time, status
FROM orders
WHERE order_time BETWEEN DATE_SUB(NOW(), INTERVAL 30 DAY) AND NOW()
ORDER BY order_time DESC
LIMIT 100;


-- Q2: Hot Products Statistics (JOIN + GROUP BY)

EXPLAIN ANALYZE
SELECT 
    p.productid,
    p.name AS product_name,
    p.category,
    COUNT(*) AS purchase_count,
    SUM(cb.order_amount) AS total_revenue
FROM customer_behavior cb
INNER JOIN product p ON cb.product_id = p.productid
WHERE cb.behavior_type = 3  -- Purchase behavior
  AND cb.behavior_time >= DATE_SUB(NOW(), INTERVAL 90 DAY)
GROUP BY p.productid, p.name, p.category
ORDER BY purchase_count DESC
LIMIT 10;


-- Q3: Member Consumption Analysis (Multi-table JOIN)

EXPLAIN ANALYZE
SELECT 
    m.memberid,
    m.name AS member_name,
    COUNT(DISTINCT o.orderid) AS order_count,
    COALESCE(SUM(o.total_amount), 0) / 100 AS total_spent_yuan,
    MAX(o.order_time) AS last_purchase,
    DATEDIFF(NOW(), MAX(o.order_time)) AS days_since_last_purchase
FROM member m
INNER JOIN card c ON c.memberid = m.memberid
INNER JOIN orders o ON o.cardid = c.cardid
WHERE o.order_time >= DATE_SUB(NOW(), INTERVAL 365 DAY)
GROUP BY m.memberid, m.name
HAVING order_count >= 1
ORDER BY total_spent_yuan DESC
LIMIT 20;


-- Q4: Transaction Records by Card (idx_cardid)

EXPLAIN ANALYZE
SELECT 
    r.id,
    r.cardid,
    r.value,
    r.time,
    r.spendtype,
    r.description,
    CASE r.spendtype 
        WHEN 0 THEN IF(r.value > 0, 'Recharge', 'Consumption')
        WHEN 1 THEN 'Points Exchange'
    END AS transaction_type
FROM record r
WHERE r.cardid = (SELECT cardid FROM card LIMIT 1)
ORDER BY r.time DESC
LIMIT 50;


-- Q5: Category Sales Statistics (idx_product_category)

EXPLAIN ANALYZE
SELECT 
    p.category,
    COUNT(DISTINCT o.orderid) AS order_count,
    COUNT(DISTINCT o.cardid) AS unique_customers,
    SUM(oi.quantity) AS items_sold,
    SUM(oi.subtotal) / 100 AS revenue_yuan,
    AVG(oi.subtotal) / 100 AS avg_order_value
FROM order_items oi
INNER JOIN product p ON oi.productid = p.productid
INNER JOIN orders o ON oi.orderid = o.orderid
WHERE o.order_time >= DATE_SUB(NOW(), INTERVAL 30 DAY)
  AND o.status = 1
GROUP BY p.category
ORDER BY revenue_yuan DESC;
