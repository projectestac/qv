function getQueryParam(name){
    var value = "";
    var location = new String(document.location);
	var iQuery = location.indexOf("?");
	if (iQuery>=0){
    	var query = location.substring(iQuery+1);
    	var pairs = query.split(/[;&]/);
   		for ( var i = 0; i < pairs.length; i++ ) {
   			var key = pairs[i].split('=');
   			if (key[0]==name){
   				value = key[1];
   				break; 
   			}
   		}
  }
  if (value!=""){
    if (value.indexOf("#")>=0) value=new String(value).substring(0, value.indexOf("#")); 
  }
	return value;
}

function isNull(o){
	return o==null || o.length==0;
}

function endsWidth(text, end){
  var bEnds = false;
  if (text!=null){
    bEnds = end==text.substring(text.length-1, text.length);
  }
  return bEnds;
}

function includeScript(file){
  var path = getQueryParam('js');
  if (isNull(path)){
    path="scripts/";
  }
  if (!endsWidth(path, '/')) path+="/";
  document.writeln('<SCRIPT type="text/javascript" src="'+path+file+'"></SCRIPT>');
}

function includeCSS(skin, lang){
	if (notNull(getQueryParam('skin'))){
	  skin=getQueryParam('skin');
	  //if (skin!='default' && skin!='infantil' && skin!='formal') skin='default';
	}
	var base="css/";
	if (notNull(getQueryParam('css'))){
	  base=getQueryParam('css');
	  if (!endsWidth(base, '/')) path+="/";
	}
	document.write('<LINK type="text/css" rel="stylesheet" href="'+base+skin+'/'+skin+'.css"/>');
	if (notNull(getQueryParam('lang'))){
	  lang=getQueryParam('lang');
	}
 	if (lang==null || lang=='null') lang='ca';
	    
    document.write('<LINK type="text/css" rel="stylesheet" href="'+base+skin+'/'+skin+'_'+lang+'.css"/>');
	if (notNull(getQueryParam('server'))){
	  document.write('<LINK type="text/css" rel="stylesheet" href="'+base+skin+'/'+skin+'_server.css"/>');
	}
    document.writeln('<LINK rel="icon" type="image/x-icon" href="'+base+skin+'/image/favicon.ico"/>');
    document.writeln('<LINK rel="shortcut icon" type="image/x-icon" href="'+base+skin+'/image/favicon.ico"/>');
    document.writeln('<SCRIPT type="text/javascript" src="'+base+skin+'/'+skin+'.js"></SCRIPT>');
}
