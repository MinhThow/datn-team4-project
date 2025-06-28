package com.java6.datn.Service;

import java.math.BigDecimal;

public interface CartService {
    int getCartItemCount(Integer userID);
    BigDecimal getCartTotal(Integer userID);
} 