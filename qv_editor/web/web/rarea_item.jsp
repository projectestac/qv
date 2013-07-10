<%@page contentType="text/html; charset=ISO-8859-1" import="edu.xtec.qv.editor.beans.IQVListControlBean"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.IQVListControlBean qvsb=(edu.xtec.qv.editor.beans.IQVListControlBean)qvb.getSpecificBean();
String sType = qvb.getParameter("p_list_type");
if (sType==null) sType = IQVListControlBean.TARGET_TYPE;
String sLayer = "rareaLayer";
String sForm = qvb.getParameter("p_form");
String sListName = qvb.getParameter("p_list_name");
String sAction = qvb.getParameter("action");
String sRareaAction =qvb.getParameter("p_"+sType+"_action");


String sIdent = qvb.getParameter(sListName);

edu.xtec.qv.qti.ResponseLabel o = null;
String sContinue = request.getParameter("p_continue");
if ("next".equals(sContinue)){
	 o = (edu.xtec.qv.qti.ResponseLabel)qvsb.getNextObject(sType, sIdent);
}else if ("back".equals(sContinue)){
	 o = (edu.xtec.qv.qti.ResponseLabel)qvsb.getPreviousObject(sType, sIdent);
}else{
	 o = (edu.xtec.qv.qti.ResponseLabel)qvsb.getObject(sType, sIdent);
}

if (o!=null) sIdent = o.getIdent();
else sRareaAction = IQVListControlBean.A_ADD_LIST_OBJECT;

String sHotspotType = edu.xtec.qv.editor.util.QTIUtil.getHotspotType(qvb.getPregunta());
String sRareaType = edu.xtec.qv.qti.ResponseLabel.RAREA_RECTANGLE;
String sLabel = "";
String sWidth = "";
String sHeight = "";
String sX0 = "";
String sY0 = "";
String sPunts = "";
boolean bIsCorrect = false;
if (o!=null && sRareaAction!=null && sRareaAction.equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT)){
	if (o.getIdent()!=null && (!edu.xtec.qv.qti.util.StringUtil.isNumber(o.getIdent()) || o.getIdent().length()<8)){
		sLabel = o.getIdent(); 
	}
	sRareaType = o.getRarea();
	sWidth = o.getWidth()!=null?o.getWidth():"";
	sHeight = o.getHeight()!=null?o.getHeight():"";
	sX0 = o.getX0()!=null?o.getX0():"";
	sY0 = o.getY0()!=null?o.getY0():"";
	sPunts = o.getText();
	bIsCorrect = edu.xtec.qv.editor.util.QTIUtil.isCorrectResponseLabel(qvb.getPregunta(), o);
}
java.util.Vector vRareaTypes = new java.util.Vector();
vRareaTypes.addElement(edu.xtec.qv.qti.ResponseLabel.RAREA_RECTANGLE);
vRareaTypes.addElement(edu.xtec.qv.qti.ResponseLabel.RAREA_ELLIPSE);
vRareaTypes.addElement(edu.xtec.qv.qti.ResponseLabel.RAREA_BOUNDED);
%>	

<script language="JavaScript">
<!--
var form = <%=sForm%>;
document.write('<INPUT type="hidden" name="show">');
// -->
</script>
<SCRIPT src="scripts/move_layer.js"></SCRIPT>


<INPUT type="hidden" name="p_continue">
<DIV id='<%=sLayer%>' style="position:absolute; top:130; left:350; z-index:1000; width:325; height: 290; padding:5px;"  class='layer-box'>
<TABLE class='layer-box' border='0' cellpadding='5' cellspacing='5' width='350px' height='100%' style='height:100%'>
<TR>
	<TD>
		<TABLE border='0' cellpadding='0' cellspacing='0' style="width:100%">
		<TR>
			<TD valign="top" style="width:50px;background:url('imatges/move_layer_off.gif') no-repeat;" onmousedown="move('<%=sLayer%>');" title="<%=qvb.getMsg("layer.move.tip")%>">&nbsp;</TD>
			<!--TD style="width:50" valign="top" title="<%=qvb.getMsg("layer.move.tip")%>">
				<A id="move_link" href="javascript:move('<%=sLayer%>');" class="layer-link"><IMG src="imatges/move_layer_off.gif" border="0" width="20" height="20"/></A>
			</TD-->
			<TD align="right" class="layer-title">
				<%=qvb.getMsg("item.response_label."+sType+".title")%>
				<br>
				<A href="javascript:<%=sForm%>.p_continue.value='back';<%=sForm%>.p_list_name.value='<%=sListName%>';<%=sForm%>.<%=sListName%>.value='<%=sIdent%>';<%=sForm%>.p_list_type.value='<%=sType%>';enviar('p_<%=sType%>_action', <%=sForm%>);" class="layer-link" style="text-decoration:none">&lt;</A>
				<A href="javascript:<%=sForm%>.p_continue.value='next';<%=sForm%>.p_list_name.value='<%=sListName%>';<%=sForm%>.<%=sListName%>.value='<%=sIdent%>';<%=sForm%>.p_list_type.value='<%=sType%>';enviar('p_<%=sType%>_action', <%=sForm%>);" class="layer-link" style="text-decoration:none">&gt;</A>
			</TD>
		</TR>
		</TABLE>
	</TD>
</TR>

<TR>
  <TD valign="top" height='100%' style='height:100%'>
  	<TABLE border='0' cellpadding='3' cellspacing='0' >
  	<TR>
  		<TD class="layer-text" valign="top" width="90"><%=qvb.getMsg("item.response_label.rarea.type")%></TD>
  		<TD valign="top">
			<SELECT name="p_rarea_type" size="1" class="layer-form" onchange="if (this.value=='<%=edu.xtec.qv.qti.ResponseLabel.RAREA_BOUNDED%>'){document.getElementById('rarea_bounded').style.display='block';document.getElementById('rarea_other').style.display='none';}else{document.getElementById('rarea_bounded').style.display='none';document.getElementById('rarea_other').style.display='block';}">
<%
java.util.Enumeration enumTypes=vRareaTypes.elements();
while(enumTypes.hasMoreElements()){
	String sTmpRareaType = (String)enumTypes.nextElement();
%>	
				<OPTION value="<%=sTmpRareaType%>" <%=sTmpRareaType.equalsIgnoreCase(sRareaType)?"selected":""%> ><%=qvb.getMsg("item.response_label.rarea.type."+sTmpRareaType.toLowerCase())%></OPTION>
<%}%>
			</SELECT>
  		</TD>
  	</TR>
  	<TR>
  		<TD class="layer-text"><%=qvb.getMsg("item.response_label.label")%></TD>
  		<TD><INPUT type="text" size="11" name="p_rarea_ident" value="<%=sLabel%>" class="layer-form" /></TD>
  	</TR>
<!--INPUT type="hidden" name="p_rarea_ident" value="<%=sIdent%>"/-->
<%//if (edu.xtec.qv.qti.ResponseLabel.RAREA_BOUNDED.equals(sRareaType)){%>
  	<TR>
  		<TD colspan="2">
			<DIV id="rarea_bounded" style="display: <%=edu.xtec.qv.qti.ResponseLabel.RAREA_BOUNDED.equals(sRareaType)?"block":"none"%>">
			<%if (sIdent==null){%>
				<jsp:include page="bounded_response.jsp" flush="true"/>
			<%}else{%>
				<jsp:include page="bounded_response.jsp" flush="true">
					<jsp:param name="ident" value="<%=sIdent%>"/>
				</jsp:include>
			<%}%>
			</DIV>
  		</TD>
  	</TR>
<%//}else{%>
  	<TR>
  		<TD colspan="2">
		<DIV id="rarea_other" style="display: <%=edu.xtec.qv.qti.ResponseLabel.RAREA_BOUNDED.equals(sRareaType)?"none":"block"%>">
		<TABLE width="100%" cellpadding="0" cellspacing="2" border="0">	
		  	<TR>
		  		<TD class="layer-text"><%=qvb.getMsg("item.response_label.x0")%></TD>
		  		<TD><INPUT type="text" size="3" name="p_rarea_x0" value="<%=sX0%>" class="layer-form" /></TD>
		  	</TR>
		  	<TR>
		  		<TD class="layer-text"><%=qvb.getMsg("item.response_label.y0")%></TD>
		  		<TD><INPUT type="text" size="3" name="p_rarea_y0" value="<%=sY0%>" class="layer-form" /></TD>
		  	</TR>
		  	<TR>
		  		<TD class="layer-text"><%=qvb.getMsg("item.response_label.width")%></TD>
		  		<TD class="layer-form"><INPUT type="text" size="3" name="p_rarea_width" value="<%=sWidth%>" class="layer-form" /> <%=qvb.getMsg("pixels")%></TD>
		  	</TR>
		  	<TR>
		  		<TD class="layer-text"><%=qvb.getMsg("item.response_label.height")%></TD>
		  		<TD class="layer-form"><INPUT type="text" size="3" name="p_rarea_height" value="<%=sHeight%>" class="layer-form" /> <%=qvb.getMsg("pixels")%></TD>
		  	</TR>
		  </TABLE>
		</DIV> 	
  		</TD>
  	</TR>
<%//}%>  	
<%
if (edu.xtec.qv.editor.beans.IQVListControlBean.HOTSPOT_TYPE.equals(sType) &&
	  (edu.xtec.qv.editor.util.QVConstants.ZONE_HOTSPOT_TYPE.equals(sHotspotType) ||
 	   edu.xtec.qv.editor.util.QVConstants.OPTION_HOTSPOT_TYPE.equals(sHotspotType))){%>
  	<TR>
  		<TD colspan="2" class="layer-text">
  			<INPUT type="checkbox" name="p_rarea_is_correct_response" class="layer-form" <%=bIsCorrect?"checked":""%>/>
  			Zona amb resposta correcta
  		</TD>
  	</TR>
<%}%>  	
  	</TABLE>
  </TD>
  <TD width='30'></TD>
</TR>
<TR>	
	<TD class="layer-link" colspan="2" align="center">
		<A href="javascript:<%=sForm%>.hide_layers.value='true';<%=sForm%>.p_list_name.value='<%=sListName%>';<%=sForm%>.<%=sListName%>.value='<%=sIdent%>';<%=sForm%>.p_list_type.value='<%=sType%>';if (<%=sForm%>.bounded_list!=null){var tmp='';for(i=0;i<<%=sForm%>.bounded_list.options.length;i++){tmp+=<%=sForm%>.bounded_list.options[i].text+',';}<%=sForm%>.p_rarea_bounded_list.value=tmp;}enviar('<%=sRareaAction%>', <%=sForm%>);" title="<%=qvb.getMsg("item.response_label.ok.tip")%>" class="layer-link"><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("item.response_label.ok")%></A>
		&nbsp;|&nbsp;
		<A href="javascript:<%=sForm%>.p_<%=sType%>_action.value='';if (<%=sForm%>.p_rarea_width!=null){<%=sForm%>.p_rarea_width.value='';}hide_rarea_layer();" title="<%=qvb.getMsg("item.response_label.cancel.tip")%>" class="layer-link"><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("item.response_label.cancel")%></A>
	</TD>
</TR>
</TABLE>
</DIV>
<SCRIPT>
<%if (sAction!=null && sAction.equalsIgnoreCase("p_"+sType+"_action") && sRareaAction!=null && 
	( sRareaAction.equalsIgnoreCase(IQVListControlBean.A_ADD_LIST_OBJECT) ||
	  sRareaAction.equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT))){%>
	show_rarea_layer(<%=sForm%>);
<%}else{%>
	hide_rarea_layer();
<%}%>
</SCRIPT>


