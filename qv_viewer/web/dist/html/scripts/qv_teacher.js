

function writeTeacherApplet(){
	//document.write('<applet width="0" height="0" codebase="." archive="appl/qv_local.jar" code="edu.xtec.qv.player.cb.TeacherQVApplet" name="app_1">');
	document.write('<applet width="0" height="0" codebase="." archive="appl/qv_local.jar" code="edu.xtec.qv.player.CorrectQVApplet" name="app_1">');
	document.write('<param value="application/x-java-applet;version=1.3" name="type">');
	document.write('<param value="'+getGroup()+'" name="group">');
	document.write('</applet>');
}

function writeGroups(){
	document.writeln("<SELECT id='p_group' name='p_group' onchange='writeStudents(this.value);'>");
	appl=getApplet('app_1');
	var groups;
	if (appl!=null){
		groups = appl.getGroups();
		groups = groups.split(";");
		document.writeln("<option value=''></option>");
		for (i=0;i<groups.length;i++){
			if (groups[i]!=''){
				document.writeln("<option value='"+groups[i]+"'>"+groups[i]+"</option>");
			}
		}
		document.writeln("</SELECT>");
	}
}

function writeUsers(){
}

function writeStudents(group){
	layer = document.getElementById('students');
	if (layer!=null){
		out="<OL>";
		appl=getApplet('app_1');
		if (appl!=null){
			var students = appl.getStudents(group);
			students = students.split(";");
			for (i=0;i<students.length;i++){
				if (students[i]!=''){
					out+="<li><a href=\"javascript:openCB('"+students[i]+"');\" >"+students[i]+"</a></li>";
				}
			}
		}
		out+="</OL>";
		layer.innerHTML=out;
	}
}

function getSelectedGroup(){
	var group = '';
	list = document.getElementById('p_group');
	if (list!=null){
		group = list.options[list.selectedIndex].value;
	}
	return group;
}
	
function openCB(file){
	var name = getNameFromXML(file);
	var surname = getSurnameFromXML(file);
	open_popup('section_1.html?p_qv=cb&p_name='+name+'&p_surname='+surname+'&p_school=&p_group='+getSelectedGroup()+'&p_teacher=true;','CB4');
}

function getNameFromXML(file){
	var name='';
	if (notNull(file)){
		name=file.substring(0, file.indexOf("_"));
	}
	return name;
}

function getSurnameFromXML(file){
	var surname='';
	if (notNull(file)){
		surname=file.substring(file.indexOf("_")+1, file.indexOf("_res.xml"));
		surname=replaceChars(surname, '_', " ");
	}
	return surname;
}

function writeReport(){
	layer = document.getElementById('report');
	if (layer!=null){
		var out ="";
		appl=getApplet('app_1');
		if (appl!=null){
			var students = appl.getReportData(getGroup());
			out+=students;
		}
		layer.innerHTML=out;
	}
}



