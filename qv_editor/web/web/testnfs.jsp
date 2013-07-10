<%@page contentType="text/html; charset=ISO-8859-1"%>
<HTML>
<HEAD>
<TITLE>Test NFS</TITLE>
<script>
	function getURL(form) {
		if (form.p_username.value!='' && form.p_qv_name.value!=''){
			form.quadernURL.value = "http://clic.edu365.com/quaderns/"+form.p_username.value+"/"+form.p_qv_name.value+"/"+form.p_qv_name.value+".xml";
			form.quadernURL.value = form.quadernURL.value.toLowerCase();
		}
	}
</script>

</HEAD>

<%
	String sNom = request.getParameter("nom");
	//String sURL = "/windows/e/dev/qv/qv_editor/src/webapp/quaderns/sarjona/testnfs/";
	String sURL = "/web/quaderns/sarjona/testnfs/";
	if (sNom!=null && sNom.length()>0){
		sURL = sURL+sNom;
		if ("add".equalsIgnoreCase(request.getParameter("accio"))){
			try{
				java.io.File fFile = new java.io.File(sURL);
				if (!fFile.exists()) fFile.mkdirs();
				sURL = sURL+"/"+sNom+".xml";
				java.io.FileOutputStream fos=new java.io.FileOutputStream(sURL);
				java.io.Writer pw = new java.io.BufferedWriter(new java.io.OutputStreamWriter(fos, "ISO-8859-1"));
				//PrintWriter pw=new PrintWriter(fos);
				String sXML="text\nlinia1\nlinia2\nlinia3\nlínia4\nblablablablabla";
				//pw.print(sXML); 
				pw.write(sXML); 
				pw.flush();
				fos.getFD().sync();
				pw.close();
				fos.flush();
				fos.close();
			}
			catch (java.io.IOException e){
				System.out.println("EXCEPCIO guardant el fitxer '"+sURL+"' --> "+e);
			}			
		} else if ("del".equalsIgnoreCase(request.getParameter("accio"))){	
			try{
				java.io.File fFile = new java.io.File(sURL);
				if (fFile!=null){
					// Eliminar el contingut del directori
					java.io.File[] files = fFile.listFiles();
					if (files!=null){
						for (int i=0;i<files.length;i++){
							java.io.File tmpFile = files[i];
							tmpFile.delete();
						}
					}
					// Eliminar el directori
					fFile.delete();
				}	
			} catch (Exception e){
				System.out.println("EXCEPCIO eliminant el directori '"+sURL+"'");
			}			
		}
	}
	
	
%>

<BODY>
  <FORM method="POST">
	<INPUT type="hidden" name="accio" value="" size="100"/>
  	<TABLE cellpadding="5" cellspacing="0" border="0">
  	<TR>					
  		<TD>Nom: </TD>
  		<TD><INPUT type="text" name="nom" />&nbsp;</TD>
  		<TD><INPUT type="submit" value="Crea" onclick="this.form.accio.value='add';"/></TD>
  		<TD><INPUT type="submit" value="Esborra" onclick="this.form.accio.value='del';"/></TD>
<%
	out.println("url="+sURL);
	if (sURL.endsWith(".xml")){
		java.io.Reader reader = new java.io.InputStreamReader(new java.io.FileInputStream(sURL), "ISO-8859-1");
		out.println(reader.read());
		reader.close();
	}
%>  		
  	</TR>
	</TABLE>
  </FORM>
</BODY>

</HTML>