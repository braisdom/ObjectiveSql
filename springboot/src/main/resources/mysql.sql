CREATE DATABASE IF NOT EXISTS `objective_sql` CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS `objective_sql`.`members`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `no` VARCHAR(100),
   `name` VARCHAR(100),
   `gender` INT(2),
   `mobile` VARCHAR(11),
   `other_info` VARCHAR(512),
   PRIMARY KEY ( `id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
