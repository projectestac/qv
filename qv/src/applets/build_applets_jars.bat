@echo off
rem Variables que caldra configurar per cada PC
rem set JDK118_HOME=c:\java\jdk1.1.8
set JDK118_HOME=c:\java\jdk1.3.1_09
set JDK_HOME=c:\java\jdk1.3.1_09

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
echo Building hotspot applet...
%JDK118_HOME%\bin\javac -d %BUILD_DIR% -classpath ..\..\lib\jaws.jar;%JDK118_HOME%\lib\classes.zip *JS*.java
cd %BUILD_DIR%
%JDK118_HOME%\bin\jar cvf %APPL_DIST%\QVHotspotAppletJS.jar *JS*.class
cd..
echo. 

echo Building drag and drop applet...
%JDK118_HOME%\bin\javac -d %BUILD_DIR% -classpath %JDK118_HOME%\lib\classes.zip QVDragDrop*.java ImageLoaderThread.java ImageObserver.java Source.java Target.java QVHotspotLabelJS.java QVRenderHotspotAppletJS.java QVAppletJS.java
cd %BUILD_DIR%
%JDK118_HOME%\bin\jar cvf %APPL_DIST%\QVDragDrop.jar QVDragDrop*.class ImageLoaderThread.class ImageObserver.class Source.class Target.class QVHotspotLabelJS.class QVRenderHotspotAppletJS.class QVAppletJS.class
rem %JDK_HOME%\bin\jarsigner -keystore ..\qv.keystore -storepass virtuals %APPL_DIST%\QVDragDrop.jar qv
cd..
echo. 

echo Building order applet...
%JDK118_HOME%\bin\javac -d %BUILD_DIR% -classpath %JDK118_HOME%\lib\classes.zip *Ord*.java ImageLoaderThread.java ImageObserver.java QVAppletJS.java
cd %BUILD_DIR%
%JDK118_HOME%\bin\jar cvf %APPL_DIST%\QVOrdering.jar *Ord*.class ImageLoaderThread.class ImageObserver.class QVAppletJS.class
cd..
echo. 

echo Building render slider applet...
%JDK_HOME%\bin\javac -d %BUILD_DIR% -classpath %LIB_DIR%\jaws.jar;%JDK118_HOME%\lib\classes.zip *RenderSlider*.java qvApplet.java
cd %BUILD_DIR%
%JDK118_HOME%\bin\jar cvf %APPL_DIST%\qvRenderSliderApplet.jar *RenderSlider*.class qvApplet.class
cd..
echo. 

echo Building render hotspot applet...
%JDK_HOME%\bin\javac -d %BUILD_DIR% -classpath %LIB_DIR%\jaws.jar;%JDK118_HOME%\lib\classes.zip qvApplet.java qvHotspotLabel.java qvRender*Applet.java
cd %BUILD_DIR%
%JDK118_HOME%\bin\jar cvf %APPL_DIST%\qvRenderHotspotApplet.jar qvApplet.class qvHotspotLabel.class qvRender*Applet.class qvRender*Applet$*.class
cd..

