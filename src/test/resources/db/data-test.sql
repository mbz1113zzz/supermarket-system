-- Test data initialization script
-- Used for the isolated supermarket_test schema

-- Wipe existing test rows (respecting FK order)
DELETE FROM record WHERE 1=1;
DELETE FROM order_item WHERE 1=1;
DELETE FROM orders WHERE 1=1;
DELETE FROM card WHERE 1=1;
DELETE FROM member WHERE 1=1;
DELETE FROM product WHERE 1=1;
DELETE FROM manager WHERE 1=1;
DELETE FROM purchase_order WHERE 1=1;
DELETE FROM supplier WHERE 1=1;
DELETE FROM promotion WHERE 1=1;
DELETE FROM coupon WHERE 1=1;

-- Insert test administrator (password: 123456, stored as BCrypt)
INSERT INTO manager (managerid, password, name, role) VALUES
('admin', '$2a$10$WLYHCo.8L3lIfJQrYJwBdO1UpnZCdUt9cmeom.sKcTiWbKyLzHGGK', 'QA Administrator', 'Administrator');

-- Insert demo members (password: test123)
INSERT INTO member (memberid, password, name, sex, birthday) VALUES
('M001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Demo Member A', 'male', '1990-01-15'),
('M002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'Demo Member B', 'female', '1995-06-20');

-- Insert demo membership cards
INSERT INTO card (cardid, memberid, balance, integral, lose) VALUES
('CARD001', 'M001', 50000, 1000, 0),
('CARD002', 'M002', 30000, 500, 0);

-- Insert sample merchandise
INSERT INTO product (productid, name, price, stock, integral, category, description) VALUES
(1, 'Sample Energy Bar', 1000, 100, 10, 'Grocery', 'Baseline SKU for regression tests'),
(2, 'Mock Fruit Tonic', 2000, 50, 20, 'Beverage', 'High-margin drink for promo cases'),
(3, 'Everyday Care Kit', 500, 200, 5, 'Household', 'Low-cost staple for smoke tests');

-- Insert sample suppliers
INSERT INTO supplier (supplier_id, name, contact, phone, address, status) VALUES
('SUP001', 'Northwind Ingredients', 'Alex Zhang', '13800138001', '85 Harbor Road, Testville', 1),
('SUP002', 'Maple Leaf Drinks', 'Linda Li', '13800138002', '22 Market Street, Sample City', 1);
