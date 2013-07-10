<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%>
	<jsp:forward page="error.html"/>
<%} else if (!qvb.isValidated()){
	qvb.redirectToValidation();
} else if (qvb.getRedirectPage()!=null){
	qvb.redirectResponse(qvb.getRedirectPage());
} else {%>
	<jsp:forward page="error.html"/>
<%}%>

