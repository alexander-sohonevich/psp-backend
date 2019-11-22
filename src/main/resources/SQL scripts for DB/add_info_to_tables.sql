 INSERT INTO auto 
 VALUES ('Chevrolet', 'Traverse', 2011, 18965 ,'1GNKVGED6BJ388628'), 
 ('AUDI', 'TT 2.0T', 2008, 32476 ,'TRUAF38J881006447');
 
 INSERT INTO body
 VALUES ('1GNKVGED6BJ388628', 'Фургон', 'Черный', 4),
 ('TRUAF38J881006447', 'Купе', 'Серый', 3);
 
 INSERT INTO car_status
 VALUES ('1GNKVGED6BJ388628', 'В наличии'), 
 ('TRUAF38J881006447', 'Под заказ');
 
 INSERT INTO systems
 VALUES ('1GNKVGED6BJ388628', 'Кожзам', 'Красный', 'Обычное радио'), 
 ('TRUAF38J881006447', 'Кожаный', 'Черный', 'Мультимедийная система');
 
 INSERT INTO transmission
 values ('1GNKVGED6BJ388628', '4WD', 18),
 ('TRUAF38J881006447', 'RWD', 15);

SELECT * from auto;

SELECT a.BRAND, a.MODEL, a.YEAR_OF_ISSUE, a.COST, a.VIN_CODE,
	   b.BODY_TYPE, b.BODY_COLOR, b.NUMBER_OF_DOORS,
       c.CAR_STATUS,
       s.SALON, s.SALON_COLOR, s.MUTLIMEDIA_SYSTEM,
       t.TRANSMISSION, t.WHEEL_DIAMETER 
FROM auto a
JOIN body b ON a.VIN_CODE=b.VIN_CODE 
JOIN car_status c ON a.VIN_CODE=c.VIN_CODE 
JOIN systems s ON a.VIN_CODE=s.VIN_CODE 
JOIN transmission t ON a.VIN_CODE=t.VIN_CODE;
