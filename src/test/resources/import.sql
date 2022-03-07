INSERT INTO employees (employee_id, created_date, email, total_balance, currency) VALUES ('XY123456', '2022-01-31', 'xy123456@gmail.com', '2555.95','EUR');
INSERT INTO employees (employee_id, created_date, email, total_balance, currency) VALUES ('MA987654', '2020-01-31', 'ma987654@gmail.com', '1009.66', 'EUR');

INSERT INTO payment_methods (employee_id, account_number, alias) VALUES ('XY123456', 'ES8353358391774291346121','BBVA');
INSERT INTO payment_methods (employee_id, account_number, alias) VALUES ('XY123456', 'ES6416402088669009013173','SANTANDER');
INSERT INTO payment_methods (employee_id, account_number, alias) VALUES ('XY123456', '0x9961715FF0Eb43B47bE76E3774f17bbb5740d5fb', 'Ethereum');

INSERT INTO payment_methods (employee_id, account_number, alias) VALUES ('MA987654', 'ES8353358391774291346109','BBVA-1');


INSERT INTO withdraws (employee_id, execution_date, amount, currency, status, payment_method) VALUES ('XY123456',  '2020-01-31', '1009.66', 'EUR','NOTIFIED', 'BBVA');


INSERT INTO withdraws (employee_id, execution_date, amount, currency, status, payment_method) VALUES ('MA987654',  '2020-01-31', '1009.66', 'EUR','CREATED', 'BBVA');
