
function writeInstallApplet(){
	document.write('<applet width="0" height="0" codebase="." archive="prova/cb/appl/qv_local.jar" code="edu.xtec.qv.installer.QVInstaller" name="app_1">');
	document.write('<param value="application/x-java-applet;version=1.3" name="type">');
	//document.write('<param value="'+getGroup()+'" name="group">');
	document.write('</applet>');
}

function checkInstallAppletIsRunning(){
	var bOk = false;
	appl=getApplet('app_1');
	if (appl!=null){
		try{
			appl.getSelectedDir();
			bOk = true;
		} catch (err){
			alert("No es té accés a l'applet Java. Comproveu que teniu habilitat el Java.");
		}
	} else {
		alert("No es té accés a l'applet Java. Comproveu que teniu habilitat el Java.");
	}
	return bOk;
}

function showFileChooser(){
	appl=getApplet('app_1');
	if (appl!=null){
	   try{
		appl.setDoShowFileChooser(true);
		checkFolder();
	   } catch (err){
		alert("err:"+err);
	   }
	}
}

function showFileChooserFile(){
	appl=getApplet('app_1');
	if (appl!=null){
	   try{
		appl.setDoShowFileChooser(true, false);
		checkFolder();
	   } catch (err){
		alert("err:"+err);
	   }
	}
}




var currentFolder = "";

function checkFolder() {
	appl=getApplet('app_1');
	if (appl!=null){
		var dir = appl.getSelectedDir();
		if (dir=="" || dir==currentFolder) {
        		timerID = setTimeout("checkFolder()",1000);
        	} else {
			currentFolder = dir;
			document.forms[0].p_installDir.value = currentFolder;
			//alert("dir="+dir);
		}
	}
}

function doInstall(dir){
	appl=getApplet('app_1');
	if (appl!=null){
		document.forms[0].installButton.disabled = true;
		//AA1 Element.update($('installMessages'), 'Instal·lant...');
		setStatusMessage('Instal·lant...');//AA1
		appl.setDoInstall(true, dir);
		checkInstall();
	}
}

function doUninstall(){//A7
	appl=getApplet('app_1');
	if (appl!=null){
		document.forms[0].uninstallButton.disabled = true;
		setStatusMessage('Desinstal·lant...');
		appl.setDoUninstall(true);
		checkUninstall();
	}
}

function doInstallAssessment(dir){
	appl=getApplet('app_1');
	if (appl!=null){
		document.forms[0].installButton.disabled = true;
		//AA1 Element.update($('installMessages'), 'Instal·lant...');
		setStatusMessage('Instal·lant prova...');//AA1
		appl.setDoInstallAssessment(true, dir);
		checkInstallAssessment();
	}
}

function setStatusMessage(message){//AA1
	var elem = document.getElementById("installMessages");
	if (elem!=null){
		elem.innerHTML=message;
	}
}

function checkInstall() {
	appl=getApplet('app_1');
	if (appl!=null){
		//AA1 Element.update($('installMessages'), appl.getInstallCurrentMessage());
		setStatusMessage(appl.getInstallCurrentMessage());//AA1
		var installResult = appl.getInstallResult();
		if (installResult=="") {
        		timerID = setTimeout("checkInstall()",200);
        	} else {
			if (installResult=="ok"){
				var continuePage = appl.getUrlInstalledDir()+"/manage_groups_sharedfolder.html";
				var result = "Instal·lació finalitzada correctament.";
				result += "<br/><br/>Per poder editar els grups i instal·lar les proves necessitareu logar-vos com a Administrador.<br/>";
				result += "<br/>Usuari: <b>admin</b>";
				result += "<br/>Contrasenya: <b>pwd</b><br/>";
				result += "<br/><a href=\""+continuePage+"\">Continua</a>.";
				//AA1 Element.update($('installMessages'), result);
				setStatusMessage(result);//AA1
				alert("Instal·lació finalitzada correctament.");
				//document.location=continuePage;
			} else	{
				document.forms[0].installButton.disabled = false;
				//AA1 Element.update($('installMessages'), installResult);
				setStatusMessage(installResult);//AA1
				alert(installResult);
			}
			//alert("resultat="+installResult);
		}
	}
}

function checkUninstall() {//A7
	appl=getApplet('app_1');
	if (appl!=null){
		setStatusMessage(appl.getInstallCurrentMessage());//AA1
		var installResult = appl.getInstallResult();
		if (installResult=="") {
        		timerID = setTimeout("checkUninstall()",200);
        	} else {
			if (installResult=="ok"){
				var result = "Desinstal·lació finalitzada correctament.";
				setStatusMessage(result);//AA1
				alert("Desinstal·lació finalitzada correctament.");
			} else	{
				document.forms[0].uninstallButton.disabled = false;
				setStatusMessage(installResult);
				alert(installResult);
			}
			//alert("resultat="+installResult);
		}
	}
}

function checkInstallAssessment() {
	appl=getApplet('app_1');
	if (appl!=null){
		setStatusMessage(appl.getInstallCurrentMessage());
		var installResult = appl.getInstallResult();
		if (installResult=="") {
        		timerID = setTimeout("checkInstallAssessment()",200);
        	} else {
			if (installResult=="ok"){
				var continuePage = "indexAdmin.html";
				var result = "Instal·lació finalitzada correctament.<br/>"
				//result += "<a href=\""+continuePage+"\">Torneu a l'administració de les proves</a>.";
				result += "<a onClick=\"document.forms[0].action='"+continuePage+"';document.forms[0].submit();\">Torneu a l'administració de les proves</a>.";


				setStatusMessage(result);
				alert("Instal·lació finalitzada correctament.");
				document.forms[0].installButton.disabled = false;
				//document.location=continuePage;
			} else	{
				document.forms[0].installButton.disabled = false;
				setStatusMessage(installResult);
				alert(installResult);
			}
		}
	}
}
