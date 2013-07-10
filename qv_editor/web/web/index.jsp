<%@page contentType="text/html; charset=ISO-8859-1" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" /><%
if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.QVMainBean qvsb=(edu.xtec.qv.editor.beans.QVMainBean)qvb.getSpecificBean();

if (!qvb.isHeaderLoaded()){
%>
<jsp:include page="header.jsp" flush="true" />
<%}%>	

<jsp:include page="zipFolder.jsp" flush="true" />
<jsp:include page="preferences.jsp" flush="true" />
<jsp:include page="import.jsp" flush="true" />

<script>var xinha_editors = null;</script>
<FORM name="indexForm" method="POST" action="index.jsp">
<INPUT type="hidden" name="page" value="index"/>
<INPUT type="hidden" name="action"/>
<INPUT type="hidden" name="id_quadern"/>
<INPUT type="hidden" name="edition_mode" value="<%=qvb.VISUAL_EDITION_MODE%>"/>
<CENTER>
<%
String sMsg = qvb.getMsg("qv_editor.alert");
if (!"qv_editor.alert".equals(sMsg)) {%>
	<div id="avis" class='layer-box' style="padding:5px;width:60%">
		<div class="layer-text"><%=sMsg%></div>
	</div>
	<br/>
<%} %>

<TABLE class='index-box' border='0' cellpadding='5' cellspacing='0' width='60%'>
<TR>
	<TD colspan="3" align="right" class="index-title"><BR/><%=qvb.getMsg("index.title")%>&nbsp;&nbsp;&nbsp;</TD>
</TR>
<TR>
  <TD>&nbsp;</TD>
  <TD width='100%' align="center">
    <TABLE border='0' cellpadding='5' cellspacing='0'>
<%
String[] quaderns = qvsb.getQuadernsUser();
if (quaderns!=null){
	for(int i=0;i<quaderns.length;i++){
%>
	<TR>
		<TD class='index-form' align="left">
			<A onMouseOver="this.style.cssText='color:#991C1F';window.status='';return true;" onMouseOut="this.style.cssText='color:#004080';" href='javascript:this.document.indexForm.id_quadern.value="<%=quaderns[i]%>";enviar("set_quadern",this.document.indexForm)' class="index-link" title="<%=qvb.getMsg("index.action.set.tip")%>"><%=quaderns[i]%></A>
		</TD>
		<TD>
			<A onMouseOver="window.status='';return true;" href="#" onclick="open_popup('<%=qvb.getPreviewURL(quaderns[i])%>','Preview','670','500');"><IMG src='imatges/preview_off.gif' title="<%=qvb.getMsg("index.action.preview.tip")%>" onMouseOver='this.src="imatges/preview_on.gif"' onMouseOut='this.src="imatges/preview_off.gif"' border='0' height="16"/></A>
        </TD>
		<TD>
			<A onMouseOver="window.status='';return true;" href='javascript:this.document.indexForm.id_quadern.value="<%=quaderns[i]%>";enviar("del_quadern",this.document.indexForm)'><IMG src='imatges/del_quadern_off.gif' title="<%=qvb.getMsg("index.action.del.tip")%>" onMouseOver='this.src="imatges/del_quadern_on.gif"' onMouseOut='this.src="imatges/del_quadern_off.gif"' border='0'/></A>
        </TD>
		<TD>
			<A onMouseOver="window.status='';return true;" href='javascript:this.document.indexForm.id_quadern.value="<%=quaderns[i]%>";enviar("export_quadern",this.document.indexForm)' ><IMG src='imatges/export_off.gif' title="<%=qvb.getMsg("index.action.import.tip")%>" onMouseOver='this.src="imatges/export_on.gif"' onMouseOut='this.src="imatges/export_off.gif"' border='0' width="13" height="13"/></A>
        </TD>
        <!-- TD>&nbsp;&nbsp;&nbsp;</TD>
        <TD>
			<A href="#" onclick="open_popup('../../qv_admin/preview.jsp?p_select_qv=false&p_username=<%=qvb.getUserId()%>&p_quadern=<%=quaderns[i]%>&p_skin=<%=qvb.getDefaultSkin()%>','HTML','700','500');" class="index-link" title="Generador de pàgines web">HTML</A>        
        </TD-->
	</TR>
<%
	}
}
%>
	<TR>
		<TD colspan="3">
			<TABLE border='0' cellpadding='0' cellspacing='0'>
			<TR>
				<TD>
					<INPUT type="text" name="nou_quadern" title="<%=qvb.getMsg("index.assessment_name.tip")%>" />&nbsp;
					<A href='javascript:if (document.indexForm.nou_quadern.value!="")enviar("add_quadern",this.document.indexForm)' class="index-link" title="<%=qvb.getMsg("index.action.add.tip")%>"><%=qvb.getMsg("index.action.add")%></A>&nbsp;&nbsp;
				</TD>
			</TR>
			<TR>
				<TD >
				    <TABLE border='0' cellpadding='3' cellspacing='0' width='100%'>
				    <TR>
				    	<TD height="20"/>
				    </TR>
				    <TR>
				      <TD width='200'>
				        <TABLE cellpadding="0" border="0" cellspacing="0" style="background-color:white;border: #104a7b 1px solid; padding:1px; padding-right: 0px; padding-left: 0px;" width='100%'>
				        <TR>
					        <TD style="height:5px; width:<%=qvb.getPercentatgeOcupat()-qvb.getPercentatgeOcupatQuadern()%>%; font-size:1px; background-color:#004080" />
					        <TD style="height:5px; width:<%=100-qvb.getPercentatgeOcupat()%>%; font-size:1px; background-color:#FFFFFF" />
				        </TR>
				        </TABLE>
				      </TD>
				      <TD class='index-text' title="<%=qvb.getMsg("index.non_available_space.tip")%>"><B><%=qvb.getPercentatgeOcupat()%>% <%=qvb.getMsg("index.non_available_space")%></B> </TD>
				    </TR>
				    <TR>
				    	<TD align="center" class="index-text" title="<%=qvb.getMsg("index.available_space.tip")%>" ><%=qvb.getMsg("index.available_space")%> <%=qvb.toMB(qvb.getEspaiLliure())%>Mb /<%=qvb.toMB(qvb.getMaxEspai())%>Mb</TD>
				    </TR>
				    </TABLE>
				</TD>
			</TR>
			</TABLE>
		</TD>
	</TR>
	</TABLE>
  </TD>
  <TD></TD>
</TR>
<TR>
  <TD height='50%' width='50'></TD>
  <TD>&nbsp;</TD>
  <TD height='50%' width='50'></TD>
</TR>
<TR>
  <TD height='50%' width='50'></TD>
  <TD>
  	<TABLE border='0' cellpadding='5' cellspacing='0'>
  	<TR>
  		<TD><A href="#" onclick="show_preferences_layer();" title="<%=qvb.getMsg("preference.tip")%>" class="index-link"><%=qvb.getMsg("preference.name")%></A></TD>
  		<TD><A href="#" onclick="show_import_layer();" title="<%=qvb.getMsg("import.tip")%>" class="index-link"><%=qvb.getMsg("import.name")%></A></TD>
  		<TD><A href="javascript:enviar('logout',this.document.indexForm)" title="<%=qvb.getMsg("logout.tip")%>" class="index-link"><%=qvb.getMsg("logout.name")%></A></TD>
  	</TR>
  	</TABLE>
  </TD>
  <TD height='50%' width='50'></TD>
</TR>
<TR>
  <TD height='50%' width='50'></TD>
  <TD>
	<TABLE border='0' cellpadding='3' cellspacing='0' width="100%">
	<TR>
	  	<TD class="index-text" align="right" style="font-size:9" title="qv@xtec.cat" ><%=qvb.getMsg("msg.index.contact_info")%> <A href="mailto:qv@xtec.cat" class="index-link" style="font-size:11;font-weight:bold;text-decoration:none;">qv@xtec.cat</A></TD
	</TR>
	</TABLE>
  </TD>
  <TD height='50%' width='50'></TD>
</TR>
</TABLE>
</CENTER>
</FORM>
<%if (!qvb.isHeaderLoaded()){
	qvb.loadHeader();
%>
<jsp:include page="footer.jsp" flush="true" />
<%}%>

