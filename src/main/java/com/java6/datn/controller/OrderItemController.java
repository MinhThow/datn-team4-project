package com.java6.datn.controller;

import com.java6.datn.dto.OrderItemDTO;
import com.java6.datn.service.OrderItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
@CrossOrigin(origins = "*")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public List<OrderItemDTO> getAll() {
        return orderItemService.getAllOrderItems();
    }

    @GetMapping("/order/{orderID}")
    public List<OrderItemDTO> getByOrder(@PathVariable Integer orderID) {
        return orderItemService.getOrderItemsByOrder(orderID);
    }

    @GetMapping("/{id}")
    public OrderItemDTO getById(@PathVariable Integer id) {
        return orderItemService.getOrderItemById(id);
    }

    @PostMapping
    public OrderItemDTO create(@RequestBody OrderItemDTO orderItemDTO) {
        return orderItemService.createOrderItem(orderItemDTO);
    }

    @PutMapping("/{id}")
    public OrderItemDTO update(@PathVariable Integer id, @RequestBody OrderItemDTO orderItemDTO) {
        return orderItemService.updateOrderItem(id, orderItemDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        orderItemService.deleteOrderItem(id);
    }
}
