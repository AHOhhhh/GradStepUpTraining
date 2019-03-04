DROP TABLE IF EXISTS enterprise_qualification_history;
CREATE TABLE enterprise_qualification_history (
  `id`                                         VARCHAR(36)        NOT NULL,
  `enterprise_id`                              VARCHAR(36)        NOT NULL,
  `name`                                       VARCHAR(200)       NOT NULL,
  `uniform_social_credit_code`                 VARCHAR(88)        NOT NULL,
  `certificate_for_uniform_social_credit_code` VARCHAR(200)       NOT NULL,
  `business_license_number`                    VARCHAR(100),
  `certificate_for_business_license`           VARCHAR(200),
  `tax_payer_number`                           VARCHAR(100),
  `certificate_for_tax_registration`           VARCHAR(200),
  `organization_code`                          VARCHAR(100),
  `certificate_for_organization`               VARCHAR(200),
  `registration_address`                       VARCHAR(200),
  `artificial_person_name`                     VARCHAR(100)       NOT NULL,
  `artificial_person_contact`                  VARCHAR(100)       NOT NULL,
  `validation_status`                          VARCHAR(50)        NOT NULL,
  `comment`                                    VARCHAR(100),
  `status`                                     VARCHAR(50)        NOT NULL,
  `created_at`                                 DATETIME           NOT NULL,
  `updated_at`                                 DATETIME           NOT NULL,
  `created_by`                                 VARCHAR(36)        NOT NULL,
  `updated_by`                                 VARCHAR(36)        NOT NULL,
  `deleted`                                    BOOLEAN            NOT NULL DEFAULT FALSE,
  `version`                                    INTEGER            NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_enterprise_history_id` FOREIGN KEY (`enterprise_id`) REFERENCES `enterprise` (`id`)
)

  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
