<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" import="edu.xtec.qv.editor.util.QVMaterialListControl"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}

String sLayer = "mathtmlLayer";
String sForm = qvb.getParameter("form");

boolean bDisplay = qvb.getBooleanParameter("display", false);
String sText = qvb.getParameter(QVMaterialListControl.P_MATERIAL_HTML);
%>

<DIV id='<%=sLayer%>' style="position:absolute; top:90; left:25; z-index:1000;display:none;visibility:hidden;" >
<TABLE border='0' cellpadding='0' cellspacing='0'>
<TR>
	<TD valign="top">
			<TEXTAREA rows="10" cols="65" id="p_material_html" name="p_material_html" class='edit-form'><%=sText%></TEXTAREA>
			<script type="text/javascript">
				xinha_editors = addEditor(xinha_editors, 'p_material_html');
			</script>
	
<% String sImg="\'./\' : \'/qv_editor/quaderns/"+qvb.getUserId()+"/"+qvb.getIdQuadern()+"/\'"; %>
		<!-- CWHTM:AreaTextoHTML id="p_material_html" name="p_material_html" specialReplacements="<%=sImg%>" lang="<%=qvb.getLanguage()%>" base="../../components/xinha/" theme="qv" cols="35" rows="15" images="../components/xinha/modules/InsertImage/insert_image.jsp"  colorPickerCellSize="8px"><%=sText%></CWHTM:AreaTextoHTML-->
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
	show_layer('<%=sLayer%>');
	
</SCRIPT>
