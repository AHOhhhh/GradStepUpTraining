DROP TABLE IF EXISTS `pay_method`;

CREATE TABLE pay_method (
  `id`            VARCHAR(36) PRIMARY KEY               NOT NULL,
  `name`          VARCHAR(36)                           NOT NULL,
  `enterprise_id` VARCHAR(36)                           NOT NULL,
  `created_at`    DATETIME                              NOT NULL,
  `updated_at`    DATETIME                              NOT NULL,
  `created_by`    VARCHAR(36)                           NOT NULL,
  `updated_by`    VARCHAR(36)                           NOT NULL,
  `deleted`       BOOLEAN                               NOT NULL DEFAULT FALSE,
  `version`       INTEGER                               NOT NULL,

  CONSTRAINT pay_method_enterprise_id_fk FOREIGN KEY (enterprise_id) REFERENCES enterprise (id)
)

  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;