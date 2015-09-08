<%@page contentType="text/html; charset=ISO-8859-1"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.admin.beans.QVBiblioManagementBean" scope="request" /><%
if(!qvb.init(request, session, response)){%>
<%}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<title>Administració de la biblioteca de Quaderns Virtuals</title>
	<LINK TYPE="text/css" href="qv_html.css" rel="stylesheet" />
	<LINK rel="icon" type="image/x-icon" href="http://clic.xtec.net/qv_web/image/favicon.ico"/>
    <LINK rel="shortcut icon" type="image/x-icon"  href="http://clic.xtec.net/qv_web/image/favicon.ico"/>
</head>

<body>
<form name="qv_form" method="post" action="act_bd.jsp" target="_self">

<h1 style="text-align:right" class="title">Administració de la biblioteca de Quaderns Virtuals</h1>
<div style="text-align:right">
<A href="act_edition.jsp" title="Creació d'una nova fitxa  a la Biblioteca">Crea fitxa</A> | 
<A href="index.jsp" title="Torna a la pàgina anterior">Torna</A>  
</div>

	<table cellpadding="2" cellspacing="0" border="0">
	<tr>
	<td>
	<table cellpadding="2" cellspacing="0" border="0">
	<tr>
		<td>Àrea:</td>
		<td>
  			<SELECT class='layer-form' name="p_area">
  				<OPTION value="">Totes les àrees</OPTION>
<%
			java.util.Enumeration enumArees = qvb.getArees().elements();
			while (enumArees.hasMoreElements()){
				edu.xtec.lom.Area oArea = (edu.xtec.lom.Area)enumArees.nextElement();
%>				
  				<OPTION value="<%=oArea.getId()%>" <%=oArea.getId()==qvb.getIntParameter("p_area")?"selected":""%>><%=oArea.getText(qvb.getLanguage())%></OPTION>
<%
			}
%>  			
  			</SELECT>						
		</td>
	</tr>
	<tr>
		<td>Idioma:</td>
		<td>
  			<SELECT class='layer-form' name="p_language">
  				<OPTION value="">Tots els idiomes</OPTION>
<%
			java.util.Enumeration enumLanguage = qvb.getLanguages().elements();
			while (enumLanguage.hasMoreElements()){
				edu.xtec.lom.Idioma oIdioma = (edu.xtec.lom.Idioma)enumLanguage.nextElement();
%>				
  				<OPTION value="<%=oIdioma.getId()%>" <%=oIdioma.getId().equals(qvb.getParameter("p_language"))?"selected":""%>><%=oIdioma.getText(qvb.getLanguage())%></OPTION>
<%
			}
%>  			
  			</SELECT>						
		</td>
	</tr>
	<tr>
		<td>Nivell:</td>
		<td>
  			<SELECT class='layer-form' name="p_level">
  				<OPTION value="">Tots els nivells</OPTION>
<%
			java.util.Enumeration enumLevel = qvb.getLevels().elements();
			while (enumLevel.hasMoreElements()){
				edu.xtec.lom.Nivell oLevel = (edu.xtec.lom.Nivell)enumLevel.nextElement();
%>				
  				<OPTION value="<%=oLevel.getId()%>" <%=oLevel.getId()==qvb.getIntParameter("p_level")?"selected":""%>><%=oLevel.getText(qvb.getLanguage())%></OPTION>
<%
			}
%>  			
  			</SELECT>						
		</td>
	</tr>
	<tr>
		<td><br/>Mostra</td>
		<td><br/><INPUT type="text" name="p_max_page"  value="<%=qvb.getParameter("p_max_page")!=null?qvb.getParameter("p_max_page"):"20"%>" size="2"/> quaderns per pàgina</td>
	</tr>
	</table>
</td>
<td>
	<table cellpadding="2" cellspacing="0" border="0">
	<tr>
		<td>Autor:</td>
		<td><INPUT type="text" name="p_author" value="<%=qvb.getParameter("p_author")!=null?qvb.getParameter("p_author"):""%>"/></td>
	</tr>
	<tr>
		<td>Text:</td>
		<td><INPUT type="text" name="p_description"  value="<%=qvb.getParameter("p_description")!=null?qvb.getParameter("p_description"):""%>"/></td>
	</tr>
	<tr>
		<td>Estat:</td>
		<td>
		<SELECT name="p_state">
			<OPTION value="private" <%="private".equals(qvb.getParameter("p_state"))?"selected":""%>>Privat</OPTION>
			<OPTION value="public" <%="public".equals(qvb.getParameter("p_state"))?"selected":""%>>Publicat</OPTION>
		</SELECT>
		</td>
	</tr>
	<tr>
		<td><br/><INPUT type="submit" name="p_send" value="Cerca" title="Cerca"/></td>
		<td></td>
	</tr>
	</table>
</td>	
</tr>
</table>
					
<br/><br/>

<%  
	int iFrom = qvb.getIntParameter("p_from",0);
	java.util.Vector vActivities = qvb.getActivities(); 
	int iMaxPage = qvb.getIntParameter("p_max_page", 20);
	int iCurrentPage = (iFrom/iMaxPage);
%>

<table cellpadding="2" cellspacing="0" border="0" width="100%">
<tr>
	<td>S'han trobat <%=vActivities.size()%> quaderns</td>
	<td align="right">
	<%if (iCurrentPage>0){ %>
		<A href="#"  onclick="document.mainForm.p_from.value=<%=((iCurrentPage-1)*iMaxPage)%>;document.mainForm.submit();"><img src="http://clic.xtec.net/qv_web/image/back.gif" border="0" title="Anterior"/></A>
	<%} %>
	<%if (vActivities.size()>iMaxPage){ 
		int iPages = vActivities.size()/iMaxPage;
		if (vActivities.size()%iMaxPage!=0) iPages++;
	%>
		Pàgina 
		<SELECT name="p_from" onchange="this.form.submit();">
	<%
		for (int i=0;i<iPages;i++){
	%>
			<option value="<%=i*iMaxPage%>" <%=(iCurrentPage==i)?"selected":""%>><%=i+1%></option>
	<%	} %>
		</SELECT>
		/<%=iPages%>
		<%if (iCurrentPage<(iPages-1)){ %>
			<A href="#" onclick="document.mainForm.p_from.value=<%=((iCurrentPage+1)*iMaxPage)%>;document.mainForm.submit();"><img src="http://clic.xtec.net/qv_web/image/next.gif" border="0" title="Següent"/></A>
		<%} else{%>
				<span>&nbsp;&nbsp;</span>
		<%}%>
	<%} %>
	</td>
</tr>
</table>
					<br/>

					<table cellpadding="2" cellspacing="0" border="0" style="background:#FEFEFE;" width="100%">
					  <tr class="titol2" style="background: #36627F;color: #FEFEFE;">
					  	<td>Data</td>
						<td class="linia-titol" width="1px" style="padding:0"></td>
					  	<td width="90%">Títol</td>
					  </tr>
					  <tr>
					  	<td colspan="3" class="linia-titol" height="1" width="100%"></td>
					  </tr>
<%
	for (int i=iFrom;i<(iFrom+iMaxPage);i++){
		if (i>=vActivities.size()) break;
		edu.xtec.qv.biblio.Activity oActivity = (edu.xtec.qv.biblio.Activity)vActivities.get(i);
%>		
					  <tr <%=i%2==0?"style='background:#ECECEC'":""%>>
					  	<td><%=qvb.formatDate(oActivity.getRevisionDate())%></td>
						<td class="linia-titol" width="1px" style="padding:0"></td>
					  	<td><a href="act_edition.jsp?activity_id=<%=oActivity.getId()%>"><%=oActivity.getTitle(qvb.getLanguage())%></a></td>
					  </tr>
<%
	}
%>
				    </table>				    
				</tr>
				</table>
			</td>
		</tr>
		</table>	
	</td>
</tr>
</table>


</form>
</body>
</html>
