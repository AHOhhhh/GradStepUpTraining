create table `transport_plan` (
`id` bigint(20) NOT NULL AUTO_INCREMENT primary KEY,
`order_id` varchar(36),
`scheduled_pickup_time` datetime,
`pickup_telephone` varchar(100),
`scheduled_flight` varchar(100),
`scheduled_take_off_time` datetime,
`scheduled_landing_time` datetime,
`expected_delivery_time` datetime,
`delivery_telephone` varchar(100)
)DEFAULT CHARSET=utf8mb4;