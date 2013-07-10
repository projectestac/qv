var enContrasenya       = false;
var v_connectat         = "Connecta't!";

var Ns4 = false; var Ns5 = false; var Ns6 = false; var Ns4up = false;
var Ie4 = false; var Ie5 = false; var Ie6 = false; var Ie4up = false;

if (navigator.appName.indexOf("Netscape") != -1) {
  if (navigator.userAgent.indexOf("Netscape6") != -1) Ns6 = true;
  else if (parseInt(navigator.appVersion) >= 5) Ns5 = true;
  else if (parseInt(navigator.appVersion) >= 4) Ns4 = true;
  if (Ns4 || Ns5 || Ns6) Ns4up = true;
}
else if (navigator.appName.indexOf("Explorer") != -1) {
  if (navigator.userAgent.indexOf("MSIE 6") != -1) Ie6 = true;
  if (navigator.userAgent.indexOf("MSIE 5") != -1) Ie5 = true;
  else if (parseInt(navigator.appVersion) >= 4) Ie4 = true;
  if (Ie4 || Ie5 || Ie6) Ie4up = true;
}

var Shift = false, Ctrl = false, Alt = false;
var SHIFT_KEYCODE = 16, CTRL_KEYCODE = 17, ALT_KEYCODE = 18;

function keyDown(evt) {
  var keycode;
  if (!enContrasenya) {
    return;
  }
  if (Ns4up) {
    keycode = evt.which;
    if (keycode == SHIFT_KEYCODE && (Ns5 || Ns6)) Shift = true;
    if (keycode == CTRL_KEYCODE && (Ns5 || Ns6)) Ctrl = true;
    if (keycode == ALT_KEYCODE && (Ns5 || Ns6)) Alt = true;
  }
  else if (Ie4up) {
    keycode = event.keyCode;
    Shift = event.shiftKey;
    Ctrl = event.ctrlKey;
    Alt = event.altKey;
  }
  if (keycode == 13) {
    if (!Shift && !Ctrl && !Alt) {
      document.login.submit();
      return;
    }
  }
}
document.onkeydown = keyDown;
if (Ns4up) document.captureEvents(Event.KEYDOWN);

function pintaDataEnCatala() {
  var messos = new Array("gener", "febrer", "març", "abril", "maig", "juny", "juliol" , "agost", "setembre", "octubre", "novembre", "desembre");
  var d = new Date();
  document.write("<span class='data-text'>");
  document.write(d.getDate().toString() +" "+messos[d.getMonth()]+" "+d.getFullYear().toString());
  document.write("</span>");
}

function getCookie(Name) {
  var search = Name + "="
  if (document.cookie.length > 0) { // if there are any cookies
    offset = document.cookie.indexOf(search)
    if (offset != -1) { // if cookie exists
      offset += search.length
      // set index of beginning of value
      end = document.cookie.indexOf(";", offset)
      // set index of end of cookie value
      if (end == -1)
      end = document.cookie.length
      return unescape(document.cookie.substring(offset, end))
    }
  }
}
var co = getCookie("usuari-edu365");  // obtenim el username

function pintaLoginForm() {
  document.writeln(
'      <form action="/pls/edu365/edu_sec_plsql_2.login" method="post" name="login">\n'+
'      <td rowspan="2" bgcolor="#015C96"><font face="Courier New, Courier, mono" size="2"><img src="/imgbarra/usuari_2.gif" width="60" height="13" ><br>\n'+
'        <input type="hidden" name="p_url" value="'+location+'">\n'+
'        <input type="text" name="p_username" maxlength="8" size="8" style="font-size:10pt;">\n'+
'        </font></td>\n'+
'      <td rowspan="2" bgcolor="#015C96"><font face="Courier New, Courier, mono" size="2"><img src="/imgbarra/contrasenya_2.gif" width="68" height="13"><br>\n'+
'        <input type="password" name="p_password" maxlength="8" size="8" style="font-size:10pt;" onFocus="enContrasenya=true;" onBlur="enContrasenya=false;">\n'+
'        <a href="javascript:document.login.submit()" onMouseOver="window.status=v_connectat; return true" onMouseOut="window.status=self.defaultStatus; return true"><img src="/imgbarra/fletxa_barra.gif" width="17" height="16" border="0" alt="'+ v_connectat+'"></a></font></td></form>');
}

function pintaUsername() {
  document.writeln(
'      <td rowspan="2" bgcolor="#015C96" valign="middle"><font face="Courier New, Courier, mono" size="2">\n'+
'        <font size="3" face="Arial, Helvetica, sans-serif"><b><font color="#FFFFFF">'+ co.toString() +'</font></b></font></font></td>\n'+
'      <td rowspan="2" bgcolor="#015C96" valign="middle">\n'+
'        <div align="right"><a href="/pls/edu365/edu_sec_plsql_2.logout?p_url='+escape(location)+'"><img src="/imgbarra/fi_sessio.gif" width="57" height="29" border="0" alt="Finalitzar sessió" onMouseOver="window.status=\'Finalitzar sessió\'; return true" onMouseOut="window.status=self.defaultStatus; return true"></a><img src="/imgbarra/shim.gif" width="20" height="29" border="0"></div>\n'+
'      </td>');
}



