function writeResultsTable(content){
	var results = "";
	results+="<TABLE border=1 width=\"85%\">";
	results+=" <TR>";
	results+="  <TD align=\"left\" width=\"50%\"><B>Alumne</B></TD>";
	results+="  <TD align=\"center\"><B>Estat</B></TD>";
	results+="  <TD align=\"center\"><B>Puntuació</B></TD>";
	results+="  <TD align=\"center\"><B>Lliuraments</B></TD>";
	results+="  <TD align=\"center\"><B>Temps</B></TD>";
	results+=" </TR>";
	if (content!='')
		results+=content;
	else
		results+="<TD align=\"center\" colspan=\"5\"></TD>";
	results+="</TABLE>";

	var e = document.getElementById("users");
	if (e!=null){
		//alert("results:"+results);
		e.innerHTML = results;
	} 
}

function getResultsTableBody(){
	var results = "";
	appl=getApplet('app_1');
	if (appl!=null){
		//A Internet Explorer 6 no es reben bé els arrays, es retorna la matriu de la forma m[0][0]:m[0][1]:...m[0][n]|m[1][0]:m[1][1]..m[1][n]|....|m[t][0]..m[t][n]
		//alert("groupId:"+getSelectedGroupId());
		var result = appl.getResultsSummary(getSelectedGroupId());
		//alert("result:"+result);
		////if (result.length()>1){ ATENCIÓ!!!!! COMPROVAR QUE AMB FIREFOX LENGTH TAMBÉ ÉS ATRIBUT...
		//if (result.length>1){
		if (result!=""){
			var studentResultsArray = result.split('!'); 
			for (i=0;i<studentResultsArray.length;i++){
				results+="<TR>";
				var subarray = studentResultsArray[i].split(';');
				var userId = subarray[0];
				var groupId = subarray[1];
				var userName = subarray[2];
				var state = subarray[3];
				var score = subarray[4];
				var attempts = subarray[5];
				var time = subarray[6];
				var teacherLink = subarray[7];
			
//				results+="<TD align=\"left\"><A href=\""+teacherLink+"\">"+userName+"</A></TD>";

b="<TD align=\"left\"><A href=\"#\" onClick=\"javascript:popupWindow(\'"+replaceAllOcc(teacherLink,'\\','/')+"\',800,600);\">"+userName+"</A></TD>";
//alert("b="+b);
results+=b;


				results+="<TD align=\"center\">"+state+"</TD>";
				results+="<TD align=\"center\">"+score+"</TD>";
				results+="<TD align=\"center\">"+attempts+"</TD>";
				results+="<TD align=\"center\">"+time+"</TD>";
				results+="</TR>";
			}
			results+="</TABLE>";
		} else {
			results+="<TR>";
			results+="<TD align=\"center\" colspan=\"5\">No hi ha resultats</TD>";
			results+="</TR>";
		}
	}
	return results;
}

function replaceAllOcc(s, fromChar, toChar){
	var i = s.indexOf(fromChar);
	while (i>=0){
		s = s.substring(0,i)+toChar+s.substring(i+1);//s.replace(fromChar, toChar);
		i = s.indexOf(fromChar);
	}
	return s;
}

function exportResults(){
	var results = "";
	appl=getApplet('app_1');
	if (appl!=null){
		//alert("groupId:"+getSelectedGroupId());
		var result = appl.downloadCSVResultReport(getSelectedGroupId());
		//alert("result:"+result);
	}
}

function popupWindow(url, popW, popH){
	//alert("url="+url);
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