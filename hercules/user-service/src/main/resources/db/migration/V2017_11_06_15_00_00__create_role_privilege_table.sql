DROP TABLE IF EXISTS `role_privilege`;

CREATE TABLE `role_privilege` (
  `role_id`      INTEGER NOT NULL,
  `privilege_id` INTEGER NOT NULL,
  PRIMARY KEY (`role_id`, `privilege_id`),
  CONSTRAINT `fk_roleprivilege_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_roleprivilege_privilege` FOREIGN KEY (`privilege_id`) REFERENCES `privilege` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)

  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;