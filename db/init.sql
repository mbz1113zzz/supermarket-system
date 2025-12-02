-- Database Initialization Script
-- Usage: mysql -u root -p < db/init.sql

SOURCE db/core/schema.sql;
SOURCE db/core/data.sql;

SELECT 'Database initialization completed!' AS message;
