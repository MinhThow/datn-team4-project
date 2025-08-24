package com.java6.datn.Momo;

import java.util.HashMap;
import java.util.Map;
// giả lập đơn hàng
public class OrderStore {
    // giả DB: orderId -> totalAmount (VND)
    private static final Map<Long, Long> DATA = new HashMap<>();
    static {
        DATA.put(1L, 10000L);   // 10.000đ
        DATA.put(2L, 250000L);  // 250.000đ
    }
    public static Long getTotal(Long orderId) {
        return DATA.get(orderId);
    }
}
