<%@page contentType="text/html; charset=ISO-8859-1"%>
<%
	String sImage = request.getParameter("image");
	String sWidth = request.getParameter("width");
	String sHeight = request.getParameter("height");
	int iWidth, iHeight;
	try{
		iWidth=Integer.parseInt(sWidth);
		iHeight=Integer.parseInt(sHeight);
	}catch(Exception e){iWidth=0; iHeight=0;}
%>
<html>
<head>
	<title><%=sImage%></title>
<style>
.xy{
	font-family : Verdana, Geneva, Arial, Helvetica, sans-serif;
	font-size:15;
	font-weight:bold;
	border:0px;
}
.rule{
	font-family : Verdana, Geneva, Arial, Helvetica, sans-serif;
	font-size:10;
	border:0px;
	color: #CCCCCC;
}
</style>
</head>
<body style="margin:20;padding:0">

<div style="position:absolute;left:20;top:8;" name="grid-vertical">
	<table style="background-color:none;height:<%=iHeight+10%>" cellpadding="0" cellspacing="0">
	<tr>
<%for(int i=0;i<iWidth-49;i=i+50){%>
		<td style="background-color:none;border-right:dashed 1px #CCCCCC;width:50;text-align:right;vertical-align:top">
			<div class="rule"><%=i+50%></div>
			<div style="height:<%=iHeight+2%>" ></div>
			<div class="rule" style="vertical-align:bottom"><%=i+50%></div>
		</td>
<%}%>
	</tr>
 	</table>
</div>
<div style="position:absolute;left:0;top:20;" name="grid-horizontal">
	<table style="background-color:none;width:<%=iWidth+60%>" cellpadding="0" cellspacing="0">
<%for(int i=0;i<iHeight-49;i=i+50){%>
	<tr>
		<td style="border-bottom:dashed 1px #CCCCCC;width:20;vertical-align:bottom" class="rule"><%=i+50%></td>
		<td style="border-bottom:dashed 1px #CCCCCC;height:50">&nbsp;</td>
		<td style="border-bottom:dashed 1px #CCCCCC;width:40;vertical-align:bottom" class="rule">&nbsp;<%=i+50%></td>
	</tr>
<%}%>
 	</table>
</div>
<%if (sImage!=null){%>
<img src="<%=sImage%>" border="1" style="width:<%=sWidth%>;height:<%=sHeight%>;cursor:crosshair"/>
<%}%>
<CENTER>
<br>
<form name="Show">
x=<input type="text" name="MouseX" value="0" size="4" class="xy">&nbsp;&nbsp;
y=<input type="text" name="MouseY" value="0" size="4" class="xy">
</form>
</CENTER>
<script language="JavaScript1.2">
<!--

// Detect if the browser is IE or not.
// If it is not IE, we assume that the browser is NS.
var IE = document.all?true:false

// If NS -- that is, !IE -- then set up for mouse capture
if (!IE) document.captureEvents(Event.MOUSEMOVE)

// Set-up to use getMouseXY function onMouseMove
document.onmousemove = getMouseXY;

// Temporary variables to hold mouse x-y pos.s
var tempX = 0
var tempY = 0

// Main function to retrieve mouse x-y pos.s

function getMouseXY(e) {
  if (IE) { // grab the x-y pos.s if browser is IE
    tempX = event.clientX + document.body.scrollLeft-2
    tempY = event.clientY + document.body.scrollTop-2
  } else {  // grab the x-y pos.s if browser is NS
    tempX = e.pageX
    tempY = e.pageY
  }  
  // catch possible negative values in NS4
  if (tempX < 20){tempX = 0}else{tempX-=20}
  if (tempY < 20){tempY = 0}else{tempY-=20}
  if (tempX==0 || tempY==0 || tempX > <%=sWidth%> || tempY > <%=sHeight%>){tempX = 0;tempY = 0}
  // show the position values in the form named Show
  // in the text fields named MouseX and MouseY
  document.Show.MouseX.value = tempX
  document.Show.MouseY.value = tempY
  return true
}

//-->
</script>
</body>
</html>


