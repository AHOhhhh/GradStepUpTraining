
CREATE TABLE `pay_channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(36) NOT NULL,
  `created_by` varchar(36) DEFAULT NULL,
  `updated_by` varchar(36) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `pay_channel$index_value` (`value`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4;



CREATE TABLE `currency_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(36) NOT NULL,
  `code` varchar(36) NOT NULL,
  `int_code` int(11) NOT NULL,
  `created_by` varchar(36) DEFAULT NULL,
  `updated_by` varchar(36) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `currency_code$index_code` (`code`),
  KEY `currency_code$index_int_code` (`int_code`)
) ENGINE=InnoDB AUTO_INCREMENT=225 DEFAULT CHARSET=utf8mb4;



CREATE TABLE `org_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(36) NOT NULL,
  `created_by` varchar(36) DEFAULT NULL,
  `updated_by` varchar(36) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `org_code$index_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4;




CREATE TABLE `deferred_pay_trans` (
  `id` varchar(36) NOT NULL,
  `payment_id` varchar(36) NOT NULL,
  `order_amount` decimal(20,4) NOT NULL,
  `pay_amount` decimal(20,4) NOT NULL,
  `pay_cust_id` varchar(36) NOT NULL,
  `pay_cust_name` varchar(36) NOT NULL,
  `currency_code_id` int(11) NOT NULL,
  `pay_status` varchar(36) NOT NULL,
  `pay_repeated` tinyint(1) NOT NULL DEFAULT '0',
  `paid_time` datetime DEFAULT NULL,
  `created_by` varchar(36) NOT NULL,
  `updated_by` varchar(36) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `currency_code_id` (`currency_code_id`),
  KEY `deferred_pay_trans$index_order_id` (`payment_id`),
  KEY `deferred_pay_trans$index_pay_request_repeated` (`pay_repeated`),
  KEY `deferred_pay_trans$index_pay_status` (`pay_status`),
  KEY `deferred_pay_trans$index_updated_at` (`updated_at`),
  KEY `deferred_pay_trans$index_deleted` (`deleted`),
  CONSTRAINT `deferred_pay_trans_ibfk_1` FOREIGN KEY (`currency_code_id`) REFERENCES `currency_code` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



CREATE TABLE `offline_pay_trans` (
  `id` varchar(36) NOT NULL,
  `payment_id` varchar(36) NOT NULL,
  `order_amount` decimal(20,4) NOT NULL,
  `pay_amount` decimal(20,4) NOT NULL,
  `deposit_bank` varchar(50) NOT NULL,
  `collection_account_name` varchar(36) NOT NULL,
  `collection_account_number` varchar(36) NOT NULL,
  `bank_transaction_number` varchar(36) NOT NULL,
  `bank_transaction_time` date DEFAULT NULL,
  `bank_transaction_comment` text,
  `comment` text,
  `pay_cust_id` varchar(36) NOT NULL,
  `pay_cust_name` varchar(36) NOT NULL,
  `currency_code_id` int(11) NOT NULL,
  `pay_status` varchar(36) NOT NULL,
  `paid_time` datetime DEFAULT NULL,
  `created_by` varchar(36) NOT NULL,
  `updated_by` varchar(36) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `offline_pay_trans$currency_code_id` (`currency_code_id`),
  KEY `offline_pay_trans$index_payment_id` (`payment_id`),
  KEY `offline_pay_trans$index_pay_status` (`pay_status`),
  KEY `offline_pay_trans$index_updated_at` (`updated_at`),
  KEY `offline_pay_trans$index_deleted` (`deleted`),
  CONSTRAINT `offline_pay_trans_ibfk_1` FOREIGN KEY (`currency_code_id`) REFERENCES `currency_code` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `payment` (
  `id` varchar(36) NOT NULL,
  `amount` decimal(20,4) NOT NULL,
  `status` int(11) NOT NULL,
  `paid_time` datetime DEFAULT NULL,
  `pay_repeated` tinyint(1) NOT NULL DEFAULT '0',
  `currency` varchar(10) DEFAULT 'CNY',
  `pay_user_id` varchar(36) DEFAULT NULL,
  `pay_channel` varchar(36) NOT NULL,
  `pay_method` varchar(36) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `created_by` varchar(36) NOT NULL,
  `updated_by` varchar(36) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- Table structure for table `payment_request`
--



CREATE TABLE `payment_request` (
  `id` varchar(36) NOT NULL,
  `order_id` varchar(36) NOT NULL,
  `order_type` int(11) NOT NULL,
  `amount` decimal(20,4) NOT NULL,
  `payment_status` int(11) NOT NULL,
  `currency` varchar(10) DEFAULT 'CNY',
  `paid_time` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `created_by` varchar(36) NOT NULL,
  `updated_by` varchar(36) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- Table structure for table `payment_requests`
--



CREATE TABLE `payment_requests` (
  `payment_id` varchar(36) NOT NULL,
  `request_id` varchar(36) NOT NULL,
  PRIMARY KEY (`payment_id`,`request_id`),
  KEY `fk_payment_request_id` (`request_id`),
  CONSTRAINT `fk_payment_id` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`id`),
  CONSTRAINT `fk_payment_request_id` FOREIGN KEY (`request_id`) REFERENCES `payment_request` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- Table structure for table `payment_transaction`
--



CREATE TABLE `payment_transaction` (
  `id` varchar(36) NOT NULL,
  `transaction_id` varchar(36) NOT NULL,
  `payment_id` varchar(36) NOT NULL,
  `order_id` varchar(36) NOT NULL,
  `enterprise_id` varchar(36) NOT NULL,
  `order_type` int(11) NOT NULL,
  `vendor` varchar(36) NOT NULL,
  `transaction_type` int(11) NOT NULL,
  `pay_method` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `paid_time` datetime DEFAULT NULL,
  `amount` decimal(20,4) NOT NULL,
  `currency` varchar(10) DEFAULT 'CNY',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `created_by` varchar(36) NOT NULL,
  `updated_by` varchar(36) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL,
  `comment` text,
  PRIMARY KEY (`id`),
  KEY `fk_payment_transaction_payment` (`payment_id`),
  KEY `payment_transaction$transaction_id` (`transaction_id`),
  CONSTRAINT `fk_payment_transaction_payment` FOREIGN KEY (`payment_id`) REFERENCES `payment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



CREATE TABLE `refund_trans` (
  `id` varchar(36) NOT NULL,
  `payment_id` varchar(36) NOT NULL,
  `pay_amount` decimal(20,4) NOT NULL,
  `pay_cust_id` varchar(36) NOT NULL,
  `pay_cust_name` varchar(36) NOT NULL,
  `currency_code_id` int(11) NOT NULL,
  `pay_status` varchar(36) NOT NULL,
  `paid_time` datetime DEFAULT NULL,
  `comment` text,
  `created_by` varchar(36) NOT NULL,
  `updated_by` varchar(36) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `refund_trans$currency_code_id` (`currency_code_id`),
  KEY `refund_trans$index_payment_id` (`payment_id`),
  KEY `refund_trans$index_pay_status` (`pay_status`),
  KEY `refund_trans$index_updated_at` (`updated_at`),
  KEY `refund_trans$index_deleted` (`deleted`),
  CONSTRAINT `refund_trans_ibfk_1` FOREIGN KEY (`currency_code_id`) REFERENCES `currency_code` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
