DROP TABLE IF EXISTS `user`;

CREATE TABLE user (
  `id`            VARCHAR(36) PRIMARY KEY      NOT NULL,
  `username`      VARCHAR(50) UNIQUE           NOT NULL,
  `password`      VARCHAR(152)                 NOT NULL,
  `fullname`      VARCHAR(50),
  `email`         VARCHAR(100),
  `cellphone`     VARCHAR(50),
  `telephone`     VARCHAR(50),
  `enterprise_id` VARCHAR(36),
  `resettable`    BOOLEAN,
  `status`        VARCHAR(50),
  `role_id`       INTEGER                      NOT NULL,
  `created_at`    DATETIME                     NOT NULL,
  `updated_at`    DATETIME                     NOT NULL,
  `created_by`    VARCHAR(36)                  NOT NULL,
  `updated_by`    VARCHAR(36)                  NOT NULL,
  `deleted`       BOOLEAN                      NOT NULL DEFAULT FALSE,
  `version`       INTEGER                      NOT NULL,
  CONSTRAINT `fk_user_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `fk_user_enterprise_id` FOREIGN KEY (`enterprise_id`) REFERENCES `enterprise` (`id`)
)

  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;