setBrowser();
var IE = isInternetExplorer();

var tempX = 0
var tempY = 0

function getMouseXY(e) {
  var show = form["show"].value;
  if (show!=''){
	  var layer = document.getElementById('show');
	  if (IE) {
	    tempX = event.clientX + document.body.scrollLeft
	    tempY = event.clientY + document.body.scrollTop
	    tempX = event.x
	    tempY = event.y
	  } else { 
	    tempX = e.pageX
	    tempY = e.pageY
	  }  
	  
	  if (tempX < 0){tempX = 0}else{tempX-=35}
	  if (tempY < 0){tempY = 0}else{tempY-=30}

	  moveIdTo(show,tempX,tempY);
  }  
  return true
}

function move(layer){
	form.show.value=layer;
	setIdProperty(layer,'cursor', 'move');
	//setIdProperty('move_link','cursor', 'move');
    if (!isInternetExplorer()) document.captureEvents(Event.MOUSEMOVE | Event.MOUSEUP);
    document.onmouseup = stopMove;
	document.onmousemove = getMouseXY;
}

function stopMove() {
  if (form["show"]!=null){
	  setIdProperty(form["show"].value,'cursor', 'default');
	  //form["show"].focus();
	  form["show"].value='';
  }
  if (!isInternetExplorer()) document.releaseEvents(Event.MOUSEMOVE | Event.MOUSEUP);
}
