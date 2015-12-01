@echo off
rem Variables que caldra configurar per cada PC
rem set JDK118_HOME=c:\java\jdk1.1.8
set JDK118_HOME=c:\java\jdk1.3.1_09
set JDK13_HOME=c:\java\jdk1.3.1_09
set JDK14_HOME=C:\java\j2sdk1.4.2_04\
set CLASSPATH118=%JDK_HOME118%\lib\classes.jar
set CLASSPATH13=%JDK_HOME%\lib\tools.jar

set DEFAULT_JDK_HOME=%JDK118_HOME%
set DEFAULT_CLASSPATH=%CLASSPATH118%

echo JDK=%DEFAULT_JDK_HOME%


rem Referencies relatives. No es necessari configurar els seus valors
set QV_WEBAPP=..\..\..\web
set APPL_DIST=%QV_WEBAPP%\appl
set LIB_DIR=..\..\lib
set BUILD_DIR=build

if exist "%BUILD_DIR%" goto init_dist_dir
mkdir %BUILD_DIR%

:init_dist_dir
cd %BUILD_DIR%
if exist "%APPL_DIST%" goto build_jars
mkdir %APPL_DIST%

:build_jars
cd ..
echo Building order applet...
rem %JDK118_HOME%\bin\javac -d %BUILD_DIR% -classpath %JDK118_HOME%\lib\classes.zip *Ord*.java ImageLoaderThread.java ImageObserver.java QVAppletJS.java
%DEFAULT_JDK_HOME%\bin\javac -d %BUILD_DIR% -classpath %DEFAULT_CLASSPATH% *Ord*.java ImageLoaderThread.java ImageObserver.java QVAppletJS.java
cd %BUILD_DIR%
%DEFAULT_JDK_HOME%\bin\jar cvf %APPL_DIST%\QVOrdering.jar *Ord*.class ImageLoaderThread.class ImageObserver.class QVAppletJS.class
cd..
echo. 
