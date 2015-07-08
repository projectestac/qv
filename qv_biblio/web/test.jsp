<%@page session="false" contentType="text/html; charset=iso-8859-1"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>Quaderns Virtuals</title>
  <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
</head>

<body>
<%
	edu.xtec.util.db.ConnectionBeanProvider pool = null;
	java.sql.Connection oConnection = null;
	String sConfig = request.getParameter("configdb");
	if (sConfig!=null){
		java.io.BufferedReader brConfig = new java.io.BufferedReader(new java.io.StringReader(sConfig));
		String sLine = null;
		java.util.HashMap oMap = new java.util.HashMap();
		while ((sLine=brConfig.readLine())!=null){
			int iEnd = sLine.indexOf("=");
			if (iEnd>0){
				oMap.put(sLine.substring(0, iEnd).trim(), sLine.substring(iEnd+1).trim());
			}
		}
		
		if (oMap!=null & !oMap.isEmpty()){
			try{
				String sDifferent = request.getParameter("different");
				if (sDifferent!=null){
					System.out.println("Connexió directa (sense dbconn.jar)");
					String sDriver = (String)oMap.get("dbDriver");
					if ("JNDI".equalsIgnoreCase(sDriver)){
						javax.naming.InitialContext ctx = new javax.naming.InitialContext();
						String sURL = (oMap.containsKey("dbContext")?(String)oMap.get("dbContext")+"/":"")+(String)oMap.get("dbServer");
						javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup(sURL);
			            oConnection = ds.getConnection();
					}else{
						Class.forName(sDriver); 
			            oConnection = java.sql.DriverManager.getConnection((String)oMap.get("dbServer"),(String)oMap.get("dbLogin"),(String)oMap.get("dbPassword"));					
					}
				}else{
					pool = edu.xtec.util.db.ConnectionBeanProvider.getConnectionBeanProvider(true, oMap);
					//pool = edu.xtec.util.db.ConnectionBeanProvider.getConnectionBeanProvider(true, edu.xtec.qv.biblio.db.BiblioDatabase.getDBProperties());
					edu.xtec.util.db.ConnectionBean oConnectionBean = pool.getConnectionBean();
					if (oConnectionBean!=null)oConnection=oConnectionBean.getConnection();					
				}
								
				if (oConnection!=null){
%>
					<br>Connexió a base de dades realitzada amb èxit!	<br>
<%
					oConnection.close();
				}else{ %>
					Connexió a base de dades nula.<br><br>
					<%=pool!=null?pool.getInfo():"null"%>
<%  			}
				
				
			}catch (Exception e){
				System.out.println(oMap);
%>
				EXCEPTION: <%=e.toString()%><br>
				<%e.printStackTrace();%>
				Map: <%=oMap%>
<%				
			}
		}
	}
%>	
  <FORM method="post">
	<TEXTAREA name="configdb" cols="100" rows="10"><%=sConfig!=null?sConfig.trim():""%></TEXTAREA><br>
	<INPUT type="checkbox" name="different" <%=request.getParameter("different")!=null?"checked":""%>/>Connexió directa (sense dbconn.jar)<br>
	<INPUT type="submit" value="Envia"/>
  </FORM>
  


</body>
</html>
