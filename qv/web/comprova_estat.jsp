<%@page session="false" contentType="text/html; charset=iso-8859-1"%>
<%
String sServer = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
String msg="OK";
edu.xtec.qv.db.LearningDatabaseAdmin oDB = new edu.xtec.qv.db.LearningDatabaseAdmin();
if (!oDB.checkConnection()) msg ="ERROR: database connection failed";
else{
	java.net.URL oURL = new java.net.URL(sServer+"/qv_editor/");
	try{
		oURL.openStream();
		oURL = new java.net.URL(sServer+"/qv_biblio/");
		try{
			oURL.openStream();
		}catch (Exception e){
			msg = "ERROR: access to qv container failed";
		}

	}catch (Exception e){
		msg = "ERROR: access to qv_editor container failed";
	}
}
%>
<%=msg%>
