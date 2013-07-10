function getUser(){
	var user = getQueryParam('p_name')
	return user;
}

function isTeacher(){
	var teacher = getQueryParam("p_teacher");
	if (teacher== null || teacher=="" || "true"!=teacher) teacher=false;
	return teacher;
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

function notNull(o){
	return o!=null && o.length>0;
}

function getScore(itemId){
	var score = "";
	app = getApplet('app_1');
	if (app!=null){
    	score=app.getScore(itemId);
    }
	return score;
}
function setScore(id, resp){
	appl=getApplet('app_1');
	if (appl!=null){
		//alert("id="+id+"  resp="+resp);
		//appl.addResponse(id, resp);
		appl.setScore(id.substring(6), resp);
	}
}



function loadScores(){
	var score = eval('document.'+document.forms[0].name+'.score_1130703683598343');
	if (score!=null){
		score.value=getScore('1130703683598343');
	}
}