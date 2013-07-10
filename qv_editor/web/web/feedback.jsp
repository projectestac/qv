<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
	String sFeedbackOK = qvb.getParameter("feedback_ok");
	String sPuntuationOK = qvb.getParameter("puntuation_ok");
	String sFeedbackKO = qvb.getParameter("feedback_ko");
	String sPuntuationKO = qvb.getParameter("puntuation_ko");
	String sHint = qvb.getParameter("hint");
%>

<!-- INICI FEEDBACK -->
<TABLE class='edit-box' width="100%" cellpadding="5" cellspacing="0" border="0">
<TR>
	<TD width="15%" class="edit-text"><%=qvb.getMsg("feedback.ok")%></TD>
	<TD><INPUT type="text" size="60" name="feedback_ok" value="<%=sFeedbackOK%>" class="edit-form"/></TD>
	<TD class="edit-text"><%=qvb.getMsg("feedback.puntuation")%>: <INPUT type="text" size="3" name="puntuation_ok" value="<%=sPuntuationOK%>" class="edit-form"/></TD>	
</TR>
<TR>
	<TD class="edit-text"><%=qvb.getMsg("feedback.ko")%></TD>
	<TD><INPUT type="text" size="60" name="feedback_ko" value="<%=sFeedbackKO%>" class="edit-form"/></TD>
	<TD class="edit-text"><%=qvb.getMsg("feedback.puntuation")%>: <INPUT type="text" size="3" name="puntuation_ko" value="<%=sPuntuationKO%>" class="edit-form"/></TD>
</TR>
<!--TR>
	<TD class="edit-text"><%=qvb.getMsg("hint")%></TD>
	<TD><INPUT type="text" size="60" name="hint" value="<%=sHint%>" class="edit-form"/></TD>
	<TD class="edit-text"></TD>
</TR-->
<INPUT type="hidden" name="hint" value="<%=sHint%>"/>
</TABLE>
<!-- FI FEEDBACK -->
