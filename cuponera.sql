-- MySQL dump 10.13  Distrib 5.7.23, for Linux (x86_64)
--
-- Host: localhost    Database: cuponera
-- ------------------------------------------------------
-- Server version	5.7.23-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `cuponera`
--

/*!40000 DROP DATABASE IF EXISTS `cuponera`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `cuponera` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_spanish2_ci */;

USE `cuponera`;

--
-- Temporary table structure for view `all_users`
--

DROP TABLE IF EXISTS `all_users`;
/*!50001 DROP VIEW IF EXISTS `all_users`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `all_users` AS SELECT 
 1 AS `email`,
 1 AS `password`,
 1 AS `user_type`,
 1 AS `id`,
 1 AS `confirmed`,
 1 AS `id_confirmation`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `authentication`
--

DROP TABLE IF EXISTS `authentication`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authentication` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  `token` varchar(255) COLLATE utf8_spanish2_ci NOT NULL,
  `auth` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authentication`
--

LOCK TABLES `authentication` WRITE;
/*!40000 ALTER TABLE `authentication` DISABLE KEYS */;
/*!40000 ALTER TABLE `authentication` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `company` (
  `id` varchar(6) COLLATE utf8_spanish2_ci NOT NULL,
  `name` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  `address` varchar(100) COLLATE utf8_spanish2_ci NOT NULL,
  `contact_name` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  `telephone` char(9) COLLATE utf8_spanish2_ci NOT NULL,
  `email` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  `type_company` int(11) NOT NULL,
  `pct_comission` int(11) NOT NULL,
  `password` char(64) COLLATE utf8_spanish2_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `type_company` (`type_company`),
  CONSTRAINT `FK_CompanyType` FOREIGN KEY (`type_company`) REFERENCES `company_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` VALUES ('ABC123','Variedades la Bendicion','San Salvador','Diego Lemus','2222-4444','prueba5@gmail.com',1,10,'688787d8ff144c502c7f5cffaafe2cc588d86079f9de88304c26b0cb99ce91c6'),('EMP256','La Cabuda','La mascota','Guillermo Calderon','7878-8959','guillermo.calderon@udb.edu.sv',4,5,'688787d8ff144c502c7f5cffaafe2cc588d86079f9de88304c26b0cb99ce91c6');
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company_type`
--

DROP TABLE IF EXISTS `company_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `company_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company_type`
--

LOCK TABLES `company_type` WRITE;
/*!40000 ALTER TABLE `company_type` DISABLE KEYS */;
INSERT INTO `company_type` VALUES (1,'Restaurante'),(2,'Transportes pesados'),(4,'Salon de Belleza'),(5,'Taller');
/*!40000 ALTER TABLE `company_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  `email` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  `password` char(64) COLLATE utf8_spanish2_ci NOT NULL,
  `id_company` varchar(6) COLLATE utf8_spanish2_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `FK_EmployeeCompany` (`id_company`),
  CONSTRAINT `FK_EmployeeCompany` FOREIGN KEY (`id_company`) REFERENCES `company` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'Carlos','Lemus','carlos@gmail.com','688787d8ff144c502c7f5cffaafe2cc588d86079f9de88304c26b0cb99ce91c6','ABC123'),(3,'Franklin','Esquivel','frank.esquivel115@gmail.com','688787d8ff144c502c7f5cffaafe2cc588d86079f9de88304c26b0cb99ce91c6','ABC123');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password_resets`
--

DROP TABLE IF EXISTS `password_resets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `password_resets` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  `token` varchar(255) COLLATE utf8_spanish2_ci NOT NULL,
  `date` datetime DEFAULT NULL,
  `expired` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `token_UNIQUE` (`token`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_resets`
--

LOCK TABLES `password_resets` WRITE;
/*!40000 ALTER TABLE `password_resets` DISABLE KEYS */;
INSERT INTO `password_resets` VALUES (1,'Test@gmail.com','sdasdasdadwedawdd','2018-08-18 12:07:32',0),(2,'fas','dafs','2018-08-17 12:30:11',1),(3,'prueba@gmail.com','67b17056-962e-40ec-b882-5913da1c2cfb','2018-08-20 20:48:52',1),(4,'lopezleonardo282@gmail.com','c647a3ba-9227-4792-aab3-cd35bb624270','2018-08-22 16:50:36',1),(5,'prueba5@gmail.com','3e910358-5dec-4c8a-a257-6bfb583f2ee4','2018-08-22 17:55:45',1);
/*!40000 ALTER TABLE `password_resets` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `password_resets_BEFORE_INSERT` BEFORE INSERT ON `password_resets` FOR EACH ROW BEGIN
	SET NEW.date = current_timestamp();
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `promotion`
--

DROP TABLE IF EXISTS `promotion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `promotion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  `regular_price` decimal(18,2) NOT NULL,
  `ofert_price` decimal(18,2) NOT NULL,
  `init_date` date NOT NULL,
  `end_date` date NOT NULL,
  `limit_date` date NOT NULL,
  `limit_cant` int(11) NOT NULL,
  `description` varchar(500) COLLATE utf8_spanish2_ci NOT NULL,
  `other_details` varchar(500) COLLATE utf8_spanish2_ci NOT NULL,
  `image` varchar(75) COLLATE utf8_spanish2_ci NOT NULL,
  `coupons_sold` int(11) NOT NULL DEFAULT '0',
  `coupons_available` int(11) NOT NULL,
  `earnings` decimal(18,2) NOT NULL DEFAULT '0.00',
  `charge_service` decimal(18,2) NOT NULL DEFAULT '0.00',
  `id_company` varchar(6) COLLATE utf8_spanish2_ci NOT NULL,
  `id_state` int(11) NOT NULL,
  `rejected_description` varchar(300) COLLATE utf8_spanish2_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_empresa` (`id_company`),
  KEY `id_estado` (`id_state`),
  CONSTRAINT `FK_PromotionCompany` FOREIGN KEY (`id_company`) REFERENCES `company` (`id`),
  CONSTRAINT `FK_PromotionState` FOREIGN KEY (`id_state`) REFERENCES `promotion_state` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotion`
--

LOCK TABLES `promotion` WRITE;
/*!40000 ALTER TABLE `promotion` DISABLE KEYS */;
INSERT INTO `promotion` VALUES (1, 'Gran venta de animales', '3.00', '2.00', '2018-08-26', '2018-08-30', '2018-08-31', 12, '<p>asdf</p>', 'sdaf', 'ofertaanimal.png', 6, 6, '12.00', '1.20', 'ABC123', 2, ''),
(2, 'RegaShoes', '10.00', '9.00', '2018-08-26', '2018-08-30', '2018-08-31', 100, '<p>Vamos a regalar zapatos, ven y canjea el cupon y encuentra tus zapatos ideales</p>', 'Valido solo por las tardes', 'Suburbia-zapatos1.jpg', 6, 94, '54.00', '5.40', 'ABC123', 2, ''),
(5, 'Banquete para 6', '16.00', '15.00', '2018-08-26', '2018-09-02', '2018-09-03', 25, '<p><span class=\"ql-font-serif\">Te tenemos una oferta solo para ti!, un banquete familiar para 6 personas por un precio increible al presentar la factura en tu restaurante mas cercano</span></p>', 'No hay detalles extra', 'banquet.jpg', 2, 23, '30.00', '3.00', 'ABC123', 2, ''),
(6, 'Hamburguesas Gigas', '8.00', '6.00', '2018-08-26', '2018-08-30', '2018-08-31', 40, '<p>Ven y disfruta de unas ricas hamburguesas al mejor precio que te puedas imaginar en nuestros establecimientos, te esperamos!!</p>', 'La oferta sera valida solamente si se presenta la factura de compra', 'hamburofert.jpg', 0, 40, '0.00', '0.00', 'ABC123', 1, ''),
(7, 'Ropa para bebes', '5.00', '4.00', '2018-08-26', '2018-09-04', '2018-09-05', 15, '<p>Ven y disfruta las grandes ofertas que tenemos para tu bebe con los mejores estilos para toda clase de ocasion, que esperas compra ya tu cupon!!</p>', 'Presentar factura en caja para hacer efectivo el canje', 'ofertabebes.jpg', 0, 15, '0.00', '0.00', 'ABC123', 2, ''),
(8, 'Vacaciones Caribeñas', '800.00', '794.00', '2018-08-26', '2018-09-02', '2018-09-03', 20, '<p>Ven y disfruta un viaje unico con toda tu familia por la riviera maya y las bellas playas del caribe, ven que esperas la playa te esta esperando!	</p>', 'La oferta es valida para 4 personas', 'ofertacaribe.jpg', 0, 20, '0.00', '0.00', 'ABC123', 1, ''),
(9, 'Black Friday - Ropa', '20.00', '18.00', '2018-08-26', '2018-08-30', '2018-08-31', 0, '<p>Ven y disfruta de los mejores precios en la mejor semana para comprar lo que quieras con tus amigos o familia, ven ya te esperamos!</p>', 'Debes presentar tu factura de compra para obtener un 10% en tu gasto total', 'ofertaropa.jpg', 0, 0, '0.00', '0.00', 'ABC123', 2, ''),
(10, 'Servicio de Veterinaria', '3.00', '2.00', '2018-08-26', '2018-08-28', '2018-08-29', 10, '<p>Trae a tu mascota a nuestra clinica tenemos el mejor personal especializado de la zona para el tratamiento correcto o problema que tenga tu mascota</p>', 'El 5% se aplica al total de la consulta', 'ofertveter.jpg', 0, 10, '0.00', '0.00', 'ABC123', 2, ''),
(11, 'Bahia Principe', '250.00', '200.00', '2018-08-26', '2018-09-02', '2018-09-03', 30, '<p>El hotel Bahia Principe te ofrece en esta ocasion como especial de agosto una oferta que no podras rechazar ya que al pagar 1 oferta puede venir un acompaÃ±ante GRATIS!! contigo para mas informacion llamar al Tel. 2222-6969</p>', 'No hay detalles extra', 'vacaciones.jpg', 0, 30, '0.00', '0.00', 'ABC123', 2, ''),
(12, 'Rebajas Video oca', '10.00', '8.00', '2018-08-26', '2018-08-29', '2018-08-30', 0, '<p>En Video Oca te ofrecemos el mejor equipo electronico a los mejores precios, solamente con la presentacion de la factura de pago tienes el 5% de descuento en TODO lo que compres, que esperas compra uno YA!!</p>', 'Valido solo para articulos web', 'webofrett.jpg', 0, 0, '0.00', '0.00', 'ABC123', 2, ''),
(13, 'Celular - LG G4 beat', '5.00', '5.00', '2018-08-26', '2018-08-28', '2018-08-30', 5, '<p>Te presentamos el telefono LG G4 beat el cual posee una gran calidad tanto en camara, como espacio de almacenamiento, memoria RAM ademas es calidad asegurada!</p>', 'Al presentar esta factura en nuestras instalaciones tienes un 14% de descuento en la compra de este celular', 'phoneofert.png', 0, 5, '0.00', '0.00', 'ABC123', 2, '');

/*!40000 ALTER TABLE `promotion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promotion_state`
--

DROP TABLE IF EXISTS `promotion_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `promotion_state` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `state` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotion_state`
--

LOCK TABLES `promotion_state` WRITE;
/*!40000 ALTER TABLE `promotion_state` DISABLE KEYS */;
INSERT INTO `promotion_state` VALUES (1,'En espera de aprobación'),(2,'Aprobada'),(3,'Rechazada'),(4,'Descartada'),(5,'Activa'),(6,'Pasada');
/*!40000 ALTER TABLE `promotion_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sales` (
  `coupon_code` varchar(13) COLLATE utf8_spanish2_ci NOT NULL,
  `promotion_id` int(11) NOT NULL,
  `client_id` int(11) NOT NULL,
  `date` datetime DEFAULT NULL,
  `verified` tinyint(1) NOT NULL DEFAULT '0',
  `sales_state` int(11) NOT NULL,
  PRIMARY KEY (`coupon_code`),
  UNIQUE KEY `coupon_code` (`coupon_code`),
  KEY `client_id` (`client_id`),
  KEY `promotion_id` (`promotion_id`),
  KEY `sales_state` (`sales_state`),
  CONSTRAINT `FK_ClientSales` FOREIGN KEY (`client_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_SalesPromotion` FOREIGN KEY (`promotion_id`) REFERENCES `promotion` (`id`),
  CONSTRAINT `FK_SalesState` FOREIGN KEY (`sales_state`) REFERENCES `sales_state` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES ('ABC1230000001',1,1,'2/8/2017',0,1),('ABC1230000002',2,1,'2/8/2017',0,2);
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `update_promotion_data` BEFORE INSERT ON `sales` FOR EACH ROW BEGIN
	DECLARE ofer_price DOUBLE;
    DECLARE pct_comission INTEGER;
    DECLARE limit_cant INTEGER;
    
	SELECT P.ofert_price, C.pct_comission, P.limit_cant INTO @ofert_price, @pct_comission, @limit_cant FROM promotion AS P INNER JOIN company AS C ON P.id_company = C.id WHERE P.id = NEW.promotion_id;
	IF @limit_cant > 0 THEN
		UPDATE promotion SET coupons_available = (coupons_available - 1), coupons_sold = (coupons_sold + 1), earnings =  ((coupons_sold) *(@ofert_price)), charge_service = ((earnings)*(@pct_comission / 100)) WHERE id =  NEW.promotion_id;
	ELSE
		UPDATE promotion SET coupons_sold = (coupons_sold + 1), earnings = ((coupons_sold) *(@ofert_price)), charge_service = ((earnings)*(@pct_comission / 100)) WHERE id =  NEW.promotion_id;
    END IF;

    SET NEW.date = current_timestamp();
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `sales_state`
--

DROP TABLE IF EXISTS `sales_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sales_state` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `state` varchar(20) COLLATE utf8_spanish2_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_state`
--

LOCK TABLES `sales_state` WRITE;
/*!40000 ALTER TABLE `sales_state` DISABLE KEYS */;
INSERT INTO `sales_state` VALUES (1,'Canjeado'),(2,'Disponible'),(3,' Vencido');
/*!40000 ALTER TABLE `sales_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  `email` varchar(50) COLLATE utf8_spanish2_ci NOT NULL,
  `password` char(64) COLLATE utf8_spanish2_ci NOT NULL,
  `user_type` int(11) NOT NULL,
  `dui` char(10) COLLATE utf8_spanish2_ci NOT NULL,
  `nit` char(17) COLLATE utf8_spanish2_ci NOT NULL,
  `confirmed` tinyint(1) NOT NULL,
  `id_confirmation` varchar(70) COLLATE utf8_spanish2_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `dui` (`dui`),
  UNIQUE KEY `nit` (`nit`),
  KEY `user_type` (`user_type`),
  CONSTRAINT `FK_UserType` FOREIGN KEY (`user_type`) REFERENCES `user_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Alberto','Lemus','albeto@gmai.com','688787d8ff144c502c7f5cffaafe2cc588d86079f9de88304c26b0cb99ce91c6',1,'12345678-9','1234-123456-123-1',1,NULL),(2,'Leonardo','Lopez','lopezleonardo282@gmail.com','688787d8ff144c502c7f5cffaafe2cc588d86079f9de88304c26b0cb99ce91c6',1,'78945612-8','1234-123456-123-2',1,''),(3,'Guillermo','Calderon','mochila@gmail.com','688787d8ff144c502c7f5cffaafe2cc588d86079f9de88304c26b0cb99ce91c6',1,'58748548-0','1234-123450-123-1',1,'');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_type`
--

DROP TABLE IF EXISTS `user_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) COLLATE utf8_spanish2_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_type`
--

LOCK TABLES `user_type` WRITE;
/*!40000 ALTER TABLE `user_type` DISABLE KEYS */;
INSERT INTO `user_type` VALUES (1,'client'),(2,'administrator');
/*!40000 ALTER TABLE `user_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'cuponera'
--
/*!50106 SET @save_time_zone= @@TIME_ZONE */ ;
/*!50106 DROP EVENT IF EXISTS `change_state_promotion` */;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = utf8 */ ;;
/*!50003 SET character_set_results = utf8 */ ;;
/*!50003 SET collation_connection  = utf8_general_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = '+00:00' */ ;;
/*!50106 CREATE*/ /*!50117 DEFINER=`root`@`localhost`*/ /*!50106 EVENT `change_state_promotion` ON SCHEDULE EVERY 1 DAY STARTS '2018-08-18 00:00:00' ON COMPLETION PRESERVE ENABLE DO UPDATE cuponera.promotion SET id_state = 6 WHERE limit_date < DATE_FORMAT(NOW(), '%Y-%m-%d')/ 
END */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
/*!50106 DROP EVENT IF EXISTS `event_check_procedures` */;;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = utf8 */ ;;
/*!50003 SET character_set_results = utf8 */ ;;
/*!50003 SET collation_connection  = utf8_general_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE*/ /*!50117 DEFINER=`root`@`localhost`*/ /*!50106 EVENT `event_check_procedures` ON SCHEDULE EVERY 5 MINUTE STARTS '2018-08-18 12:29:43' ON COMPLETION PRESERVE ENABLE DO CALL cuponera.check_reset_requests() */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
DELIMITER ;
/*!50106 SET TIME_ZONE= @save_time_zone */ ;

--
-- Dumping routines for database 'cuponera'
--
/*!50003 DROP PROCEDURE IF EXISTS `check_reset_requests` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `check_reset_requests`()
BEGIN	
	UPDATE password_resets SET expired = current_timestamp() > DATE_ADD(date, INTERVAL 1 DAY);
    
    DELETE FROM password_resets WHERE current_timestamp() > DATE_ADD(date, INTERVAL 2 DAY) AND expired = 1;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `check_sale` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `check_sale`(IN _coupon_code VARChAR(13), IN _dui VARCHAR(10))
BEGIN
	SELECT * FROM sales AS S INNER JOIN user AS U ON S.client_id = U.id WHERE S.coupon_code = _coupon_code AND U.dui = _dui;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `count_sales` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `count_sales`(IN _company VARCHAR(6))
BEGIN
SELECT COUNT(*) AS cuenta FROM sales WHERE coupon_code LIKE CONCAT('%',_company,'%');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `insert_promotion` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_promotion`(IN _title VARCHAR(50), IN _regular_price INTEGER, IN _ofert_price INTEGER, IN _init_date DATE, IN _end_date DATE,IN _limit_date DATE, IN _limit_cant INTEGER, IN _description VARCHAR(500), IN _other_details VARCHAR(500), IN _image VARCHAR(75), IN _id_company VARCHAR(6))
BEGIN
	DECLARE _coupons_available INTEGER;  
	SET @_coupons_available = _limit_cant;
    INSERT INTO promotion(title, regular_price, ofert_price, init_date, end_date, limit_date, limit_cant, description, other_details, image, coupons_available, id_company, id_state, rejected_description) VALUES(_title, _regular_price, _ofert_price, _init_date, _end_date, _limit_date, _limit_cant, _description, _other_details, _image, @_coupons_available, _id_company, 1, "");
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `insert_sales` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_sales`(IN _coupon_code VARCHAR(13), IN _promotion INTEGER, IN _client INTEGER, _state INTEGER, OUT _affected_row INTEGER)
BEGIN
	DECLARE xlimit_cant integer;
	DECLARE xcoupons_available integer;
    
    SELECT limit_cant INTO xlimit_cant FROM promotion WHERE id = _promotion;
    SELECT coupons_available INTO xcoupons_available FROM promotion WHERE id = _promotion;
	
    IF xlimit_cant > 0 THEN
		IF  1 > xcoupons_available THEN
			SET _affected_row = 0;
		ELSE
			INSERT INTO sales(coupon_code, promotion_id, client_id, sales_state) VALUE(_coupon_code, _promotion, _client, _state);
            SET _affected_row = 1;
		END IF;
	ELSE
		INSERT INTO sales(coupon_code, promotion_id, client_id, sales_state) VALUE(_coupon_code, _promotion, _client,  _state);
        SET _affected_row = 2;
	END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `update_company` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_company`(IN _id VARCHAR(6), IN _address VARCHAR(100), IN _contact_name VARCHAR(50), IN _telephone VARCHAR(9), IN _email VARCHAR(50), IN _name VARCHAR(50), IN _type INTEGER)
BEGIN
	UPDATE company SET address = _address, contact_name = _contact_name, telephone = _telephone, email = _email, name = _name, type_company = _type WHERE id = _id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `update_company_password` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_company_password`(IN _id VARCHAR(6), IN _password CHAR(64))
BEGIN
	UPDATE company SET password = _password WHERE id = _id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `update_employee` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_employee`(IN _id INTEGER, IN _email VARCHAR(50), IN _name VARCHAR(50), IN _last_name VARCHAR(50))
BEGIN
	UPDATE employee SET name = _name, email = _email, last_name = _last_name WHERE id = _id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `update_employee_password` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_employee_password`(IN _id INTEGER, IN _password CHAR(64))
BEGIN
	UPDATE employee SET password = _password WHERE id = _id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `update_user` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_user`(IN _id INTEGER, IN _name VARCHAR(50), IN _last_name VARCHAR(50), IN _email VARCHAR(50), IN _id_type INTEGER)
BEGIN
	UPDATE user SET name = _name, last_name = _last_name, email = _email, user_type = _id_type  WHERE id = _id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `update_user_password` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_AUTO_VALUE_ON_ZERO' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_user_password`(IN _id INTEGER, IN _password CHAR(64))
BEGIN
	UPDATE user SET password = _password WHERE id = _id;
END ;;
DELIMITER ;

DROP PROCEDURE if EXISTS pagination;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `pagination`(IN _x INTEGER)
BEGIN
	DECLARE maxi INTEGER;
    DECLARE mini INTEGER;
    
    SET maxi = _x*10;
    SET mini = (_x*10) - 10;
    
	SELECT promotion.title, promotion.image,promotion.id, CEIL((SELECT COUNT(*) FROM promotion WHERE id_state = 2)/10) AS 'Items' FROM promotion
    WHERE id_state = 2
	ORDER BY promotion.limit_date DESC
	LIMIT mini,maxi;
END;;
DELIMITER ;

/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Current Database: `cuponera`
--

DELIMITER $$
--
-- Eventos
--
DROP EVENT IF EXISTS `change_state_promotion`$$
CREATE DEFINER=`root`@`localhost` EVENT `change_state_promotion` ON SCHEDULE EVERY 1 DAY STARTS '2018-08-14 00:00:00' ON COMPLETION PRESERVE ENABLE DO UPDATE cuponera.promotion SET id_state = 6 WHERE limit_date < DATE_FORMAT(NOW(), '%Y-%m-%d')/ 
END$$

DROP EVENT IF EXISTS `change_state_sales`$$
CREATE DEFINER=`root`@`localhost` EVENT `change_state_sales` ON SCHEDULE EVERY 1 DAY STARTS '2018-08-14 00:00:00' ON COMPLETION NOT PRESERVE ENABLE DO update sales set sales_state = 3 where promotion_id = (SELECT promotion.id
FROM cuponera.promotion promotion
WHERE promotion.limit_date < DATE_FORMAT(NOW(), '%Y-%m-%d')) AND sales_state = 2$$

DELIMITER ;
COMMIT;

USE `cuponera`;

--
-- Final view structure for view `all_users`
--

/*!50001 DROP VIEW IF EXISTS `all_users`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `all_users` AS select `employee`.`email` AS `email`,`employee`.`password` AS `password`,'employee' AS `user_type`,`employee`.`id` AS `id`,'' AS `confirmed`,'' AS `id_confirmation` from `employee` union select `company`.`email` AS `email`,`company`.`password` AS `password`,'company' AS `user_type`,`company`.`id` AS `id`,'' AS `confimed`,'' AS `id_confirmation` from `company` union (select `u`.`email` AS `email`,`u`.`password` AS `password`,`t`.`type` AS `user_type`,`u`.`id` AS `id`,`u`.`confirmed` AS `confirmed`,`u`.`id_confirmation` AS `id_confirmation` from (`user` `u` join `user_type` `t` on((`u`.`user_type` = `t`.`id`)))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-08-22 20:24:18
