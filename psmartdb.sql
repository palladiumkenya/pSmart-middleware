
CREATE DATABASE `psmart`;

CREATE TABLE `endpoints` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `endpointPurpose` varchar(225) NOT NULL,
  `endpointUrl` varchar(225) DEFAULT NULL,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `createDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `user` varchar(45) DEFAULT NULL,
  `void` bit(1) DEFAULT b'0',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `endpointPurpose_UNIQUE` (`endpointPurpose`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=latin1 COMMENT='EMR ENDPOINT CONFIGRATION';

INSERT INTO `psmart`.`endpoints`
(`endpointPurpose`)
VALUES 
("HTTP POST - Push SHR to EMR"),
("HTTP POST - Push encrypted SHR to EMR"),
("HTTP POST - Push the card assignment details to EMR"),
("HTTP POST - Push Authentication credentials to EMR, get back an auth token"),
("HTTP GET - Fetch SHR from EMR. Takes Card serial as parameter"),
("HTTP GET - Fetch eligible list from EMR"),
("HTTP GET - Fetch inactive cards from Registry");

