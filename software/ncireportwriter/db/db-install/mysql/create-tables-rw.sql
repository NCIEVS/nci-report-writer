/*L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
L*/

-- Create Report Writer database
--
-- ------------------------------------------------------

--
-- Create schema reportwriter
--

CREATE DATABASE IF NOT EXISTS @database.name@;
USE @database.name@;

--
-- Definition of table `USER`
--

CREATE TABLE `USER` (
  `ID` bigint(20) NOT NULL,
  `LOGIN_NAME` varchar(100) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Definition of table `hi_value`
-- 090217, table name must be lowercase to support
-- hibernate's primary key generator (HILO)
--

CREATE TABLE `hi_value` (
  `next_value` int(10) unsigned NOT NULL auto_increment,
  PRIMARY KEY  (`next_value`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Definition of table `CUSTOMIZED_QUERY`
--

CREATE TABLE `CUSTOMIZED_QUERY` (
  `ID` int(11) NOT NULL,
  `QUERY_EXPRESSION` varchar(255) default NULL,
  `CODING_SCHEME_NAME` varchar(255) default NULL,
  `CODING_SCHEME_VERSION` varchar(255) default NULL,
  `CREATED_BY` bigint(20) default NULL,
  `LAST_MODIFIED` datetime default NULL,
  PRIMARY KEY  (`ID`),
  KEY `CREATED_BY` (`CREATED_BY`),
  CONSTRAINT `FK_CUSTOMIZED_QUERY_USER` FOREIGN KEY (`CREATED_BY`) REFERENCES `USER` (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Definition of table `REPORT_FORMAT`
--

CREATE TABLE `REPORT_FORMAT` (
  `ID` int(11) NOT NULL,
  `DESCRIPTION` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Definition of table `REPORT_STATUS`
--

CREATE TABLE `REPORT_STATUS` (
  `ID` int(11) NOT NULL,
  `LABEL` varchar(255) default NULL,
  `DESCRIPTION` varchar(255) default NULL,
  `ACTIVE` tinyint(1) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Definition of table `REPORT`
--

CREATE TABLE `REPORT` (
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
  CONSTRAINT `FK_REPORT_REPORT_FORMAT` FOREIGN KEY (`HAS_FORMAT`) REFERENCES `REPORT_FORMAT` (`ID`),
  CONSTRAINT `FK_REPORT_REPORT_STATUS` FOREIGN KEY (`HAS_STATUS`) REFERENCES `REPORT_STATUS` (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Definition of table `CUSTOMIZED_REPORT`
--

CREATE TABLE `CUSTOMIZED_REPORT` (
  `REPORT_ID` int(11) NOT NULL,
  `BASED_ON` int(11) default NULL,
  PRIMARY KEY  (`REPORT_ID`),
  KEY `BASED_ON` (`BASED_ON`),
  KEY `REPORT_ID` (`REPORT_ID`),
  CONSTRAINT `FK_CUSTOMIZED_REPORT_CUSTOMIZED_QUERY` FOREIGN KEY (`BASED_ON`) REFERENCES `CUSTOMIZED_QUERY` (`ID`),
  CONSTRAINT `FK_CUSTOMIZED_REPORT_REPORT` FOREIGN KEY (`REPORT_ID`) REFERENCES `REPORT` (`ID`) ON DELETE CASCADE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Definition of table `STANDARD_REPORT_TEMPLATE`
--

CREATE TABLE `STANDARD_REPORT_TEMPLATE` (
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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Definition of table `REPORT_COLUMN`
--

CREATE TABLE `REPORT_COLUMN` (
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
  CONSTRAINT `FK_REPORT_COLUMN_STANDARD_REPORT_TEMPLATE` FOREIGN KEY (`BELONGS_TO`) REFERENCES `STANDARD_REPORT_TEMPLATE` (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Definition of table `STANDARD_REPORT`
--

CREATE TABLE `STANDARD_REPORT` (
  `REPORT_ID` int(11) NOT NULL,
  `BASED_ON_TEMPLATE` int(11) default NULL,
  PRIMARY KEY  (`REPORT_ID`),
  KEY `REPORT_ID` (`REPORT_ID`),
  KEY `BASED_ON_TEMPLATE` (`BASED_ON_TEMPLATE`),
  CONSTRAINT `FK_STANDARD_REPORT_REPORT` FOREIGN KEY (`REPORT_ID`) REFERENCES `REPORT` (`ID`),
  CONSTRAINT `FK_STANDARD_REPORT_STANDARD_REPORT_TEMPLATE` FOREIGN KEY (`BASED_ON_TEMPLATE`) REFERENCES `STANDARD_REPORT_TEMPLATE` (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

COMMIT;