head	1.2;
access;
symbols
	inicial:1.1.1.1 xtec:1.1.1;
locks; strict;
comment	@# @;


1.2
date	2004.06.03.11.24.29;	author sarjona;	state dead;
branches;
next	1.1;

1.1
date	2004.05.27.07.22.58;	author sarjona;	state Exp;
branches
	1.1.1.1;
next	;

1.1.1.1
date	2004.05.27.07.22.58;	author sarjona;	state Exp;
branches;
next	;


desc
@@


1.2
log
@Revisats import i preferences: afegits missatges d'idioma i posats en capa (enlloc de que s'obrin en una finestra diferent)
@
text
@<%@@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" /><%
if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.QVPreferenciesBean qvsb=(edu.xtec.qv.editor.beans.QVPreferenciesBean)qvb.getSpecificBean();

String sLanguage = qvsb.getLanguage();
if (!qvb.isHeaderLoaded()){
%>
<jsp:include page="header.jsp" flush="true" />
<%}%>	
<FORM name="configForm" method="POST" action="preferencies.jsp">
<INPUT type="hidden" name="page" value="index"/>
<INPUT type="hidden" name="action"/>
<INPUT type="hidden" name="id_quadern"/>
<TABLE class='quadreDades' border='0' cellpadding='5' cellspacing='5' width='100%'>
<TR>	
	<TD colspan="3" class="textValorDades" align="right">
		<A href="javascript:enviar('save_settings', this.document.configForm);" class="link"><IMG src="imatges/save_off.gif" border="0" width='15' height='15' /><span style="text-decoration:none">&nbsp;</span>Guardar preferències</A>
		&nbsp;|&nbsp;
		<A href="javascript:enviar('close_settings', this.document.configForm);window.close()" class="link"><IMG src="imatges/close_off.gif" border="0" width='15' height='15' /><span style="text-decoration:none">&nbsp;</span>Tancar </A>
	</TD>
</TR>
<TR>	
	<TD colspan="3" class="titolPreferencies" >Preferències</TD>
</TR>
<TR>
  <TD width='30'></TD>
  <TD width="100%">
  	<TABLE border='0' cellpadding='5' cellspacing='15' width="100%">
  	<TR>
  		<TD class="textNomPreferencia" valign="top">Idioma</TD>
  		<TD valign="top">
  			<TABLE border='0' cellpadding='0' cellspacing='0'>
  			<TR>
		  		<TD valign="top">
		  			<SELECT name="lang" class='textValorPreferencia' >
		  				<OPTION value="ca" <%=sLanguage.equals("ca")?"selected":""%> >Català</OPTION>
		  				<OPTION value="es" <%=sLanguage.equals("es")?"selected":""%> >Castellà</OPTION>
		  			</SELECT>
		  		</TD>
  			</TR>
  			</TABLE>
  		</TD>
  	</TR>
  	<TR>
  		<TD class="textNomPreferencia" valign="top">Autosave</TD>
  		<TD valign="top">
  			<TABLE border='0' cellpadding='0' cellspacing='0'>
  			<TR>
		  		<TD valign="top">
		  			<INPUT type="checkbox" name="autosave" class='textValorPreferencia' <%=qvsb.isAutosave()?"checked":""%>/>
		  		</TD>
		  		<TD class='textValorPreferencia'>Desar el quadern automàticament</TD>
  			</TR>
  			</TABLE>
  		</TD>
  	</TR>
  	<TR>
  		<TD class="textNomPreferencia" valign="top">Còpies de seguretat</TD>
  		<TD valign="top">
  			<TABLE border='0' cellpadding='0' cellspacing='0'>
  			<TR>
		  		<TD valign="top">
		  			<INPUT type="text" name="backup" class='textValorPreferencia' value='<%=qvsb.getNumBackups()%>' size="2" maxlength="2"/>
		  		</TD>
  			</TR>
  			</TABLE>
  		</TD>
  	</TR>
  	<TR>
  		<TD class="textNomPreferencia" valign="top" width="30%">Opcions avançades</TD>
  		<TD valign="top">
  			<TABLE border='0' cellpadding='0' cellspacing='0'>
  			<TR>
		  		<TD valign="top">
		  			<INPUT type="checkbox" name="editXML" class='textValorPreferencia' <%=qvsb.isEditableXML()?"checked":""%>/>
		  		</TD>
		  		<TD class='textValorPreferencia'>Editar les preguntes amb XML</TD>
  			</TR>
  			</TABLE>
  		</TD>
  	</TR>
  	</TABLE>
  </TD>
  <TD width='30' height='50%'></TD>
</TR>
</TABLE>
</FORM>
<%if (!qvb.isHeaderLoaded()){
	qvb.loadHeader();
%>
<jsp:include page="footer.jsp" flush="true" />
<%}%>

@


1.1
log
@Initial revision
@
text
@@


1.1.1.1
log
@Editor de QV.
S'ha separat del projecte qv
@
text
@@
