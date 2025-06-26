# Tác vụ tạm dừng

Các tác vụ bị chặn hoặc tạm dừng vì lý do bên ngoài.

## Cách sử dụng
- Di chuyển từ active khi bị chặn: `mv .context/tasks/active/TASK-XXX.md .context/tasks/hold/TASK-XXX.md`
- Cập nhật trạng thái thành `hold` và thêm lý do chặn trong ghi chú
- Di chuyển lại sang active khi được bỏ chặn

## Trạng thái
Tất cả tác vụ trong thư mục này phải có `status: hold` trong front matter. 