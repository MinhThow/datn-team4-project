---
title: "Tạo API tìm kiếm sản phẩm (có phân trang, sale price)"
type: "task"
status: "planned"
created: "2025-06-25T22:50:00"
updated: "2025-06-25T22:50:00"
id: "TASK-HP-007"
priority: "high"
memory_types: ["procedural"]
dependencies: ["TASK-HP-002"]
tags: ["backend", "api", "search", "product"]
---

# TASK-HP-007: Tạo API tìm kiếm sản phẩm (có phân trang, sale price)

## Description
Tạo API tìm kiếm sản phẩm theo tên/mô tả, trả về đúng cấu trúc homepage, có phân trang, join sale price nếu có.

## Objectives
- API tìm kiếm theo tên/mô tả sản phẩm.
- Trả về đúng cấu trúc dữ liệu homepage (có sale price, phân trang).
- Hỗ trợ các tham số `keyword`, `page`, `size`.

## Steps
1. [ ] Tạo endpoint `/api/v1/products/search` trong ProductController.
2. [ ] Bổ sung logic tìm kiếm theo tên/mô tả trong ProductService.
3. [ ] Đảm bảo join SaleDetails để lấy sale price nếu có.
4. [ ] Hỗ trợ phân trang.
5. [ ] Viết unit test cho service.
6. [ ] Kiểm thử API bằng Postman/Swagger.

## Progress
- [ ] Endpoint tạo xong
- [ ] Logic tìm kiếm hoàn thiện
- [ ] Logic sale price hoàn thiện
- [ ] Phân trang hoàn thiện
- [ ] Test hoàn thiện

## Dependencies
- TASK-HP-002 (Product API/Service/Repository)

## Notes
- Đảm bảo đúng chuẩn Java naming convention.
- Ưu tiên code rõ ràng, dễ mở rộng.

## Next Steps
- Sau khi hoàn thành, chuyển sang task tích hợp Thymeleaf. 