DROP TABLE IF EXISTS `contact`;

CREATE TABLE contact (
  `id`            VARCHAR(36) PRIMARY KEY        NOT NULL,
  `enterprise_id` VARCHAR(36)                    NOT NULL,
  `name`          VARCHAR(50)                    NOT NULL,
  `cellphone`     VARCHAR(50),
  `address`       VARCHAR(200),
  `telephone`     VARCHAR(50),
  `is_default`    BOOLEAN                                 DEFAULT FALSE,
  `country`       VARCHAR(100)                   NOT NULL,
  `country_abbr`  VARCHAR(20)                    NOT NULL,
  `province`      VARCHAR(100),
  `province_id`   VARCHAR(20),
  `city`          VARCHAR(100),
  `city_id`       VARCHAR(20),
  `district`      VARCHAR(100),
  `district_id`   VARCHAR(20),
  `postcode`      VARCHAR(50),
  `email`         VARCHAR(200),
  `created_at`    DATETIME                       NOT NULL,
  `updated_at`    DATETIME                       NOT NULL,
  `created_by`    VARCHAR(36)                    NOT NULL,
  `updated_by`    VARCHAR(36)                    NOT NULL,
  `deleted`       BOOLEAN                        NOT NULL DEFAULT FALSE,
  `version`       INTEGER                        NOT NULL,
  CONSTRAINT `fk_enterprise_id` FOREIGN KEY (`enterprise_id`) REFERENCES `enterprise` (`id`)
)

  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;