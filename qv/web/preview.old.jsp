<%@page contentType="text/html; charset=ISO-8859-1" import="edu.xtec.qv.admin.beans.QVPreviewBean;

import edu.xtec.qv.admin.beans.SelectOpt"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.admin.beans.QVPreviewBean" scope="request" /><%
if(!qvb.init(request, session, response)){%>
<%}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">





<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<title>Generador de pàgines web de QV</title>
	<LINK TYPE="text/css" href="qv_html.css" rel="stylesheet" />
	<LINK rel="icon" type="image/x-icon" href="favicon.ico"/>
    <LINK rel="shortcut icon" type="image/x-icon"  href="favicon.ico"/>

<script language="JavaScript" src="http://clic.xtec.net/qv/dist/launchQV.js" type="text/javascript"></script>

<script type="text/javascript">
function  refreshUsername(){
	var form = document.qv_form;
	form.submit();
}

function visualitza(){
	var pars=getParameters();
	if (pars.length>0){
		launchQV(pars);
	}
}

function refreshHTML(){
	var form = document.qv_form;
	var t="<A href=\"#\" onclick=\"launchQV('"+getParameters()+"');\">Enllaç</A>";
	form.p_params.value=t;
}

function getParameters(){
	var form = document.qv_form;
	var pars="";
	if (form.tipus_qv.value==null){
		if (form.tipus_qv[0].checked){
			if (form.p_username.value.length>0) pars ="user="+form.p_username.value;
			if (form.p_quadern.value.length>0)	pars +=",quadern="+form.p_quadern.value;
		} else if (form.tipus_qv[1].checked){
			if (form.p_biblioteca.value.length>0) pars ="quadern="+form.p_biblioteca.value;
			if (form.p_lang.value.length>0) pars+=",lang="+form.p_lang.value;
		} else if (form.tipus_qv[2].checked){
			if (form.p_xml.value.length>0)	pars ="xml="+form.p_xml.value;
		}
	}else {
		if (form.p_username.value.length>0){
			if (pars.length>0) pars+=","; 
			pars +="user="+form.p_username.value;
		}
		if (form.p_quadern.value.length>0){
			if (pars.length>0) pars+=","; 
			pars +="quadern="+form.p_quadern.value;
		}
		if (form.p_xml.value.length>0){
			if (pars.length>0) pars+=","; 
			pars +="xml="+form.p_xml.value;
		}
	}
	if (form.p_skin.value.length>0){
		if (pars.length>0) pars+=",";
		pars += "skin="+form.p_skin.value;
	}
	if (form.p_page.value.length>0) {
		if (pars.length>0) pars+=",";
		pars += "page="+form.p_page.value;
	}
	return pars;
}

function disable(v){
	switch(v){
		case "usuari":
			disableControl("usuari", false);
			disableControl("biblioteca", true);
			disableControl("xml", true);
			break;
		case "biblioteca":
			disableControl("usuari", true);
			disableControl("biblioteca", false);
			disableControl("xml", true);
			break;
		case "xml":
			disableControl("usuari", true);
			disableControl("biblioteca", true);
			disableControl("xml", false);
			break;
	}
	refreshHTML();
}

function disableControl(nom, disable){
	var form = document.qv_form;
	switch(nom){
		case "usuari":
			form.p_username.disabled=disable;
			form.p_quadern.disabled=disable;
			break;
		case "biblioteca":
			form.p_biblioteca.disabled=disable;
			form.p_lang.disabled=disable;
			break;
		case "xml":
			form.p_xml.disabled=disable;
			break;
		case "target":
			form.p_page_title.disabled=disable;
			form.p_previous_text.disabled=disable;
			form.p_title.disabled=disable;
			form.p_next_text.disabled=disable;
	}
}

function open_popup(page, title, width, height) {
	var features = "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width="+width+",height="+height;
	var popup = window.open(page,title, features);
	popup.focus();
    return popup;
}

function collapse(id){
  var oTemp=document.getElementById(id);
  var form = document.qv_form;
  if(oTemp.style.display=="block"){
    oTemp.style.display="none";
  }
  else{
    oTemp.style.display="block";
  }
  form.p_html_code.value=oTemp.style.display;
}
	
</script>
<%
boolean bSelectQV = request.getParameter("p_select_qv")==null  || !request.getParameter("p_select_qv").equals("false");
if (!bSelectQV && (qvb.getUsername()==null || qvb.getUsername().trim().length()==0) && (qvb.getQuadern()==null || qvb.getQuadern().trim().length()==0)  && (qvb.getXML()==null || qvb.getXML().trim().length()==0) ){
	bSelectQV = true;
}
String sHTMLCode = request.getParameter("p_html_code")!=null?request.getParameter("p_html_code"):"none";


%>
</head>

<body onload="refreshHTML();">
<form name="qv_form" method="post" action="preview.jsp" target="_self">
<input type="hidden" name="p_html_code" value="<%=sHTMLCode%>"/>
<input type="hidden" name="p_select_qv" value="<%=bSelectQV%>"/>
<table border="0" cellpadding="2" cellspacing="0" width="100%">
<tr>
	<td align="right" class="title">Generador de pàgines web de QV</td>
</tr>
</table>
<br>
<table border="0" cellpadding="2" cellspacing="0" class="box" width="100%">
<!--  PARAMETRES DEL QUADERN -->

<!--  URL DEL QUADERN -->
<% if (bSelectQV){ 

%>
<tr>
	<td><input type="radio" name="tipus_qv" value="usuari" <%=(qvb.getTipus()==null || QVHTMLBean.TIPUS_USUARI.equals(qvb.getTipus()))?"checked":""%> onclick="disable(this.value);">Quadern creat amb l'Editor de QV</td>
</tr>
<tr>
	<td style="padding-left:50px">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td>Nom d'usuari&nbsp;</td>
			<td><input type="text" name="p_username" value="<%=qvb.getUsername()%>" onchange="refreshUsername();refreshHTML();" <%=(qvb.getTipus()==null || QVHTMLBean.TIPUS_USUARI.equals(qvb.getTipus()))?"":"DISABLED"%> ></td>
			<td>&nbsp;<A href="#" onclick="refreshUsername();" border="0">refresca</A></td>
			<td width="20px"></td>
			<td>Nom del quadern&nbsp;</td>
			<td>
				<select name="p_quadern" style="width:170px" <%=(qvb.getTipus()==null || QVHTMLBean.TIPUS_USUARI.equals(qvb.getTipus()))?"":"DISABLED"%> onchange="refreshHTML();">
<%
	java.util.Enumeration enumQuaderns = qvb.getQuaderns().elements();


%>					
					<option value=""><%=enumQuaderns.hasMoreElements()?"&lt;Tria un quadern&gt;":""

%></option>
<%
	while(enumQuaderns.hasMoreElements()){
		edu.xtec.qv.admin.beans.util.SelectOptionObject oOption = (edu.xtec.qv.admin.beans.util.SelectOptionObject)enumQuaderns.nextElement();



%>
					<OPTION value="<%=oOption.getText()%>"><%=oOption.getText()


%></OPTION>
<%			
	}



%>	
					
				</select>
			</td>
		</tr>
		</table>
	</td>
</tr>
<tr>
	<td><input type="radio" name="tipus_qv" value="biblioteca" <%=QVHTMLBean.TIPUS_BIBLIOTECA.equals(qvb.getTipus())?"checked":""%>  onclick="disable(this.value);">Quadern de la Biblioteca de QV</td>
</tr>
<tr>
	<td style="padding-left:50px">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td>Nom del quadern&nbsp;</td>
			<td><input type="text" name="p_biblioteca" <%=(QVHTMLBean.TIPUS_BIBLIOTECA.equals(qvb.getTipus()))?"":"DISABLED"%> onchange="refreshHTML();"></td>
			<td width="46"></td>
			<td>Idioma&nbsp;</td>
			<td>
				<SELECT name="p_lang" <%=(QVHTMLBean.TIPUS_BIBLIOTECA.equals(qvb.getTipus()))?"":"DISABLED"%> >
					<OPTION value="">&lt;Tria un idioma&gt;</OPTION>
					<OPTION value="ca">català</OPTION>
					<OPTION value="es">espanyol</OPTION>
					<OPTION value="en">anglès</OPTION>
				</SELECT>
			</td>
		</tr>
		</table>
	</td>
</tr>
<tr>
	<td><input type="radio" name="tipus_qv" value="xml" <%=QVHTMLBean.TIPUS_XML.equals(qvb.getTipus())?"checked":""%>  onclick="disable(this.value);">Adreça del quadern</td>
</tr>
<tr>
	<td style="padding-left:50px">
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td>Enllaç&nbsp;</td>
			<td><input type="text" name="p_xml" size="70" <%=(QVHTMLBean.TIPUS_XML.equals(qvb.getTipus()))?"":"DISABLED"%> onchange="refreshHTML();"></td>
		</tr>
		</table>
	</td>
</tr>
<%} else{ 

%>
	<input type="hidden" name="tipus_qv"/>
	<input type="hidden" name="p_username" value="<%=qvb.getUsername()!=null?qvb.getUsername():""%>"/>
	<input type="hidden" name="p_quadern" value="<%=qvb.getQuadern()!=null?qvb.getQuadern():""%>"/>
	<input type="hidden" name="p_xml" value="<%=qvb.getXML()!=null?qvb.getXML():""%>"/>
<%} 

%>
<tr>
	<td style="height:10px"></td>
</tr>
<!--  APARENÇA I FULL DEL QUADERN -->
<tr>
	<td>
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td width="20px"></td>
			<td>Aparença&nbsp;</td>
			<td>
				<select name="p_skin" style="width:170px" <%=(qvb.getTipus()==null || QVHTMLBean.TIPUS_USUARI.equals(qvb.getTipus()))?"":"DISABLED"%> onchange="refreshHTML();">
<%
	java.util.Enumeration enumAparences = qvb.getAparences().elements();


%>					
					<option value=""><%=enumAparences.hasMoreElements()?"&lt;Tria una aparença&gt;":""

%></option>
<%
	while(enumAparences.hasMoreElements()){
		edu.xtec.qv.admin.beans.util.SelectOptionObject oOption = (edu.xtec.qv.admin.beans.util.SelectOptionObject)enumAparences.nextElement();


%>
					<OPTION value="<%=oOption.getText()%>" <%=oOption.getText().equals(request.getParameter("p_skin"))?"selected":""%>><%=oOption.getValue()%></OPTION>
<%			
	}
%>				
				</select>
			</td>
			<td width="20px"></td>
			<td>Full&nbsp;</td>
			<td><input type="text" name="p_page" size="1" onchange="refreshHTML();" value="<%=request.getParameter("p_page")!=null?request.getParameter("p_page"):""%>"/></td>
			<td style="text-align:right; width:40%"><A href="#" onclick="visualitza();">Visualitza el quadern</A></td>
		</tr>
		</table>
	</td>
</tr>
<tr>
	<td style="height:10px"></td>
</tr>
</table>

<br>
<table border="0" cellpadding="2" cellspacing="0" class="box" width="100%">
<!--  PARAMETRES HTML -->
<tr>
	<td><input type="radio" name="p_target" value="_self" <%=(qvb.getTarget()==null || QVHTMLBean.TARGET_SELF.equals(qvb.getTarget()))?"checked":""%> onclick="disableControl('target', true);">El Quadern Virtual ocupa tota la pàgina</td>
</tr>
<tr>
	<td><input type="radio" name="p_target" value="" <%=QVHTMLBean.TARGET_BLANK.equals(qvb.getTarget())?"checked":""%>  onclick="disableControl('target', false);">Enllaç al Quadern Virtual que s'obre en una finestra nova</td>
</tr>
<tr>
	<td style="padding-left:22px">
		<table border="0" cellpadding="1" cellspacing="0">
		<tr>
			<td valign="top">Títol de la pàgina:&nbsp;</td>
			<td><input type="text" size="65" name="p_page_title" title="Títol de la pàgina" <%=QVHTMLBean.TARGET_BLANK.equals(qvb.getTarget())?"":"DISABLED"%>></textarea></td>
		</tr>
		<tr>
			<td valign="top">Codi HTML previ:&nbsp;</td>
			<td><textarea cols="57" rows="3" name="p_previous_text" title="Text previ a l'enllaç del quadern" <%=QVHTMLBean.TARGET_BLANK.equals(qvb.getTarget())?"":"DISABLED"%>></textarea></td>
		</tr>
		<tr>
			<td valign="top">Text de l'enllaç&nbsp;</td>
			<td><input type="text" name="p_title" size="65" value="<%=request.getParameter("p_title")!=null?request.getParameter("p_title"):""%>" <%=QVHTMLBean.TARGET_BLANK.equals(qvb.getTarget())?"":"DISABLED"%>/></td>
		</tr>
		<tr>
			<td valign="top">Codi HTML posterior&nbsp;</td>
			<td><textarea cols="57" rows="3" name="p_next_text" title="Text posterior a l'enllaç del quadern" <%=QVHTMLBean.TARGET_BLANK.equals(qvb.getTarget())?"":"DISABLED"%>></textarea></td>
		</tr>
		</table>
	</td>
</tr>
<!--  VISUALITZA I DESA HTML -->
<tr>
	<td style="height:20px"></td>
</tr>
<tr>
	<td align="center">
		<A href="#" onclick="document.qv_form.target='_blank';document.qv_form.action='qv_html.jsp';document.qv_form.submit();">Visualitza pàgina web</A>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<A href="#" onclick="document.qv_form.target='_self';document.qv_form.action='qv_html.jsp?download=true';document.qv_form.submit();document.qv_form.action='preview.jsp'" >Desa pàgina web</A>
	</td>
</tr>
<!--  OPCIONS AVANÇADES -->
<tr>
	<td align="right">
		<A href="#" onclick="refreshHTML();collapse('html_code');" >Opcions Avançades>></A>
	</td>
</tr>
<tr>
	<td>
		<DIV id="html_code" style="margin-top:5; margin-left: 30;  margin-bottom: 10; display: <%=sHTMLCode%>">
			<textarea cols="76" rows="3" name="p_params" readonly></textarea>
		</DIV>
	</td>
</tr>
</table>
</form>
</body>
</html>
