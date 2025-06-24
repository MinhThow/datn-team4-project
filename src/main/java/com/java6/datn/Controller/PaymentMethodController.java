package com.java6.datn.Controller;

import com.java6.datn.DTO.PaymentMethodDTO;
import com.java6.datn.Service.PaymentMethodService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
@CrossOrigin(origins = "*") // Nếu bạn cần CORS
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping
    public List<PaymentMethodDTO> getAll() {
        return paymentMethodService.getAllPaymentMethods();
    }

    @GetMapping("/{id}")
    public PaymentMethodDTO getById(@PathVariable Long id) {
        return paymentMethodService.getPaymentMethodById(id);
    }

    @PostMapping
    public PaymentMethodDTO create(@RequestBody PaymentMethodDTO paymentMethodDTO) {
        return paymentMethodService.createPaymentMethod(paymentMethodDTO);
    }

    @PutMapping("/{id}")
    public PaymentMethodDTO update(@PathVariable Long id, @RequestBody PaymentMethodDTO paymentMethodDTO) {
        return paymentMethodService.updatePaymentMethod(id, paymentMethodDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        paymentMethodService.deletePaymentMethod(id);
    }
}
