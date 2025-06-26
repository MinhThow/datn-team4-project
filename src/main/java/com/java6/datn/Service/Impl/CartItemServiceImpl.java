package com.java6.datn.Service.Impl;

import com.java6.datn.DTO.CartItemDTO;
import com.java6.datn.Entity.CartItem;
import com.java6.datn.Entity.Product;
import com.java6.datn.Entity.User;
import com.java6.datn.Mapper.CartItemMapper;
import com.java6.datn.Repository.CartItemRepository;
import com.java6.datn.Repository.ProductRepository;
import com.java6.datn.Repository.UserRepository;
import com.java6.datn.Service.CartItemService;
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

