<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
String sActionURL = qvb.getSetting("qv.unzipServlet")+"path="+qvb.getUserLocalURL()+"&url="+request.getRequestURI()+"&maxsize="+qvb.getEspaiLliure();
%>

<script>
	function getFileName(form_file){
		path = form_file.value;
		filename = '';
		if (path!=null){
			if (path.lastIndexOf('.zip')<0){
				alert("<%=qvb.getMsg("unzip.error.noZip")%>");
			}else{
				if (path.indexOf('/')>=0){
					filename=path.substring(path.lastIndexOf('/')+1);
				}else if (path.indexOf('\\')>=0){
					filename=path.substring(path.lastIndexOf('\\')+1);
				}else{
					filename=path;
				}
				if (filename!=null){
					filename = filename.substring(0, filename.lastIndexOf('.zip'));
					if (filename.lastIndexOf('.qv')==filename.length-3) filename = filename.substring(0, filename.lastIndexOf('.qv'));
				}
			}
		}
		return filename;
	}
</script>
<FORM name="importForm" method="POST" action="<%=sActionURL%>" enctype="multipart/form-data">
<INPUT type="hidden" name="action"/>
<DIV id='importLayer' style="position:absolute; top:200; left:350; width:300; z-index:1000; padding:5px; display:none;"  class='layer-box'>
<TABLE border='0' cellpadding='3' cellspacing='0' width="400" class="layer-box">
<TR>
	<TD align="right" class="layer-title">
		<%=qvb.getMsg("import.title")%>
	</TD>
</TR>
<TR>
	<TD>
		<TABLE border='0' cellpadding='3' cellspacing='0'>
		<TR>
			<TD width="5%" class="layer-text"></TD>
			<TD width="5%" valign="top" class="layer-text"><B>1.</B></TD>
			<TD valign="top" class="layer-text"><%=qvb.getMsg("import.step1")%></TD>
		</TR>
		<TR>
			<TD colspan="2"/>
			<TD><INPUT type="file" name="browse" accept="application/x-zip-compressed" class="layer-form" onchange="this.form.fname.value=getFileName(this)"/></TD>
		</TR>
		<TR>
			<TD width="5%" class="layer-text"></TD>
			<TD width="5%" valign="top" class="layer-text"><B>2.</B></TD>
			<TD valign="top" class="layer-text"><%=qvb.getMsg("import.step2")%></TD>
		</TR>
		<TR>
			<TD colspan="2" class="layer-text"/>
			<TD><INPUT type="text" name="fname" class="layer-form" /></TD>
		</TR>
		</TABLE>
	</TD>
</TR>
<TR>
  	<TD align="center">
		<A href='javascript: enviar("import_quadern",document.importForm)' class="layer-link"><%=qvb.getMsg("import.name")%></A>
		&nbsp;&nbsp;
	  	<A href="#" onclick="hide_import_layer();" class="layer-link"><%=qvb.getMsg("close")%></A>
	</TD
</TR>
<TR>
	<TD height="10"/>
</TR>
</TABLE>
</DIV>
</FORM>
<SCRIPT>
	hide_import_layer();
</SCRIPT>

