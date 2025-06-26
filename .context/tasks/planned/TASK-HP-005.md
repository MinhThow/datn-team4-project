---
title: "Tạo Category API/Service/Repository"
type: "task"
status: "planned"
created: "2025-06-25T22:40:00"
updated: "2025-06-25T22:40:00"
id: "TASK-HP-005"
priority: "high"
memory_types: ["procedural"]
dependencies: ["TASK-HP-001"]
tags: ["backend", "api", "category", "repository", "service"]
---

# TASK-HP-005: Tạo Category API/Service/Repository

## Description
Tạo entity, repository, service, controller cho danh mục sản phẩm. API trả về danh sách category đúng với cấu trúc dữ liệu đã mapping từ homepage.

## Objectives
- Tạo entity Category (nếu chưa có).
- Tạo repository, service, controller cho danh mục.
- API trả về đúng trường dữ liệu với DB (`CategoryID`, `Name`, `Description`).

## Steps
1. [ ] Tạo entity Category (nếu chưa có).
2. [ ] Tạo CategoryRepository.
3. [ ] Tạo CategoryService và CategoryServiceImpl.
4. [ ] Tạo CategoryController (API: `/api/v1/categories`).
5. [ ] Viết unit test cho service.
6. [ ] Kiểm thử API bằng Postman/Swagger.

## Progress
- [ ] Entity tạo xong
- [ ] Repository tạo xong
- [ ] Service tạo xong
- [ ] Controller tạo xong
- [ ] Test hoàn thiện

## Dependencies
- TASK-HP-001 (mapping homepage)

## Notes
- Đảm bảo đúng chuẩn Java naming convention.
- Ưu tiên code rõ ràng, dễ mở rộng.

## Next Steps
- Sau khi hoàn thành, chuyển sang task Banner. 