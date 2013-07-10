<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.QVPreguntaHotspotBean qvsb=(edu.xtec.qv.editor.beans.QVPreguntaHotspotBean)qvb.getSpecificBean();
String sForm = "document.hotspotForm";

String[] sImages = qvb.getAssessmentResources(qvb.IMAGE_RESOURCE);
String sPath = qvb.getQuadernResourcesURL()+"/";
String sURI = qvb.getParameter("p_background_image_uri");
if (sURI==null){
	sURI = qvsb.getBackgroundImage();
}
int iMinNumber = qvsb.getMinNumber();
int iMaxNumber = qvsb.getMaxNumber();
String sBorderColor = qvsb.getBorderColorStyle();
if ("".equals(sBorderColor)){
	sBorderColor = "#RRGGBB";
}

String sBgImage = qvsb.getBackgroundImageStyle();
String sBgColor = qvsb.getBackgroundColorStyle();
String sBackgroundType = (sBgImage!=null || "image".equals(request.getParameter("p_hotspot_background_type")))?"image":"color";
if ("color".equals(sBackgroundType) && (sBgColor==null || "".equals(sBgColor))){
	sBgColor = "#RRGGBB";
}


String sHotspotType = qvsb.getHotspotType();
java.util.Vector vHotspotTypes = new java.util.Vector();
vHotspotTypes.addElement(qvsb.ZONE_HOTSPOT_TYPE);
//vHotspotTypes.addElement(qvsb.OPTION_HOTSPOT_TYPE);
vHotspotTypes.addElement(qvsb.FREE_HOTSPOT_TYPE);
vHotspotTypes.addElement(qvsb.DOT_HOTSPOT_TYPE);

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
<FORM name="hotspotForm" method="POST" action="edit.jsp">
<INPUT type="hidden" name="page" value="pregunta_hotspot"/>
<INPUT type="hidden" name="action"/>
<INPUT type="hidden" name="edition_mode" value="<%=qvb.getEditionMode()%>"/>
<INPUT type="hidden" name="ident_pregunta" value="<%=qvsb.getIdentPregunta()%>"/>
<INPUT type="hidden" name="ident_resposta" value="<%=qvsb.getIdentResposta()%>"/>
<INPUT type="hidden" name="hide_layers" value="<%=qvb.getParameter("hide_layers")!=null?qvb.getParameter("hide_layers"):""%>"/>
<INPUT type="hidden" name="width"/>
<INPUT type="hidden" name="height"/>
<SCRIPT language="javascript">
	setCurrentForm("hotspotForm");
</SCRIPT>
<%if (qvb.getEditionMode()!=null && qvb.getEditionMode().equalsIgnoreCase(qvb.TEXT_EDITION_MODE)){%>
<TABLE width="100%" style="height:100%" cellpadding="5" cellspacing="5" border="0" class="edit-box">
<TR>
	<TD align="right" class="edit-title"><%=qvb.filter(qvb.getMsg("item.type.hotspot.name"))%>&nbsp;&nbsp;&nbsp;</TD>
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
			<jsp:param name="assessment_title" value='<%=qvb.filter(qvb.getMsg("item.type.hotspot.name"))%>' />
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
					<TD colspan="2" class='edit-text'>
						Nombre de respostes:&nbsp;&nbsp;
						<i>mínim</i><INPUT type="text" size="1" name="p_min_number" class="layer-form" value="<%=iMinNumber%>"/>
						&nbsp;&nbsp;
						<i>màxim</i><INPUT type="text" size="1" name="p_max_number" class="layer-form" value="<%=iMaxNumber>=0?iMaxNumber:iMinNumber%>"/>
					</TD>
				</TR>
				<TR>
					<TD class='layer-text' title="<%=qvb.getMsg("item.dragdrop.style.tip")%>">
						<%=qvb.getMsg("item.dragdrop.style")%>: &nbsp;&nbsp;
						<i><%=qvb.getMsg("item.dragdrop.style.border_color")%></i>&nbsp;<INPUT type="text" name="p_style_border_color" class="layer-form" size="8" value="<%=sBorderColor%>"/>
						<jsp:include page="help.jsp" flush="true">
							<jsp:param name="p_help_page" value="html_color.html" />
						</jsp:include>
					</TD>
					<TD class='layer-text' width="65%">
						<i><%=qvb.getMsg("item.dragdrop.style.background")%></i>&nbsp;
						<SELECT name='p_hotspot_background_type' class='layer-form'"  onchange="enviar('set_hotspot_background_type', <%=sForm%>);">
							<OPTION value="color" <%="color".equals(sBackgroundType)?"selected":""%> ><%=qvb.getMsg("item.dragdrop.style.background.color")%></OPTION>
							<OPTION value="image" <%="image".equals(sBackgroundType)?"selected":""%>><%=qvb.getMsg("item.dragdrop.style.background.image")%></OPTION>
						</SELECT>&nbsp;
						<%if ("image".equals(sBackgroundType)){%>
<%
if (sImages!=null && sImages.length>0){ %>
					<SELECT name='p_style_background_image' class='layer-form'>
						<OPTION value=""></OPTION>
<%	for(int i=0;i<sImages.length;i++){%>
						<OPTION value="<%=sImages[i]%>" <%=sImages[i].equals(sBgImage)?"selected":""%> ><%=sImages[i]%></OPTION>
<%}%>
					</SELECT>
<%} else {%>
					<%=qvb.getMsg("material.msg.noImages")%>
<%}%>

						<%} else{%>
							<INPUT type="text" name="p_style_background_color" class="layer-form" size="8" value="<%=sBgColor%>"/>
							<jsp:include page="help.jsp" flush="true">
								<jsp:param name="p_help_page" value="html_color.html" />
							</jsp:include>
						<%}%>
					</TD>
				</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD class='edit-text' width='70'>Tipus</TD>
			<TD width="90%">
				<SELECT name='p_hotspot_type' class='layer-form' onchange="enviar('set_hotspot_type', <%=sForm%>);">
<%	for(int i=0;i<vHotspotTypes.size();i++){
		String tmpHotspotType = (String)vHotspotTypes.elementAt(i);%>
					<OPTION value="<%=tmpHotspotType%>" <%=tmpHotspotType.equals(sHotspotType)?"selected":""%> ><%=qvb.getMsg(tmpHotspotType)%></OPTION>
<%}%>
				</SELECT>				
			</TD>
		</TR>
		<TR>
			<TD class='edit-text' width='70'>Imatge</TD>
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
					<jsp:param name="p_list_type" value="<%=edu.xtec.qv.editor.beans.IQVListControlBean.HOTSPOT_TYPE%>"/>
					<jsp:param name="p_list_title" value='<%=qvb.getMsg("item.hotspot.target.title")%>' />
					<jsp:param name="p_list_tip" value='<%=qvb.getMsg("item.hotspot.target.tip")%>' />
					<jsp:param name="hide_up" value="true" />
					<jsp:param name="hide_down" value="true" />
					<jsp:param name="p_help_page" value='<%=((edu.xtec.qv.editor.util.QVConstants.DOT_HOTSPOT_TYPE.equals(sHotspotType))?"hotspot_dot_rarea.html":"hotspot_rarea.html")%>' />
				</jsp:include>
			</TD>
		</TR>
<%if (edu.xtec.qv.editor.util.QVConstants.DOT_HOTSPOT_TYPE.equals(sHotspotType)){%>
		<TR>
			<TD colspan='2'>
				<jsp:include page="listControl.jsp" flush="true">
					<jsp:param name="p_form" value="<%=sForm%>" />
					<jsp:param name="p_list_name" value="dot_response_list"/>
					<jsp:param name="p_list_type" value="<%=edu.xtec.qv.editor.beans.IQVListControlBean.DOT_RESPONSE_TYPE%>"/>
					<jsp:param name="p_list_title" value='<%=qvb.getMsg("item.hotspot.dot.response.title")%>' />
					<jsp:param name="p_list_tip" value='<%=qvb.getMsg("item.hotspot.dot.response.tip")%>' />
					<jsp:param name="hide_up" value="true" />
					<jsp:param name="hide_down" value="true" />
					<jsp:param name="p_help_page" value="hotspot_dot_response.html" />
				</jsp:include>
			</TD>
		</TR>
<%}%>		
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
