package com.java6.datn.Service;

import java.math.BigDecimal;

/**
 * CartService - Interface định nghĩa các service methods cho quản lý giỏ hàng
 * 
 * <p>Interface này cung cấp các operations cơ bản để tính toán thông tin giỏ hàng:</p>
 * 
 * <ul>
 *   <li><strong>Cart Item Count:</strong> Đếm số lượng items trong giỏ hàng</li>
 *   <li><strong>Cart Total:</strong> Tính tổng giá trị giỏ hàng</li>
 * </ul>
 * 
 * <p><strong>Use Cases:</strong></p>
 * <ul>
 *   <li>Hiển thị badge số lượng items trên header</li>
 *   <li>Hiển thị tổng tiền trên header</li>
 *   <li>Tính toán cho checkout process</li>
 *   <li>Analytics và reporting</li>
 * </ul>
 * 
 * <p><strong>Business Logic:</strong></p>
 * <ul>
 *   <li>Nếu userID = null (guest user): trả về 0</li>
 *   <li>Chỉ tính các cart items có trạng thái active</li>
 *   <li>Tính toán dựa trên quantity × price của từng item</li>
 * </ul>
 * 
 * <p><strong>Implementation:</strong> CartServiceImpl</p>
 * <p><strong>Related Services:</strong> CartItemService, ProductService</p>
 * <p><strong>Related Entities:</strong> CartItem, Product, User</p>
 * 
 * @author DATN Team 4
 * @version 1.0
 * @since 2025-01-16
 * @see com.java6.datn.Service.Impl.CartServiceImpl
 * @see com.java6.datn.Service.CartItemService
 * @see com.java6.datn.Entity.CartItem
 */
public interface CartService {
    
    /**
     * Đếm tổng số lượng items trong giỏ hàng của user
     * 
     * <p>Method này tính tổng số lượng tất cả items trong giỏ hàng.
     * Ví dụ: nếu có 2 items với quantity 3 và 2, kết quả sẽ là 5.</p>
     * 
     * <p><strong>Logic:</strong></p>
     * <ol>
     *   <li>Nếu userID = null → return 0 (guest user)</li>
     *   <li>Lấy tất cả CartItem của user</li>
     *   <li>Sum quantity của tất cả items</li>
     *   <li>Return tổng số lượng</li>
     * </ol>
     * 
     * <p><strong>Sử dụng cho:</strong></p>
     * <ul>
     *   <li>Badge hiển thị số items trên cart icon</li>
     *   <li>Validation trước khi checkout</li>
     *   <li>Analytics tracking</li>
     * </ul>
     * 
     * @param userID ID của user cần đếm cart items (nullable)
     * @return int tổng số lượng items trong giỏ hàng, 0 nếu empty hoặc user = null
     * 
     * @see com.java6.datn.Service.CartItemService#getCartItemsByUserID(Integer)
     */
    int getCartItemCount(Integer userID);
    
    /**
     * Tính tổng giá trị của tất cả items trong giỏ hàng
     * 
     * <p>Method này tính tổng tiền của giỏ hàng bằng cách:</p>
     * <ol>
     *   <li>Lấy tất cả CartItem của user</li>
     *   <li>Với mỗi item: lấy price từ Product</li>
     *   <li>Tính: item.quantity × product.price</li>
     *   <li>Sum tất cả values</li>
     * </ol>
     * 
     * <p><strong>Business Rules:</strong></p>
     * <ul>
     *   <li>Nếu userID = null → return BigDecimal.ZERO</li>
     *   <li>Sử dụng current price từ Product table (không cache)</li>
     *   <li>Handle case product bị xóa hoặc inactive</li>
     *   <li>Precision: 2 decimal places cho currency</li>
     * </ul>
     * 
     * <p><strong>Sử dụng cho:</strong></p>
     * <ul>
     *   <li>Hiển thị tổng tiền trên header</li>
     *   <li>Cart summary page</li>
     *   <li>Checkout calculation</li>
     *   <li>Order preview</li>
     * </ul>
     * 
     * @param userID ID của user cần tính tổng giỏ hàng (nullable)
     * @return BigDecimal tổng giá trị giỏ hàng, ZERO nếu empty hoặc user = null
     * 
     * @see com.java6.datn.Service.CartItemService#getCartItemsByUserID(Integer)
     * @see com.java6.datn.Service.ProductService#getProductById(Integer)
     */
    BigDecimal getCartTotal(Integer userID);
} 