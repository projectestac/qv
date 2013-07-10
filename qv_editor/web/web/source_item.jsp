<%@page contentType="text/html; charset=ISO-8859-1" import="edu.xtec.qv.editor.beans.IQVListControlBean"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" /><%
if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.IQVListControlBean qvsb=(edu.xtec.qv.editor.beans.IQVListControlBean)qvb.getSpecificBean();


String sType = IQVListControlBean.SOURCE_TYPE;
String sLayer = "sourceLayer";
String sForm = qvb.getParameter("p_form");
String sListName = qvb.getParameter("p_list_name");
String sAction = qvb.getParameter("action");
String sSourceAction =qvb.getParameter("p_source_action");

String sIdent = qvb.getParameter(sListName);
edu.xtec.qv.qti.ResponseLabel o = null;
String sContinue = request.getParameter("p_continue");
if ("next".equals(sContinue)){
	 o = (edu.xtec.qv.qti.ResponseLabel)qvsb.getNextObject(sType, sIdent);
}else if ("back".equals(sContinue)){
	 o = (edu.xtec.qv.qti.ResponseLabel)qvsb.getPreviousObject(sType, sIdent);
}else{
	 o = (edu.xtec.qv.qti.ResponseLabel)qvsb.getObject(sType, sIdent);
}
if (o!=null) sIdent = o.getIdent();
else sSourceAction=IQVListControlBean.A_ADD_LIST_OBJECT;
String sText = "";
String sURI = "";
String sWidth = "0";
String sHeight = "0";
String sX0 = "";
String sY0 = "";
if (o!=null && sSourceAction!=null && sSourceAction.equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT)){
	sText = o.getMatText();
	sURI = o.getURI();
	sWidth = o.getWidth();
	sHeight = o.getHeight();
	sX0 = o.getX0();
	sY0 = o.getY0();
}

String[] sImages = qvb.getAssessmentResources(qvb.IMAGE_RESOURCE);
String sPath = qvb.getQuadernResourcesURL()+"/";

String sNullImage = "imatges/pixel.gif";
%>

<script language="JavaScript">
<!--
var form = <%=sForm%>;
document.write('<INPUT type="hidden" name="show">');
// -->
</script>
<SCRIPT src="scripts/move_layer.js"></SCRIPT>


<%if (sImages!=null && sImages.length>0){ %>
<SCRIPT>
var image_ = new Image();
image_.src="<%=sNullImage%>";
<%	for(int i=0;i<sImages.length;i++){%>
		var image_<%=sImages[i].replace('.','_')%> = new Image();
		image_<%=sImages[i].replace('.','_')%>.src="<%=sPath+sImages[i]%>";
<%	}%>
</SCRIPT>
<%}%>
<INPUT type="hidden" name="p_continue">
<DIV id='<%=sLayer%>' style="position:absolute; top:100; left:250; z-index:1000; width:400; padding:5px;"  class='layer-box' >
<TABLE class='layer-box' border='0' cellpadding='5' cellspacing='5' width='450px' height='100%' style='height:100%'>
<TR>
	<TD>
		<TABLE border='0' cellpadding='0' cellspacing='0' style="width:100%">
		<TR>
			<TD valign="top" style="width:50px;background:url('imatges/move_layer_off.gif') no-repeat;" onmousedown="move('<%=sLayer%>');" title="<%=qvb.getMsg("layer.move.tip")%>">&nbsp;</TD>
			<TD align="right" class="layer-title">
				<%=qvb.getMsg("item.response_label.source.title")%>
				<br>
				<A href="javascript:<%=sForm%>.p_continue.value='back';<%=sForm%>.p_list_name.value='<%=sListName%>';<%=sForm%>.<%=sListName%>.value='<%=sIdent%>';<%=sForm%>.p_list_type.value='<%=sType%>';enviar('p_source_action', <%=sForm%>);" class="layer-link" style="text-decoration:none">&lt;</A>
				<A href="javascript:<%=sForm%>.p_continue.value='next';<%=sForm%>.p_list_name.value='<%=sListName%>';<%=sForm%>.<%=sListName%>.value='<%=sIdent%>';<%=sForm%>.p_list_type.value='<%=sType%>';enviar('p_source_action', <%=sForm%>);" class="layer-link" style="text-decoration:none">&gt;</A>
			</TD>
		</TR>
		</TABLE>
	</TD>
</TR>
<TR>
  <TD valign="top" height='100%' style='height:100%'>
  	<TABLE border='0' cellpadding='5' cellspacing='0' >
	<TR>
		<TD class="layer-text" valign="top" width="80"><%=qvb.getMsg("item.response_label.source.uri")%></TD>
		<TD valign="top" class="layer-form" colspan="3" >
	<%
	if (sImages!=null && sImages.length>0){ %>
						<SELECT name='p_source_uri' onChange='var img=eval(new String("image_"+this.value).replace(/\./g, "_"));document.p_image_preview.src=img.src;document.p_image_preview.width=getProportionalWidth(img.width,img.height,150,150);document.p_image_preview.height=getProportionalHeight(img.width,img.height,150,150);this.form.p_source_width.value=img.width;this.form.p_source_height.value=img.height' class='layer-form' >
							<OPTION value=""></OPTION>
	<%	for(int i=0;i<sImages.length;i++){%>
							<OPTION value="<%=sImages[i]%>" <%=sImages[i].equals(sURI)?"selected":""%> ><%=sImages[i]%></OPTION>
	<%}%>
						</SELECT>
	<%} else {%>
						<%=qvb.getMsg("material.msg.noImages")%>
	<%}%>

	  	</TD>
	</TR>
	<TR>
		<TD class="layer-text" valign="top" ><%=qvb.getMsg("material.text")%></TD>
		<TD class="layer-form" colspan="3">
			<TEXTAREA name="p_source_text" rows="3" cols="30" class='layer-form'><%=sText%></TEXTAREA>
		</TD>
	</TR>
	<TR>
		<TD class="layer-text" valign="top" ><%=qvb.getMsg("material.width")%></TD>
		<TD class="layer-text" colspan="3">
			<INPUT type="text" name="p_source_width" size="4" class='layer-form' value="<%=sWidth%>"/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=qvb.getMsg("material.height")%>
			<INPUT type="text" name="p_source_height" size="4" class='layer-form' value="<%=sHeight%>"/>
		</TD>
	</TR>
  	<TR>
		<TD height='30' colspan='4'></TD>
	</TR>
	<TR>
		<TD colspan="4" align="center" class="layer-box">
			<IMG name="p_image_preview" src="<%=((sURI==null || sURI.length()<=0) && sImages!=null && sImages.length>0)?sNullImage:sPath+sURI%>" width="100 height="100"/>
		</TD>
	</TR>
	</TABLE>
  </TD>
  <TD width='30'></TD>
</TR>
<TR>	
	<TD class="layer-link" colspan="2" align="center">
		<A href="javascript:<%=sForm%>.hide_layers.value='true';<%=sForm%>.p_list_name.value='<%=sListName%>';<%=sForm%>.<%=sListName%>.value='<%=sIdent%>';<%=sForm%>.p_list_type.value='<%=sType%>';enviar('<%=sSourceAction%>', <%=sForm%>);" title="<%=qvb.getMsg("item.response_label.ok.tip")%>" class="layer-link"><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("item.response_label.ok")%></A>
		&nbsp;|&nbsp;
		<A href="javascript:<%=sForm%>.p_source_action.value='';<%=sForm%>.p_source_width.value='';<%=sForm%>.p_source_height.value='';hide_source_layer();" title="<%=qvb.getMsg("item.response_label.cancel.tip")%>" class="layer-link"><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("item.response_label.cancel")%></A>
	</TD>
</TR>
</TABLE>
</DIV>

<SCRIPT>
<%if (sAction!=null && sAction.equalsIgnoreCase("p_source_action") && sSourceAction!=null && 
	( sSourceAction.equalsIgnoreCase(IQVListControlBean.A_ADD_LIST_OBJECT) ||
	  sSourceAction.equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT))){%>
	show_source_layer(<%=sForm%>);
<%}else{%>
	hide_source_layer();
<%}%>
document.p_image_preview.width=getProportionalWidth(<%=sWidth%>,<%=sHeight%>,150,150);document.p_image_preview.height=getProportionalHeight(<%=sWidth%>,<%=sHeight%>,150,150);
</SCRIPT>

