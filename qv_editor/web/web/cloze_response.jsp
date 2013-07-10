<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html"  import="edu.xtec.qv.editor.beans.IQVListControlBean,edu.xtec.qv.editor.util.QVClozeResponseListControl,edu.xtec.qv.qti.QTISuperResponse"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.QVPreguntaClozeBean qvsb=(edu.xtec.qv.editor.beans.QVPreguntaClozeBean)qvb.getSpecificBean();

String sType = IQVListControlBean.CLOZE_RESPONSE_TYPE;
String sLayer = "clozeResponseLayer";
String sForm = qvb.getParameter("p_form");
String sListName=qvb.getParameter("p_list_name");
String sAction = qvb.getParameter("action");
String sClozeResponseAction =qvb.getParameter("p_cloze_response_action");

String sIdent = qvb.getParameter(sListName);
if (sIdent==null){
	sIdent = qvb.getParameter("ident_resp_lid");
	if (sIdent==null){
		sIdent = qvb.getParameter("ident_resposta");
		if (sIdent==null){
			sIdent = edu.xtec.qv.editor.util.QTIUtil.getRandomIdent();
		}
	}
}
QTISuperResponse o = (QTISuperResponse)qvsb.getObject(sType, sIdent);
int iNumRespostes = qvsb.getNumResponseLabels(sIdent);
String sResponseOptionType = qvb.getParameter("response_option_type");
if (sResponseOptionType==null){
	sResponseOptionType = (String)session.getAttribute("response_option_type");
}
String sRows = qvb.getParameter(QVClozeResponseListControl.P_ROWS);
String sColumns = qvb.getParameter(QVClozeResponseListControl.P_COLUMNS);
if (o!=null && sClozeResponseAction!=null && sClozeResponseAction.equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT)){
	if (sResponseOptionType==null || sAction==null || (!sAction.equals("set_response_option_type") && !sAction.equals("add_num_respostes"))){
		sResponseOptionType = qvsb.showAsList(sIdent)?"close":"open";
	}
	sRows = qvsb.getRows(sIdent);
	sColumns = qvsb.getColumns(sIdent);
}
%>
<INPUT type="hidden" name="ident_resp_lid" value="<%=sIdent%>" />
<INPUT type="hidden" name="num_respostes" value="<%=iNumRespostes%>"/>
<DIV id='<%=sLayer%>' style="position:absolute; top:150; left:350; width:550; z-index:1000; padding:5px;"  class='layer-box'>
<TABLE class='layer-box' border='0' cellpadding='0' cellspacing='5' width='550'>
<TR>	
	<TD colspan="3" align="right" class="layer-title" >
		<%=qvb.getMsg("item.cloze_response.response.title")%>
		<BR/><B>[<%=sIdent%>]</B>
		<BR/><P/>
	</TD>
</TR>
<TR>
  <TD width='30'></TD>
  <TD width="100%">
  	<TABLE border='0' cellpadding='3' cellspacing='0' width="100%">
  	<TR>
		<TD width='150' class='layer-text' title="<%=qvb.getMsg("item.response_option_type.tip")%>" ><%=qvb.getMsg("item.response_option_type")%></TD>
		<TD>
			<SELECT name='response_option_type' class='layer-form' onchange='enviar("set_response_option_type", <%=sForm%>)'>
				<OPTION value='close' <%=sResponseOptionType.equals("close")?"selected":""%> ><%=qvb.getMsg("item.response_option_type.close")%></OPTION>
				<OPTION value='open' <%=sResponseOptionType.equals("open")?"selected":""%>><%=qvb.getMsg("item.response_option_type.open")%></OPTION>
			</SELECT>
		</TD>	
	</TR>
  	</TABLE>
  </TD>
  <TD width='30' height='50%'></TD>
</TR>
<%if (sResponseOptionType.equals("open")){%>
<TR>
  <TD width='30'></TD>
  <TD width="100%">
  	<TABLE border='0' cellpadding='3' cellspacing='0' width="100%">
  	<TR>
		<TD width='150' class='layer-text' title="<%=qvb.getMsg("item.size.tip")%>" ><%=qvb.getMsg("item.size")%></TD>
		<TD class='layer-text' title="<%=qvb.getMsg("item.rows.tip")%>" >
			<%=qvb.getMsg("item.rows")%>&nbsp;&nbsp;&nbsp;<INPUT type='text' size='5' name="<%=QVClozeResponseListControl.P_ROWS+"_"+sIdent%>" value="<%=sRows!=null?sRows:""%>" class="layer-form"/>
		</TD>	
		<TD class='layer-text' title="<%=qvb.getMsg("item.columns.tip")%>" >
			<%=qvb.getMsg("item.columns")%>&nbsp;&nbsp;&nbsp;<INPUT type='text' size='5' name="<%=QVClozeResponseListControl.P_COLUMNS+"_"+sIdent%>" value="<%=sColumns!=null?sColumns:""%>" class="layer-form"/>
		</TD>
	</TR>
  	</TABLE>
  </TD>
  <TD width='30' height='50%'></TD>
</TR>
<%}%>
<TR>
	<TD/>
	<TD class="layer-text" valign="bottom">
		<%if(sResponseOptionType.equals("open")){%><%=qvb.getMsg("item.cloze_response.valid_responses")%><%}%>
		<%if(sResponseOptionType.equals("close")){%><%=qvb.getMsg("item.cloze_response.options_to_show")%><%}%>
	</TD>
	<TD/>
</TR>
<TR>
	<TD/>
	<TD>
		<TABLE width="100%" cellpadding="1" cellspacing="0" border="0" class="layer-box" style="background-color:#FFFFFF" >
<%
	for (int i=0;i<iNumRespostes;i++){
		String sIdentRespLabel = qvsb.getIdentResponseLabel(sIdent, i);
		String sTextRespLabel = qvsb.getTextResponseLabel(sIdent, sIdentRespLabel, i);
%>	
		<TR>
			<TD>
				<TABLE style="background-color:#b7ccf0" id="<%="resposta_"+sIdent+"_"+i%>" width="100%" cellpadding="1" cellspacing="0" border="0" onclick="seleccionar_item('<%="resposta_"+sIdent+"_"%>', this.id, <%=iNumRespostes%>, <%=sForm+".ident_resposta"%>, '<%=sIdentRespLabel%>');" <%=sIdentRespLabel.equals(qvsb.getCurrentIdentResponseLabel())?"style='background-color:#FDB671'":""%> >
				<TR>
					<TD width="10"/>
					<TD align='left'>
<%if (sResponseOptionType!=null && sResponseOptionType.equals("close") ){%>
						<INPUT type="radio" name="<%=QVClozeResponseListControl.P_CORRECT_RESPONSES+"_"+sIdent%>" value="<%=sIdentRespLabel%>"  <%=qvsb.isCorrectResponseLabel(sIdent, sIdentRespLabel)?"checked":""%> />
<%}%>					
					</TD>
					<TD align='left' class="layer-text">
						<INPUT type='hidden' name='<%=QVClozeResponseListControl.P_RESPONSE_LABEL_IDENT+"_"+sIdent%>' value='<%=sIdentRespLabel%>'/>
						<INPUT type='text' size='70' name="<%=QVClozeResponseListControl.P_RESPONSE_LABEL_TEXT+"_"+sIdent%>" value="<%=sTextRespLabel%>" class="layer-form"/>
					</TD>
				</TR>
				</TABLE>
			</TD>
		</TR>
<% } %>				
		</TABLE>
	</TD>
	<TD/>
</TR>
<TR>
	<TD/>
	<TD>
		<jsp:include page="control.jsp" flush="true">
			<jsp:param name="form" value="<%=sForm%>" />
			<jsp:param name="name_add_action_param" value="add_num_respostes" />
			<jsp:param name="name_del_action_param" value="del_resposta" />
			<jsp:param name="name_up_action_param" value="up_resposta" />
			<jsp:param name="name_down_action_param" value="down_resposta" />
			<jsp:param name="name_num_items" value="num_respostes" />
			<jsp:param name="value_num_items" value="<%=String.valueOf(iNumRespostes)%>" />
			<jsp:param name="name_param" value="ident_resp_lid" />
			<jsp:param name="value_param" value="<%=sIdent%>" />
		</jsp:include>
	</TD>
	<TD/>
</TR>
<TR>
	<TD height="20"/>
</TR>
<TR>	
	<TD class="layer-link" colspan="3" align="center">
		<A href="javascript:<%=sForm%>.hide_layers.value='true';<%=sForm%>.p_list_name.value='<%=sListName%>';<%=sForm%>.<%=sListName%>.value='<%=sIdent%>';<%=sForm%>.p_list_type.value='<%=sType%>';enviar('<%=sClozeResponseAction%>', <%=sForm%>);" class="layer-link" title="<%=qvb.getMsg("item.cloze_response.action.ok.tip")%>" ><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("item.cloze_response.action.ok")%></A>
		&nbsp;|&nbsp;
		<A href="javascript:<%=sForm%>.hide_layers.value='true';hide_cloze_response_layer();" class="layer-link" title="<%=qvb.getMsg("item.cloze_response.action.cancel.tip")%>" ><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("item.cloze_response.action.cancel")%></A>
	</TD>
</TR>
</TABLE>
</DIV>
<SCRIPT>
<%if ( (sAction!=null && sAction.equalsIgnoreCase("p_ordered_response_action")) || 
	 (sAction!=null && !sAction.equalsIgnoreCase("p_material_action") && sClozeResponseAction!=null && ( sClozeResponseAction.equalsIgnoreCase(IQVListControlBean.A_ADD_LIST_OBJECT) ||
	  sClozeResponseAction.equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT)))){%>
	show_cloze_response_layer(<%=sForm%>);
	if (<%=sForm%>.<%=QVClozeResponseListControl.P_RESPONSE_LABEL_TEXT+"_"+sIdent%>!=null){
		if (<%=((sAction!=null && sAction.equalsIgnoreCase("add_pregunta")) || (sClozeResponseAction!=null && sClozeResponseAction.equals(IQVListControlBean.A_SET_LIST_OBJECT))) && iNumRespostes>1 && (sAction!=null && !sAction.equals("add_num_respostes"))%>){
			<%=sForm%>.<%=QVClozeResponseListControl.P_RESPONSE_LABEL_TEXT+"_"+sIdent%>[0].focus();
		}else if (<%=sForm%>.<%=QVClozeResponseListControl.P_RESPONSE_LABEL_TEXT+"_"+sIdent%>[<%=iNumRespostes-1%>]!=null){
			<%=sForm%>.<%=QVClozeResponseListControl.P_RESPONSE_LABEL_TEXT+"_"+sIdent%>[<%=iNumRespostes-1%>].focus();
		}else{
			<%=sForm%>.<%=QVClozeResponseListControl.P_RESPONSE_LABEL_TEXT+"_"+sIdent%>.focus();
		}
	}
<%}else{%>
	hide_cloze_response_layer();
<%}%>
</SCRIPT>
<!-- FI dades pel conjunt de respostes d'ordenacio amb identificador <%=sIdent%> -->
