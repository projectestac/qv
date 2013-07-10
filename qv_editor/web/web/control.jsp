<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
	String sForm = qvb.getParameter("form");
	String sNameAddActionParam = qvb.getParameter("name_add_action_param", "add_item");
	String sNameDelActionParam = qvb.getParameter("name_del_action_param", "del_item");
	String sNameUpActionParam = qvb.getParameter("name_up_action_param", "up_item");
	String sNameDownActionParam = qvb.getParameter("name_down_action_param", "down_item");
	String sNameNumItems = qvb.getParameter("name_num_items", "num_items");
	int iNumItems = qvb.getIntParameter("value_num_items", 0);	
	String sParamValues = "";
	String[] vNameParam = qvb.getParameterValues("name_param");
	if (vNameParam!=null){
		String[] vValueParam = qvb.getParameterValues("value_param");
		for (int i=0;i<vNameParam.length;i++){
			String sName = vNameParam[i];
			String sValue = (vValueParam!=null && i<vValueParam.length)?vValueParam[i]:"";
			sParamValues += sForm+"."+sName+".value=\""+sValue+"\";";
		}
	}
%>
<!-- INICI botons de control -->
<TABLE cellpadding="5" cellspacing="0" border="0">
<TR>
	<TD>
		<A href='javascript:<%=sParamValues%><%=sForm+"."+sNameNumItems+".value="+(iNumItems+1)%>;enviar("<%=sNameAddActionParam%>", <%=sForm%>);' class='link'><IMG src='imatges/add_off.gif' alt='<%=qvb.getMsg("add_button")%>' border='0' width='10' height='10' ><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("add_button")%></A>
	</TD>
<%if (iNumItems>0){%>
	<TD width='5' class='link'>|</TD>
	<TD>
		<A href='javascript:enviar("<%=sNameDelActionParam%>", <%=sForm%>);' class='link'><IMG src='imatges/del_off.gif' alt='<%=qvb.getMsg("del_button")%>' border='0' width='10' height='10' ><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("del_button")%></A>
	</TD>
<%if (iNumItems>1){%>
	<TD width='5' class='link'>|</TD>
	<TD>
		<A href='javascript:enviar("<%=sNameUpActionParam%>", <%=sForm%>);' class='link'><IMG src='imatges/up_off.gif' alt='<%=qvb.getMsg("up_button")%>' border='0' width='10' height='10' ><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("up_button")%></A>
	</TD>
	<TD width='5' class='link'>|</TD>
	<TD>
		<A href='javascript:enviar("<%=sNameDownActionParam%>", <%=sForm%>);' class='link'><IMG src='imatges/down_off.gif' alt='<%=qvb.getMsg("down_button")%>' border='0' width='10' height='10' ><span style="text-decoration:none">&nbsp;</span><%=qvb.getMsg("down_button")%></A>
	</TD>
<%}%>	
<%}%>
</TR>
</TABLE>
<!-- FI botons de control -->
	