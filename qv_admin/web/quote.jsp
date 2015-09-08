<%@page contentType="text/html; charset=ISO-8859-1" import="edu.xtec.qv.admin.beans.QVPreviewBean,edu.xtec.qv.admin.SelectOptionObject"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.admin.beans.QVQuoteBean" scope="request" /><%
if(!qvb.init(request, session, response)){%>
<%}%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<title>Administració de QV - Quotes</title>
	<LINK TYPE="text/css" href="qv_html.css" rel="stylesheet" />
	<LINK rel="icon" type="image/x-icon" href="favicon.ico"/>
    <LINK rel="shortcut icon" type="image/x-icon"  href="favicon.ico"/>
</head>

<body>
<form name="qv_form" method="post" action="quote.jsp" target="_self">
<ul>
<li>
	Nom d'usuari&nbsp;
	<%if (qvb.isAdmin()){ %>
			<input type="text" name="p_username" value="<%=qvb.getUsername()%>" onchange="refreshUsername();" />
			<input type="submit" name="p_searchuser" value="cerca"/>
	<%} else {%>
			<input type="hidden" name="p_username" value="<%=qvb.getUserId()%>"/><b><%=qvb.getUserId()%></b>
	<%} %>
</li>	
<li>
	Espai en ús&nbsp;&nbsp;&nbsp;&nbsp;
	<%if (qvb.isAdmin()){ %>
			<input type="text" disabled name="p_usedspace" value="<%="".equals(qvb.getUsername())?"":String.valueOf(qvb.parsetoMB(qvb.getUserUsedSpace()))%>" size="5"/>
	<%} else {%>
			<input type="hidden" name="p_usedspace" value="<%=qvb.getUserUsedSpace()%>"/><b><%=qvb.getUserUsedSpace()%></b>
	<%} %>
	MB
</li>
<li>
	Quota&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<%if (qvb.isAdmin()){ %>
			<input type="text" name="p_quote" value="<%="".equals(qvb.getUsername())?"":String.valueOf(qvb.parsetoMB(qvb.getUserQuote()))%>" size="5"/>
	<%} else {%>
			<input type="hidden" name="p_quote" value="<%=qvb.getUserQuote()%>"/><b><%=qvb.getUserQuote()%></b>
	<%} %>
	MB
</li>
</ul>	
<input type="submit" name="p_save" value="desa"/>
<input type="reset" name="p_reset" value="restaura"/>
<input type="button" name="p_back" value="torna" onclick="document.location='index.jsp'"/>
</form>
</body>
<%@page import="java.util.Enumeration"%>
</html>
