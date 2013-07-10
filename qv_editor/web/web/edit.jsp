<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" import="java.util.*,edu.xtec.qv.qti.QTIObject"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
qvb.loadHeader();
%>

<jsp:include page="header.jsp" flush="true" />

<!-- xinha -->
<script type='text/javascript'>
<% String sBase = qvb.getSetting("wysiwyg.base_url"); //"../../components/xinha/";%>
<% String sImg="{ \'./\' : \'"+qvb.getSetting("wysiwyg.replace_img")+qvb.getUserId()+"/"+qvb.getIdQuadern()+"/\' }"; %>
  var _editor_url = "<%=sBase%>";
  var _editor_lang = "<%=qvb.getLanguage()%>";
  var xinha_editors = null;
  var xinha_init = null; 
  var xinha_config = null; 
  var xinha_plugins = null;
  var specialReplacements = <%=sImg%>;
</script>
<script language='javascript' type='text/javascript' src='<%=sBase%>htmlarea.js'></script>
<script language='javascript' type='text/javascript' src='scripts/htmleditor.js'></script>
<!-- /xinha -->



<%if (qvb.getParameter("action")!=null && qvb.getParameter("action").equalsIgnoreCase("preview")){%>
<SCRIPT>
<!--
	//open_popup('<%=qvb.getPreviewURL()%>','Preview','670','500');
-->
</SCRIPT>
<%}%>

<FORM name="editForm" method="POST" action="edit.jsp">
<INPUT type="hidden" name="page" value="<%=qvb.getIncludePage()%>"/>
<INPUT type="hidden" name="action"/>
<INPUT type="hidden" name="id_quadern"/>
<INPUT type="hidden" name="ident_full"/>
<INPUT type="hidden" name="ident_pregunta"/>
<INPUT type="hidden" name="form" />
<INPUT type="hidden" name="edition_mode" value="<%=qvb.getEditionMode()%>"/>
</FORM>
<TABLE border='0' cellpadding='0' cellspacing='0' width='100%' height="100%" style='height: 100%'>
<!-- INICI capçalera -->
<TR>
	<TD >
		<TABLE border='0' cellpadding='0' cellspacing='0' width='100%'>
		<TR>
			<TD valign='bottom' >
				<IMG src="imatges/logo.gif"/>
			</TD>
			<TD valign='bottom' align="right">
				<TABLE border='0' cellpadding='0' cellspacing='0'>
				<TR>
					<TD valign='bottom' align='center' width="60" title="<%=qvb.getMsg("action.preview.tip")%>">
						<!-- A href="javascript:enviar('preview', document.forms[document.editForm.form.value]);" class="link"-->
						<A href="#" onclick="open_popup('<%=qvb.getPreviewURL()%>','Preview','670','500');return false;" class="link">
							<IMG src="imatges/preview_assessment_off.gif" height="17"  border="0"/><BR/>
							<%=qvb.getMsg("action.preview")%><BR><IMG src="imatges/pixel.gif" width="1" border="0"/>
						</A>
					</TD>
					<%if (qvb.isEditableXML()){%>
					<TD style="padding-left:10" valign='bottom' align='center' width="60" title="<%=qvb.getMsg("action.edition_mode.tip")%>">
						<A href="javascript:if (set_edition_mode(document.forms[document.editForm.form.value])){enviar('editXML', document.forms[document.editForm.form.value]);}" class="link">
							<IMG src="imatges/edition_mode_off.gif" width="20"  border="0"/><BR/>
							<%=qvb.getEditionMode().equalsIgnoreCase(qvb.TEXT_EDITION_MODE)?qvb.getMsg("action.edition_mode.visual"):qvb.getMsg("action.edition_mode.text")%><BR/><IMG src="imatges/pixel.gif" width="1" border="0"/>
						</A>
					</TD>
					<%}%>
					<TD style="padding-left:10" valign='bottom' align='center' width="60" title="<%=qvb.getMsg("action.save.tip")%>">
						<A href="javascript:enviar('save_quadern', document.forms[document.editForm.form.value]);" class="link">
							<IMG src="imatges/save_off.gif" width="14" height="16" border="0"/><BR/>
							<%=qvb.getMsg("action.save")%><BR/><IMG src="imatges/pixel.gif" width="1" border="0"/>
						</A>
					</TD>
					<TD style="padding-left:10;padding-right:20" valign='bottom' align='center' width="60" title="<%=qvb.getMsg("action.home.tip")%>">
						<A href='javascript:redirectToHome()' class="link">
							<IMG src="imatges/home_off.gif" width="20" height="20" border="0"/><BR/>
							<%=qvb.getMsg("action.home")%><BR/><IMG src="imatges/pixel.gif" width="1" border="0"/>
						</A>
					</TD>
				</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD height='10'/>
		</TR>
		</TABLE>
	</TD>
</TR>
<!-- FI capçalera -->
<TR>
	<TD valign='top' height="100%">
		<TABLE border='0' cellpadding='0' cellspacing='0' width='100%' height="100%" style="height: 100%;">
		<TR>
			<TD width='150' valign='top' class='edit-background'>
<!-- INICI menu esquerra -->			
				<TABLE width='150' cellpadding="0" cellspacing="0" border="0">
				<!-- INICI Quadern -->
				<TR>
<%
String sMenuQuadernClass = "menu-assessment";
if (qvb.getIdFull()==null || qvb.getIdFull().length()<=0){
	sMenuQuadernClass = "menu-assessment-selected";
}

%>
					<TD class='<%=sMenuQuadernClass%>' height='30'>
						&nbsp;<A title="<%=qvb.getMsg("assessment")%>: <%=qvb.getTitle(qvb.getQuadern())%>" onMouseOver="window.status='';return true;" href='javascript:document.editForm.id_quadern.value="<%=qvb.getIdQuadern()%>";enviar("set_quadern",this.document.editForm)' class='<%=sMenuQuadernClass%>'><%=qvb.getShowName(qvb.getQuadern(),10)%></A>
					</TD>
				</TR>
				<!-- FI Quadern -->
<!-- INICI llista de FULLS -->
<%
Vector vFulls = qvb.getFullsQuadern();
if (vFulls!=null && !vFulls.isEmpty()){
	int iFull=1;
	Enumeration enumFulls = vFulls.elements();
	while (enumFulls.hasMoreElements()){
		QTIObject oFull = (QTIObject)enumFulls.nextElement();
		String sMenuFullClass = "menu-section";
		if (qvb.getIdPregunta()==null && oFull.getIdent().equals(qvb.getIdFull())){
			sMenuFullClass = "menu-section-selected";
		}
%>
				<TR>
					<TD class='<%=sMenuFullClass%>' height='20'>
						&nbsp;&nbsp;&nbsp;<A title='<%=qvb.getMsg("section")%>: <%=qvb.getTitle(oFull)%>' onMouseOver="window.status=''; return true;" href='javascript:document.editForm.ident_full.value="<%=oFull.getIdent()%>";enviar("set_full",this.document.editForm)' class='<%=sMenuFullClass%>'><%=qvb.getShowName(oFull)!=null?qvb.getShowName(oFull,15):qvb.getMsg("section")+" "+iFull%></A>
					</TD>
				</TR>
<!-- INICI llista de PREGUNTES -->
<%
Vector vPreguntes = oFull.getAttributeVectorValue(QTIObject.ITEM);
if (vPreguntes!=null && !vPreguntes.isEmpty()){
%>
				<TR>
					<TD>
						<TABLE width="100%" cellpadding="0" cellspacing="0" border="0">
<%
	int iPregunta=1;
	Enumeration enumPreguntes = vPreguntes.elements();
	while (enumPreguntes.hasMoreElements()){
		QTIObject oPregunta = (QTIObject)enumPreguntes.nextElement();
		String sMenuPreguntaClass = "menu-item";
		if (oFull.getIdent().equals(qvb.getIdFull()) && oPregunta.getIdent().equals(qvb.getIdPregunta())){
			sMenuPreguntaClass = "menu-item-selected";
		}
%>
						<TR>
							<TD height='20' class='<%=sMenuPreguntaClass%>'>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<A title='<%=qvb.getMsg("item")%>: <%=qvb.getTitle(oPregunta)%>' onMouseOver="window.status=''; return true;" href='javascript:document.editForm.ident_full.value="<%=oFull.getIdent()%>";document.editForm.ident_pregunta.value="<%=oPregunta.getIdent()%>";enviar("set_pregunta",this.document.editForm)' class='<%=sMenuPreguntaClass%>'><%=qvb.getShowName(oPregunta)!=null?qvb.getShowName(oPregunta,12):qvb.getMsg("item")+" "+iPregunta%></A>
							</TD>
						</TR>
						<TR>
							<TD height='1' class='menu-section'></TD>
						</TR>
<%
		iPregunta++;
	}
%>		
						</TABLE>
					</TD>
				</TR>
<%}%>
<!-- FI llista de PREGUNTES -->
<%
		iFull++;
	}
%>
<!-- FI llista de FULLS -->
<%}%>			
				<TR>
					<TD height="27">&nbsp;</TD>
				</TR>				
				</TABLE>
<!-- FI menu esquerra -->			
			</TD>

<!--/FORM-->
			<TD width='5'>&nbsp;</TD>
<!-- INICI contingut  -->
			<TD valign='top' height='100%'>
				<TABLE width="100%" cellpadding="0" cellspacing="0" border="0" height="100%" style='height:100%;'>
				<TR>
					<TD height='1' width='15'></TD>
					<TD></TD>
					<TD width='15'></TD>
				</TR>
				<TR>
					<TD></TD>
					<TD valign="top">
<% if(qvb.getIncludePage()!=null){
String sInclude = qvb.getIncludePage()+".jsp";
%>
<jsp:include page="<%=sInclude%>" flush="true" />
<% }else{ %>
ERROR
<% } %>
					</TD>
					<TD heigth='1'></TD>
				</TR>			
				</TABLE>
			</TD>
		</TR>		
		</TABLE>
	</TD>
<!-- FI contingut  -->
</TR>
<TR>
	<TD height="15"/>
</TR>
</TABLE>


<!-- INICI menu flotant -->
<script>
if (!document.layers)
document.write('<div id="divStayTopLeft" style="position:absolute">')
</script> 

<layer id="divStayTopLeft">
<FORM name="menuForm" method="POST" action="edit.jsp">
<INPUT type="hidden" name="page" value="<%=qvb.getIncludePage()%>"/>
<INPUT type="hidden" name="action"/>
<TABLE border="0" width="148" cellspacing="3" cellpadding="0" class='edit-box'>
 <TR>
 	<TD align="center"><A href='javascript:enviar("add_full",this.document.menuForm)'><IMG src='imatges/addFull_off.gif' title='<%=qvb.getMsg("add_full_button")%>' border='0' width='20' height='20' /></A></TD>
	<TD align="center"><A href='javascript:enviar("add_pregunta",this.document.menuForm)'><IMG src='imatges/addPregunta_off.gif' title='<%=qvb.getMsg("add_pregunta_button")%>' border='0' width='20' height='20' /></A></TD>
	<TD align="center"><A href='javascript:enviar("del",this.document.menuForm)'><IMG src='imatges/del_off.gif' title='<%=qvb.getIdPregunta()!=null?qvb.getMsg("del_pregunta_button"):qvb.getMsg("del_full_button")%>' border='0' width='20' height='20' /></A></TD>
	<TD align="center"><A href='javascript:enviar("up",this.document.menuForm)'><IMG src='imatges/up_off.gif' title='<%=qvb.getMsg("up_button")%>' border='0' width='20' height='20' /></A></TD>
	<TD align="center"><A href='javascript:enviar("down",this.document.menuForm)'><IMG src='imatges/down_off.gif' title='<%=qvb.getMsg("down_button")%>' border='0' width='20' height='20' /></A></TD>
 </TR>	
</TABLE>
</FORM>
</layer>

<script type="text/javascript">
if (!document.layers)
document.write('</div>')

JSFX_FloatDiv("divStayTopLeft", 10, -35).floatIt();
//JSFX_FloatTopDiv();
</script>
<!-- FI menu flotant -->


<jsp:include page="footer.jsp" flush="true" />
