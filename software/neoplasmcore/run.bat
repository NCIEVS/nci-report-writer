set OCP=%CLASSPATH%
set lib1=..\ncireportwriter\lib
set lib2=..\ncireportwriter\extlib
set lib3=lib
set lib4=extlib
set CLASSPATH=.
set CLASSPATH=%CLASSPATH%;%lib1%\*
set CLASSPATH=%CLASSPATH%;%lib2%\*
set CLASSPATH=%CLASSPATH%;%lib3%\*
set CLASSPATH=%CLASSPATH%;%lib4%\*
copy data\*.* .
E:\jdk1.7.0_05\bin\java -Xmx1300m -classpath %CLASSPATH% gov.nih.nci.evs.app.neopl.NeoplasmCorePackageGenerator ThesaurusInferred_16.12d.owl Neoplasm_Core_16.12d.xls false true true
set CLASSPATH=%OCP%

