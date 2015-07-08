function launchQV(args){
    launchQVWH(args, 788, 540);
}

function launchQVWH(args, popW, popH){
    var w = 800, h = 600, x0=0, y0=0;
    if (document.all || document.layers) {
      w = screen.availWidth;
      h = screen.availHeight;
    }
    if(popW>(w-12))
      popW=w-12;
    if(popH>(h-28))
      popH=h-28;
    var leftPos = 0, topPos = 0;
    if(w>800 && h>600){
      leftPos = (w-popW)/2;
      topPos = (h-popH)/2;
    }
    
    var url = getQV(args);
    if (url!=null){
       var target=getParameterValue(args, 'target');
	   if (target=="_self"){
	   	document.location=url;
	   } else{
	    window.open(url,'QuadernVirtual','scrollbars=yes,resizable=yes,width='+ popW +',height=' + popH + ',top=' + topPos + ',left=' + leftPos+ ',screenY=' + topPos + ',screenX=' + leftPos);
	   }    
    }
}

function getParameterValue(args, param){
  var value = "";
  istart = args.indexOf(param+"=");
  if (istart!=-1) {
    sep=(args.substr(istart)).indexOf(",");
    if (sep==-1) sep=args.length;
    else sep=sep-param.length-1;
    value=args.substr(istart+param.length+1,sep);
    //alert(param+"="+value+"  sep="+sep+" start="+(istart+param.length+1));
  }
  return value;
}

function getQV(args){
    var url = null;
    args = new String(args);    
    idAssignacio=getParameterValue(args, 'id');
	if (idAssignacio==null || idAssignacio.length==0){
		quadernURL=getParameterValue(args, 'xml');
		if (quadernURL==null || quadernURL.length==0){     
		  quadern=getParameterValue(args, 'quadern');
		  if (quadern.length>0){
			  user=getParameterValue(args, 'user');
			  if (user.length>0){
				// Quadern de l'espai d'usuaris fet amb l'editor
				quadernURL = "http://clic.xtec.cat/qv_viewer/quaderns/"+user+"/"+quadern+"/";
			  }else{
				// Quadern de la biblioteca
				lang=getParameterValue(args, 'lang');
				if (lang==null || lang.length!=2) lang="ca";
				quadernURL = "http://clic.xtec.cat/qv_viewer/quaderns/biblioteca/"+quadern+"_"+lang+"/";
			  }
		  }
		}
		if (quadernURL!=null && quadernURL.length>0) url=quadernURL;
	}
//    if ( (idAssignacio!=null && idAssignacio.length>0) || (quadernURL!=null && quadernURL.length>0) ){
    if ( (idAssignacio!=null && idAssignacio.length>0) || (quadernURL!=null && quadernURL.length<=0) ){
    server=getParameterValue(args, 'server');
    if (server==null || server.length==0){
    	server="http://clic.xtec.cat/qv";
    }
      quadernXSL=getParameterValue(args, 'skin');
      full=getParameterValue(args, 'page');
      color=getParameterValue(args, 'color');
      teacher=getParameterValue(args, 'teacher');
      if (teacher!=null && teacher=="true") url=server+'/getAssignacionsDocent?';
      else if (full!=null && full.length>0) url=server+'/getQuadernAlumne?';
      else url=server+'/getAssignacio?';
      
	  
	  /*if (idAssignacio!=null && idAssignacio.length>0) url+='?assignacioId='+idAssignacio;
	  else url+='?quadernURL='+quadernURL;*/
      
      if (idAssignacio!=null && idAssignacio.length>0) url+='&assignacioId='+idAssignacio;
      if (quadernXSL!=null && quadernXSL.length>0) url+='&skin='+quadernXSL;
      if (color!=null && color.length>0) url+='&color='+color;
      if (full!=null && full.length>0) url+='&section='+full;
    }
    return url;
}
