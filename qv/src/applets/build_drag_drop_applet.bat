@echo off
rem Variables que caldra configurar per cada PC
set JDK118_HOME=c:\java\jdk1.1.8
set JDK_HOME=c:\java\jdk1.3.1_09

rem Referencies relatives. No es necessari configurar els seus valors
set QV_WEBAPP=..\..\..\web
set APPL_DIST=%QV_WEBAPP%\appl
set APPL_DIST=.
set LIB_DIR=%QV_WEBAPP%\WEB-INF\lib
set BUILD_DIR=build
set QV_KEYSTORE="C:\Documents and Settings\sarjona\.keystore\qv.keystore"

if exist "%BUILD_DIR%" goto init_dist_dir
mkdir %BUILD_DIR%

:init_dist_dir
cd %BUILD_DIR%
if exist "%APPL_DIST%" goto build_jars
mkdir %APPL_DIST%

:build_jars

echo Building drag and drop applet...
cd ..
%JDK_HOME%\bin\javac -d %BUILD_DIR% -classpath %JDK_HOME%\lib\tools.jar QVDragDrop*.java ImageLoaderThread.java ImageObserver.java Source.java Target.java QVHotspotLabelJS.java QVRenderHotspotAppletJS.java QVAppletJS.java
copy *.gif %BUILD_DIR%
cd %BUILD_DIR%
%JDK_HOME%\bin\jar cvf %APPL_DIST%\QVDragDrop.jar QVDragDrop*.class ImageLoaderThread.class ImageObserver.class Source.class Target.class QVHotspotLabelJS.class QVRenderHotspotAppletJS.class QVAppletJS.class *.gif
%JDK_HOME%\bin\jarsigner -keystore %QV_KEYSTORE% -storepass virtuals %APPL_DIST%\QVDragDrop.jar qv
cd..
echo. 

