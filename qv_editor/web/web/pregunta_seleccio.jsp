<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" /><%
if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
	edu.xtec.qv.editor.beans.QVPreguntaSeleccioBean qvsb=(edu.xtec.qv.editor.beans.QVPreguntaSeleccioBean)qvb.getSpecificBean();
String sForm = "document.seleccioForm";
String sCardinalitat = qvsb.getCardinalitat();
String sIdentResp = qvsb.getIdentResponse();
if (!qvb.isHeaderLoaded()){
%>
<jsp:include page="header.jsp" flush="true" />
<%}%>

<FORM name="seleccioForm" method="POST" action="edit.jsp">
<INPUT type="hidden" name="page" value="pregunta_seleccio"/>
<INPUT type="hidden" name="action"/>
<INPUT type="hidden" name="edition_mode" value="<%=qvb.getEditionMode()%>"/>
<INPUT type="hidden" name="num_respostes" value="<%=qvsb.getNumResponseLabels(sIdentResp)%>"/>
<INPUT type="hidden" name="index_resposta"/>
<INPUT type="hidden" name="ident_pregunta" value="<%=qvb.getPregunta()!=null?qvb.getPregunta().getIdent():""%>"/>
<INPUT type="hidden" name="ident_resposta" value="<%=qvb.getParameter("ident_resposta", "")%>"/>
<INPUT type="hidden" name="hide_layers" />
<SCRIPT language="javascript">
	setCurrentForm("seleccioForm");
</SCRIPT>

<%if (qvb.getEditionMode()!=null && qvb.getEditionMode().equalsIgnoreCase(qvb.TEXT_EDITION_MODE)){%>
<TABLE width="100%" style="height:100%" cellpadding="5" cellspacing="5" border="0" class="edit-box">
<TR>
	<TD align="right" class="edit-title"><%=qvb.getMsg("item.type.selection.name")%>&nbsp;&nbsp;&nbsp;</TD>
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
	<TD class="edit-box">
		<jsp:include page="enunciat.jsp" flush="true">
			<jsp:param name="form" value="<%=sForm%>" />
			<jsp:param name="assessment_title" value='<%=qvb.filter(qvb.getMsg("item.type.selection.name"))%>' />
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
		<jsp:include page="resposta.jsp" flush="true">
			<jsp:param name="ident_resp_lid" value="<%=sIdentResp%>" />
			<jsp:param name="cardinalitat" value="<%=sCardinalitat%>" />
			<jsp:param name="show_as_list" value="<%=String.valueOf(qvsb.showAsList(sIdentResp))%>" />
			<jsp:param name="show_cardinality" value="true" />
			<jsp:param name="show_as_fib" value="false" />
			<jsp:param name="show_images" value="<%=String.valueOf(qvsb.showImages(sIdentResp))%>" />
			<jsp:param name="show_ident_resp" value="false" />
			<jsp:param name="is_vertical" value="<%=String.valueOf(qvsb.isVertical(sIdentResp))%>" />
			<jsp:param name="num_respostes_linia" value="1" />
			<jsp:param name="form" value="<%=sForm%>" />
		</jsp:include>
	</TD>
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
	<TD height='10'></TD>
</TR>

<TR>
	<TD>
		<jsp:include page="feedback.jsp" flush="true">
			<jsp:param name="feedback_ok" value='<%=qvsb.getOKFeedback()%>' /> 
			<jsp:param name="puntuation_ok" value='<%=qvsb.getOKPuntuation()%>'/>
			<jsp:param name="feedback_ko" value='<%=qvsb.getKOFeedback()%>' />
			<jsp:param name="puntuation_ko" value='<%=qvsb.getKOPuntuation()%>' />
			<jsp:param name="hint" value='<%=qvsb.getHint()%>' />
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
