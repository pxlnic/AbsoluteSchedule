SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `addressId` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(50) NOT NULL,
  `address2` varchar(50) DEFAULT NULL,
  `cityId` int(10) NOT NULL,
  `postalCode` varchar(10) DEFAULT NULL,
  `phone` varchar(20) NOT NULL,
  `createdBy` varchar(40) NOT NULL,
  `createDate` datetime NOT NULL,
  `lastUpdate` timestamp NULL DEFAULT NULL,
  `lastUpdateBy` varchar(50) NOT NULL,
  PRIMARY KEY (`addressId`),
  KEY `cityId` (`cityId`),
  CONSTRAINT `address_ibfk_1` FOREIGN KEY (`cityId`) REFERENCES `city` (`cityId`) ON DELETE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `appointment`;
CREATE TABLE `appointment` (
  `appointmentId` int(10) NOT NULL AUTO_INCREMENT,
  `customerId` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` text,
  `location` text,
  `contact` text,
  `url` text,
  `start` datetime DEFAULT NULL,
  `end` datetime DEFAULT NULL,
  `createDate` datetime NOT NULL,
  `createdBy` varchar(40) NOT NULL,
  `lastUpdate` timestamp NULL DEFAULT NULL,
  `lastUpdateBy` varchar(40) NOT NULL,
  PRIMARY KEY (`appointmentId`),
  KEY `customerId` (`customerId`),
  CONSTRAINT `appointment_ibfk_1` FOREIGN KEY (`customerId`) REFERENCES `customer` (`customerId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
  `cityId` int(10) NOT NULL AUTO_INCREMENT,
  `city` varchar(50) NOT NULL,
  `countryId` int(11) NOT NULL,
  `createDate` datetime NOT NULL,
  `createdBy` varchar(40) NOT NULL,
  `lastUpdate` timestamp NULL DEFAULT NULL,
  `lastUpdateBy` varchar(40) NOT NULL,
  PRIMARY KEY (`cityId`),
  KEY `countryId` (`countryId`),
  CONSTRAINT `city_ibfk_1` FOREIGN KEY (`countryId`) REFERENCES `country` (`countryId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `country`;
CREATE TABLE `country` (
  `countryId` int(11) NOT NULL AUTO_INCREMENT,
  `country` varchar(50) NOT NULL,
  `createdBy` varchar(40) NOT NULL,
  `createDate` datetime NOT NULL,
  `lastUpdate` timestamp NULL DEFAULT NULL,
  `lastUpdateBy` varchar(50) NOT NULL,
  PRIMARY KEY (`countryId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `customerId` int(11) NOT NULL AUTO_INCREMENT,
  `customerName` varchar(255) NOT NULL,
  `addressId` int(11) NOT NULL,
  `active` tinyint(1) NOT NULL,
  `createDate` datetime NOT NULL,
  `createdBy` varchar(40) NOT NULL,
  `lastUpdate` timestamp NULL DEFAULT NULL,
  `lastUpdateBy` varchar(40) NOT NULL,
  PRIMARY KEY (`customerId`),
  KEY `addressId` (`addressId`),
  CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`addressId`) REFERENCES `address` (`addressId`) ON DELETE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `incrementtypes`;
CREATE TABLE `incrementtypes` (
  `incrementTypeId` int(11) NOT NULL,
  `incrementTypeDescription` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `reminder`;
CREATE TABLE `reminder` (
  `reminderId` int(10) NOT NULL,
  `reminderDate` datetime NOT NULL,
  `snoozeIncrement` int(11) NOT NULL,
  `snoozeIncrementTypeId` int(11) NOT NULL,
  `appointmentId` int(10) NOT NULL,
  `createdBy` varchar(45) DEFAULT NULL,
  `createdDate` datetime DEFAULT NULL,
  `remindercol` varchar(45) DEFAULT NULL,
  KEY `appointmentId` (`appointmentId`),
  CONSTRAINT `reminder_ibfk_1` FOREIGN KEY (`appointmentId`) REFERENCES `appointment` (`appointmentId`) ON DELETE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `active` tinyint(4) NOT NULL,
  `createBy` varchar(40) NOT NULL,
  `createDate` datetime NOT NULL,
  `lastUpdate` timestamp NULL DEFAULT NULL,
  `lastUpdatedBy` varchar(50) NOT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;