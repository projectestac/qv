<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.IQVEditorXMLBean qvsb=(edu.xtec.qv.editor.beans.IQVEditorXMLBean)qvb.getSpecificBean();

String sAction = qvb.getParameter("action");
String sXMLText = "";
if (sAction!=null && sAction.equalsIgnoreCase("save_quadern") && qvsb.getErrors().isEmpty()){
	sXMLText = qvsb.QTIObjectToXML();
}else{
	sXMLText = qvb.getParameter("xmlText");
	if (sXMLText==null){
		sXMLText = qvb.getParameter("xml");
		if (sXMLText==null){
			sXMLText = qvsb.QTIObjectToXML();
		}
	}
}
if (sXMLText!=null){
	//sXMLText=edu.xtec.qv.qti.util.StringUtil.filterHTML(sXMLText);
	sXMLText=edu.xtec.qv.qti.util.StringUtil.replaceTagsToHTMLCode(sXMLText);
}
int iLines = qvsb.getNumberLines(sXMLText)+2;
if (iLines<20) iLines = 20;
String sForm = qvb.getParameter("form");
if (!qvb.isHeaderLoaded()){
%>
<jsp:include page="header.jsp" flush="true" />
<%}%>

<TABLE style="height:100%" cellpadding="0" cellspacing="0" border="0">
<TR>
	<TD>
		<TABLE style="height:100%" cellpadding="0" cellspacing="0" border="0">
		<%if (qvsb.getErrorLines()!=null && !qvsb.getErrorLines().isEmpty()){
			java.util.Vector vMsg = new java.util.Vector();
			vMsg.addElement(String.valueOf(qvsb.getErrors().size()));
		%>
		<TR>
			<TD colspan="2" ><IMG src="imatges/alert.gif" width="25"/></TD>
			<TD class="edit-text">
				<%=qvb.getMsg(qvsb.getErrors().size()>1?"xml.more_errors_validating":"xml.one_error_validating", vMsg)%>
			</TD>
		</TR>
		<%}%>
		<TR>
			<TD valign="top" align="right" class="edit-text" >
			<SPAN style="height: 18">&nbsp;</SPAN>
			<%for(int i=1;i<=iLines;i++){
				String sClass =qvsb.isErrorLine(i)?"error-line":"edit-text";
				String sMessage = qvb.filter(qvsb.getErrors(i));
			%>
				<A href="#" class="<%=sClass%>" style="text-decoration:none;" title="<%=sMessage%>"><%=i%>:<BR/></A>
			<%}%>
			</TD>
			<TD width="5"/>
			<TD valign="top" class="edit-text" height="100%">
			<TEXTAREA name="xmlText" rows="<%=iLines%>" cols="85" wrap="off" class="edit-text" ><%=sXMLText%></TEXTAREA>
			</TD>
		</TR>
		</TABLE>
	</TD>
</TR>
<TR>
	<TD>
		<TABLE cellpadding="0" cellspacing="0" border="0">
<%java.util.Enumeration enumLines = qvsb.getErrorLines().elements();
while (enumLines.hasMoreElements()){
Integer iLine=(Integer)enumLines.nextElement();
java.util.Enumeration enumErrorsLine = qvsb.getError(iLine.intValue()).elements();
while (enumErrorsLine.hasMoreElements()){
%>
		<TR>
			<TD valign="top" class="edit-text"><%=qvb.getMsg("xml.line")%>&nbsp;</TD>
			<TD valign="top" align="right" class="edit-text"><B><%=iLine%></B>:&nbsp;</TD>
			<TD valign="top" class="edit-text" ><%=enumErrorsLine.nextElement()%></TD>
		</TR>
<%}
}%>
		<TABLE>
	</TD>		
</TR>
</TABLE>
<%if (!qvb.isHeaderLoaded()){
	qvb.loadHeader();
%>
<jsp:include page="footer.jsp" flush="true" />
<%}%>
