@echo off

rem ---------------------------------------------------------------------------
setlocal enabledelayedexpansion
set ocp=%CLASSPATH%

set cp=.
set cp=%cp%;.\conf;..\conf
set cp=%cp%;classes
for %%x in (..\lib\*.jar lib\*.jar) do (
  set cp=!cp!;%%x
)
set cp=%cp%;..\build\web\WEB-INF\classes
set CLASSPATH=%cp%

set java=%JAVA_HOME%\bin\java
set javaArgs=-Xms256m -Xmx512m
set class=gov.nih.nci.evs.reportwriter.test.lexevs.DataUtilsTest
set class=gov.nih.nci.evs.reportwriter.test.lexevs.RemoteServerUtilTest
set class=gov.nih.nci.evs.reportwriter.test.lexevs.DistributedValueSetTest
set class=gov.nih.nci.evs.reportwriter.test.lexevs.Test
set args=-propertyFile C:/apps/evs/ncireportwriter-webapp/conf/ncireportwriter.properties

rem ---------------------------------------------------------------------------
@echo on
"%java%" %javaArgs% %class% %args%

@echo off
rem ---------------------------------------------------------------------------
set CLASSPATH=%ocp%
@echo on
