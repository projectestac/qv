// $Id: qv_local.js,v 1.17 2009-06-23 10:19:10 sarjona Exp $

includeScript('order.js');//A4

includeScript('qv_login.js');//A5


var viewer_server = "/qv_viewer/";
var teacher = null;
var assessment = null;

var iniResponses = "";//Albert
var iniScores = "";//Albert
var isByHandCorrectEditable = true;//Albert

function setTeacher(t){
  teacher=t;
}
function setAssessment(a){
  assessment=a;
}

function getUser(){
	var user = getQueryParam('p_name')
	return user;
}


if (!window.utf8_decode){
  includeScript('php.js');
}

function writeUserName(){
	var user = utf8_decode(unescape(getQueryParam('fullname')));
	if (notNull(user)){
	 document.write(user);
  }
}


function getQueryParam(name){
    var value = "";
    var location = new String(document.location);
	var iQuery = location.indexOf("?");
	if (iQuery>=0){
    	var query = location.substring(iQuery+1);
		if (name=='server' && query.indexOf("$$")>=0){
	    	var server = query.split(/[;$$]/);
	   		for (var i = 0; i < server.length; i++) {
				if (server[i].indexOf("http://")==0){
					value = "$$"+server[i]+"$$";
					break;
				}
			}
		} else{
	    	var pairs = query.split(/[;&]/);
	   		for ( var i = 0; i < pairs.length; i++ ) {
	   			var key = pairs[i].split('=');
	   			if (key[0]==name){
	   				value = key[1];
	   				break; 
	   			}
	   		}			
		}		
   	}
  if (value!=""){
    if (value.indexOf("#")>=0) value=new String(value).substring(0, value.indexOf("#")); 
  }
	return value;
}

function getServerBase(){
  var location = new String(document.location);
  var iQuery = location.indexOf("?");
  if (iQuery>0) location = location.substring(0, iQuery);
  var iQuery = location.lastIndexOf("/");
  if (iQuery>0) location = location.substring(0, iQuery+1);
  return location;
}

function getJClicURL(url){
	var jclicurl = url;
	if (url!=null){
		if (url.indexOf('http')<0){
			jclicurl = getServerBase()+url;
		}
	}
	return jclicurl;
}

function replaceChars(pattern, newstring, entry) {
	temp = "" + entry; // temporary holder
	
	while (temp.indexOf(pattern)>-1) {
  	pos= temp.indexOf(pattern);
  	temp = "" + (temp.substring(0, pos) + newstring + 
  	temp.substring((pos + pattern.length), temp.length));
	}
	return temp;
}

function generateHTML(user, assess, num){
  var service = viewer_server+"generateHTML";
  var pars = "user="+user+"&assessment="+assess+"&section="+num;
  var success = function(){setTimeout("sendWithQueryParams(getSectionName('"+num+"'));", 5000);};
  var failure = function(){alert("Error creating HTML for section "+num);};
  var myAjax = new Ajax.Request(service, {method:'post', postBody:pars, onSuccess:success, onFailure:failure});
  //callService(service, pars, success, failure);	  
  
}

//Per compatibilitat amb versions anteriors
function getSectionName(num){
  var section = "index.htm";
  if (num>0) section="section_"+num+".htm";
  return section;
}

//Per compatibilitat amb versions anteriors
function gotoSection(num){  
  var section = getSectionName(num);
	if (new String(document.location).substring(0,10).indexOf("http:")!=-1 || isWorkingWithServer()){
    var service = section;
    var pars = "<?xml version=\"1.0\"?>";
    var success = function(){sendWithQueryParams(section);}
    var failure = function(){generateHTML(teacher, assessment, num);}
    var myAjax = new Ajax.Request(service, {method:'get', onSuccess:success, onFailure:failure});
    //callService(service, pars, success, failure);	  
  }else{
	 sendWithQueryParams(section);
  }
}

function getSectionNum(numStr, maxSections, diff, randomizable){ //Albert
  var num = parseInt(numStr);
  if (isSectionRandomOrder()){
	var order_sections = parseInt(getSectionOrder());
	if (diff == 0){ //Vol anar a la pàgina 'num'
		num = getRandomValueRandomizable(num, maxSections, randomizable);
	} else if (diff == -1){ //Vol anar a la pàgina anterior a 'num'
		realValue = getRealValueRandomizable(num, maxSections, randomizable);
		num = getRandomValueRandomizable(realValue-1, maxSections, randomizable);
	} else if (diff == 1){ //Vol anar a la pàgina següent a 'num'
		realValue = getRealValueRandomizable(num, maxSections,randomizable);
		if (realValue==maxSections){
			num = 0;
		} else {
			num = getRandomValueRandomizable(realValue+1, maxSections, randomizable);
		}
	}
  } else {
	num = num + diff;
	if (num>maxSections || num<0){
		num = 0;
	}
  }
  return num;
}

//function getSectionName(num){ //Albert
function getSectionNameRandomizable(numStr, maxSections, diff, randomizable){ //Albert
	//alert("getSectionNameRandomizable");
  var num = getSectionNum(numStr, maxSections, diff, randomizable);
  var section = "index.htm";
  if (num>0) section="section_"+num+".htm";

  return section;
}

//function gotoSection(num){  //Albert
function gotoSectionRandomizable(num, maxSections, diff, randomizable){  //Albert
	//alert("gotoSectionRandomizable("+num+", "+maxSections+", "+diff+", "+randomizable+")");
  	//var section = getSectionName(num); //Albert
	var section = getSectionNameRandomizable(num, maxSections, diff, randomizable);
	if (new String(document.location).substring(0,10).indexOf("http:")!=-1 || isWorkingWithServer()){
	if (!isTeacher()){
		saveTime();
	}
	var num2 = parseInt(num);
	if (num!=0){
		num2 = getSectionNum(num, maxSections, diff, randomizable);
	}
    //alert("section="+section+" num2="+num2);
    var service = section;
    var pars = "<?xml version=\"1.0\"?>";
    var success = function(){sendWithQueryParams(section);}
    var failure = function(){generateHTML(teacher, assessment, num2);}
    var myAjax = new Ajax.Request(service, {method:'get', onSuccess:success, onFailure:failure});
    //callService(service, pars, success, failure);	  
  }else{
	 sendWithQueryParams(section);
  }
}

function writeSectionNumRandomizable(num, maxSections, randomizable){ //Albert
	if (isSectionRandomOrder()){
		var order_sections = parseInt(getSectionOrder());
		document.write(getRealValueRandomizable(num, maxSections, randomizable));
	} else {
		document.write(num);
	}
}

function sendWithQueryParams(url){
	var fullUrl = url;
	if (fullUrl.indexOf("?")<0){
		fullUrl += "?";
	}
	fullUrl+=getMandatoryQueryParams();
  document.location=fullUrl;
}

function getMandatoryQueryParams(){
  var fullUrl = "";
	
	//A5-->
	if (notNull(getQueryParam('p_checkUser'))) fullUrl+='&p_checkUser='+getQueryParam('p_checkUser');
	if (notNull(getQueryParam('p_userId'))) fullUrl+='&p_userId='+getQueryParam('p_userId');
	if (notNull(getQueryParam('p_password'))) fullUrl+='&p_password='+getQueryParam('p_password');
	if (notNull(getQueryParam('p_groupId'))) fullUrl+='&p_groupId='+getQueryParam('p_groupId');

	if (notNull(getQueryParam('loginFilePath'))) fullUrl+='&loginFilePath='+getQueryParam('loginFilePath');
	if (notNull(getQueryParam('configFilePath'))) fullUrl+='&configFilePath='+getQueryParam('configFilePath');
	if (notNull(getQueryParam('dataFileRelativePath'))) fullUrl+='&dataFileRelativePath='+getQueryParam('dataFileRelativePath');
	//<--A5
	if (isWorkingWithServer()) fullUrl+='&server='+getQueryParam('server');
	if (notNull(getQueryParam('assignmentid'))) fullUrl+='&assignmentid='+getQueryParam('assignmentid');
	if (notNull(getQueryParam('userview'))) fullUrl+='&userview='+getQueryParam('userview');
	if (notNull(getQueryParam('userid'))) fullUrl+='&userid='+getQueryParam('userid');
	if (notNull(getQueryParam('skin'))) fullUrl+='&skin='+getQueryParam('skin');
	if (notNull(getQueryParam('lang'))) fullUrl+='&lang='+getQueryParam('lang');
	if (notNull(getQueryParam('showinteraction'))) fullUrl+='&showinteraction='+getQueryParam('showinteraction');
	if (notNull(getQueryParam('js'))) fullUrl+='&js='+getQueryParam('js');
	if (notNull(getQueryParam('appl'))) fullUrl+='&appl='+getQueryParam('appl');
	if (notNull(getQueryParam('css'))) fullUrl+='&css='+getQueryParam('css');
	if (notNull(getQueryParam('fullname'))) fullUrl+='&fullname='+getQueryParam('fullname');
	if (notNull(getQueryParam('showsolution'))) fullUrl+='&showsolution='+getQueryParam('showsolution');
	if (notNull(getQueryParam('showhint'))) fullUrl+='&showhint='+getQueryParam('showhint');
	//if (notNull(getQueryParam('order_items'))) fullUrl+='&order_items='+getQueryParam('order_items');//Albert
	if (notNull(getQueryParam('order_items'))){//Albert
		//alert("s'ha establert un order_items");
		fullUrl+='&order_items='+getQueryParam('order_items');
		if (notNull(getQueryParam('item_order'))){//Albert
			//alert("ja hi havia un order inicialment:"+getQueryParam('item_order'));
			fullUrl+='&item_order='+getQueryParam('item_order');
		} else if(getItemOrder()>=0){
			//alert("no hi havia ordre inicialment i ara es:"+getItemOrder());
			fullUrl+='&item_order='+getItemOrder();
		} else {
			//alert("no hi havia ordre inicialment i no n'hi ha d'haver");
		}
	}
	if (notNull(getQueryParam('order_sections'))){//Albert
		//alert("s'ha establert un order_sections");
		fullUrl+='&order_sections='+getQueryParam('order_sections');
		if (notNull(getQueryParam('section_order'))){//Albert
			//alert("ja hi havia un order inicialment:"+getQueryParam('section_order'));
			fullUrl+='&section_order='+getQueryParam('section_order');
		} else if(getSectionOrder()>=0){
			//alert("no hi havia ordre inicialment i ara es:"+getSectionOrder());
			fullUrl+='&section_order='+getSectionOrder();
		} else {
			//alert("no hi havia ordre inicialment i no n'hi ha d'haver");
		}
	}
	return fullUrl;
}


function notNull(o){
	return o!=null && o.length>0 && o!='null';
}

function getXML(){
	appl=getApplet('app_1');
	if (appl!=null){
		return appl.getXMLFilename();
	}
	return null;
}

function getScore(itemId){
	var score = "";
	app = getApplet('app_1');
	if (app!=null){
    	score=app.getScore(itemId);
    }
	return score;
}
function setScore(id, resp){
	appl=getApplet('app_1');
	if (appl!=null){
		//alert("id="+id+"  resp="+resp);
		//appl.addResponse(id, resp);
		appl.setScore(id.substring(6), resp);
	}
}

function loadScores(){
	var score = eval('document.'+document.forms[0].name+'.score_1130703683598343');
	if (score!=null){
		score.value=getScore('1130703683598343');
	}
}


function getApplet(name){
	appl=document.applets[name];
	if (appl==null){
		appl = document.applets[0];
		for (i=0;i<document.applets.length;i++){
			if (document.applets[i].name==name){
				appl = document.applets[i];
				break;
			}
		}
	}
	return appl;
}

function saveSection(lang){
	setMessage('action_saving', 'messages-ok');
  Element.setStyle(document.body, {cursor: 'progress'} );
	gotoTopPage();
	var app = getApplet("app_1");
	if (app!=null){
		var responses=''+generate_responses(document.forms[0], false)+'';
		var success = function(t){saveSectionComplete(t);}
    var failure = function(){saveSectionFailed();}
	 	var service = getServer();
  	var pars = '<'+'?xml version="1.0" encoding="UTF-8"?'+'>\n'
    pars+='<bean id="save_section" >\n';
    pars+=' <param name="assignmentid" value="'+getQueryParam('assignmentid')+'"/>\n';
  	pars+=' <param name="sectionid" value="'+app.getSectionId()+'"/>\n';
	pars+=' <param name="sectionorder" value="'+getSectionOrder()+'"/>\n'; //Albert
	pars+=' <param name="itemorder" value="'+getItemOrder()+'"/>\n'; //Albert
	pars+=' <param name="time" value="'+getPassedTime()+'"/>\n'; //Albert
  	pars+=" <responses><![CDATA["+responses+"]]></responses>\n";
  	//pars+=" <responses><![CDATA["+encodeURI(responses)+"]]></responses>\n";
  	pars+="</bean>";
	//alert("pars="+pars);
  	callService(service, pars, success, failure);
   	//var myAjax = new Ajax.Request(service, {method:'post', contentType: 'text/xml', postBody:pars, onSuccess:success, onFailure:failure});    
	}
  Element.setStyle(document.body, {cursor: 'default'} );
}

function saveSectionComplete(t){
  var eSection = getXMLElement(t.responseXML, 'section');
  if (notNull(getXMLAttribute(eSection, 'error'))){
    setMessage(getXMLAttribute(eSection, 'error'), 'messages-ko');
  }else{
  	setMessage('action_save_ok', 'messages-ok');
	  updateState(getXMLAttribute(eSection, 'state'));
  }

  restartTime(); //Albert
}
function saveSectionFailed(){
	setMessage('action_save_ko', 'messages-ko');
}

function saveSectionTeacher(lang){//Albert
	setMessage('action_saving', 'messages-ok');
  Element.setStyle(document.body, {cursor: 'progress'} );
	gotoTopPage();
	var app = getApplet("app_1");
	if (app!=null){
		var responses=''+generate_responses(document.forms[0], false)+'';
		iniResponses = responses;//A
		//alert("iniResponses="+iniResponses);
		app.correct(''+responses+'');//A
		var scores = app.getScores();//A
		var success = function(t){saveSectionTeacherComplete(t);}
    var failure = function(){saveSectionTeacherFailed();}
	 	var service = getServer();
  	var pars = '<'+'?xml version="1.0" encoding="UTF-8"?'+'>\n'
    pars+='<bean id="save_section_teacher" >\n';
    pars+=' <param name="assignmentid" value="'+getQueryParam('assignmentid')+'"/>\n';
  	pars+=' <param name="sectionid" value="'+app.getSectionId()+'"/>\n';
  	pars+=" <responses><![CDATA["+responses+"]]></responses>\n";
  	pars+=" <scores><![CDATA["+scores+"]]></scores>\n";//A
  	pars+="</bean>";
	//alert("pars="+pars);
  	callService(service, pars, success, failure);
	}
  Element.setStyle(document.body, {cursor: 'default'} );
}

function saveSectionTeacherComplete(t){//Albert
  var eSection = getXMLElement(t.responseXML, 'section');
  if (notNull(getXMLAttribute(eSection, 'error'))){
    setMessage(getXMLAttribute(eSection, 'error'), 'messages-ko');
  }else{
  	setMessage('action_save_ok', 'messages-ok');
	  updateState(getXMLAttribute(eSection, 'state'));
  }
  updateScores();//A
  updateFeedback();//A

}
function saveSectionTeacherFailed(){//Albert
	setMessage('action_save_ko', 'messages-ko');
}


function deliverSection(){
	setMessage('action_delivering', 'messages-ok');
  Element.setStyle(document.body, {cursor: 'progress'} );
	gotoTopPage();
	var app = getApplet("app_1");
	if (app!=null){
		var responses=''+generate_responses(document.forms[0], false)+'';
		app.correct(''+responses+'');
		var scores = app.getScores();

		var success = function(t){deliverSectionComplete(t, responses);}
    var failure = function(){deliverSectionFailed();}

	 	var service = getServer();
  	var pars = '<'+'?xml version="1.0" encoding="UTF-8"?'+'>\n'
    pars+='<bean id="deliver_section" >\n';
    pars+=' <param name="assignmentid" value="'+getQueryParam('assignmentid')+'"/>\n';
  	pars+=' <param name="sectionid" value="'+app.getSectionId()+'"/>\n';
	pars+=' <param name="sectionorder" value="'+getSectionOrder()+'"/>\n'; //Albert
	pars+=' <param name="itemorder" value="'+getItemOrder()+'"/>\n'; //Albert
  	pars+=' <param name="time" value="'+getPassedTime()+'"/>\n'; //Albert
	pars+=" <responses><![CDATA["+responses+"]]></responses>\n";
  	pars+=" <scores><![CDATA["+scores+"]]></scores>\n";
  	pars+="</bean>";
  	//alert("pars="+pars);
	callService(service, pars, success, failure);
   	//var myAjax = new Ajax.Request(service, {method:'post', contentType: 'text/xml', postBody:pars, onSuccess:success, onFailure:failure});
	}
  Element.setStyle(document.body, {cursor: 'default'} );	
}

function deliverSectionComplete(t, responses){
  var eSection = getXMLElement(t.responseXML, 'section');
	if (getXMLAttribute(eSection, 'error').length>0){
		setMessage(getXMLAttribute(eSection, 'error'), 'messages-ko');
	}else{
		setMessage('action_deliver_ok', 'messages-ok');
		checkActionButtons(getXMLAttribute(eSection, 'attempts'), getXMLAttribute(eSection, 'maxdeliver'), getXMLAttribute(eSection, 'state'));
		updateScores();
		updateAttempts(getXMLAttribute(eSection, 'attempts'), getXMLAttribute(eSection, 'maxdeliver'));
		updateState(getXMLAttribute(eSection, 'state'));
		updateTime(getXMLAttribute(eSection, 'time')); //Albert
		updateTimeVisibility(getXMLAttribute(eSection, 'state'));
		updateScoreVisibility(getXMLAttribute(eSection, 'attempts'), getXMLAttribute(eSection, 'showcorrection'));

		if (getXMLAttribute(eSection, 'showcorrection')!='0')	updateFeedback();
	}

	restartTime(); //Albert
}
function deliverSectionFailed(){
	setMessage('action_deliver_ko', 'messages-ko');
}

//Albert-->

function saveTime(){
	//alert("saveTime()");
	//setMessage('action_delivering', 'messages-ok');
  	Element.setStyle(document.body, {cursor: 'progress'} );
	//gotoTopPage();
	var app = getApplet("app_1");
	if (app!=null){
		var success = function(){saveTimeComplete();}
    		var failure = function(){saveTimeFailed();}
		var sectionId = app.getSectionId();
		//alert("isTeacher()?"+isTeacher()+" getQueryParam(server)?"+getServer());
		if (!isTeacher() && sectionId!="index"){

			var service = getServer();
			var pars = '<'+'?xml version="1.0" encoding="UTF-8"?'+'>\n'
    			pars+='<bean id="save_time" >\n';
    			pars+=' <param name="assignmentid" value="'+getQueryParam('assignmentid')+'"/>\n';
  			pars+=' <param name="sectionid" value="'+sectionId+'"/>\n';
  			pars+=' <param name="time" value="'+getPassedTime()+'"/>\n'; 
  			pars+="</bean>";
  			//alert("pars="+pars);
			callService(service, pars, success, failure);
		}
	}
  	Element.setStyle(document.body, {cursor: 'default'} );	
}

function saveTimeComplete(){
	restartTime(); //Albert
	//alert("guardat ");//+t.responseXML);
}
function saveTimeFailed(){
	//setMessage('action_deliver_ko', 'messages-ko');
	//alert("no guardat ");//+t.responseXML);
}


//<--Albert

function correctTeacherSection(){
	setMessage('action_correcting', 'messages-ok');
  Element.setStyle(document.body, {cursor: 'progress'} );
	gotoTopPage();
 	var app = getApplet("app_1");
	if (app!=null){
		var success = function(t){correctTeacherSectionComplete(t);}
    var failure = function(){correctTeacherSectionFailed();}

	 	var service = getServer();
  	var pars = '<'+'?xml version="1.0" encoding="UTF-8"?'+'>\n'
    pars+='<bean id="correct_section" >\n';
    pars+=' <param name="assignmentid" value="'+getQueryParam('assignmentid')+'"/>\n';
  	pars+=' <param name="sectionid" value="'+app.getSectionId()+'"/>\n';
	if (manualChangedResponsesOrScores()){
		var responses=''+generate_responses(document.forms[0], false)+'';
		app.correct(''+responses+'');
		var scores = app.getScores();
		pars+=" <responses><![CDATA["+responses+"]]></responses>\n";
  		pars+=" <scores><![CDATA["+scores+"]]></scores>\n";
	}
  	pars+="</bean>";
	//alert("pars="+pars);
  	callService(service, pars, success, failure);
   	//var myAjax = new Ajax.Request(service, {method:'post', contentType: 'text/xml', postBody:pars, onSuccess:success, onFailure:failure});
	}
	Element.setStyle(document.body, {cursor: 'default'} );	
}

function correctTeacherSectionComplete(t){
	setMessage('action_correct_ok', 'messages-ok');
	updateScoreFields(false);
	updateScores();
	updateFeedback();
	Element.hide($('action-buttons'));
}
function correctTeacherSectionFailed(){
	setMessage('action_correct_ko', 'messages-ko');
}

function getResponses(){
	var app = getApplet("app_1");
	if (app!=null){
		var success = function(t){getResponsesComplete(t);}
	 	var failure = function(){getResponsesFailed();}

   	var service = getServer();
  	var pars = '<'+'?xml version="1.0" encoding="UTF-8"?'+'>\n<bean id="get_section" >\n <param name="assignmentid" value="'+getQueryParam('assignmentid')+'"/>\n <param name="sectionid" value="'+app.getSectionId()+'"/>\n</bean>';
    callService(service, pars, success, failure);  	
   	//var myAjax = new Ajax.Request(service, {method:'post', contentType: 'text/xml', postBody:pars, onSuccess:success, onFailure:failure});	  
	}
}

function getResponsesComplete(t){
	var eSection = getXMLElement(t.responseXML,'section');
  if (notNull(getXMLAttribute(eSection, 'error'))){
    setMessage(getXMLAttribute(eSection, 'error'), 'messages-ko');
  }else{
	iniResponses = getXMLText(t.responseXML, 'responses');//Albert
	iniScores = getXMLText(t.responseXML, 'scores');//Albert

  	checkActionButtons(getXMLAttribute(eSection, 'attempts'), getXMLAttribute(eSection, 'maxdeliver'), getXMLAttribute(eSection, 'state'));
  	select_responses(document.forms[0], getXMLText(t.responseXML, 'responses'));
  	updateAttempts(getXMLAttribute(eSection, 'attempts'), getXMLAttribute(eSection, 'maxdeliver'));
  	updateState(getXMLAttribute(eSection, 'state'));
  	if (isTeacher() || (getXMLAttribute(eSection, 'attempts')>0 && getXMLAttribute(eSection, 'showcorrection')>0)){
		if (isTeacher()){//A
			updateFeedback(getXMLText(t.responseXML, 'pending_scores'));//A
		} else {
	  		updateFeedback(getXMLText(t.responseXML, 'scores'));
		}
		showScoreAndTimeGlobalFields();//A3
  	}
	updateTimeVisibility(getXMLAttribute(eSection, 'state'));
	updateTime(getXMLAttribute(eSection, 'time')); //Albert
  }	
}

function showScoreAndTimeGlobalFields(){//A3
	var e = $('header-puntuation-score');
	if (e!=null){
		if (e.style!=null){
			e.style["visibility"]="visible";
		}
		Element.show(e);
	}
	/*var e2 = $('header-puntuation-time');
	if (e2!=null){
		if (e2.style!=null){
			alert("-3-");
			e2.style["visibility"]="visible";
		}
		Element.show(e2);
	}*/
}

function getResponsesFailed(){
	alert('getResponsesFailed');
}



function disableSection(){
	Form.disable(document.forms[0]);
	disableApplets();
}

function disableSectionPunts(punts){//Albert
  for (i=0;i<document.forms[0].elements.length;i++){
    var obj=document.forms[0].elements[i];
    var name=obj.name;
    if (name.indexOf("txt_score_")==0){
	if (!punts)
		obj.disabled=true;
    } else {
      //obj.editable=false;
	//obj.enabled=false;
	obj.disabled=true;
    }
  }
  disableApplets();
}

function updateScoreFields(editable){//Albert
	if (!editable){
		isByHandCorrectEditable = false;
		for (i=0;i<document.forms[0].elements.length;i++){
    			var obj=document.forms[0].elements[i];
			var name=obj.name;
    			if (name.indexOf("txt_score_")==0){
				var itemId = name.substring("txt_score_".length);
				var currentScore = obj.value;
				var e = $('score_'+itemId);
				if (e!=null){
					if (currentScore)
						Element.update(e, currentScore);
					else
						Element.update(e, ''); //Elimino el camp de text de l'score

					if (!isTeacher()){
						var e2 = $('divscore_'+itemId);
						if (e2!=null){
							if (e2.style!=null){ //A2
								e2.style["visibility"]="hidden";
							}							
							Element.hide(e2); //No posem el label de la puntuació si no en té establerta
						}
					}
				}				
			}
    		}
	} 
}

function showScores(){//A2
	for (i=0;i<document.forms[0].elements.length;i++){
    		var obj=document.forms[0].elements[i];
		var name=obj.name;
    		if (name.indexOf("txt_score_")==0){
			
			var itemId = name.substring("txt_score_".length);
			var currentScore = obj.value;
			var e2 = $('divscore_'+itemId);
			if (e2!=null){
				if (e2.style!=null){
					//alert("txt_score_! "+e2.style["visibility"]);
					e2.style["visibility"]="visible";
				}
				Element.show(e2);
			}
    		}
	}
}

function disableApplets(){
	//alert('disableApplets');
	for (i=0;i<document.applets.length;i++){
		var obj=document.applets[i];
		if (obj.name!="app_1"){
			obj.setEditable(false);
			//obj.isEditable=false;
		}
	}	
}

function isWorkingWithServer(){
	return notNull(getQueryParam('server'));
}


function checkActionButtons(attempts, maxdeliver, state){
	if (state=='2' || (!isTeacher() && (parseInt(maxdeliver)>0 && parseInt(attempts)>=parseInt(maxdeliver)))){
		Element.hide($('action-buttons'));
	}
	if (!isWorkingWithServer() || !isTeacher() || state=='2'){ //Albert
		updateScoreFields(false); //No són editables
	}
	if (isTeacher()) //A2
		showScores();
	/*if (!isTeacher() && state!='2'){
		var e = $('header-puntuation-time');
		if (e!=null)
			Element.hide(e); 
	}*/
}

function updateTimeVisibility(state){
	//alert("updateTimeVisibility state:"+state);
	var e = $('header-puntuation-time');
	if (!isTeacher() && state!='2'){
		if (e!=null){
			if (e.style!=null){
				e.style["visibility"]="hidden";
			}
			Element.hide(e);
		}
	} else {
		if (e!=null){
			if (e.style!=null){
				e.style["visibility"]="visible";
			}
			Element.show(e);
		}
	}
}

function updateScoreVisibility(attempts, showcorrection){
	//alert("attempts:"+attempts+" showCorrection:"+showcorrection);
	if (isTeacher() || (attempts>0 && showcorrection>0)){
		showScoreAndTimeGlobalFields();//A3
  	}
}

function isTeacher(){
	//alert("isTeacher()?"+(getQueryParam('userview')=='teacher'));
	return getQueryParam('userview')=='teacher';
}

function getXMLText(e, elemName){
  var text = "";
	if (elemName==null && e!=null) {
    if (e.childNodes[1]==null) text=e.firstChild.nodeValue;
    else text=e.childNodes[1].nodeValue;
  } else if (e.getElementsByTagName(elemName).length>0 && e.getElementsByTagName(elemName)[0].firstChild!=null){
		text=e.getElementsByTagName(elemName)[0].firstChild.nodeValue;
	}
	text=replaceChars('&#39;',"'", text);
        //text=utf8_decode(text);
	return text;	
}
function getXMLElements(e, elemName){
	if (e.getElementsByTagName(elemName).length>0){
		return e.getElementsByTagName(elemName);
	}
	return "";	
}
function getXMLElement(e, elemName){
	if (e.getElementsByTagName(elemName).length>0){
		return e.getElementsByTagName(elemName)[0];
	}
	return "";	
}
function getXMLAttribute(e, attName){
	if (e!=null && e.attributes!=null && e.attributes.getNamedItem(attName)!=null){
    return e.attributes.getNamedItem(attName).value;
  }
  return "";
}

function gotoTopPage(){
  window.scrollTo(0,0);
}


function correctSection(){
  setMessage('action_correcting', 'messages-ok');
  Element.setStyle(document.body, {cursor: 'progress'} );
  //document.body.toggleClassName('busy-page');
	gotoTopPage();
	var app = getApplet("app_1");
	if (app!=null){
		app.correct(''+generate_responses(document.forms[0], false)+'');
		//select_responses(document.forms[0],app.getScores());
		Element.update($('score'), ''+app.getScore()+'');
		updateFeedback();
		var solutions = document.getElementsByClassName('solution-bt');
		var i=0;
		while (i<solutions.length){
		  updateSolutionVisibility(solutions[i].id.substring(11));
  		i++;
    }
	}
  Element.setStyle(document.body, {cursor: 'default'} );
  setMessage('action_correct_ok', 'messages-ok');
  //document.body.toggleClassName('normal-page');
}

function setMessage(msgId,classId){
	var emsg = $('messages');
	Element.addClassName(emsg, classId);
	var msgText="";
	if (typeof getMessage != "undefined"){msgText=getMessage(msgId);}
	Element.update(emsg, msgText);	
}

function formatAttempts(attempts, maxdeliver){
	if (parseInt(maxdeliver)>0) attempts+="/"+maxdeliver;
	return attempts;
}


function updateAttempts(attempts, maxdeliver){
	Element.update($('attempts'), ''+formatAttempts(attempts, maxdeliver));
	if (parseInt(maxdeliver)>0 && parseInt(attempts)>=parseInt(maxdeliver)){
		disableSection();
	}
	
}

function updateState(state){
  var txt="";
  if (state>=0) txt="-"+state;
  if ($('state')!=null){
  	$('state').className='';
	  $('state').toggleClassName('state'+txt);
  }
}


function updateScores(score){
	////alert("updateScores("+score+")");
	if (score==null) score=getApplet("app_1").getScore();
	Element.update($('score'), ''+score+'');
}

function updateItemScore(itemId, score){ //Albert
	var eScore = $('divscore_'+itemId);
	if (eScore!=null){
		if (eScore.style!=null){//A2
			eScore.style["visibility"]="visible";
		}							
		Element.show(eScore);
	}

	//alert("updateItemScore("+itemId+","+score+")");
	//if (score==null) score=getApplet("app_1").getScore();
	var e = $('score_'+itemId);
	var obj = document.getElementById("txt_score_"+itemId);
	if (obj!=null){ //Hi ha camp de text
		obj.value=score;
	} else if (e!=null){ //No hi ha camp de text
		Element.update(e, ''+score+'');
	}
}

function changeCorrect(itemId){ //Albert
	//alert("changeCorrect");

	if (isByHandCorrectEditable){
	  var e = $('feedback_'+itemId);
	  if (e!=null){
		var correct = !Element.hasClassName(e, 'item-feedback-ok');
		if (correct){
			if (Element.hasClassName(e, 'item-feedback-ko')) Element.removeClassName(e, 'item-feedback-ko');
			Element.addClassName(e, 'item-feedback-ok');			}else{
			if (Element.hasClassName(e, 'item-feedback-ok')) Element.removeClassName(e, 'item-feedback-ok');
			Element.addClassName(e, 'item-feedback-ko');			}
		//var feedback = appl.getFeedback(id);
		////Element.update(e, 'aaa');
		////updateSolutionVisibility(id);


		var e2 = $('punts_'+itemId);
		if (e2!=null){
			//Form.enable(e2);
			document.forms[0].aaa.enabled=true;
			document.forms[0].aaa.editable=true;
		}
	  }
	}
}

function updateTime(time){
	//alert("updateTime("+time+")");
	var e = $('section-time');
	if (e!=null){
		Element.update(e, time);
	}
}

function updateFeedback(scores){
//alert("updateFeedback("+scores+")");
	

	var appl = getApplet('app_1');
	if (appl!=null){
		var corrects;
		if (scores!=null) {
			corrects=scores;
		}else{
			corrects = appl.getScores();
		}
		corrects = corrects.split('#');
		for (i=0;i<corrects.length;i++){
			var t = corrects[i].split('=');
			if (t.length==2){
				var last = t[0].indexOf('_correct');
				if (last>=0){
					var id = t[0].substring(0, last);
					var correct = t[1];
					var e = $('feedback_'+id);
					if (e!=null){
						if (correct=='true'){
							if (Element.hasClassName(e, 'item-feedback-ko')) Element.removeClassName(e, 'item-feedback-ko');
							Element.addClassName(e, 'item-feedback-ok');						
						}else{
							if (Element.hasClassName(e, 'item-feedback-ok')) Element.removeClassName(e, 'item-feedback-ok');
							Element.addClassName(e, 'item-feedback-ko');						
						}
						var feedback = appl.getFeedback(id);
						Element.update(e, ''+feedback);
						updateSolutionVisibility(id);
					}
				}else{
					var last = t[0].indexOf('_score');
					if (last>=0){
						var id = t[0].substring(0, last);
						var score = t[1];
						if (id==appl.getSectionId()){
							updateScores(score);
						} else { //Albert
							updateItemScore(id, score);
						}
					}
				}
			}
		}
	}
	if (isTeacher()){
		updateByHandFeedback();
	}
	return false;
}

function updateByHandFeedback(){
		var corrects = iniResponses;
		corrects = corrects.split('#');
		for (i=0;i<corrects.length;i++){
			var t = corrects[i].split('=');
			if (t.length==2){
				var last = t[0].indexOf('_manual_corr');
				if (last>=0){
					var id = t[0].substring(0, last);
					var correct = t[1];
					var e = $('feedback_'+id);
					if (e!=null){
						if (correct=='true'){
							if (Element.hasClassName(e, 'item-feedback-ko')) Element.removeClassName(e, 'item-feedback-ko');
							Element.addClassName(e, 'item-feedback-ok');						
						}else{
							if (Element.hasClassName(e, 'item-feedback-ok')) Element.removeClassName(e, 'item-feedback-ok');
							Element.addClassName(e, 'item-feedback-ko');						
						}
						
					}
				}else{
					var last = t[0].indexOf('_manual_sco');
					if (last>=0){
						var id = t[0].substring(0, last);
						var score = t[1];
						updateItemScore(id, score);
					}
				}
			}
		}
	return false;
}

function updateSolutionVisibility(elemid, first){
  //return updateVisibility('solution', elemid, first && (!(getQueryParam('showcorrection')=='1')));
  return updateVisibility('solution', elemid, first);
}
function updateHintVisibility(elemid, first){
  return updateVisibility('hint', elemid, first);
}
function updateVisibility(type, elemid, first){
  if ($(type+'_'+elemid)!=null){
    Element.hide(type+'_'+elemid);
    if (!first && (!notNull(getQueryParam('show'+type)) || getQueryParam('show'+type)=='1') && !Element.empty(type+'_'+elemid)){
      Element.show(type+'bt_'+elemid);
    }else{
      Element.hide(type+'bt_'+elemid);
    }
  }


/*  if (!first && getQueryParam('show'+type)=='1' && !Element.empty(type+'_'+elemid)){
    Element.show(type+'bt_'+elemid);
  }else{
    Element.hide(type+'bt_'+elemid);
  }*/
  return false;
}


function getMessage(id){
	if (eval("window."+id)!=null) return eval(id);
	return id;
}

function writeMessage(id){
	document.write(getMessage(id));
}


function addInteraction(itemid){
	if (Form.getInputs(document.forms[0], 'text', itemid+"_new")==""){
		var html="<div id='"+itemid+"_new_div'><input type='text' class='new_int' name='"+itemid+"_new' size='105'/><A id='send-interaction-bt' href='javascript:sendInteraction(\""+itemid+"\");'><div alt='"+getMessage('action_send_msg')+"'></div><div id='send-interaction-txt'>"+getMessage('action_send_msg')+"</div></A></div>";
		new Insertion.Bottom(itemid, html);
	}
}

function sendInteraction(itemid){
	if (isWorkingWithServer()){
		var app = getApplet('app_1');
		if (app!=null){
			var message = Form.Element.getValue(Form.getInputs(document.forms[0], 'text', itemid+"_new")[0]);
			var success = function(t){sendInteractionComplete(t, itemid, message);}
		 	var failure = function(){sendInteractionFailed();}
		
		 	var service = getServer();
    	var pars = '<'+'?xml version="1.0" encoding="UTF-8"?'+'>\n'
      pars+='<bean id="add_message" >\n';
      pars+=' <param name="assignmentid" value="'+getQueryParam('assignmentid')+'"/>\n';
    	pars+=' <param name="sectionid" value="'+app.getSectionId()+'"/>\n';
    	pars+=' <param name="itemid" value="'+itemid.substr(8)+'"/>\n';
    	pars+=' <param name="userid" value="'+getQueryParam('userid')+'"/>\n';
//    	pars+=" <message><![CDATA["+utf8_encode(message)+"]]></message>\n";
    	pars+=" <message><![CDATA["+message+"]]></message>\n";
    	pars+="</bean>";
    	callService(service, pars, success, failure);
     	//var myAjax = new Ajax.Request(service, {method:'post', contentType: 'text/xml', postBody:pars, onSuccess:success, onFailure:failure});	  
		}		
	}
}

function sendInteractionComplete(t, itemid, message){
	Element.hide($(itemid+"_new_div"));
	addMessage(itemid, message);
}

function addMessage(itemid, msg, isother){
	if ($(itemid).innerHTML.toLowerCase().indexOf('<br')>=0) Element.update(itemid, "");
	if (isother) html="<div class='item-interaction-msg-other'>"
	else html="<div>";
	html+=msg+"</div>";
	new Insertion.Bottom(itemid, html);		
}

function sendInteractionFailed(){
	alert('sendInteractionFailed');
}


function getMessages(){
	var app = getApplet("app_1");
	if (app!=null){
		var success = function(t){getMessagesComplete(t);}
	 	var failure = function(){getMessagesFailed();}
	
	 	var service = getServer();
  	var pars = '<'+'?xml version="1.0" encoding="UTF-8"?'+'>\n<bean id="get_messages" >\n <param name="assignmentid" value="'+getQueryParam('assignmentid')+'"/>\n <param name="sectionid" value="'+app.getSectionId()+'"/>\n</bean>';
  	callService(service, pars, success, failure);
   	//var myAjax = new Ajax.Request(service, {method:'post', contentType: 'text/xml', postBody:pars, onSuccess:success, onFailure:failure});	  
	}
}

function getMessagesComplete(t){
	var messages = getXMLElements(t.responseXML, 'message');
	var currentuserid = getXMLAttribute(getXMLElement(t.responseXML, 'bean'), 'userid');
	if (currentuserid=='') currentuserid=getQueryParam('userid');
	for(var i=0;i<messages.length;i++){
		var itemid=getXMLAttribute(messages[i], 'itemid');
		var userid=getXMLAttribute(messages[i], 'userid');
		var username=getXMLAttribute(messages[i], 'username');
		var text = getXMLText(messages[i], null);
		addMessage("int_msg_"+itemid, username+": "+text, currentuserid!=userid);
		//alert(messages[i].getElementsByTagName('text')[0].firstChild.nodeValue);
	}
}
function getMessagesFailed(){
	alert("getMessagesFailed");
}

function callService(service, pars, success, failure){
	//alert("pars:"+pars);
  var appl=getApplet('app_1');
	if (appl!=null){
//    var response = appl.callService(service, pars, "UTF-8");
    var response = appl.callService(service, pars);
    if (response!=""){
      var t = new Response(response);
      success(t);
    } else{
      failure();
    }
  }else{
   	var myAjax = new Ajax.Request(service, {method:'post', contentType: 'text/xml', encoding: 'UTF-8', postBody:pars, onSuccess:success, onFailure:failure});  
  }
}

function getSections(){
	var success = function(t){getSectionsComplete(t);}
 	var failure = function(){getSectionsFailed();}

 	var service = getServer();
	var pars = '<'+'?xml version="1.0" encoding="UTF-8"?'+'>\n<bean id="get_sections" >\n <param name="assignmentid" value="'+getQueryParam('assignmentid')+'"/>\n</bean>';
  //service = "http://phobos.xtec.cat/dpllphp1/moodle/moodle16/mod/qv/action/beans.php";
  //pars = '<'+'?xml version="1.0" encoding="UTF-8"?'+'>\n<bean id="get_sections" >\n <param name="assignmentid" value="2"/>\n</bean>';
  //var myAjax = new Ajax.Request(service, {method:'post', contentType: 'text/xml', postBody:pars, onSuccess:success, onFailure:failure});
  callService(service, pars, success, failure);	  
}

function getSectionsComplete(t){
	var assignmentScore = 0;
  var sections = getXMLElements(t.responseXML, 'section');
	for(var i=0;i<sections.length;i++){
		var sectionid=getXMLAttribute(sections[i], 'id');
		var state=getXMLAttribute(sections[i], 'state');
		var e = $(sectionid+"_state")
		if (e){
			if (Element.hasClassName(e, 'state')) Element.removeClassName(e, 'state');
			if (Element.hasClassName(e, 'state-0')) Element.removeClassName(e, 'state-0');
			if (Element.hasClassName(e, 'state-1')) Element.removeClassName(e, 'state-1');
			if (Element.hasClassName(e, 'state-2')) Element.removeClassName(e, 'state-2');
			Element.addClassName(sectionid+"_state", 'state-'+state);
			$(sectionid+"_state").title=getMessage("state_"+state);
		}
		if ($(sectionid+"_state_txt")){
		  $(sectionid+"_state_txt").update(state);
		}

		var maxdeliver=getXMLAttribute(sections[i], 'maxdeliver');
		var attempts=getXMLAttribute(sections[i], 'attempts');
		if ($(sectionid+"_attempts")){
			Element.update($(sectionid+"_attempts"), formatAttempts(attempts, maxdeliver));
		}
		var time=getXMLAttribute(sections[i], 'time');
		if ($(sectionid+"_time") && isWorkingWithServer()){
			Element.update($(sectionid+"_time"), time);
		}

    	   if (isTeacher() || getXMLAttribute(sections[i], 'showcorrection')>0){		
  		var scores="";
		if (isTeacher()){//A
			scores = getXMLAttribute(sections[i], 'pending_scores');//A
		} else {
			scores = getXMLAttribute(sections[i], 'scores');
		}
  		if (scores.indexOf(sectionid+'_score=')>=0){
  			var iScore =scores.indexOf(sectionid+'_score=');
  			var score=scores.substring(iScore+(sectionid+'_score=').length,scores.substring(iScore).indexOf('#'));
  			if ($(sectionid+"_score")){
  				Element.update($(sectionid+"_score"), score);
  				assignmentScore+=parseFloat(score);
  			}
  		}
		
  	   }
	}
	/*var section_order_attrib=getXMLAttribute(getXMLElement(t.responseXML, 'bean'), 'section_order');
	if (section_order_attrib!=null){
		alert("indica section_order="+section_order_attrib);
		//section_order=
	} else {
		alert("no indica section_order");
	}*/
	if ($("assignment-time")){
		var time = getXMLAttribute(getXMLElement(t.responseXML, 'bean'), 'time');//Albert
		Element.update($("assignment-time"), ""+time);
	}
	
	if ($("assignment-score")){
		Element.update($("assignment-score"), ""+assignmentScore);
	}

}

function getSectionsFailed(){
	alert('getSectionsFailed');
}

function initMessages(){
  if (notNull(getQueryParam('lang'))){
    lang=getQueryParam('lang');
    includeScript('qv_messages_'+lang+'.js');
    //document.write('<SCRIPT type="text/javascript" src="scripts/qv_messages_'+lang+'.js">'+'</'+'SCRIPT>');
  }
}

function writeSectionTimeHeaderCell(){
	/*document.write("<th width=\"20\">");
	document.write("<div class=\"section-info\">");
	document.write(getMessage('sectiontime'));
	document.write("</div>");
	document.write("</th>");*/
	if ($("section_header_time")){
		Element.update($("section_header_time"), getMessage('sectiontime'));
	}
}

function writeSectionTimeCell(num, id){
	document.write("<td class=\"section-info\">");
	document.write("<span id=\"section_time_opt"+num+"\">");
	document.write("<div class=\"section-info\" id=\""+id+"_time\">&nbsp;</div>");
	document.write("</span>");
	document.write("</td>");
}

function writeColspan(num){
	document.write("<td colspan=\""+num+"\"></td>");
}

function init(){
	//A5 -->
	if (hasToCheckUser()){
		var logged = checkUserIsLogged();
		if (!logged)
			gotoLoginPage();
		iniConfigFilePaths();
	}

	//<-- A5
	if (isWorkingWithServer() || hasToCheckUser()){
		getResponses();
		getMessages();
		if (isTeacher()){
			//Albert disableSection();
			disableSectionPunts(true);
		}
	} else {
		updateScoreFields(false);
	}
	showScores();
}

function initSections(){
	//A5 -->
	if (hasToCheckUser()){
		var logged = checkUserIsLogged();
		if (!logged)
			gotoLoginPage();
		iniConfigFilePaths();
	}
	
	//<-- A5
	if (isWorkingWithServer() || hasToCheckUser()){
		getSections();
	}
}

function getAppletsBase(){
  var base = getQueryParam('appl');
  if (isNull(base)) base='./appl';
  if (!endsWidth(base, '/')) base+="/";
  return base;
}

function writeCorrectQVApplet(assessment, section, section_number){
	//A5
	var check = hasToCheckUser();


  var params = "assessment="+assessment+"$$section="+section+"$$section_number="+section_number+"$$responses="+filter(getQueryParam('param'))+"$$";

/*	if (check){
		params = params + "loginFileRelativePath=../../login/login.properties$$";
	}*/

  writeJavaApplet('edu.xtec.qv.player.CorrectQVApplet', getAppletsBase(), 'qv_local.jar', params, 0, 0, null, null, 'qvApplet', 'app_1', true);
}

/*function getCorrectQVApplet(assessment, section, section_number){
  var params = "assessment="+assessment+"$$section="+section+"$$section_number="+section_number+"$$responses="+filter(getQueryParam('param'))+"$$";
  var sApplet = getJavaApplet('edu.xtec.qv.player.CorrectQVApplet', getAppletsBase(), 'qv_local.jar', params, 0, 0, null, null, 'qvApplet', 'app_1', true);
  return sApplet;
}*/



function Response(text){
    this.responseText = text;
    if (document.implementation.createDocument){ 
      // Mozilla, create a new DOMParser 
      this.responseXML = (new DOMParser()).parseFromString(text, "text/xml");
    } else if (window.ActiveXObject){
    // Internet Explorer, create a new XML document using ActiveX 
    // and use loadXML as a DOM parser. 
    this.responseXML = new ActiveXObject("Microsoft.XMLDOM"); 
    this.responseXML.async="false"; 
    this.responseXML.loadXML(text);
  }    
    
    
}

function openPopup(type, divname){
  var divwin = dhtmlwindow.open('div'+divname, 'div', divname, getMessage(type+"_bt"), 'width=450px,height=300px,left=200px,top=150px,resize=1,scrolling=1'); 
  return false;
}


//A5-->

function getServer(){
	if (isWorkingWithServer()) {
		server = getQueryParam('server');
		if (server.indexOf("$$") >= 0) {
			server = server.substring(2, server.length - 2);
		}
		return server;
	} else {
		return "local";		
	}
}

function iniConfigFilePaths(){
	appl=getApplet('app_1');
	if (appl!=null){
		if (notNull(getQueryParam('loginFilePath'))) 
			appl.setLoginFilePath(getQueryParam('loginFilePath'));
		if (notNull(getQueryParam('configFilePath'))) 
			appl.setConfigFilePath(getQueryParam('configFilePath'));
		if (notNull(getQueryParam('dataFileRelativePath')))
			appl.setDataRelativeFilePath(getQueryParam('dataFileRelativePath'));
	}
}

//<--A5

//OLD-->


function getSurname(){
	var surname = getQueryParam('p_surname');
	if (surname!=null){
		//surname = replaceChars(surname, '+', " ");
		surname = replaceChars('+', " ", surname);
				while (surname.indexOf("%")>=0){
			surname=unescape(surname);
		}
	}
	return surname;
}

function getGroup(){
	var group = getQueryParam('p_group');
	return group;
}

//A5


//<--OLD