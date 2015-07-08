function open_popup(page, title, width, height) {
	var features = "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width="+width+",height="+height;
	var popup = window.open(page,title, features);
	popup.focus();
    return popup;
}

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
	return value;
}