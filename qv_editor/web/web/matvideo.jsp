<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" import="edu.xtec.qv.editor.util.QVMaterialListControl"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
String sLayer = "matvideoLayer";

boolean bDisplay = qvb.getBooleanParameter("display", false);
String sURI = qvb.getParameter(QVMaterialListControl.P_MATERIAL_VIDEO_URI);
String[] sVideos = qvb.getAssessmentResources(qvb.VIDEO_RESOURCE);
String sPath = qvb.getQuadernResourcesURL()+"/";

if ((sURI==null || sURI.length()<=0) && sVideos!=null && sVideos.length>0){
	sURI = sVideos[0];
}
%>

<DIV id='<%=sLayer%>' style="position:absolute; top:90; left:20; width:500; z-index:1000;display:none;visibility:hidden;">
<TABLE border='0' cellpadding='5' cellspacing='5' width='100%' >
<TR>
	<TD class="layer-text" valign="top" width="60"><%=qvb.getMsg("material.uri")%></TD>
	<TD valign="top" class="layer-form" >
<%
if (sVideos!=null && sVideos.length>0){%>
					<SELECT name='<%=QVMaterialListControl.P_MATERIAL_VIDEO_URI%>' class='layer-form'>
<%	for(int i=0;i<sVideos.length;i++){%>
						<OPTION value="<%=sVideos[i]%>" <%=sVideos[i].equals(sURI)?"selected":""%> ><%=sVideos[i]%></OPTION>
<%}%>
					</SELECT>
<%} else {%>
					<%=qvb.getMsg("material.msg.noVideos")%>
<%}%>
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
