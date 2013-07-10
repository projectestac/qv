function enviarForm(elem){
	enviar(elem.name, elem.form);
}

function enviar(action, form){
	switch(action){
		case "set_quadern":
			form.page.value="quadern";
			break;
		case "del_quadern":
			if (!confirm("Estàs segur/a que vols esborrar aquest quadern?")){
				return;
			}
			break;
		case "add_full":
			form.page.value="quadern";
			break;
		case "set_full":
			form.page.value="quadern";
			break;
		case "del_full":
			form.page.value="quadern";
			if (!confirm("Estàs segur/a que vols esborrar aquest full?")){
				return;
			}
			break;
		case "add_pregunta":
			form.page.value="pregunta";
			break;
		case "set_pregunta":
			form.page.value="pregunta";
			break;
		case "del_pregunta":
			form.page.value="pregunta";
			if (!confirm("Estàs segur/a que vols esborrar aquesta pregunta?")){
				return;
			}
			break;
		case "add_fitxer":
			if (form.fitxer.value==""){
				return;
			}
			break;
		case "del_fitxer":
			if (!confirm("Estàs segur/a que vols esborrar aquest fitxer?")){
				return;
			}
			break;
		case "del":
			if (!confirm("Estàs segur/a que vols eliminar-ho?")){
				return;
			}
			break;
		case "del_resposta":
			if (form.num_respostes!=null){
				var numResp = parseInt(form.num_respostes.value)-1;
				form.num_respostes.value = numResp;
			}
			break;
	}
	form.action.value=action;
	form.submit();
}

function redirectToHome(){
	document.location = "index.jsp?page=index";
}

function open_popup(page, title, width, height) {
	var features = "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width="+width+",height="+height;
	var popup = window.open(page,title, features);
	popup.focus();
    return popup;
}

function seleccionar_item(nameId, valueId, total, elemForm, valueElemForm){
	for(i=0;i<total;i++){
		var sId = nameId+i;
		var mat = document.getElementById(sId);
		if (mat!=null){
			if (valueId==sId){
				//mat.style.cssText="border: 3px #004080 solid;";
				mat.style.cssText="background-color: #FDB671";
				if (elemForm!=null){
					elemForm.value=valueElemForm;
				}
			}else{
				mat.style.cssText="background-color:#b7ccf0";
			}
		}
	}
}

function get_window_width(){
	var iWidth=0;
	if (window.innerWidth != null){
	  iWidth = window.innerWidth;
	}else if (document.body.clientWidth != null){
	  iWidth = document.body.clientWidth;
	}
	return iWidth;	
}

function get_window_heigth(){
 if (window.innerHeight != null)
  return window.innerHeight;
if (document.body.clientHeight != null)
  return document.body.clientHeight;
return (0);	
}

function setCurrentForm(formName){
	document.editForm.form.value=formName;
}

function page_size(){
	var x,y;
	var test1 = document.body.scrollHeight;
	var test2 = document.body.offsetHeight
	if (test1 > test2) // all but Explorer Mac
	{
		x = document.body.scrollWidth;
		y = document.body.scrollHeight;
	}
	else // Explorer Mac;
	     //would also work in Explorer 6 Strict, Mozilla and Safari
	{
		x = document.body.offsetWidth;
		y = document.body.offsetHeight;
	}
	alert("x="+x+",y="+y);
}

function setVisibility(elem){
	if (elem!=null){
		var visibility = elem.style.visibility;
		if (visibility=="visible"){
			elem.style.visibility="hidden";
		}else{
			elem.style.visibility="visible";
		}
	}
}

function setSize(form, index){
	form["image_width_"+index].value="";
	form["image_height_"+index].value="";
	enviar("set_image_size", form);
}

function open_close_menu(form, menu){
	if (form[menu]!=null){
		if (form[menu].value=="open"){
			form[menu].value="close";
		}else{
			form[menu].value="open";
		}
	}
}

function not_null(value){
	return (value!=null && value.length>0);
}

function getProportionalWidth(iWidth, iHeight, iMaxWidth, iMaxHeight){
	var iProportionalWidth = -1;
	if (iWidth>0 && iHeight>0){
		if (iWidth>=iHeight && iWidth>iMaxWidth){
			iProportionalWidth=iMaxWidth;
		}else if (iHeight>iWidth && iHeight>iMaxHeight){
			iProportion = iHeight/iMaxHeight;
			iProportionalWidth=iWidth/iProportion;
		}else{
			if (iWidth>iMaxWidth){
				iProportionalWidth = iMaxWidth;
			}else{
				iProportionalWidth = iWidth;
			}
		}
	}
	//alert("width-> real="+iWidth+" max="+iMaxHeight+"  prop="+iProportionalWidth);
	return iProportionalWidth;
}

function getProportionalHeight(iWidth, iHeight, iMaxWidth, iMaxHeight){
	var iProportionalHeight = -1;
	if (iWidth>0 && iHeight>0){
		if (iWidth>iHeight && iWidth>iMaxWidth){
			iProportion = iWidth/iMaxWidth;
			iProportionalHeight=iHeight/iProportion;
		}else if (iHeight>=iWidth && iHeight>iMaxHeight){
			iProportionalHeight=iMaxHeight;
		}else{
			if (iHeight>iMaxHeight){
				iProportionalHeight = iMaxHeight;
			}else{
				iProportionalHeight = iHeight;
			}
		}
	}
	return iProportionalHeight;
}


function htmlToText(html){
	txt = "";
	if (html!=null){
		i = 0;
		while (i<html.length){
			c = html.charAt(i);
			switch (c){
				case '<':
					i++;
					iEnd = html.indexOf(">", i);
					i = iEnd+1;				
					break;
				case '\'' | '\"':
					txt += "\\"+c;
					i++;	
					break;
				default:
					txt += c;
					i++;
					break;
			}
		}
	}
	return txt;
}

function set_edition_mode(form){
	if (form!=null && form.edition_mode){
		var old = form.edition_mode.value;
		switch(old){
			case 'text':
				form.edition_mode.value='visual';
				break;
			default:
				form.edition_mode.value='text';
				break;
		}	
	}else{
		return false;
	}
	return true;
}


