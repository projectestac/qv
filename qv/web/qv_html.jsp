<%@page contentType="text/html; charset=ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<HTML>
<HEAD>
	<TITLE><%=request.getParameter("p_page_title")!=null?request.getParameter("p_page_title"):"Quaderns Virtuals"%></TITLE>
<% 
	String sParams = "";
	String sQVFile = "qv";
	String sTipus = request.getParameter("tipus_qv");
	if (sTipus!=null && sTipus.trim().length()>0){
		if ("usuari".equals(sTipus)){
			String sUsername = request.getParameter("p_username");
			String sQuadern = request.getParameter("p_quadern");
			if (sUsername!=null && sQuadern!=null && sQuadern.trim().length()>0){
				sParams="user="+sUsername+",quadern="+sQuadern;
				sQVFile=sQuadern;
			}
		} else if ("biblioteca".equals(sTipus)){
			String sBiblioteca = request.getParameter("p_biblioteca");
			if (sBiblioteca!=null && sBiblioteca.trim().length()>0){
				sParams="quadern="+sBiblioteca;
				sQVFile=sBiblioteca;
			}
		} else if ("xml".equals(sTipus)){
			String sXML = request.getParameter("p_xml");
			if (sXML!=null && sXML.trim().length()>0){
				sParams="xml="+sXML;
			}
		}
	} else{
		String sUsername = request.getParameter("p_username");
		String sQuadern = request.getParameter("p_quadern");		
		String sXML = request.getParameter("p_xml");
		if (sUsername!=null){
			if (sParams.trim().length()>0) sParams += ",";
			sParams+="user="+sUsername;
		}
		if (sQuadern!=null){
			if (sParams.trim().length()>0) sParams += ",";
			sParams+="quadern="+sQuadern;
		}
		if (sXML!=null){
			if (sParams.trim().length()>0) sParams += ",";
			sParams+="xml="+sXML;
		}
	}
	if (sParams.trim().length()>0){
		String sSkin = request.getParameter("p_skin");
		if (sSkin!=null && sSkin.trim().length()>0) {
			sParams+=",skin="+sSkin;
		}
		String sPage = request.getParameter("p_page");
		if (sPage!=null && sPage.trim().length()>0) {
			sParams+=",page="+sPage;
		}
		String sTarget = request.getParameter("p_target");
		if (sTarget!=null && sTarget.trim().length()>0) {
			sParams+=",target="+sTarget;
		}
	}
	String sPrevious=request.getParameter("p_previous_text");
	if (sPrevious==null) sPrevious="";
	String sTitle=request.getParameter("p_title");
	if (sTitle==null || sTitle.trim().length()==0) sTitle="Enllaç al Quadern Virtual";
	String sNext=request.getParameter("p_next_text");
	if (sNext==null) sNext="";

	String sContentType = "text/html";
	if (request.getParameter("download")!=null) {
		sContentType = "application/unknow";
		response.setHeader("Content-Disposition","attachment; filename=\"" + sQVFile + ".html" + "\"");
	}
%>	
	<meta http-equiv="Content-Type" content="<%=sContentType%>; charset=iso-8859-1">
	<script language="JavaScript" src="http://clic.xtec.net/qv/dist/launchQV.js" type="text/javascript"></script>
</HEAD>

<BODY>
<%=request.getParameter("p_target")%>
<%if (sParams.indexOf("target=_self")>=0){ %>
	<script type="text/javascript">
	launchQV('<%=sParams%>');
	</script>
<%} else{%>
	<%=sPrevious%>
	<A href="#" onclick="launchQV('<%=sParams%>');"><%=sTitle%></A>
	<%=sNext%>
<%} %>
</BODY>
</HTML>
