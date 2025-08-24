package com.java6.datn.Config;

public class VnPayConfig {
    public static final String VNPAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static final String TMN_CODE = "W4LSGG8Z"; // trong email VNPAY gửi
    public static final String HASH_SECRET = "T1746A1PAXYTJ5UZ352K9V39J7NSXY8C"; // trong email VNPAY gửi
    public static final String RETURN_URL = "http://localhost:8080/api/pay/vnpay-return";
}
