DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `max_withdrawal_amount` DOUBLE DEFAULT NULL
);

CREATE TABLE `payment_methods` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `user_id` bigint(20) NOT NULL
);