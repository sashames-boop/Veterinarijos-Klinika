-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: veterinarija
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `registration`
--

DROP TABLE IF EXISTS `registration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `registration` (
  `id` int NOT NULL AUTO_INCREMENT,
  `confirmed` bit(1) NOT NULL,
  `date` varchar(255) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  `client_id` int DEFAULT NULL,
  `groomer_id` int DEFAULT NULL,
  `pet_id` int DEFAULT NULL,
  `vet_id` int DEFAULT NULL,
  `cancelled` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6npxo2wewgosl8p7pf0tacnfy` (`client_id`),
  KEY `FKegshq9cm0rlbt3h74his5csvj` (`groomer_id`),
  KEY `FKfvonklj8iubah6nfgw6e2s0ms` (`pet_id`),
  KEY `FKrut23y1ymvhoo4kidg4qsnjg5` (`vet_id`),
  CONSTRAINT `FK6npxo2wewgosl8p7pf0tacnfy` FOREIGN KEY (`client_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKegshq9cm0rlbt3h74his5csvj` FOREIGN KEY (`groomer_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKfvonklj8iubah6nfgw6e2s0ms` FOREIGN KEY (`pet_id`) REFERENCES `pet` (`id`),
  CONSTRAINT `FKrut23y1ymvhoo4kidg4qsnjg5` FOREIGN KEY (`vet_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registration`
--

LOCK TABLES `registration` WRITE;
/*!40000 ALTER TABLE `registration` DISABLE KEYS */;
INSERT INTO `registration` VALUES (2,_binary '\0','2026-11-05','11:11',1,7,1,NULL,_binary '\0');
/*!40000 ALTER TABLE `registration` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-03 21:29:30
