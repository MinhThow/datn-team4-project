package com.java6.datn.Controller;

import com.java6.datn.DTO.CartItemDTO;
import com.java6.datn.DTO.CartUpdateRequest;
import com.java6.datn.Entity.Product;
import com.java6.datn.Repository.CartItemRepository;
import com.java6.datn.Repository.ProductRepository;
import com.java6.datn.Service.CartItemService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping
//    public ResponseEntity<?> getCart(HttpSession session) {
//        Integer userId = (Integer) session.getAttribute("userId");
//        if (userId == null)
//            return ResponseEntity.status(401).body("Chưa đăng nhập");
//
//        List<CartItemDTO> cart = cartItemService.getCartDTOByUser(userId);
//        return ResponseEntity.ok(cart);
//    }

    public ResponseEntity<?> getCart(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");

        // ⚠ Tạm gán userId để test
        if (userId == null) {
            userId = 2;
            session.setAttribute("userId", userId); // tạm gán session
        }

        List<CartItemDTO> cart = cartItemService.getCartDTOByUser(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(HttpSession session,
                                       @RequestParam Integer productId,
                                       @RequestParam Integer productSizeId,
                                       @RequestParam Integer quantity) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null)
            return ResponseEntity.status(401).body("Chưa đăng nhập");

        CartItem item = cartItemService.addOrUpdateCart(userId, productId, productSizeId, quantity);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Integer id) {
        cartItemService.deleteCartItem(id);
        return ResponseEntity.ok("Đã xoá");
    }

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


}


