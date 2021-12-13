
create database mike_test;

CREATE TABLE `employees` (
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

CREATE TABLE `tasks` (
  `taskID` bigint(20) NOT NULL AUTO_INCREMENT,
  `taskName` varchar(100) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `assigner` varchar(20) DEFAULT NULL,
  `assignee` varchar(20) DEFAULT NULL,
  `dateCreated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dateModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `employeeID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`taskID`)
) ENGINE=InnoDB;

INSERT IGNORE INTO `employees` (`employeeID`, `firstName`, `lastName`, `emailAddress`, `active`, `dateCreated`, `dateModified`) VALUES
(NULL, 'Mikehenry', 'Maina', 'mikemacharia39@gmail.com', 1, '2020-12-14 08:31:00', '2020-12-13 21:00:00'),
(NULL, 'Maxwell', 'Mzito', NULL, 1, '2020-12-14 10:58:38', '2020-12-14 10:58:38'),
(NULL, 'Brian', 'Mala', NULL, 1, '2020-12-14 10:58:38', '2020-12-14 10:58:38'),
(NULL, 'Joan', 'Mala', NULL, 1, '2020-12-14 10:58:38', '2020-12-14 10:58:38'),
(NULL, 'Kenn', 'Mala', NULL, 1, '2020-12-14 10:58:38', '2020-12-14 10:58:38'),
(NULL, 'Kevin', 'Mala', NULL, 1, '2020-12-14 10:58:38', '2020-12-14 10:58:38'),
(NULL, 'Alice', 'Mala', NULL, 1, '2020-12-14 10:58:38', '2020-12-14 10:58:38'),
(NULL, 'Grace', 'Mala', NULL, 1, '2020-12-14 10:58:38', '2020-12-14 10:58:38'),
(NULL, 'Jabe', 'Mala', NULL, 1, '2020-12-14 10:58:38', '2020-12-14 10:58:38'),
(NULL, 'June', 'Mala', NULL, 1, '2020-12-14 10:58:38', '2020-12-14 10:58:38'),
(NULL, 'Kanari', 'Mala', NULL, 1, '2020-12-14 10:58:38', '2020-12-14 10:58:38'),
(NULL, 'Katana', 'Omosh', NULL, 1, '2020-12-14 10:58:38', '2020-12-14 10:58:38'),
(NULL, 'Baraka', 'Q', NULL, 1, '2020-12-14 10:58:38', '2020-12-14 10:58:38'),
(NULL, 'Kellen', 'K', NULL, 1, '2020-12-14 10:58:38', '2020-12-14 10:58:38'),
(NULL, 'Nice', 'J', NULL, 1, '2020-12-14 10:58:38', '2020-12-14 10:58:38'),
(NULL, 'Brenda', 'Mala', NULL, 1, '2020-12-14 10:58:38', '2020-12-14 10:58:38'),
(NULL, 'James Wambua', 'Mwanza', NULL, 1, '2020-12-14 14:01:59', '2020-12-14 14:01:59');