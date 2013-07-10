
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
	<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<TITLE>Editor de Quaderns Virtuals</TITLE>	
</HEAD>

<BODY>

<jsp:include page="testheader.jsp" flush="true" />

<% String sName=request.getParameter("username");
if (sName==null) sName="";%>
Hello <%=sName%>!!
