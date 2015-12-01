//var code='JClicApplet';
//var jarBase='http://localhost:8080/qv/applets/jclic';
//var archive='jclicapplet.jar,jclic.jar,jclicxml.jar,jmfhandlers.jar,utilities.jar';

var _info = navigator.userAgent;
var _ns = false;
var _mac = false;
var _ie = (_info.indexOf("MSIE") > 0 && _info.indexOf("Win") > 0 && _info.indexOf("Windows 3.1") < 0);
if(_info.indexOf("Opera")>=0){
   _ie=false;
   _ns=true;
}
else if(_ie==false){
  _ns = (navigator.appName.indexOf("Netscape") >= 0 && ((_info.indexOf("Win") > 0 && _info.indexOf("Win16") < 0 /*&& java.lang.System.getProperty("os.version").indexOf("3.5") < 0*/) || (_info.indexOf("Sun") > 0) || (_info.indexOf("Linux") > 0) || (_info.indexOf("AIX") > 0) || (_info.indexOf("OS/2") > 0)));
  _mac = _info.indexOf("Mac_PowerPC") > 0;
}

function writeJavaPlugin(code, jarBase, archive, params, width, height, rWidth, rHeight){
	writeJavaPlugin(code, jarBase, archive, params, width, height, rWidth, rHeight,'applet','',false);
}

function writeJavaApplet(code, jarBase, archive, params, width, height, rWidth, rHeight, type, name, scriptable){
   var w=width.toString();
   var h=height.toString();
   var nsw=w;
   var nsh=h;
   if(rWidth!=null) w=rWidth.toString();
   if(rHeight!=null) h=rHeight.toString();

     document.write('<APPLET NAME="'+name+'" ARCHIVE="'+archive+'" CODE="'+code+'" CODEBASE="' +jarBase+ '"');
     //document.write(' ARCHIVE="'+archive+'"');
     document.writeln(' WIDTH="' +nsw+ '" HEIGHT="' +nsh+ '" MAYSCRIPT="true" >');
//     document.writeln('<PARAM NAME="type" VALUE="application/x-java-applet;version=1.3">');
//     document.writeln('<PARAM NAME="scriptable" VALUE="' +scriptable+ '">');
	writeParams2(params, true);
     document.writeln('</APPLET>');
}

function writeJavaPlugin(code, jarBase, archive, params, width, height, rWidth, rHeight, type, name, scriptable){
   var w=width.toString();
   var h=height.toString();
   var nsw=w;
   var nsh=h;
   if(rWidth!=null) w=rWidth.toString();
   if(rHeight!=null) h=rHeight.toString();

   if (_ie == true){
      document.writeln(
        '<OBJECT classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"' +
        ' WIDTH="' +w+ '" HEIGHT="' +h+ '" type="' +type+ '" name="applet_' +name+ '">');
      document.writeln('<PARAM NAME=CODE VALUE="'+code+'">');
      document.writeln('<PARAM NAME=CODEBASE VALUE="' +jarBase+ '">');
      document.writeln('<PARAM NAME=ARCHIVE VALUE="'+archive+'">');

      //writeCacheInfo(true);
      writeParams2(params, true);
      document.writeln('<PARAM NAME="type" VALUE="application/x-java-applet;jpi-version=1.3.1">');
      document.writeln('<PARAM NAME="scriptable" VALUE="' +scriptable+ '">');
      //writeParams(project, true);
      writeDownloadPageInfo();
      document.writeln('</OBJECT>');
   }
   else if (_ns == true){
      document.write(
        '<EMBED type="application/x-java-applet;version=1.3"'+
        ' name="applet_'+ name +'" id="'+ name+ '" CODE="'+code+'" CODEBASE="' +jarBase+ '" ARCHIVE="'+archive+'"'+
        ' WIDTH="' +nsw+ '" HEIGHT="' +nsh +'" scriptable=true type="' +type+ '" MAYSCRIPT');
      //writeCacheInfo(false);
      //writeParams(project, false);
	writeParams2(params,false);
      document.writeln(
        ' scriptable=true ' + //scriptable+
        ' pluginspage="http://www.xtec.es/recursos/clic/jclic/instal_plugin.htm">');
	//writeParams2(params);
      document.writeln('<NOEMBED>');
      writeDownloadPageInfo();
      document.writeln('</NOEMBED>');     
   }
   else{
     if(_mac == true){
       setSystemSounds('false');
     }
     document.write('<APPLET CODE="'+code+'" CODEBASE="' +jarBase+ '"');
     document.write(' ARCHIVE="'+archive+'"');
     document.writeln(' WIDTH="' +nsw+ '" HEIGHT="' +nsh+ '">');
     document.writeln('<PARAM NAME="type" VALUE="application/x-java-applet;version=1.3">');
     document.writeln('<PARAM NAME="scriptable" VALUE="' +scriptable+ '">');
     //writeParams(project, true);
	writeParams2(params, true);
     document.writeln('</APPLET>');
  }
}

function writeParams2(params, p){
	j=params.indexOf('=');
	while (j>0){
		i=params.indexOf(';');
		if (i<0) i=params.length;
		param=params.substring(0,i);
		paramName=param.substring(0,j);
		paramValue=param.substring(j+1);
		if(p) document.writeln('<PARAM NAME="'+paramName+'" VALUE="'+paramValue+'">');
		else document.write(' ' +paramName+ '="' +paramValue+ '" ');
		params=params.substring(i+1);
		j=params.indexOf('=');
	}

}

function writeDownloadPageInfo(){
  document.writeln(
    '<SPAN STYLE="background-color: #F5DEB3; color: Black; display: block; padding: 10; font-family: Verdana,Arial; border-style: solid; border-width: thin; font-size: 12px; text-align: center; font-weight: bold;">'+
    'Per utilitzar aquesta aplicaci&oacute; cal instal&middot;lar el plug-in Java&#153; 1.3.1<BR><A HREF="http://www.xtec.es/recursos/clic/jclic/instal_plugin.htm" TARGET="_blank">Feu clic aqu&iacute; per descarregar-lo</A><BR>&nbsp;<BR>'+
    'Para utilizar esta aplicaci&oacute;n hay que instalar el plug-in Java&#153; 1.3.1<BR><A HREF="http://www.xtec.es/recursos/clic/jclic/instal_plugin_esp.htm" TARGET="_blank">Haga clic aqu&iacute; para descargarlo</A><BR>&nbsp;<BR>'+
    'In order to run this program you need to install the Java&#153; plug-in 1.3.1<BR><A HREF="http://www.xtec.es/recursos/clic/jclic/instal_plugin_eng.htm" TARGET="_blank">Click here to download it</A><BR>'+
    '</SPAN>');
}
