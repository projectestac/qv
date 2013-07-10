<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.QVPreguntaDrawBean qvsb=(edu.xtec.qv.editor.beans.QVPreguntaDrawBean)qvb.getSpecificBean();
String sForm = "document.dragdropForm";

String[] sImages = qvb.getAssessmentResources(qvb.IMAGE_RESOURCE);
String sPath = qvb.getQuadernResourcesURL()+"/";
String sURI = qvb.getParameter("p_background_image_uri");
if (sURI==null){
	sURI = qvsb.getBackgroundImage();
}
int iColors = qvsb.getColors();

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
<INPUT type="hidden" name="page" value="pregunta_draw"/>
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
	<TD align="right" class="edit-title"><%=qvb.filter(qvb.getMsg("item.type.draw.name"))%>&nbsp;&nbsp;&nbsp;</TD>
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
			<jsp:param name="assessment_title" value='<%=qvb.filter(qvb.getMsg("item.type.draw.name"))%>' />
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
			<TD class='edit-text' width='200'><%=qvb.getMsg("item.draw.background")%></TD>
			<TD width="80%">
<%
if (sImages!=null && sImages.length>0){ %>
					<SELECT name='p_background_image_uri' onChange='var img=eval(new String("image_"+this.value).replace(/\./g, "_"));this.form.width.value=img.width;this.form.height.value=img.height' class='layer-form' >
						<OPTION value=""></OPTION>
<%	for(int i=0;i<sImages.length;i++){%>
						<OPTION value="<%=sImages[i]%>" <%=sImages[i].equals(sURI)?"selected":""%> ><%=sImages[i]%></OPTION>
<%}%>
					</SELECT>
					&nbsp;&nbsp;&nbsp;&nbsp;<A href="javascript:var img=eval(new String('image_'+<%=sForm%>.p_background_image_uri.value).replace(/\./g, '_'));open_popup(img.src,'image_xy',img.width, img.height);void(0);" class="link"><%=qvb.getMsg("item.draw.action.view")%></A>
<%} else {%>
					<%=qvb.getMsg("material.msg.noImages")%>
<%}%>
				
			</TD>
		</TR>
		<TR>
			<TD class='edit-text' width='200'><%=qvb.getMsg("item.draw.colors")%></TD>
			<TD>
				<SELECT name='p_colors' class='layer-form'>
<%	for(int i=1;i<9;i++){%>
					<OPTION value="<%=i%>" <%=iColors==i?"selected":""%> ><%=i%></OPTION>
<%}%>
				</SELECT>
			</TD>
		</TR>
		<TR title='<%=qvb.getMsg("item.draw.size.tip")%>'>
			<TD class='edit-text' width='200' ><%=qvb.getMsg("material.width")%></TD>
			<TD class='edit-text' >
				<INPUT name="p_width" type="text" size="4" class='layer-form' value="<%=qvsb.getWidth()%>"/> píxels
			</TD>
		</TR>
		<TR title='<%=qvb.getMsg("item.draw.size.tip")%>'>
			<TD class='edit-text' width='200' ><%=qvb.getMsg("material.height")%></TD>
			<TD class='edit-text' >
				<INPUT name="p_height" type="text" size="4" class='layer-form' value="<%=qvsb.getHeight()%>"/> píxels
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
