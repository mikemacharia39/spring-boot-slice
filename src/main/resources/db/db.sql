
create database mike_test;

Create Table: CREATE TABLE `employees` (
    `employeeID` bigint(20) NOT NULL AUTO_INCREMENT,
    `firstName` varchar(20) NOT NULL,
    `lastName` varchar(20) NOT NULL,
    `emailAddress` varchar(150) DEFAULT NULL,
    `active` tinyint(3) NOT NULL DEFAULT '1',
    `dateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `dateModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `changeDetails` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`employeeID`)
    ) ENGINE=InnoDB;