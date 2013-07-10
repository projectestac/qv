// --- Constants

var scoring="";
function setScoring(sc){
	scoring=sc;
}

function openWindow(url, popW, popH){
    var w = 800, h = 600, x0=0, y0=0;
    if (document.all || document.layers) {
      w = screen.availWidth;
      h = screen.availHeight;
    }
    //var popW = 788, popH = 540;
    if(popW>(w-12))
      popW=w-12;
    if(popH>(h-28))
      popH=h-28;
    var leftPos = 0, topPos = 0;
    if(w>800 && h>600){
      leftPos = (w-popW)/2;
      topPos = (h-popH)/2;
    }
    window.open(url,'qv','toolbar=yes,directories=false,scrollbars=yes,resizable=yes,width=' + popW + ',height=' + popH + ',top=' + topPos + ',left=' + leftPos+ ',screenY=' + topPos + ',screenX=' +leftPos);
}

// --- Analisi de les respostes ---

function generate_responses(test, bCheck){
  var response="";
  var radios="";
  for(i=0; i<test.elements.length; i++){
    var obj=test.elements[i];
    var v=null;
    var b=bCheck;
    if(obj.type=="checkbox"){
		if (obj.checked) v=obj.id;
		else b=false;
    }else if(obj.type=="radio"){
      b=false;
      if(obj.checked)
	  v=obj.value;
      if(radios.indexOf(obj.name+"#")<0)
        radios+=obj.name+"#";
    }else if(obj.type=="select-one"){
      var k=obj.selectedIndex;
      if(k>0)
        v=obj.options[k].value;
    }else if(obj.type=="text"){
      if(obj.name.indexOf("interaccio_")==0){
        document.forms[0].interaction.value=document.forms[0].interaction.value+"#"+obj.name+"="+obj.value+"#";
        b=false;
      }else if(obj.name.indexOf("txt_score_")==0){//Albert
	  var itemId = obj.name.substring("txt_score_".length);
	  var correctedByHand = hasCorrectedByHandScore(itemId);
	  if (correctedByHand){
		response+=itemId+"_manual_sco="+getByHandCorrectionScore(itemId)+"#";
	  }
	  var correctedByHandCorrect = hasCorrectedByHandCorrect(itemId);
	  if (correctedByHandCorrect){
		response+=itemId+"_manual_corr="+getByHandCorrectionCorrect(itemId)+"#";
	  }
	  b=false;
	}else
	    v=obj.value;
    }else if(obj.type=="textarea"){
      if(obj.name.indexOf("interaccio_")==0){
        document.forms[0].interaction.value=document.forms[0].interaction.value+"#"+obj.name+"="+obj.value+"#";
        b=false;
      }else
	    v=obj.value;
    }else if(obj.type=="hidden"){
		if(obj.name=="param" || obj.name=="interaction" || obj.name=="QTIUrl" || obj.name=="pwdPage" || obj.name=="finishedPage" || obj.name=="endPage" || obj.name=="es_correccio" || obj.name=="time" ||
			obj.name=="next" || obj.name=="wantCorrect"){
	        b=false;
    	}else{
			//b=false;
			//alert("applet?"+obj.name);
			v=getAppletValue(obj.name);
			//alert("v="+v);
    	}
    }else
      b=false;

    if(b && !notEmpty(v, true)){
      response=null;
      break;
    }

    if(v!=null){
      response+=obj.name+"="+v+"#";
    }
  }


  //alert("response:"+response+"<--");
 
  if(response!=null && bCheck && radios.length>0){
    var i=0;
    while(i<radios.length){
      var k=radios.indexOf("#",i);      
      var s=radios.substring(i, k);
      if(response.indexOf(s+"=")<0){
        notEmpty(null, true);
        response=null;
        break;
      }
      i=k+1;
    }
  }
  //alert("response:"+response+"<--");
  return response;
}

function manualChangedResponsesOrScores(){//Albert
	var changed = false;
	var currentResponses = generate_responses(document.forms[0], false);
	if (/*iniResponses != currentResponses &&*/ (currentResponses.indexOf("_manual_")>0 || iniResponses.indexOf("_manual_")>0))
		changed = true;
	return changed;
}

//SCORE BY HAND CORRECTION
function hasCorrectedByHandScore(itemId){//Albert
	//Retorna si s'ha establert manualment la correcció d'un item
	var hasCorrected = false;
	var itemManualSco = itemId+"_manual_sco=";
	var currentByHandCorrection = getByHandCorrectionScore(itemId);
	//alert("currentByHandCorrection="+currentByHandCorrection);	
	var initialScoreAutomatic = getInitialScoreAutomatic(itemId);
	//alert("initialScoreAutomatic="+initialScoreAutomatic);

	if (iniResponses!= null && iniResponses.indexOf(itemManualSco)>=0){ //Ja estava corregit manualment
		hasCorrected = true;
		//alert("Ja estava corregit manualment l'item: "+itemId);
		hasCorrected = true;
	} else if(currentByHandCorrection!=null && currentByHandCorrection.length>0 && currentByHandCorrection!=initialScoreAutomatic){
		//alert("Ha canviat la correcció de l'item:"+itemId);
		hasCorrected = true;
	}

	//alert("hasCorrectedByHand("+itemId+")?"+hasCorrected);
	return hasCorrected;
}

function getInitialScoreByHand(itemId){//Albert
	//Retorna la puntuació manual que havia fet el profesor de l'item
	var iniResp = null;
	var itemManualSco = itemId+"_manual_sco=";

	if (iniResponses!= null && iniResponses.indexOf(itemManualSco)>=0){ //Ja estava corregit manualment
		var ind = iniResponses.indexOf(itemManualSco);
		var end = iniResponses.indexOf("#", ind);
		iniResp = iniResponses.substring(ind+itemManualSco.length, end);

	}
	return iniResp;
}

function getInitialScoreAutomatic(itemId){//Albert
	var iniScore = null;
	var itemSco = itemId+"_score=";

	if (iniScores!=null && iniScores.indexOf(itemSco)>=0){ //Ja estava corregit automàticament
		var ind = iniScores.indexOf(itemSco);
		var end = iniScores.indexOf("#", ind);
		iniScore = iniScores.substring(ind+itemSco.length, end);
	}
	return iniScore;
}


function getByHandCorrectionScore(itemId){//Albert
	var inputName = "txt_score_"+itemId;
	var obj = document.getElementById(inputName);
	if (obj!=null){
		return obj.value;
	}
	return null;
}


//CORRECT BY HAND CORRECTION
function hasCorrectedByHandCorrect(itemId){//Albert
	//Retorna si s'ha establert manualment la correcció d'un item
	var hasCorrected = false;
	var itemManualSco = itemId+"_manual_corr=";
	var currentByHandCorrection = getByHandCorrectionCorrect(itemId);
	//alert("currentByHandCorrection="+currentByHandCorrection);	
	var initialScoreAutomatic = getInitialCorrectAutomatic(itemId);
	//alert("initialScoreAutomatic="+initialScoreAutomatic);

	//alert("currentByHandCorrection!=null?"+currentByHandCorrection!=null);
	//alert("currentByHandCorrection.length="+currentByHandCorrection.length);
	//alert("currentByHandCorrection!=initialScoreAutomatic?"+(currentByHandCorrection+"")!=(initialScoreAutomatic+""));
	

	if (iniResponses!= null && iniResponses.indexOf(itemManualSco)>=0){ //Ja estava corregit manualment
		hasCorrected = true;
		//alert("Ja estava corregit manualment l'item: "+itemId);
		hasCorrected = true;
	} else if(getByHandCorrectionCorrectEstablished(itemId) &&(currentByHandCorrection+"")!=(initialScoreAutomatic+"")){
		//alert("Ha canviat la correcció de l'item:"+itemId);
		hasCorrected = true;
	} else {
		//alert("No estava establerta la correcció manualment i no l'ha corregit");
	}

	//alert("hasCorrectedByHand("+itemId+")?"+hasCorrected);
	return hasCorrected;
}

function getInitialCorrectByHand(itemId){//Albert
	//Retorna la puntuació manual que havia fet el profesor de l'item
	var iniResp = null;
	var itemManualSco = itemId+"_manual_corr=";

	if (iniResponses!= null && iniResponses.indexOf(itemManualSco)>=0){ //Ja estava corregit manualment
		var ind = iniResponses.indexOf(itemManualSco);
		var end = iniResponses.indexOf("#", ind);
		iniResp = iniResponses.substring(ind+itemManualSco.length, end);

	}
	return iniResp;
}

function getInitialCorrectAutomatic(itemId){//Albert
	var iniScore = null;
	var itemSco = itemId+"_correct=";

	if (iniScores!=null && iniScores.indexOf(itemSco)>=0){ //Ja estava corregit automàticament
		var ind = iniScores.indexOf(itemSco);
		var end = iniScores.indexOf("#", ind);
		iniScore = iniScores.substring(ind+itemSco.length, end);
	}
	return iniScore;
}

function getByHandCorrectionCorrectEstablished(itemId){ //Albert
	var established = false;
	var e = $('feedback_'+itemId);
	if (e!=null){
		established = (Element.hasClassName(e, 'item-feedback-ok') || Element.hasClassName(e, 'item-feedback-ko'));
	}
	return established;
}

function getByHandCorrectionCorrect(itemId){ //Albert
	var correct = false;
	var e = $('feedback_'+itemId);
	if (e!=null)
		correct = Element.hasClassName(e, 'item-feedback-ok');
	return correct;
}

/////////////////////////////////////////// END BY HAND CORRECTION

function notEmpty(sText, bAlert){
  var bResult=(sText!=null && sText.length>0);
  if(!bResult && bAlert)
    alert("Cal respondre a totes les preguntes!");
  return bResult;
}

function getAppletValue(name){
	appl=document.applets[name];
	if (appl==null){
		appl = document.applets[0];
		for (var i=0;i<document.applets.length;i++){
			if (document.applets[i].name==name){
				appl = document.applets[i];
				break;
			}
		}
	}
	if (appl!=null){
		//try{
			var s=""+appl.getStringRepresentation();
		//}
		//catch (er){
		//	var s=""+appl.toString();
		//alert("s="+s+"<--");
		//}
		return s;
	}
	else return "";
}

// --- Control de submits  ---

var submited=false;

function doSubmit(bCheck){
  var bResult=false;
  var bSubmit = false;
  var s=generate_responses(document.forms[0], bCheck);
  if(s!=null && s.length>0){
   	document.forms[0].param.value=s;
	bSubmit = true;
  }else if (!bCheck){
	document.forms[0].param.value=" ";
	bSubmit = true;
  }  
  updateTime();
  if (bSubmit){
    submited=true;
    document.forms[0].submit();
    //setTimeout("submited=false", 5000);
    bResult=true;
  }
  return bResult;
}


function isSubmitted(){
  if(submited)
    alert("La vostra resposta s'est? processant. Espereu un moment, si us plau...");
  return submited;
}

function updateTime(){
	document.forms[0].time.value=getPassedTime();
}

// --- Marcatge dels valors actuals de les respostes ---

function select_responses(form, responses){
  //iniResponses = responses;//Albert
//alert("select_responses-> "+responses);
  for (i=0;i<form.elements.length;i++){
    var obj=form.elements[i];
    var name=obj.name;
    var k=responses.indexOf(name+"=");
    if(k>=0){    
      var z=responses.indexOf('#', k);
      var value=responses.substring(k+name.length+1, z);
      /*if((obj.type=="checkbox"||obj.type=="radio") && value==obj.value)
        obj.checked=true;*/
	if(obj.type=="radio" && value==obj.value)
        obj.checked=true;
	else if(obj.type=="checkbox"){
		while (k>=0){
			value=responses.substring(k+name.length+1, z);
			//alert("obj.name="+obj.name+" value="+obj.value+" id="+obj.id);
			// INCREIBLE, PERO AMB L'ALERT DE SOBRE FUNCIONA AMB NS6, SENSE NO.
		 	if (value==obj.value || value==obj.id) obj.checked=true;
			k=responses.indexOf(name,k+1);
			z=responses.indexOf('#', k);
		}
	}
      else if(obj.type=="select-one")
        select_option_list(obj, value);
      else if(obj.type=="text" || obj.type=="textarea")
        obj.value=value;
      else if(obj.type=="hidden"){
		select_applet_response(name,value);
		obj.value=value;
      }
    }
  }
}


function select_applet_response(name, value){
	//alert("select_applet_response"); //("+name+", "+value+")");
	appl=document.applets[name];
	if(appl==null){
		for (ti=0;ti<document.applets.length;ti++){
			if (document.applets[ti].name==name){
				appl = document.applets[ti];
				break;
			}
		}
	}

	if (appl!=null){
		//try{
			//appl.showStatus(value);
		try{
			//alert(appl.code);
			appl.selectInitialResponses(value);
		}catch(ex){
			try{
				appl.initFromParam(value);
			}catch(ex2){
			}
		}
		//}
		//catch (ex){
		//}
	}
}

function select_option_list(select,value){
  for(j=0;j<select.options.length;j++)
    if(select.options[j].value==value)
      select.selectedIndex=j;
}

// --- Applet ---

function set_param_applet(paramName, value){
  for (i=0;i<document.forms[0].elements.length;i++)
    if (document.forms[0].elements[i].name==paramName)
      document.forms[0].elements[i].value=value;
}

function getItemValue(values, itemIdent, valueName){
	var val="";
	var id=itemIdent+"_"+valueName+"=";
	if (values!=null){
		var indId=values.indexOf(id);
		if (indId>=0){
			var indIdEnd=values.indexOf("#",indId);
			val=values.substring(indId+id.length, indIdEnd);
		}
	}
	return val;
}

function writeItemScore(itemIdent){
	document.write(getItemValue(scoring, itemIdent, "score"));
}

function writeItemCorrect(itemIdent){
	document.write(getItemValue(scoring, itemIdent, "correct"));
}

function isItemCorrectionEstablished(itemIdent){
	return (getItemValue(scoring, itemIdent, "correct")=="true" || 
		getItemValue(scoring, itemIdent, "correct")=="false");
}

function isItemCorrect(itemIdent){
	return (getItemValue(scoring, itemIdent, "correct")=="true");
}

function writeItemCorrectCell(itemIdent){
	var bgColor="";
	if (isItemCorrectionEstablished(itemIdent)){
		if (isItemCorrect(itemIdent))
			bgColor="#00ff00"; //green
		else
			bgColor="#ff0000"; //red
		//document.write("<TR><TD BGCOLOR=\""+bgColor+"\" ALIGN=\"RIGHT\">"+getItemValue(scoring,itemIdent,"score")+"</TD></TR>");
		document.write("<TR><TD BGCOLOR=\""+bgColor+"\" ALIGN=\"RIGHT\"></TD></TR>"); //sense mostrar els punts
	}
}

// --- Verificacio de respostes numeriques ---

function verify_integer(text){
  var bResult=(text!=null && text.length>0);
  for (i=0; i<text.length && bResult; i++){
    if (text.charAt(i)<'0' || text.charAt(i)>'9'){
      if (!(i==0 && (text.charAt(0)=='-' || text.charAt(0)=='+'))){ //sign
        alert("ERROR: La resposta hauria de ser un nombre enter.");
        bResult=false;
      }
    }
  }
  return bResult;
}

function verify_decimal(text){
  var bResult=(text!=null && text.length>0);
  var bPunt=false;
  for (i=0; i<text.length && bResult; i++){
    if (text.charAt(i)<'0' || text.charAt(i)>'9'){
      if (!((i==0 && (text.charAt(0)=='-' || text.charAt(0)=='+'))
          || (!bPunt && (text.charAt(i)=='.'||text.charAt(i)==',')))){ //sign
        alert("ERROR: La resposta hauria de ser un nombre real.");
        bResult=false;
      }
      if (text.charAt(i)=='.'||text.charAt(i)==',')
        bPunt=true;
    }
  }
  return bResult;
}

function verify_scientific(text){
  var bResult=(text!=null && text.length>0);
  var bPunt=false;
  var bExp=false;
  for (i=0; i<text.length && bResult; i++){
    if (text.charAt(i)<'0' || text.charAt(i)>'9'){
      if (!((i==0 && (text.charAt(0)=='-' || text.charAt(0)=='+'))
          || (!bPunt && (text.charAt(i)=='.'||text.charAt(i)==','))
          || (!bExp && (text.charAt(i)=='e'||text.charAt(i)=='E')))){ //sign				
        alert("ERROR: La resposta hauria de ser un nombre expressat en notaci? cient?fica ([-]num[.num][e[sign]num]).");
        bResult=false;
      }
      if (text.charAt(i)=='.'||text.charAt(i)==',')
        bPunt=true;
      if (text.charAt(i)=='e'||text.charAt(i)=='E'){
        bExp=true;
        bPunt=true; //no admetem punts a la part decimal
        if (text.length>(i+1) && (text.charAt(i+1)=='-' || text.charAt(i+1)=='+'))
          i++;
      }
    }
  }
  return bResult;
}

function verify_boolean(text){
  var bResult=(text!=null && text.length>0 && 
               (text=="true" || text=="false" || text=="cert" || text=="fals" || text=="si" || text=="no"));
  if(!bResult)
    alert("ERROR: La resposta hauria de ser del tipus 'cert o fals'");
  return bResult;
}

function filter(text){
	document.write(text);
}

