<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" import="edu.xtec.qv.editor.beans.IQVListControlBean,edu.xtec.qv.qti.QTISuperMat"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.IQVListControlBean qvsb=(edu.xtec.qv.editor.beans.IQVListControlBean)qvb.getSpecificBean();

String sType = IQVListControlBean.MATERIAL_TYPE;
String sLayer = "materialLayer";
String sForm = qvb.getParameter("p_form");
String sListName=qvb.getParameter("p_list_name");
String sAction = qvb.getParameter("action");
String sMaterialAction =qvb.getParameter("p_material_action");

String sIdent = qvb.getParameter(sListName);
edu.xtec.qv.qti.QTISuperMat o = (edu.xtec.qv.qti.QTISuperMat)qvsb.getObject(sType, sIdent);
String sMatContentType = edu.xtec.qv.qti.QTISuperMat.MATHTML_CONTENT_TYPE;
String sText = "";
String sURI = "";
String sWidth = "";
String sHeight = "";
if (o!=null && sMaterialAction!=null && sMaterialAction.equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT)){
	sMatContentType = o.getMatContentType();
	sText = o.getText();
	sURI = o.getURI();
	if (o instanceof edu.xtec.qv.qti.Matimage){
		sWidth = String.valueOf(((edu.xtec.qv.qti.Matimage)o).getWidth());
		sHeight = String.valueOf(((edu.xtec.qv.qti.Matimage)o).getHeight());
	}
}

java.util.Vector vContentTypes = new java.util.Vector();
vContentTypes.addElement(edu.xtec.qv.qti.QTISuperMat.MATHTML_CONTENT_TYPE);
vContentTypes.addElement(edu.xtec.qv.qti.QTISuperMat.MATTEXT_CONTENT_TYPE);
vContentTypes.addElement(edu.xtec.qv.qti.QTISuperMat.MATIMAGE_CONTENT_TYPE);
vContentTypes.addElement(edu.xtec.qv.qti.QTISuperMat.MATAUDIO_CONTENT_TYPE);
vContentTypes.addElement(edu.xtec.qv.qti.QTISuperMat.MATVIDEO_CONTENT_TYPE);
vContentTypes.addElement(edu.xtec.qv.qti.QTISuperMat.MATBREAK_CONTENT_TYPE);
%>	

<DIV id='<%=sLayer%>' style="position:absolute; top:150; left:350; z-index:1000; width:550; height: 400; padding:5px; 2px solid"  class='layer-box'>
<TABLE class='layer-box' border='0' cellpadding='5' cellspacing='5' width='100%' height='100%' style='height:100%'>
<TR>	
	<TD colspan="2" align="right" class="layer-title" ><%=qvb.getMsg("material.title")%></TD>
</TR>
<TR>
  <TD valign="top" height='100%' style='height:100%'>
  	<TABLE border='0' cellpadding='5' cellspacing='5' height='100%' style='height:100%'>
  	<TR>
  		<TD class="layer-text" valign="top" width="60"><%=qvb.getMsg("material.contentType")%></TD>
  		<TD valign="top">
			<SELECT name="p_content_type" size="1" class="layer-form" onchange="set_material_layer(this.value);" >
<%
java.util.Enumeration enumTypes=vContentTypes.elements();
while(enumTypes.hasMoreElements()){
	String sContentType = (String)enumTypes.nextElement();
%>	
				<OPTION value="<%=sContentType%>" <%=sContentType.equalsIgnoreCase(sMatContentType)?"selected":""%> ><%=qvb.getMsg("material.contentType."+sContentType)%></OPTION>
<%}%>
			</SELECT>
  		</TD>
  	</TR>
  	<TR>
  		<TD colspan="2" height='100%' style='height:100%'>
			<jsp:include page="mattext.jsp" flush="true">
				<jsp:param name="p_material_text" value="<%=edu.xtec.qv.qti.util.StringUtil.htmlToText(sText)%>" />
				<jsp:param name="display" value="<%=String.valueOf(o!=null && QTISuperMat.isMattext(sMatContentType) || (sMaterialAction!=null && sMaterialAction.equalsIgnoreCase(IQVListControlBean.A_ADD_LIST_OBJECT)))%>" />
			</jsp:include>
			
			<jsp:include page="mathtml.jsp" flush="true">
				<jsp:param name="p_material_html" value="<%=sText%>" />
				<jsp:param name="display" value="<%=String.valueOf(o!=null && QTISuperMat.isMatHTML(sMatContentType))%>" />
			</jsp:include>

			<jsp:include page="matimage.jsp" flush="true">
				<jsp:param name="p_material_uri" value="<%=sURI%>" />
				<jsp:param name="p_material_width" value="<%=sWidth%>" />
				<jsp:param name="p_material_height" value="<%=sHeight%>" />
				<jsp:param name="display" value="<%=String.valueOf(o!=null && QTISuperMat.isMatimage(sMatContentType))%>" />
			</jsp:include>

			<jsp:include page="mataudio.jsp" flush="true">
				<jsp:param name="p_material_audio_uri" value="<%=sURI%>" />
				<jsp:param name="display" value="<%=String.valueOf(o!=null && QTISuperMat.isMataudio(sMatContentType))%>" />
			</jsp:include>

			<jsp:include page="matvideo.jsp" flush="true">
				<jsp:param name="p_material_video_uri" value="<%=sURI%>" />
				<jsp:param name="display" value="<%=String.valueOf(o!=null && QTISuperMat.isMatvideo(sMatContentType))%>" />
			</jsp:include>
  		</TD>
  	</TR>
  	</TABLE>
  </TD>
  <TD width='30'></TD>
</TR>
<TR>	
	<TD class="layer-link" colspan="2" align="center">
		<A href="javascript:<%=sForm%>.hide_layers.value='true';<%=sForm%>.p_material_html.value=p_material_htmlEditor.getHTML();<%=sForm%>.p_list_name.value='<%=sListName%>';<%=sForm%>.<%=sListName%>.value='<%=sIdent%>';<%=sForm%>.p_list_type.value='<%=sType%>';enviar('<%=sMaterialAction%>', <%=sForm%>);" title="<%=qvb.getMsg("material.action.ok.tip")%>" class="layer-link"><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("material.action.ok")%></A>
		&nbsp;|&nbsp;
		<A href="javascript:<%=sForm%>.p_content_type.value='';hide_material_layer();" title="<%=qvb.getMsg("material.action.cancel.tip")%>" class="layer-link"><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("material.action.cancel")%></A>
	</TD>
</TR>
</TABLE>
</DIV>
<SCRIPT>
<%if (sAction!=null && sAction.equalsIgnoreCase("p_material_action") && sMaterialAction!=null && 
	( sMaterialAction.equalsIgnoreCase(IQVListControlBean.A_ADD_LIST_OBJECT) ||
	  sMaterialAction.equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT))){%>
	set_material_layer('<%=sMatContentType%>');
	show_material_layer(<%=sForm%>);
<%}else{%>
	hide_material_layer();
<%}%>
</SCRIPT>


