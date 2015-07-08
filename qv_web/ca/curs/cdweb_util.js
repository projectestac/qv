 
var vf=false;     //mantener fecha fija
var v_m=9;		//mes -1     0=Gener
var codi_a="2006-2007";   //"2004-2005"
var v_a=codi_a.substr(0,5);

 
function escribir_des(){
    	
  var s=new String(document.URL);
	var base_url=s.substring(0,s.indexOf("cdweb/")+6);
	var element=new String(s.substring(s.indexOf("/materials/")+11,s.lastIndexOf("/"))).toUpperCase();
	var codi_any=codi_a;
	
    //var ss=base_url+"CDpaquet.Descarga?idTipusElement=material&idElement="+element+"&codiAny="+codi_any;
	element=element.toLowerCase();
	var ss=base_url+"dades/materials/tele/"+codi_any+"/"+element+".zip";
	var sb="icons/descarrega_.gif";
	
	if (document.URL.indexOf("file:")!=-1) {
		document.write("&nbsp;");
		
	} else {	    
		
		document.write("<a href=\""+ss+"\"><img src=\""+sb+"\" width=75 height=25 border=0 alt=\"Descàrrega\"></a>"); 
	}
	
	return true;
}

function escribir_data(m,a){
	var f=new Date();
	var v_mes=new Array("Gener","Febrer","Març","Abril","Maig","Juny","Juliol","Agost","Setembre","Octubre","Novembre","Decembre");
	var mes="";
	var any="";
	var sdata;
	
	if (m>11){ mes=v_mes[f.getMonth()]; } else	{ mes=v_mes[m]; }	
	if (a>0) { any=a; } else { any=f.getYear(); }
		
	sdata=mes+" de "+ any;
	
	document.write(sdata);
	
	return true;
}

function escribir_data_mat(){
     	if (vf) {
     		escribir_data(v_m,v_a);
     	} else {
		if (document.URL.indexOf("file:")!=-1) {
			escribir_data(v_m,v_a);
		} else {	    
			escribir_data(100,0);
		}
     	}	
	return true;		
}
