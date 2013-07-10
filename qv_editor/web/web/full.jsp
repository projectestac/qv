<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.QVFullBean qvsb=(edu.xtec.qv.editor.beans.QVFullBean)qvb.getSpecificBean();
String sForm = "fullForm";
if (!qvb.isHeaderLoaded()){
%>
<jsp:include page="header.jsp" flush="true" />
<%}%>

<FORM accept-charset="ISO-8859-1" name="fullForm" method="POST" action="edit.jsp">
<INPUT type="hidden" name="page" value="full"/>
<INPUT type="hidden" name="action"/>
<INPUT type="hidden" name="hide_layers" />
<INPUT type="hidden" name="edition_mode" value="<%=qvb.getEditionMode()%>"/>
<SCRIPT language="javascript">
	setCurrentForm("<%=sForm%>");
</SCRIPT>
<%if (qvb.getEditionMode()!=null && qvb.getEditionMode().equalsIgnoreCase(qvb.TEXT_EDITION_MODE)){%>
<TABLE width="100%" style="height:100%" cellpadding="5" cellspacing="5" border="0" class="edit-box">
<TR>
	<TD align="right" class="edit-title"><%=qvb.getMsg("section")%>&nbsp;&nbsp;&nbsp;</TD>
</TR>
<TR>
	<TD class="edit-text" height="100%">
	<jsp:include page="editorXML.jsp" flush="true">
		<jsp:param name="form" value="<%=sForm%>" />
	</jsp:include>
	</TD>
</TR>
</TABLE>
<%}else{%>
<TABLE class='edit-box' width="100%" cellpadding="10" cellspacing="0" border="0" style='height:100%;'>
<TR>
	<TD valign='top'>
		<!-- INICI Dades del quadern -->
		<jsp:include page="enunciat.jsp" flush="true">
			<jsp:param name="form" value='<%="document."+sForm%>' />
			<jsp:param name="title_pregunta" value='<%=qvsb.getTitle()%>' /> 
			<jsp:param name="scoremodel" value='<%=qvsb.getScoreModel()%>' />
			<jsp:param name="titol_materials" value="Materials de la introducció" />
		</jsp:include>
	</TD>
</TR>
<TR>
	<TD height='15'></TD>
</TR>
</TABLE>
<%}%>
</FORM>
<%if (!qvb.isHeaderLoaded()){
	qvb.loadHeader();
%>
<jsp:include page="footer.jsp" flush="true" />
<%}%>
