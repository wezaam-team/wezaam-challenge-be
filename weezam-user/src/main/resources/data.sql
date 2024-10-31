INSERT INTO users (id, first_name, max_withdrawal_amount) VALUES (1, 'John', 1000);
INSERT INTO users (id, first_name, max_withdrawal_amount) VALUES (2, 'Mike', 1000);
INSERT INTO users (id, first_name, max_withdrawal_amount) VALUES (3, 'Alex', 1000);
INSERT INTO users (id, first_name, max_withdrawal_amount) VALUES (4, 'Tom', 1000);

INSERT INTO payment_methods (id, user_id, name) VALUES (1, 1, 'My bank account');
INSERT INTO payment_methods (id, user_id, name) VALUES (2, 1, 'My mom account');
INSERT INTO payment_methods (id, user_id, name) VALUES (3, 2, 'Work account');
INSERT INTO payment_methods (id, user_id, name) VALUES (4, 3, 'My bank account');
INSERT INTO payment_methods (id, user_id, name) VALUES (5, 3, 'Secret account');
INSERT INTO payment_methods (id, user_id, name) VALUES (6, 4, 'My bank account');
INSERT INTO payment_methods (id, user_id, name) VALUES (7, 4, 'Secret account');