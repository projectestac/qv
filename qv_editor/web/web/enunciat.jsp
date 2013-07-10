<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<%@taglib uri="/WEB-INF/CWHTM.tld" prefix="CWHTM" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
	String sForm = qvb.getParameter("form");
	String sAssessmentTitle = qvb.getParameter("assessment_title");
	String sTitle = qvb.getParameter("title_pregunta");
	String sEnunciat = qvb.getParameter("enunciat_pregunta"); 
	String sScoreModel = qvb.getParameter("scoremodel");
	String sFullOrder = qvb.getParameter("ordre_full");
	String sTitolMaterials = qvb.getParameter("titol_materials", "Materials");
	String sNameNumItems = qvb.getParameter("name_num_items", "num_materials");
	int iNumItems = qvb.getIntParameter(sNameNumItems, 0);
	boolean bShowURL = qvb.getBooleanParameter("p_show_url", false);
%>

<!-- INICI ENUNCIAT -->
<INPUT type="hidden" name="num_materials" />
<TABLE width="100%" cellpadding="5" cellspacing="0" border="0">
<%if (sAssessmentTitle!=null){%>
<TR>	
	<TD colspan="2" align="right" class="edit-title"><%=sAssessmentTitle%>&nbsp;&nbsp;&nbsp;</TD>
</TR>
<%}%>
<%if (bShowURL){%>
<TR>
	<TD class='edit-text' width='10%'><%=qvb.getMsg("url")%></TD>	
	<!-- TD class="edit-form"><%=qvb.getQuadernRemoteFile()%></TD-->
	<TD class="edit-form"><A href="<%=qvb.getPreviewURL()%>" target="_blank" class="edit-link"><%=qvb.getQuadernPublicURL()%></A>
	<!--A href="<%=qvb.getQuadernPublicURL()%>" target="_blank" class="edit-link"><%=qvb.getQuadernPublicURL()%></A-->
	</TD>
</TR>
<%}%>
<TR>
	<TD class='edit-text' width='10%'><%=qvb.getMsg("title")%></TD>	
	<TD><INPUT type="text" size="60" name="title" value="<%=sTitle%>" class='edit-form'/></TD>	
</TR>
<%if (sEnunciat!=null){%>
<TR>
	<TD class='edit-text' valign="top"><%=qvb.getMsg("statement")%></TD>
	<TD>
		<TEXTAREA rows="3" cols="60" id="enunciat_pregunta" name="enunciat_pregunta" class='edit-form'><%=sEnunciat%></TEXTAREA>
		<!-- >script type="text/javascript">
			xinha_editors = addEditor(xinha_editors, 'enunciat_pregunta');
		</script-->
	</TD>
</TR>
<%}%>
<%if (sScoreModel!=null){%>
<TR>
	<TD class='edit-text' width='10%' title="<%=qvb.getMsg("score_model.tip")%>"><%=qvb.getMsg("score_model")%></TD>	
	<TD>
		<SELECT class='edit-form' name="scoremodel">
			<OPTION value="SumOfScores" <%=sScoreModel.equals("SumOfScores")?"selected":""%> ><%=qvb.getMsg("score.sum_of_scores")%></OPTION>
			<OPTION value="NumberCorrect" <%=sScoreModel.equals("NumberCorrect")?"selected":""%> ><%=qvb.getMsg("score.number_correct")%></OPTION>
		</SELECT>
</TR>
<%}%>
<TR>
	<TD colspan='2' height='5'/>
</TR>
<%if (qvb.getSpecificBean() instanceof edu.xtec.qv.editor.beans.QVQuadernBean){%>
<TR>
	<TD><INPUT type="hidden" name="show_lom"></TD>
	<TD><A href="#" onclick="if (get_layer('lomLayer')!=null){show_lom_layer();}else{<%=sForm%>.show_lom.value='true';enviar('open_lom', <%=sForm%>);}" title="<%=qvb.getMsg("lom.action.label.tip")%>" class="edit-link"><%=qvb.getMsg("lom.action.label")%></A></TD>
</TR>
<TR>
	<TD colspan="2">
		<jsp:include page="listControl.jsp" flush="true">
			<jsp:param name="p_form" value="<%=sForm%>" />
			<jsp:param name="p_list_name" value="metadata_list"/>
			<jsp:param name="p_list_type" value="metadata"/>
			<jsp:param name="p_list_title" value='<%=qvb.getMsg("metadata.title")%>' />
			<jsp:param name="p_list_tip" value='<%=qvb.getMsg("metadata.tip")%>' />
			<jsp:param name="p_help_page" value="metadata.html" />
		</jsp:include>
	</TD>
</TR>
<%}%>
<%if (qvb.getSpecificBean() instanceof edu.xtec.qv.editor.beans.QVFullBean ||
 	  qvb.getSpecificBean() instanceof edu.xtec.qv.editor.beans.QVPreguntaBean){%>
<TR>
	<TD colspan="2">
		<jsp:include page="listControl.jsp" flush="true">
			<jsp:param name="p_form" value="<%=sForm%>" />
			<jsp:param name="p_list_name" value="material_list"/>
			<jsp:param name="p_list_type" value="material"/>
			<jsp:param name="p_list_title" value='<%=qvb.getMsg("material.title")%>' />
			<jsp:param name="p_list_tip" value='<%=qvb.getMsg("material.tip")%>' />
		</jsp:include>
	</TD>
</TR>
<%}%>

<% //Albert
if (qvb.getSpecificBean() instanceof edu.xtec.qv.editor.beans.QVFullBean){%>
<TR>
	<TD class='edit-text' width='10%' title="<%=qvb.getMsg("section.order.tip")%>"><%=qvb.getMsg("section.order")%></TD>	
	<TD>
		<SELECT class='edit-form' name="ordre_full">
			<OPTION value="no_random" <%= ((edu.xtec.qv.editor.beans.QVFullBean)qvb.getSpecificBean()).getFullOrder().equals("no_random")?"selected":""%> ><%=qvb.getMsg("section.order.norandom")%></OPTION>
			<OPTION value="random" <%=((edu.xtec.qv.editor.beans.QVFullBean)qvb.getSpecificBean()).getFullOrder().equals("random")?"selected":""%> ><%=qvb.getMsg("section.order.random")%></OPTION>
		</SELECT>
	</TD>
</TR>
<%}%>

<%/*if (qvb.getSpecificBean() instanceof edu.xtec.qv.editor.beans.QVQuadernBean){
	sInteraction = "Mostrar quadres d'intervenció al quadern";
	sInteractionTip = "Mostra els camps de text a tot el quadern per tal que tant l'alumne com el professor puguin escriure comentaris i resoldre dubtes";
}else if (qvb.getSpecificBean() instanceof edu.xtec.qv.editor.beans.QVFullBean){
	sInteraction = "Mostrar quadres d'intervenció al full";
	sInteractionTip = "Mostra els camps de text a tot el full per tal que tant l'alumne com el professor puguin escriure comentaris i resoldre dubtes";
}*/
%>
<%if (qvb.getSpecificBean() instanceof edu.xtec.qv.editor.beans.IQVInteractionBean){
	edu.xtec.qv.editor.beans.IQVInteractionBean qvsb = (edu.xtec.qv.editor.beans.IQVInteractionBean)qvb.getSpecificBean();
%>
<TR>
	<TD colspan="2" class="edit-text">
		<INPUT type="hidden" name="<%=qvsb.P_INTERACTIONSWITCH%>"/>
		<INPUT type="checkbox" name="checkinteraction" class='edit-form' title="<%=qvb.getMsg(qvsb.getInteractionTip())%>" <%=qvsb.isInteractionswitch()?"checked":""%> onclick="if(this.checked){this.form.<%=qvsb.P_INTERACTIONSWITCH%>.value='Yes';}else{this.form.<%=qvsb.P_INTERACTIONSWITCH%>.value='No';}"/><%=qvb.getMsg(qvsb.getInteractionText())%>
	</TD>
</TR>
<%}%>
</TABLE>
<%if (qvb.getSpecificBean() instanceof edu.xtec.qv.editor.beans.QVQuadernBean){%>
	<jsp:include page="lom.jsp" flush="true" />
<%}%>
<!-- FI ENUNCIAT -->
