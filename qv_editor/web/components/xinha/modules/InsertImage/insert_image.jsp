<%@page contentType="text/html; charset=ISO-8859-1" errorPage="error.html"%>
<jsp:useBean id="qvb" class="edu.xtec.qv.editor.beans.QVBean" scope="request" />
<%if(!qvb.init(request, session, response)){%><jsp:forward page="redirect.jsp"/><%}

String sPath = qvb.getQuadernResourcesURL()+"/";
%>

<!DOCTYPE html
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
  <title>Insert Image</title>

<script type="text/javascript" src="<%=qvb.getSetting("wysiwyg.base_url")%>popups/popup.js"></script>
<link rel="stylesheet" type="text/css" href="<%=qvb.getSetting("wysiwyg.base_url")%>popups/popup.css" />

<script type="text/javascript">


Xinha = window.opener.Xinha;
function i18n(str) {
  return (Xinha._lc(str, 'Xinha'));
}

function Init() {
  __dlg_translate('Xinha');
  __dlg_init(null,{width:410,height:400});
  // Make sure the translated string appears in the drop down. (for gecko)
  document.getElementById("f_align").selectedIndex = 1;
  document.getElementById("f_align").selectedIndex = 5;
  var param = window.dialogArguments;
  if (param["f_base"]) {
      document.getElementById("f_base").value = param["f_base"];
  }
  document.getElementById("f_url").value    = param["f_url"] ? param["f_url"] : "";
  document.getElementById("f_alt").value    = param["f_alt"] ? param["f_alt"] : "";
  document.getElementById("f_border").value = (typeof param["f_border"]!="undefined") ? param["f_border"] : "";
  document.getElementById("f_align").value  = param["f_align"] ? param["f_align"] : "";
  document.getElementById("f_vert").value   = (typeof param["f_vert"]!="undefined") ? param["f_vert"] : "";
  document.getElementById("f_horiz").value  = (typeof param["f_horiz"]!="undefined") ? param["f_horiz"] : "";
  if (param["f_url"]) {
      window.ipreview.location.replace(Xinha._resolveRelativeUrl(param.f_base, param.f_url));
   	  var imgList = document.getElementById("src");
      if (imgList!=null){
	      for (i=0;i<imgList.options.length;i++){
	      	var imgOption = imgList.options[i];
	      	if (imgOption.value.indexOf(param["f_url"])>=0) imgOption.selected=true;
	      	else imgOption.selected=false;
    	  }
      }
  }
  document.getElementById("f_url").focus();
}

function onOK() {
  var required = {
    "f_url": i18n("You must enter the URL")
  };
  for (var i in required) {
    var el = document.getElementById(i);
    if (!el.value) {
      alert(required[i]);
      el.focus();
      return false;
    }
  }
  // pass data back to the calling window
  var fields = ["f_url", "f_alt", "f_align", "f_border",
                "f_horiz", "f_vert"];
  var param = new Object();
  for (var i in fields) {
    var id = fields[i];
    var el = document.getElementById(id);
    param[id] = el.value;
  }
  __dlg_close(param);
  return false;
}

function onCancel() {
  __dlg_close(null);
  return false;
}

function onPreview() {
  var f_url = document.getElementById("f_url");
  var url = f_url.value;
  var base = document.getElementById("f_base").value;
  if (!url) {
    alert(i18n("You must enter the URL"));
    f_url.focus();
    return false;
  }
  window.ipreview.location.replace(Xinha._resolveRelativeUrl(base, url));
  return false;
}
</script>

</head>

<body class="dialog" onload="Init()">

<div class="title">Insert Image</div>
<!--- new stuff --->
<form action="" method="get">
<input type="hidden" name="base" id="f_base"/>
<table border="0" width="100%" style="padding: 0px; margin: 0px">
  <tbody>

  <tr>
    <td style="width: 10em; text-align: right">Image URL:</td>
    <td>
<%
		String[] sImages = qvb.getAssessmentResources(qvb.IMAGE_RESOURCE);
		if (sImages!=null && sImages.length>0){ %>
		<SELECT name='src' id="src" onchange="this.form.f_url.value=this.value;onPreview();">
			<OPTION value=""></OPTION>
			<% for(int i=0;i<sImages.length;i++){%>
			<OPTION value="<%=sPath+sImages[i]%>" ><%=sImages[i]%></OPTION>
			<%}%>
		</SELECT>
<%} else{%>There are not available images.<%}%>
    
    <input type="hidden" name="url" id="f_url" style="width:75%"
      title="Enter the image URL here" />
      <!-- button name="preview" onclick="return onPreview();"
      title="Preview the image in a new window">Preview</button-->
    </td>
  </tr>
  <tr>
    <td style="width: 7em; text-align: right">Alternate text:</td>
    <td><input type="text" name="alt" id="f_alt" style="width:100%"
      title="For browsers that don't support images" /></td>
  </tr>

  </tbody>
</table>

<br />

<fieldset style="float: left; margin-left: 5px;">
<legend>Layout</legend>

<div class="space"></div>

<div class="fl">Alignment:</div>
<select size="1" name="align" id="f_align"
  title="Positioning of this image">
  <option value=""                             >Not set</option>
  <option value="left"                         >Left</option>
  <option value="right"                        >Right</option>
  <option value="texttop"                      >Texttop</option>
  <option value="absmiddle"                    >Absmiddle</option>
  <option value="baseline" selected="1"        >Baseline</option>
  <option value="absbottom"                    >Absbottom</option>
  <option value="bottom"                       >Bottom</option>
  <option value="middle"                       >Middle</option>
  <option value="top"                          >Top</option>
</select>

<br />

<div class="fl">Border thickness:</div>
<input type="text" name="border" id="f_border" size="5"
title="Leave empty for no border" />

<div class="space"></div>

</fieldset>

<fieldset>
<legend>Spacing</legend>

<div class="space"></div>

<div class="fr">Horizontal:</div>
<input type="text" name="horiz" id="f_horiz" size="5"
title="Horizontal padding" />

<br />

<div class="fr">Vertical:</div>
<input type="text" name="vert" id="f_vert" size="5"
title="Vertical padding" />

<div class="space"></div>

</fieldset>
<br style="clear:all"/>
<div>
Image Preview:<br />
    <iframe name="ipreview" id="ipreview" frameborder="0" style="border : 1px solid gray;" 
	height="200" width="100%" src="<%=qvb.getSetting("wysiwyg.base_url")%>popups/blank.html"></iframe>
</div>
<div id="buttons">
<button type="button" name="ok" onclick="return onOK();">OK</button>
<button type="button" name="cancel" onclick="return onCancel();">Cancel</button>
</div>
</form>
</body>
</html>