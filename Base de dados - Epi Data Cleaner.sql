CREATE DATABASE  IF NOT EXISTS `epi_data_cleaner` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `epi_data_cleaner`;
-- MySQL dump 10.13  Distrib 8.0.12, for Win64 (x86_64)
--
-- Host: localhost    Database: epi_data_cleaner
-- ------------------------------------------------------
-- Server version	8.0.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `acao`
--

DROP TABLE IF EXISTS `acao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `acao` (
  `acaid` bigint(20) NOT NULL AUTO_INCREMENT,
  `acanovovalor` varchar(255) NOT NULL,
  `acaordem` smallint(6) NOT NULL,
  `acavalor` varchar(255) DEFAULT NULL,
  `campo_camid` bigint(20) NOT NULL,
  `operacao_opeid` bigint(20) NOT NULL,
  PRIMARY KEY (`acaid`),
  KEY `FK_gdp52jv3qnmnp9uwbtqjbhnqc` (`operacao_opeid`),
  KEY `FK_ej6qe3hre2v7bht7g7wtpd0b0` (`campo_camid`),
  CONSTRAINT `FK_ej6qe3hre2v7bht7g7wtpd0b0` FOREIGN KEY (`campo_camid`) REFERENCES `campo` (`camid`),
  CONSTRAINT `FK_gdp52jv3qnmnp9uwbtqjbhnqc` FOREIGN KEY (`operacao_opeid`) REFERENCES `operacao` (`opeid`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acao`
--

LOCK TABLES `acao` WRITE;
/*!40000 ALTER TABLE `acao` DISABLE KEYS */;
/*!40000 ALTER TABLE `acao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `arquivo`
--

DROP TABLE IF EXISTS `arquivo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `arquivo` (
  `arqid` bigint(20) NOT NULL AUTO_INCREMENT,
  `arqcabecalho` int(11) NOT NULL,
  `arqcaminho` varchar(512) NOT NULL,
  `arqdelimitador` varchar(50) NOT NULL,
  `arqnome` varchar(150) NOT NULL,
  PRIMARY KEY (`arqid`),
  UNIQUE KEY `uk_arquivo` (`arqcaminho`,`arqnome`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `arquivo`
--

LOCK TABLES `arquivo` WRITE;
/*!40000 ALTER TABLE `arquivo` DISABLE KEYS */;
/*!40000 ALTER TABLE `arquivo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campo`
--

DROP TABLE IF EXISTS `campo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `campo` (
  `camid` bigint(20) NOT NULL AUTO_INCREMENT,
  `camnome` varchar(255) NOT NULL,
  `camtipodado` int(11) DEFAULT NULL,
  `arquivo_arqid` bigint(20) NOT NULL,
  PRIMARY KEY (`camid`),
  KEY `FK_dul0bjs5rg4ihkgqsrfygwnga` (`arquivo_arqid`),
  CONSTRAINT `FK_dul0bjs5rg4ihkgqsrfygwnga` FOREIGN KEY (`arquivo_arqid`) REFERENCES `arquivo` (`arqid`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campo`
--

LOCK TABLES `campo` WRITE;
/*!40000 ALTER TABLE `campo` DISABLE KEYS */;
/*!40000 ALTER TABLE `campo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `condicao`
--

DROP TABLE IF EXISTS `condicao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `condicao` (
  `conid` bigint(20) NOT NULL AUTO_INCREMENT,
  `conoperadorlogico` int(11) DEFAULT NULL,
  `conordem` smallint(6) NOT NULL,
  `convaloresperado` varchar(128) DEFAULT NULL,
  `operacao_opeid` bigint(20) NOT NULL,
  `regra_regid` bigint(20) NOT NULL,
  PRIMARY KEY (`conid`),
  KEY `FK_cclcetdnre67oa83q9wuo08qa` (`operacao_opeid`),
  KEY `FK_biomvqcm76x36prtb23v2odbl` (`regra_regid`),
  CONSTRAINT `FK_biomvqcm76x36prtb23v2odbl` FOREIGN KEY (`regra_regid`) REFERENCES `regra` (`regid`),
  CONSTRAINT `FK_cclcetdnre67oa83q9wuo08qa` FOREIGN KEY (`operacao_opeid`) REFERENCES `operacao` (`opeid`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `condicao`
--

LOCK TABLES `condicao` WRITE;
/*!40000 ALTER TABLE `condicao` DISABLE KEYS */;
/*!40000 ALTER TABLE `condicao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operacao`
--

DROP TABLE IF EXISTS `operacao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `operacao` (
  `opeid` bigint(20) NOT NULL AUTO_INCREMENT,
  `opehabilitarvalor` bit(1) NOT NULL,
  `openome` varchar(25) NOT NULL,
  `opetipodado` int(11) NOT NULL,
  PRIMARY KEY (`opeid`),
  UNIQUE KEY `uk_operacao` (`opetipodado`,`opehabilitarvalor`,`openome`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operacao`
--

LOCK TABLES `operacao` WRITE;
/*!40000 ALTER TABLE `operacao` DISABLE KEYS */;
INSERT INTO `operacao` VALUES (9,_binary '\0','É do tipo data',0),(8,_binary '\0','É do tipo fracionário',0),(7,_binary '\0','É do tipo inteiro',0),(6,_binary '\0','É do tipo textual',0),(5,_binary '\0','Não permitir números',0),(2,_binary '','Começado com',0),(4,_binary '','Contido em',0),(1,_binary '','Igual a',0),(3,_binary '','Terminado com',0),(17,_binary '\0','É do tipo data',1),(16,_binary '\0','É do tipo fracionário',1),(15,_binary '\0','É do tipo inteiro',1),(14,_binary '\0','É do tipo textual',1),(13,_binary '','Contido em',1),(10,_binary '','Igual a',1),(11,_binary '','Maior que',1),(12,_binary '','Menor que',1),(26,_binary '\0','É do tipo data',2),(25,_binary '\0','É do tipo fracionário',2),(24,_binary '\0','É do tipo inteiro',2),(23,_binary '\0','É do tipo textual',2),(21,_binary '','Casas decimais',2),(22,_binary '','Contido em',2),(18,_binary '','Igual a',2),(19,_binary '','Maior que',2),(20,_binary '','Menor que',2),(35,_binary '\0','É do tipo data',3),(34,_binary '\0','É do tipo fracionário',3),(33,_binary '\0','É do tipo inteiro',3),(32,_binary '\0','É do tipo textual',3),(29,_binary '','Antes de',3),(31,_binary '','Contido em',3),(28,_binary '','Depois de',3),(30,_binary '','Formato',3),(27,_binary '','Igual a',3);
/*!40000 ALTER TABLE `operacao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `regra`
--

DROP TABLE IF EXISTS `regra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `regra` (
  `regid` bigint(20) NOT NULL AUTO_INCREMENT,
  `regdescricao` varchar(512) NOT NULL,
  `regtipodado` int(11) NOT NULL,
  `regtitulo` varchar(100) NOT NULL,
  PRIMARY KEY (`regid`),
  UNIQUE KEY `uk_regra` (`regtipodado`,`regtitulo`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `regra`
--

LOCK TABLES `regra` WRITE;
/*!40000 ALTER TABLE `regra` DISABLE KEYS */;
/*!40000 ALTER TABLE `regra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `regra_campo`
--

DROP TABLE IF EXISTS `regra_campo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `regra_campo` (
  `regras_regid` bigint(20) NOT NULL,
  `campos_camid` bigint(20) NOT NULL,
  KEY `FK_ksdxom0ynv0qtb24qybi1f9ef` (`campos_camid`),
  KEY `FK_iglb5gc17rgfcp47qlaf5ds48` (`regras_regid`),
  CONSTRAINT `FK_iglb5gc17rgfcp47qlaf5ds48` FOREIGN KEY (`regras_regid`) REFERENCES `regra` (`regid`),
  CONSTRAINT `FK_ksdxom0ynv0qtb24qybi1f9ef` FOREIGN KEY (`campos_camid`) REFERENCES `campo` (`camid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `regra_campo`
--

LOCK TABLES `regra_campo` WRITE;
/*!40000 ALTER TABLE `regra_campo` DISABLE KEYS */;
/*!40000 ALTER TABLE `regra_campo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `regra_template`
--

DROP TABLE IF EXISTS `regra_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `regra_template` (
  `regras_regid` bigint(20) NOT NULL,
  `templates_temid` bigint(20) NOT NULL,
  KEY `FK_e4vch0f0pif0vmubl8f51wvnv` (`templates_temid`),
  KEY `FK_tnvpiootxuwful1v6w2997lun` (`regras_regid`),
  CONSTRAINT `FK_e4vch0f0pif0vmubl8f51wvnv` FOREIGN KEY (`templates_temid`) REFERENCES `template` (`temid`),
  CONSTRAINT `FK_tnvpiootxuwful1v6w2997lun` FOREIGN KEY (`regras_regid`) REFERENCES `regra` (`regid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `regra_template`
--

LOCK TABLES `regra_template` WRITE;
/*!40000 ALTER TABLE `regra_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `regra_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `template`
--

DROP TABLE IF EXISTS `template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `template` (
  `temid` bigint(20) NOT NULL AUTO_INCREMENT,
  `temnome` varchar(255) NOT NULL,
  PRIMARY KEY (`temid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `template`
--

LOCK TABLES `template` WRITE;
/*!40000 ALTER TABLE `template` DISABLE KEYS */;
/*!40000 ALTER TABLE `template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'epi_data_cleaner'
--

--
-- Dumping routines for database 'epi_data_cleaner'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-12-06 13:05:24
