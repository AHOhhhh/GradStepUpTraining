DROP TABLE IF EXISTS `vendor`;

CREATE TABLE `vendor` (
  `id`            VARCHAR(36) PRIMARY KEY NOT NULL,
  `name`          VARCHAR(50)             NOT NULL,
  `business_type` VARCHAR(36)             NOT NULL,
  `created_at`    DATETIME                NOT NULL,
  `updated_at`    DATETIME                NOT NULL,
  `created_by`    VARCHAR(36)             NOT NULL,
  `updated_by`    VARCHAR(36)             NOT NULL,
  `deleted`       BOOLEAN                 NOT NULL DEFAULT FALSE,
  `version`       INTEGER                 NOT NULL
)

  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
