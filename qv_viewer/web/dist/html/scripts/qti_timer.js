var running = false
var endTime = null
var startTime = null
var timerID = null
var passedHours=0;
var passedMinutes=0;
var passedSeconds=0;

var r=null;
	
function startTimerISO(notebookTimeISO,notebookPassedTime,sectionTimeISO,sectionPassedTime){
	/*timeISO= [T]hh[:]mm[:]ss (ISO 8601)  passedTime= hh:mm:ss */
	var hours=0;
	var minutes=0;
	var seconds=0;
	startTime = null;
        startTime = new Date();
        startTime = startTime.getTime();
	//alert("nt="+notebookTimeISO+" npt="+notebookPassedTime+" st="+sectionTimeISO+" spt="+sectionPassedTime);
	if ((notebookTimeISO==null || notebookTimeISO.length<2)&& (sectionTimeISO==null || sectionTimeISO.length<2)) return; //No es far? compte enrere
	var nt=getTime(notebookTimeISO);
	var st=getTime(sectionTimeISO);
	var npt=getTime(notebookPassedTime);
	var spt=getTime(sectionPassedTime);

	if (notebookTimeISO==null || notebookTimeISO.length<2){
		r=subtractTime(st,spt);
	}
	else if (sectionTimeISO==null || sectionTimeISO.length<2){
		r=subtractTime(nt,npt);
	}
	else{
		var r1=subtractTime(st,spt);
		var r2=subtractTime(nt,npt);
		r=minTime(r1,r2);
	}
	//alert("h="+r[0]+":"+r[1]+":"+r[2]);
	startTimer(r[0],r[1],r[2]);
}

function restartTime(){//Albert
	startTime = new Date();
	startTime = startTime.getTime();
}

function getTime(time){
	/* time= [T]hh[:]mm[:]ss (ISO 8601)*/
	var t = new Array(3);
	t[0]=0;
	t[1]=0;
	t[2]=0;
	if (time==null || time.length<2) return t;
	if (time.charAt(0).toLowerCase()=='t') time=time.substring(1);
	i=time.indexOf(':');
	if (i>0){ //Format amb :
		if (time.length>1) t[0]=parseInt(time.substring(0,2));
		if (time.length>4) t[1]=parseInt(time.substring(3,5));
		if (time.length>7) t[2]=parseInt(time.substring(6,8));
	}
	else{ //Format sense :
		if (time.length>1) t[0]=parseInt(time.substring(0,2));
		if (time.length>3) t[1]=parseInt(time.substring(2,4));
		if (time.length>5) t[2]=parseInt(time.substring(4,6));
	}
	return t;
}

function subtractTime(timeUp, timeDown){
	/* timeUp i timeDown son arrays de tres posicions= {h,m,s} */
	var t = new Array(3);
	t[2]=timeUp[2]-timeDown[2];
	t[1]=timeUp[1]-timeDown[1];
	t[0]=timeUp[0]-timeDown[0];
	if (t[2]<0){
		t[2]+=60;
		t[1]-=1;
	}
	if (t[1]<0){
		t[1]+=60;
		t[0]-=1;
	}
	return t;
}

function minTime(timeA, timeB){
	/* timeA i timeB son arrays de tres posicions= {h,m,s} */
	if (timeA[0]>timeB[0]) return timeB;
	else if (timeA[0]<timeB[0]) return timeA;
	else if (timeA[1]>timeB[1]) return timeB;
	else if (timeA[1]<timeB[1]) return timeA;
	else if (timeA[2]>timeB[2]) return timeB;
	else return timeA;
}

function getPassedTime(){
	//startTime
      	var now = new Date()
	now = now.getTime()
	var delta = new Date(now-startTime)
	var theHour= delta.getHours()-1
      	var theMin = delta.getMinutes()
      	var theSec = delta.getSeconds()
	//alert("passed: "+theHour+":"+theMin+":"+theSec+" ini: "+passedHours+":"+passedMinutes+":"+passedSeconds);
	if (theHour<10) theHour = "0"+theHour;
	if (theMin<10) theMin = "0"+theMin;
	if (theSec<10) theSec = "0"+theSec;
	return theHour+":"+theMin+":"+theSec;
}

function startTimer(h,m,s) {
	running = true
        //startTime = new Date()
        //startTime = startTime.getTime()
	var t=(((((h*60)+m)*60)+s)*1000);
        endTime = startTime + t
        showCountDown()
}

function showCountDown() {
        var now = new Date()
        now = now.getTime()
        if (endTime - now <= 0) {
        	stopTimer()
	}
	else {
                var delta = new Date(endTime - now)
		var theHour= delta.getHours()-1
                var theMin = delta.getMinutes()
                var theSec = delta.getSeconds()
                //var theTime = theMin
		var theTime = "";
		if (theHour>0) theTime+=theHour+":";
                theTime += ((theMin < 10) ? "0" : "") + theMin
                theTime += ((theSec < 10) ? ":0" : ":") + theSec
		window.status = "Temps disponible: "+theTime
                if (running) {
                        timerID = setTimeout("showCountDown()",1000)
                }
        }
}

function disableAll(){
  form=document.forms[0];
  for (i=0;i<form.elements.length;i++){
    var obj=form.elements[i];
    if (obj.type!="hidden"  && obj.name.indexOf("interaccio_")!=0 && obj.type!="submit"){
    	if (obj.type=="textarea" || obj.type=="text"){
			obj.readonly="no";
		} else {
			obj.disabled=false;
		}
    }
  }
}

function stopTimer() {
        clearTimeout(timerID)
        running = false
        window.status = " *** Temps esgotat ***"
	//doSubmit2(false);
	//document.forms[0].submit();
	disableAll();
}
