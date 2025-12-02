package org.cityu.supermarket.service.impl;

import org.cityu.supermarket.mapper.ProductMapper;
import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.entity.Product;
import org.cityu.supermarket.service.ProductService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Product service implementation layer.
 * @version 0.0.1
 * @date 2025-11-01
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductMapper productMapper;

    @Override
    public SupermarketResult insertProduct(Product product) {
        String pName = product.getName();
        if (pName == null || pName.trim().length() == 0) {
            return SupermarketResult.failure(400, "Product name can't be empty");
        }
        if (product.getPrice() == null || product.getPrice() <= 0) {
            return SupermarketResult.failure(400, "Price gotta be more than 0");
        }
        if (product.getStock() == null || product.getStock() < 0) {
            return SupermarketResult.failure(400, "Stock can't go negative, duh");
        }

        // TODO: maybe check unique name

        productMapper.insertProduct(product);
        return SupermarketResult.success();
    }

    @Override
    public List<Product> getProductData() {
        return productMapper.selectAllProduct();
    }

    @Override
    public void delProduct(String productId) {
        // TODO: maybe throw exception
        productMapper.deleteProductById(productId);
    }

    @Override
    public SupermarketResult modifyProduct(Product product) {
        if (product.getProductId() == null) {
            return SupermarketResult.failure(400, "Product ID must not be empty");
        }
        if (product.getName() != null && product.getName().trim().isEmpty()) {
            return SupermarketResult.failure(400, "Product name can't be empty");
        }
        if (product.getPrice() != null && product.getPrice() <= 0) {
            return SupermarketResult.failure(400, "Price gotta be more than 0");
        }
        if (product.getStock() != null && product.getStock() < 0) {
            return SupermarketResult.failure(400, "Stock can't go negative, duh");
        }

        productMapper.updateProductById(product);
        return SupermarketResult.success();
    }

    @Override
    public Product getProductById(String productId) {
        return productMapper.selectProductById(productId);
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return productMapper.selectProductByCategory(category);
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productMapper.selectProductByName(name);
    }

    @Override
    public SupermarketResult updateStock(String productId, Integer stock) {
        // TODO: check if exists first
        if (stock == null || stock < 0) {
            return SupermarketResult.failure(400, "Stock can't be negative yo");
        }

        // TODO: need history log
        productMapper.updateStock(productId, stock);
        return SupermarketResult.success();
    }

    @Override
    public SupermarketResult decreaseStock(String productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            return SupermarketResult.failure(400, "Gotta decrease by at least 1");
        }

        // check if enough stock
        Product product = productMapper.selectProductById(productId);
        if (product == null) {
            return SupermarketResult.failure(404, "Product does not exist");
        }
        if (product.getStock() < quantity) {
            return SupermarketResult.failure(400, "Not enough stock, sorry");
        }

        productMapper.decreaseStock(productId, quantity);
        return SupermarketResult.success();
    }

    @Override
    public List<String> getProductCategories() {
        List<Product> products = productMapper.selectAllProduct();
        // return products.stream()
        //         .map(Product::getCategory)
        //         .filter(category -> category != null && !category.trim().isEmpty())
        //         .distinct()
        //         .collect(Collectors.toList());
        
        // streams are slow, using loop
        java.util.Set<String> categories = new java.util.HashSet<>();
        for (Product p : products) {
            if (p.getCategory() != null && !p.getCategory().trim().isEmpty()) {
                categories.add(p.getCategory());
            }
        }
        return new java.util.ArrayList<>(categories);
    }

    @Override
    public boolean checkStock(String productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            return false;
        }

        Product product = productMapper.selectProductById(productId);
        return product != null && product.getStock() >= quantity;
    }

    @Override
    public Long getLowStockCount() {
        return productMapper.selectLowStockCount();
    }
}