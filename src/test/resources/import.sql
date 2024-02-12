INSERT INTO users VALUES (1, 'User 1');
INSERT INTO users VALUES (2, 'User 2');
INSERT INTO users VALUES (3, 'User 3');

INSERT INTO payment_methods (id, name, user_id, available_amount) VALUES (1, 'Payment 1-1', 1, 10);
INSERT INTO payment_methods (id, name, user_id, available_amount) VALUES (2, 'Payment 2-1', 1, 20);
INSERT INTO payment_methods (id, name, user_id, available_amount) VALUES (3, 'Payment 1-2', 2, 30);
INSERT INTO payment_methods (id, name, user_id, available_amount) VALUES (4, 'Payment 1-3', 3, 40);