---
title: "Tích hợp Thymeleaf render dữ liệu động vào index.html"
type: "task"
status: "planned"
created: "2025-06-25T22:55:00"
updated: "2025-06-25T22:55:00"
id: "TASK-HP-008"
priority: "high"
memory_types: ["procedural"]
dependencies: ["TASK-HP-002", "TASK-HP-005", "TASK-HP-006", "TASK-HP-007"]
tags: ["frontend", "thymeleaf", "homepage", "integration"]
---

# TASK-HP-008: Tích hợp Thymeleaf render dữ liệu động vào index.html

## Description
Tích hợp dữ liệu động từ các API backend vào template `index.html` bằng Thymeleaf. Đảm bảo các block sản phẩm, danh mục, banner, tìm kiếm đều được render động đúng dữ liệu.

## Objectives
- Controller trả về model cho template `index.html`.
- Sử dụng Thymeleaf để render các block động (sản phẩm, danh mục, banner, tìm kiếm).
- Đảm bảo giao diện hiển thị đúng dữ liệu thực tế.

## Steps
1. [ ] Tạo HomepageController trả về model cho `index.html`.
2. [ ] Lấy dữ liệu từ các service (Product, Category, Banner, Search) và add vào model.
3. [ ] Sử dụng Thymeleaf để render từng block động trong template.
4. [ ] Kiểm thử giao diện với dữ liệu thực tế.
5. [ ] Viết hướng dẫn sử dụng cho team frontend nếu cần.

## Progress
- [ ] Controller tạo xong
- [ ] Model trả về đúng dữ liệu
- [ ] Thymeleaf render đúng block
- [ ] Giao diện kiểm thử xong
- [ ] Hướng dẫn sử dụng hoàn thiện

## Dependencies
- TASK-HP-002 (Product)
- TASK-HP-005 (Category)
- TASK-HP-006 (Banner)
- TASK-HP-007 (Search)

## Notes
- Đảm bảo đúng chuẩn Java naming convention.
- Ưu tiên code rõ ràng, dễ mở rộng.

## Next Steps
- Sau khi hoàn thành, kiểm thử tổng thể homepage. 