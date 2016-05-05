How to run the NCI ReportWriter without UI.
(Reference: [EVSREPORT-94] Generate reports without the UI.)

Sample program for running the NCI ReportWriter without UI is available at:
<report writer installation dir>\software\ncireportwriter\test\src\java\gov\nih\nci\evs\reportwriter\test

Folder structure:

<Root Folder where the StandaloneReportGenerator will be running>
|   compile.bat
|   run.bat
|   log4j.xml
|   template.dat
|   ReportGenerationRunner$ReportFormatType.class
|   ncireportwriter.properties
|   StandaloneReportGenerator.java
|   StandaloneReportGenerator.class
|   
+---config
|       application-config-client-local.xml
|       application-config-client-remote.xml
|       application-config-client.xml
|       application-config.xml
|       ApplicationSecurityConfig.xml
|       context.xml
|       hibernate.cfg.xml
|       
\---output
        FDA_Quality_of_Matrics_Terminology__16.04d.txt
        FDA_Quality_of_Matrics_Terminology__16.04d.xls
        FDA_Quality_of_Matrics_Terminology__16.04d.htm
        

==================================================================================
Sample compile.bat:

set OCP=%CLASSPATH%
set lib=<report writer installation dir>\software\build_ncirw
set lib1=<report writer installation dir>\software\ncireportwriter\extlib
set lib2=<report writer installation dir>\software\ncireportwriter\lib
set CLASSPATH=.
set CLASSPATH=%CLASSPATH%;%lib%\*
set CLASSPATH=%CLASSPATH%;%lib1%\*
set CLASSPATH=%CLASSPATH%;%lib2%\*
rem set CLASSPATH=%CLASSPATH%;%lib3%\*
javac -d . -classpath %CLASSPATH%  *.java
set CLASSPATH=%OCP%

==================================================================================
Sample run.bat:

set OCP=%CLASSPATH%
set lib=<report writer installation dir>\software\build_ncirw
set lib1=<report writer installation dir>\software\ncireportwriter\extlib
set lib2=<report writer installation dir>\software\ncireportwriter\lib
set CLASSPATH=.
set CLASSPATH=%CLASSPATH%;%lib%\*
set CLASSPATH=%CLASSPATH%;%lib1%\*
set CLASSPATH=%CLASSPATH%;%lib2%\*
java -Xmx1300m -classpath %CLASSPATH% -DLG_CONFIG_FILE="C:\LexEVS6.3.0.RC1\resources\config\lbconfig.props" gov.nih.nci.evs.reportwriter.test.StandaloneReportGenerator template.dat output
set CLASSPATH=%OCP%

==================================================================================
Sample Standard Report Template data file:

label:FDA Quality of Matrics Terminology
rootConceptCode:C123273
codingSchemeName:NCI_Thesaurus
codingSchemeVersion:16.04d
associationName:Concept_In_Subset
direction:source
level:ALL
delimiter:|
columnCollection:
	columnNumber:1
	label:Source
	fieldId:Associated Concept Property
	propertyType:GENERIC
	propertyName:Contributing_Source
	isPreferred:
	representationalForm:
	source:
	qualifierName:
	qualifierValue:
	delimiter:|
	conditionalColumnId:
	reportTemplate:
	
	columnNumber:2
	label:Subset Code
	fieldId:Associated Concept Code
	propertyType:
	propertyName:
	isPreferred:
	representationalForm:
	source:
	qualifierName:
	qualifierValue:
	delimiter:|
	conditionalColumnId:
	reportTemplate:

	columnNumber:3
	label:Subset Name
	fieldId:Associated Concept Property
	propertyType:PRESENTATION
	propertyName:Preferred_Name
	isPreferred:
	representationalForm:
	source:
	qualifierName:
	qualifierValue:
	delimiter:|
	conditionalColumnId:
	reportTemplate:
	
	columnNumber:4
	label:Concept Code
	fieldId:Code
	propertyType:
	propertyName:
	isPreferred:
	representationalForm:
	source:
	qualifierName:
	qualifierValue:
	delimiter:|
	conditionalColumnId:
	reportTemplate:

	columnNumber:5
	label:Source PT
	fieldId:Property
	propertyType:PRESENTATION
	propertyName:FULL_SYN
	isPreferred:
	representationalForm:PT
	source:FDA
	qualifierName:subsource-name
	qualifierValue:QM
	delimiter:|
	conditionalColumnId:
	reportTemplate:
	
	columnNumber:6
	label:Source Synonym(s)
	fieldId:Property
	propertyType:PRESENTATION
	propertyName:FULL_SYN
	isPreferred:
	representationalForm:SY
	source:FDA
	qualifierName:subsource-name
	qualifierValue:QM
	delimiter:|
	conditionalColumnId:
	reportTemplate:	


==================================================================================
Note: All file in the config directory can be copied from the 
<report writer installation dir>\software\ncireportwriter\conf

