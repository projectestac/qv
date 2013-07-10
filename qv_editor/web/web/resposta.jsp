<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" /><%
if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.IQVResponseBean qvsb=(edu.xtec.qv.editor.beans.IQVResponseBean)qvb.getSpecificBean();
String sIdResp = qvb.getParameter(qvsb.IDENT_RESPONSE_LID_PARAM);
int iNumRespostes = qvsb.getNumResponseLabels(sIdResp);
int iNumRespostesLinia = qvb.getIntParameter(qvsb.NUM_RESPOSTES_LINIA_PARAM, 1);
String sCardinalitat = qvb.getParameter(qvsb.CARDINALITAT_PARAM);
String sForm = qvb.getParameter("form");
String sResponseMatType = qvb.getParameter("response_material_type");
if (sResponseMatType==null){
	sResponseMatType = qvsb.getResponseMaterialType(sIdResp);
}
boolean bShowCardinality = qvb.getBooleanParameter("show_cardinality", true);
boolean bShowAsList = qvb.getBooleanParameter("show_as_list", true);
boolean bShowAsFIB = qvb.getBooleanParameter("show_as_fib", false);
boolean bShowImages = qvb.getBooleanParameter("show_images", true);
boolean bShowIdentResp = qvb.getBooleanParameter("show_ident_resp", true);
boolean bIsVertical = qvb.getBooleanParameter("is_vertical", false);
boolean bIsMultiple = sCardinalitat!=null && sCardinalitat.equalsIgnoreCase("Multiple");

%>

<!-- INICI dades pel conjunt de respostes amb identificador <%=sIdResp%> -->
<INPUT type="hidden" name="<%=qvsb.IDENT_RESPONSE_LID_PARAM%>" value="<%=sIdResp%>" />
<TABLE class='edit-box' width="100%" cellpadding="5" cellspacing="0" border="0">
<TR>
	<TD>
		<TABLE class='edit-box' width="100%" cellpadding="5" cellspacing="0" border="0">
<%if (bShowCardinality){%>
		<TR>
			<TD width='20%' class='edit-text' title="<%=qvb.getMsg("item.cardinality.tip")%>" ><%=qvb.getMsg("item.cardinality")%></TD>	
			<TD >
				<SELECT name='cardinalitat' class='edit-form' onChange='enviar("set_cardinalitat", <%=sForm%>)'>
					<OPTION value='Single' <%=sCardinalitat.equals("Single")?"selected":""%> ><%=qvb.getMsg("item.cardinality.single")%></OPTION>
					<OPTION value='Multiple' <%=sCardinalitat.equals("Multiple")?"selected":""%>><%=qvb.getMsg("item.cardinality.multiple")%></OPTION>
				</SELECT>
			</TD>	
		</TR>
<%}%>
<%if (bShowAsFIB || bShowAsList){%>
		<TR>
			<TD colspan="2" class='edit-text'>
			<INPUT type='checkbox' name='<%=qvsb.MOSTRAR_LLISTA_PARAM+"_"+sIdResp%>' <%=bShowAsList?"checked":""%> <%=sCardinalitat.equals("Multiple")?"disabled":""%> onClick='enviar("set_mostrar_llista", <%=sForm%>)' /> Mostrar com a llista desplegable 
			</TD>	
		</TR>
<%}%>
<%if (!bShowAsFIB && !bShowAsList){%>
		<TR>
			<TD width='20%' class='edit-text' title="<%=qvb.getMsg("item.orientation.tip")%>" ><%=qvb.getMsg("item.orientation")%></TD>	
			<TD >
				<SELECT name='orientation' class='edit-form'>
					<OPTION value='vertical' <%=bIsVertical?"selected":""%>><%=qvb.getMsg("item.orientation.vertical")%></OPTION>
					<OPTION value='horizontal' <%=!bIsVertical?"selected":""%> ><%=qvb.getMsg("item.orientation.horizontal")%></OPTION>
				</SELECT>
			</TD>	
		</TR>
<%}%>		
<%if (!bShowAsFIB && !bShowAsList){%>
		<TR>
			<TD width='20%' class='edit-text' title="<%=qvb.getMsg("item.materialResponse.tip")%>" ><%=qvb.getMsg("item.materialResponse")%></TD>	
			<TD >
				<SELECT name='response_material_type' class='edit-form' onChange='enviar("set_response_material_type", <%=sForm%>)'>
					<OPTION value='text' <%=sResponseMatType.equals("text")?"selected":""%> ><%=qvb.getMsg("item.materialResponse.text")%></OPTION>
					<OPTION value='image' <%=sResponseMatType.equals("image")?"selected":""%> ><%=qvb.getMsg("item.materialResponse.image")%></OPTION>
					<OPTION value='audio' <%=sResponseMatType.equals("audio")?"selected":""%> ><%=qvb.getMsg("item.materialResponse.audio")%></OPTION>
				</SELECT>
			</TD>	
		</TR>
<%}%>		
		</TABLE>	
	</TD>
</TR>
<TR>
	<TD height='15'></TD>
</TR>
<TR>
	<TD class='edit-text' valign='top'>
		<TABLE width="100%" cellpadding="0" cellspacing="0" border="0">
		<TR>
<%if (bShowIdentResp){%>
			<TD width='70' class='edit-text'><B>[<%=sIdResp%>]</B></TD>
<%}
if (bShowAsFIB){%>		
			<TD class='edit-text'><B><%=qvb.getMsg("item.selection_response.valid_responses")%></B></TD>
<%} else if (bIsMultiple) {%>
			<TD class='edit-text'><%=qvb.getMsg("item.selection_response.options_to_show.multiple")%></TD>
<%} else{%>
			<TD class='edit-text'><%=qvb.getMsg("item.selection_response.options_to_show.single")%></TD>
<%}%>
		</TR>
		</TABLE>
	</TD>
</TR>
<TR>
	<TD>
		<TABLE width="100%" cellpadding="0" cellspacing="0" border="0">
<%
String[] sRecursos = null;
if (sResponseMatType.equals("image")) sRecursos = qvb.getAssessmentResources(qvb.IMAGE_RESOURCE);
else if (sResponseMatType.equals("audio")) sRecursos = qvb.getAssessmentResources(qvb.AUDIO_RESOURCE);

if (!bShowImages || sResponseMatType.equals("text") || (sRecursos!=null && sRecursos.length>0)){
	for (int i=0;i<iNumRespostes;i=i+iNumRespostesLinia){
%>	
		<TR>
<%
		for (int j=i;j<i+iNumRespostesLinia;j++){
			String sIdentRespLabel = qvsb.getIdentResponseLabel(sIdResp, j);
			String sTextRespLabel = qvsb.getTextResponseLabel(sIdResp, sIdentRespLabel, j);
			String sResourceResp = qvsb.getResourceResponseLabel(sIdResp, sIdentRespLabel);
%>
			<TD>
				<TABLE class='edit-background' id="<%="resposta_"+sIdResp+"_"+j%>" width="100%" cellpadding="0" cellspacing="0" border="0" onclick="seleccionar_item('<%="resposta_"+sIdResp+"_"%>', this.id, <%=iNumRespostes%>, <%=sForm+".ident_resposta"%>, '<%=sIdentRespLabel%>');" <%=sIdentRespLabel.equals(qvsb.getCurrentIdentResponseLabel())?"style='background-color:#FDB671'":""%> >
				<TR>
					<TD width='5%'>
<%
if (sResponseMatType.equals("text") || (sRecursos!=null && sRecursos.length>0)){
if (bIsMultiple){%>
						<INPUT type="checkbox" name="<%=qvsb.RESPOSTES_CORRECTES_PARAM+"_"+sIdResp%>" value="<%=sIdentRespLabel%>" <%=qvsb.isCorrectResponseLabel(sIdResp, sIdentRespLabel)?"checked":""%> />
<%} else if (!bShowAsFIB ){%>
						<INPUT type="radio" name="<%=qvsb.RESPOSTES_CORRECTES_PARAM+"_"+sIdResp%>" value="<%=sIdentRespLabel%>"  <%=qvsb.isCorrectResponseLabel(sIdResp, sIdentRespLabel)?"checked":""%> />
<%}
}%>
					</TD>
					<TD align='left'>
						<INPUT type='hidden' name='<%=qvsb.IDENT_RESPOSTES_PARAM+"_"+sIdResp%>' value='<%=sIdentRespLabel%>'/>
						<%if (sResponseMatType.equals("text")){%>
							<INPUT type='text' size='<%=iNumRespostesLinia>1?20:60%>' name="<%=qvsb.TEXT_RESPOSTES_PARAM+"_"+sIdResp%>" value="<%=sTextRespLabel%>" class="edit-form"/>
						<%}%>
					</TD>
					<TD width='10'>&nbsp;</TD>
					<TD align='left'>
	<%
		if (bShowImages && sResponseMatType!=null && !sResponseMatType.equals("text")){
			if (sRecursos!=null && sRecursos.length>0){
	%>		
						<SELECT name="<%=qvsb.RECURS_RESPOSTES_PARAM+"_"+sIdResp%>" class="edit-form">
							<OPTION value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</OPTION>
	<%
				for(int k=0;k<sRecursos.length;k++){
	%>
							<OPTION value="<%=sRecursos[k]%>" <%=sRecursos[k].equalsIgnoreCase(sResourceResp)?"selected":""%>><%=sRecursos[k]%></OPTION>
	<%			}%>
						</SELECT>
	<%		}
	}%>			
					</TD>
				</TR>
				</TABLE>
<%}%>
			</TD>
		</TR>
<%
	}
}
%>				
<%if (bShowImages && sResponseMatType!=null && !sResponseMatType.equals("text") && (sRecursos==null || sRecursos.length==0)){%>
		<TR>
			<TD class="edit-text"><%=qvb.getMsg("item.response.noResources."+sResponseMatType)%></TD>
		</TR>
<%}%>					
		</TABLE>
	</TD>
</TR>
<TR>
	<TD>
		<jsp:include page="control.jsp" flush="true">
			<jsp:param name="form" value="<%=sForm%>" />
			<jsp:param name="name_add_action_param" value="add_num_respostes" />
			<jsp:param name="name_del_action_param" value="del_resposta" />
			<jsp:param name="name_up_action_param" value="up_item" />
			<jsp:param name="name_down_action_param" value="down_item" />
			<jsp:param name="name_num_items" value="num_respostes" />
			<jsp:param name="value_num_items" value="<%=String.valueOf(iNumRespostes)%>" />
			<jsp:param name="name_param" value="ident_resp_lid" />
			<jsp:param name="value_param" value="<%=sIdResp%>" />
		</jsp:include>
	</TD>
</TR>
</TABLE>
<!-- FI dades pel conjunt de respostes amb identificador <%=sIdResp%> -->
