package com.java6.datn.Service;

import com.java6.datn.DTO.PaymentMethodDTO;
import java.util.List;

public interface PaymentMethodService {
    List<PaymentMethodDTO> getAllPaymentMethods();
    PaymentMethodDTO getPaymentMethodById(Integer id);
    PaymentMethodDTO createPaymentMethod(PaymentMethodDTO paymentMethodDTO);
    PaymentMethodDTO updatePaymentMethod(Integer id, PaymentMethodDTO paymentMethodDTO);
    void deletePaymentMethod(Integer id);
}

