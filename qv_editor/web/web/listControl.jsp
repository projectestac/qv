<%@page contentType="text/html; charset=ISO-8859-1"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.IQVListControlBean qvsb=(edu.xtec.qv.editor.beans.IQVListControlBean)qvb.getSpecificBean();

String sForm = qvb.getParameter("p_form");
String sListName=qvb.getParameter("p_list_name");
String sType=qvb.getParameter("p_list_type");
String sTitle=qvb.getParameter("p_list_title");
String sTip=qvb.getParameter("p_list_tip","");
String sDefault = qvb.getParameter("p_default_ident_list");
String sListSize = qvb.getParameter("p_list_size", "3");
String sListWidth =qvb.getParameter("p_list_width", "450");
String sCurrent = qvsb.getCurrentListObject(sType);
String sHelpPage = qvb.getParameter("p_help_page", "");

String sListActionName = "p_"+sType+"_action";
String sAction =qvb.getParameter(sListActionName);
String sAddAction = edu.xtec.qv.editor.beans.IQVListControlBean.A_ADD_LIST_OBJECT;
String sDelAction = edu.xtec.qv.editor.beans.IQVListControlBean.A_DEL_LIST_OBJECT;
String sSetAction = edu.xtec.qv.editor.beans.IQVListControlBean.A_SET_LIST_OBJECT;
String sUpAction = edu.xtec.qv.editor.beans.IQVListControlBean.A_UP_LIST_OBJECT;
String sDownAction = edu.xtec.qv.editor.beans.IQVListControlBean.A_DOWN_LIST_OBJECT;
%>
<!-- INICI llista amb controls -->
<SCRIPT>
<!--
	if (<%=sForm%>.p_list_name==null){
		document.write('<INPUT type="hidden" name="p_list_name" />');
	}
	if (<%=sForm%>.p_list_type==null){
		document.write('<INPUT type="hidden" name="p_list_type" />');
	}
	if (<%=sForm%>.<%=sListActionName%>==null){
		document.write('<INPUT type="hidden" name="<%=sListActionName%>" value="<%=sAction!=null?sAction:""%>" />');
	}
-->
</SCRIPT>
<TABLE width="100%" cellpadding="0" cellspacing="0" border="0">
<TR>
	<TD width="80" valign="top" class="edit-text" title="<%=sTip%>" >
		<%=sTitle%>
	</TD>
	<TD title="<%=sTip%>" >
		<SELECT name="<%=sListName%>" size="<%=sListSize%>" width="<%=sListWidth%>" style="width: <%=sListWidth%>px;" class="edit-form" title="<%=sTip%>" >
<%
java.util.Enumeration enumList=qvsb.getListObjects(sType).elements();
while(enumList.hasMoreElements()){
	edu.xtec.qv.editor.util.ListObject o = (edu.xtec.qv.editor.util.ListObject)enumList.nextElement();
	String sIdent=o.getIdent();
	String sValue=o.getValue();
	if ("true".equals(qvb.getParameter("p_translate_value"))){
		sValue=qvb.getMsg(sValue);
	}
	
%>	
			<OPTION value="<%=sIdent%>" <%=sCurrent.equalsIgnoreCase(sIdent)?"selected":""%> ><%=sValue%></OPTION>
<%}%>
		</SELECT>
		<jsp:include page="help.jsp" flush="true">
			<jsp:param name="p_help_page" value="<%=sHelpPage%>" />
		</jsp:include>
	</TD>
</TR>
<TR>
	<TD/>
	<TD>
		<TABLE cellpadding="3" cellspacing="0" border="0">
		<TR>
<%if (qvb.getParameter("hide_add")==null){%>		
			<TD class="layer-off-text">
				<A href='#' onclick='if (<%=sDefault!=null%>){var index=<%=sForm%>.<%=sListName%>.length;<%=sForm%>.<%=sListName%>[index]=new Option("","<%=sDefault%>"); <%=sForm%>.<%=sListName%>[index].selected=true;}<%=sForm%>.hide_layers.value="false";<%=sForm%>.p_list_name.value="<%=sListName%>";<%=sForm%>.p_list_type.value="<%=sType%>";<%=sForm%>.<%=sListActionName%>.value="<%=sAddAction%>"; enviar("<%=sListActionName%>", <%=sForm%>);' title="<%=qvb.getMsg("list.action.add.tip")%>" class='edit-link'><IMG src='imatges/add_off.gif' border='0' width='10' height='10' ><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("list.action.add")%></A>
			</TD>
			<TD width='5' class='edit-link'>|</TD>
<%}%>
<%if (qvb.getParameter("hide_del")==null){%>
			<TD class="layer-off-text">
				<A href='#' onclick='if (not_null(<%=sForm%>.<%=sListName%>.value) && confirm("<%=qvb.getMsg("list.action.del.confirm")%>")) {<%=sForm%>.hide_layers.value="true";<%=sForm%>.p_list_name.value="<%=sListName%>";<%=sForm%>.p_list_type.value="<%=sType%>"; <%=sForm%>.<%=sListActionName%>.value="<%=sDelAction%>"; enviar("<%=sListActionName%>", <%=sForm%>);}' title="<%=qvb.getMsg("list.action.del.tip")%>" class='edit-link'><IMG src='imatges/del_off.gif' border='0' width='10' height='10' ><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("list.action.del")%></A>
			</TD>
			<TD width='5' class='edit-link'>|</TD>
<%}%>
<%if (qvb.getParameter("hide_set")==null){%>
			<TD>
				<A href='#' onclick='if(not_null(<%=sForm%>.<%=sListName%>.value)){<%=sForm%>.hide_layers.value="false";<%=sForm%>.p_list_name.value="<%=sListName%>";<%=sForm%>.p_list_type.value="<%=sType%>";<%=sForm%>.<%=sListActionName%>.value="<%=sSetAction%>"; enviar("<%=sListActionName%>", <%=sForm%>);}' title="<%=qvb.getMsg("list.action.set.tip")%>" class='edit-link'><IMG src='imatges/set_off.gif' border='0' width='10' height='11' ><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("list.action.set")%></A>
			</TD>
<%}%>
<%if (qvb.getParameter("hide_up")==null){%>
			<TD width='5' class='edit-link'>|</TD>
			<TD>
				<A href='#' onclick='if (not_null(<%=sForm%>.<%=sListName%>.value)){<%=sForm%>.hide_layers.value="true";<%=sForm%>.p_list_name.value="<%=sListName%>";<%=sForm%>.p_list_type.value="<%=sType%>"; <%=sForm%>.<%=sListActionName%>.value="<%=sUpAction%>"; enviar("<%=sListActionName%>", <%=sForm%>);}' title="<%=qvb.getMsg("list.action.up.tip")%>" class='edit-link'><IMG src='imatges/up_off.gif' border='0' width='10' height='10' ><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("list.action.up")%></A>
			</TD>
			<TD width='5' class='edit-link'>|</TD>
<%}%>
<%if (qvb.getParameter("hide_down")==null){%>
			<TD>
				<A href='#' onclick='if (not_null(<%=sForm%>.<%=sListName%>.value)){<%=sForm%>.hide_layers.value="true";<%=sForm%>.p_list_name.value="<%=sListName%>";<%=sForm%>.p_list_type.value="<%=sType%>"; <%=sForm%>.<%=sListActionName%>.value="<%=sDownAction%>"; enviar("<%=sListActionName%>", <%=sForm%>);}' title="<%=qvb.getMsg("list.action.down.tip")%>" class='edit-link'><IMG src='imatges/down_off.gif' border='0' width='10' height='10' ><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("list.action.down")%></A>
			</TD>
<%}%>
		</TR>
		</TABLE>
	</TD>
</TR>
</TABLE>
<%
boolean bHideLayers = qvb.getParameter("hide_layers")!=null && qvb.getParameter("hide_layers").equalsIgnoreCase("true");
if (!bHideLayers && sAction!=null && sAction.length()>0){
	if (sType!=null && sType.equalsIgnoreCase(edu.xtec.qv.editor.beans.IQVListControlBean.METADATA_TYPE)){%>
		<jsp:include page="metadata.jsp" flush="true">
			<jsp:param name="p_list_name" value="<%=sListName%>"/>
		</jsp:include>
	<%}else if (sType!=null && sType.equalsIgnoreCase(edu.xtec.qv.editor.beans.IQVListControlBean.MATERIAL_TYPE)){%>
		<jsp:include page="material.new.jsp" flush="true">
			<jsp:param name="p_list_name" value="<%=sListName%>"/>	
		</jsp:include>
	<%}else if (sType!=null && sType.equalsIgnoreCase(edu.xtec.qv.editor.beans.IQVListControlBean.ORDERED_RESPONSE_TYPE)){%>
		<jsp:include page="ordered_response.jsp" flush="true">
			<jsp:param name="p_list_name" value="<%=sListName%>"/>	
		</jsp:include>
	<%}else if (sType!=null && sType.equalsIgnoreCase(edu.xtec.qv.editor.beans.IQVListControlBean.CLOZE_RESPONSE_TYPE)){%>
		<jsp:include page="cloze_response.jsp" flush="true">
			<jsp:param name="p_list_name" value="<%=sListName%>"/>	
		</jsp:include>
	<%}else if (edu.xtec.qv.editor.beans.IQVListControlBean.TARGET_TYPE.equals(sType)){%>
		<jsp:include page="rarea_item.jsp" flush="true">
			<jsp:param name="p_list_type" value="<%=sType%>"/>
			<jsp:param name="p_list_name" value="<%=sListName%>"/>	
		</jsp:include>
	<%}else if (edu.xtec.qv.editor.beans.IQVListControlBean.SOURCE_TYPE.equals(sType)){%>
		<jsp:include page="source_item.jsp" flush="true">
			<jsp:param name="p_list_name" value="<%=sListName%>"/>	
		</jsp:include>
	<%}else if (edu.xtec.qv.editor.beans.IQVListControlBean.HOTSPOT_TYPE.equals(sType)){%>
		<jsp:include page="rarea_item.jsp" flush="true">
			<jsp:param name="p_list_type" value="<%=sType%>"/>
			<jsp:param name="p_list_name" value="<%=sListName%>"/>	
		</jsp:include>
	<%}else if (edu.xtec.qv.editor.beans.IQVListControlBean.BOUNDED_TYPE.equals(sType)){%>
		<jsp:include page="bounded_response.jsp" flush="true">
			<jsp:param name="p_list_type" value="<%=sType%>"/>
			<jsp:param name="p_list_name" value="<%=sListName%>"/>	
		</jsp:include>
	<%}else if (edu.xtec.qv.editor.beans.IQVListControlBean.DOT_RESPONSE_TYPE.equals(sType)){%>
		<jsp:include page="dot_response.jsp" flush="true">
			<jsp:param name="p_list_type" value="<%=sType%>"/>
			<jsp:param name="p_list_name" value="<%=sListName%>"/>	
		</jsp:include>
	<%}else if (edu.xtec.qv.editor.beans.IQVListControlBean.DRAGDROP_RESPONSE_TYPE.equals(sType)){%>
		<jsp:include page="dragdrop_response.jsp" flush="true">
			<jsp:param name="p_list_type" value="<%=sType%>"/>
			<jsp:param name="p_list_name" value="<%=sListName%>"/>	
		</jsp:include>
	<%}else if (edu.xtec.qv.editor.beans.IQVListControlBean.DRAGDROP_POSITION_TYPE.equals(sType)){%>
		<jsp:include page="dragdrop_position.jsp" flush="true">
			<jsp:param name="p_list_type" value="<%=sType%>"/>
			<jsp:param name="p_list_name" value="<%=sListName%>"/>	
		</jsp:include>
	<%}
}%>

<!-- FI llista amb controls -->
	