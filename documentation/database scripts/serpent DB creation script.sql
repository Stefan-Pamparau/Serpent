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
CREATE SCHEMA IF NOT EXISTS `serpentDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `serpentDB` ;

-- -----------------------------------------------------
-- Table `serpentDB`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `serpentDB`.`user` (
  `ID` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `firstname` VARCHAR(45) NULL COMMENT '',
  `surname` VARCHAR(45) NULL COMMENT '',
  `address` VARCHAR(45) NULL COMMENT '',
  `email` VARCHAR(45) NULL COMMENT '',
  `phone` VARCHAR(45) NULL COMMENT '',
  `sex` VARCHAR(45) NULL COMMENT '',
  `age` INT NULL COMMENT '',
  PRIMARY KEY (`ID`)  COMMENT '')
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
