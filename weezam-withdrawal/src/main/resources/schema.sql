DROP TABLE IF EXISTS `withdrawals`;
CREATE TABLE `withdrawals` (
  `id` bigint(20) IDENTITY NOT NULL PRIMARY KEY,
  `transaction_id` bigint(20) DEFAULT NULL,
  `amount` DOUBLE DEFAULT NULL,
  `created_at` TIMESTAMP DEFAULT NULL,
  `execute_at` TIMESTAMP DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `payment_method_id` bigint(20) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL
);