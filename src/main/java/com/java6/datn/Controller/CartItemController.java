package com.java6.datn.Controller;

import com.java6.datn.DTO.CartItemDTO;
import com.java6.datn.Service.CartItemService;
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
    public List<CartItemDTO> getByUser(@PathVariable Long userID) {
        return cartItemService.getCartItemsByUser(userID);
    }

    @GetMapping("/{id}")
    public CartItemDTO getById(@PathVariable Long id) {
        return cartItemService.getCartItemById(id);
    }

    @PostMapping
    public CartItemDTO create(@RequestBody CartItemDTO cartItemDTO) {
        return cartItemService.createCartItem(cartItemDTO);
    }

    @PutMapping("/{id}")
    public CartItemDTO update(@PathVariable Long id, @RequestBody CartItemDTO cartItemDTO) {
        return cartItemService.updateCartItem(id, cartItemDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
    }
}

