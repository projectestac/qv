<%@page contentType="text/html; charset=ISO-8859-1" import="edu.xtec.qv.editor.util.QVMaterialListControl"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
String sLayer = "matflashLayer";

boolean bDisplay = qvb.getBooleanParameter("display", false);
String sURI = qvb.getParameter(QVMaterialListControl.P_MATERIAL_URI);
String[] sImages = qvb.getAssessmentResources(qvb.FLASH_RESOURCE);
String sPath = qvb.getQuadernResourcesURL()+"/";
String sWidth = qvb.getParameter(QVMaterialListControl.P_MATERIAL_WIDTH, "");
String sHeight = qvb.getParameter(QVMaterialListControl.P_MATERIAL_HEIGHT, "");
%>
<script type="text/javascript">
function writeFlash(url){
	var divprev = document.getElementById('p_flash_preview');
	if (divprev!=null){
		var html ="<object codebase='http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0' classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' width='150' height='150'>";
		html+="<param name='movie' value='"+url+"'>";
		html+="<param value='high' name='quality'>";
		html+="<embed type='application/x-shockwave-flash' pluginspage='http://www.macromedia.com/go/getflashplayer' quality='high' width='150' height='150' src='"+url+"></embed>";
		html+="</object>";
		divprev.innerHTML=html;
	}
		

}
</script>
<DIV id='<%=sLayer%>' style="position:absolute; top:90; left:20; width:500; z-index:1000;display:none;visibility:hidden;">
<TABLE border='0' cellpadding='5' cellspacing='5' width='100%' >
<TR>
	<TD class="layer-text" valign="top" width="60"><%=qvb.getMsg("material.uri")%></TD>
	<TD valign="top" class="layer-form" >
<%
if (sImages!=null && sImages.length>0){ %>
					<SELECT name='<%=QVMaterialListControl.P_MATERIAL_FLASH_URI%>' class='layer-form' onchange="var url='<%=sPath%>'+this.value;writeFlash(url);">
						<OPTION value=""></OPTION>
<%	for(int i=0;i<sImages.length;i++){%>
						<OPTION value="<%=sImages[i]%>" <%=sImages[i].equals(sURI)?"selected":""%> ><%=sImages[i]%></OPTION>
<%}%>
					</SELECT>
<%} else {%>
					<%=qvb.getMsg("material.msg.noFlash")%>
<%}%>

  	</TD>
</TR>
<TR>
	<TD class="layer-text" ><%=qvb.getMsg("material.width")%></TD>
	<TD class="layer-form" >
		<INPUT type="text" class='layer-form' name="<%=QVMaterialListControl.P_MATERIAL_FLASH_WIDTH%>" size="5" value="<%=sWidth%>"/> <%=qvb.getMsg("pixels")%>
	</TD>
</TR>
<TR>
	<TD class="layer-text" ><%=qvb.getMsg("material.height")%></TD>
	<TD class="layer-form" >
		<INPUT type="text" class='layer-form' name="<%=QVMaterialListControl.P_MATERIAL_FLASH_HEIGHT%>" size="5" value="<%=sHeight%>"/> <%=qvb.getMsg("pixels")%>
	</TD>
</TR>
<TR>
	<TD colspan="2" align="center">
	<div id="p_flash_preview">
	</div>
	</TD>
</TR>
</TABLE>
</DIV>

<SCRIPT>
	<%if (bDisplay) {%>
		show_layer('<%=sLayer%>');
		writeFlash('<%=sPath+sURI%>');
	<%} else {%>
		hide_layer('<%=sLayer%>');
	<%}%>
</SCRIPT>
