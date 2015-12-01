
function openWindow(url, popW, popH){
    var w = 800, h = 600, x0=0, y0=0;
    if (document.all || document.layers) {
      w = screen.availWidth;
      h = screen.availHeight;
    }
    //var popW = 788, popH = 540;
    if(popW>(w-12))
      popW=w-12;
    if(popH>(h-28))
      popH=h-28;
    var leftPos = 0, topPos = 0;
    if(w>800 && h>600){
      leftPos = (w-popW)/2;
      topPos = (h-popH)/2;
    }
    window.open(url,'qv','toolbar=yes,directories=false,scrollbars=yes,resizable=yes,width=' + popW + ',height=' + popH + ',top=' + topPos + ',left=' + leftPos+ ',screenY=' + topPos + ',screenX=' + leftPos);
}

function checkBrowserSupported(){
	if (!isBrowserSupported())
            document.location.href="browser_alert.htm";
        //alert("Aquest navegador no és compatible amb aquesta prova. Si utilitzes MAC, prova amb Netscape.");
}

function isBrowserSupported(){
	var agt=navigator.userAgent.toLowerCase();

	var is_nav  = ((agt.indexOf('mozilla')!=-1) && (agt.indexOf('spoofer')==-1)
                && (agt.indexOf('compatible') == -1) && (agt.indexOf('opera')==-1)
                && (agt.indexOf('webtv')==-1) && (agt.indexOf('hotjava')==-1));
	var is_major = parseInt(navigator.appVersion);
	var is_nav6up = (is_nav && (is_major >= 5));

	var supported=!(agt.indexOf("mac")>=0 && !is_nav6up);
	return supported;
}

