#!/bin/csh -f

#------------------------------------------------------------------------------
set cp=.
set cp="$cp":./conf:../conf
set cp="$cp":classes
foreach jar (../lib/*.jar lib/*.jar)
  set cp="$cp":$jar
end
set cp="$cp":../build/web/WEB-INF/classes
setenv CLASSPATH $cp

#------------------------------------------------------------------------------
set java=$JAVA_HOME/bin/java
set class=gov.nih.nci.evs.reportwriter.test.lexevs.RemoteServerUtilTest
set args=(-propertyFile ~/apps/evs/ncireportwriter-webapp/conf/ncireportwriter.properties)
#set args=(-propertyFile /local/home/jboss45c/evs/ncireportwriter-webapp/conf/ncireportwriter.properties)

#------------------------------------------------------------------------------
$java $class $args
