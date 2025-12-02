package org.cityu.supermarket.documentation;

import org.cityu.supermarket.dto.doc.DatabaseColumnDoc;
import org.cityu.supermarket.dto.doc.DatabaseTableDoc;

import java.util.ArrayList;
import java.util.List;

/**
 * Static DB model defs for Knife4j/Swagger display
 */
public final class DatabaseSchemaRegistry {

    private DatabaseSchemaRegistry() {
    }

    /**
     * Provides field descriptions for core business tables
     */
    public static List<DatabaseTableDoc> coreTables() {
        List<DatabaseTableDoc> tables = new ArrayList<>();

        tables.add(buildMemberTable());
        tables.add(buildCardTable());
        tables.add(buildProductTable());
        tables.add(buildOrdersTable());
        tables.add(buildOrderItemTable());
        tables.add(buildRecordTable());

        return tables;
    }

    private static DatabaseTableDoc buildMemberTable() {
        DatabaseTableDoc table = DatabaseTableDoc.of(
                "member", "Member information table",
                "PRIMARY KEY(memberid)",
                "Includes name/sex CHECK constraints plus auxiliary name index"
        );
        table.addColumn(DatabaseColumnDoc.of("memberid", "VARCHAR(50)", false,
                "Unique member identifier", "PK with indexes used by orders/cards"));
        table.addColumn(DatabaseColumnDoc.of("password", "VARCHAR(100)", false,
                "BCrypt hashed password", "Must be generated via PasswordEncoder"));
        table.addColumn(DatabaseColumnDoc.of("name", "VARCHAR(10)", false,
                "Member name", "idx_member_name regular index"));
        table.addColumn(DatabaseColumnDoc.of("sex", "VARCHAR(4)", true,
                "Gender, only allows male/female", "chk_member_sex CHECK constraint"));
        table.addColumn(DatabaseColumnDoc.of("birthday", "DATE", true,
                "Date of birth", "Nullable"));
        table.addColumn(DatabaseColumnDoc.of("create_time", "DATETIME", true,
                "Creation time", "Default CURRENT_TIMESTAMP"));
        return table;
    }

    private static DatabaseTableDoc buildCardTable() {
        DatabaseTableDoc table = DatabaseTableDoc.of(
                "card", "Membership card table",
                "PRIMARY KEY(cardid)",
                "Foreign key to memberid plus lose status constraint"
        );
        table.addColumn(DatabaseColumnDoc.of("cardid", "VARCHAR(50)", false,
                "Membership card number", "PK"));
        table.addColumn(DatabaseColumnDoc.of("memberid", "VARCHAR(50)", false,
                "Associated member ID", "FK -> member.memberid"));
        table.addColumn(DatabaseColumnDoc.of("balance", "INT UNSIGNED", true,
                "Balance (cents)", "Default 0, cannot be negative"));
        table.addColumn(DatabaseColumnDoc.of("integral", "INT UNSIGNED", true,
                "Reward points", "Default 0, cannot be negative"));
        table.addColumn(DatabaseColumnDoc.of("lose", "TINYINT(1)", true,
                "Lost status", "CHECK 0/1"));
        table.addColumn(DatabaseColumnDoc.of("create_time", "DATETIME", true,
                "Creation time", "Default CURRENT_TIMESTAMP"));
        return table;
    }

    private static DatabaseTableDoc buildProductTable() {
        DatabaseTableDoc table = DatabaseTableDoc.of(
                "product", "Product catalog table",
                "PRIMARY KEY(productid)",
                "price and quantity include CHECK constraints with name/category indexes"
        );
        table.addColumn(DatabaseColumnDoc.of("productid", "INT AUTO_INCREMENT", false,
                "Product ID", "PK"));
        table.addColumn(DatabaseColumnDoc.of("name", "VARCHAR(100)", false,
                "Product name", "idx_product_name index"));
        table.addColumn(DatabaseColumnDoc.of("price", "INT UNSIGNED", false,
                "Unit price (cents)", "CHECK price > 0"));
        table.addColumn(DatabaseColumnDoc.of("integral", "INT UNSIGNED", true,
                "Reward points granted", "Default 0"));
        table.addColumn(DatabaseColumnDoc.of("category", "VARCHAR(20)", false,
                "Product category", "idx_product_category index"));
        table.addColumn(DatabaseColumnDoc.of("stock", "INT UNSIGNED", true,
                "Stock quantity", "Default 0"));
        table.addColumn(DatabaseColumnDoc.of("unit", "VARCHAR(10)", false,
                "Unit of measure", "Default 'piece'"));
        table.addColumn(DatabaseColumnDoc.of("time", "DATETIME", true,
                "Listed time", "Default CURRENT_TIMESTAMP"));
        return table;
    }

    private static DatabaseTableDoc buildOrdersTable() {
        DatabaseTableDoc table = DatabaseTableDoc.of(
                "orders", "Order table",
                "PRIMARY KEY(orderid)",
                "cardid foreign key plus indexes on order_time/status"
        );
        table.addColumn(DatabaseColumnDoc.of("orderid", "VARCHAR(32)", false,
                "Order number", "PK"));
        table.addColumn(DatabaseColumnDoc.of("cardid", "VARCHAR(50)", false,
                "Paying membership card", "FK -> card.cardid"));
        table.addColumn(DatabaseColumnDoc.of("total_amount", "INT UNSIGNED", false,
                "Order total amount (cents)", ">=0"));
        table.addColumn(DatabaseColumnDoc.of("total_integral", "INT UNSIGNED", true,
                "Points awarded", "Default 0"));
        table.addColumn(DatabaseColumnDoc.of("payment_method", "TINYINT(1)", false,
                "Payment method", "CHECK 0=balance,1=points"));
        table.addColumn(DatabaseColumnDoc.of("order_time", "DATETIME", false,
                "Order time", "Default CURRENT_TIMESTAMP"));
        table.addColumn(DatabaseColumnDoc.of("status", "TINYINT(1)", false,
                "Order status", "CHECK 1=completed,2=canceled"));
        return table;
    }

    private static DatabaseTableDoc buildOrderItemTable() {
        DatabaseTableDoc table = DatabaseTableDoc.of(
                "order_items", "Order line item table",
                "PRIMARY KEY(item_id)",
                "Foreign keys to orders/product, cascade when deleting orders"
        );
        table.addColumn(DatabaseColumnDoc.of("item_id", "INT AUTO_INCREMENT", false,
                "Line item primary key", "PK"));
        table.addColumn(DatabaseColumnDoc.of("orderid", "VARCHAR(32)", false,
                "Order ID", "FK -> orders.orderid"));
        table.addColumn(DatabaseColumnDoc.of("productid", "INT", false,
                "Product ID", "FK -> product.productid"));
        table.addColumn(DatabaseColumnDoc.of("quantity", "INT UNSIGNED", false,
                "Purchase quantity", "CHECK quantity > 0"));
        table.addColumn(DatabaseColumnDoc.of("price", "INT UNSIGNED", false,
                "Settled unit price (cents)", "Captured from order placement"));
        table.addColumn(DatabaseColumnDoc.of("subtotal", "INT UNSIGNED", false,
                "Subtotal (cents)", "quantity * price"));
        return table;
    }

    private static DatabaseTableDoc buildRecordTable() {
        DatabaseTableDoc table = DatabaseTableDoc.of(
                "record", "Transaction record table",
                "PRIMARY KEY(id)",
                "Tracks card inflow/outflow, optional orderid with spendtype CHECK"
        );
        table.addColumn(DatabaseColumnDoc.of("id", "INT AUTO_INCREMENT", false,
                "Record primary key", "PK"));
        table.addColumn(DatabaseColumnDoc.of("cardid", "VARCHAR(50)", false,
                "Linked membership card", "FK -> card.cardid"));
        table.addColumn(DatabaseColumnDoc.of("value", "INT", false,
                "Transaction amount (cents)", "Sign indicates expense/income"));
        table.addColumn(DatabaseColumnDoc.of("time", "DATETIME", false,
                "Transaction time", "Default CURRENT_TIMESTAMP"));
        table.addColumn(DatabaseColumnDoc.of("spendtype", "TINYINT(1)", false,
                "Transaction type", "CHECK 0=cash,1=points"));
        table.addColumn(DatabaseColumnDoc.of("orderid", "VARCHAR(32)", true,
                "Related order", "FK -> orders.orderid, nullable"));
        table.addColumn(DatabaseColumnDoc.of("description", "VARCHAR(100)", true,
                "Remarks", "Nullable"));
        return table;
    }
}
