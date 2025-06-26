package com.java6.datn.controller;

import com.java6.datn.dto.CartItemDTO;
import com.java6.datn.service.CartItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
@CrossOrigin(origins = "*")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public List<CartItemDTO> getAll() {
        return cartItemService.getAllCartItems();
    }

    @GetMapping("/user/{userID}")
    public List<CartItemDTO> getByUser(@PathVariable Integer userID) {
        return cartItemService.getCartItemsByUser(userID);
    }

    @GetMapping("/{id}")
    public CartItemDTO getById(@PathVariable Integer id) {
        return cartItemService.getCartItemById(id);
    }

    @PostMapping
    public CartItemDTO create(@RequestBody CartItemDTO cartItemDTO) {
        return cartItemService.createCartItem(cartItemDTO);
    }

    @PutMapping("/{id}")
    public CartItemDTO update(@PathVariable Integer id, @RequestBody CartItemDTO cartItemDTO) {
        return cartItemService.updateCartItem(id, cartItemDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        cartItemService.deleteCartItem(id);
    }
}

