CREATE TABLE `order_logistic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
`logistics_status_info` varchar(255) NOT NULL,
`update_info_time` datetime NOT NULL,
`update_info_user_name` varchar(36) NOT NULL,
`order_id` varchar(36) NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;