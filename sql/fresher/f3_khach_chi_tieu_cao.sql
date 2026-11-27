-- F3. Khách chi tiêu cao (xem đề). Viết MỘT câu SELECT bên dưới, kết thúc bằng dấu ;

SELECT c.full_name,
       c.email,
       COALESCE(SUM(b.id), 0)           AS so_lan_dat,
       COALESCE(SUM(b.total_amount), 0) AS tong_chi_tieu
FROM customers c
         LEFT JOIN bookings b ON c.id = b.customer_id
WHERE b.status = 'CONFIRMED'
GROUP BY c.id, c.full_name, c.email
HAVING COALESCE(SUM(b.total_amount), 0) >= 500000
ORDER BY tong_chi_tieu DESC, c.email ASC;
