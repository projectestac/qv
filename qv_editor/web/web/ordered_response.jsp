<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html"  import="edu.xtec.qv.editor.beans.IQVListControlBean,edu.xtec.qv.editor.util.QVOrderedResponseListControl,edu.xtec.qv.qti.QTISuperResponse"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.QVPreguntaOrdenacioBean qvsb=(edu.xtec.qv.editor.beans.QVPreguntaOrdenacioBean)qvb.getSpecificBean();

String sType = IQVListControlBean.ORDERED_RESPONSE_TYPE;
String sLayer = "orderedResponseLayer";
String sForm = qvb.getParameter("p_form");
String sListName=qvb.getParameter("p_list_name");
String sAction = qvb.getParameter("action");
String sOrderedResponseAction =qvb.getParameter("p_ordered_response_action");

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
String sOrientation = qvb.getParameter(QVOrderedResponseListControl.P_ORIENTATION+"_"+sIdent, QVOrderedResponseListControl.ROW_ORIENTATION);
boolean bShuffle = true;
int iNumRespostes = qvsb.getNumResponseLabels(sIdent);
String sResponseMatType = qvb.getParameter("response_material_type_"+sIdent, qvb.getParameter("response_material_type"));
String sWidth = qvb.getParameter("width");
String sHeight = qvb.getParameter("height");
if (o!=null && sOrderedResponseAction!=null && sOrderedResponseAction.equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT)){
	edu.xtec.qv.qti.IMSRenderObject oRender = (edu.xtec.qv.qti.IMSRenderObject)o.getRender();
	if (oRender!=null){
		sOrientation = oRender.getOrientation();
		sWidth = oRender.getWidth();
		sHeight = oRender.getHeight();
	}
	bShuffle = qvsb.isShuffle(sIdent);
	if (sResponseMatType==null){
		sResponseMatType = qvsb.getResponseMaterialType(sIdent);
	}
}
if (sResponseMatType==null){
	sResponseMatType = "text";
}
String[] sRecursos = null;
if (sResponseMatType.equals("image")) sRecursos = qvb.getAssessmentResources(qvb.IMAGE_RESOURCE);
else if (sResponseMatType.equals("audio")) sRecursos = qvb.getAssessmentResources(qvb.AUDIO_RESOURCE);
%>
<INPUT type="hidden" name="ident_resp_lid" value="<%=sIdent%>" />
<INPUT type="hidden" name="num_respostes" value="<%=iNumRespostes%>"/>
<DIV id='<%=sLayer%>' style="position:absolute; top:150; left:350; width:400; z-index:1000; padding:5px; 2px solid"  class='layer-box'>
<TABLE class='layer-box' border='0' cellpadding='5' cellspacing='5' width='550'>
<TR>	
	<TD colspan="3" align="right" class="layer-title" ><%=qvb.getMsg("item.ordered_response.response.title")%></TD>
</TR>
<TR>
  <TD width='30'></TD>
  <TD width="100%">
  	<TABLE border='0' cellpadding='3' cellspacing='0' width="100%">
  	<TR>
		<TD width='25%' class='layer-text' title="<%=qvb.getMsg("item.orientation.tip")%>" ><%=qvb.getMsg("item.orientation")%></TD>	
		<TD>
			<SELECT name='<%=QVOrderedResponseListControl.P_ORIENTATION+"_"+sIdent%>' class='layer-form'>
				<OPTION value='<%=QVOrderedResponseListControl.ROW_ORIENTATION%>' <%=(sOrientation==null||sOrientation.equalsIgnoreCase(QVOrderedResponseListControl.ROW_ORIENTATION))?"selected":""%>><%=qvb.getMsg("item.orientation.row")%></OPTION>
				<OPTION value='<%=QVOrderedResponseListControl.COLUMN_ORIENTATION%>' <%=(sOrientation!=null&&sOrientation.equalsIgnoreCase(QVOrderedResponseListControl.COLUMN_ORIENTATION))?"selected":""%> ><%=qvb.getMsg("item.orientation.column")%></OPTION>
			</SELECT>
		</TD>	
  	</TR>
  	<TR>
		<TD class='layer-text' title="<%=qvb.getMsg("item.materialResponse.tip")%>" ><%=qvb.getMsg("item.materialResponse")%></TD>	
		<TD >
			<SELECT name='<%="response_material_type_"+sIdent%>' class='layer-form' onChange='enviar("set_response_material_type", <%=sForm%>)'>
				<OPTION value='text' <%=sResponseMatType.equals("text")?"selected":""%> ><%=qvb.getMsg("item.materialResponse.text")%></OPTION>
				<OPTION value='image' <%=sResponseMatType.equals("image")?"selected":""%> ><%=qvb.getMsg("item.materialResponse.image")%></OPTION>
			</SELECT>
		</TD>	
  	</TR>
  	<TR>
		<TD class='layer-text' title="<%=qvb.getMsg("item.size.tip")%>" ><%=qvb.getMsg("item.size")%></TD>
		<TD>
		  	<TABLE border='0' cellpadding='0' cellspacing='0' width="100%">
		  	<TR>
				<TD class='layer-text' title="<%=qvb.getMsg("item.width.tip")%>" >
					<%=qvb.getMsg("item.width")%>&nbsp;&nbsp;&nbsp;<INPUT type='text' size='5' name="<%=QVOrderedResponseListControl.P_WIDTH+"_"+sIdent%>" value="<%=sWidth!=null?sWidth:""%>" class="layer-form"/>
				</TD>	
				<TD class='layer-text' title="<%=qvb.getMsg("item.height.tip")%>" >
					<%=qvb.getMsg("item.height")%>&nbsp;&nbsp;&nbsp;<INPUT type='text' size='5' name="<%=QVOrderedResponseListControl.P_HEIGHT+"_"+sIdent%>" value="<%=sHeight!=null?sHeight:""%>" class="layer-form"/>
				</TD>
			</TR>
		  	</TABLE>
		</TD>	
  	</TR>
	<TR>
		<TD colspan="2" class='layer-text' title="<%=qvb.getMsg("item.shuffle.tip")%>" >
			<INPUT type="checkbox" name="<%=QVOrderedResponseListControl.P_SHUFFLE%>" class="layer-form" <%=bShuffle?"checked":""%>/><%=qvb.getMsg("item.shuffle")%></TD>
		</TD>	
	</TR>
  	</TABLE>
  </TD>
  <TD width='30' height='50%'></TD>
</TR>
<TR>
	<TD/>
	<TD>
		<TABLE width="100%" cellpadding="1" cellspacing="0" border="0" class="layer-box" style="background-color:#FFFFFF" >
<%
	for (int i=0;i<iNumRespostes;i++){
		String sIdentRespLabel = qvsb.getIdentResponseLabel(sIdent, i);
		String sTextRespLabel = qvsb.getTextResponseLabel(sIdent, sIdentRespLabel, i);
		String sResourceResp = qvsb.getResourceResponseLabel(sIdent, sIdentRespLabel);
		int iOrderRespLabel = qvsb.getOrderResponseLabel(sIdent, sIdentRespLabel);
		if (iNumRespostes==1) iOrderRespLabel=0;
%>	
		<TR>
			<TD>
				<TABLE style="background-color:#b7ccf0" id="<%="resposta_"+sIdent+"_"+i%>" width="100%" cellpadding="1" cellspacing="0" border="0" onclick="seleccionar_item('<%="resposta_"+sIdent+"_"%>', this.id, <%=iNumRespostes%>, <%=sForm+".ident_resposta"%>, '<%=sIdentRespLabel%>');" <%=sIdentRespLabel.equals(qvsb.getCurrentIdentResponseLabel())?"style='background-color:#FDB671'":""%> >
				<TR>
					<TD width='5%'>
						<INPUT type="text" size="1" name="<%=QVOrderedResponseListControl.P_RESPONSE_LABEL_ORDER+"_"+sIdent%>" value="<%=iOrderRespLabel<0?String.valueOf(i+1):String.valueOf(iOrderRespLabel+1)%>"  class="layer-form" />
					</TD>
					<TD width="10"/>
					<TD align='left'>
						<INPUT type='hidden' name='<%=QVOrderedResponseListControl.P_RESPONSE_LABEL_IDENT+"_"+sIdent%>' value='<%=sIdentRespLabel%>'/>
					</TD>
					<TD align='left' class="layer-text">
<%if (sResponseMatType.equals("text")){%>
						<INPUT type='text' size='70' name="<%=QVOrderedResponseListControl.P_RESPONSE_LABEL_TEXT+"_"+sIdent%>" value="<%=sTextRespLabel%>" class="layer-form"/>
<%}else if (sResponseMatType.equals("image")){
	if(sRecursos!=null && sRecursos.length>0){%>		
						<SELECT name="<%=QVOrderedResponseListControl.P_RESPONSE_LABEL_IMAGE+"_"+sIdent%>" class="layer-form">
							<OPTION value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</OPTION>
<%		for(int k=0;k<sRecursos.length;k++){%>
							<OPTION value="<%=sRecursos[k]%>" <%=sRecursos[k].equalsIgnoreCase(sResourceResp)?"selected":""%>><%=sRecursos[k]%></OPTION>
<%		}%>
						</SELECT>
<%	}else{%>
	 No hi ha recursos
<%} 
} %>								
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
	<TD class="layer-link" colspan="3" align="center">
		<A href="javascript:<%=sForm%>.hide_layers.value='true';<%=sForm%>.p_list_name.value='<%=sListName%>';<%=sForm%>.<%=sListName%>.value='<%=sIdent%>';<%=sForm%>.p_list_type.value='<%=sType%>';enviar('<%=sOrderedResponseAction%>', <%=sForm%>);" class="layer-link" title="<%=qvb.getMsg("item.ordered_response.action.ok.tip")%>" ><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("item.ordered_response.action.ok")%></A>
		&nbsp;|&nbsp;
		<A href="javascript:<%=sForm%>.hide_layers.value='true';hide_ordered_response_layer();" class="layer-link" title="<%=qvb.getMsg("item.ordered_response.action.cancel.tip")%>" ><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("item.ordered_response.action.cancel")%></A>
	</TD>
</TR>
</TABLE>
</DIV>
<SCRIPT>
<%if ( (sAction!=null && sAction.equalsIgnoreCase("p_ordered_response_action")) || 
	 (sAction!=null && !sAction.equalsIgnoreCase("p_material_action") && sOrderedResponseAction!=null && ( sOrderedResponseAction.equalsIgnoreCase(IQVListControlBean.A_ADD_LIST_OBJECT) ||
	  sOrderedResponseAction.equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT)))){%>
	show_ordered_response_layer(<%=sForm%>);
	if (<%=sForm%>.<%=QVOrderedResponseListControl.P_RESPONSE_LABEL_TEXT+"_"+sIdent%>!=null){
		if (<%=((sAction!=null && sAction.equalsIgnoreCase("add_pregunta")) || (sOrderedResponseAction!=null && sOrderedResponseAction.equals(IQVListControlBean.A_SET_LIST_OBJECT))) && iNumRespostes>1 && (sAction!=null && !sAction.equals("add_num_respostes"))%>){
			<%=sForm%>.<%=QVOrderedResponseListControl.P_RESPONSE_LABEL_TEXT+"_"+sIdent%>[0].focus();
		}else if (<%=sForm%>.<%=QVOrderedResponseListControl.P_RESPONSE_LABEL_TEXT+"_"+sIdent%>[<%=iNumRespostes-1%>]!=null){
			<%=sForm%>.<%=QVOrderedResponseListControl.P_RESPONSE_LABEL_TEXT+"_"+sIdent%>[<%=iNumRespostes-1%>].focus();
		}else{
			<%=sForm%>.<%=QVOrderedResponseListControl.P_RESPONSE_LABEL_TEXT+"_"+sIdent%>.focus();
		}
	}else if (<%=sForm%>.<%=QVOrderedResponseListControl.P_RESPONSE_LABEL_IMAGE+"_"+sIdent%>!=null && <%=sForm%>.<%=QVOrderedResponseListControl.P_RESPONSE_LABEL_IMAGE+"_"+sIdent%>[<%=iNumRespostes-1%>]!=null){
		<%=sForm%>.<%=QVOrderedResponseListControl.P_RESPONSE_LABEL_IMAGE+"_"+sIdent%>[<%=iNumRespostes-1%>].focus();
	}
<%}else{%>
	hide_ordered_response_layer();
<%}%>
</SCRIPT>
<!-- FI dades pel conjunt de respostes d'ordenacio amb identificador <%=sIdent%> -->
