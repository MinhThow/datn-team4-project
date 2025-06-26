package com.java6.datn.controller;

import com.java6.datn.dto.OrderDTO;
import com.java6.datn.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderDTO> getAll() {
        return orderService.getAllOrders();
    }

    @GetMapping("/user/{userID}")
    public List<OrderDTO> getByUser(@PathVariable Integer userID) {
        return orderService.getOrdersByUser(userID);
    }

    @GetMapping("/{id}")
    public OrderDTO getById(@PathVariable Integer id) {
        return orderService.getOrderById(id);
    }

    @PostMapping
    public OrderDTO create(@RequestBody OrderDTO orderDTO) {
        return orderService.createOrder(orderDTO);
    }

    @PutMapping("/{id}")
    public OrderDTO update(@PathVariable Integer id, @RequestBody OrderDTO orderDTO) {
        return orderService.updateOrder(id, orderDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        orderService.deleteOrder(id);
    }
}

