package com.java6.datn.Service;

import com.java6.datn.DTO.CartItemDTO;
import com.java6.datn.Entity.CartItem;
import com.java6.datn.Entity.Product;

import com.java6.datn.Entity.ProductImage;
import com.java6.datn.Entity.ProductSize;
import com.java6.datn.Repository.CartItemRepository;
import com.java6.datn.Repository.ProductRepository;
import com.java6.datn.Repository.ProductSizeRepository;
import com.java6.datn.Repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {
    @Autowired private CartItemRepository cartItemRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private ProductSizeRepository productSizeRepo;
    @Autowired private ProductImageRepository productImageRepo;

    public List<CartItemDTO> getCartDTOByUser(Integer userId) {
        List<CartItem> items = cartItemRepo.findByUserID(userId);
        List<CartItemDTO> result = new ArrayList<>();

        for (CartItem item : items) {
            Product product = productRepo.findById(item.getProductID()).orElse(null);
            ProductSize size = productSizeRepo.findById(item.getProductSizeID()).orElse(null);
            ProductImage img = (product != null) ? productImageRepo.findFirstByProductAndIsMain(product, true) : null;

            if (product == null || size == null) continue;

            BigDecimal price = product.getPrice();
            BigDecimal total = price.multiply(BigDecimal.valueOf(item.getQuantity()));

            CartItemDTO dto = new CartItemDTO();
            dto.setProductID(item.getProductID());
            dto.setProductSizeID(item.getProductSizeID());
            dto.setCartItemID(item.getCartItemID());
            dto.setProductName(product.getName());
            dto.setSize(size.getSize());
            dto.setImageUrl(img != null ? img.getImageUrl() : "");
            dto.setPrice(price);
            dto.setQuantity(item.getQuantity());
            dto.setTotal(total);
            result.add(dto);
        }

        return result;
    }

    public CartItem addOrUpdateCart(Integer userId, Integer productId, Integer productSizeId, Integer quantity) {
        Optional<CartItem> existing = cartItemRepo.findByUserIDAndProductIDAndProductSizeID(userId, productId, productSizeId);
        CartItem item;
        if (existing.isPresent()) {
            item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            item = new CartItem();
            item.setUserID(userId);
            item.setProductID(productId);
            item.setProductSizeID(productSizeId);
            item.setQuantity(quantity);
            item.setAddedAt(new Timestamp(System.currentTimeMillis()));
        }
        return cartItemRepo.save(item);
    }

    public void deleteCartItem(Integer cartItemId) {
        cartItemRepo.deleteById(cartItemId);
    }

//    public void clearCart(Integer userId) {
//        List<CartItem> items = cartItemRepo.findByUserID(userId);
//        cartItemRepo.deleteAll(items);
//    }

    public interface CartService {
        void clearCartByUserId(Integer userId);
    }

}