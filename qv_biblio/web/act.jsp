<%@page session="false" contentType="text/html; charset=iso-8859-1"
%><jsp:useBean id="qvb" class="edu.xtec.qv.biblio.beans.QVActivityBean" scope="request" /><%
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
<table border="0" cellpadding="0" cellspacing="0" width="100%" style="height:100%">
<tr>
	<td>
	<!-- CAPÇALERA -->
		<table border="0" cellpadding="0" cellspacing="0" width="700">
		<tr>
			<td><a href="<%=sWebServer%>/index.htm"><img src="<%=sWebServer%>/image/logo.gif" width="93" height="107" alt="Logo de Quaderns Virtuals" border="0"/></a></td>
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
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="height:100%">
				<%
					String sMsg = qvb.getMsg("biblio.alert");
					if (!"biblio.alert".equals(sMsg) && !"".equals(sMsg)){ %>
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
					<td class="titol1"><!-- InstanceBeginEditable name="titol" --><a name="inici"/><%=qvb.getActivityTitle()%><!-- InstanceEndEditable --></td>
				</tr>
				<tr>
					<td class="linia-titol" height="1px" width="100%"></td>
				</tr>
				<tr>
					<!-- InstanceBeginEditable name="separacio-titol-contingut" --><td height="10px"></td><!-- InstanceEndEditable -->
				</tr>
				<tr>
					<td class="text" style="height:100%"><!-- InstanceBeginEditable name="text" -->
				    <p>
<% if (qvb.isCCLicense()){ %>
					
					<a href="http://creativecommons.org/licenses/<%=qvb.getActivityLicense()%>/2.1/es/deed.<%=qvb.getLanguage()%>" target="_blank"><img src="<%=sWebServer%>/image/cc/<%=qvb.getActivityLicense()%>.gif" width="70" height="25" border="0" align="left" style="margin-right:10px"/></a>
<!--
<rdf:RDF xmlns="http://web.resource.org/cc/"
  xmlns:dc="http://purl.org/dc/elements/1.1/"
  xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
<Work rdf:about="http://clic.xtec.net/qv_biblio/biblio_act.jsp?activity_id=<%=qvb.getActivityId()%>">
   <dc:title><%=qvb.getActivityTitle()%></dc:title>
   <dc:date><%=qvb.getActivityDate()%></dc:date>
   <dc:description><%=qvb.getActivityDescription()%></dc:description>
<dc:creator>
<Agent><dc:title></dc:title></Agent>
</dc:creator>
<dc:rights>
<Agent><dc:title></dc:title></Agent>
</dc:rights>
<dc:type rdf:resource="http://purl.org/dc/dcmitype/Interactive" />
<dc:source rdf:resource="http://clic.xtec.net/qv_biblio/biblio_act.jsp?activity_id=<%=qvb.getActivityId()%>"/>
<license rdf:resource="http://creativecommons.org/licenses/<%=qvb.getActivityLicense()%>/2.1/" />
</Work>
<License rdf:about="http://creativecommons.org/licenses/<%=qvb.getActivityLicense()%>/2.1/">
 <permits rdf:resource="http://web.resource.org/cc/Reproduction" />
 <permits rdf:resource="http://web.resource.org/cc/Distribution" />
 <requires rdf:resource="http://web.resource.org/cc/Notice" />
 <requires rdf:resource="http://web.resource.org/cc/Attribution" />
<prohibits rdf:resource="http://web.resource.org/cc/CommercialUse" />
<permits rdf:resource="http://web.resource.org/cc/DerivativeWorks" />
<requires rdf:resource="http://web.resource.org/cc/ShareAlike" />
</License>
</rdf:RDF>
-->					
<% } %>					
					<img src="<%=sWebServer%>/image/biblio/<%=qvb.getActivityImage()%>" align="right" />
					
<%
	java.util.Enumeration enumAuthors = qvb.getActivityAuthors("aut").elements();
	while (enumAuthors.hasMoreElements()){
		edu.xtec.qv.biblio.Author oAuthor = (edu.xtec.qv.biblio.Author)enumAuthors.nextElement();
%>		
					<%=oAuthor.getFullName()%>
					<%String sMail = oAuthor.getMail();	
					if (sMail == null) {
						if (oAuthor.getEdu365Id()!=null) sMail=oAuthor.getEdu365Id()+"@xtec.cat";
					}%>
					<%if (sMail!=null){ %>
						<a href="mailto:<%=sMail%>"><img src="<%=sWebServer%>/image/bustia_off.gif" width="15" height="9" border="0" onmouseover="this.src='<%=sWebServer%>/image/bustia_on.gif';" onmouseout="this.src='<%=sWebServer%>/image/bustia_off.gif';"/></a>
					<%}if (enumAuthors.hasMoreElements()){%>,&nbsp; <%} %>
					
<%	} %>                    
                    <br/>
<%
	java.util.Enumeration enumEduInst = qvb.getActivityEducationalInstitutions().elements();
	while (enumEduInst.hasMoreElements()){
		edu.xtec.qv.biblio.EducationalInstitution oEduInst = (edu.xtec.qv.biblio.EducationalInstitution)enumEduInst.nextElement();
%>		
				    <a <%=(oEduInst.getURL()!=null?"href='"+oEduInst.getURL()+"' target='blank'":"href='#'")%>" class="titol2" style="font-weight:400;text-decoration:none"><%=oEduInst.getName()%></a>
<%	} %>                                        
					</p>
					
				    <p><%=qvb.getActivityDescription()%></p>
					<table cellpadding="5" cellspacing="0" border="0" style="padding-left:20px;padding-right:20px;">
					<tr>
						<td class="titol-taula"><%=qvb.getMsg("biblio.area")%></td>
						<td><%=qvb.getActivityArea()%></td>
					</tr>
					<tr>
					  <td class="titol-taula"><%=qvb.getMsg("biblio.level")%></td>
					  <td>
					  <% 
					  	java.util.Enumeration enumLevels = qvb.getActivityLevel().elements();
					    boolean first=true;
					  	while (enumLevels.hasMoreElements()){
							String sLevel = "";
							if (!first) sLevel = ", ";
							first=false;
							sLevel += enumLevels.nextElement();
					  %>
					  		<%=sLevel%>
					  <%} %>
					  </td>
					  </tr>
					<tr>
					  <td class="titol-taula"><%=qvb.getMsg("biblio.date")%></td>
					  <td><%=qvb.getActivityDate()%></td>
				    </tr>
				    
				    <tr>
				    	<td colspan="2"></td>
				    </tr>				    
					</table>

<%
	java.util.Enumeration enumVersion = qvb.getActivityVersions().elements();
	while (enumVersion.hasMoreElements()){
		edu.xtec.qv.biblio.Version oVersion = (edu.xtec.qv.biblio.Version)enumVersion.nextElement();
%>		
					<table cellpadding="0" cellspacing="0" border="0" style="background:#FEFEFE; width:100%; padding:3px 5px; ">
					<tr>
						<td colspan="2" class="linia-titol" height="1" width="100%"></td>
					</tr>
					<tr style="background-color: #36627F;">
						<td class="titol2" >Quadern Virtual &nbsp;&nbsp;[<%=qvb.getLanguageText(oVersion.getLang())%>] <span style="font-weight:100"><%=oVersion.getDescription()!=null?" - "+oVersion.getDescription():""%></span></td>
						<td class="titol3" align="right"><%=qvb.formatDate(oVersion.getRevisionDate())%></td>
					</tr>
					<tr>
						<td colspan="2" style="font-style: italic">
<%
	java.util.Hashtable hVerAut = qvb.getVersionAuthors(oVersion.getId());
	java.util.Enumeration enumRol = hVerAut.keys();
	while (enumRol.hasMoreElements()){
		String sRol = (String)enumRol.nextElement();
		java.util.Vector vAuthors = (java.util.Vector)hVerAut.get(sRol);
		java.util.Enumeration enumAutRol = vAuthors.elements();
%>
		<%=qvb.getRoleText(sRol)%>:
<%		
		while (enumAutRol.hasMoreElements()){
			edu.xtec.qv.biblio.Author oAuthor = (edu.xtec.qv.biblio.Author)enumAutRol.nextElement();
%>
						<%=oAuthor.getFullName()%>
<%			if (oAuthor.getMail()!=null){ %>
						<a href="mailto:<%=oAuthor.getMail()%>"><img src="<%=sWebServer%>/image/bustia_off.gif" width="15" height="9" border="0" onmouseover="this.src='<%=sWebServer%>/image/bustia_on.gif';" onmouseout="this.src='<%=sWebServer%>/image/bustia_off.gif';"/></a>
<%		
			}
		}
		if (enumRol.hasMoreElements()){
%>
			<br/>
<%			
		}
	}
%>						
						</td>						
					</tr>
					<tr>
						<td colspan="2" height="5px"></td>
					</tr>
					<tr>
						<td colspan="2" valign="top">
						<div class="version-content" >
							<%String sURL=qvb.getProperty("qv.biblio.public_url")+oVersion.getFolder()+"_"+oVersion.getLang(); %>
							<%=sURL%>
						</div>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<div style="color:#000; width:445px;float:left;">
							<%if (oVersion.getSections()>0){%><%=oVersion.getSections()%> <%=qvb.getMsg("biblio.version.sections")%> <br> <%} %>
							<%if (oVersion.getItems()>0){%><%=oVersion.getItems()%> <%=qvb.getMsg("biblio.version.items")%><br><%} %>
							<!-- (<%=oVersion.getTotalScore()%> <%=qvb.getMsg("biblio.version.totalscore")%>)-->
							<!-- %=oVersion.getZipSize()%-->
							<!-- http://clic.xtec.net/quaderns/biblioteca/<%=oVersion.getPath()+"_"+oVersion.getLang()%>-->
							</div>						
						
						
							<div style="float:right;vertical-align:bottom;">
								<br>
								<!-- a href="javascript:launchQV('quadern=<%=oVersion.getFolder()%>,lang=<%=oVersion.getLang()%>,skin=<%=oVersion.getSkin()%>,page=<%=oVersion.getPage()%>');"><%=qvb.getMsg("biblio.view")%></a--> 
								<!-- a href="#" onclick="window.open('<%=sURL%>?skin=<%=oVersion.getSkin()%>&section=<%=oVersion.getPage()%>&lang=<%=qvb.getLanguage()%>','QV');"><%=qvb.getMsg("biblio.view")%></a--> 
								<a href="<%=sURL%>?skin=<%=oVersion.getSkin()%>&section=<%=oVersion.getPage()%>&lang=<%=qvb.getLanguage()%>" target="_blank"><%=qvb.getMsg("biblio.view")%></a> 
								&nbsp;|&nbsp;
								<a href="<%=qvb.getProperty("qv.zipServlet")%>path=<%=qvb.getProperty("store.localURL")%>&folder=<%=oVersion.getFolder()%>_<%=oVersion.getLang()%>&fname=<%=oVersion.getFolder()%>_<%=oVersion.getLang()%>&includeall=true"><%=qvb.getMsg("biblio.download")%></a> <%=oVersion.getZipSize()>0?"("+oVersion.getZipSize()+")":""%> 
								<div style="height:3px"></div>
							</div>
						</td>
					</tr>
					<tr>
						<td colspan="2" class="linia-titol" height="1" width="100%"></td>
					</tr>
					</table>
					<!-- div style="text-align:right;font-size:10px;"><%=qvb.getMsg("biblio.quote")%> http://clic.xtec.net/quaderns/biblioteca/<%=oVersion.getFolder()%>/<%=oVersion.getLang()%></div-->
					<br/>
<%
	}
%>
					<div style="text-align:right;" class="text">
						<a href="act_list.jsp"><img src="<%=sWebServer%>/image/back_off.gif" border="0" onmouseover="this.src='<%=sWebServer%>/image/back_on.gif'" onmouseout="this.src='<%=sWebServer%>/image/back_off.gif'"/></a>
						<a href="#inici"><img src="<%=sWebServer%>/image/up_off.gif" border="0"  onmouseover="this.src='<%=sWebServer%>/image/up_on.gif'" onmouseout="this.src='<%=sWebServer%>/image/up_off.gif'"/></a>
					</div>
					<!-- InstanceEndEditable --></td>
				</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
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

<!-- phpmyvisites -->
<script type="text/javascript">
<!--
var a_vars = Array();
var pagename='qv_act_<%=qvb.getLanguage()%>';

var phpmyvisitesSite = 4;
var phpmyvisitesURL = "http://apliedu.xtec.cat/visites/phpmv2/phpmyvisites.php";
//-->
</script>
<script language="javascript" src="http://apliedu.xtec.cat/visites/phpmv2/phpmyvisites.js" type="text/javascript"></script>
<!-- /phpmyvisites --> 


</body>
<!-- InstanceEnd --></html>
