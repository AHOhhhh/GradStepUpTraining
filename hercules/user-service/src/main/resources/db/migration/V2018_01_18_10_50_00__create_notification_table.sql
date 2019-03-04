DROP TABLE IF EXISTS notification;

CREATE TABLE notification (
  `id`                BIGINT(20) UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
  `enterprise_id`     VARCHAR(36)                                    NOT NULL,
  `notification_type` VARCHAR(36)                                    NOT NULL,
  `order_id`          VARCHAR(36)                                    NOT NULL,
  `name`              VARCHAR(36)                                    NOT NULL,
  `order_type`        VARCHAR(36),
  `order_info`        VARCHAR(100),
  `status`            VARCHAR(36),
  `description`       VARCHAR(300)                                   NOT NULL,
  `service_types`     VARCHAR(300),
  `created_at`        DATETIME                                       NOT NULL,
  `updated_at`        DATETIME                                       NOT NULL,
  `created_by`        VARCHAR(36)                                    NOT NULL,
  `updated_by`        VARCHAR(36)                                    NOT NULL,
  `deleted`           BOOLEAN                                        NOT NULL DEFAULT FALSE,
  `version`           INTEGER                                        NOT NULL
)

  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
