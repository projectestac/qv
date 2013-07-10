<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" /><%
if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.QVQuadernBean qvsb=(edu.xtec.qv.editor.beans.QVQuadernBean)qvb.getSpecificBean();
if (!qvb.isHeaderLoaded()){
%>
<jsp:include page="header.jsp" flush="true" />
<%}%>
<%if (qvsb.isErrorUploading()){%>
<DIV id='errorUpload' style="position:absolute; top:250; left:300; width:400; z-index:1000; padding:5px; 2px solid;"  class='error-box'>
<TABLE border='0' cellpadding='3' cellspacing='0'>
<TR>
	<TD valign="top">
		<IMG src="imatges/alert.gif"/>
	</TD>
  	<TD class="error-text">
  		<%=qvb.getMsg(qvsb.getErrorUploading())%>
	  	<BR><p/>
	</TD
</TR>
<TR>
  	<TD colspan="2" class="error-text" align="center">
	  	<A href="#" onclick="set_layer_visibility('errorUpload','hidden');" class="error-text"><U><B><%=qvb.getMsg("close")%></B></U></A>
	</TD
</TR>
</TABLE>
</DIV>
<SCRIPT>
	center_layer('errorUpload',400,100);
</SCRIPT>

<%}%>

<TABLE width="100%" cellpadding="0" cellspacing="0" border="0" style='height: 100%;'>
<TR>
	<TD valign='top' class="edit-box">
		<!-- INICI Dades del quadern -->
		<FORM name="quadernForm" method="POST" action="edit.jsp">
		<INPUT type="hidden" name="page" value="quadern"/>
		<INPUT type="hidden" name="action"/>
		<INPUT type="hidden" name="hide_layers" />
		<SCRIPT language="javascript">
			setCurrentForm("quadernForm");
		</SCRIPT>
		<jsp:include page="enunciat.jsp" flush="true">
			<jsp:param name="form" value="document.quadernForm" />
			<jsp:param name="title_pregunta" value='<%=qvsb.getTitle()%>' /> 
			<jsp:param name="scoremodel" value='<%=qvsb.getScoreModel()%>' />
			<jsp:param name="p_show_url" value="true" />
		</jsp:include>
		<!-- FI Dades del quadern -->
	</TD>
</TR>
		</FORM>
<TR>
	<TD height='10'>&nbsp;</TD>
</TR>
<TR>
	<TD height='100%'>
		<!-- INICI Fitxers -->
	    <TABLE class='file-box' border='0' cellpadding='5' cellspacing='0' width='100%' style='height: 100%;'>
	    <TR>
	      <TD width='50'></TD>
	      <TD>&nbsp;</TD>
	      <TD width='50'></TD>
	    </TR>
	    <TR>
	      <TD>&nbsp;</TD>
	      <TD valign='top'>
			<FORM name="delFitxerForm" method="POST" action="edit.jsp">
			<INPUT type="hidden" name="page" value="quadern"/>
			<INPUT type="hidden" name="action"/>
			<INPUT type="hidden" name="id_fitxer"/>
	        <TABLE border='0' cellpadding='3' cellspacing='0'>
	        <TR>
	        	<TD colspan="2">
					<jsp:include page="recurs.jsp" flush="true">
						<jsp:param name="resource_type" value="<%=qvb.IMAGE_RESOURCE%>" />
					</jsp:include>
				</TD>
			</TR>
	        <TR>
	        	<TD colspan="2">
					<jsp:include page="recurs.jsp" flush="true">
						<jsp:param name="resource_type" value="<%=qvb.AUDIO_RESOURCE%>" />
					</jsp:include>
				</TD>
			</TR>
	        <TR>
	        	<TD colspan="2">
					<jsp:include page="recurs.jsp" flush="true">
						<jsp:param name="resource_type" value="<%=qvb.VIDEO_RESOURCE%>" />
					</jsp:include>
				</TD>
			</TR>
	        <TR>
	        	<TD colspan="2">
					<jsp:include page="recurs.jsp" flush="true">
						<jsp:param name="resource_type" value="<%=qvb.FLASH_RESOURCE%>" />
					</jsp:include>
				</TD>
			</TR>
			<TR>
	        	<TD colspan="2">
					<jsp:include page="recurs.jsp" flush="true">
						<jsp:param name="resource_type" value="<%=qvb.APPLICATION_RESOURCE%>" />
					</jsp:include>
				</TD>
			</TR>
	        <TR>
	        	<TD colspan="2">
					<jsp:include page="recurs.jsp" flush="true">
						<jsp:param name="resource_type" value="<%=qvb.REST_RESOURCE%>" />
					</jsp:include>
				</TD>
			</TR>
			</FORM>
	        <TR>
	          <TD colspan='2' height='20'>
	          </TD>
	        </TR>
	        <TR>
	          <TD colspan='2'>
				<FORM name="uploadForm" method="POST" action="edit.jsp" enctype="multipart/form-data">
				<INPUT type="hidden" name="page" value="quadern"/>
				<INPUT type="hidden" name="action"/>
		            <INPUT type="file" name="fitxer" class="file-text"/>
		            <BR/>
					<A href='javascript:enviar("add_fitxer",this.document.uploadForm)' class="file-text" onMouseOver='document.upload_img.src="imatges/upload_on.gif"' onMouseOut='document.upload_img.src="imatges/upload_off.gif"' title='<%=qvb.getMsg("add_fitxer_button")%>'><IMG name="upload_img" src='imatges/upload_off.gif' alt='<%=qvb.getMsg("add_fitxer_button")%>' border='0'/><SPAN style="text-decoration:none">&nbsp;&nbsp;</SPAN><%=qvb.getMsg("add_fitxer_button")%></A>
				</FORM>
	          </TD>
	        </TR>
	        <TR>
	          <TD colspan='2'>
	            <TABLE border='0' cellpadding='3' cellspacing='0' width='100%'>
	            <TR>
	              <TD width='200'>
	                <table cellpadding="0" border="0" cellspacing="0" style="background-color:white;border: #104a7b 1px solid; padding:1px; padding-right: 0px; padding-left: 0px;" width='100%'>
	                <tr>
	                <td style="height:5px; width:<%=qvb.getPercentatgeOcupatQuadern()%>%; font-size:1px; background-color:#006633" />
	                <td style="height:5px; width:<%=qvb.getPercentatgeOcupat()-qvb.getPercentatgeOcupatQuadern()%>%; font-size:1px; background-color:#76a769" />
	                <td style="height:5px; width:<%=100-qvb.getPercentatgeOcupat()%>%; font-size:1px; background-color:#FFFFFF" />
	                </tr>
	                </table>
	              </TD>
	              <TD class='file-text'><%=qvb.getPercentatgeOcupat()%>% ocupat</TD>
	            </TR>
	            <TR>
	            	<TD colspan='2' class='file-text'>
	            		<SPAN style="font-weight: normal"><%=qvb.getMsg("index.available_space")%>: </SPAN> <%=qvb.toMB(qvb.getEspaiLliure())%>Mb /<%=qvb.toMB(qvb.getMaxEspai())%>Mb
	            	</TD>
	            </TR>
	            </TABLE>
	          </TD>
	        </TR>
	        </TABLE>
	      </TD>
	      <TD>&nbsp;</TD>
	    </TR>
	    <TR>
	      <TD height='50%' width='50'></TD>
	      <TD>&nbsp;</TD>
	      <TD width='50'></TD>
	    </TR>
	    </TABLE>  
		<!-- FI Fitxers -->
	</TD>
</TR>
</TABLE>
<%if (!qvb.isHeaderLoaded()){
	qvb.loadHeader();
%>
<jsp:include page="footer.jsp" flush="true" />
<%}%>

