# Bài làm CinemaBox – Database + Backend

Họ tên:
Mã sinh viên:
Lớp:

## Pha 1 (không dùng AI)

Tự đánh dấu phần đã xong trước mốc 1 (chỉ để GV tham khảo, không thay cho kết quả chấm):

- [ ] I1  - [ ] I2  - [ ] I3  - [ ] Endpoint GET /api/showtimes/{id}
- [ ] F1  - [ ] F2  - [ ] F3  - [ ] API khách hàng (tạo, xem, danh sách, validate, lỗi)

Ghi chú (nếu có):

## Pha 2 (được dùng AI)

### Cách bạn chống bán vượt số ghế khi nhiều người đặt cùng lúc

Dùng pessimistic write lock trên bản ghi suất chiếu trong `showtimeRepository.lockById(...)` và bao toàn quá trình kiểm tra ghế, thanh toán, trừ ghế, lưu vé bằng một transaction. Khóa này khiến các request cùng suất chiếu xử lý tuần tự, nên không có hai request cùng đọc cùng số ghế và cùng trừ vượt. Dữ liệu được kiểm chứng bằng test rollback và logic kiểm tra `availableSeats < quantity`; schema còn có `CHECK (available_seats >= 0)`.

### Cách bạn hoàn tác khi thanh toán lỗi

Thanh toán được gọi trong transaction trước khi lưu booking và trước khi trừ ghế. `PaymentFailedException` được xử lý thành 402 PAYMENT_FAILED; Spring transaction rollback nên không tạo vé CONFIRMED và không thay đổi ghế. Test `BookingRollbackTest.paymentRejectedRollsBackSeatsAndConfirmedBooking` so sánh số ghế và số vé CONFIRMED trước/sau request bị từ chối.

### Test bạn đã viết (tên lớp, tên test, mỗi test kiểm tra điều gì)

- `TuKiemTraTest.apiMauTraVePhim`: kiểm chứng môi trường và API mẫu GET phim.
- `TuKiemTraTest.loiKhongTimThayDungDinhDangChung`: kiểm chứng lỗi 404 có định dạng chung.
- `BookingRollbackTest.paymentRejectedRollsBackSeatsAndConfirmedBooking`: giả lập thanh toán bị từ chối khi tổng tiền > 1.000.000 đồng, kiểm tra 402 và xác nhận ghế cùng số vé CONFIRMED không đổi.

### Nhật ký dùng AI

| # | Bạn hỏi AI điều gì | AI gợi ý gì | Bạn giữ / sửa / bỏ, vì sao |
|---|---|---|---|
| 1 | Cách chống oversell trong đặt vé đồng thời | Pessimistic lock trên suất chiếu hoặc check ghế trước khi thanh toán | Giữ pessimistic lock vì nó tuần tự hóa request và kiểm tra ghế sau khi khóa; kiểm chứng bằng cấu trúc transaction và test API. |
| 2 | Cách xử lý thanh toán bị từ chối | Gọi thanh toán trong transaction, để exception rollback booking và ghế | Giữ cách này; ghế chưa trừ và booking chưa lưu trước thanh toán, test xác nhận dữ liệu không đổi. |
| 3 | Cách trả lỗi đồng thời đúng mã | Thêm exception cụ thể cho 402, 409 và validation 400 | Giữ riêng PAYMENT_FAILED và các mã 409, loại bỏ fallback 500 cho input; DTO JSON và handler đảm nhận 400. |
