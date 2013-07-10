<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html" %>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}
	String sOrdrePregunta = qvb.getParameter("ordre_pregunta");
	String sOrdreIntern = ((edu.xtec.qv.editor.beans.QVPreguntaBean)qvb.getSpecificBean()).getItemOrder(qvb.getPregunta().getIdent());
	if (sOrdrePregunta==null || sOrdrePregunta.trim().length()<1 || sOrdrePregunta.trim().equalsIgnoreCase("null")){
		sOrdrePregunta = sOrdreIntern;
	}
%>

<!-- INICI FEEDBACK -->
<TABLE class='edit-box' width="100%" cellpadding="5" cellspacing="0" border="0">
<TR>
	<TD width="15%" class="edit-text" title="<%=qvb.getMsg("item.order.tip")%>"><%=qvb.getMsg("section.order")%></TD>
	<TD>
		<SELECT class='edit-form' name="ordre_pregunta">
			<OPTION value="no_random" <%= sOrdrePregunta.equals("no_random")?"selected":""%> ><%=qvb.getMsg("item.order.norandom")%></OPTION>
			<OPTION value="random" <%=sOrdrePregunta.equals("random")?"selected":""%> ><%=qvb.getMsg("item.order.random")%></OPTION>
		</SELECT>
	</TD>
	<TD class="edit-text"></TD>	
</TR>

</TABLE>
<!-- FI ORDER_PREGUNTA -->
