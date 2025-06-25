package com.java6.datn.Service.Impl;

import com.java6.datn.DTO.PaymentMethodDTO;
import com.java6.datn.Entity.PaymentMethod;
import com.java6.datn.Mapper.PaymentMethodMapper;
import com.java6.datn.Repository.PaymentMethodRepository;
import com.java6.datn.Service.PaymentMethodService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public List<PaymentMethodDTO> getAllPaymentMethods() {
        return paymentMethodRepository.findAll()
                .stream()
                .map(PaymentMethodMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentMethodDTO getPaymentMethodById(Integer id) {
        PaymentMethod method = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentMethod not found"));
        return PaymentMethodMapper.toDTO(method);
    }

    @Override
    public PaymentMethodDTO createPaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod entity = PaymentMethodMapper.toEntity(paymentMethodDTO);
        return PaymentMethodMapper.toDTO(paymentMethodRepository.save(entity));
    }

    @Override
    public PaymentMethodDTO updatePaymentMethod(Integer id, PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod existing = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentMethod not found"));

        existing.setName(paymentMethodDTO.getName());
        existing.setDescription(paymentMethodDTO.getDescription());

        return PaymentMethodMapper.toDTO(paymentMethodRepository.save(existing));
    }

    @Override
    public void deletePaymentMethod(Integer id) {
        paymentMethodRepository.deleteById(id);
    }
}
