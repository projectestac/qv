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
	//select_applet_response(name,value);
	obj.value=value;
      }
    }
  }
}

function select_applet_response(name, value){
	appl=document.applets[name];	
	//alert("select_applet_response name="+name);
	if (appl!=null){
		//try{
			//appl.showStatus(value);
			//alert(name+"  "+value);
			appl.initFromParam(value);
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

function writeAudio(id, file, videotype, width, height){
  document.writeln('<object classid="clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B" id="'+id+'" height="'+height+'" width="'+width+'">');
  document.writeln('<param name="src" value="'+file+'"><param value="false" name="AUTOSTART">');
  document.writeln('<embed type="'+videotype+'" autoplay="false" autostart="false" console="one" controls="ControlPanel" nojava="true" src="'+file+'" height="'+height+'" width="'+width+'">');
  document.writeln('<noembed><a href="'+file+'">'+file+'</a></noembed></object>');            
}