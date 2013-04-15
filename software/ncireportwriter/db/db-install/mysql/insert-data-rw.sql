/*L
  Copyright Northrop Grumman Information Technology.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
L*/

-- ----------------------------------------------------------------------------
-- Create Report Writer database
-- Note: Please do not replace any values with @ prefix and @postfix in them
--   (For example: @database.name@).  These values are set during our 
--   build and deployment.
-- ----------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Insert reportwriter data to a specific database.
-- Note: Please do not replace any @VALUE@.
-- ----------------------------------------------------------------------------
USE @database.name@;

-- ----------------------------------------------------------------------------
-- Data for table `USER`
-- Note: Please do not replace any @VALUE@.
-- ----------------------------------------------------------------------------
INSERT INTO `USER` (`ID`,`LOGIN_NAME`) VALUES
	(101,'@rw.admin.user.name@'),
	(102,'@rw.user.name@');

-- ----------------------------------------------------------------------------
-- Data for table `hi_value`
-- Note: Need to set hi_value whenever you manually add rows to a table.
-- ----------------------------------------------------------------------------
INSERT INTO `hi_value` (`next_value`) VALUES
	(110);

-- ----------------------------------------------------------------------------
-- Data for table `REPORT_FORMAT`
-- ----------------------------------------------------------------------------
INSERT INTO `REPORT_FORMAT` (`ID`,`DESCRIPTION`) VALUES
	(404,'Text (tab delimited)'),
	(405,'Microsoft Office Excel'),
	(406,'HyperText Markup Language');

-- ----------------------------------------------------------------------------
-- Data for table `REPORT_STATUS`
-- ----------------------------------------------------------------------------
INSERT INTO `REPORT_STATUS` (`ID`,`LABEL`,`DESCRIPTION`,`ACTIVE`) VALUES
	(505,'DRAFT','REPORT is a draft, not ready for download.',1),
	(506,'APPROVED','REPORT has been approved for download by USERs',1);

-- ----------------------------------------------------------------------------
-- Data for table `STANDARD_REPORT_TEMPLATE`
-- ----------------------------------------------------------------------------
INSERT INTO `standard_report_template` (`ID`, `LABEL`, `ROOT_CONCEPT_CODE`, `ASSOCIATION_NAME`, `DIRECTION`, `CODING_SCHEME_NAME`, `CODING_SCHEME_VERSION`, `LEVEL`, `DELIMITER`) VALUES
	(202, 'FDA-UNII Subset REPORT', 'C63923', 'Concept_In_Subset', 0, 'NCI Thesaurus', '11.11d', -1, '$'),
	(2323, 'Individual Case Safety (ICS) Subset REPORT', 'C54447', 'Concept_In_Subset', 0, 'NCI Thesaurus', '11.10e', -1, '$'),
	(3535, 'Structured Product Labeling (SPL) REPORT', 'C54452', 'Concept_In_Subset', 0, 'NCI Thesaurus', '11.10e', -1, '$'),
	(4040, 'CDISC Subset REPORT', 'C61410', 'Concept_In_Subset', 0, 'NCI Thesaurus', '11.09d', -1, '$'),
	(4646, 'CDRH Subset REPORT', 'C62596', 'Concept_In_Subset', 0, 'NCI Thesaurus', '11.11d', -1, '$'),
	(6060, 'FDA-SPL Country Code REPORT', 'Semantic_Type|null|null|null|Geographic Area|exactMatch', 'Concept_In_Subset', 0, 'NCI Thesaurus', '11.09d', -1, '$'),
	(6868, 'CDISC SDTM Terminology (Orig)', 'C61410', 'Concept_In_Subset', 0, 'NCI Thesaurus', '10.11e', -1, '$'),
	(7272, 'NICHD Subset Report', 'C90259', 'Concept_In_Subset', 0, 'NCI Thesaurus', '11.10e', -1, '$'),
	(7474, 'CDISC ADaM Terminology', 'C81222', 'Concept_In_Subset', 0, 'NCI Thesaurus', '11.09d', -1, '$'),
	(7676, 'CDISC SEND Terminology', 'C77526', 'Concept_In_Subset', 0, 'NCI Thesaurus', '11.09d', -1, '$'),
	(7878, 'CDISC CDASH Terminology', 'C77527', 'Concept_In_Subset', 0, 'NCI Thesaurus', '11.09d', -1, '$'),
	(8080, 'CDISC SDTM Terminology', 'C66830', 'Concept_In_Subset', 0, 'NCI Thesaurus', '11.09d', -1, '$'),
	(8282, 'NCPDP Subset', 'C89415', 'Concept_In_Subset', 0, 'NCI Thesaurus', '11.09d', -1, '$'),
	(10100, 'eStability Subset Report', 'C96069', 'Concept_In_Subset', 0, 'NCI Thesaurus', '11.10e', -1, '$');

-- ----------------------------------------------------------------------------
-- Data for table `REPORT_COLUMN`
-- ----------------------------------------------------------------------------
INSERT INTO `report_column` (`ID`, `COLUMN_NUMBER`, `LABEL`, `FIELD_ID`, `PROPERTY_TYPE`, `PROPERTY_NAME`, `IS_PREFERRED`, `REPRESENTATIONAL_FORM`, `SOURCE`, `QUALIFIER_NAME`, `QUALIFIER_VALUE`, `DELIMITER`, `CONDITIONAL_COLUMN`, `BELONGS_TO`) VALUES
	(303, 1, 'FDA UNII Code (use for SPL)', 'Property', 'Generic', 'FDA_UNII_Code', NULL, '', ' ', '', '', '|', -1, 202),
	(304, 2, 'FDA Preferred Term', 'Property', 'Presentation', 'FULL_SYN', NULL, 'PT', 'FDA', '', '', '|', -1, 202),
	(1212, 3, 'NCI Concept Code', 'Code', '', '', NULL, '', ' ', '', '', '|', -1, 202),
	(2929, 1, 'Source', 'Property', 'GENERIC', 'Contributing_Source', NULL, '', ' ', '', '', '|', -1, 2323),
	(2930, 2, 'Subset Code', 'Associated Concept Code', '', '', NULL, '', ' ', '', '', '|', -1, 2323),
	(2931, 3, 'Subset Name', 'Associated Concept Property', 'PRESENTATION', 'Preferred_Name', NULL, '', ' ', '', '', '|', -1, 2323),
	(3131, 4, 'Concept Code', 'Code', '', '', NULL, '', ' ', '', '', '|', -1, 2323),
	(3132, 5, 'Source PT', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'FDA', 'subsource-name', 'ICSR', '|', -1, 2323),
	(9696, 7, 'Source Definition', 'Property', 'DEFINITION', 'ALT_DEFINITION', NULL, '', 'FDA', 'attr', 'ICSR', '|', -1, 2323),
	(3334, 6, 'Source Synonym(s)', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'SY', 'FDA', 'subsource-name', 'ICSR', '|', -1, 2323),
	(3636, 1, 'Source', 'Property', 'GENERIC', 'Contributing_Source', NULL, '', ' ', '', '', '|', -1, 3535),
	(3637, 2, 'Subset Code', 'Associated Concept Code', '', '', NULL, '', ' ', '', '', '|', -1, 3535),
	(3638, 3, 'Subset Name', 'Associated Concept Property', 'PRESENTATION', 'Preferred_Name', NULL, '', ' ', '', '', '|', -1, 3535),
	(3639, 4, 'Concept Code', 'Code', '', '', NULL, '', ' ', '', '', '|', -1, 3535),
	(3640, 5, 'Source PT', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'FDA', 'subsource-name', 'SPL', '|', -1, 3535),
	(3641, 6, 'Source Definition', 'Property', 'DEFINITION', 'ALT_DEFINITION', NULL, '', 'FDA', '', '', '|', -1, 3535),
	(3642, 7, 'Source Synonym(s)', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'SY', 'FDA', 'subsource-name', 'SPL', '|', -1, 3535),
	(4141, 1, 'Source', 'Property', 'GENERIC', 'Contributing_Source', NULL, '', ' ', '', '', '|', -1, 4040),
	(4142, 2, 'Subset Code', 'Associated Concept Code', '', '', NULL, '', ' ', '', '', '|', -1, 4040),
	(4143, 3, 'Subset Name', 'Associated Concept Property', 'PRESENTATION', 'Preferred_Name', NULL, '', ' ', '', '', '|', -1, 4040),
	(4144, 4, 'Concept Code', 'Code', '', '', NULL, '', ' ', '', '', '|', -1, 4040),
	(4145, 5, 'Source PT', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'CDISC', '', '', '|', -1, 4040),
	(4146, 6, 'Source PT Code', 'Property', 'PRESENTATION', 'Preferred_Name', NULL, '', '', '', '', '|', -1, 4040),
	(4147, 7, 'Source Definition', 'Property', 'DEFINITION', 'ALT_DEFINITION', NULL, '', 'CDISC', '', '', '|', -1, 4040),
	(4148, 8, 'Source Synonym(s)', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'SY', 'CDISC', '', '', '|', -1, 4040),
	(4747, 1, 'Source', 'Property', 'GENERIC', 'Contributing_Source', NULL, '', ' ', '', '', '|', -1, 4646),
	(4748, 2, 'NCIt Subset Code', 'Associated Concept Code', '', '', NULL, '', ' ', '', '', '|', -1, 4646),
	(4749, 3, 'FDA Subset Name', 'Associated Concept Property', 'PRESENTATION', 'Preferred_Name', NULL, '', ' ', '', '', '|', -1, 4646),
	(4750, 4, 'NCIt Concept Code', 'Code', '', '', NULL, '', ' ', '', '', '|', -1, 4646),
	(4848, 5, 'FDA Source Code', 'Property Qualifier', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'FDA', 'subsource-name', 'CDRH', '|', -1, 4646),
	(4849, 6, 'FDA Source PT', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'FDA', 'subsource-name', 'CDRH', '|', -1, 4646),
	(4850, 7, 'FDA Source Synonym(s)', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'SY', 'FDA', 'subsource-name', 'CDRH', '|', -1, 4646),
	(4851, 8, 'FDA Source Definition', 'Property', 'DEFINITION', 'ALT_DEFINITION', NULL, '', 'FDA', '', '', '|', -1, 4646),
	(4852, 9, 'NCIt Definition', 'Property', 'DEFINITION', 'DEFINITION', NULL, '', ' ', '', '', '|', -1, 4646),
	(4853, 10, 'Parent Concept\'s NCIt Concept Code', '1st CDRH Parent Code', '', '', NULL, '', '', '', '', '|', -1, 4646),
	(4854, 11, 'Parent Concept\'s FDA Source Code', '1st CDRH Parent Property Qualifier', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'FDA', 'source-code', '', '|', -1, 4646),
	(4855, 12, 'Parent Concept\'s FDA Source PT', '1st CDRH Parent Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'FDA', 'subsource-name', 'CDRH', '|', -1, 4646),
	(6161, 1, 'ISO Code', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'CA3', 'NCI', '', '', '|', -1, 6060),
	(6162, 2, 'NCI Concept Code', 'Code', '', '', NULL, '', ' ', '', '', '|', -1, 6060),
	(6163, 3, 'NCI Preferred Term', 'Property', 'PRESENTATION', 'Preferred_Name', NULL, '', ' ', '', '', '|', -1, 6060),
	(6969, 1, 'Code', 'Code', '', '', NULL, '', '', '', '', '|', -1, 6868),
	(6970, 2, 'Codelist Code', 'Associated Concept Code', '', '', NULL, '', '', '', '', '|', -1, 6868),
	(6971, 3, 'Codelist Extensible (Yes/No)', 'Associated Concept Property', 'GENERIC', 'Extensible_List', NULL, '', '', '', '', '|', -1, 6868),
	(6972, 4, 'Codelist Name', 'Associated Concept Property', 'PRESENTATION', 'FULL_SYN', NULL, 'SY', 'CDISC', '', '', '|', -1, 6868),
	(6973, 5, 'CDISC Submission Value', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'CDISC', '', '', '|', -1, 6868),
	(6974, 6, 'CDISC Preferred Term', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'CDISC', '', '', '|', -1, 6868),
	(6975, 7, 'CDISC Symonym(s)', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'SY', 'CDISC', '', '', '|', -1, 6868),
	(6976, 8, 'CDISC Definition', 'Property', 'DEFINITION', 'ALT_DEFINITION', NULL, '', 'CDISC', '', '', '|', -1, 6868),
	(6977, 9, 'NCI Preferred Term', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'NCI', '', '', '|', -1, 6868),
	(7373, 1, 'NCIt Code', 'Code', '', '', NULL, '', '', '', '', '|', -1, 7272),
	(7374, 2, 'NCIt PT', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'NCI', '', '', '|', -1, 7272),
	(7375, 3, 'NICHD PT', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'NICHD', '', '', '|', -1, 7272),
	(7376, 4, 'NICHD SY', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'SY', 'NICHD', '', '', '|', -1, 7272),
	(7377, 5, 'NCIt Definition', 'Property', 'DEFINITION', 'DEFINITION', NULL, '', '', '', '', '|', -1, 7272),
	(7378, 6, 'NCIt Code of NICHD Parent', '1st NICHD Parent Code', '', '', NULL, '', '', '', '', '|', -1, 7272),
	(7379, 7, 'NICHD Parent PT', '1st NICHD Parent Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'NICHD', '', '', '|', -1, 7272),
	(7380, 8, 'NCIt Code of second NICHD Parent', '2nd NICHD Parent Code', '', '', NULL, '', '', '', '', '|', -1, 7272),
	(7381, 9, 'NICHD second Parent PT', '2nd NICHD Parent Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'NICHD', '', '', '|', -1, 7272),
	(7382, 10, 'NCIt Code of NICHD Subset', 'Associated Concept Code', '', '', NULL, '', '', '', '', '|', -1, 7272),
	(7383, 11, 'NICHD PT of NICHD Subset', 'Associated Concept Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'NICHD', '', '', '|', -1, 7272),
	(7501, 1, 'Code', 'Code', '', '', NULL, '', '', '', '', '|', -1, 7474),
	(7502, 2, 'Codelist Code', 'Associated Concept Code', '', '', NULL, '', '', '', '', '|', -1, 7474),
	(7503, 3, 'Codelist Extensible (Yes/No)', 'Associated Concept Property', 'GENERIC', 'Extensible_List', NULL, '', '', '', '', '|', -1, 7474),
	(7504, 4, 'Codelist Name', 'Associated Concept Property', 'PRESENTATION', 'FULL_SYN', NULL, 'SY', 'CDISC', '', '', '|', -1, 7474),
	(7505, 5, 'CDISC Submission Value', 'CDISC Submission Value', 'PRESENTATION', 'FULL_SYN', NULL, '', 'CDISC', '', '', '|', -1, 7474),
	(7506, 6, 'CDISC Synonym(s)', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'SY', 'CDISC', '', '', '|', -1, 7474),
	(7507, 7, 'CDISC Definition', 'Property', 'DEFINITION', 'ALT_DEFINITION', NULL, '', 'CDISC', '', '', '|', -1, 7474),
	(7508, 8, 'NCI Preferred Term', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'NCI', '', '', '|', -1, 7474),
	(7701, 1, 'Code', 'Code', '', '', NULL, '', '', '', '', '|', -1, 7676),
	(7702, 2, 'Codelist Code', 'Associated Concept Code', '', '', NULL, '', '', '', '', '|', -1, 7676),
	(7703, 3, 'Codelist Extensible (Yes/No)', 'Associated Concept Property', 'GENERIC', 'Extensible_List', NULL, '', '', '', '', '|', -1, 7676),
	(7704, 4, 'Codelist Name', 'Associated Concept Property', 'PRESENTATION', 'FULL_SYN', NULL, 'SY', 'CDISC', '', '', '|', -1, 7676),
	(7705, 5, 'CDISC Submission Value', 'CDISC Submission Value', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'CDISC', '', '', '|', -1, 7676),
	(7706, 6, 'CDISC Synonym(s)', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'SY', 'CDISC', '', '', '|', -1, 7676),
	(7707, 7, 'CDISC Definition', 'Property', 'DEFINITION', 'ALT_DEFINITION', NULL, '', 'CDISC', '', '', '|', -1, 7676),
	(7708, 8, 'NCI Preferred Term', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'NCI', '', '', '|', -1, 7676),
	(7901, 1, 'Code', 'Code', '', '', NULL, '', '', '', '', '|', -1, 7878),
	(7902, 2, 'Codelist Code', 'Associated Concept Code', '', '', NULL, '', '', '', '', '|', -1, 7878),
	(7903, 3, 'Codelist Extensible (Yes/No)', 'Associated Concept Property', 'GENERIC', 'Extensible_List', NULL, '', '', '', '', '|', -1, 7878),
	(7904, 4, 'Codelist Name', 'Associated Concept Property', 'PRESENTATION', 'FULL_SYN', NULL, 'SY', 'CDISC', '', '', '|', -1, 7878),
	(7905, 5, 'CDISC Submission Value', 'CDISC Submission Value', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'CDISC', '', '', '|', -1, 7878),
	(7906, 6, 'CDISC Synonym(s)', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'SY', 'CDISC', '', '', '|', -1, 7878),
	(7907, 7, 'CDISC Definition', 'Property', 'DEFINITION', 'ALT_DEFINITION', NULL, '', 'CDISC', '', '', '|', -1, 7878),
	(7908, 8, 'NCI Preferred Term', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'NCI', '', '', '|', -1, 7878),
	(8101, 1, 'Code', 'Code', '', '', NULL, '', '', '', '', '|', -1, 8080),
	(8102, 2, 'Codelist Code', 'Associated Concept Code', '', '', NULL, '', '', '', '', '|', -1, 8080),
	(8103, 3, 'Codelist Extensible (Yes/No)', 'Associated Concept Property', 'GENERIC', 'Extensible_List', NULL, '', '', '', '', '|', -1, 8080),
	(8104, 4, 'Codelist Name', 'Associated Concept Property', 'PRESENTATION', 'FULL_SYN', NULL, 'SY', 'CDISC', '', '', '|', -1, 8080),
	(8105, 5, 'CDISC Submission Value', 'CDISC Submission Value', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'CDISC', '', '', '|', -1, 8080),
	(8106, 6, 'CDISC Synonym(s)', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'SY', 'CDISC', '', '', '|', -1, 8080),
	(8107, 7, 'CDISC Definition', 'Property', 'DEFINITION', 'ALT_DEFINITION', NULL, '', 'CDISC', '', '', '|', -1, 8080),
	(8108, 8, 'NCI Preferred Term', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'NCI', '', '', '|', -1, 8080),
	(8301, 1, 'NCIt Subset Code', 'Associated Concept Code', '', '', NULL, '', '', '', '', '|', -1, 8282),
	(8302, 2, 'NCPDP Subset Preferred Term', 'Associated Concept Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'NCPDP', '', '', '|', -1, 8282),
	(8303, 3, 'NCIt Code', 'Code', '', '', NULL, '', '', '', '', '|', -1, 8282),
	(8304, 4, 'NCPDP Preferred Term', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'NCPDP', '', '', '|', -1, 8282),
	(8305, 5, 'NCIt Preferred Term', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'NCI', '', '', '|', -1, 8282),
	(8306, 6, 'NCIt Definition', 'Property', 'DEFINITION', 'DEFINITION', NULL, '', 'NCI', '', '', '|', -1, 8282),
	(9494, 1, 'NCIt Subset Code', 'Associated Concept Code', '', '', NULL, '', '', '', '', '|', -1, NULL),
	(9495, 2, 'NCIt Subset Preferred Term', 'Associated Concept Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'FDA', 'subsource-name', 'Stability', '|', -1, NULL),
	(9496, 3, 'NCIt Concept Code', 'Code', '', '', NULL, '', '', '', '', '|', -1, NULL),
	(9497, 4, 'NCIt Definition', 'Property', 'DEFINITION', 'DEFINITION', NULL, '', 'NCI', '', '', '|', -1, NULL),
	(9498, 5, 'Stability Preferred Term', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'FDA', 'subsource-name', 'Stability', '|', -1, NULL),
	(9499, 6, 'Stability Definition', 'Property', 'DEFINITION', 'ALT_DEFINITION', NULL, '', 'FDA', 'attr', 'Stability', '|', -1, NULL),
	(10201, 1, 'NCIt Subset Code', 'Associated Concept Code', '', '', NULL, '', '', '', '', '|', -1, 10100),
	(10202, 2, 'NCIt Subset Preferred Term', 'Associated Concept Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'FDA', 'subsource-name', 'Stability', '|', -1, 10100),
	(10203, 3, 'NCIt Concept Code', 'Code', '', '', NULL, '', '', '', '', '|', -1, 10100),
	(10204, 4, 'NCIt Preferred Term', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'NCI', '', '', '|', -1, 10100),
	(10205, 5, 'NCIt Definition', 'Property', 'DEFINITION', 'DEFINITION', NULL, '', 'NCI', '', '', '|', -1, 10100),
	(10206, 6, 'Stability Preferred Term', 'Property', 'PRESENTATION', 'FULL_SYN', NULL, 'PT', 'FDA', 'subsource-name', 'Stability', '|', -1, 10100),
	(10207, 7, 'Stability Definition', 'Property', 'DEFINITION', 'ALT_DEFINITION', NULL, '', 'FDA', 'attr', 'Stability', '|', -1, 10100);

-- ----------------------------------------------------------------------------
-- Data for table `REPORT`
-- ----------------------------------------------------------------------------
INSERT INTO `report` (`ID`, `LABEL`, `LAST_MODIFIED`, `PATH_NAME`, `HAS_FORMAT`, `HAS_STATUS`, `MODIFIED_BY`, `CREATED_BY`) VALUES
	(10911, 'Structured Product Labeling (SPL) REPORT.txt', NULL, '', 404, 505, NULL, 101),
	(10912, 'Structured Product Labeling (SPL) REPORT.xls', NULL, '', 405, 505, NULL, 101),
	(5862, 'CDISC Subset REPORT .txt', NULL, '', 404, 505, NULL, 101),
	(5863, 'CDISC Subset REPORT .xls', NULL, '', 405, 505, NULL, 101),
	(11015, 'FDA-UNII Subset REPORT.txt', NULL, '', 404, 505, NULL, 101),
	(11016, 'FDA-UNII Subset REPORT.xls', NULL, '', 405, 505, NULL, 101),
	(8900, 'FDA-SPL Country Code REPORT.txt', NULL, '', 404, 505, NULL, 101),
	(8901, 'FDA-SPL Country Code REPORT.xls', NULL, '', 405, 505, NULL, 101),
	(10807, 'CDRH Subset REPORT.txt', NULL, '', 404, 505, NULL, 101),
	(10808, 'CDRH Subset REPORT.xls', NULL, '', 405, 505, NULL, 101),
	(10809, 'CDRH Subset REPORT.htm', NULL, '', 406, 505, NULL, 101),
	(10917, 'NICHD Subset Report.txt', NULL, '', 404, 505, NULL, 101),
	(10918, 'NICHD Subset Report.xls', NULL, '', 405, 505, NULL, 101),
	(10919, 'NICHD Subset Report.htm', NULL, '', 406, 505, NULL, 101),
	(10323, 'CDISC SDTM Terminology.txt', NULL, '', 404, 505, NULL, 101),
	(10324, 'CDISC SDTM Terminology.xls', NULL, '', 405, 505, NULL, 101),
	(10325, 'CDISC SDTM Terminology.htm', NULL, '', 406, 505, NULL, 101),
	(10302, 'CDISC ADaM Terminology.txt', NULL, '', 404, 505, NULL, 101),
	(10303, 'CDISC ADaM Terminology.xls', NULL, '', 405, 505, NULL, 101),
	(10304, 'CDISC ADaM Terminology.htm', NULL, '', 406, 505, NULL, 101),
	(10335, 'CDISC CDASH Terminology.txt', NULL, '', 404, 505, NULL, 101),
	(10336, 'CDISC CDASH Terminology.xls', NULL, '', 405, 505, NULL, 101),
	(10337, 'CDISC CDASH Terminology.htm', NULL, '', 406, 505, NULL, 101),
	(8902, 'FDA-SPL Country Code REPORT.htm', NULL, '', 406, 505, NULL, 101),
	(10908, 'Individual Case Safety (ICS) Subset REPORT.txt', NULL, '', 404, 505, NULL, 101),
	(10909, 'Individual Case Safety (ICS) Subset REPORT.xls', NULL, '', 405, 505, NULL, 101),
	(10910, 'Individual Case Safety (ICS) Subset REPORT.htm', NULL, '', 406, 505, NULL, 101),
	(10611, 'NCPDP Subset.txt', NULL, '', 404, 505, NULL, 101),
	(10612, 'NCPDP Subset.xls', NULL, '', 405, 505, NULL, 101),
	(10613, 'NCPDP Subset.htm', NULL, '', 406, 505, NULL, 101),
	(10913, 'Structured Product Labeling (SPL) REPORT.htm', NULL, '', 406, 505, NULL, 101),
	(10320, 'CDISC SEND Terminology.txt', NULL, '', 404, 505, NULL, 101),
	(10321, 'CDISC SEND Terminology.xls', NULL, '', 405, 505, NULL, 101),
	(10322, 'CDISC SEND Terminology.htm', NULL, '', 406, 505, NULL, 101),
	(11017, 'FDA-UNII Subset REPORT.htm', NULL, '', 406, 505, NULL, 101),
	(10914, 'eStability Subset Report.txt', NULL, '', 404, 505, NULL, 101),
	(10915, 'eStability Subset Report.xls', NULL, '', 405, 505, NULL, 101),
	(10916, 'eStability Subset Report.htm', NULL, '', 406, 505, NULL, 101);

-- ----------------------------------------------------------------------------
-- Data for table `STANDARD_REPORT`
-- ----------------------------------------------------------------------------
INSERT INTO `STANDARD_REPORT` (`REPORT_ID`,`BASED_ON_TEMPLATE`) VALUES
	(11015, 202),
	(11016, 202),
	(10908, 2323),
	(10909, 2323),
	(10911, 3535),
	(10912, 3535),
	(5862, 4040),
	(5863, 4040),
	(10807, 4646),
	(10808, 4646),
	(8900, 6060),
	(8901, 6060),
	(10809, 4646),
	(10917, 7272),
	(10918, 7272),
	(10919, 7272),
	(10323, 8080),
	(10324, 8080),
	(10325, 8080),
	(10302, 7474),
	(10303, 7474),
	(10304, 7474),
	(10335, 7878),
	(10336, 7878),
	(10337, 7878),
	(8902, 6060),
	(10910, 2323),
	(10611, 8282),
	(10612, 8282),
	(10613, 8282),
	(10913, 3535),
	(10320, 7676),
	(10321, 7676),
	(10322, 7676),
	(11017, 202),
	(10914, 10100),
	(10915, 10100),
	(10916, 10100);

-- ----------------------------------------------------------------------------
COMMIT;