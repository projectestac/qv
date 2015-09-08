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
<form name="qv_form" method="post" action="act_edition.jsp" target="_self">
<input type="hidden" name="p_action" value="none"/>
<input type="hidden" name="activity_id" value="<%=qvb.getParameter("activity_id")%>"/>

<h1 style="text-align:right" class="title"><%=qvb.getActivityId()>0?"Edició":"Creació"%> d'una activitat de la biblioteca de Quaderns Virtuals</h1>

<div style="text-align:right">
<A href="act_bd.jsp" title="Torna a la pàgina anterior">Torna</A>  
</div>

<table border="0" cellpadding="2" cellspacing="0" width="100%">
<tr>
	<td width="100px">Títol</td>
	<td><input type="text" name="p_title" size="80" value="<%=qvb.getActivityTitle()%>"/></td>
</tr>
<tr>
	<td width="100px" valign="top">Àrees</td>
	<td>
  			<SELECT name="p_area" size="3" multiple>
  				<OPTION value=""></OPTION>
<%
			java.util.Enumeration enumArees = qvb.getArees().elements();
			while (enumArees.hasMoreElements()){
				edu.xtec.lom.Area oArea = (edu.xtec.lom.Area)enumArees.nextElement();
%>				
  				<OPTION value="<%=oArea.getId()%>" <%=qvb.hasActivityArea(oArea.getId())?"selected":""%> ><%=oArea.getText("ca")%></OPTION>
<%
			}
%>  			
  			</SELECT>						
	</td>
</tr>
<tr>
	<td width="100px" valign="top">Nivells</td>
	<td>
  			<SELECT name="p_level" size="3" multiple>
  				<OPTION value=""></OPTION>
<%
			java.util.Enumeration enumLevels = qvb.getLevels().elements();
			while (enumLevels.hasMoreElements()){
				edu.xtec.lom.Nivell oLevel = (edu.xtec.lom.Nivell)enumLevels.nextElement();
%>				
  				<OPTION value="<%=oLevel.getId()%>" <%=qvb.hasActivityLevel(oLevel.getId())?"selected":""%> ><%=oLevel.getText("ca")%></OPTION>
<%
			}
%>  			
  			</SELECT>						
	</td>
</tr>

<tr>
	<td width="100px" valign="top">Autors</td>
	<td>
  			<SELECT name="p_author" size="3" multiple>
  				<OPTION value=""></OPTION>
<%
			java.util.Enumeration enumAuthors = qvb.getAuthors().elements();
			while (enumAuthors.hasMoreElements()){
				edu.xtec.qv.biblio.Author oAuthor = (edu.xtec.qv.biblio.Author)enumAuthors.nextElement();
%>				
  				<OPTION value="<%=oAuthor.getId()%>" <%=qvb.hasActivityAuthor(oAuthor.getId())?"selected":""%> ><%=oAuthor.getFullName()%></OPTION>
<%
			}
%>  			
  			</SELECT>						
	</td>
</tr>

<tr>
	<td width="100px">Centre</td>
	<td><input type="text" name="p_centre" size="9" maxlength="8" value="<%=qvb.getActivityEducationalInstitutions()%>"/></td>
</tr>


<tr>
	<td width="100px" valign="top">Imatge</td>
	<td>
  			<SELECT name="p_image">
  				<OPTION value=""></OPTION>
<%
				String[] aImages = qvb.getImageFiles();
				for (int i=0;i<aImages.length;i++){
%>
  				<OPTION value="<%=aImages[i]%>" <%=aImages[i].equalsIgnoreCase(qvb.getActivityImage())?"selected":""%>><%=aImages[i]%></OPTION>
<%					
				}
%>  				
  			</SELECT>
  			&nbsp;&nbsp;
	</td>
</tr>
<tr>
	<td width="100px">Estat</td>
	<td>
		<SELECT name="p_state">
			<OPTION value="private" <%="private".equals(qvb.getActivityState())?"selected":""%>>Privat</OPTION>
			<OPTION value="public" <%="public".equals(qvb.getActivityState())?"selected":""%>>Publicat</OPTION>
		</SELECT>
	</td>
</tr>


<tr>
	<td width="100px">Data de creació</td>
	<td><input type="text" name="p_creation_date" size="9" maxlength="10" value="<%=qvb.getActivityCreationDate()%>"/></td>
</tr>
<tr>
	<td width="100px">Data de revisió</td>
	<td><input type="text" name="p_revision_date" size="9" maxlength="10"  value="<%=qvb.getActivityRevisionDate()%>"/></td>
</tr>
<tr>
	<td colspan="2">
		<%if (qvb.getActivityId()>0){%><input type="submit" name="p_preview" value="Mostra" onclick="window.open('/qv_biblio/act.jsp?activity_id=<%=qvb.getActivityId()%>','')"/><% }%>
		<input type="submit" name="p_save" value="Desa" onclick="this.form.p_action.value='save';"/>
	</td>
</tr>
</table>

</form>
</body>
</html>
