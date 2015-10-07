<%@page contentType="text/html; charset=ISO-8859-1"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.admin.beans.QVLoginBean" scope="request" /><%
if(!qvb.init(request, session, response)){
%>
  <jsp:forward page="index.jsp"/><%
}%>
<jsp:include page="header.jsp" flush="true" />
<link rel="stylesheet" href="thirdparty/bootstrap/css/bootstrap.min.css">
<header><h1>Quaderns Virtuals</h1></header>

<%
if (qvb.isLoginIncorrect()) {
%><div class="alert alert-danger" role="alert">L'usuari i/o contrasenya introduïts són incorrectes</div><%
}
%>
<div class="panel panel-info">
<div class="panel-body bg-info">

    <form name="loginForm" method="POST" action="login.jsp">
      <input type="hidden" name="page" value="index">
      <input type="hidden" name="action" value="login">
      <div class="form-group">
        <label for="p_username">Nom d'usuari</label>
        <input type="text" class="form-control" id="p_username" name="p_username" placeholder="Usuari">
      </div>
      <div class="form-group">
        <label for="p_password">Contrasenya</label>
        <input type="password" class="form-control" id="p_password" placeholder="Contrasenya" name="p_password">
      </div>
      <button type="submit" class="btn btn-default">Envia</button>
    </form>
</div>
</div>
<jsp:include page="footer.jsp" flush="true" />

