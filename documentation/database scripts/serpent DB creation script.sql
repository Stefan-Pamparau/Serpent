-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema serpentDB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema serpentDB
-- -----------------------------------------------------
drop database if exists serpentDB;

CREATE DATABASE IF NOT EXISTS serpentDB;
use serpentDB;

-- -----------------------------------------------------
-- Table `serpentDB`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `serpentDB`.`user` (
  `ID` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `firstname` VARCHAR(45) NULL COMMENT '',
  `surname` VARCHAR(45) NULL COMMENT '',
  `address` VARCHAR(45) NULL COMMENT '',
  `email` VARCHAR(45) NULL COMMENT '',
  `password` VARCHAR(45) NULL COMMENT '',
  `phone` VARCHAR(45) NULL COMMENT '',
  `sex` VARCHAR(45) NULL COMMENT '',
  `age` INT NULL COMMENT '',
  PRIMARY KEY (`ID`)  COMMENT '')
  ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO user(firstname, surname, address, email, password, phone, sex, age)
VALUES
  ('Ioana', 'Popescu', 'Suceava', 'ioana.popescu@gmail.com', '12345', '12345','F','18'),
  ('David', 'Marian', 'Cluj', 'david.marian@gmail.com', '12345', '12345','M','18'),
  ('Cosmin', 'Telescu', 'Bucuresti', 'cosmin.telescu@gmail.com', '12345', '12345','M','18'),
  ('Mihai', 'Radu', 'Timisoare', 'mihai.radu@gmail.com', '12345', '12345','M', '18'),
  ('Tudor', 'Popa', 'Bacau', 'tudor.popa@gmail.com', '12345', '12345','M', '18'),
  ('Test', 'Client', 'Unknown', 'client@gmail.com', '12345','12345', 'M','18');

DROP USER 'admin'@localhost;
CREATE USER 'admin'@'localhost';
GRANT ALL PRIVILEGES ON serpentDB.* TO 'admin'@'localhost' WITH GRANT OPTION;

DROP USER 'user'@localhost;
CREATE USER 'user'@'localhost';
GRANT SELECT ON serpentDB.* TO 'client'@'localhost';
