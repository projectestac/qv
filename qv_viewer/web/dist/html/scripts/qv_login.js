
function writeTeacherApplet(jarRelativePath){
	//document.write('<applet width="0" height="0" codebase="." archive="appl/qv_local.jar" code="edu.xtec.qv.player.CorrectQVApplet" name="app_1">');
	//document.write('<applet width="0" height="0" codebase="." archive="prova/cb/appl/qv_local.jar" code="edu.xtec.qv.player.CorrectQVApplet" name="app_1">');
	document.write('<applet width="0" height="0" codebase="." archive="'+jarRelativePath+'" code="edu.xtec.qv.player.CorrectQVApplet" name="app_1">');
	document.write('<param value="application/x-java-applet;version=1.3" name="type">');
	document.write('<param value="'+getGroup()+'" name="group">');
	document.write('</applet>');
}

function setDataRelativeFilePath(dataRelativePath){
//alert("setDataRelativeFilePath("+dataRelativePath+")");
	appl=getApplet('app_1');
	if (appl!=null){
		appl.setDataRelativeFilePath(dataRelativePath);
	}
}

function checkAppletIsRunning(checkNumUsers){
	var bOk = false;
	appl=getApplet('app_1');
	if (appl!=null){
		try{
			var b = appl.canLoadLoginProperties();
			if (b){
				var totalUsers = appl.getTotalUsers();
				if (checkNumUsers && totalUsers<1){
					alert("No hi ha cap usuari creat per realitzar la prova.");
				} else
					bOk = true;
			}
			else{
				alert("No es pot llegir la configuració d'usuaris.");
			}
		} catch (err){
			alert("No es té accés a l'applet Java. Comproveu que teniu habilitat el Java.");
		}
	} else {
		alert("No es té accés a l'applet Java. Comproveu que teniu habilitat el Java.");
	}
	return bOk;
}

function writeLoginGroups(){
	writeLoginGroupsStyle("", true); //doChangeAction
}

function writeLoginGroupsStyle(style, doChangeAction){
	writeLoginGroupsStyle(style, doChangeAction, "");
}

function writeLoginGroupsStyle(style, doChangeAction, defaultOption){
	document.writeln("<SELECT id='select_group' name='select_group' class='"+style+"' ");
	if (doChangeAction)
		document.writeln("onchange='selectedGroupId(this.options.selectedIndex);'");
	document.writeln(">");
	appl=getApplet('app_1');
	var groups;
	if (appl!=null){
		groups = appl.getLoginGroups();
		groups = groups.split(";");
		document.writeln("<option id='0' value=''></option>");
		if (defaultOption!=null && defaultOption.length>0)
			document.writeln("<option id='-1' value='"+defaultOption+"'>"+defaultOption+"</option>");
		if (groups.length<1){
			//document.writeln("<option id='-1' value='grup per defecte'>grup per defecte</option>");
		} else{
			//for (i=0;i<groups.length;i++){
			for (i=0; i<groups.length;i++){
				if (groups[i]!=''){
					document.writeln("<option id='"+(i+1)+"' value='"+groups[i]+"'>"+groups[i]+"</option>");
				}
			}
		}
		document.writeln("</SELECT>");
	}
}

function getSelectedGroupId(){
	var selectedIndex = document.forms[0].select_group.options.selectedIndex;
	var id = document.forms[0].select_group.options[selectedIndex].id;
	//return document.forms[0].select_group.options.selectedIndex;
	return id;
}

var lastSelectedGroupId = 0;

function selectedGroupId(id){
	//alert("id="+id+" selected:"+getSelectedGroupId());
	var groupId = getSelectedGroupId();
	appl=getApplet('app_1');
	if (appl!=null){
		var e = document.getElementById("users");
	  	if (e!=null){
			updateLoginParams(lastSelectedGroupId);
			e.innerHTML = writeLoginUsers(true, false);		
		}
	}
	initParams();
	lastSelectedGroupId = groupId;	
}

function getNotRepeatedUserId(numUser, userId){
	var groupId = getSelectedGroupId();
	var notRepeatedUserId = userId;
	var repeated = false;
	appl=getApplet('app_1');
	if (appl!=null){
		for (i=1;!repeated && i<numUser;i++){
			if (appl.getUserId(groupId, i)==userId)
				repeated = true;
		}
	}
	if (!repeated)
		return notRepeatedUserId;
	else
		return getNotRepeatedUserId(numUser, userId+"(2)");
}

function writeLoginUser(editable, isNew, numUser){
	var groupId = getSelectedGroupId();
	var loginUser = "";
	appl=getApplet('app_1');
	if (appl!=null){
		var userName = "";
		var userId = "";
		var userIT = "";
		var userNE = "";
		if (isNew){
			var prefix = document.forms[0].prefixUsers.value;
			if (numUser<10) numUser = "00"+numUser;
			else if (numUser<100) numUser = "0"+numUser;
			userName = "";
			userId = getNotRepeatedUserId(numUser, prefix+numUser);
			userIT = "NO";
			userNE = "NO";
		} else {
			userName = appl.getUserName(groupId, numUser);
			userId = appl.getUserId(groupId, numUser);
			userIT = appl.getUserIT(groupId, numUser);
			userNE = appl.getUserNE(groupId, numUser);
		}
		loginUser+="<tr>";
		loginUser+="<td>";
			loginUser+=writeValue(userId,"userId"+numUser, 9, false, editable);
		loginUser+="</td>";
		loginUser+="<td>";
			loginUser+=writeValue(userName,"userName"+numUser, 30, false, editable);
		loginUser+="</td>";
		loginUser+="<td>";
			loginUser+=writeValue(userIT,"userIT"+numUser, 2, true, editable);
		loginUser+="</td>";
		loginUser+="<td>";
			loginUser+=writeValue(userNE,"userNE"+numUser, 2, true, editable);
		loginUser+="</td>";
		loginUser+="<td>";
			loginUser+="<a href=\"#\" onClick=\"deleteUser("+numUser+")\" alt=\"Esborra\" title=\"Esborra\"><img border=\"0\" src=\"image/delete.gif\"/></a>";
		loginUser+="</td>";
		loginUser+="</tr>";
	}
	return loginUser;
}

function writeValue(value, fieldName, numCols, isCheck, editable){
	var sValue = "";
	if (editable){
		var type = "";
		var valueCodified = "";
		var options = "";
		if (isCheck){
			type = "checkbox";
			valueCodified = "";
			if (value=="NO" || value=="no" || value=="No" || value=="False" || value=="false")
				options = "";
			else
				options = "CHECKED";
		} else {
			type = "text";
			valueCodified = value;
		}
		sValue+="<input type=\""+type+"\" id=\""+fieldName+"\" name=\""+fieldName+"\" size=\""+numCols+"\" "+options+" value=\""+valueCodified+"\"></input>";
	} else {
		sValue+=value;
	}
	return sValue;
}

function writeLoginUsers(editable, isNewUser){
	var groupId = getSelectedGroupId();
	var loginUsers = "";
	loginUsers+= "<table>";
	if (appl!=null){
		var numUsers = parseInt(appl.getNumUsers(groupId));
		totalUsers = numUsers;

		if (numUsers>0 || isNewUser){
			loginUsers+= "<tr>";
			loginUsers+= "<td>Identificador</td>";
			loginUsers+= "<td>Nom</td>";
			loginUsers+= "<td>IT</td>";
			loginUsers+= "<td>NE</td>";
			loginUsers+= "<td>Esborra</td>";
			loginUsers+= "</tr>";

			for (i = 1; i<=numUsers; i++){
				loginUsers+= writeLoginUser(editable, false, i); //!isNew
			}
		}
	}
	loginUsers+= "</table>";
	return loginUsers;
}

function updateLoginParams(groupId){
	appl=getApplet('app_1');
	if (appl!=null){
	   for(i=0; i<document.forms[0].elements.length; i++){
		var obj=document.forms[0].elements[i];
		var objName = obj.name;
		if (objName.indexOf("userId")==0){
			var numUser = getNumUser("userId", objName);
			var value = obj.value;
			if (value==null) value = "";
			appl.setUserId(groupId, numUser, value);
		} else if (objName.indexOf("userName")==0){
			var numUser = getNumUser("userName", objName);
			var value = obj.value;
			if (value==null) value = "";
			appl.setUserName(groupId, numUser, value);
		} else if (objName.indexOf("userIT")==0){
			var numUser = getNumUser("userIT", objName);
			var checked = obj.checked;
			appl.setUserIT(groupId, numUser, checked);
		} else if (objName.indexOf("userNE")==0){
			var numUser = getNumUser("userNE", objName);
			var checked = obj.checked;
			appl.setUserNE(groupId, numUser, checked);
		}
	   }
	}
	appl.setNumUsers(groupId, document.forms[0].numUsers.value);
	appl.setPrefixUsers(groupId, document.forms[0].prefixUsers.value);
	appl.setPassword(groupId, document.forms[0].password.value);
	initParams();
}

var totalUsers = 0;

function addUser(){
	var groupId = getSelectedGroupId();
	appl=getApplet('app_1');
	if (appl!=null){
	  //var numUser = parseInt(appl.getNumUsers(groupId))+1;
	  var e = document.getElementById("users");
	  if (e!=null){
		updateLoginParams(groupId);
		e.innerHTML = writeLoginUsers(true, true);//editable, isNewUser

		totalUsers ++;
		var currentHTML = e.innerHTML;
		var newUser = writeLoginUser(true, true, totalUsers);
		var i = currentHTML.lastIndexOf("</tr>");
		if (i>0){
			e.innerHTML = currentHTML.substring(0, i+5) + newUser + currentHTML.substring(i+5);
			//alert("inner fi:"+e.innerHTML);
		}
	  }
	}
	initParams();
}

function deleteUser(numUser){
	var groupId = getSelectedGroupId();
	appl=getApplet('app_1');
	if (appl!=null){
		var e = document.getElementById("users");
	  	if (e!=null){
			updateLoginParams(groupId);
			appl.deleteUser(groupId, numUser);
			e.innerHTML = writeLoginUsers(true, false);
		}
	}
	initParams();
}

function getNumUser(fieldPrefix, fieldValue){
	return fieldValue.substring(fieldPrefix.length);
}

function getNumUsers(){
	var groupId = getSelectedGroupId();
	if (totalUsers>0)
		return totalUsers;
	else{
		appl=getApplet('app_1');
		if (appl!=null)
			return appl.getNumUsers(groupId);
		else
			return 0;
	}
}

function initParams(){
	var groupId = getSelectedGroupId();
	document.forms[0].numUsers.value = getNumUsers();
	appl=getApplet('app_1');
	if (appl!=null){
		document.forms[0].prefixUsers.value = appl.getPrefixUsers(groupId);
		document.forms[0].password.value = appl.getPassword(groupId);
	}
}

function restartParams(){
	var groupId = getSelectedGroupId();
	appl=getApplet('app_1');
	if (appl!=null){
		var e = document.getElementById("users");
	  	if (e!=null){
			appl.restartParams(groupId, document.forms[0].numUsers.value, document.forms[0].prefixUsers.value);
			e.innerHTML = writeLoginUsers(true, false);
		}
	}	
}

function doLogin(userId, password){
	var groupId = getSelectedGroupId();
	return doLoginGroup(groupId, userId, password);
}

function doLoginGroup(groupId, userId, password){
	appl=getApplet('app_1');
	if (appl!=null){
		//alert("groupId="+groupId+", userId="+userId+", password="+password);
		return appl.doLogin(groupId, userId, password);
	} else {
		alert("No es pot validar l'usuari. No s'ha pogut executar l'applet");
		return false;
	}
}

function checkUserIsLogged(){
	var userId = getQueryParam('p_userId');
	var password = getQueryParam('p_password');
	var groupId = getQueryParam('p_groupId');
	appl=getApplet('app_1');
	if (appl!=null){
		//alert("groupId="+groupId+", userId="+userId+", password="+password);
		var logged = appl.doLogin(groupId, userId, password);
		if (!logged){
			alert("L'usuari no està logat.");
			return false;
		} else {
			return true;
		}
	} else {
		alert("No es pot validar l'usuari. No s'ha pogut executar l'applet.");
		return false;
	}
}


function gotoLoginPage(){
	var assessment = getQueryParam('p_assessment');
	document.location='../../index2.html?assessment='+assessment;
}

function gotoIndexPage(extraParams){
	var ordering = "";
	appl=getApplet('app_1');
	if (appl!=null){
		var section_order = appl.getSectionOrder();
		//if (section_order!=null && section_order.trim()!="0"){
		if (section_order!=null && section_order!="0"){
			ordering = "&order_sections=1&section_order="+section_order;
		}
		var item_order = appl.getItemOrder();
		//if (item_order!=null && item_order.trim()!="0"){
		if (item_order!=null && item_order!="0"){
			ordering += "&order_items=1&item_order="+item_order;
		}
	}
	var assessment = getQueryParam('p_assessment');
	var userId = getQueryParam('p_userId');
	var password = getQueryParam('p_password');
	var groupId = getQueryParam('p_groupId');
	document.location='index.htm?assessment='+assessment
		+'&p_userId='+userId+'&p_password='+password
		+'&p_groupId='+groupId+'&p_checkUser=true'
		+'&server=local'
		+ordering
		+extraParams;
}

function hasToCheckUser(){
	var check = false;
	var checkUser = getQueryParam('p_checkUser');
	if (checkUser!=null && checkUser=='true')
		check = true;
	return check;
}

function getLoginFilePath(){
	appl=getApplet('app_1');
	if (appl!=null){
		return appl.getLoginFilePath();
	} else {
		return "";
	}
}

