-- userdrop database sas;

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema SAS
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema SAS
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `SAS` DEFAULT CHARACTER SET utf8 ;
USE `SAS` ;

-- -----------------------------------------------------
-- Table `SAS`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAS`.`category` (
  `categoryId` INT NOT NULL AUTO_INCREMENT,
  `categoryName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`categoryId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `SAS`.`announcement`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SAS`.`announcement` (
  `announcementId` INT NOT NULL AUTO_INCREMENT,
  `announcementTitle` VARCHAR(200) NOT NULL,
  `announcementDescription` VARCHAR(10000) NOT NULL,
  `publishDate` DATETIME NULL,
  `closeDate` DATETIME NULL,
  `announcementDisplay` ENUM('Y', 'N') NOT NULL,
  `categoryId` INT NOT NULL,
  `viewCount` SMALLINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`announcementId`),
  INDEX `fk_announcement_category_idx` (`categoryId` ASC) VISIBLE,
  CONSTRAINT `fk_announcement_category`
    FOREIGN KEY (`categoryId`)
    REFERENCES `SAS`.`category` (`categoryId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `SAS`.`user` (
  `id` INT NOT NULL auto_increment,
  `username` VARCHAR(45) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `email` VARCHAR(150) NULL,
  `password` VARCHAR(100) NOT NULL,
  `role` ENUM('admin', 'announcer') NOT NULL DEFAULT 'announcer',
  `createdOn` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updatedOn` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
