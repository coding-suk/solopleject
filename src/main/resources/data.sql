INSERT INTO coupon (name, discount_amount, min_order_amount, is_percent, type, created_at, expired_at)
SELECT * FROM (SELECT '회원가입 축하 쿠폰', 1000, 5000, false, 'SIGNUP', NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY)) AS tmp
WHERE NOT EXISTS (
    SELECT name FROM coupon WHERE name = '회원가입 축하 쿠폰'
);
