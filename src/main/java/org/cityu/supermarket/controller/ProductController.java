package org.cityu.supermarket.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.cityu.supermarket.common.vo.SupermarketResult;
import org.cityu.supermarket.entity.*;
import org.cityu.supermarket.service.ProductService;
import org.cityu.supermarket.entity.Product;
import org.cityu.supermarket.entity.PageResult;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * Product controller
 * @version 0.0.1
 * @date 2025-11-01
 */
@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {
    @Resource
    private ProductService productService;

    /**
     * add product
     * @param product product info
     * @return result
     */
    @PostMapping
    public SupermarketResult insertProduct(@RequestBody Product product){
        // Used to have business logic here, now moved back to Service layer
        // Controller just forwards requests
        return productService.insertProduct(product);
    }

    /**
     * get product data (paginated)
     * @param pageIndex page number
     * @param pageSize items per page
     * @return product list and total count
     */
    @GetMapping
    public SupermarketResult getProduct(@RequestParam(defaultValue = "1") String pageIndex, 
                                      @RequestParam(defaultValue = "10") String pageSize){
        // TODO: add param validation and exception handling
        PageResult pageResult = new PageResult();
        PageHelper.startPage(Integer.parseInt(pageIndex), Integer.parseInt(pageSize));
        List<Product> lists = productService.getProductData();
        PageInfo<Product> pageInfo = new PageInfo<>(lists);
        pageResult.setList(pageInfo.getList());
        pageResult.setPageTotal(pageInfo.getTotal());
        return SupermarketResult.success(pageResult);
    }

    /**
     * get all products (no pagination)
     * @return all products list
     */
    @GetMapping("/all")
    public SupermarketResult getAllProducts(){
        List<Product> products = productService.getProductData();
        return SupermarketResult.success(products);
    }

    /**
     * get products by category
     * @param category product category
     * @return product list
     */
    @GetMapping("/category/{category}")
    public SupermarketResult getProductsByCategory(@PathVariable String category){
        List<Product> products = productService.getProductByCategory(category);
        return SupermarketResult.success(products);
    }

    /**
     * search products by name
     * @param name product name
     * @return product list
     */
    @GetMapping("/search")
    public SupermarketResult searchProducts(@RequestParam String name){
        List<Product> products = productService.getProductByName(name);
        return SupermarketResult.success(products);
    }

    /**
     * get product by ID
     * @param productId product ID
     * @return product details
     */
    @GetMapping("/{productId}")
    public SupermarketResult getProductById(@PathVariable String productId){
        // TODO: use ResultCode constants instead of magic numbers
        Product product = productService.getProductById(productId);
        if (product != null) {
            return SupermarketResult.success(product);
        } else {
            return SupermarketResult.failure(404, "Product not found");
        }
    }

    /**
     * delete product
     * @param productId product ID
     * @return result
     */
    // TODO: add pre-delete business checks, like checking order dependencies
    @DeleteMapping("/{productId}")
    public SupermarketResult delProduct(@PathVariable String productId){
        // TODO: optimize Service method to return deletion result
        productService.delProduct(productId);
        return SupermarketResult.success();
    }

    /**
     * modify product
     * @param product product info
     * @return result
     */
    @PutMapping
    public SupermarketResult modifyProduct(@RequestBody Product product){
        return productService.modifyProduct(product);
    }

    /**
     * update product stock
     * @param productId product ID
     * @param stock new stock quantity
     * @return result
     */
    @PatchMapping("/{productId}/stock")
    public SupermarketResult updateStock(@PathVariable String productId, @RequestParam Integer stock){
        return productService.updateStock(productId, stock);
    }

    /**
     * get product categories
     * @return category list
     */
    @GetMapping("/categories")
    public SupermarketResult getCategories(){
        List<String> categories = productService.getProductCategories();
        return SupermarketResult.success(categories);
    }

    /**
     * check stock
     * @param productId product ID
     * @param quantity required quantity
     * @return check result
     */
    @GetMapping("/{productId}/stock/check")
    public SupermarketResult checkStock(@PathVariable String productId, @RequestParam Integer quantity){
        boolean available = productService.checkStock(productId, quantity);
        if (available) {
            return SupermarketResult.success("Stock is sufficient");
        } else {
            return SupermarketResult.failure(400, "Insufficient stock");
        }
    }
}