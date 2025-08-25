package com.java6.datn.Controller;

import com.java6.datn.DTO.CartItemDTO;
import com.java6.datn.DTO.CartUpdateRequest;
import com.java6.datn.Entity.Product;
import com.java6.datn.Repository.CartItemRepository;
import com.java6.datn.Repository.ProductRepository;
import com.java6.datn.Service.CartItemService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.java6.datn.Security.CustomUserDetails;


import com.java6.datn.Entity.CartItem;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired private CartItemService cartItemService;
    @Autowired
    private CartItemRepository cartItemRepo;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemService.CartService cartService;


    @GetMapping
    public ResponseEntity<?> getCart() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
    Integer userId = userDetails.getUserId();
        List<CartItemDTO> cart = cartItemService.getCartDTOByUser(userId);
        return ResponseEntity.ok(cart);
    }

    private Integer getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return userDetails.getUserId();
    }

    public ResponseEntity<?> getCart(HttpSession session) {

        Integer userId = getCurrentUserId(); // laays id user

        if (userId == null) return ResponseEntity.status(401).body("Người dùng chưa đăng nhập");

        List<CartItemDTO> cart = cartItemService.getCartDTOByUser(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(HttpSession session,
                                       @RequestParam Integer productId,
                                       @RequestParam Integer productSizeId,
                                       @RequestParam Integer quantity) {
//        Integer userId = (Integer) session.getAttribute("userId");
        Integer userId = getCurrentUserId();

        if (userId == null)
            return ResponseEntity.status(401).body("Chưa đăng nhập");

        CartItem item = cartItemService.addOrUpdateCart(userId, productId, productSizeId, quantity);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestParam("userId") Integer userId) {
        cartService.clearCartByUserId(userId);
        return ResponseEntity.ok("Đã xoá");
    }

//    @DeleteMapping("/api/cart/clear")
//    public ResponseEntity<?> clearCart(@RequestParam("userId") Integer userId) {
//        cartItemService.clearCartByUserId(userId);  // ⬅ bạn cần implement method này
//        return ResponseEntity.noContent().build();
//    }


    @PutMapping("/update")
    public ResponseEntity<?> updateQuantity(@RequestBody Map<String, Object> body) {
        try {
            Integer cartItemID = ((Number) body.get("cartItemID")).intValue();
            Integer quantity = ((Number) body.get("quantity")).intValue();

            if (cartItemID == null || quantity == null || quantity <= 0) {
                return ResponseEntity.badRequest().body("Invalid input");
            }

            Optional<CartItem> optional = cartItemRepo.findById(cartItemID);
            if (optional.isPresent()) {
                CartItem item = optional.get();
                item.setQuantity(quantity);
                cartItemRepo.save(item);
                return ResponseEntity.ok("Updated");
            }

            return ResponseEntity.badRequest().body("CartItem not found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        }
    }

    @PutMapping("/api/cart/update")
    public ResponseEntity<?> updateQuantity(@RequestBody CartUpdateRequest request) {
        Integer id = request.getCartItemID();
        Optional<CartItem> optional = cartItemRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm");
        }

        CartItem item = optional.get();
        item.setQuantity(request.getQuantity());

        // lấy giá từ productID
        Optional<Product> productOpt = productRepository.findById(item.getProductID());
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy thông tin sản phẩm");
        }

        cartItemRepository.save(item);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCartItem(@RequestParam("id") Integer id) {
        if (!cartItemRepo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm trong giỏ");
        }
        cartItemRepo.deleteById(id);
        return ResponseEntity.ok("Đã xóa sản phẩm khỏi giỏ");
    }



}


