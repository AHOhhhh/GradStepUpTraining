DROP TABLE IF EXISTS `operation_log`;

CREATE TABLE operation_log (
  `id`               VARCHAR(36) PRIMARY KEY               NOT NULL,
  `enterprise_id`    VARCHAR(36)                           NOT NULL,
  `enterprise_name`  VARCHAR(200)                          NULL,
  `operator_id`      VARCHAR(36)                           NOT NULL,
  `operator_role`    VARCHAR(100)                          NOT NULL,
  `type`             VARCHAR(36)                           NOT NULL,
  `target_user_id`   VARCHAR(36)                           NULL,
  `target_user_name` VARCHAR(50)                           NULL,
  `created_at`       DATETIME                              NOT NULL,
  `updated_at`       DATETIME                              NOT NULL,
  `created_by`       VARCHAR(36)                           NOT NULL,
  `updated_by`       VARCHAR(36)                           NOT NULL,
  `deleted`          BOOLEAN                               NOT NULL DEFAULT FALSE,
  `version`          INTEGER                               NOT NULL,

  CONSTRAINT operation_log_enterprise_id_fk FOREIGN KEY (enterprise_id) REFERENCES enterprise (id)
)

  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;