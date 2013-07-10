<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" import="edu.xtec.qv.editor.util.QVMaterialListControl"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
String sLayer = "mattextLayer";

boolean bDisplay = qvb.getBooleanParameter("display", false);
String sText = qvb.getParameter(QVMaterialListControl.P_MATERIAL_TEXT);
%>

<DIV id='<%=sLayer%>' style="position:absolute; top:90; left:25; z-index:1000;display:none;visibility:hidden;">
<TABLE border='0' cellpadding='0' cellspacing='0'>
<TR>
	<TD valign="top">
		<TEXTAREA rows='15' cols='60' name='<%=QVMaterialListControl.P_MATERIAL_TEXT%>' class='layer-form' > <%=sText%></TEXTAREA>
  	</TD>
</TR>
</TABLE>
</DIV>

<SCRIPT>
	<%if (bDisplay) {%>
		show_layer('<%=sLayer%>');
	<%} else {%>
		hide_layer('<%=sLayer%>');
	<%}%>
</SCRIPT>
