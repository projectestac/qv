<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" import="edu.xtec.qv.editor.beans.IQVListControlBean"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
edu.xtec.qv.editor.beans.IQVListControlBean qvsb=(edu.xtec.qv.editor.beans.IQVListControlBean)qvb.getSpecificBean();

String sType = IQVListControlBean.METADATA_TYPE;
String sLayer = "metadataLayer";
String sForm = qvb.getParameter("p_form");
String sListName=qvb.getParameter("p_list_name");
String sAction = qvb.getParameter("action");
String sMetadataAction =qvb.getParameter("p_metadata_action");

String sIdent = qvb.getParameter(sListName);
edu.xtec.qv.qti.QTIMetadatafield o = (edu.xtec.qv.qti.QTIMetadatafield)qvsb.getObject(sType, sIdent);
String sLabel = "";
String sEntry = "";
if (o!=null && sMetadataAction!=null && sMetadataAction.equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT) && !"cancel_action".equals(sAction)){
	sLabel = o.getFieldLabel();
	sEntry = o.getFieldEntry();
}
%>	

<DIV id='<%=sLayer%>' style="position:absolute; top:150; left:350; width:400; z-index:1000; padding:5px;"  class='layer-box'>
<TABLE class='layer-box' border='0' cellpadding='5' cellspacing='5' width='400px'>
<TR>	
	<TD colspan="3" align="right" class="layer-title" ><%=qvb.getMsg("metadata.title")%></TD>
</TR>
<TR>
  <TD width='30'></TD>
  <TD width="100%">
  	<TABLE border='0' cellpadding='5' cellspacing='5'>
  	<TR>
  		<TD class="layer-text" valign="top"><%=qvb.getMsg("metadata.label")%></TD>
  		<TD valign="top">
  			<INPUT type="text" name="p_label" value="<%=sLabel%>" class='layer-form'  size="15"/>
  		</TD>
  	</TR>
  	<TR>
  		<TD class="layer-text" valign="top"><%=qvb.getMsg("metadata.entry")%></TD>
  		<TD valign="top">
  			<INPUT type="text" name="p_entry" value="<%=sEntry%>" class='layer-form' size="33"/>
  		</TD>
  	</TR>
  	</TABLE>
  </TD>
  <TD width='30' height='50%'></TD>
</TR>
<TR>	
	<TD class="layer-link" colspan="3" align="center">
		<A href="javascript:<%=sForm%>.hide_layers.value='true';<%=sForm%>.p_list_name.value='<%=sListName%>';<%=sForm%>.<%=sListName%>.value='<%=sIdent%>';<%=sForm%>.p_list_type.value='<%=sType%>';enviar('<%=sMetadataAction%>', <%=sForm%>);" class="layer-link" title="<%=qvb.getMsg("metadata.action.ok.tip")%>" ><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("metadata.action.ok")%></A>
		&nbsp;|&nbsp;
		<A href="javascript:hide_metadata_layer();enviar('cancel_action', <%=sForm%>);" class="layer-link" title="<%=qvb.getMsg("metadata.action.cancel.tip")%>" ><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("metadata.action.cancel")%></A>
	</TD>
</TR>
</TABLE>
</DIV>
<SCRIPT>
<%
if (sAction!=null && sAction.equalsIgnoreCase("p_metadata_action") && sMetadataAction!=null && 
	  ( sMetadataAction.equalsIgnoreCase(IQVListControlBean.A_ADD_LIST_OBJECT) ||
	  sMetadataAction.equalsIgnoreCase(IQVListControlBean.A_SET_LIST_OBJECT))){%>
	<%=sForm%>.p_label.focus();
	show_metadata_layer(<%=sForm%>);
<%}else{%>
	hide_metadata_layer();
<%}%>
</SCRIPT>
