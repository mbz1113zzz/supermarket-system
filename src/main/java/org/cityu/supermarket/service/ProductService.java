package org.cityu.supermarket.service;

import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.entity.Product;

import java.util.List;

/**
 * Product service interface.
 * @version 0.0.1
 * @date 2025-11-01
 */
public interface ProductService {
    /**
     * Create a new product.
     * @param product product payload
     * @return operation result
     */
    SupermarketResult insertProduct(Product product);

    /**
     * Fetch every product record.
     * @return product list
     */
    List<Product> getProductData();

    /**
     * Delete product information.
     * @param productId product identifier
     */
    void delProduct(String productId);

    /**
     * Update product information.
     * @param product product payload
     * @return operation result
     */
    SupermarketResult modifyProduct(Product product);

    /**
     * Fetch product by id.
     * @param productId product identifier
     * @return product entity
     */
    Product getProductById(String productId);

    /**
     * Query products by category.
     * @param category category code
     * @return product list
     */
    List<Product> getProductByCategory(String category);

    /**
     * Fuzzy search products by name.
     * @param name keyword
     * @return matching products
     */
    List<Product> getProductByName(String name);

    /**
     * Overwrite product inventory.
     * @param productId product id
     * @param stock stock quantity
     * @return operation result
     */
    SupermarketResult updateStock(String productId, Integer stock);

    /**
     * Decrease inventory when orders are placed.
     * @param productId product id
     * @param quantity quantity to deduct
     * @return operation result
     */
    SupermarketResult decreaseStock(String productId, Integer quantity);

    /**
     * Fetch all product categories.
     * @return category list
     */
    List<String> getProductCategories();

    /**
     * Check whether inventory is sufficient.
     * @param productId product id
     * @param quantity required quantity
     * @return true if enough stock
     */
    boolean checkStock(String productId, Integer quantity);

    /**
     * Count products flagged as low stock.
     * @return number of low-stock items
     */
    Long getLowStockCount();
}