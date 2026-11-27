-- I2. Suất chiếu ngày 05/12/2026 còn từ 20 ghế (xem đề). Viết MỘT câu SELECT bên dưới, kết thúc bằng dấu ;
SELECT m.title , r.name, s.start_time , s.available_seats
FROM movies m
JOIN showtimes s ON m.id = s.movie_id
JOIN rooms r ON s.room_id = r.id
WHERE s.start_time >= '2026-12-05 00:00:00'
  AND s.start_time < '2026-12-06 00:00:00'
  AND s.available_seats >= 20;
