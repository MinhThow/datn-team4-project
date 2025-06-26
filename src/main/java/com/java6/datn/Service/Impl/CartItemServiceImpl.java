package com.java6.datn.service.impl;

import com.java6.datn.dto.CartItemDTO;
import com.java6.datn.entity.CartItem;
import com.java6.datn.entity.Product;
import com.java6.datn.entity.User;
import com.java6.datn.mapper.CartItemMapper;
import com.java6.datn.repository.CartItemRepository;
import com.java6.datn.repository.ProductRepository;
import com.java6.datn.repository.UserRepository;
import com.java6.datn.service.CartItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository,
                               UserRepository userRepository,
                               ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<CartItemDTO> getAllCartItems() {
        return cartItemRepository.findAll().stream()
                .map(CartItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CartItemDTO> getCartItemsByUser(Integer userID) {
        return cartItemRepository.findByUserUserID(userID).stream()
                .map(CartItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CartItemDTO getCartItemById(Integer id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));
        return CartItemMapper.toDTO(cartItem);
    }

    @Override
    public CartItemDTO createCartItem(CartItemDTO cartItemDTO) {
        User user = userRepository.findById(cartItemDTO.getUserID())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(cartItemDTO.getProductID())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemDTO.getQuantity());
        return CartItemMapper.toDTO(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemDTO updateCartItem(Integer id, CartItemDTO cartItemDTO) {
        CartItem existing = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        CartItemMapper.updateEntityFromDTO(existing, cartItemDTO);

        return CartItemMapper.toDTO(cartItemRepository.save(existing));
    }

    @Override
    public void deleteCartItem(Integer id) {
        cartItemRepository.deleteById(id);
    }
}

