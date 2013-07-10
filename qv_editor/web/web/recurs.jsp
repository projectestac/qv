<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" /><%
if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}

String sResourceType = qvb.getParameter("resource_type");
String sOpenCloseParamName = qvb.getParameter("open_close_param_name", "open_close_"+sResourceType);
String sOpenCloseParamValue = qvb.getParameter(sOpenCloseParamName);
boolean bOpen = sOpenCloseParamValue!=null && sOpenCloseParamValue.equalsIgnoreCase("open");
String[] files = qvb.getAssessmentResources(sResourceType);
String sOpenImgSrc = "imatges/open_menu-off.gif";
String sCloseImgSrc = "imatges/close_menu-off.gif";
String sImageSrc = sOpenImgSrc;
if (bOpen){
	sImageSrc = sCloseImgSrc;
}

if (!qvb.isHeaderLoaded()){
%>
<jsp:include page="header.jsp" flush="true" />
<%}%>

<INPUT type='hidden' name="<%=sOpenCloseParamName%>" value='<%=sOpenCloseParamValue%>'/>
<TABLE border='0' cellpadding='3' cellspacing='0'>
<!-- INICI RECURS: <%=sResourceType%> -->
<!--TR>
	<TD width='15'>
		<A href='#'  onclick="open_close_menu(document.delFitxerForm, '<%=sOpenCloseParamName%>');enviar('open_close_menu',document.delFitxerForm);" class='file-title'><IMG src='imatges/<%=sImageSrc%>' width='10' height='10' border='0'/></A>
	</TD>
	<TD width="100%" class='file-text'>
		<A href='#' onclick="open_close_menu(document.delFitxerForm, '<%=sOpenCloseParamName%>');enviar('open_close_menu',document.delFitxerForm);" class='file-title'><%=qvb.getMsg(sResourceType)%></A>
		(<%=files!=null?files.length:0%>)
	</TD>
</TR-->
<TR>
	<TD class="file-text">
		<A href='#' onclick="collapse('<%=sResourceType%>', document.img_<%=sResourceType%>, '<%=sOpenImgSrc%>', '<%=sCloseImgSrc%>');" class='file-title'><IMG src='<%=sImageSrc%>' name="img_<%=sResourceType%>" width='10' height='10' border='0'/>&nbsp;&nbsp;<%=qvb.getMsg(sResourceType)%></A>&nbsp;(<%=files!=null?files.length:0%>)
	</TD>
</TR>
<TR>
	<TD>
	<DIV id="<%=sResourceType%>" style="margin-left: 15; display: none">
	<TABLE border='0' cellpadding='3' cellspacing='0'>
<%
	for(int i=0;i<files.length;i++){
%>
	<TR>
		<TD width="92%" class='file-text'>
			<A href="<%=qvb.getQuadernResourcesURL()+"/"+files[i]%>" onClick="open_popup('<%=qvb.getQuadernResourcesURL()+"/"+files[i]%>', 'imatge', 425, 290); return false" class='file-text'><%=files[i]%></A>
		</TD>
		<TD>
			<A href='javascript:this.document.delFitxerForm.id_fitxer.value="<%=files[i]%>";enviar("del_fitxer",this.document.delFitxerForm)'><IMG src='imatges/delete_off.gif' onMouseOver='this.src="imatges/delete_on.gif"' onMouseOut='this.src="imatges/delete_off.gif"' border='0'/></A>
        </TD>
	</TR>
<%
	}
%>
	</TABLE>
	</TD>
</TR>
<!-- FI RECURSOS: <%=sResourceType%> -->
</TABLE>

<%if (!qvb.isHeaderLoaded()){
	qvb.loadHeader();
%>
<jsp:include page="footer.jsp" flush="true" />
<%}%>

