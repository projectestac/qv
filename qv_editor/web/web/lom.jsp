<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.IQVLOMBean qvsb=(edu.xtec.qv.editor.beans.IQVLOMBean)qvb.getSpecificBean();

String sForm=qvb.getParameter("form");
String sCopyright = qvsb.getLOMCopyright();

%>	
<script language="JavaScript">
<!--
var form = <%=sForm%>;
document.write('<INPUT type="hidden" name="show">');
// -->
</script>
<SCRIPT src="scripts/move_layer.js"></SCRIPT>

<INPUT type="hidden" name="lom"/>
<%if("true".equals(qvb.getParameter("show_lom")) || (!qvsb.existsLOM() && !"save_lom".equals(request.getParameter("action")))){%>
<DIV id='lomLayer' style="position:absolute; top:50; left:350; width:530; z-index:1000; padding:5px; 2px solid; display:none;"  class='layer-box'>
<TABLE class='layer-box' border='0' cellpadding='5' cellspacing='5' width='550px'>
<TR>	
	<TD colspan=3">
		<TABLE border='0' cellpadding='0' cellspacing='0' style="width:100%">
		<TR>
			<TD valign="top" style="width:50px;background:url('imatges/move_layer_off.gif') no-repeat;" onmousedown="move('lomLayer');" title="<%=qvb.getMsg("layer.move.tip")%>">&nbsp;</TD>
			<TD align="right" class="layer-title"><%=qvb.getMsg("lom.title")%></TD>
		</TR>
		</TABLE>
	</TD>
</TR>
<TR>
  <TD width='30'></TD>
  <TD width="100%">
  	<TABLE border='0' cellpadding='0' cellspacing='5' width="100%">
  	<TR title="<%=qvb.getMsg("lom.autor.tip")%>">
  		<TD class="layer-text" valign="top"><%=qvb.getMsg("lom.autor")%></TD>
  		<TD valign="top">
  			<INPUT type="text" name="p_author" size="47" class='layer-form' value="<%=qvsb.getLOMAuthor()%>"/>
  		</TD>
  	</TR>
  	<TR title="<%=qvb.getMsg("lom.titol.tip")%>">
  		<TD class="layer-text" valign="top"><%=qvb.getMsg("lom.titol")%></TD>
  		<TD valign="top">
  			<INPUT type="text" name="p_title" size="47" class='layer-form' value="<%=qvsb.getLOMTitle()%>"/>
  		</TD>
  	</TR>
  	<TR title="<%=qvb.getMsg("lom.descripcio.tip")%>">
  		<TD class="layer-text" valign="top"><%=qvb.getMsg("lom.descripcio")%></TD>
  		<TD valign="top">
  			<TEXTAREA name="p_description" cols="45" rows="3" class='layer-form'><%=qvsb.getLOMDescription()%></TEXTAREA>
  		</TD>
  	</TR>
  	<TR title="<%=qvb.getMsg("lom.area.tip")%>">
  		<TD class="layer-text" valign="top"><%=qvb.getMsg("lom.area")%></TD>
  		<TD valign="top">
  			<SELECT class='layer-form' name="p_area">
  				<OPTION value=""></OPTION>
<%
			String sArea = qvsb.getLOMArea();
			java.util.Enumeration enumArees = qvsb.getArees().elements();
			while (enumArees.hasMoreElements()){
				edu.xtec.lom.Area oArea = (edu.xtec.lom.Area)enumArees.nextElement();
%>				
  				<OPTION value="<%=oArea.getKeyword()%>" <%=oArea.getKeyword().equals(sArea)?"selected":""%>><%=oArea.getText(qvb.getLanguage())%></OPTION>
<%
			}
%>  			
  			</SELECT>
  		</TD>
  	</TR>
  	<TR title="<%=qvb.getMsg("lom.idioma.tip")%>">
  		<TD class="layer-text" valign="top"><%=qvb.getMsg("lom.idioma")%></TD>
  		<TD valign="top">
  			<SELECT class='layer-form' name="p_language">
<%
			java.util.Enumeration enumIdiomes = qvsb.getIdiomes().elements();
			while (enumIdiomes.hasMoreElements()){
				edu.xtec.lom.Idioma oIdioma = (edu.xtec.lom.Idioma)enumIdiomes.nextElement();
%>				
  				<OPTION value="<%=oIdioma.getId()%>" <%=oIdioma.getId().equals(qvsb.getLOMLanguage())?"selected":""%> ><%=oIdioma.getText(qvb.getLanguage())%></OPTION>
<%
			}
%>  			
  			</SELECT>
  		</TD>
  	</TR>
  	<TR title="<%=qvb.getMsg("lom.nivell.tip")%>">
  		<TD class="layer-text" valign="top"><%=qvb.getMsg("lom.nivell")%></TD>
  		<TD valign="top">
  			<TABLE border="0">
  			<TR>
  				<TD class='layer-form' valign='top'>
<%
	java.util.Vector vNivells = qvsb.getNivells();
	java.util.Vector vLOMLevels = qvsb.getLOMEducationalLevels();
	for(int i=0;i<vNivells.size();i++){
		edu.xtec.lom.Nivell oNivell = (edu.xtec.lom.Nivell)vNivells.get(i);
%>  
		  			<INPUT name="p_level" type="checkbox" value="<%=oNivell.getId()%>" <%=vLOMLevels.contains(oNivell.getText("ca"))?"checked":""%>/><%=oNivell.getText(qvb.getLanguage())%><br>
<%
		if (i==(vNivells.size()/2)){
%>
				</TD>
  				<TD class='layer-form' valign='top'>
<%
		}
	} %>			
  				</TR>
  			</TR>
  			</TABLE>
  		</TD>
  	</TR>
  	<TR title="<%=qvb.getMsg("lom.llicencia.tip")%>">
  		<TD valign="top" class="layer-text"><%=qvb.getMsg("lom.llicencia")%></TD>
  		<TD valign="top" class='layer-form'>
  			<INPUT type="radio" name="p_copyright" value="yes" <%="yes".equals(sCopyright)?"checked":""%> onchange="if (!this.checked){this.form.p_license.disabled=true;}else{this.form.p_license.disabled=false;}" />Sí
  			<INPUT type="radio" name="p_copyright" value="no" <%="no".equals(sCopyright)?"checked":""%> onchange="if (this.checked){this.form.p_license.disabled=true;}else{this.form.p_license.disabled=false;}"/>No
  			<br>
  			<TEXTAREA name="p_license" wrap="off" style="font-size:10px;font-weight:normal" class="layer-form" cols="65" rows="6" <%="no".equals(sCopyright)?"disabled":""%> ><%=qvsb.getLOMLicense()%></TEXTAREA>
			<jsp:include page="help.jsp" flush="true">
					<jsp:param name="p_help_page" value="llicencia.html" />
			</jsp:include>
  		</TD>
  	</TR>
  	</TABLE>
  </TD>
<TR>	
	<TD class="layer-link" colspan="3" align="center">
		<A href="javascript:<%=sForm%>.lom.value='hide';enviar('save_lom', <%=sForm%>);" class="layer-link"><%=qvb.getMsg("lom.action.save_and_close")%></A>
		&nbsp;|&nbsp;
		<A href="javascript:hide_lom_layer();" class="layer-link"><%=qvb.getMsg("lom.action.close")%></A>
	</TD>
</TR>
</TABLE>
</DIV>
<SCRIPT>
	show_lom_layer();
</SCRIPT>
<%}%>

</FORM>

<!--SCRIPT>
<%if(!qvsb.existsLOM() && !"save_lom".equals(request.getParameter("action"))){%>
	show_lom_layer();
<%}%>
</SCRIPT-->


