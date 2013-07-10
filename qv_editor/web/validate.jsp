<%

String sUser = request.getParameter("p_user");
String sPassword = request.getParameter("p_password");
String sRes = "KO";
try {
	if (sUser!=null && sPassword!=null){
		java.net.Socket socket = new java.net.Socket("www.xtec.net", 110);
		java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
		String sInput = br.readLine();
		java.io.DataOutputStream dos = new java.io.DataOutputStream(socket.getOutputStream());
		dos.writeBytes("USER "+sUser+"\r\n");
		sInput += br.readLine();
		dos.writeBytes("PASS "+sPassword+"\r\n");
		sInput += br.readLine();
		dos.writeBytes("QUIT\r\n");
		dos.close();
		br.close();
		socket.close();
		if (sInput.indexOf("ERR")<0){
			sRes="OK";
		}
	}else{
		sRes = "p_user or p_password parameter not found";
	}
}
catch (java.io.IOException e) {
}

%>

<%=sRes%>
