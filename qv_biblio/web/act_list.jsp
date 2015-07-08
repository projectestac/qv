<%@page session="false" contentType="text/html; charset=iso-8859-1"
%><jsp:useBean id="qvb" class="edu.xtec.qv.biblio.beans.QVActivityListBean" scope="request" /><%
if(!qvb.init(request, response)){%><jsp:forward page="error.jsp"/><%}%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><!-- InstanceBegin template="../../../qv_web/src/webapp/Templates/WebQV_jsp.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
<!-- InstanceBeginEditable name="doctitle" -->
  <title>Quaderns Virtuals</title>
<!-- InstanceEndEditable -->  
<%String sWebServer=qvb.getProperty("server.base")+qvb.getProperty("server.base.web"); %>  
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
  <LINK rel="stylesheet" type="text/css" href="<%=sWebServer%>/css/qv_web.css" />
  <LINK rel="icon" type="image/x-icon" href="<%=sWebServer%>/image/favicon.ico"/>
  <LINK rel="shortcut icon" type="image/x-icon"  href="<%=sWebServer%>/image/favicon.ico"/>

<!-- InstanceBeginEditable name="menu_sel" -->
<script language="JavaScript" type="text/javascript">
var menu_n1_sel="m_biblio";
var menu_n2_sel="m_biblio_cerca";
</script>
<!-- InstanceEndEditable -->  

<script type="text/javascript" src="<%=sWebServer%>/<%=qvb.getLanguage()%>/scripts/menu_qv.js"></script>
<script type="text/javascript" src="<%=sWebServer%>/scripts/qv_web.js"></script>
<script type="text/javascript" src="<%=sWebServer%>/scripts/launchQV.js"></script>
</head>

<body>
<form name="mainForm" action="act_list.jsp" method="POST">	
<table border="0" cellpadding="0" cellspacing="0" width="100%" style="height:100%">
<tr>
	<td>
	<!-- CAPÇALERA -->
		<table border="0" cellpadding="0" cellspacing="0" width="700">
		<tr>
			<td><a href="<%=sWebServer%>/index.htm"><img src="<%=sWebServer%>/image/logo.gif" width="93" height="107" alt="Quaderns Virtuals" border="0"/></a></td>
			<td width="600" style="background: url(<%=sWebServer%>/image/fons_menu.gif)">
				<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td height="31px"></td>
				</tr>
				<tr>
					<td>
						<table border="0" cellpadding="0" cellspacing="0" height="16px">
						<tr>
							<script>
								for(i=0;i<menu_n1.length;i++){
									var bg = "";
									document.writeln("<td>|</td>");
									if (menu_n1_sel==menu_n1[i][0]) bg=" style='background: url(<%=sWebServer%>/image/m_n1_fons.jpg)' ";
									document.writeln("<td id="+menu_n1[i][0]+bg+">&nbsp;&nbsp;");
									if (menu_n1[i][2].indexOf('http://')==0){
										document.writeln("<a class='menu' href='"+menu_n1[i][2]+"'>"+menu_n1[i][1]+"</a>");
									}else{
										document.writeln("<a class='menu' href='<%=sWebServer%>/<%=qvb.getLanguage()%>/"+menu_n1[i][2]+"'>"+menu_n1[i][1]+"</a>");
									}
									document.writeln("&nbsp;&nbsp;</td>");
								}
							</script>
							<td>|</td>
						</tr>
						</table>					
					</td>
				</tr>
				<tr>
					<td height="7px"></td>
				</tr>
				<tr>
					<td>
						<table border="0" cellpadding="0" cellspacing="0" height="16px" style='background: url(<%=sWebServer%>/image/m_n1_fons.jpg)'>
						<tr>
							<script>
							 if (menu_n1_sel!=""){
								var menu_n2 = eval(menu_n1_sel+"_n2");
								for(i=0;i<menu_n2.length;i++){
									var bg = "";
									document.writeln("<td>|</td>");
									if (menu_n2_sel==menu_n2[i][0]) bg=" style='background: url(<%=sWebServer%>/image/m_n2_fons.jpg)' ";
									document.writeln("<td id="+menu_n2[i][0]+bg+">&nbsp;&nbsp;");
									if (menu_n2[i][2].indexOf('http://')==0){
										document.writeln("<a class='menu' href='"+menu_n2[i][2]+"'>"+menu_n2[i][1]+"</a>");
									}else{
										document.writeln("<a class='menu' href='<%=sWebServer%>/<%=qvb.getLanguage()%>/"+menu_n2[i][2]+"'>"+menu_n2[i][1]+"</a>");
									}
									document.writeln("&nbsp;&nbsp;</td>");
								}
							 } else{
							 	document.writeln("<td></td>");
							 }
							</script>
						</tr>
						</table>
					</td>
				</tr>
				</table>
			</td>
			<td width="8" style="background: url(<%=sWebServer%>/image/fons_menu_d.gif)"></td>
		</tr>
		</table>
	</td>
</tr>
<tr>
	<td height="20px"></td>
</tr>
<tr>
	<td style="height:100%">
	<!-- CONTINGUT -->
		<table border="0" cellpadding="0" cellspacing="0" width="700" style="height:100%">
		<tr>
			<td width="80"></td>
			<td style="height:100%">
				<table border="0" cellpadding="5" cellspacing="0" width="100%" style="height:100%">
				<%
					String sMsg = qvb.getMsg("biblio.alert");
					if (!"biblio.alert".equals(sMsg) && !"".equals(sMsg) ){ %>
				<tr>
					<td>
					  <!-- INICI AVIS -->
					  <div id="avis">
					  <table style="background: rgb(254, 254, 254) none repeat scroll 0%; width: 100%;" border="0" cellpadding="0" cellspacing="3">
                        <tbody>
                          <tr>
                            <td colspan="2" class="linia-titol" height="1" width="100%"></td>
                          </tr>
                          <tr>
                            <td colspan="2">
								<%=sMsg%>
							</td>
                          </tr>
                          <tr>
                            <td colspan="2" class="linia-titol" height="1" width="100%"></td>
                          </tr>
                        </tbody>
				    </table>
					<br/>
					</div>
				  <!-- FI AVIS -->
					</td>
				</tr>
				<%} %>
				<tr>
					<td class="titol1"><!-- InstanceBeginEditable name="titol" --><%=qvb.getMsg("biblio.list.title")%> <!-- InstanceEndEditable --></td>
				</tr>
				<tr>
					<td class="linia-titol" height="1px" width="100%" style="padding:0"></td>
				</tr>
				<tr>
					<!-- InstanceBeginEditable name="separacio-titol-contingut" --><td height="20px"></td><!-- InstanceEndEditable -->
				</tr>
				<tr>
					<td class="text" style="height:100%;"><!-- InstanceBeginEditable name="text" -->

					<table cellpadding="2" cellspacing="0" border="0">
					<tr>
					<td>
					<table cellpadding="2" cellspacing="0" border="0">
					<tr>
						<td><%=qvb.getMsg("biblio.area")%>:</td>
						<td>
				  			<SELECT class='layer-form' name="p_area">
				  				<OPTION value=""><%=qvb.getMsg("biblio.area.all")%></OPTION>
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
						<td><%=qvb.getMsg("biblio.language")%>:</td>
						<td>
				  			<SELECT class='layer-form' name="p_language">
				  				<OPTION value=""><%=qvb.getMsg("biblio.language.all")%></OPTION>
				<%
							java.util.Enumeration enumLanguage = qvb.getIdiomes().elements();
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
						<td><%=qvb.getMsg("biblio.level")%>:</td>
						<td>
				  			<SELECT class='layer-form' name="p_level">
				  				<OPTION value=""><%=qvb.getMsg("biblio.level.all")%></OPTION>
				<%
							java.util.Enumeration enumLevel = qvb.getNivells().elements();
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
						<td><br/><%=qvb.getMsg("biblio.show")%></td>
						<td><br/><INPUT type="text" name="p_max_page"  value="<%=qvb.getParameter("p_max_page")!=null?qvb.getParameter("p_max_page"):"20"%>" size="2"/> <%=qvb.getMsg("biblio.qv_page")%></td>
					</tr>
					</table>
				</td>
				<td>
					<table cellpadding="2" cellspacing="0" border="0">
					<tr>
						<td><%=qvb.getMsg("biblio.author")%>:</td>
						<td><INPUT type="text" name="p_author" value="<%=qvb.getParameter("p_author")!=null?qvb.getParameter("p_author"):""%>"/></td>
					</tr>
					<tr>
						<td><%=qvb.getMsg("biblio.title")%>:</td>
						<td><INPUT type="text" name="p_title" value="<%=qvb.getParameter("p_title")!=null?qvb.getParameter("p_title"):""%>"/></td>
					</tr>
					<tr>
						<td><%=qvb.getMsg("biblio.description")%>:</td>
						<td><INPUT type="text" name="p_description"  value="<%=qvb.getParameter("p_description")!=null?qvb.getParameter("p_description"):""%>"/></td>
					</tr>
					<tr>
						<td><br/><INPUT type="submit" name="p_send" value="<%=qvb.getMsg("biblio.search")%>" title="<%=qvb.getMsg("biblio.search")%>"/></td>
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
						<td><%=qvb.getMsg("biblio.qv_found",String.valueOf(vActivities.size()))%></td>
						<td align="right">
						<%if (iCurrentPage>0){ %>
							<A href="#"  onclick="document.mainForm.p_from.value=<%=((iCurrentPage-1)*iMaxPage)%>;document.mainForm.submit();"><img src="<%=sWebServer%>/image/back.gif" border="0" title="Anterior"/></A>
						<%} %>
						<%if (vActivities.size()>iMaxPage){ 
							int iPages = vActivities.size()/iMaxPage;
							if (vActivities.size()%iMaxPage!=0) iPages++;
						%>
							<%=qvb.getMsg("biblio.page")%> 
							<SELECT name="p_from" onchange="this.form.submit();">
						<%
							for (int i=0;i<iPages;i++){
						%>
								<option value="<%=i*iMaxPage%>" <%=(iCurrentPage==i)?"selected":""%>><%=i+1%></option>
						<%	} %>
							</SELECT>
							/<%=iPages%>
							<%if (iCurrentPage<(iPages-1)){ %>
								<A href="#" onclick="document.mainForm.p_from.value=<%=((iCurrentPage+1)*iMaxPage)%>;document.mainForm.submit();"><img src="<%=sWebServer%>/image/next.gif" border="0" title="Següent"/></A>
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
					  	<td><%=qvb.getMsg("biblio.date")%></td>
						<td class="linia-titol" width="1px" style="padding:0"></td>
					  	<td width="80%"><%=qvb.getMsg("biblio.title")%></td>
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
						<!--td><a href="javascript:launchQV('quadern=clima,skin=default');"><img src="../image/visualitza_off.gif" width="18" height="11" onmouseover="this.src='../image/visualitza_on.gif';" onmouseout="this.src='../image/visualitza_off.gif';" border="0"/></a></td-->
					  	<td><a href="act.jsp?activity_id=<%=oActivity.getId()%>"><%=oActivity.getTitle(qvb.getLanguage())%></a>
					  	<%if(oActivity.getRevisionDate().after(new java.util.Date(new java.util.Date().getTime()-604800000))){ %>&nbsp;&nbsp;<img src="../qv_web/<%=qvb.getLanguage()%>/image/nou.gif" width="21" height="9"/><%} %></td>
					  </tr>
<%
	}
%>
				    </table>
				    
				    <p><%=qvb.getMsg("biblio.requirements")%></p>
					  <!-- InstanceEndEditable --></td>
				</tr>
				</table>
			</td>
		</tr>
		</table>	
	</td>
</tr>
<tr>
	<td>
	<!-- PEU -->
		<table border="0" cellpadding="0" cellspacing="0" width="700">
		<tr>
			<td width="80" height="40" valign="bottom"></td>
			<td><a href="http://www.xtec.net"><img src="<%=sWebServer%>/image/linia_inf.gif" title="Xarxa Telemàtica Educativa de Catalunya" width="620" height="14" border="0"/></a></td>
		</tr>
		</table>
	</td>
</tr>
</table>
</form>
<script src="http://www.google-analytics.com/urchin.js" type="text/javascript">
</script>
<script type="text/javascript">
_uacct = "UA-2220604-2";
urchinTracker();
</script>

<!-- phpmyvisites -->
<script type="text/javascript">
<!--
var a_vars = Array();
var pagename='qv_biblio_<%=qvb.getLanguage()%>';

var phpmyvisitesSite = 4;
var phpmyvisitesURL = "http://apliedu.xtec.cat/visites/phpmv2/phpmyvisites.php";
//-->
</script>
<script language="javascript" src="http://apliedu.xtec.cat/visites/phpmv2/phpmyvisites.js" type="text/javascript"></script>
<!-- /phpmyvisites --> 

</body>
<!-- InstanceEnd --></html>
