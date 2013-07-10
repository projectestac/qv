<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" import="edu.xtec.qv.editor.util.QVMaterialListControl"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
String sLayer = "matlatexLayer";

String sForm = qvb.getParameter("p_form");

boolean bDisplay = qvb.getBooleanParameter("display", false);
String sEquation = qvb.getParameter(QVMaterialListControl.P_MATERIAL_LATEX_EQUATION);
String sWidth = qvb.getParameter(QVMaterialListControl.P_MATERIAL_LATEX_WIDTH, "400");
String sHeight = qvb.getParameter(QVMaterialListControl.P_MATERIAL_LATEX_HEIGHT, "100");
%>

<SCRIPT src="../../qv/skins/common/scripts/javaplugin.js"></SCRIPT>
<DIV id='<%=sLayer%>' style="position:absolute; top:90; left:20; width:500; z-index:1000;display:none;visibility:hidden;">
<TABLE border='0' cellpadding='5' cellspacing='5' width='100%' >
<TR>
	<TD class="layer-text" valign="top" width="60"><%=qvb.getMsg("material.latex.equation")%></TD>
	<TD valign="top" class="layer-form" >
		<TEXTAREA name='<%=QVMaterialListControl.P_MATERIAL_LATEX_EQUATION%>' class='layer-form' cols="50" rows="5"><%=sEquation%></TEXTAREA>
  	</TD>
</TR>
<TR>
	<TD class="layer-text" ><%=qvb.getMsg("material.width")%></TD>
	<TD class="layer-form" >
		<INPUT type="text" class='layer-form' name="<%=QVMaterialListControl.P_MATERIAL_LATEX_WIDTH%>" size="5" value="<%=sWidth%>"/> 
		<span class="layer-text">&nbsp;&nbsp;&nbsp;<%=qvb.getMsg("material.height")%></span>
		<INPUT type="text" class='layer-form' name="<%=QVMaterialListControl.P_MATERIAL_LATEX_HEIGHT%>" size="5" value="<%=sHeight%>"/>		
	</TD>
</TR>
<TR>
	<TD colspan="2" class="layer-text" style="vertical-align: top"><A href="javascript:updateHotEqnApplet('matlatex_applet', <%=sForm%>.<%=QVMaterialListControl.P_MATERIAL_LATEX_EQUATION%>.value);" class="layer-link"><%=qvb.getMsg("material.latex.update")%></A></TD>
</TR>
<TR>
	<TD class="layer-form" colspan="2">
		<script type="text/javascript">
		<!--
			//var params="equation=<%=sEquation.replaceAll("'",",")%>$$";
			var params="equation=$$";
			//writeHotEqnApplet(params, '465', '100', 'matlatex_applet');
		-->
		</script>	
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
