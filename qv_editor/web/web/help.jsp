<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
String sHelpPagesPath = "help/"+qvb.getLanguage()+"/";
String sHelpPage = qvb.getParameter("p_help_page");
if (sHelpPage!=null && sHelpPage.trim().length()>0 && !sHelpPage.equalsIgnoreCase("null")){
	sHelpPage = sHelpPagesPath+sHelpPage;
}else{
	sHelpPage = null;
}
%>
<%if (qvb.showHelp() && sHelpPage!=null){%>
	<A href="#" onclick="help_window=window.open('<%=sHelpPage%>','<%=qvb.getMsg("help")%>','directories=0,width=400,height=500,location=0,menubar=0,resizable=0,screenX=50,screenY=0,scrollbars=1,status=0,toolbar=0'); help_window.focus(); return false;" class="text-link" title="<%=qvb.getMsg("help.tip")%>" ><IMG src="imatges/help_off.gif" width="10" height="10" onMouseOver='this.src="imatges/help_on.gif";this.width="15";this.height="15"' onMouseOut='this.src="imatges/help_off.gif";this.width="10";this.height="10"' border='0'/></A></TD>
<%}%>
