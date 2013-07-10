<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.QVPreguntaDragDropBean qvsb=(edu.xtec.qv.editor.beans.QVPreguntaDragDropBean)qvb.getSpecificBean();
String sForm = "document.dragdropForm";

String[] sImages = qvb.getAssessmentResources(qvb.IMAGE_RESOURCE);
String sPath = qvb.getQuadernResourcesURL()+"/";
String sURI = qvb.getParameter("p_background_image_uri");
if (sURI==null){
	sURI = qvsb.getBackgroundImage();
}

boolean bShuffle = qvsb.isShuffle();
boolean bInside = qvsb.isInside();
boolean bAlign = qvsb.ALIGN_AUTO.equals(qvsb.getAlign());
String sBorderColor = qvsb.getBorderColorStyle();
if ("".equals(sBorderColor)){
	sBorderColor = "#RRGGBB";
}

if (!qvb.isHeaderLoaded()){
%>
<jsp:include page="header.jsp" flush="true" />
<%}%>

<%if (sImages!=null && sImages.length>0){ %>
<SCRIPT>
var image_ = new Image();
<%	for(int i=0;i<sImages.length;i++){%>
		var image_<%=sImages[i].replace('.','_')%> = new Image();
		image_<%=sImages[i].replace('.','_')%>.src="<%=sPath+sImages[i]%>";
<%	}%>
</SCRIPT>
<%}%>
<FORM name="dragdropForm" method="POST" action="edit.jsp">
<INPUT type="hidden" name="page" value="pregunta_dragdrop"/>
<INPUT type="hidden" name="edition_mode" value="<%=qvb.getEditionMode()%>"/>
<INPUT type="hidden" name="action"/>
<INPUT type="hidden" name="ident_pregunta" value="<%=qvsb.getIdentPregunta()%>"/>
<INPUT type="hidden" name="ident_resposta" value="<%=qvsb.getIdentResposta()%>"/>
<INPUT type="hidden" name="hide_layers" value="<%=qvb.getParameter("hide_layers")!=null?qvb.getParameter("hide_layers"):""%>"/>
<INPUT type="hidden" name="width"/>
<INPUT type="hidden" name="height"/>
<SCRIPT language="javascript">
	setCurrentForm("dragdropForm");
</SCRIPT>
<%if (qvb.getEditionMode()!=null && qvb.getEditionMode().equalsIgnoreCase(qvb.TEXT_EDITION_MODE)){%>
<TABLE width="100%" style="height:100%" cellpadding="5" cellspacing="5" border="0" class="edit-box">
<TR>
	<TD align="right" class="edit-title"><%=qvb.filter(qvb.getMsg("item.type.drag_drop.name"))%>&nbsp;&nbsp;&nbsp;</TD>
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
<TABLE width="100%" style="height:100%"cellpadding="0" cellspacing="0" border="0"> 
<TR>
	<TD class="edit-box" valign="top">
		<jsp:include page="enunciat.jsp" flush="true">
			<jsp:param name="form" value="<%=sForm%>" />
			<jsp:param name="assessment_title" value='<%=qvb.filter(qvb.getMsg("item.type.drag_drop.name"))%>' />
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
		<TABLE class='edit-box' width="100%" cellpadding="5" cellspacing="0" border="0">
		<TR>
			<TD colspan="2">
				<TABLE class='edit-box' width="100%" cellpadding="5" cellspacing="0" border="0">
				<TR>
					<TD class='layer-text' title="<%=qvb.getMsg("item.dragdrop.shuffle.tip")%>">
						<INPUT type="checkbox" name="p_shuffle" class="layer-form" <%=bShuffle?"checked":""%>/><%=qvb.getMsg("item.dragdrop.shuffle")%>
					</TD>
				</TR>
				<TR>
					<TD class='layer-text' title="<%=qvb.getMsg("item.dragdrop.inside.tip")%>">
						<INPUT type="checkbox" name="p_inside" class="layer-form" <%=bInside?"checked":""%>/><%=qvb.getMsg("item.dragdrop.inside")%>
					</TD>
				</TR>
				<TR>
					<TD class='layer-text' title="<%=qvb.getMsg("item.dragdrop.align.tip")%>">
						<INPUT type="checkbox" name="p_align" class="layer-form" <%=bAlign?"checked":""%>/><%=qvb.getMsg("item.dragdrop.align")%>
					</TD>
				</TR>
				<TR>
					<TD class='layer-text' title="<%=qvb.getMsg("item.dragdrop.style.tip")%>">
						<%=qvb.getMsg("item.dragdrop.style")%>: &nbsp;&nbsp;
						<i><%=qvb.getMsg("item.dragdrop.style.border_color")%></i>&nbsp;<INPUT type="text" name="p_style_border_color" class="layer-form" size="8" value="<%=sBorderColor%>"/>&nbsp;&nbsp;&nbsp;&nbsp;
						<jsp:include page="help.jsp" flush="true">
							<jsp:param name="p_help_page" value="html_color.html" />
						</jsp:include>			
					</TD>
				</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD class='edit-text' width='70'>Fons</TD>
			<TD width="90%">
<%
if (sImages!=null && sImages.length>0){ %>
					<SELECT name='p_background_image_uri' onChange='var img=eval(new String("image_"+this.value).replace(/\./g, "_"));this.form.width.value=img.width;this.form.height.value=img.height' class='layer-form' >
						<OPTION value=""></OPTION>
<%	for(int i=0;i<sImages.length;i++){%>
						<OPTION value="<%=sImages[i]%>" <%=sImages[i].equals(sURI)?"selected":""%> ><%=sImages[i]%></OPTION>
<%}%>
					</SELECT>
					&nbsp;&nbsp;&nbsp;&nbsp;<A href="javascript:var img=eval(new String('image_'+<%=sForm%>.p_background_image_uri.value).replace(/\./g, '_'));open_popup('image_xy.jsp?image='+img.src+'&width='+img.width+'&height='+img.height,'image_xy',img.width+70, img.height+90);void(0);" class="link">Coordenades XY</A>
<%} else {%>
					<%=qvb.getMsg("material.msg.noImages")%>
<%}%>
				
			</TD>
		</TR>
		<TR>
			<TD colspan='2'>
				<jsp:include page="listControl.jsp" flush="true">
					<jsp:param name="p_form" value="<%=sForm%>" />
					<jsp:param name="p_list_name" value="rarea_response_list"/>
					<jsp:param name="p_list_type" value="<%=edu.xtec.qv.editor.beans.IQVListControlBean.TARGET_TYPE%>"/>
					<jsp:param name="p_list_title" value='<%=qvb.getMsg("item.dragdrop.target.title")%>' />
					<jsp:param name="p_list_tip" value='<%=qvb.getMsg("item.dragdrop.target.tip")%>' />
					<jsp:param name="hide_up" value="true" />
					<jsp:param name="hide_down" value="true" />
					<jsp:param name="p_help_page" value="dragdrop_target.html" />
				</jsp:include>
			</TD>
		</TR>
		<TR>
			<TD colspan='2'>
				<jsp:include page="listControl.jsp" flush="true">
					<jsp:param name="p_form" value="<%=sForm%>" />
					<jsp:param name="p_list_name" value="source_response_list"/>
					<jsp:param name="p_list_type" value="<%=edu.xtec.qv.editor.beans.IQVListControlBean.SOURCE_TYPE%>"/>
					<jsp:param name="p_list_title" value='<%=qvb.getMsg("item.dragdrop.source.title")%>' />
					<jsp:param name="p_list_tip" value='<%=qvb.getMsg("item.dragdrop.source.tip")%>' />
					<jsp:param name="hide_up" value="true" />
					<jsp:param name="hide_down" value="true" />
					<jsp:param name="p_help_page" value="dragdrop_source.html" />
				</jsp:include>
			</TD>
		</TR>
		<TR>
			<TD colspan='2'>
				<jsp:include page="listControl.jsp" flush="true">
					<jsp:param name="p_form" value="<%=sForm%>" />
					<jsp:param name="p_list_name" value="dragdrop_response_list"/>
					<jsp:param name="p_list_type" value="<%=edu.xtec.qv.editor.beans.IQVListControlBean.DRAGDROP_RESPONSE_TYPE%>"/>
					<jsp:param name="p_list_title" value='<%=qvb.getMsg("item.hotspot.dot.response.title")%>' />
					<jsp:param name="p_list_tip" value='<%=qvb.getMsg("item.hotspot.dot.response.tip")%>' />
					<jsp:param name="hide_up" value="true" />
					<jsp:param name="hide_down" value="true" />
					<jsp:param name="p_help_page" value="dragdrop_responses.html" />
				</jsp:include>
			</TD>
		</TR>
		</TABLE>
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
