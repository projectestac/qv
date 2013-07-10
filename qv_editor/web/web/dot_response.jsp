<%@page contentType="text/html; charset=ISO-8859-1" import="edu.xtec.qv.editor.beans.IQVListControlBean"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.IQVListControlBean qvsb=(edu.xtec.qv.editor.beans.IQVListControlBean)qvb.getSpecificBean();
String sType = qvb.getParameter("p_list_type");
if (sType==null) sType = IQVListControlBean.DOT_RESPONSE_TYPE;
String sLayer = "dotResponseLayer";
String sForm = qvb.getParameter("p_form");
String sListName = qvb.getParameter("p_list_name");
String sAction = qvb.getParameter("action");
String sDotAction =qvb.getParameter("p_"+sType+"_action");


String sIdent = qvb.getParameter(sListName);

edu.xtec.qv.qti.Varequal o = (edu.xtec.qv.qti.Varequal)qvsb.getObject(sType, sIdent);

if (o!=null) sIdent = o.getIdent();
else sDotAction = IQVListControlBean.A_ADD_LIST_OBJECT;

String sText = "";
if (o!=null && sDotAction!=null && sDotAction.equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT)){
	sText = o.getText();
}
%>	

<script language="JavaScript">
<!--
var form = <%=sForm%>;
document.write('<INPUT type="hidden" name="show">');
// -->
</script>
<SCRIPT src="scripts/move_layer.js"></SCRIPT>


<INPUT type="hidden" name="p_continue">
<DIV id='<%=sLayer%>' style="position:absolute; top:130; left:350; z-index:1000; width:400; height: 290; padding:5px;"  class='layer-box'>
<TABLE class='layer-box' border='0' cellpadding='5' cellspacing='5' width='300px' height='100%' style='height:100%'>
<TR>
	<TD>
		<TABLE border='0' cellpadding='0' cellspacing='0' style="width:100%">
		<TR>
			<TD valign="top" style="width:50;background:url('imatges/move_layer_off.gif') no-repeat;" onmousedown="move('<%=sLayer%>');" title="<%=qvb.getMsg("layer.move.tip")%>">&nbsp;</TD>
			<!--TD style="width:50" valign="top" title="<%=qvb.getMsg("layer.move.tip")%>">
				<A id="move_link" href="javascript:move('<%=sLayer%>');" class="layer-link"><IMG src="imatges/move_layer_off.gif" border="0" width="20" height="20"/></A>
			</TD-->
			<TD align="right" class="layer-title">
				<%=qvb.getMsg("item.hotspot."+sType+".title")%>
				<br>
			</TD>
		</TR>
		</TABLE>
	</TD>
</TR>

<TR>
  <TD valign="top" height='100%' style='height:100%'>
  	<TABLE border='0' cellpadding='3' cellspacing='0' >
  	<TR>
  		<TD class="layer-text" valign="top"><%=qvb.getMsg("item.hotspot.dot.response.order")%></TD>
  		<TD><TEXTAREA wrap="soft" cols="40" rows="5" name="p_dot_response_text" class="layer-form"><%=sText%></TEXTAREA></TD>
  	</TR>
  	</TABLE>
<TR>	
	<TD class="layer-link" colspan="2" align="center">
		<A href="javascript:<%=sForm%>.hide_layers.value='true';<%=sForm%>.p_list_name.value='<%=sListName%>';<%=sForm%>.<%=sListName%>.value='<%=sIdent%>';<%=sForm%>.p_list_type.value='<%=sType%>';if (<%=sForm%>.bounded_list!=null){var tmp='';for(i=0;i<<%=sForm%>.bounded_list.options.length;i++){tmp+=<%=sForm%>.bounded_list.options[i].text+',';}<%=sForm%>.p_rarea_bounded_list.value=tmp;}enviar('<%=sDotAction%>', <%=sForm%>);" title="<%=qvb.getMsg("item.response_label.ok.tip")%>" class="layer-link"><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("item.response_label.ok")%></A>
		&nbsp;|&nbsp;
		<A href="javascript:<%=sForm%>.p_<%=sType%>_action.value='';hide_dot_response_layer();" title="<%=qvb.getMsg("item.response_label.cancel.tip")%>" class="layer-link"><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("item.response_label.cancel")%></A>
	</TD>
</TR>
</TABLE>
</DIV>
<SCRIPT>
<%if (sAction!=null && sAction.equalsIgnoreCase("p_"+sType+"_action") && sDotAction!=null && 
	( sDotAction.equalsIgnoreCase(IQVListControlBean.A_ADD_LIST_OBJECT) ||
	  sDotAction.equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT))){%>
	show_dot_response_layer(<%=sForm%>);
<%}else{%>
	hide_dot_response_layer();
<%}%>
</SCRIPT>


