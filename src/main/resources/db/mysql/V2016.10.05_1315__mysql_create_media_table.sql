CREATE TABLE `media` (
 `ID` int(11) NOT NULL AUTO_INCREMENT,
 `PRODUCT_TYPE` varchar(10) NOT NULL,
 `title` varchar(50) NOT NULL,
 `price` double(10,2) NOT NULL,
 `code` varchar(10) NOT NULL,
 `genre` varchar(20) DEFAULT NULL,
 `artist` varchar(50) DEFAULT NULL,
 `directors` varchar(50) DEFAULT NULL,
 `production_label` varchar(50) DEFAULT NULL,
 `author` varchar(50) DEFAULT NULL,
 PRIMARY KEY (`ID`),
 UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;