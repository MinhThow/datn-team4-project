---
title: "Phân tích & Mapping dữ liệu động cho Homepage"
type: "task"
status: "completed"
created: "2025-06-25T22:30:00"
updated: "2025-06-25T23:30:00"
id: "TASK-HP-001"
priority: "high"
memory_types: ["procedural"]
dependencies: []
tags: ["homepage", "analysis", "mapping", "template"]
---

# TASK-HP-001: Phân tích & Mapping dữ liệu động cho Homepage

## Description
Phân tích template `index.html` để xác định các block dữ liệu động cần render trên trang chủ (sản phẩm, danh mục, banner, v.v.). Mapping từng block với dữ liệu thực tế từ database (Products, Categories, Sales, SaleDetails). Đề xuất cấu trúc dữ liệu trả về từ backend cho từng block.

## Objectives
- Xác định tất cả các block động trên trang chủ.
- Mapping từng block với bảng dữ liệu thực tế trong DB.
- Đề xuất cấu trúc dữ liệu JSON/model cho từng block.
- Làm cơ sở cho các task backend/frontend tiếp theo.

## Steps
1. [x] Đọc và phân tích template `src/main/resources/templates/index.html`.
2. [x] Liệt kê các block động (sản phẩm nổi bật, danh mục, banner, v.v.).
3. [x] Mapping từng block với bảng dữ liệu thực tế (`Products`, `Categories`, `Sales`, `SaleDetails`).
4. [x] Đề xuất cấu trúc dữ liệu trả về từ backend cho từng block (JSON/model).
5. [x] Ghi chú các trường hợp đặc biệt (ví dụ: sản phẩm có sale, banner lấy từ bảng Sales).
6. [x] Tổng hợp thành tài liệu mapping để các task backend/frontend dựa vào.

## Progress
- [x] Template phân tích xong
- [x] Block động liệt kê xong
- [x] Mapping hoàn thành
- [x] Cấu trúc dữ liệu đề xuất xong
- [x] Tài liệu mapping hoàn thiện

## Dependencies
- Không

## Notes
- Task này là nền tảng cho toàn bộ luồng backend/frontend homepage.
- Nên tham khảo kỹ file SQL và template để tránh thiếu sót.
- **COMPLETED**: Đã phân tích đầy đủ 7 block động chính trên homepage.
- **COMPLETED**: Mapping chi tiết với database schema hiện có.
- **COMPLETED**: Đề xuất cấu trúc JSON response cho từng API endpoint.
- **COMPLETED**: Xác định ưu tiên implementation (Phase 1-3).
- **DOCUMENT CREATED**: `documentation/homepage_data_mapping_analysis.md`

## Next Steps
- Task hoàn thành và sẵn sàng chuyển sang completed status.
- Các task backend tiếp theo có thể dựa vào tài liệu mapping này để implement.
- Ưu tiên Phase 1: Product API, Search API, Cart Summary API. 