-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema sas
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema sas
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sas` DEFAULT CHARACTER SET utf8mb3 ;
-- -----------------------------------------------------
-- Schema sas
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema sas
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sas` DEFAULT CHARACTER SET utf8mb3 ;
USE `sas` ;

-- -----------------------------------------------------
-- Table `sas`.`otp_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sas`.`otp_info` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(150) NOT NULL,
  `otp` CHAR(6) NOT NULL,
  `generatedAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sas`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sas`.`category` (
  `categoryId` INT NOT NULL AUTO_INCREMENT,
  `categoryName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`categoryId`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `sas`.`subscription`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sas`.`subscription` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(150) NOT NULL,
  `categoryId` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_subscription_category_idx` (`categoryId` ASC) VISIBLE,
  CONSTRAINT `fk_subscription_category`
    FOREIGN KEY (`categoryId`)
    REFERENCES `sas`.`category` (`categoryId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sas`.`unsubscribe_token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sas`.`unsubscribe_token` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(150) NOT NULL,
  `token` VARCHAR(255) NOT NULL,
  `generatedAt` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sas`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sas`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(150) NULL DEFAULT NULL,
  `password` VARCHAR(100) NOT NULL,
  `role` ENUM('admin', 'announcer') NOT NULL DEFAULT 'announcer',
  `createdOn` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedOn` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `sas`.`announcement`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sas`.`announcement` (
  `announcementId` INT NOT NULL AUTO_INCREMENT,
  `announcementTitle` VARCHAR(200) NOT NULL,
  `announcementDescription` VARCHAR(10000) NOT NULL,
  `publishDate` DATETIME NULL DEFAULT NULL,
  `closeDate` DATETIME NULL DEFAULT NULL,
  `announcementDisplay` ENUM('Y', 'N') NOT NULL,
  `categoryId` INT NOT NULL,
  `viewCount` SMALLINT NOT NULL DEFAULT '0',
  `announcementOwner` INT NOT NULL,
  PRIMARY KEY (`announcementId`),
  INDEX `fk_announcement_category_idx` (`categoryId` ASC) VISIBLE,
  INDEX `fk_announcement_user1_idx` (`announcementOwner` ASC) VISIBLE,
  CONSTRAINT `fk_announcement_category`
    FOREIGN KEY (`categoryId`)
    REFERENCES `sas`.`category` (`categoryId`),
  CONSTRAINT `fk_announcement_user1`
    FOREIGN KEY (`announcementOwner`)
    REFERENCES `sas`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `sas`.`file`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sas`.`file` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `originalFileName` VARCHAR(255) NOT NULL,
  `uniqueFileName` VARCHAR(255) NOT NULL,
  `announcementId` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uniqueFileName_UNIQUE` (`uniqueFileName` ASC) VISIBLE,
  INDEX `fk_file_announcement1_idx` (`announcementId` ASC) VISIBLE,
  CONSTRAINT `fk_file_announcement1`
    FOREIGN KEY (`announcementId`)
    REFERENCES `sas`.`announcement` (`announcementId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `sas` ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
