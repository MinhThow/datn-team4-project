package com.java6.datn.Mapper;

import com.java6.datn.DTO.PaymentMethodDTO;
import com.java6.datn.Entity.PaymentMethod;

public class PaymentMethodMapper {

    public static PaymentMethodDTO toDTO(PaymentMethod entity) {
        PaymentMethodDTO dto = new PaymentMethodDTO();
        dto.setPaymentMethodID(entity.getPaymentMethodID());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public static PaymentMethod toEntity(PaymentMethodDTO dto) {
        PaymentMethod entity = new PaymentMethod();
        entity.setPaymentMethodID(dto.getPaymentMethodID());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }
}
