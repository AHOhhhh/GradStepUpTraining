CREATE TABLE `order_status` (
  `id`          int(11)     NOT NULL PRIMARY KEY,
  `name`        varchar(36) NOT NULL UNIQUE,
  `description` varchar(100)         DEFAULT NULL,
  `created_at`  datetime    NOT NULL,
  `updated_at`  datetime    NOT NULL,
  `created_by`  varchar(36) NOT NULL,
  `updated_by`  varchar(36) NOT NULL,
  `deleted`     tinyint(1)  NOT NULL DEFAULT '0',
  `version`     int(11)     NOT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `order_type` (
  `id`          int(11)     NOT NULL PRIMARY KEY ,
  `name`        varchar(36) NOT NULL UNIQUE ,
  `description` varchar(100)         DEFAULT NULL,
  `created_at`  datetime    NOT NULL,
  `updated_at`  datetime    NOT NULL,
  `created_by`  varchar(36) NOT NULL,
  `updated_by`  varchar(36) NOT NULL,
  `deleted`     tinyint(1)  NOT NULL DEFAULT '0',
  `version`     int(11)     NOT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE `cancel_reason` (
  `id`          int(11)     NOT NULL PRIMARY KEY ,
  `name`        varchar(36) NOT NULL UNIQUE ,
  `description` varchar(100)         DEFAULT NULL,
  `created_at`  datetime    NOT NULL,
  `updated_at`  datetime    NOT NULL,
  `created_by`  varchar(36) NOT NULL,
  `updated_by`  varchar(36) NOT NULL,
  `deleted`     tinyint(1)  NOT NULL DEFAULT '0',
  `version`     int(11)     NOT NULL
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

