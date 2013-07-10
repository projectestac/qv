<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" /><%
if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.QVLoginBean qvsb=(edu.xtec.qv.editor.beans.QVLoginBean)qvb.getSpecificBean();

if (!qvb.isHeaderLoaded()){
%>
<jsp:include page="header.jsp" flush="true" />
<%}%>	

<FORM name="loginForm" method="POST" action="login.jsp">
<INPUT type="hidden" name="page" value="index"/>
<INPUT type="hidden" name="action"/>

Nom d'usuari/ària: <INPUT type="text" name="p_username">
<br>
Contrasenya: <INPUT type="password" name="p_password">
<br>
<A href="javascript:enviar('login',this.document.loginForm);">Envia</A>
</FORM>
<jsp:include page="footer.jsp" flush="true" />

