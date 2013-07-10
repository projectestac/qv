<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.IQVPreferencesBean qvsb=(edu.xtec.qv.editor.beans.IQVPreferencesBean)qvb.getSpecificBean();

String sForm="preferencesForm";
String sLanguage = qvsb.getLanguage();
String sDefaultSkin = qvsb.getDefaultSkin();
if (sDefaultSkin==null) sDefaultSkin="default";
java.util.Enumeration enumSkins = qvsb.getSkins().elements();
%>	

<FORM name="<%=sForm%>" method="POST" action="index.jsp">
<INPUT type="hidden" name="page" value="index"/>
<INPUT type="hidden" name="action"/>
<INPUT type="hidden" name="id_quadern"/>
<INPUT type="hidden" name="preference"/>
<DIV id='preferencesLayer' style="position:absolute; top:150; left:350; width:500; z-index:1000; padding:5px; display:none;"  class='layer-box'>
<TABLE class='layer-box' border='0' cellpadding='5' cellspacing='5' width='450px'>
<TR>	
	<TD colspan="3" align="right" class="layer-title" ><%=qvb.getMsg("preference.title")%></TD>
</TR>
<TR>
  <TD width='30'></TD>
  <TD width="100%">
  	<TABLE border='0' cellpadding='5' cellspacing='5' width="100%">
  	<TR title="<%=qvb.getMsg("preference.language.tip")%>">
  		<TD class="layer-text" valign="top"><%=qvb.getMsg("preference.language")%></TD>
  		<TD valign="top">
  			<TABLE border='0' cellpadding='0' cellspacing='0'>
  			<TR>
		  		<TD valign="top">
		  			<SELECT name="lang" class='layer-form' >
		  				<OPTION value="ca" <%=sLanguage.equals("ca")?"selected":""%> ><%=qvb.getMsg("preference.language.ca")%></OPTION>
		  				<OPTION value="es" <%=sLanguage.equals("es")?"selected":""%> ><%=qvb.getMsg("preference.language.es")%></OPTION>
		  				<OPTION value="en" <%=sLanguage.equals("en")?"selected":""%> ><%=qvb.getMsg("preference.language.en")%></OPTION>
		  			</SELECT>
		  		</TD>
  			</TR>
  			</TABLE>
  		</TD>
  	</TR>
  	<TR title="<%=qvb.getMsg("preference.lookAndFeel.tip")%>">
  		<TD class="layer-text" valign="top"><%=qvb.getMsg("preference.lookAndFeel")%></TD>
  		<TD valign="top">
  			<TABLE border='0' cellpadding='0' cellspacing='0'>
  			<TR>
		  		<TD valign="top">
		  			<SELECT name="lookAndFeel" class='layer-form' >
<%while(enumSkins.hasMoreElements()){
	String sSkin = (String)enumSkins.nextElement();
%>	
		  				<OPTION value="<%=sSkin%>" <%=sSkin.equalsIgnoreCase(sDefaultSkin)?"selected":""%> ><%=qvb.getMsg("skin."+sSkin)%></OPTION>
<%}%>		  				
		  			</SELECT>
		  		</TD>
  			</TR>
  			</TABLE>
  		</TD>
  	</TR>
  	<TR>
  		<TD class="layer-text" valign="top"><%=qvb.getMsg("preference.options")%></TD>
		<INPUT type="hidden" name="autosave" class='layer-form' value="<%=qvsb.isAutosave()%>"/>
  		<TD valign="top">
  			<TABLE border='0' cellpadding='0' cellspacing='0'>
  			<!--TR title="<%=qvb.getMsg("preference.autosave.tip")%>">
		  		<TD valign="top">
		  			<INPUT type="checkbox" name="autosave" class='layer-form' <%=qvsb.isAutosave()?"checked":""%>/>
		  		</TD>
		  		<TD class='layer-form'><%=qvb.getMsg("preference.autosave")%></TD>
  			</TR-->
  			<!--TR title="<%=qvb.getMsg("preference.backup.tip")%>">
		  		<TD valign="top">
		  			<INPUT type="text" name="backup" class='layer-form' value='<%=qvsb.getNumBackups()%>' size="2" maxlength="2"/>
		  		</TD>
  			</TR-->
  			<TR title="<%=qvb.getMsg("preference.editXML.tip")%>">
		  		<TD valign="top">
		  			<INPUT type="checkbox" name="editXML" class='layer-form' <%=qvsb.isEditableXML()?"checked":""%>/>
		  		</TD>
		  		<TD class='layer-form'><%=qvb.getMsg("preference.editXML")%></TD>
  			</TR>
  			<TR title="<%=qvb.getMsg("preference.help.tip")%>">
		  		<TD valign="top">
		  			<INPUT type="checkbox" name="help" class='layer-form' <%=qvsb.showHelp()?"checked":""%>/>
		  		</TD>
		  		<TD class='layer-form'><%=qvb.getMsg("preference.help")%></TD>
  			</TR>
  			</TABLE>
  		</TD>
  	</TR>
  	</TABLE>
  </TD>
  <TD width='30' height='50%'></TD>
</TR>
<TR>	
	<TD class="layer-link" colspan="3" align="center">
		<A href="javascript:enviar('save_settings', this.document.<%=sForm%>);" class="layer-link"><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("preference.action.save")%></A>
		&nbsp;|&nbsp;
		<A href="javascript:document.<%=sForm%>.preference.value='hide';enviar('save_settings', this.document.<%=sForm%>);" class="layer-link"><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("preference.action.save_and_close")%></A>
		&nbsp;|&nbsp;
		<A href="javascript:hide_preferences_layer();" class="layer-link"><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("preference.action.close")%></A>
	</TD>
</TR>
</TABLE>
</DIV>
</FORM>

<SCRIPT>
<%if(qvb.getParameter("preference")!=null && !qvb.getParameter("preference").equalsIgnoreCase("hide")){%>
	show_preferences_layer();
<%}else{%>
	hide_preferences_layer();
<%}%>
</SCRIPT>


