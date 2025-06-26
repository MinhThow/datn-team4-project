# Quản lý tác vụ

Workflow tác vụ có cấu trúc theo Aegis Framework.

## Trạng thái tác vụ
- `planned/` - Tác vụ sẵn sàng để bắt đầu
- `active/` - Đang thực hiện
- `hold/` - Bị chặn hoặc tạm dừng
- `completed/` - Tác vụ đã hoàn thành

## Luồng tác vụ
```
planned → active → completed
    ↓        ↓
   hold ←----┘
    ↓
  active (khi được bỏ chặn)
```

## Thao tác
- Sử dụng `mv` (không bao giờ `cp`) để chuyển trạng thái tác vụ
- Cập nhật front matter khi di chuyển tác vụ
- Luôn cập nhật timestamp với năm hiện tại và thời gian chính xác

## Đặt tên tác vụ
- Định dạng: `TASK-XXX.md` với XXX là số tăng dần
- Sử dụng tiêu đề mô tả trong front matter 