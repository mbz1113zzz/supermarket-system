package org.cityu.supermarket.mapper;

import org.cityu.supermarket.entity.Product;

import java.util.List;

/**
 * Product data access layer interface
 * @version 0.0.1
 * @date 2025-11-01
 */
public interface ProductMapper {
    /**
     * Insert product
     * Note: When using MyBatis to insert a record into MySQL, we need to return the auto-generated primary key.
     * Solution: Set useGeneratedKeys to true in the XML mapper, and specify keyProperty and keyColumn for the primary key field and Java property.
     * @param product
     */
    void insertProduct(Product product);

    /**
     * Get all products
     * Note: Use date_format for time fields, otherwise you'll get .0 in the time data
     * @return
     */
    List<Product> selectAllProduct();

    /**
     * Delete product by productId
     * @param productId
     */
    void deleteProductById(String productId);

    /**
     * Update product by productId
     * @param product
     */
    void updateProductById(Product product);

    /**
     * Get product by productId
     * @param productId
     * @return
     */
    Product selectProductById(String productId);

    /**
     * Get products by category
     * @param category
     * @return
     */
    List<Product> selectProductByCategory(String category);

    /**
     * Search products by name (fuzzy search)
     * @param name
     * @return
     */
    List<Product> selectProductByName(String name);

    /**
     * Update product stock
     * @param productId product ID
     * @param stock stock quantity
     */
    void updateStock(String productId, Integer stock);

    /**
     * Decrease product stock
     * @param productId product ID
     * @param quantity quantity to decrease
     */
    void decreaseStock(String productId, Integer quantity);

    /**
     * Get low stock product count
     * @return
     */
    Long selectLowStockCount();
}