DROP TABLE IF EXISTS `role`;

CREATE TABLE role (
  `id`          INTEGER PRIMARY KEY                   NOT NULL,
  `name`        VARCHAR(36) UNIQUE                    NOT NULL,
  `description` VARCHAR(100),
  `created_at`  DATETIME                              NOT NULL,
  `updated_at`  DATETIME                              NOT NULL,
  `created_by`  VARCHAR(36)                           NOT NULL,
  `updated_by`  VARCHAR(36)                           NOT NULL,
  `deleted`     BOOLEAN                               NOT NULL DEFAULT FALSE,
  `version`     INTEGER                               NOT NULL
)

  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
