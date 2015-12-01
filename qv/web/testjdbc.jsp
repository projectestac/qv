<%@page session="false" contentType="text/html; charset=iso-8859-1"
%><html>
<head><title>ConnectionBeanBroker status</title></head>
<body>
<H2>ConnectionBean status</H2>
<p>Current time is: <%=new java.util.Date()%></p>
<%
	try{
		javax.naming.InitialContext ctx = new javax.naming.InitialContext();
		javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup("jdbc/pool/QVConnectionPoolDS");
		java.sql.Connection c = ds.getConnection();

		java.sql.PreparedStatement pstmt = c.prepareCall("select count(*) from assignacio");
		java.sql.ResultSet rs = pstmt.executeQuery();
		if (rs.next()) out.println("total:"+rs.getString(1));
		
		pstmt.close();
		c.close();
	} catch (Exception e){
		e.printStackTrace();
	}
%>	
</body>
</html>
