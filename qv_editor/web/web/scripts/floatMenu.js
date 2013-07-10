/*
Floating Menu script-  Roy Whittle (http://www.javascript-fx.com/)
Script featured on/available at http://www.dynamicdrive.com/
This notice must stay intact for use
*/

//Enter "frombottom" or "fromtop"
var verticalpos="frombottom"

function JSFX_FloatTopDiv()
{
   var startX = 11,
   startY = 52;
   var ns = (navigator.appName.indexOf("Netscape") != -1);
   var opera = (navigator.userAgent.indexOf("Opera")!=-1);
   if (ns){
   	startY = 58;
   }else if (opera){
    startX = 29;
    startY = 61;
   }
   var d = document;
   function ml(id)
   {
       var el=d.getElementById?d.getElementById(id):d.all?d.all[id]:d.layers[id];
       if(d.layers)el.style=el;
       el.sP=function(x,y){this.style.left=x;this.style.top=y;};
       el.x = startX;
       if (verticalpos=="fromtop")
       el.y = startY;
       else{
        el.y = ns ? pageYOffset + innerHeight - 6 : document.body.scrollTop + document.body.clientHeight;
        el.y -= startY;
       }
       return el;
   }
   window.stayTopLeft=function()
   {
       if (verticalpos=="fromtop"){
       var pY = ns ? pageYOffset : document.body.scrollTop;
       ftlObj.y += (pY + startY - ftlObj.y)/8;
       }
       else{
       var pY = ns ? pageYOffset + innerHeight - 6 : document.body.scrollTop + document.body.clientHeight;
	   var newPY = (pY - startY - ftlObj.y)/8;
	   var height = ns ? innerHeight : document.body.clientHeight;
       ftlObj.y += (pY - startY - ftlObj.y)/8;
       }
       ftlObj.sP(ftlObj.x, ftlObj.y);
       setTimeout("stayTopLeft()", 10);
   }
   ftlObj = ml("divStayTopLeft");
   stayTopLeft();
}


var ns = (navigator.appName.indexOf("Netscape") != -1);
var d = document;
function JSFX_FloatDiv(id, sx, sy)
{
	var el=d.getElementById?d.getElementById(id):d.all?d.all[id]:d.layers[id];
	var px = document.layers ? "" : "px";
	window[id + "_obj"] = el;
	if(d.layers)el.style=el;
	el.cx = el.sx = sx;el.cy = el.sy = sy;
	el.sP=function(x,y){this.style.left=x+px;this.style.top=y+px;};

	el.floatIt=function()
	{
		var pX, pY;
		pX = (this.sx >= 0) ? 0 : ns ? innerWidth : 
		document.documentElement && document.documentElement.clientWidth ? 
		document.documentElement.clientWidth : document.body.clientWidth;
		pY = ns ? pageYOffset : document.documentElement && document.documentElement.scrollTop ? 
		document.documentElement.scrollTop : document.body.scrollTop;
		if(this.sy<0) 
		pY += ns ? innerHeight : document.documentElement && document.documentElement.clientHeight ? 
		document.documentElement.clientHeight : document.body.clientHeight;
		this.cx += (pX + this.sx - this.cx)/8;this.cy += (pY + this.sy - this.cy)/8;
		this.sP(this.cx, this.cy);
		setTimeout(this.id + "_obj.floatIt()", 10);
	}
	return el;
}
