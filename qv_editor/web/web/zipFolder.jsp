<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" /><%
if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}

String sError = request.getParameter("error");
boolean bIsZipError = (sError!=null && sError.startsWith("zip."));
boolean bIsUnzipError = (sError!=null && sError.startsWith("unzip."));
java.util.Vector vValues = new java.util.Vector();

String sPath = request.getParameter("path");

if (sError!=null){%>
<DIV id='zipFolder' style="position:absolute; top:250; left:350; width:300; z-index:1000; padding:5px; 2px solid;"  class='error-box'>
<%if (sError.equalsIgnoreCase("zip.error.alertSize")){
	String sFolder = request.getParameter("folder");
	String sFilecount = request.getParameter("filecount");
	String sTotalsize = request.getParameter("totalsize");
	vValues.addElement(sFolder);
	vValues.addElement(sFilecount);
	vValues.addElement(sTotalsize);
%>
<TABLE border='0' cellpadding='3' cellspacing='0' width="100%">
<TR>
	<TD valign="top">
		<IMG src="imatges/alert.gif"/>
	</TD>
  	<TD class="error-text">
  		<B><%=qvb.getMsg("zip.alert")%></B>
  		<BR/><%=qvb.getMsg(sError, vValues)%>
	</TD
</TR>
<TR>
  	<TD colspan="2" class="error-text" align="center">
	  	<BR/><A href="../zip?path=<%=sPath%>&folder=<%=sFolder%>&url=web/index.jsp&ok=true" onclick="set_layer_visibility('zipFolder', 'hidden');" class="error-text"><U><B><%=qvb.getMsg("zip.download")%></B></U></A>
	  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	  	<A href="#" onclick="set_layer_visibility('zipFolder', 'hidden');" class="error-text"><U><B><%=qvb.getMsg("close")%></B></U></A>
	</TD
</TR>
</TABLE>
<%} else if (bIsZipError){
	String sFolder = request.getParameter("folder");
	String sFilecount = request.getParameter("filecount");
	String sTotalsize = request.getParameter("totalsize");
	vValues.addElement(sFolder);
	vValues.addElement(sFilecount);
	vValues.addElement(sTotalsize);
%>
<TABLE border='0' cellpadding='3' cellspacing='0'>
<TR>
	<TD valign="top">
		<IMG src="imatges/error.gif" width="44" height="44"/>
	</TD>
  	<TD class="error-text">
  		<B><%=qvb.getMsg("zip.error")%></B>
  		<BR/><%=qvb.getMsg(sError, vValues)%>
	</TD>
</TR>
<TR>
  	<TD colspan="2" class="error-text" align="center">
	  	<A href="#" onclick="set_layer_visibility('zipFolder', 'hidden');" class="error-text"><U><B><%=qvb.getMsg("close")%></B></U></A>
	</TD
</TR>
</TABLE>
<%} else if (bIsUnzipError){
	String sZip = request.getParameter("zipFile");
	vValues.addElement(sZip);
%>
<TABLE border='0' cellpadding='3' cellspacing='0'>
<TR>
	<TD valign="top">
		<IMG src="imatges/error.gif" width="44" height="44"/>
	</TD>
  	<TD class="error-text">
  		<B><%=qvb.getMsg("unzip.error")%></B>
  		<BR/><%=qvb.getMsg(sError, vValues)%>
	</TD
</TR>
<TR>
  	<TD colspan="2" class="error-text" align="center">
	  	<A href="#" onclick="set_layer_visibility('zipFolder', 'hidden');" class="error-text"><U><B><%=qvb.getMsg("close")%></B></U></A>
	</TD
</TR>
</TABLE>
<%}%>
</DIV>
<SCRIPT>
	center_layer('zipFolder',300,100);
</SCRIPT>
<%}%>
