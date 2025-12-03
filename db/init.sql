-- Database Initialization Script
-- Usage: mysql -u root -p < db/init.sql

SOURCE db/core/schema.sql;
SOURCE db/core/data.sql;
SOURCE db/extension/schema.sql;
SOURCE db/extension/data.sql;
SOURCE db/extension/advanced_features.sql;

SELECT 'Database initialization completed!' AS message;
