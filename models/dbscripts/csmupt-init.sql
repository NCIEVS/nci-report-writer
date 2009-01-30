-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.18-nt


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema csmupt
--

CREATE DATABASE IF NOT EXISTS csmupt;
USE csmupt;

--
-- Definition of table `csm_application`
--

DROP TABLE IF EXISTS `csm_application`;
CREATE TABLE `csm_application` (
  `APPLICATION_ID` bigint(20) NOT NULL auto_increment,
  `APPLICATION_NAME` varchar(255) NOT NULL,
  `APPLICATION_DESCRIPTION` varchar(200) NOT NULL,
  `DECLARATIVE_FLAG` tinyint(1) NOT NULL default '0',
  `ACTIVE_FLAG` tinyint(1) NOT NULL default '0',
  `UPDATE_DATE` date default '0000-00-00',
  `DATABASE_URL` varchar(100) default NULL,
  `DATABASE_USER_NAME` varchar(100) default NULL,
  `DATABASE_PASSWORD` varchar(100) default NULL,
  `DATABASE_DIALECT` varchar(100) default NULL,
  `DATABASE_DRIVER` varchar(100) default NULL,
  PRIMARY KEY  (`APPLICATION_ID`),
  UNIQUE KEY `UQ_APPLICATION_NAME` (`APPLICATION_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `csm_application`
--

/*!40000 ALTER TABLE `csm_application` DISABLE KEYS */;
INSERT INTO `csm_application` (`APPLICATION_ID`,`APPLICATION_NAME`,`APPLICATION_DESCRIPTION`,`DECLARATIVE_FLAG`,`ACTIVE_FLAG`,`UPDATE_DATE`,`DATABASE_URL`,`DATABASE_USER_NAME`,`DATABASE_PASSWORD`,`DATABASE_DIALECT`,`DATABASE_DRIVER`) VALUES 
 (1,'csmupt','CSM UPT Super Admin Application',0,1,'2008-12-15','jdbc:mysql://localhost:3306/csmupt','root','qN+MnXquuqO8j2uyHEABIQ==','org.hibernate.dialect.MySQLDialect','com.mysql.jdbc.Driver'),
 (2,'ncireportwriter','ncireportwriter',0,1,'2008-12-15','jdbc:mysql://localhost:3306/csmupt','root','qN+MnXquuqO8j2uyHEABIQ==','org.hibernate.dialect.MySQLDialect','com.mysql.jdbc.Driver');
/*!40000 ALTER TABLE `csm_application` ENABLE KEYS */;


--
-- Definition of table `csm_filter_clause`
--

DROP TABLE IF EXISTS `csm_filter_clause`;
CREATE TABLE `csm_filter_clause` (
  `FILTER_CLAUSE_ID` bigint(20) NOT NULL auto_increment,
  `CLASS_NAME` varchar(100) NOT NULL,
  `FILTER_CHAIN` varchar(2000) NOT NULL,
  `TARGET_CLASS_NAME` varchar(100) NOT NULL,
  `TARGET_CLASS_ATTRIBUTE_NAME` varchar(100) NOT NULL,
  `TARGET_CLASS_ATTRIBUTE_TYPE` varchar(100) NOT NULL,
  `TARGET_CLASS_ALIAS` varchar(100) default NULL,
  `TARGET_CLASS_ATTRIBUTE_ALIAS` varchar(100) default NULL,
  `GENERATED_SQL_USER` varchar(4000) NOT NULL,
  `GENERATED_SQL_GROUP` varchar(4000) NOT NULL,
  `APPLICATION_ID` bigint(20) NOT NULL,
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`FILTER_CLAUSE_ID`),
  KEY `FK_APPLICATION_FILTER_CLAUSE` (`APPLICATION_ID`),
  CONSTRAINT `FK_APPLICATION_FILTER_CLAUSE` FOREIGN KEY (`APPLICATION_ID`) REFERENCES `csm_application` (`APPLICATION_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `csm_filter_clause`
--

/*!40000 ALTER TABLE `csm_filter_clause` DISABLE KEYS */;
/*!40000 ALTER TABLE `csm_filter_clause` ENABLE KEYS */;


--
-- Definition of table `csm_group`
--

DROP TABLE IF EXISTS `csm_group`;
CREATE TABLE `csm_group` (
  `GROUP_ID` bigint(20) NOT NULL auto_increment,
  `GROUP_NAME` varchar(255) NOT NULL,
  `GROUP_DESC` varchar(200) default NULL,
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  `APPLICATION_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`GROUP_ID`),
  UNIQUE KEY `UQ_GROUP_GROUP_NAME` (`APPLICATION_ID`,`GROUP_NAME`),
  KEY `idx_APPLICATION_ID` (`APPLICATION_ID`),
  CONSTRAINT `FK_APPLICATION_GROUP` FOREIGN KEY (`APPLICATION_ID`) REFERENCES `csm_application` (`APPLICATION_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `csm_group`
--

/*!40000 ALTER TABLE `csm_group` DISABLE KEYS */;
INSERT INTO `csm_group` (`GROUP_ID`,`GROUP_NAME`,`GROUP_DESC`,`UPDATE_DATE`,`APPLICATION_ID`) VALUES 
 (1,'admin','admin','2008-12-15',2),
 (2,'non-admin','non-admin read only','2008-12-15',2);
/*!40000 ALTER TABLE `csm_group` ENABLE KEYS */;


--
-- Definition of table `csm_pg_pe`
--

DROP TABLE IF EXISTS `csm_pg_pe`;
CREATE TABLE `csm_pg_pe` (
  `PG_PE_ID` bigint(20) NOT NULL auto_increment,
  `PROTECTION_GROUP_ID` bigint(20) NOT NULL,
  `PROTECTION_ELEMENT_ID` bigint(20) NOT NULL,
  `UPDATE_DATE` date default '0000-00-00',
  PRIMARY KEY  (`PG_PE_ID`),
  UNIQUE KEY `UQ_PROTECTION_GROUP_PROTECTION_ELEMENT_PROTECTION_GROUP_ID` (`PROTECTION_ELEMENT_ID`,`PROTECTION_GROUP_ID`),
  KEY `idx_PROTECTION_ELEMENT_ID` (`PROTECTION_ELEMENT_ID`),
  KEY `idx_PROTECTION_GROUP_ID` (`PROTECTION_GROUP_ID`),
  CONSTRAINT `FK_PROTECTION_ELEMENT_PROTECTION_GROUP` FOREIGN KEY (`PROTECTION_ELEMENT_ID`) REFERENCES `csm_protection_element` (`PROTECTION_ELEMENT_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_PROTECTION_GROUP_PROTECTION_ELEMENT` FOREIGN KEY (`PROTECTION_GROUP_ID`) REFERENCES `csm_protection_group` (`PROTECTION_GROUP_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `csm_pg_pe`
--

/*!40000 ALTER TABLE `csm_pg_pe` DISABLE KEYS */;
INSERT INTO `csm_pg_pe` (`PG_PE_ID`,`PROTECTION_GROUP_ID`,`PROTECTION_ELEMENT_ID`,`UPDATE_DATE`) VALUES 
 (1,1,3,'0000-00-00'),
 (2,2,3,'0000-00-00');
/*!40000 ALTER TABLE `csm_pg_pe` ENABLE KEYS */;


--
-- Definition of table `csm_privilege`
--

DROP TABLE IF EXISTS `csm_privilege`;
CREATE TABLE `csm_privilege` (
  `PRIVILEGE_ID` bigint(20) NOT NULL auto_increment,
  `PRIVILEGE_NAME` varchar(100) NOT NULL,
  `PRIVILEGE_DESCRIPTION` varchar(200) default NULL,
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`PRIVILEGE_ID`),
  UNIQUE KEY `UQ_PRIVILEGE_NAME` (`PRIVILEGE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `csm_privilege`
--

/*!40000 ALTER TABLE `csm_privilege` DISABLE KEYS */;
INSERT INTO `csm_privilege` (`PRIVILEGE_ID`,`PRIVILEGE_NAME`,`PRIVILEGE_DESCRIPTION`,`UPDATE_DATE`) VALUES 
 (1,'CREATE','This privilege grants permission to a user to create an entity. This entity can be an object, a database entry, or a resource such as a network connection','2008-12-15'),
 (2,'ACCESS','This privilege allows a user to access a particular resource.  Examples of resources include a network or database connection, socket, module of the application, or even the application itself','2008-12-15'),
 (3,'READ','This privilege permits the user to read data from a file, URL, database, an object, etc. This can be used at an entity level signifying that the user is allowed to read data about a particular entry','2008-12-15'),
 (4,'WRITE','This privilege allows a user to write data to a file, URL, database, an object, etc. This can be used at an entity level signifying that the user is allowed to write data about a particular entity','2008-12-15'),
 (5,'UPDATE','This privilege grants permission at an entity level and signifies that the user is allowed to update data for a particular entity. Entities may include an object, object attribute, database row etc','2008-12-15'),
 (6,'DELETE','This privilege permits a user to delete a logical entity. This entity can be an object, a database entry, a resource such as a network connection, etc','2008-12-15'),
 (7,'EXECUTE','This privilege allows a user to execute a particular resource. The resource can be a method, function, behavior of the application, URL, button etc','2008-12-15');
/*!40000 ALTER TABLE `csm_privilege` ENABLE KEYS */;


--
-- Definition of table `csm_protection_element`
--

DROP TABLE IF EXISTS `csm_protection_element`;
CREATE TABLE `csm_protection_element` (
  `PROTECTION_ELEMENT_ID` bigint(20) NOT NULL auto_increment,
  `PROTECTION_ELEMENT_NAME` varchar(100) NOT NULL,
  `PROTECTION_ELEMENT_DESCRIPTION` varchar(200) default NULL,
  `OBJECT_ID` varchar(100) NOT NULL,
  `ATTRIBUTE` varchar(100) default NULL,
  `ATTRIBUTE_VALUE` varchar(100) default NULL,
  `PROTECTION_ELEMENT_TYPE` varchar(100) default NULL,
  `APPLICATION_ID` bigint(20) NOT NULL,
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`PROTECTION_ELEMENT_ID`),
  UNIQUE KEY `UQ_PE_PE_NAME_ATTRIBUTE_VALUE_APP_ID` (`OBJECT_ID`,`ATTRIBUTE`,`ATTRIBUTE_VALUE`,`APPLICATION_ID`),
  KEY `idx_APPLICATION_ID` (`APPLICATION_ID`),
  CONSTRAINT `FK_PE_APPLICATION` FOREIGN KEY (`APPLICATION_ID`) REFERENCES `csm_application` (`APPLICATION_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `csm_protection_element`
--

/*!40000 ALTER TABLE `csm_protection_element` DISABLE KEYS */;
INSERT INTO `csm_protection_element` (`PROTECTION_ELEMENT_ID`,`PROTECTION_ELEMENT_NAME`,`PROTECTION_ELEMENT_DESCRIPTION`,`OBJECT_ID`,`ATTRIBUTE`,`ATTRIBUTE_VALUE`,`PROTECTION_ELEMENT_TYPE`,`APPLICATION_ID`,`UPDATE_DATE`) VALUES 
 (1,'csmupt','CSM UPT Super Admin Application Protection Element','csmupt','','','',1,'2008-12-15'),
 (2,'ncireportwriter','ncireportwriter','ncireportwriter','','','',1,'2008-12-15'),
 (3,'ncireportwriter','ncireportwriter','ncireportwriter','','','ncireportwriter',2,'2008-12-15'),
 (4,'csmupt','csmupt','csmupt','','','csmupt',2,'2008-12-15');
/*!40000 ALTER TABLE `csm_protection_element` ENABLE KEYS */;


--
-- Definition of table `csm_protection_group`
--

DROP TABLE IF EXISTS `csm_protection_group`;
CREATE TABLE `csm_protection_group` (
  `PROTECTION_GROUP_ID` bigint(20) NOT NULL auto_increment,
  `PROTECTION_GROUP_NAME` varchar(100) NOT NULL,
  `PROTECTION_GROUP_DESCRIPTION` varchar(200) default NULL,
  `APPLICATION_ID` bigint(20) NOT NULL,
  `LARGE_ELEMENT_COUNT_FLAG` tinyint(1) NOT NULL,
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  `PARENT_PROTECTION_GROUP_ID` bigint(20) default NULL,
  PRIMARY KEY  (`PROTECTION_GROUP_ID`),
  UNIQUE KEY `UQ_PROTECTION_GROUP_PROTECTION_GROUP_NAME` (`APPLICATION_ID`,`PROTECTION_GROUP_NAME`),
  KEY `idx_APPLICATION_ID` (`APPLICATION_ID`),
  KEY `idx_PARENT_PROTECTION_GROUP_ID` (`PARENT_PROTECTION_GROUP_ID`),
  CONSTRAINT `FK_PG_APPLICATION` FOREIGN KEY (`APPLICATION_ID`) REFERENCES `csm_application` (`APPLICATION_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_PROTECTION_GROUP` FOREIGN KEY (`PARENT_PROTECTION_GROUP_ID`) REFERENCES `csm_protection_group` (`PROTECTION_GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `csm_protection_group`
--

/*!40000 ALTER TABLE `csm_protection_group` DISABLE KEYS */;
INSERT INTO `csm_protection_group` (`PROTECTION_GROUP_ID`,`PROTECTION_GROUP_NAME`,`PROTECTION_GROUP_DESCRIPTION`,`APPLICATION_ID`,`LARGE_ELEMENT_COUNT_FLAG`,`UPDATE_DATE`,`PARENT_PROTECTION_GROUP_ID`) VALUES 
 (1,'admin_pg','pg for admins',2,0,'2008-12-15',NULL),
 (2,'non-admin-pg','pg for non-admin',2,0,'2008-12-15',NULL);
/*!40000 ALTER TABLE `csm_protection_group` ENABLE KEYS */;


--
-- Definition of table `csm_role`
--

DROP TABLE IF EXISTS `csm_role`;
CREATE TABLE `csm_role` (
  `ROLE_ID` bigint(20) NOT NULL auto_increment,
  `ROLE_NAME` varchar(100) NOT NULL,
  `ROLE_DESCRIPTION` varchar(200) default NULL,
  `APPLICATION_ID` bigint(20) NOT NULL,
  `ACTIVE_FLAG` tinyint(1) NOT NULL,
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`ROLE_ID`),
  UNIQUE KEY `UQ_ROLE_ROLE_NAME` (`APPLICATION_ID`,`ROLE_NAME`),
  KEY `idx_APPLICATION_ID` (`APPLICATION_ID`),
  CONSTRAINT `FK_APPLICATION_ROLE` FOREIGN KEY (`APPLICATION_ID`) REFERENCES `csm_application` (`APPLICATION_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `csm_role`
--

/*!40000 ALTER TABLE `csm_role` DISABLE KEYS */;
INSERT INTO `csm_role` (`ROLE_ID`,`ROLE_NAME`,`ROLE_DESCRIPTION`,`APPLICATION_ID`,`ACTIVE_FLAG`,`UPDATE_DATE`) VALUES 
 (1,'admin-role','admin-role',2,1,'2008-12-15'),
 (2,'non-admin-role','non-admin-role',2,1,'2008-12-15');
/*!40000 ALTER TABLE `csm_role` ENABLE KEYS */;


--
-- Definition of table `csm_role_privilege`
--

DROP TABLE IF EXISTS `csm_role_privilege`;
CREATE TABLE `csm_role_privilege` (
  `ROLE_PRIVILEGE_ID` bigint(20) NOT NULL auto_increment,
  `ROLE_ID` bigint(20) NOT NULL,
  `PRIVILEGE_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`ROLE_PRIVILEGE_ID`),
  UNIQUE KEY `UQ_ROLE_PRIVILEGE_ROLE_ID` (`PRIVILEGE_ID`,`ROLE_ID`),
  KEY `idx_PRIVILEGE_ID` (`PRIVILEGE_ID`),
  KEY `idx_ROLE_ID` (`ROLE_ID`),
  CONSTRAINT `FK_PRIVILEGE_ROLE` FOREIGN KEY (`PRIVILEGE_ID`) REFERENCES `csm_privilege` (`PRIVILEGE_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_ROLE` FOREIGN KEY (`ROLE_ID`) REFERENCES `csm_role` (`ROLE_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `csm_role_privilege`
--

/*!40000 ALTER TABLE `csm_role_privilege` DISABLE KEYS */;
INSERT INTO `csm_role_privilege` (`ROLE_PRIVILEGE_ID`,`ROLE_ID`,`PRIVILEGE_ID`) VALUES 
 (3,1,1),
 (2,1,2),
 (4,1,3),
 (8,2,3),
 (1,1,4),
 (5,1,5),
 (6,1,6),
 (7,1,7);
/*!40000 ALTER TABLE `csm_role_privilege` ENABLE KEYS */;


--
-- Definition of table `csm_user`
--

DROP TABLE IF EXISTS `csm_user`;
CREATE TABLE `csm_user` (
  `USER_ID` bigint(20) NOT NULL auto_increment,
  `LOGIN_NAME` varchar(500) NOT NULL,
  `MIGRATED_FLAG` tinyint(1) NOT NULL default '0',
  `FIRST_NAME` varchar(100) NOT NULL,
  `LAST_NAME` varchar(100) NOT NULL,
  `ORGANIZATION` varchar(100) default NULL,
  `DEPARTMENT` varchar(100) default NULL,
  `TITLE` varchar(100) default NULL,
  `PHONE_NUMBER` varchar(15) default NULL,
  `PASSWORD` varchar(100) default NULL,
  `EMAIL_ID` varchar(100) default NULL,
  `START_DATE` date default NULL,
  `END_DATE` date default NULL,
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  `PREMGRT_LOGIN_NAME` varchar(100) default NULL,
  PRIMARY KEY  (`USER_ID`),
  UNIQUE KEY `UQ_LOGIN_NAME` (`LOGIN_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `csm_user`
--

/*!40000 ALTER TABLE `csm_user` DISABLE KEYS */;
INSERT INTO `csm_user` (`USER_ID`,`LOGIN_NAME`,`MIGRATED_FLAG`,`FIRST_NAME`,`LAST_NAME`,`ORGANIZATION`,`DEPARTMENT`,`TITLE`,`PHONE_NUMBER`,`PASSWORD`,`EMAIL_ID`,`START_DATE`,`END_DATE`,`UPDATE_DATE`,`PREMGRT_LOGIN_NAME`) VALUES 
 (1,'kimong',0,'Kim','Ong','NGIT','CBIIT','Senior Software Engineer','3013342962','zJPWCwDeSgG8j2uyHEABIQ==','OngKi@mail.nih.gov','2008-12-11',NULL,'2008-12-15',''),
 (2,'admin',0,'Kim','Ong','admin','admin','admin','3013342962','zJPWCwDeSgG8j2uyHEABIQ==','OngKi@mail.nih.gov','2008-12-11',NULL,'2008-12-15',''),
 (3,'testr',0,'test','r','testr','testr','testr','','Bc24N7TU2Dk=','',NULL,NULL,'2008-12-15','');
/*!40000 ALTER TABLE `csm_user` ENABLE KEYS */;


--
-- Definition of table `csm_user_group`
--

DROP TABLE IF EXISTS `csm_user_group`;
CREATE TABLE `csm_user_group` (
  `USER_GROUP_ID` bigint(20) NOT NULL auto_increment,
  `USER_ID` bigint(20) NOT NULL,
  `GROUP_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`USER_GROUP_ID`),
  KEY `idx_USER_ID` (`USER_ID`),
  KEY `idx_GROUP_ID` (`GROUP_ID`),
  CONSTRAINT `FK_UG_GROUP` FOREIGN KEY (`GROUP_ID`) REFERENCES `csm_group` (`GROUP_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_USER_GROUP` FOREIGN KEY (`USER_ID`) REFERENCES `csm_user` (`USER_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `csm_user_group`
--

/*!40000 ALTER TABLE `csm_user_group` DISABLE KEYS */;
INSERT INTO `csm_user_group` (`USER_GROUP_ID`,`USER_ID`,`GROUP_ID`) VALUES 
 (1,1,1),
 (2,3,2);
/*!40000 ALTER TABLE `csm_user_group` ENABLE KEYS */;


--
-- Definition of table `csm_user_group_role_pg`
--

DROP TABLE IF EXISTS `csm_user_group_role_pg`;
CREATE TABLE `csm_user_group_role_pg` (
  `USER_GROUP_ROLE_PG_ID` bigint(20) NOT NULL auto_increment,
  `USER_ID` bigint(20) default NULL,
  `GROUP_ID` bigint(20) default NULL,
  `ROLE_ID` bigint(20) NOT NULL,
  `PROTECTION_GROUP_ID` bigint(20) NOT NULL,
  `UPDATE_DATE` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`USER_GROUP_ROLE_PG_ID`),
  KEY `idx_GROUP_ID` (`GROUP_ID`),
  KEY `idx_ROLE_ID` (`ROLE_ID`),
  KEY `idx_PROTECTION_GROUP_ID` (`PROTECTION_GROUP_ID`),
  KEY `idx_USER_ID` (`USER_ID`),
  CONSTRAINT `FK_USER_GROUP_ROLE_PROTECTION_GROUP_GROUPS` FOREIGN KEY (`GROUP_ID`) REFERENCES `csm_group` (`GROUP_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_USER_GROUP_ROLE_PROTECTION_GROUP_PROTECTION_GROUP` FOREIGN KEY (`PROTECTION_GROUP_ID`) REFERENCES `csm_protection_group` (`PROTECTION_GROUP_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_USER_GROUP_ROLE_PROTECTION_GROUP_ROLE` FOREIGN KEY (`ROLE_ID`) REFERENCES `csm_role` (`ROLE_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_USER_GROUP_ROLE_PROTECTION_GROUP_USER` FOREIGN KEY (`USER_ID`) REFERENCES `csm_user` (`USER_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `csm_user_group_role_pg`
--

/*!40000 ALTER TABLE `csm_user_group_role_pg` DISABLE KEYS */;
INSERT INTO `csm_user_group_role_pg` (`USER_GROUP_ROLE_PG_ID`,`USER_ID`,`GROUP_ID`,`ROLE_ID`,`PROTECTION_GROUP_ID`,`UPDATE_DATE`) VALUES 
 (1,1,NULL,1,1,'2008-12-15'),
 (2,3,NULL,2,2,'2008-12-15'),
 (3,NULL,1,1,1,'2008-12-15'),
 (4,NULL,2,2,2,'2008-12-15');
/*!40000 ALTER TABLE `csm_user_group_role_pg` ENABLE KEYS */;


--
-- Definition of table `csm_user_pe`
--

DROP TABLE IF EXISTS `csm_user_pe`;
CREATE TABLE `csm_user_pe` (
  `USER_PROTECTION_ELEMENT_ID` bigint(20) NOT NULL auto_increment,
  `PROTECTION_ELEMENT_ID` bigint(20) NOT NULL,
  `USER_ID` bigint(20) NOT NULL,
  PRIMARY KEY  (`USER_PROTECTION_ELEMENT_ID`),
  UNIQUE KEY `UQ_USER_PROTECTION_ELEMENT_PROTECTION_ELEMENT_ID` (`USER_ID`,`PROTECTION_ELEMENT_ID`),
  KEY `idx_USER_ID` (`USER_ID`),
  KEY `idx_PROTECTION_ELEMENT_ID` (`PROTECTION_ELEMENT_ID`),
  CONSTRAINT `FK_PE_USER` FOREIGN KEY (`USER_ID`) REFERENCES `csm_user` (`USER_ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_PROTECTION_ELEMENT_USER` FOREIGN KEY (`PROTECTION_ELEMENT_ID`) REFERENCES `csm_protection_element` (`PROTECTION_ELEMENT_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `csm_user_pe`
--

/*!40000 ALTER TABLE `csm_user_pe` DISABLE KEYS */;
INSERT INTO `csm_user_pe` (`USER_PROTECTION_ELEMENT_ID`,`PROTECTION_ELEMENT_ID`,`USER_ID`) VALUES 
 (3,1,1),
 (2,2,1),
 (4,1,2);
/*!40000 ALTER TABLE `csm_user_pe` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
