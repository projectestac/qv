head	1.2;
access;
symbols
	qv_editor_20040719:1.1.1.1
	inicial:1.1.1.1
	xtec:1.1.1;
locks; strict;
comment	@# @;


1.2
date	2004.08.10.06.12.21;	author sarjona;	state dead;
branches;
next	1.1;

1.1
date	2004.05.27.07.22.58;	author sarjona;	state Exp;
branches
	1.1.1.1;
next	;

1.1.1.1
date	2004.05.27.07.22.58;	author sarjona;	state Exp;
branches;
next	;


desc
@@


1.2
log
@Eliminades jsp i classes que no es fan servir
@
text
@<%@@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" /><%
if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
	edu.xtec.qv.editor.beans.QVMaterialBean qvsb=null;
	if (qvb.getSpecificBean() instanceof edu.xtec.qv.editor.beans.QVMaterialBean){
		qvsb=(edu.xtec.qv.editor.beans.QVMaterialBean)qvb.getSpecificBean();
	}
	String sForm = qvb.getParameter("form");
	String sTitolMaterials = qvb.getParameter("titol_materials", "Materials");
	int iNumMaterials = qvsb!=null?qvsb.getNumMaterials():0;
%>
<%if(qvsb!=null){%> 
<!-- INICI materials -->
<INPUT type="hidden" name="current_material" value="<%=qvsb.getMaterialIndex()%>" />
<INPUT type="hidden" name="num_materials" />
<TABLE width="100%" cellpadding="0" cellspacing="0" border="0" style='height:100%;'>
<%if (iNumMaterials>0){%>
<TR>
	<TD valign="top" height="10%">
		<TABLE width='100%' cellpadding='0' cellspacing='0' border='0'>
		<TR>
			<TD class='textNomDades'><B><%=sTitolMaterials%></B></TD>
		</TR>
		<TR>
			<TD height='5'></TD>
		</TR>
		</TABLE>
	</TD>
</TR>
<%}%>

<%
for (int i=0;i<iNumMaterials;i++){	
%>	
<TR>
	<TD valign="top">
	<jsp:include page="material.jsp" flush="true">
		<jsp:param name="form" value="<%=sForm%>" />
		<jsp:param name="current_material" value="<%=String.valueOf(qvsb.getMaterialIndex())%>" />
		<jsp:param name="num_materials" value="<%=String.valueOf(iNumMaterials)%>" />
		<jsp:param name="material_index" value="<%=String.valueOf(i)%>" />
	</jsp:include>
	</TD>
</TR>
<TR>
	<TD height='5'></TD>
</TR>
<%}%>	
<TR>
	<TD valign="top" height="100%">
		<jsp:include page="control.jsp" flush="true">
			<jsp:param name="form" value="<%=sForm%>" />
			<jsp:param name="name_add_action_param" value="add_material" />
			<jsp:param name="name_del_action_param" value="del_material" />
			<jsp:param name="name_up_action_param" value="up_material" />
			<jsp:param name="name_down_action_param" value="down_material" />
			<jsp:param name="name_num_items" value="num_materials" />
			<jsp:param name="value_num_items" value="<%=String.valueOf(iNumMaterials)%>" />
		</jsp:include>
	</TD>
</TR>
</TABLE>
<%}%>
<!-- FI materials -->
@


1.1
log
@Initial revision
@
text
@@


1.1.1.1
log
@Editor de QV.
S'ha separat del projecte qv
@
text
@@
