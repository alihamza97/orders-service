CREATE SCHEMA `orders` ;

CREATE TABLE `orders`.`products` (
  `orderID` INT(10) NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `productID` INT(10) NOT NULL,
  PRIMARY KEY (`orderID`));
