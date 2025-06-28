---
title: "Implement chức năng Add to Cart"
type: task
status: planned
created: 2025-01-27T12:41:19
updated: 2025-01-27T12:41:19
id: TASK-006
priority: high
memory_types: [procedural, semantic]
dependencies: [TASK-003]
tags: [cart, api, javascript, user-interaction]
---

# Implement chức năng Add to Cart

## Mô tả
Phát triển tính năng thêm sản phẩm vào giỏ hàng từ trang chủ, tích hợp với CartItemController có sẵn.

## Mục tiêu
- Tích hợp với CartItemController API
- JavaScript để handle add to cart actions
- Cập nhật cart counter trong header
- User feedback (success/error messages)
- Cart persistence

## Các bước
1. Phân tích CartItemController API
2. Cập nhật product cards với add to cart buttons
3. Implement JavaScript add to cart functionality
4. Cập nhật cart counter trong header
5. Thêm user feedback (toasts/notifications)
6. Handle user authentication

## Tiến độ
- [x] **Phân tích CartItemController API** - ✅ Đã có hoàn chỉnh
- [x] **CartItemService methods** - ✅ Đã có sẵn createCartItem, getCartItemsByUser
- [ ] **Implement add to cart JavaScript** với AJAX calls
- [ ] **Cập nhật cart counter** trong header (real-time)
- [ ] **Thêm success/error notifications** (toast messages)
- [ ] **Test với authenticated users** và handle guest users

## Dependencies
- TASK-003 (Template với dữ liệu động)
- CartItemController API
- User authentication system

## Ghi chú
- ✅ **Template đã có cart icon và counter** trong header (lines 52, 121)
- ✅ **CartItemController API hoàn chỉnh**: POST `/api/cartitems`, GET `/api/cartitems/user/{userID}`
- ✅ **Add to cart buttons** đã có sẵn trong template: `.add-cart`
- **Cần handle**: Unauthenticated users (redirect to login)
- **Đơn giản hóa**: Dùng server-side cart (không cần localStorage)

## Bước tiếp theo
- Implement cart page integration
- Add quantity selectors
- Implement remove from cart functionality 