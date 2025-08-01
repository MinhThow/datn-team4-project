package com.java6.datn.Service.Impl;

import com.java6.datn.DTO.OrderItemDTO;
import com.java6.datn.DTO.OrderRequestDTO;
import com.java6.datn.DTO.OrderResponseDTO;
import com.java6.datn.Entity.*;
import com.java6.datn.Repository.*;
import com.java6.datn.Service.OrderService;
import com.java6.datn.Service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final CartItemService.CartService cartService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            UserRepository userRepository,
                            ProductRepository productRepository,
                            ProductSizeRepository productSizeRepository,
                            PaymentMethodRepository paymentMethodRepository,
                            CartItemService.CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productSizeRepository = productSizeRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.cartService = cartService;
    }

    @Override
    @Transactional // Đảm bảo toàn bộ phương thức được thực thi như một giao dịch duy nhất
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {
        // 1. Tìm User và PaymentMethod từ ID
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + requestDTO.getUserId()));
        PaymentMethod paymentMethod = paymentMethodRepository.findById(requestDTO.getPaymentMethodId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phương thức thanh toán với ID: " + requestDTO.getPaymentMethodId()));

        // 2. Tính toán lại tổng tiền ở backend để đảm bảo an toàn
        BigDecimal total = requestDTO.getOrderItems().stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Tạo đối tượng Order và lưu để lấy OrderID
        Order order = Order.builder()
                .user(user)
                .recipientName(requestDTO.getRecipientName())
                .phone(requestDTO.getPhone())
                .shippingAddress(requestDTO.getShippingAddress())
                .note(requestDTO.getNote())
                .total(total)
                .paymentMethod(paymentMethod)
                .paymentMethodName(paymentMethod.getName())
                // status và orderDate sẽ được tự động thiết lập bởi @PrePersist trong Entity
                .build();

        Order savedOrder = orderRepository.save(order);

        // 4. Tạo danh sách các OrderItem từ DTO
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDTO itemDTO : requestDTO.getOrderItems()) {
            Product product = productRepository.findById(itemDTO.getProductID())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + itemDTO.getProductID()));
            ProductSize productSize = productSizeRepository.findById(itemDTO.getProductSizeID())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy size sản phẩm với ID: " + itemDTO.getProductSizeID()));

            // Kiểm tra tồn kho và trừ số lượng
            int currentStock = productSize.getStock() != null ? productSize.getStock() : 0;
            if (currentStock < itemDTO.getQuantity()) {
                throw new RuntimeException("Số lượng tồn kho không đủ cho sản phẩm: " + product.getName() + ", size: " + productSize.getSize());
            }
            productSize.setStock(currentStock - itemDTO.getQuantity());
            productSizeRepository.save(productSize);

            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder) // Quan trọng: Gán order vừa được lưu
                    .product(product)
                    .productSize(productSize)
                    .productName(itemDTO.getProductName()) // Lưu lại tên sản phẩm tại thời điểm mua
                    .size(itemDTO.getSize())               // Lưu lại size tại thời điểm mua
                    .quantity(itemDTO.getQuantity())
                    .price(itemDTO.getPrice())             // Lưu lại giá tại thời điểm mua
                    .build();
            orderItems.add(orderItem);
        }

        // 5. Lưu tất cả OrderItem vào database
        orderItemRepository.saveAll(orderItems);
        savedOrder.setOrderItems(orderItems); // Cập nhật lại list item cho đối tượng order

        // 6. Xóa giỏ hàng của user sau khi đặt hàng thành công
        cartService.clearCartByUserId(requestDTO.getUserId());

        // 7. Chuyển đổi sang DTO để trả về cho client
        return mapToOrderResponseDTO(savedOrder);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponseDTO> result = new ArrayList<>();
        for (Order order : orders) {
            result.add(mapToOrderResponseDTO(order));
        }
        return result;
    }

    @Override
    public OrderResponseDTO getOrderById(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return null;
        return mapToOrderResponseDTO(order);
    }

    @Override
    public OrderResponseDTO updateOrderStatus(Integer orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return null;
        order.setStatus(status);
        orderRepository.save(order);
        return mapToOrderResponseDTO(order);
    }

    private OrderResponseDTO mapToOrderResponseDTO(Order order) {
        List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                .map(item -> OrderItemDTO.builder()
                        .productID(item.getProduct().getProductID())
                        .productSizeID(item.getProductSize().getProductSizeID())
                        .productName(item.getProductName())
                        .size(item.getSize())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .collect(java.util.stream.Collectors.toList());

        return OrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .recipientName(order.getRecipientName())
                .phone(order.getPhone())
                .shippingAddress(order.getShippingAddress())
                .note(order.getNote())
                .total(order.getTotal())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .paymentMethodName(order.getPaymentMethodName())
                .orderItems(orderItemDTOs)
                .name(order.getUser() != null ? order.getUser().getName() : "")
                .build();
    }
}
