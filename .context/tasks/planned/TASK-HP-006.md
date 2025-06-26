---
title: "Tạo Banner API/Service/Repository (dùng bảng Sales)"
type: "task"
status: "planned"
created: "2025-06-25T22:45:00"
updated: "2025-06-25T22:45:00"
id: "TASK-HP-006"
priority: "high"
memory_types: ["procedural"]
dependencies: ["TASK-HP-001"]
tags: ["backend", "api", "banner", "sales", "repository", "service"]
---

# TASK-HP-006: Tạo Banner API/Service/Repository (dùng bảng Sales)

## Description
Tạo entity, repository, service, controller cho banner/slider trang chủ, lấy dữ liệu từ bảng Sales. API trả về danh sách banner đúng với cấu trúc dữ liệu đã mapping từ homepage.

## Objectives
- Tạo entity Sale (nếu chưa có).
- Tạo repository, service, controller cho banner (dùng bảng Sales).
- API trả về đúng trường dữ liệu với DB (`SaleID`, `Name`, `Description`, `StartDate`, `EndDate`).

## Steps
1. [ ] Tạo entity Sale (nếu chưa có).
2. [ ] Tạo SaleRepository.
3. [ ] Tạo BannerService và BannerServiceImpl.
4. [ ] Tạo BannerController (API: `/api/v1/banners`).
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
- Sau khi hoàn thành, chuyển sang task Search. 