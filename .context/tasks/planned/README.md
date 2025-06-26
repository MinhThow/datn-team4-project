# Tác vụ đã lên kế hoạch

Các tác vụ sẵn sàng để bắt đầu nhưng chưa được kích hoạt.

## Cách sử dụng
- Sao chép từ mẫu: `cp .context/templates/tasks/TEMPLATE.md .context/tasks/planned/TASK-XXX.md`
- Cập nhật nội dung và front matter
- Di chuyển sang active khi bắt đầu: `mv .context/tasks/planned/TASK-XXX.md .context/tasks/active/TASK-XXX.md`

## Trạng thái
Tất cả tác vụ trong thư mục này phải có `status: planned` trong front matter. 