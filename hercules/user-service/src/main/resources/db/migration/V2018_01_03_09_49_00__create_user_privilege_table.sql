DROP TABLE IF EXISTS `user_privilege`;

CREATE TABLE `user_privilege` (
  `user_id`      VARCHAR(36) NOT NULL,
  `privilege_id` INTEGER NOT NULL,
  PRIMARY KEY (`user_id`, `privilege_id`),
  CONSTRAINT `fk_user_privilege_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_privilege_privilege` FOREIGN KEY (`privilege_id`) REFERENCES `privilege` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)

  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
