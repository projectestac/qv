<%@page contentType="text/html; charset=ISO-8859-1"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.admin.beans.QVIndexBean" scope="request" /><%
if(!qvb.init(request, session, response)){%>
<%}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<title>Eina d'administració de Quaderns Virtuals</title>
	<LINK TYPE="text/css" href="qv_html.css" rel="stylesheet" />
	<LINK rel="icon" type="image/x-icon" href="http://clic.xtec.net/qv_web/image/favicon.ico"/>
    <LINK rel="shortcut icon" type="image/x-icon"  href="http://clic.xtec.net/qv_web/image/favicon.ico"/>
</head>

<body>
<form name="qv_form" method="post" action="index.jsp" target="_self">

<table border="0" cellpadding="2" cellspacing="0" width="100%">
<tr>
	<td align="right" class="title">Eina d'administració de Quaderns Virtuals</td>
</tr>
<tr>
	<td>
	<%if (qvb.isAdmin() || qvb.isValidator()){ %>
		<br/><br/>
		<UL>
			<LI><A href="preview.jsp">Previsualitzador i generador de pàgines web</A></LI>
			<LI><A href="quote.jsp">Quotes d'usuaris</A></LI>
			<!--LI><A href="act_bd.jsp">Biblioteca d'activitats</A></LI-->
		</UL>
	<%} else {%>
		<script>document.location="preview.jsp";</script>
	<%} %>
	</td>
</tr>
</table>

</form>
</body>
</html>
