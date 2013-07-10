<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" import="edu.xtec.qv.editor.util.QVMaterialListControl"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
String sLayer = "matjclicLayer";

boolean bDisplay = qvb.getBooleanParameter("display", false);
String sURI = qvb.getParameter(QVMaterialListControl.P_MATERIAL_JCLIC_URI);
String sWidth = qvb.getParameter(QVMaterialListControl.P_MATERIAL_JCLIC_WIDTH, "600");
String sHeight = qvb.getParameter(QVMaterialListControl.P_MATERIAL_JCLIC_HEIGHT, "400");

String[] sJClic = qvb.getAssessmentResources(qvb.JCLIC_RESOURCE);
String sPath = qvb.getQuadernResourcesURL()+"/";
%>

<DIV id='<%=sLayer%>' style="position:absolute; top:90; left:20; width:500; z-index:1000;display:none;visibility:hidden;">
<TABLE border='0' cellpadding='5' cellspacing='5' width='100%' >
<TR>
	<TD class="layer-text" valign="top" width="60"><%=qvb.getMsg("material.uri")%></TD>
	<TD valign="top" class="layer-form" >
<%if (sJClic!=null && sJClic.length>0){%>
					<SELECT name='p_material_jclic_uri_select' class='layer-form' onchange="this.form.<%=QVMaterialListControl.P_MATERIAL_JCLIC_URI%>.value=this.value">
						<OPTION value=""></OPTION>
<% 	for(int i=0;i<sJClic.length;i++){%>
						<OPTION value="<%=sJClic[i]%>" <%=sJClic[i].equals(sURI)?"selected":""%> ><%=sJClic[i]%></OPTION>
<%	}%>
					</SELECT>
<%} else {%>
					<%=qvb.getMsg("material.msg.noJClic")%>
<%}%>
  	</TD>
</TR>
<TR>
	<TD class="layer-text" valign="top" width="60"><%=qvb.getMsg("material.jclic.uri")%></TD>
	<TD valign="top" class="layer-form" >
		<INPUT name='<%=QVMaterialListControl.P_MATERIAL_JCLIC_URI%>' class='layer-form' size="50" value="<%=sURI%>"/>
  	</TD>
</TR>
<TR>
	<TD class="layer-text" ><%=qvb.getMsg("material.width")%></TD>
	<TD class="layer-form" >
		<INPUT type="text" class='layer-form' name="<%=QVMaterialListControl.P_MATERIAL_JCLIC_WIDTH%>" size="5" value="<%=sWidth%>"/> <%=qvb.getMsg("pixels")%>
	</TD>
</TR>
<TR>
	<TD class="layer-text" ><%=qvb.getMsg("material.height")%></TD>
	<TD class="layer-form" >
		<INPUT type="text" class='layer-form' name="<%=QVMaterialListControl.P_MATERIAL_JCLIC_HEIGHT%>" size="5" value="<%=sHeight%>"/> <%=qvb.getMsg("pixels")%>
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
