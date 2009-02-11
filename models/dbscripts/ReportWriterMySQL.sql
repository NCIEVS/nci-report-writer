-- Create Report Writer database
--
-- ------------------------------------------------------

--
-- Create schema reportwriter
--

CREATE DATABASE IF NOT EXISTS reportwriter;
USE reportwriter;

--
-- Definition of table `user`
--

CREATE TABLE `user` (
  `ID` bigint(20) NOT NULL,
  `LOGIN_NAME` varchar(100) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `hi_value`
--

CREATE TABLE `hi_value` (
  `next_value` int(10) unsigned NOT NULL auto_increment,
  PRIMARY KEY  (`next_value`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `customized_query`
--

CREATE TABLE `customized_query` (
  `ID` int(11) NOT NULL,
  `QUERY_EXPRESSION` varchar(255) default NULL,
  `CODING_SCHEME_NAME` varchar(255) default NULL,
  `CODING_SCHEME_VERSION` varchar(255) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIED` datetime default NULL,
  PRIMARY KEY  (`ID`),
  KEY `CREATED_BY` (`CREATED_BY`),
  CONSTRAINT `FK_CUSTOMIZED_QUERY_USER` FOREIGN KEY (`CREATED_BY`) REFERENCES `user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `report_format`
--

CREATE TABLE `report_format` (
  `ID` int(11) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `report_status`
--

CREATE TABLE `report_status` (
  `ID` int(11) NOT NULL,
  `LABEL` varchar(255) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `ACTIVE` tinyint(1) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `report`
--

CREATE TABLE `report` (
  `ID` int(11) NOT NULL,
  `LABEL` varchar(255) default NULL,
  `LAST_MODIFIED` datetime default NULL,
  `PATH_NAME` varchar(255) default NULL,
  `HAS_FORMAT` int(11) default NULL,
  `HAS_STATUS` int(11) default NULL,
  `MODIFIED_BY` bigint(20) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `HAS_FORMAT` (`HAS_FORMAT`),
  KEY `HAS_STATUS` (`HAS_STATUS`),
  CONSTRAINT `FK_REPORT_REPORT_FORMAT` FOREIGN KEY (`HAS_FORMAT`) REFERENCES `report_format` (`ID`),
  CONSTRAINT `FK_REPORT_REPORT_STATUS` FOREIGN KEY (`HAS_STATUS`) REFERENCES `report_status` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `customized_report`
--

CREATE TABLE `customized_report` (
  `REPORT_ID` int(11) NOT NULL,
  `BASED_ON` int(11) default NULL,
  PRIMARY KEY  (`REPORT_ID`),
  KEY `BASED_ON` (`BASED_ON`),
  KEY `REPORT_ID` (`REPORT_ID`),
  CONSTRAINT `FK_CUSTOMIZED_REPORT_CUSTOMIZED_QUERY` FOREIGN KEY (`BASED_ON`) REFERENCES `customized_query` (`ID`),
  CONSTRAINT `FK_CUSTOMIZED_REPORT_REPORT` FOREIGN KEY (`REPORT_ID`) REFERENCES `report` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `standard_report_template`
--

CREATE TABLE `standard_report_template` (
  `ID` int(11) NOT NULL,
  `LABEL` varchar(255) default NULL,
  `ROOT_CONCEPT_CODE` varchar(255) default NULL,
  `ASSOCIATION_NAME` varchar(255) default NULL,
  `DIRECTION` tinyint(1) default '0',
  `CODING_SCHEME_NAME` varchar(255) default NULL,
  `CODING_SCHEME_VERSION` varchar(255) default NULL,
  `LEVEL` int(11) default '-1',
  `DELIMITER` char(1) default '$',
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `report_column`
--

CREATE TABLE `report_column` (
  `ID` int(11) NOT NULL,
  `COLUMN_NUMBER` int(11) default NULL,
  `LABEL` varchar(255) default NULL,
  `FIELD_ID` varchar(255) default NULL,
  `PROPERTY_TYPE` varchar(255) default NULL,
  `PROPERTY_NAME` varchar(255) default NULL,
  `IS_PREFERRED` tinyint(1) default NULL,
  `REPRESENTATIONAL_FORM` varchar(255) default NULL,
  `SOURCE` varchar(255) default NULL,
  `QUALIFIER_NAME` varchar(255) default NULL,
  `QUALIFIER_VALUE` varchar(255) default NULL,
  `DELIMITER` char(1) default NULL,
  `CONDITIONAL_COLUMN` int(11) default '-1',
  `BELONGS_TO` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `BELONGS_TO` (`BELONGS_TO`),
  CONSTRAINT `FK_REPORT_COLUMN_STANDARD_REPORT_TEMPLATE` FOREIGN KEY (`BELONGS_TO`) REFERENCES `standard_report_template` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Definition of table `standard_report`
--

CREATE TABLE `standard_report` (
  `REPORT_ID` int(11) NOT NULL,
  `BASED_ON_TEMPLATE` int(11) default NULL,
  PRIMARY KEY  (`REPORT_ID`),
  KEY `REPORT_ID` (`REPORT_ID`),
  KEY `BASED_ON_TEMPLATE` (`BASED_ON_TEMPLATE`),
  CONSTRAINT `FK_STANDARD_REPORT_REPORT` FOREIGN KEY (`REPORT_ID`) REFERENCES `report` (`ID`),
  CONSTRAINT `FK_STANDARD_REPORT_STANDARD_REPORT_TEMPLATE` FOREIGN KEY (`BASED_ON_TEMPLATE`) REFERENCES `standard_report_template` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Data for table `user`
--

INSERT INTO `user` (`ID`,`LOGIN_NAME`) VALUES
 (101,'admin');

--
-- Data for table `hi_value`
--

INSERT INTO `hi_value` (`next_value`) VALUES
 (68);

--
-- Data for table `report_format`
--

INSERT INTO `report_format` (`ID`,`DESCRIPTION`) VALUES
 (404,'Text (tab delimited)'),
 (405,'Microsoft Office Excel');

--
-- Data for table `report_status`
--

INSERT INTO `report_status` (`ID`,`LABEL`,`DESCRIPTION`,`ACTIVE`) VALUES
 (505,'DRAFT','Report is a draft, not ready for download.',1),
 (506,'APPROVED','Report has been approved for download by users',1);

--
-- Data for table `standard_report_template`
--

INSERT INTO `standard_report_template` (`ID`,`LABEL`,`ROOT_CONCEPT_CODE`,`ASSOCIATION_NAME`,`DIRECTION`,`CODING_SCHEME_NAME`,`CODING_SCHEME_VERSION`,`LEVEL`,`DELIMITER`) VALUES
 (202,'FDA-UNII Subset Report','C63923','Concept_In_Subset',0,'NCI Thesaurus','08.12d',1,'$'),
 (2323,'Individual Case Safety (ICS) Subset Report','C54447','Concept_In_Subset',0,'NCI Thesaurus','08.12d',1,'$'),
 (3535,'Structured Product Labeling (SPL) Report','C54452','Concept_In_Subset',0,'NCI Thesaurus','08.12d',2,'$'),
 (4040,'CDISC Subset Report ','C61410','Concept_In_Subset',0,'NCI Thesaurus','08.12d',1,'$'),
 (4646,'CDRH Subset Report','C62596','Concept_In_Subset',0,'NCI Thesaurus','08.12d',1,'$'),
 (6060,'FDA-SPL Country Code Report','Semantic_Type|null|null|null|Geographic Area|exactMatch','',0,'NCI Thesaurus','08.12d',0,'$');

--
-- Data for table `report_column`
--

INSERT INTO `report_column` (`ID`,`COLUMN_NUMBER`,`LABEL`,`FIELD_ID`,`PROPERTY_TYPE`,`PROPERTY_NAME`,`IS_PREFERRED`,`REPRESENTATIONAL_FORM`,`SOURCE`,`QUALIFIER_NAME`,`QUALIFIER_VALUE`,`DELIMITER`,`CONDITIONAL_COLUMN`,`BELONGS_TO`) VALUES
 (303,1,'FDA UNII Code','Property','Generic','FDA_UNII_Code',NULL,'',' ','','','|',-1,202),
 (304,2,'FDA Preferred Term','Property','Presentation','FULL_SYN',NULL,'PT','FDA','','','|',-1,202),
 (1212,3,'NCI Concept Code','Code','','',NULL,'',' ','','','|',-1,202),
 (2929,1,'Source','Property','GENERIC','Contributing_Source',NULL,'',' ','','','|',-1,2323),
 (2930,2,'Subset Code','Associated Concept Code','','',NULL,'',' ','','','|',-1,2323),
 (2931,3,'Subset Name','Associated Concept Property','PRESENTATION','Preferred_Name',NULL,'',' ','','','|',-1,2323),
 (3131,4,'Concept Code','Code','','',NULL,'',' ','','','|',-1,2323),
 (3132,5,'Source PT','Property','PRESENTATION','FULL_SYN',NULL,'PT','FDA','','','|',-1,2323),
 (3333,6,'Source Definition','Property','DEFINITION','ALT_DEFINITION',NULL,'','FDA','','','|',-1,2323),
 (3334,6,'Source Synonyms','Property','PRESENTATION','FULL_SYN',NULL,'SY','FDA','','','|',-1,2323),
 (3636,1,'Source','Property','GENERIC','Contributing_Source',NULL,'',' ','','','|',-1,3535),
 (3637,2,'Subset Code','Associated Concept Code','','',NULL,'',' ','','','|',-1,3535),
 (3638,3,'Subset Name','Associated Concept Property','PRESENTATION','Preferred_Name',NULL,'',' ','','','|',-1,3535),
 (3639,4,'Concept Code','Code','','',NULL,'',' ','','','|',-1,3535),
 (3640,5,'Source PT','Property','PRESENTATION','FULL_SYN',NULL,'PT','FDA','','','|',-1,3535),
 (3641,6,'Source Definition','Property','DEFINITION','ALT_DEFINITION',NULL,'','FDA','','','|',-1,3535),
 (3642,7,'Source Synonym','Property','PRESENTATION','FULL_SYN',NULL,'SY','FDA','','','|',-1,3535),
 (4141,1,'Source','Property','GENERIC','Contributing_Source',NULL,'',' ','','','|',-1,4040),
 (4142,2,'Subset Code','Associated Concept Code','','',NULL,'',' ','','','|',-1,4040),
 (4143,3,'Subset Name','Associated Concept Property','PRESENTATION','Preferred_Name',NULL,'',' ','','','|',-1,4040),
 (4144,4,'Concept Code','Code','','',NULL,'',' ','','','|',-1,4040),
 (4145,5,'Source PT','Property','PRESENTATION','FULL_SYN',NULL,'PT','CDISC','','','|',-1,4040),
 (4146,6,'NCI PT','Property','PRESENTATION','Preferred_Name',NULL,'','','','','|',-1,4040),
 (4147,7,'Source Definition','Property','DEFINITION','ALT_DEFINITION',NULL,'','CDISC','','','|',-1,4040),
 (4148,8,'Source Synonyms','Property','PRESENTATION','FULL_SYN',NULL,'SY','CDISC','','','|',-1,4040),
 (4747,1,'Source','Property','GENERIC','Contributing_Source',NULL,'',' ','','','|',-1,4646),
 (4748,2,'Subset Code','Associated Concept Code','','',NULL,'',' ','','','|',-1,4646),
 (4749,3,'Subset Name','Associated Concept Property','PRESENTATION','Preferred_Name',NULL,'',' ','','','|',-1,4646),
 (4750,4,'Concept Code','Code','','',NULL,'',' ','','','|',-1,4646),
 (4848,5,'Source Code','Property Qualifier','PRESENTATION','FULL_SYN',NULL,'PT','FDA','source-code','','|',-1,4646),
 (4849,6,'Source PT','Property','PRESENTATION','FULL_SYN',NULL,'PT','FDA','','','|',-1,4646),
 (4850,7,'Source Synonyms','Property','PRESENTATION','FULL_SYN',NULL,'SY','FDA','','','|',-1,4646),
 (4851,8,'Source Definition','Property','DEFINITION','ALT_DEFINITION',NULL,'','FDA','','','|',-1,4646),
 (4852,9,'NCI Definition','Property','DEFINITION','DEFINITION',NULL,'',' ','','','|',-1,4646),
 (4853,10,'Parent Code','1st Parent Code','','',NULL,'',' ','','','|',-1,4646),
 (4854,11,'Parent Source Code','1st Parent Property Qualifier','PRESENTATION','FULL_SYN',NULL,'PT','FDA','source-code','','|',-1,4646),
 (4855,12,'Parent Source PT','1st Parent Property','PRESENTATION','FULL_SYN',NULL,'PT','FDA','','','|',-1,4646),
 (4856,13,'Parent NCI PT','1st Parent Property','PRESENTATION','Preferred_Name',NULL,'',' ','','','|',-1,4646),
 (4857,14,'2nd Parent Concept Code','2nd Parent Code','','',NULL,'',' ','','','|',-1,4646),
 (4858,15,'2nd Parent PT','2nd Parent Property','PRESENTATION','Preferred_Name',NULL,'',' ','','','|',6,4646),
 (6161,1,'ISO Code','Property','PRESENTATION','FULL_SYN',NULL,'CA3','NCI','','','|',-1,6060),
 (6162,2,'NCI Concept Code','Code','','',NULL,'',' ','','','|',-1,6060),
 (6163,3,'NCI Preferred Term','Property','PRESENTATION','Preferred_Name',NULL,'',' ','','','|',-1,6060);

--
-- Data for table `report`
--

INSERT INTO `report` (`ID`,`LABEL`,`LAST_MODIFIED`,`PATH_NAME`,`HAS_FORMAT`,`HAS_STATUS`,`MODIFIED_BY`,`CREATED_BY`) VALUES
 (5858,'Individual Case Safety (ICS) Subset Report.txt',NULL,'',404,505,NULL,101),
 (5859,'Individual Case Safety (ICS) Subset Report.xls',NULL,'',405,505,NULL,101),
 (5860,'Structured Product Labeling (SPL) Report.txt',NULL,'',404,505,NULL,101),
 (5861,'Structured Product Labeling (SPL) Report.xls',NULL,'',405,505,NULL,101),
 (5862,'CDISC Subset Report .txt',NULL,'',404,505,NULL,101),
 (5863,'CDISC Subset Report .xls',NULL,'',405,505,NULL,101),
 (5959,'CDRH Subset Report.txt',NULL,'',404,505,NULL,101),
 (5960,'CDRH Subset Report.xls',NULL,'',405,505,NULL,101),
 (6262,'FDA-UNII Subset Report.txt',NULL,'',404,505,NULL,101),
 (6263,'FDA-UNII Subset Report.xls',NULL,'',405,505,NULL,101),
 (6767,'FDA-SPL Country Code Report.txt',NULL,'',404,505,NULL,101),
 (6768,'FDA-SPL Country Code Report.xls',NULL,'',405,505,NULL,101);

--
-- Data for table `standard_report`
--

INSERT INTO `standard_report` (`REPORT_ID`,`BASED_ON_TEMPLATE`) VALUES
 (6262,202),
 (6263,202),
 (5858,2323),
 (5859,2323),
 (5860,3535),
 (5861,3535),
 (5862,4040),
 (5863,4040),
 (5959,4646),
 (5960,4646),
 (6767,6060),
 (6768,6060);

COMMIT;