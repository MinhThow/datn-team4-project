package com.java6.datn.controller;

import com.java6.datn.dto.ProductDTO;
import com.java6.datn.dto.ProductDTO_V2;
import com.java6.datn.service.ProductService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
//@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDTO> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO getById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ProductDTO create(@RequestBody ProductDTO productDTO) {
        return productService.createProduct(productDTO);
    }

    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable Integer id, @RequestBody ProductDTO productDTO) {
        return productService.updateProduct(id, productDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        productService.deleteProduct(id);
    }

//-----------------------------------------------------

    /**
     * Get products for homepage with sale information
     * GET /api/v1/products/home
     */
    @GetMapping("/home")
    public ResponseEntity<Map<String, Object>> getHomepageProducts() {
        Map<String, Object> response = new HashMap<>();

        // Get different product categories for homepage
        List<ProductDTO_V2> bestSellers = productService.getBestSellers(8);
        List<ProductDTO_V2> newArrivals = productService.getNewArrivals(8);
        List<ProductDTO_V2> hotSales = productService.getHotSales(8);

        Map<String, List<ProductDTO_V2>> products = new HashMap<>();
        products.put("bestSellers", bestSellers);
        products.put("newArrivals", newArrivals);
        products.put("hotSales", hotSales);

        response.put("products", products);
        response.put("success", true);

        return ResponseEntity.ok(response);
    }

    /**
     * Search products with pagination and filtering
     * GET /api/v1/products/search?keyword=...&category=...&page=0&size=12
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO_V2> productPage = productService.findProductsWithSaleInfo(keyword, category, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", productPage.getContent());
        response.put("totalElements", productPage.getTotalElements());
        response.put("totalPages", productPage.getTotalPages());
        response.put("currentPage", productPage.getNumber());
        response.put("pageSize", productPage.getSize());
        response.put("hasNext", productPage.hasNext());
        response.put("hasPrevious", productPage.hasPrevious());
        response.put("success", true);

        return ResponseEntity.ok(response);
    }

    /**
     * Get best selling products
     * GET /api/v1/products/best-sellers?limit=8
     */
    @GetMapping("/best-sellers")
    public ResponseEntity<Map<String, Object>> getBestSellers(
            @RequestParam(defaultValue = "8") int limit) {

        List<ProductDTO_V2> bestSellers = productService.getBestSellers(limit);

        Map<String, Object> response = new HashMap<>();
        response.put("products", bestSellers);
        response.put("success", true);

        return ResponseEntity.ok(response);
    }

    /**
     * Get new arrival products
     * GET /api/v1/products/new-arrivals?limit=8
     */
    @GetMapping("/new-arrivals")
    public ResponseEntity<Map<String, Object>> getNewArrivals(
            @RequestParam(defaultValue = "8") int limit) {

        List<ProductDTO_V2> newArrivals = productService.getNewArrivals(limit);

        Map<String, Object> response = new HashMap<>();
        response.put("products", newArrivals);
        response.put("success", true);

        return ResponseEntity.ok(response);
    }

    /**
     * Get products on sale
     * GET /api/v1/products/hot-sales?limit=8
     */
    @GetMapping("/hot-sales")
    public ResponseEntity<Map<String, Object>> getHotSales(
            @RequestParam(defaultValue = "8") int limit) {

        List<ProductDTO_V2> hotSales = productService.getHotSales(limit);

        Map<String, Object> response = new HashMap<>();
        response.put("products", hotSales);
        response.put("success", true);

        return ResponseEntity.ok(response);
    }

    /**
     * Get single product by ID
     * GET /api/v1/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Integer id) {
        try {
            // Get product with sale info
            Pageable pageable = PageRequest.of(0, 1);
            Page<ProductDTO_V2> productPage = productService.findProductsWithSaleInfo(null, null, pageable);

            // Find the specific product (this is a simplified approach)
            ProductDTO_V2 product = productPage.getContent().stream()
                    .filter(p -> p.getProductID().equals(id))
                    .findFirst()
                    .orElse(null);

            if (product == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Product not found");
                return ResponseEntity.notFound().build();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("product", product);
            response.put("success", true);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving product: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}

