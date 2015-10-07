<%@page contentType="text/html; charset=ISO-8859-1"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.admin.beans.QVIndexBean" scope="request" /><%
if(!qvb.init(request, session, response)){%>
<%
}
%>
<jsp:include page="header.jsp" flush="true" />
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

<a href="?action=logout" title="Surt" class="index-link">Surt</a>

</form>
<jsp:include page="footer.jsp" flush="true" />
