package com.java6.datn.Service;

import com.java6.datn.DTO.PaymentMethodDTO;
import java.util.List;

public interface PaymentMethodService {
    List<PaymentMethodDTO> getAllPaymentMethods();
    PaymentMethodDTO getPaymentMethodById(Long id);
    PaymentMethodDTO createPaymentMethod(PaymentMethodDTO paymentMethodDTO);
    PaymentMethodDTO updatePaymentMethod(Long id, PaymentMethodDTO paymentMethodDTO);
    void deletePaymentMethod(Long id);
}

