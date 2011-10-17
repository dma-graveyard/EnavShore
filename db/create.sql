SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

-- -----------------------------------------------------
-- Table `vessel`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vessel`;

CREATE TABLE `vessel` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `mmsi` INT NOT NULL ,
  `imo_no` INT NULL ,
  `call_sign` VARCHAR(10) NULL ,
  `name` VARCHAR(128) NULL ,
  `updated` DATETIME NULL ,
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `vessel_position`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vessel_position` ;

CREATE TABLE `vessel_position` (
  `vessel` INT NOT NULL PRIMARY KEY,
  `nav_status` INT NULL ,
  `sog` DOUBLE NULL COMMENT 'In knots' ,
  `cog` DOUBLE NULL COMMENT 'In degrees' ,
  `heading` DOUBLE NULL COMMENT 'In degrees' ,
  `rot` DOUBLE NULL ,
  `longitude` DOUBLE NULL ,
  `latitude` DOUBLE NULL ,
  `pos_accuracy` TINYINT NULL ,
  `updated` DATETIME NOT NULL ,
  FOREIGN KEY (vessel) REFERENCES vessel(id) ON DELETE CASCADE
)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `voyage`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `route` ;

CREATE TABLE `route` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `vessel` INT NOT NULL ,
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  FOREIGN KEY (vessel) REFERENCES vessel(id) ON DELETE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `route_waypoint`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `route_waypoint` ;

CREATE TABLE `route_waypoint` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `route` INT NOT NULL ,
  `no` INT NOT NULL COMMENT '	' ,
  `latitude` DOUBLE NOT NULL ,
  `longitude` DOUBLE NOT NULL ,
  `eta` DATETIME NULL ,
  `turn_rad` DOUBLE NULL ,
  `rot` DOUBLE NULL ,
  FOREIGN KEY (route) REFERENCES route(id) ON DELETE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `route_leg`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `route_leg` ;

CREATE TABLE `route_leg` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `route` INT NOT NULL ,
  `start_wp` INT NOT NULL ,
  `end_wp` INT NOT NULL ,
  `sail_heading` ENUM('RL','GC') NOT NULL ,
  `speed` DOUBLE NOT NULL ,
  `stb_space` DOUBLE NULL COMMENT '	' ,
  `port_space` DOUBLE NULL ,
  FOREIGN KEY (route) REFERENCES route(id) ON DELETE CASCADE,
  FOREIGN KEY (start_wp) REFERENCES route_waypoint(id) ON DELETE CASCADE,
  FOREIGN KEY (end_wp) REFERENCES route_waypoint(id) ON DELETE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `service_log`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `service_log`;

CREATE TABLE IF NOT EXISTS `service_log` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `service` VARCHAR(64) NOT NULL,
  `status` ENUM('STARTED','COMPLETED','FAILED') NOT NULL DEFAULT 'STARTED',
  `mmsi` INT NULL,
  `ip` VARCHAR(64) NULL,
  `started` DATETIME NOT NULL,
  `ended` DATETIME NULL,
  `errorcode` INT NULL,
  `errortext` VARCHAR(255) NULL,
  INDEX(service, status))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
