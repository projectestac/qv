
// --- Analisi de les respostes ---

function generate_responses(test, bCheck){
  var response="";
  var radios="";
  for(i=0; i<test.elements.length; i++){
    var obj=test.elements[i];
    var v=null;
    var b=bCheck;
    if(obj.type=="checkbox"){
      v=obj.value;
    }
    else if(obj.type=="radio"){
      b=false;
      if(obj.checked)
	  v=obj.value;
      if(radios.indexOf(obj.name+"#")<0)
        radios+=obj.name+"#";
    }
    else if(obj.type=="select-one"){
      var k=obj.selectedIndex;
      if(k>0)
        v=obj.options[k].value;
    }
    else if(obj.type=="text"){
      v=obj.value;
    }
    else if(obj.type=="textarea"){
      if(obj.name.indexOf("interaccio_")==0){
        document.forms[0].interaction.value=document.forms[0].interaction.value+"#"+obj.name+"="+obj.value+"#";
        b=false;
      }
      else
	  v=obj.value;
    }
    else if(obj.type=="hidden"){
	if(obj.name=="param" || obj.name=="interaction" || obj.name=="QTIUrl" || obj.name=="pwdPage" || obj.name=="finishedPage" || obj.name=="endPage")
        b=false;
      else{
	//b=false;
	v=getAppletValue(obj.name);
	//alert("v="+v);
      }
    }
    else
      b=false;


    if(b && !notEmpty(v, true)){
      response=null;
      break;
    }

    if(v!=null)
      response+=obj.name+"="+v+"#";

  }
 
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

  return response;
}

function getAppletValue(name){
	appl=document.applets[name];
	if (appl!=null){
		try{
			s=appl.getStringRepresentation();
		}
		catch (er){
			s=appl.toString();
		}
		return s;
	}
}

function getLength(s){
	if (s==null) return 0;
	var i=0;
	try{
		i=s.length();
	}
	catch (er){
		i=s.length;
	}
	return i;
}

function notEmpty(sText, bAlert){
  var i=getLength(sText);
  var bResult=(sText!=null && i>0);
	//alert("bResult="+bResult+" sText!=null?"+(sText!=null)+" sText.length="+sText.length());
  if(!bResult && bAlert)
    alert("Cal respondre a totes les preguntes!");
  return bResult;
}

// --- Control de submits duplicats ---

var submited=false;

function checkSubmit(){
  var bResult=!submited;
  if(!bResult)
    alert("La vostra resposta s'està processant. Espereu un moment, si us plau...");
  return bResult;
}

function doSubmit3(){
  var bResult=false;
  if(checkSubmit()){
    //document.forms[0].submit();
    setTimeout("submited=false", 5000);
    submited=true;
    bResult=true;
  }
  return bResult;
}

function doSubmit2(){
  var bResult=false;
  var s=generate_responses(document.forms[0], true);
  /////if(s!=null && s.length>0){
  if(s!=null && getLength(s)>0){
    document.forms[0].param.value=s;
    bResult=doSubmit3();
  }
  return bResult;
}

function doSubmit2x(){
  var bResult=false;
  var s=generate_responses(document.forms[0], true);
/////  if(s!=null && s.length>0){
  if(s!=null && getLength(s)>0){
    bResult=doSubmit3();
  }
  return bResult;
}

// --- Marcatge dels valors actuals de les respostes ---

function select_responses(form, responses){
  for (i=0;i<form.elements.length;i++){
    var obj=form.elements[i];
    var name=obj.name;
    var k=responses.indexOf(name);
    if(k>=0){    
      var z=responses.indexOf('#', k);
      var value=responses.substring(k+name.length+1, z);
      if((obj.type=="checkbox"||obj.type=="radio") && value==obj.value)
        obj.checked=true;
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
	if (appl!=null){
		try{
			appl.showStatus(value);
		}
		catch (ex){
		}
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
        alert("ERROR: La resposta hauria de ser un nombre expressat en notació científica ([-]num[.num][e[sign]num]).");
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

