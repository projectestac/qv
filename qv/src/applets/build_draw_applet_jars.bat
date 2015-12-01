@echo off
rem Variables que caldra configurar per cada PC
set JDK118_HOME=c:\Java\jdk1.1.8
rem set JDK_HOME=c:\Java\jdk1.3.1_09

rem XTEC
set JDK_HOME=c:\java\j2sdk1.4.2_04

rem Sara
set JDK_HOME=C:\Java\j2sdk1.4.2_10

rem Referencies relatives. No es necessari configurar els seus valors
set QV_WEBAPP=..\..\webApp
rem set APPL_DIST=%QV_WEBAPP%\appl
set APPL_DIST=sara
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
echo Building draw applet...
%JDK_HOME%\bin\javac -d %BUILD_DIR% -classpath ..\..\lib\jaws.jar;%JDK_HOME%\lib\classes.zip;..\..\lib\svg_generat.jar;..\..\lib\xalan.jar edu/xtec/qv/applet/*.java
if exist "%BUILD_DIR%" goto create_jar
mkdir %BUILD_DIR%\edu\xtec\resources
:create_jar
echo copy edu\xtec\resources\* %BUILD_DIR%\edu\xtec\resources
copy edu\xtec\resources\* %BUILD_DIR%\edu\xtec\resources
cd %BUILD_DIR%
%JDK_HOME%\bin\jar cvf %APPL_DIST%\QVDrawer.jar *
cd..
echo. 
