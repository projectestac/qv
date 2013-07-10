<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
	edu.xtec.qv.editor.beans.QVPreguntaClozeBean qvsb=(edu.xtec.qv.editor.beans.QVPreguntaClozeBean)qvb.getSpecificBean();

String sForm = "document.clozeForm";
String sClozeText = qvsb.getClozeText();	
String sResponseOptionType = qvb.getParameter("response_option_type");

if (sResponseOptionType==null){
	sResponseOptionType = (String)session.getAttribute("response_option_type");
}else{
	session.setAttribute("response_option_type", sResponseOptionType);
}

if (!qvb.isHeaderLoaded()){
%>
<jsp:include page="header.jsp" flush="true" />
<%}%>

<FORM name="clozeForm" method="POST" action="edit.jsp">
<INPUT type="hidden" name="page" value="pregunta_cloze"/>
<INPUT type="hidden" name="action"/>
<INPUT type="hidden" name="edition_mode" value="<%=qvb.getEditionMode()%>"/>
<INPUT type="hidden" name="ident_pregunta" value="<%=qvsb.getIdentPregunta()%>"/>
<INPUT type="hidden" name="ident_pregunta" value="<%=qvb.getPregunta()!=null?qvb.getPregunta().getIdent():""%>"/>
<INPUT type="hidden" name="ident_resposta" value="<%=qvb.getParameter("ident_resposta", "")%>"/>
<INPUT type="hidden" name="hide_layers" value="<%=qvb.getParameter("hide_layers")!=null?qvb.getParameter("hide_layers"):""%>"/>
<SCRIPT language="javascript">
	setCurrentForm("clozeForm");
</SCRIPT>

<%if (qvb.getEditionMode()!=null && qvb.getEditionMode().equalsIgnoreCase(qvb.TEXT_EDITION_MODE)){%>
<TABLE width="100%" style="height:100%" cellpadding="5" cellspacing="5" border="0" class="edit-box">
<TR>
	<TD align="right" class="edit-title"><%=qvb.getMsg("item.type.cloze.name")%>&nbsp;&nbsp;&nbsp;</TD>
</TR>
<TR>
	<TD class="edit-text" height="100%">
	<jsp:include page="editorXML.jsp" flush="true">
		<jsp:param name="form" value="<%=sForm%>" />
	</jsp:include>
	</TD>
</TR>
</TABLE>
<%}else{%>
<TABLE width="100%" cellpadding="0" cellspacing="0" border="0"> 
<TR>
	<TD class="edit-box" valign="top">
		<jsp:include page="enunciat.jsp" flush="true">
			<jsp:param name="form" value="<%=sForm%>" />
			<jsp:param name="assessment_title" value='<%=qvb.filter(qvb.getMsg("item.type.cloze.name"))%>' />
			<jsp:param name="title_pregunta" value='<%=qvsb.getTitle()%>' /> 
			<jsp:param name="enunciat_pregunta" value='<%=qvsb.getEnunciat()%>' />
		</jsp:include>	
	</TD>
</TR>
<TR>
	<TD height='10'></TD>
</TR>
<TR>
	<TD>
		<!-- INICI Dades de la pregunta -->
		<TABLE class='edit-box' width="100%" cellpadding="5" cellspacing="0" border="0">
		<TR>
			<TD valign='top' class='edit-text' width='5%'><%=qvb.getMsg("item.cloze.paragraph")%></TD>
			<TD class="edit-link">
			<TEXTAREA rows="10" cols="65" id="paragraf_pregunta" name="paragraf_pregunta" class='edit-form' onBlur="//enviar('update_cloze',<%=sForm%>);"><%=sClozeText%></TEXTAREA>
			<script type="text/javascript">
				xinha_editors = addEditor(xinha_editors, 'paragraf_pregunta');
			</script>
			<!-- TEXTAREA rows="5" cols="60" name="paragraf_pregunta" class='edit-form' onBlur="//enviar('update_cloze',<%=sForm%>);"><%=sClozeText%></TEXTAREA-->
			
			<jsp:include page="help.jsp" flush="true">
					<jsp:param name="p_help_page" value="cloze_paragraph.html" />
			</jsp:include>
		</TR>
		<TR>
			<TD/>
			<TD>
				<A href="javascript:enviar('update_cloze',<%=sForm%>);" class="link"><%=qvb.getMsg("item.cloze.update_responses")%></A>
			</TD>
		</TR>
		<TR>
			<TD colspan='2'>
				<jsp:include page="listControl.jsp" flush="true">
					<jsp:param name="p_form" value="<%=sForm%>" />
					<jsp:param name="p_list_name" value="cloze_response_list"/>
					<jsp:param name="p_list_type" value="cloze_response"/>
					<jsp:param name="p_list_title" value='<%=qvb.getMsg("item.cloze_response.title")%>' />
					<jsp:param name="p_list_tip" value='<%=qvb.getMsg("item.cloze_response.tip")%>' />
					<jsp:param name="p_default_ident_list" value="<%=edu.xtec.qv.editor.util.QTIUtil.getRandomIdent()%>" />
					<jsp:param name="hide_add" value="true" />
					<jsp:param name="hide_del" value="true" />
					<jsp:param name="hide_up" value="true" />
					<jsp:param name="hide_down" value="true" />
					<jsp:param name="p_help_page" value="cloze_responses.html" />
				</jsp:include>
			</TD>
		</TR>
		</TABLE>
		<!-- FI Dades de la pregunta -->
	</TD>
</TR>
<TR>
	<TD height='10'></TD>
</TR>

<!-- Albert -->
<TR>
	<TD height='10'></TD>
</TR>

<TR>
	<TD>
		<jsp:include page="order_pregunta.jsp" flush="true">
			<jsp:param name="ordre_pregunta" value='<%=qvb.getParameter("ordre_pregunta")!=null?qvb.getParameter("ordre_pregunta"):""%>' /> 
		</jsp:include>	
	</TD>
</TR>
<!-- Albert -->

<TR>
	<TD>
		<jsp:include page="feedback.jsp" flush="true">
			<jsp:param name="feedback_ok" value='<%=qvsb.getOKFeedback()%>' /> 
			<jsp:param name="puntuation_ok" value='<%=qvsb.getOKPuntuation()%>'/>
			<jsp:param name="feedback_ko" value='<%=qvsb.getKOFeedback()%>' />
			<jsp:param name="puntuation_ko" value='<%=qvsb.getKOPuntuation()%>' />
		</jsp:include>
	</TD>
</TR>
</TABLE>
<%}%>
</FORM>

<%if (!qvb.isHeaderLoaded()){
	qvb.loadHeader();
%>
<jsp:include page="footer.jsp" flush="true" />
<%}%>
