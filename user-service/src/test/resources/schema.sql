CREATE SCHEMA IF NOT EXISTS `utopia`;
USE `utopia`;
CREATE TABLE IF NOT EXISTS `utopia`.`tbl_users` (
  `userId` INT NOT NULL AUTO_INCREMENT,
  `password` VARCHAR(100) NOT NULL,
  `email` VARCHAR(50) NULL DEFAULT NULL,
  `firstName` VARCHAR(45) NOT NULL,
  `lastName` VARCHAR(45) NULL DEFAULT NULL,
  `enabled` TINYINT(1) NOT NULL,
  `locked` TINYINT(1) NOT NULL,
  `userRole` INT NULL DEFAULT NULL,
  PRIMARY KEY (`userId`),
  UNIQUE INDEX `tbl_users_email_uindex` (`email` ASC)
);
