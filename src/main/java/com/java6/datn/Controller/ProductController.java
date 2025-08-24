package com.java6.datn.Controller;

import com.java6.datn.DTO.ProductDTO;
import com.java6.datn.DTO.ReviewDTO;
import com.java6.datn.Service.ProductService;
import com.java6.datn.Service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;

    public ProductController(ProductService productService, ReviewService reviewService) {
        this.productService = productService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<ProductDTO> getProducts(@RequestParam(required = false) Integer category) {
        if (category != null) {
            return productService.findByCategory(category)
                    .stream()
                    .map(product -> {
                        ProductDTO dto = new ProductDTO();
                        dto.setProductID(product.getProductID());
                        dto.setName(product.getName());
                        dto.setDescription(product.getDescription());
                        dto.setPrice(product.getPrice());
                        dto.setOldPrice(product.getOldPrice());
//                        dto.setStock(product.getStock());
//                        dto.setImage(product.getImage());
//                        dto.setSize(product.getSize());
                        dto.setCategoryID(product.getCategory().getCategoryID());
                        dto.setCategoryName(product.getCategory().getName());
                        dto.setImageUrl(product.getImageUrl());
                        return dto;
                    })
                    .toList();
        }
        return productService.getAllProducts();
    }


    @GetMapping("/{id}")
    public ProductDTO getById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public Map<String, Object> create(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        List<?> productImages = productService.getProductImagesByProductId(createdProduct.getProductID());
        Map<String, Object> response = new HashMap<>();
        response.put("product", createdProduct);
        response.put("productImages", productImages);
        return response;
    }

    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable Integer id, @RequestBody ProductDTO productDTO) {
        return productService.updateProduct(id, productDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/search")
    public List<ProductDTO> searchProducts(@RequestParam String query) {
        return productService.searchProducts(query);
    }

    @GetMapping("/{id}/detail")
    public Map<String, Object> getProductDetail(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Product ID must be positive integer");
        }
        ProductDTO product = productService.getProductById(id);
        List<ReviewDTO> reviews = reviewService.getReviewsByProductId(id);
        Double averageRating = reviewService.getAverageRating(id);
        Integer totalReviews = reviewService.getTotalReviews(id);
        List<ProductDTO> relatedProducts = productService.getRelatedProducts(id);

        Map<String, Object> response = new HashMap<>();
        response.put("product", product);
        response.put("reviews", reviews);
        response.put("averageRating", averageRating);
        response.put("totalReviews", totalReviews);
        response.put("relatedProducts", relatedProducts);

        return response;
    }

    @GetMapping("/{id}/reviews")
    public List<ReviewDTO> getProductReviews(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return reviewService.getReviewsByProductIdWithPagination(id, page, size);
    }

    @PostMapping("/{id}/reviews")
    public ReviewDTO addProductReview(@PathVariable Integer id, @RequestBody ReviewDTO reviewDTO) {
        reviewDTO.setProductId(id);
        return reviewService.createReview(reviewDTO);
    }
}
