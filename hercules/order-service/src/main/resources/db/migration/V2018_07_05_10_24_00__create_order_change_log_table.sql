


CREATE TABLE `operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(36) NOT NULL,
  `status` int(11) NOT NULL,
  `operator` varchar(36) NOT NULL,
  `operation` int(11) NOT NULL,
  `operator_role` varchar(36) DEFAULT NULL,
  `vendor` varchar(36) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `created_by` varchar(36) NOT NULL,
  `updated_by` varchar(36) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `operation_log$order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;


--
-- Table structure for table `operation_type`
--



CREATE TABLE `operation_type` (
  `id` int(11) NOT NULL,
  `name` varchar(36) NOT NULL UNIQUE ,
  `description` varchar(100) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `created_by` varchar(36) NOT NULL,
  `updated_by` varchar(36) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `order_bill` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` varchar(36) NOT NULL,
  `order_type` varchar(36) NOT NULL,
  `vendor` varchar(36) NOT NULL,
  `pay_channel` varchar(36) DEFAULT NULL,
  `pay_method` varchar(36) DEFAULT NULL,
  `product_charge` decimal(20,4) NOT NULL,
  `service_charge` decimal(20,4) NOT NULL,
  `commission_charge` decimal(20,4) NOT NULL,
  `currency` varchar(10) DEFAULT 'CNY',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `created_by` varchar(36) NOT NULL,
  `updated_by` varchar(36) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL,
  `product_charge_settled` tinyint(1) DEFAULT NULL,
  `service_charge_settled` tinyint(1) DEFAULT NULL,
  `commission_charge_settled` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

