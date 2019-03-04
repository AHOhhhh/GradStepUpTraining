-- user
CREATE INDEX `user$deleted` ON `user` (deleted);

-- enterprise
CREATE INDEX `enterprise$name` ON `enterprise` (name);
CREATE INDEX `enterprise$deleted` ON `enterprise` (deleted);

-- notification
CREATE INDEX `notification$enterprise_id` ON `notification` (enterprise_id);
CREATE INDEX `notification$notification_type` ON `notification` (notification_type);


-- contact
CREATE INDEX `contact$cellphone` ON `contact` (cellphone);
CREATE INDEX `contact$is_default` ON `contact` (is_default);
CREATE INDEX `contact$deleted` ON `contact` (deleted);
