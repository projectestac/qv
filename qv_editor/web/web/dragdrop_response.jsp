<%@page contentType="text/html; charset=ISO-8859-1" import="edu.xtec.qv.editor.beans.IQVListControlBean"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.IQVListControlBean qvsb=(edu.xtec.qv.editor.beans.IQVListControlBean)qvb.getSpecificBean();
String sType = qvb.getParameter("p_list_type");
if (sType==null) sType = IQVListControlBean.DOT_RESPONSE_TYPE;
String sLayer = "dragdropResponseLayer";
String sForm = qvb.getParameter("p_form");
String sListName = qvb.getParameter("p_list_name");
String sAction = qvb.getParameter("action");
String sDragdropResponseAction =qvb.getParameter("p_"+sType+"_action");


String sIdent = qvb.getParameter(sListName);

edu.xtec.qv.qti.And o = (edu.xtec.qv.qti.And)qvsb.getObject(sType, sIdent);

if (o!=null) sIdent = o.getIdent();
else sDragdropResponseAction = IQVListControlBean.A_ADD_LIST_OBJECT;

java.util.Vector vAllTargets = edu.xtec.qv.editor.util.QVTargetListControl.getTargetListObjects(qvb.getPregunta());
java.util.Vector vAllSources = edu.xtec.qv.editor.util.QVSourceListControl.getSourceListObjects(qvb.getPregunta());
java.util.Hashtable hSources = new java.util.Hashtable();
if (o!=null && sDragdropResponseAction!=null && sDragdropResponseAction.equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT)){
	hSources = edu.xtec.qv.editor.util.QVDragdropResponseListControl.getSources(o, vAllTargets);
}
%>	

<script language="JavaScript">
<!--
var form = <%=sForm%>;
document.write('<INPUT type="hidden" name="show">');

function getCurrentTarget(){
	var target = form.p_dragdrop_response_targets.value;
	return target;
}

function updateSourcesTarget(){
	var sel_sources = form.p_dragdrop_response_sources;
	var target = getCurrentTarget();
	form.p_dragdrop_target_selected.value=target;
	if (target!=null && target!=""){
		sel_sources.style.display="inline";
		var sources = form["p_sources_"+target].value;
		var arr_sources = sources.split(",");
		for (i=0;i<sel_sources.length;i++){
			var opt_source = sel_sources.options[i];
			opt_source.selected=false;
			for (j=0;j<arr_sources.length;j++){
				if (arr_sources[j]==opt_source.value){
					opt_source.selected=true;
					break;
				}
			}
		}
	}else{
		sel_sources.style.display="none";
	}
}

function getDragdropResponses(){
	var target = getCurrentTarget();
	if (target!=null && target!=""){
		var sourcesTarget = "";
		var sources = form.p_dragdrop_response_sources;
		for (i=0;i<sources.length;i++){
			var srcOption = sources.options[i];
			if (srcOption.selected) {
				if (sourcesTarget=="") sourcesTarget +=srcOption.value;
				else sourcesTarget +=","+srcOption.value;
			}
		}
		form["p_sources_"+target].value=sourcesTarget;
	}
}

// -->
</script>
<SCRIPT src="scripts/move_layer.js"></SCRIPT>


<INPUT type="hidden" name="p_continue">
<DIV id='<%=sLayer%>' style="position:absolute; top:130; left:250; z-index:1000; width:250; height: 200; padding:5px;"  class='layer-box'>
<TABLE class='layer-box' border='0' cellpadding='5' cellspacing='5' width='200px' height='100%' style='height:100%'>
<TR>
	<TD>
		<TABLE border='0' cellpadding='0' cellspacing='0' style="width:100%">
		<TR>
			<TD valign="top" style="width:50;background:url('imatges/move_layer_off.gif') no-repeat;" onmousedown="move('<%=sLayer%>');" title="<%=qvb.getMsg("layer.move.tip")%>">&nbsp;</TD>
			<!--TD style="width:50" valign="top" title="<%=qvb.getMsg("layer.move.tip")%>">
				<A id="move_link" href="javascript:move('<%=sLayer%>');" class="layer-link"><IMG src="imatges/move_layer_off.gif" border="0" width="20" height="20"/></A>
			</TD-->
			<TD align="right" class="layer-title">
				<%=qvb.getMsg("item.dragdrop."+sType+".title")%>
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
  		<TD class="layer-text" valign="top"><%=qvb.getMsg("item.dragdrop.target.title")%></TD>
  		<TD>
<%
if (vAllTargets!=null && !vAllTargets.isEmpty()){ %>
			<SELECT name='p_dragdrop_response_targets' class='layer-form' size='1' onchange="updateSourcesTarget();">
				<OPTION value=""></OPTION>
<%	for(int i=0;i<vAllTargets.size();i++){
		edu.xtec.qv.editor.util.ListObject oTarget = (edu.xtec.qv.editor.util.ListObject)vAllTargets.elementAt(i);
%>
				<OPTION value="<%=oTarget.getIdent()%>" ><%=oTarget.getValue()%></OPTION>
<%}%>
			</SELECT>
<%	for(int i=0;i<vAllTargets.size();i++){
		edu.xtec.qv.editor.util.ListObject oTarget = (edu.xtec.qv.editor.util.ListObject)vAllTargets.elementAt(i);
%>
				<INPUT type="hidden" name="p_ident_targets" value="<%=oTarget.getIdent()%>"/>
				<INPUT type="hidden" name="p_sources_<%=oTarget.getIdent()%>" value="<%=hSources.get(oTarget.getIdent())%>"/>
<%}%>

<%} else {%>
					<%=qvb.getMsg("material.msg.noTargets")%>
<%}%>
  		</TD>
  	</TR>
  	<TR>
  		<TD valign="top"><INPUT type="text" size="10" name="p_dragdrop_target_selected" class='layer-form-disabled'/></TD>
  		<TD>
<%
if (vAllSources!=null && !vAllSources.isEmpty()){ %>
			<SELECT name='p_dragdrop_response_sources' class='layer-form' multiple size='5' onchange="getDragdropResponses();" style="display:none">
<%	for(int i=0;i<vAllSources.size();i++){
		edu.xtec.qv.editor.util.ListObject oSource = (edu.xtec.qv.editor.util.ListObject)vAllSources.elementAt(i);
%>
				<OPTION value="<%=oSource.getIdent()%>" ><%=oSource.getValue()%></OPTION>
<%}%>
			</SELECT>
<%} else {%>
			<%=qvb.getMsg("material.msg.noSources")%>
<%}%>
  		</TD>
  	</TR>
  	</TABLE>
<TR>	
	<TD class="layer-link" colspan="2" align="center">
		<A href="javascript:getDragdropResponses();<%=sForm%>.hide_layers.value='true';<%=sForm%>.p_list_name.value='<%=sListName%>';<%=sForm%>.<%=sListName%>.value='<%=sIdent%>';<%=sForm%>.p_list_type.value='<%=sType%>';enviar('<%=sDragdropResponseAction%>', <%=sForm%>);" title="<%=qvb.getMsg("item.response_label.ok.tip")%>" class="layer-link"><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("item.response_label.ok")%></A>
		&nbsp;|&nbsp;
		<A href="javascript:<%=sForm%>.p_<%=sType%>_action.value='';hide_dragdrop_response_layer();" title="<%=qvb.getMsg("item.response_label.cancel.tip")%>" class="layer-link"><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("item.response_label.cancel")%></A>
	</TD>
</TR>
</TABLE>
</DIV>
<SCRIPT>
<%if (sAction!=null && sAction.equalsIgnoreCase("p_"+sType+"_action") && sDragdropResponseAction!=null && 
	( sDragdropResponseAction.equalsIgnoreCase(IQVListControlBean.A_ADD_LIST_OBJECT) ||
	  sDragdropResponseAction.equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT))){%>
	show_dragdrop_response_layer(<%=sForm%>);
<%}else{%>
	hide_dragdrop_response_layer();
<%}%>
</SCRIPT>


