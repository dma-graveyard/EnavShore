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

-- -----------------------------------------------------
-- Table `depth_denmark`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `depth_denmark` ;

CREATE TABLE `depth_denmark` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `lat` DOUBLE NOT NULL ,
  `lon` DOUBLE NOT NULL ,
  `n` INT NOT NULL ,
  `m` INT NOT NULL ,
  `depth` DOUBLE,
  INDEX(n, m, lat, lon))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `tide_denmark`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tide_denmark` ;

CREATE TABLE `tide_denmark` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `lat` DOUBLE NOT NULL ,
  `lon` DOUBLE NOT NULL ,
  `n` INT NOT NULL ,
  `m` INT NOT NULL ,
  `time` DATETIME NOT NULL,
  `depth` DOUBLE,
  INDEX(time, n, m, lat, lon))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `humber_depth`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `humber_depth` ;

CREATE TABLE `humber_depth` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `lat` DOUBLE NOT NULL ,
  `lon` DOUBLE NOT NULL ,
  `n` INT NOT NULL ,
  `m` INT NOT NULL ,
  `depth` DOUBLE,
  INDEX(n, m, lat, lon))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `BLACK10_14_TIDE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `BLACK10_14_TIDE` ;

CREATE TABLE `BLACK10_14_TIDE` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `dateTime` TIMESTAMP NOT NULL ,
  `depth` DOUBLE NOT NULL,
  INDEX(dateTime))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BRUFF10_14_TIDE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `BRUFF10_14_TIDE` ;

CREATE TABLE `BRUFF10_14_TIDE` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `dateTime` TIMESTAMP NOT NULL ,
  `depth` DOUBLE NOT NULL,
  INDEX(dateTime))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BURT10_14_TIDE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `BURT10_14_TIDE` ;

CREATE TABLE `BURT10_14_TIDE` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `dateTime` TIMESTAMP NOT NULL ,
  `depth` DOUBLE NOT NULL,
  INDEX(dateTime))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `GOOLE10_14_TIDE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GOOLE10_14_TIDE` ;

CREATE TABLE `GOOLE10_14_TIDE` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `dateTime` TIMESTAMP NOT NULL ,
  `depth` DOUBLE NOT NULL,
  INDEX(dateTime))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `GRIM10_14_TIDE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `GRIM10_14_TIDE` ;

CREATE TABLE `GRIM10_14_TIDE` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `dateTime` TIMESTAMP NOT NULL ,
  `depth` DOUBLE NOT NULL,
  INDEX(dateTime))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `HST10_14_TIDE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `HST10_14_TIDE` ;

CREATE TABLE `HST10_14_TIDE` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `dateTime` TIMESTAMP NOT NULL ,
  `depth` DOUBLE NOT NULL,
  INDEX(dateTime))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `HULAD10_14_TIDE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `HULAD10_14_TIDE` ;

CREATE TABLE `HULAD10_14_TIDE` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `dateTime` TIMESTAMP NOT NULL ,
  `depth` DOUBLE NOT NULL,
  INDEX(dateTime))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `HUMBE10_14_TIDE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `HUMBE10_14_TIDE` ;

CREATE TABLE `HUMBE10_14_TIDE` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `dateTime` TIMESTAMP NOT NULL ,
  `depth` DOUBLE NOT NULL,
  INDEX(dateTime))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `IMMIN10_14_TIDE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `IMMIN10_14_TIDE` ;

CREATE TABLE `IMMIN10_14_TIDE` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `dateTime` TIMESTAMP NOT NULL ,
  `depth` DOUBLE NOT NULL,
  INDEX(dateTime))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `KDOCK10_14_TIDE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `KDOCK10_14_TIDE` ;

CREATE TABLE `KDOCK10_14_TIDE` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `dateTime` TIMESTAMP NOT NULL ,
  `depth` DOUBLE NOT NULL,
  INDEX(dateTime))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `KEAD10_14_TIDE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `KEAD10_14_TIDE` ;

CREATE TABLE `KEAD10_14_TIDE` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `dateTime` TIMESTAMP NOT NULL ,
  `depth` DOUBLE NOT NULL,
  INDEX(dateTime))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `N_SH10_14_TIDE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `N_SH10_14_TIDE` ;

CREATE TABLE `N_SH10_14_TIDE` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `dateTime` TIMESTAMP NOT NULL ,
  `depth` DOUBLE NOT NULL,
  INDEX(dateTime))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `SPURN10_14_TIDE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `SPURN10_14_TIDE` ;

CREATE TABLE `SPURN10_14_TIDE` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `dateTime` TIMESTAMP NOT NULL ,
  `depth` DOUBLE NOT NULL,
  INDEX(dateTime))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `SUNK10_14_TIDE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `SUNK10_14_TIDE` ;

CREATE TABLE `SUNK10_14_TIDE` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `dateTime` TIMESTAMP NOT NULL ,
  `depth` DOUBLE NOT NULL,
  INDEX(dateTime))
ENGINE = InnoDB;