DROP TABLE IF EXISTS `objective_sql`.`members`;
DROP TABLE IF EXISTS `objective_sql`.`orders`;
DROP TABLE IF EXISTS `objective_sql`.`order_lines`;

CREATE TABLE `objective_sql`.`members`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `no` VARCHAR(100),
   `name` VARCHAR(100),
   `gender` INT(2),
   `mobile` VARCHAR(11),
   `extended_attributes` VARCHAR(512),
   `registered_at` DATETIME,
   `updated_at` DATETIME,
   PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `objective_sql`.`orders`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `no` VARCHAR(100),
   `member_id` VARCHAR(100),
   `amount` FLOAT,
   `quantity` FLOAT,
   `sales_at` DATETIME,
   PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `objective_sql`.`order_lines`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `order_id` INT(10),
   `order_no` VARCHAR(100),
   `barcode` VARCHAR(100),
   `product_id` INT(10),
   `member_id` VARCHAR(100),
   `sales_price` FLOAT,
   `amount` FLOAT,
   `quantity` FLOAT,
   PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

