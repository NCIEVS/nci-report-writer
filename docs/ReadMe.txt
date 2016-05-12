How to run the NCI ReportWriter without UI.
(Reference: [EVSREPORT-94] Generate reports without the UI.)

Use the following program to retrieve all report template available in the database:
<report writer installation dir>\software\ncireportwriter\test\src\java\gov\nih\nci\evs\reportwriter\test\StandardReportTemplateExporter.java
The command line parameter is the directory name where template files will be generated.
For example:
   java -Xmx1300m -classpath %CLASSPATH% -DLG_CONFIG_FILE="C:\LexEVS6.3.0.RC1\resources\config\lbconfig.props" gov.nih.nci.evs.reportwriter.test.StandardReportTemplateExporter templates_dir

NOTE: Change "C:\LexEVS6.3.0.RC1\resources\config\lbconfig.props" above to fit your LexEVS environment, 
      if you are running using LexEVS local runtime; i'e', local mode.

Then use the following program: 
<report writer installation dir>\software\ncireportwriter\test\src\java\gov\nih\nci\evs\reportwriter\test\StandaloneReportGenerator.java
to generate new reports in HTML, Excel, and ASCII formats. 
The program will accept two command line parameters. The first can either be a template file name, or the name of the folder containing
all template files. 

The structure of the folder where these two programs would be running is the following:
<Root Folder>
|   compile.bat
|   run.bat
|   log4j.xml
|   template.dat
|   ReportGenerationRunner$ReportFormatType.class
|   ncireportwriter.properties
|   StandaloneReportGenerator.java
|   hibernate.cfg.xml
|   
+---config
|       application-config-client-local.xml
|       application-config-client-remote.xml
|       application-config-client.xml
|       application-config.xml
|       ApplicationSecurityConfig.xml
|       context.xml
|       
\---template_dir

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
rem java -Xmx1300m -classpath %CLASSPATH% -DLG_CONFIG_FILE="C:\LexEVS6.3.0.RC1\resources\config\lbconfig.props" gov.nih.nci.evs.reportwriter.test.StandardReportTemplateExporter template_dir
java -Xmx1300m -classpath %CLASSPATH% -DLG_CONFIG_FILE="C:\LexEVS6.3.0.RC1\resources\config\lbconfig.props" gov.nih.nci.evs.reportwriter.test.StandaloneReportGenerator template.dat output
set CLASSPATH=%OCP%

rem NOTE: Change "C:\LexEVS6.3.0.RC1\resources\config\lbconfig.props" to fit your LexEVS environment, if you are running against LexEVS local runtime.

==================================================================================
Sample Standard Report Template data file:

label:CDISC ADaM Terminology
rootConceptCode:C81222
codingSchemeName:NCI Thesaurus
codingSchemeVersion:11.09d
associationName:Concept_In_Subset
direction:false
level:-1
delimiter:$
columnCollection:
	columnNumber:1
	label:Code
	fieldId:Code
	propertyType:
	propertyName:
	isPreferred:null
	representationalForm:
	source:
	qualifierName:
	qualifierValue:
	delimiter:|
	conditionalColumnId:-1

	columnNumber:2
	label:Codelist Code
	fieldId:Associated Concept Code
	propertyType:
	propertyName:
	isPreferred:null
	representationalForm:
	source:
	qualifierName:
	qualifierValue:
	delimiter:|
	conditionalColumnId:-1

	columnNumber:3
	label:Codelist Extensible (Yes/No)
	fieldId:Associated Concept Property
	propertyType:GENERIC
	propertyName:Extensible_List
	isPreferred:null
	representationalForm:
	source:
	qualifierName:
	qualifierValue:
	delimiter:|
	conditionalColumnId:-1

	columnNumber:4
	label:Codelist Name
	fieldId:Associated Concept Property
	propertyType:PRESENTATION
	propertyName:FULL_SYN
	isPreferred:null
	representationalForm:SY
	source:CDISC
	qualifierName:
	qualifierValue:
	delimiter:|
	conditionalColumnId:-1

	columnNumber:5
	label:CDISC Submission Value
	fieldId:CDISC Submission Value
	propertyType:PRESENTATION
	propertyName:FULL_SYN
	isPreferred:null
	representationalForm:
	source:CDISC
	qualifierName:
	qualifierValue:
	delimiter:|
	conditionalColumnId:-1

	columnNumber:6
	label:CDISC Synonym(s)
	fieldId:Property
	propertyType:PRESENTATION
	propertyName:FULL_SYN
	isPreferred:null
	representationalForm:SY
	source:CDISC
	qualifierName:
	qualifierValue:
	delimiter:|
	conditionalColumnId:-1

	columnNumber:7
	label:CDISC Definition
	fieldId:Property
	propertyType:DEFINITION
	propertyName:ALT_DEFINITION
	isPreferred:null
	representationalForm:
	source:CDISC
	qualifierName:
	qualifierValue:
	delimiter:|
	conditionalColumnId:-1

	columnNumber:8
	label:NCI Preferred Term
	fieldId:Property
	propertyType:PRESENTATION
	propertyName:FULL_SYN
	isPreferred:null
	representationalForm:PT
	source:NCI
	qualifierName:
	qualifierValue:
	delimiter:|
	conditionalColumnId:-1

==================================================================================
Note: All file in the config directory can be copied from the 
<report writer installation dir>\software\ncireportwriter\conf

Make sure the database connection parameters in hibernate.cfg are correct.

