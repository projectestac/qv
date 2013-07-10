<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" /><%
if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.QVPreguntaBean qvsb=(edu.xtec.qv.editor.beans.QVPreguntaBean)qvb.getSpecificBean();

String sForm = "preguntaForm";

if (!qvb.isHeaderLoaded()){
%>
<jsp:include page="header.jsp" flush="true" />
<%}%>

<FORM name="<%=sForm%>" method="POST" action="edit.jsp">
<INPUT type="hidden" name="page" value="pregunta"/>
<INPUT type="hidden" name="action"/>
<INPUT type="hidden" name="hide_layers" />
<INPUT type="hidden" name="p_ordered_response_action" />
<INPUT type="hidden" name="ident_resp_lid" value="<%=edu.xtec.qv.editor.util.QTIUtil.getRandomIdent()%>"/>
<TABLE class='edit-box' width="100%" cellpadding="5" cellspacing="5" border="0" style="height:100%">
<!-- INICI Seleccionar tipus pregunta -->
<TR>
	<TD colspan="2">
		<TABLE width="100%" cellpadding="5" cellspacing="0" border="0" style="height:100%">
		<TR>
			<TD valign="top" class='edit-text'>
				<B>1.</B>
			</TD>
			<TD valign="top" class='edit-text' width="100%" ><%=qvb.getMsg("item.add.step1")%></TD>
		</TR>
		<TR>
			<TD/>
			<TD >
				<SELECT class='edit-form' width="100" name="tipus_pregunta" onchange="show_response_options_layer(this.value)">
					<OPTION value="selection" selected><%=qvb.getMsg("item.type.selection")%></OPTION>
					<OPTION value="Ordered"><%=qvb.getMsg("item.type.ordered")%></OPTION>
					<OPTION value="cloze" ><%=qvb.getMsg("item.type.cloze")%></OPTION>
					<OPTION value="hotspot"><%=qvb.getMsg("item.type.hotspot")%></OPTION>
					<OPTION value="drag_drop"><%=qvb.getMsg("item.type.drag_drop")%></OPTION>
					<OPTION value="draw"><%=qvb.getMsg("item.type.draw")%></OPTION>
				</SELECT>
			</TD>
		</TR>
		</TABLE>
	</TD>
</TR>
<!-- FI Seleccionar tipus pregunta -->
<TR>
	<TD colspan="2">
		<jsp:include page="item_response_options.jsp">
			<jsp:param name="form" value="<%=sForm%>"/>
		</jsp:include>
	</TD>
</TR>
<TR>
	<TD colspan="2">
		<TABLE width="100%" cellpadding="5" cellspacing="0" border="0" style="height:100%">
		<TR>
			<TD valign="top" class='edit-text'>
				<B>3.</B>
			</TD>
			<TD valign="top" class='edit-text' width="100%" ><%=qvb.getMsg("item.add.step3")%></TD>
		</TR>
		<TR>
			<TD/>
			<TD >
				<SELECT class='edit-form' width="100" name="ordre_pregunta">
					<OPTION value="no_random" selected><%=qvb.getMsg("item.order.norandom")%></OPTION>
					<OPTION value="random"><%=qvb.getMsg("item.order.random")%></OPTION>
				</SELECT>
			</TD>
		</TR>
		</TABLE>
	</TD>
</TR>
<TR valign="top">
	<TD >&nbsp;</TD>
	<TD align="left" height="100%" width="97%">
		<A class='link' href='javascript:if (this.document.<%=sForm%>.tipus_pregunta.value=="Ordered"){this.document.<%=sForm%>.hide_layers.value="false";this.document.<%=sForm%>.p_ordered_response_action.value="<%=edu.xtec.qv.editor.beans.IQVListControlBean.A_SET_LIST_OBJECT%>";}enviar("add_pregunta",this.document.preguntaForm)' title="<%=qvb.getMsg("item.action.add.tip")%>"><%=qvb.getMsg("item.action.add")%></A>
	</TD>
</TR>
</TABLE>
</FORM>
<SCRIPT>
	show_response_options_layer("selection");
</SCRIPT>
<%if (!qvb.isHeaderLoaded()){
	qvb.loadHeader();
%>
<jsp:include page="footer.jsp" flush="true" />
<%}%>
