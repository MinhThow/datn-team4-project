package com.java6.datn.Service;

import com.java6.datn.Config.VnPayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class VnPayService {

    // =========================
    // Helpers
    // =========================
    /** URL-encode theo mặc định (space -> '+') giống VNPAY */
    private static String urlEncode(String s) throws Exception {
        if (s == null) return "";
        return URLEncoder.encode(s, StandardCharsets.US_ASCII.toString());
    }

    private static String hmacSHA512(String key, String data) throws Exception {
        Mac hmac512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac512.init(secretKey);
        byte[] hashBytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    // =========================
    // Tạo link thanh toán
    // =========================
    public String createPaymentUrl(int orderId, long amount, String ipAddress) throws Exception {
        // Fix IPv6 localhost -> IPv4
        if ("0:0:0:0:0:0:0:1".equals(ipAddress)) {
            ipAddress = "127.0.0.1";
        }

        String vnp_TxnRef = orderId + "_" + System.currentTimeMillis(); // unique
        String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", VnPayConfig.TMN_CODE);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // x100 theo chuẩn VNPAY
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang " + orderId);
        vnp_Params.put("vnp_OrderType", "billpayment");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.RETURN_URL);
        vnp_Params.put("vnp_IpAddr", ipAddress);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        // sort key
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        // build hashData & query (PHẢI GIỐNG NHAU)
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String k = fieldNames.get(i);
            String v = vnp_Params.get(k);
            if (v != null && !v.isEmpty()) {
                String ek = urlEncode(k);
                String ev = urlEncode(v);
                hashData.append(ek).append("=").append(ev);
                query.append(ek).append("=").append(ev);
                if (i < fieldNames.size() - 1) {
                    hashData.append("&");
                    query.append("&");
                }
            }
        }

        String vnp_SecureHash = hmacSHA512(VnPayConfig.HASH_SECRET, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        query.append("&vnp_SecureHashType=HmacSHA512"); // thêm sau cùng, KHÔNG hash

        String paymentUrl = VnPayConfig.VNPAY_URL + "?" + query;

        // logs
        log.info("🧾 ==== TẠO URL VNPAY ====");
        log.info("orderId: {}", orderId);
        log.info("amount (VND): {}", amount);
        log.info("hashData: {}", hashData);
        log.info("secureHash (local): {}", vnp_SecureHash);
        log.info("paymentUrl: {}", paymentUrl);
        log.info("🧾 =======================");

        return paymentUrl;
    }

    // =========================
    // Validate callback từ VNPAY
    // =========================
    public boolean validateReturn(Map<String, String> allParams) throws Exception {
        String vnp_SecureHash = allParams.get("vnp_SecureHash");

        // bỏ các tham số hash ra
        Map<String, String> sorted = new TreeMap<>();
        for (Map.Entry<String, String> e : allParams.entrySet()) {
            String k = e.getKey();
            if (!"vnp_SecureHash".equalsIgnoreCase(k) && !"vnp_SecureHashType".equalsIgnoreCase(k)) {
                sorted.put(k, e.getValue());
            }
        }

        // build lại hashData (PHẢI URL-ENCODE GIỐNG CÁCH GỬI)
        StringBuilder hashData = new StringBuilder();
        for (Iterator<Map.Entry<String, String>> it = sorted.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, String> entry = it.next();
            String ek = urlEncode(entry.getKey());
            String ev = urlEncode(entry.getValue());
            hashData.append(ek).append("=").append(ev);
            if (it.hasNext()) hashData.append("&");
        }

        String myHash = hmacSHA512(VnPayConfig.HASH_SECRET, hashData.toString());

        log.info("🔑 ==== VALIDATE RETURN ====");
        log.info("hashData(rebuilt): {}", hashData);
        log.info("secureHash from VNPAY: {}", vnp_SecureHash);
        log.info("secureHash (local):    {}", myHash);
        log.info("🔑 ========================");

        return myHash.equalsIgnoreCase(vnp_SecureHash);
    }
}
