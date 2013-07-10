<%@page contentType="text/html; charset=ISO-8859-1" import="edu.xtec.qv.editor.util.QVMaterialListControl"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
String sLayer = "matimageLayer";

boolean bDisplay = qvb.getBooleanParameter("display", false);
String sURI = qvb.getParameter(QVMaterialListControl.P_MATERIAL_URI);
String[] sImages = qvb.getAssessmentResources(qvb.IMAGE_RESOURCE);
String sPath = qvb.getQuadernResourcesURL()+"/";

String sNullImage = "imatges/pixel.gif";
String sWidth = qvb.getParameter(QVMaterialListControl.P_MATERIAL_WIDTH, "");
String sHeight = qvb.getParameter(QVMaterialListControl.P_MATERIAL_HEIGHT, "");
%>

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
<DIV id='<%=sLayer%>' style="position:absolute; top:90; left:20; width:500; z-index:1000;display:none;visibility:hidden;">
<TABLE border='0' cellpadding='5' cellspacing='5' width='100%' >
<TR>
	<TD class="layer-text" valign="top" width="60"><%=qvb.getMsg("material.uri")%></TD>
	<TD valign="top" class="layer-form" >
<%
if (sImages!=null && sImages.length>0){ %>
					<SELECT name='<%=QVMaterialListControl.P_MATERIAL_URI%>' onChange='var img=eval(new String("image_"+this.value).replace(/\./g, "_"));document.p_image_preview.src=img.src;document.p_image_preview.width=getProportionalWidth(img.width,img.height,150,150);document.p_image_preview.height=getProportionalHeight(img.width,img.height,150,150);this.form.<%=QVMaterialListControl.P_MATERIAL_WIDTH%>.value=img.width;this.form.<%=QVMaterialListControl.P_MATERIAL_HEIGHT%>.value=img.height' class='layer-form' >
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
	<TD class="layer-text" ><%=qvb.getMsg("material.width")%></TD>
	<TD class="layer-form" >
		<INPUT type="text" class='layer-form' name="<%=QVMaterialListControl.P_MATERIAL_WIDTH%>" size="5" value="<%=sWidth%>"/> <%=qvb.getMsg("pixels")%>
	</TD>
</TR>
<TR>
	<TD class="layer-text" ><%=qvb.getMsg("material.height")%></TD>
	<TD class="layer-form" >
		<INPUT type="text" class='layer-form' name="<%=QVMaterialListControl.P_MATERIAL_HEIGHT%>" size="5" value="<%=sHeight%>"/> <%=qvb.getMsg("pixels")%>
	</TD>
</TR>
<TR>
	<TD colspan="2" align="center">
		<IMG name="p_image_preview" src="<%=((sURI==null || sURI.length()<=0) && sImages!=null && sImages.length>0)?sNullImage:sPath+sURI%>" width="100" height="100"/>
	</TD>
</TR>
</TABLE>
</DIV>

<SCRIPT>
	<%if (bDisplay) {%>
		show_layer('<%=sLayer%>');
	<%} else {%>
		hide_layer('<%=sLayer%>');
	<%}%>
</SCRIPT>
