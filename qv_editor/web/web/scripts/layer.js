var hide_select=false;

NS6=false;
IE4=(document.all);
if (!IE4) {NS6=(document.getElementById);}
NS4=(document.layers);

function collapse(id, img, openImgSrc, closeImgSrc){
  var oTemp=document.getElementById(id);
  if(oTemp.style.display=="block"){
    oTemp.style.display="none";
    if (img!=null) img.src=openImgSrc;
  }
  else{
    oTemp.style.display="block";
    if (img!=null) img.src=closeImgSrc;
  }
}

function center_layer(name,width,height)
{
	if (NS4){
		floatX = Math.round((window.innerWidth-20)/2)-Math.round(width/2);
		floatY = Math.round((window.innerHeight-20)/2)-Math.round(height/2);
		document.layers[name].pageX = document.body.scrollLeft + floatX;
		document.layers[name].pageY = document.body.scrollTop + floatY;
	}else if (NS6){
		floatX = Math.round((window.innerWidth-20)/2)-Math.round(width/2);
		floatY = Math.round((window.innerHeight-20)/2)-Math.round(height/2);
		document.getElementById(name).style.left=(document.body.scrollLeft + floatX)+"px";
		document.getElementById(name).style.top=(document.body.scrollTop + floatY)+"px";	
	}else if (IE4){
		floatX = Math.round((document.body.offsetWidth-20)/2)-Math.round(width/2);
		floatY=Math.round((document.body.offsetHeight-20)/2)-Math.round(height/2);
		document.all[name].style.posLeft = document.body.scrollLeft + floatX;
		document.all[name].style.posTop = document.body.scrollTop + floatY;	
	}
}

function get_layer(name){
	if(document.layers){
		layer = document.layers[name];
	}else if (document.all){
		layer = document.all[name];
	}else if (!document.all && document.getElementById){
		layer = document.getElementById(name);
	}
	return layer;
}

function get_layer_visibility(name){
	visibility = 'hidden';
	if(document.layers){
		layer = document.layers[name];
		if (layer!=null) visibility = layer.visibility;
	}else if (document.all){
		layer = document.all[name];
		if (layer!=null) {
			visibility = layer.style.visibility;
			if (layer.style.display=='none') visibility = 'hidden';
		}
	}else if (!document.all && document.getElementById){
		layer = document.getElementById(name);
		if (layer!=null) {
			visibility = layer.style.visibility;
			if (layer.style.display=='none') visibility = 'hidden';
		}
	}
	return visibility;
}

function set_layer_visibility(name, visibility){
	if(document.layers){
		layer = document.layers[name];
		if (layer!=null) layer.visibility=visibility;
	}else if (document.all){
		layer = document.all[name];
		if (layer!=null) {
			layer.style.visibility=visibility;
			if (layer.style.visibility=="visible") layer.style.display="inline";
		}
	}else if (!document.all && document.getElementById){
		layer = document.getElementById(name);
		if (layer!=null) {
			layer.style.visibility=visibility;
			if (layer.style.visibility=="visible") layer.style.display="inline";
		}
	}
}

/* Mostra i centra la capa que s'indica a layerName. Width i height indiquen les mides de la capa */
function show_and_center_layer(layerName, width, height){
	center_layer(layerName, width, height);
	set_layer_visibility(layerName, 'visible');
}

/* Mostra la capa que s'indica a layerName.*/
function show_layer(layerName){
	set_layer_visibility(layerName, 'visible');
}

/* Amaga la capa indicada i esborra els camps del formulari indicat */
function hide_and_reset_layer(layerName, form){
	set_layer_visibility(layerName, 'hidden');
	if (form!=null) form.reset();
}

/* Amaga la capa indicada */
function hide_layer(layerName){
	set_layer_visibility(layerName, 'hidden');
}

function hide_all_form_select(){
  for(j=0; j<document.forms.length; j++){
  	hide_form_select(document.forms[j]);
  }
}

function hide_form_select(form){
	if (IE4 && form!=null){
	  for(i=0; i<form.elements.length; i++){
    	var obj=form.elements[i];
	    if(obj.type=="select-one"){
	    	if ((obj.name!="") && (obj.name!="p_content_type") && (obj.name!="p_material_uri") && (obj.name!="p_material_flash_uri") && (obj.name!="p_material_audio_uri") && (obj.name!="p_material_video_uri") && (obj.name!="p_material_jclic_uri_select") && (obj.name!="p_material_application_uri") && !is_layer_select(obj.name)){
	    		obj.style.display='none';
	    	}
	    }
	  }	
	}
}

function show_all_form_select(form){
  for(k=0; k<document.forms.length; k++){
  	show_form_select(document.forms[k]);
  }
}

function show_form_select(form){
	if (IE4 && form!=null){
	  for(i=0; i<form.elements.length; i++){
    	var obj=form.elements[i];
	    if(obj.type=="select-one"){
	    	if ((obj.name!="") && (obj.name!="p_content_type") && (obj.name!="p_material_uri") && (obj.name!="p_material_flash_uri") && (obj.name!="p_material_audio_uri") && (obj.name!="p_material_video_uri") && (obj.name!="p_material_application_uri") && (obj.name!="p_material_jclic_uri_select") && !is_layer_select(obj.name)){
	    		obj.style.display='inline';
	    	}
	    }
	  }	
	}
}

function is_element_in_document(elem){
  for(i=0; i<document.forms.length; i++){
  	if (document.forms[i].elements[elem]!=null){
  		return document.forms[i].elements[elem];
  	}
  }
  return null;
}

/** COMMON FUNCTIONS FOR LAYER **/
function hide_all_layers(){
	hide_preferences_layer();
	hide_import_layer();
	hide_metadata_layer();
	hide_lom_layer();
	hide_material_layer();
	hide_ordered_response_layer();
	hide_cloze_response_layer();
}

function is_layer_select(name){
	return 	is_ordered_response_select(name) ||
			is_cloze_response_select(name) ||
			is_rarea_select(name) ||
			is_source_select(name) ||
			is_dragdrop_response_select(name) ||
			is_lom_select(name);
}




/** IMPORT LAYER **/
function show_import_layer(){
	hide_preferences_layer();
	show_and_center_layer('importLayer', 300, 220);
}

function hide_import_layer(){
	hide_and_reset_layer('importLayer', document.importForm);
}

/** PREFERENCES LAYER **/
function show_preferences_layer(){
	hide_import_layer();
	show_and_center_layer('preferencesLayer', 500, 250);
}
function hide_preferences_layer(){
	hide_and_reset_layer('preferencesLayer', null);
}

/** LOM (labeler) LAYER **/
function show_lom_layer(){
	hide_select=true;
	hide_all_form_select();
	show_and_center_layer('lomLayer', 530, 500);
}

function hide_lom_layer(){
	show_all_form_select();
	hide_layer('lomLayer');
}

function is_lom_select(name){
	return (name!=null && (name.indexOf("p_area")>=0 || name.indexOf("p_language")>=0));
}

/** METADATA LAYER **/
function show_metadata_layer(form){
	//hide_all_layers();
	hide_select=true;
	show_and_center_layer('metadataLayer', 250, 150);
}
function hide_metadata_layer(){
	show_all_form_select();
	hide_layer('metadataLayer');
}

function update_metadata_info(form, label, entry){
	form.label.value=label;
	form.entry.value=entry;
}

/** ORDERED RESPONSE LAYER **/
function show_ordered_response_layer(form){
	//hide_all_layers();
	hide_select=true;
	show_and_center_layer('orderedResponseLayer', 550, 400);
}
function hide_ordered_response_layer(){
	show_all_form_select();
	hide_layer('orderedResponseLayer');
}

function is_ordered_response_select(name){
	return (name!=null && ( (name.indexOf("orientation_")>=0) ||
		(name.indexOf("response_material_type_")>=0) ||
		(name.indexOf("p_response_label_image_")>=0)));
}

/** CLOZE RESPONSE LAYER **/
function show_cloze_response_layer(form){
	hide_select=true;
	show_and_center_layer('clozeResponseLayer', 550, 400);
}
function hide_cloze_response_layer(){
	show_all_form_select();
	hide_layer('clozeResponseLayer');
}

function is_cloze_response_select(name){
	return (name!=null && ( (name.indexOf("response_option_type")>=0)));
}

/** MATERIAL LAYER **/
function show_material_layer(form){
	//hide_all_layers();
	hide_select=true;
	show_and_center_layer('materialLayer', 550, 400);
}
function hide_material_layer(){
	show_all_form_select();
	hide_mat_layers();
	hide_layer('materialLayer');
}

function hide_mat_layers(){
	try{
		if (get_layer_visibility('mathtmlLayer')!='hidden')
			p_material_htmlEditor.setMode("textmode");
	}catch(er){;} 
	hide_layer('mathtmlLayer');
	hide_layer('mattextLayer');
	hide_layer('matimageLayer');
	hide_layer('matflashLayer');
	hide_layer('mataudioLayer');
	hide_layer('matvideoLayer');
	hide_layer('matjclicLayer');
	hide_layer('matapplicationLayer');
	hide_layer('matlatexLayer');
}

function set_material_layer(current){
	hide_mat_layers();
	switch(current){
		case "text":
			show_layer('mattextLayer');
			//document.fullForm.p_material_text.value=htmlToText(p_material_htmlEditor.getHTML());
			//p_material_htmlEditor.setHTML(document.fullForm.p_material_text.value);
			//document.fullForm.p_material_text.value=p_material_htmlEditor.getHTML();
			break;
		case "html":
			show_layer('mathtmlLayer');
			try{
				if (p_material_htmlEditor._editMode!="wysiwyg")
					p_material_htmlEditor.setMode("wysiwyg");
			}catch(e){;}
			//p_material_htmlEditor.setHTML(document.fullForm.p_material_text.value);
			break;
		case "image":
			show_layer('matimageLayer');
			break;
		case "flash":
			show_layer('matflashLayer');
			break;
		case "audio":
			show_layer('mataudioLayer');
			break;
		case "video":
			show_layer('matvideoLayer');
			break;
		case "jclic":
			show_layer('matjclicLayer');
			break;
		case "application":
			show_layer('matapplicationLayer');
			break;
		case "latex":
			show_layer('matlatexLayer');
			break;
		case "break":
			break;
	}
}

/** RAREA LAYER **/
function show_rarea_layer(form){
	hide_select=true;
	show_and_center_layer('rareaLayer', 325, 250);
}
function hide_rarea_layer(){
	show_all_form_select();
	hide_layer('rareaLayer');
}

function is_rarea_select(name){
	return (name!=null && (name.indexOf("p_rarea_type")>=0 || name.indexOf("bounded_list")>=0));
}

/** SOURCE LAYER **/
function show_source_layer(form){
	hide_select=true;
	show_and_center_layer('sourceLayer', 325, 250);
}
function hide_source_layer(){
	show_all_form_select();
	hide_layer('sourceLayer');
}

function is_source_select(name){
	return (name!=null && (name.indexOf("p_source_uri")>=0 ||
		 name.indexOf("p_source_targets")>=0));
}

/** DRAGDROP POSITION LAYER **/
function show_dragdrop_position_layer(form){
	hide_select=true;
	show_and_center_layer('dragdropPositionLayer', 450, 350);
}
function hide_dragdrop_position_layer(){
	show_all_form_select();
	hide_layer('dragdropPositionLayer');
}

/** DOT RESPONSE LAYER **/
function show_dot_response_layer(form){
	hide_select=true;
	show_and_center_layer('dotResponseLayer', 325, 250);
}
function hide_dot_response_layer(){
	show_all_form_select();
	hide_layer('dotResponseLayer');
}

/** DRAGDROP RESPONSE LAYER **/
function show_dragdrop_response_layer(form){
	hide_select=true;
	show_and_center_layer('dragdropResponseLayer', 325, 250);
}
function hide_dragdrop_response_layer(){
	show_all_form_select();
	hide_layer('dragdropResponseLayer');
}

function is_dragdrop_response_select(name){
	return (name!=null && (name.indexOf("p_dragdrop_response_sources")>=0 || name.indexOf("p_dragdrop_response_targets")>=0));
}

/** ADD ITEM LAYER **/
function show_response_options_layer(option){
  if (option!=null){
  	if (option=="selection" || option=="Ordered" || option=="cloze"){
  		setDisplay("inicialValuesLayer", "block");
  	}else{
	  setDisplay("inicialValuesLayer", "none");
  	}
  	
  	if (option=="selection" || option=="Ordered"){
	  setDisplay("numberResponsesLayer", "block");	
  	}else{
	  setDisplay("numberResponsesLayer", "none");
  	}

  	if (option=="selection"){
	  setDisplay("cardinalityLayer", "block");	
  	}else{
	  setDisplay("cardinalityLayer", "none");
  	}

  	if (option=="selection" || option=="Ordered"){
	  setDisplay("orientationLayer", "block");	
  	}else{
	  setDisplay("orientationLayer", "none");
  	}

  	if (option=="selection" || option=="Ordered"){
	  setDisplay("materialResponseLayer", "block");	
  	}else{
	  setDisplay("materialResponseLayer", "none");
  	}

  	if (option=="cloze"){
	  setDisplay("responseOptionLayer", "block");	
  	}else{
	  setDisplay("responseOptionLayer", "none");
  	}
  	if (option=="hotspot"){
  		setDisplay("hotspotNoEditableLayer", "block");
  	}else{
	  setDisplay("hotspotNoEditableLayer", "none");
  	}
  	if (option=="drag_drop"){
  		setDisplay("dragdropNoEditableLayer", "block");
  	}else{
	  setDisplay("dragdropNoEditableLayer", "none");
  	}
  	if (option=="draw"){
  		setDisplay("drawNoEditableLayer", "block");
  	}else{
	  setDisplay("drawNoEditableLayer", "none");
  	}
  }
}

function setDisplay(id, display){
  var oLayer=document.getElementById(id);
  if (oLayer!=null && oLayer.style!=null)
	  oLayer.style.display=display;
}



/** MOVE LAYER **/
var isNav4, isNav6, isIE4;

/*
 * Browser version snooper; determines your browser
 * (Navigator 4, Navigator 6, or Internet Explorer 4/5)
 */
function setBrowser()
{
    if (navigator.appVersion.charAt(0) == "4")
    {
        if (navigator.appName.indexOf("Explorer") >= 0)
        {
            isIE4 = true;
        }
        else
        {
            isNav4 = true;
        }
    }
    else if (navigator.appVersion.charAt(0) > "4")
    {
        isNav6 = true;
    }
}

function isNavigator(){
  return isNav4 || isNav6;
}


function isInternetExplorer(){
  return isIE4;
}

/*
 *
 * Given a selector string, return a style object
 * by searching through stylesheets. Return null if
 * none found
 *
 */
function getStyleBySelector( selector )
{
    if (!isNav6)
    {
        return null;
    }
    var sheetList = document.styleSheets;
    var ruleList;
    var i, j;

    /* look through stylesheets in reverse order that
       they appear in the document */
    for (i=sheetList.length-1; i >= 0; i--)
    {
        ruleList = sheetList[i].cssRules;
        for (j=0; j<ruleList.length; j++)
        {
            if (ruleList[j].type == CSSRule.STYLE_RULE &&
                ruleList[j].selectorText == selector)
            {
                return ruleList[j].style;
            }   
        }
    }
    return null;
}

/*
 *
 * Given an id and a property (as strings), return
 * the given property of that id.  Navigator 6 will
 * first look for the property in a tag; if not found,
 * it will look through the stylesheet.
 *
 * Note: do not precede the id with a # -- it will be
 * appended when searching the stylesheets
 *
 */
function getIdProperty( id, property )
{
    if (isNav6)
    {
        var styleObject = document.getElementById( id );
        if (styleObject != null)
        {
            styleObject = styleObject.style;
            if (styleObject[property])
            {
                return styleObject[ property ];
            }
        }
        styleObject = getStyleBySelector( "#" + id );
        return (styleObject != null) ?
            styleObject[property] :
            null;
    }
    else if (isNav4)
    {
        return document[id][property];
    }
    else
    {
        return document.all[id].style[property];
    }
}

/*
 *
 * Given an id and a property (as strings), set
 * the given property of that id to the value provided.
 *
 * The property is set directly on the tag, not in the
 * stylesheet.
 *
 */
function setIdProperty( id, property, value )
{
    if (isNav6)
    {
        var styleObject = document.getElementById( id );
        if (styleObject != null)
        {
            styleObject = styleObject.style;
            styleObject[ property ] = value;
        }
        
        /*
        styleObject = getStyleBySelector( "#" + id );
        if (styleObject != null)
        {
            styleObject[property] = value;
        }
        */
    }
    else if (isNav4)
    {
        document[id][property] = value;
    }
    else if (isIE4)
    {
         document.all[id].style[property] = value;
    }
}

/*
 *
 * Move a given id.  If additive is true,
 * then move it by xValue dots horizontally and
 * yValue units vertically.  If additive is
 * false, then move it to (xValue, yValue)
 *
 * Note: do not precede the id with a # -- it will be
 * appended when searching the stylesheets
 *
 * Note also: length units are preserved in Navigator 6
 * and Internet Explorer. That is, if left is 2cm and
 * top is 3cm, and you move to (4, 5), the left will
 * become 4cm and the top 5cm.
 *
 */
function generic_move( id, xValue, yValue, additive )
{
    var left = getIdProperty(id, "left");
    var top = getIdProperty(id, "top");
    var leftMatch, topMatch;

    if (isNav4)
    {
        leftMatch = new Array( 0, left, "");
        topMatch = new Array( 0, top, "");
    }
    else if (isNav6 || isIE4 )
    {
        var splitexp = /([-0-9.]+)(\w+)/;
        leftMatch = splitexp.exec( left );
        topMatch = splitexp.exec( top );
        if (leftMatch == null || topMatch == null)
        {
            leftMatch = new Array(0, 0, "px");
            topMatch = new Array(0, 0, "px");
        }
    }
    left = ((additive) ? parseFloat( leftMatch[1] ) : 0) + xValue;
    top = ((additive) ? parseFloat( topMatch[1] ) : 0) + yValue;
    setIdProperty( id, "left", left + leftMatch[2] );
    setIdProperty( id, "top", top + topMatch[2] );
}

/*
 *
 * Move a given id to position (xValue, yValue)
 *
 */
function moveIdTo( id, x, y )
{
    generic_move( id, x, y, false );
}

/*
 *
 * Move a given id to (currentX + xValue, currentY + yValue)
 *
 */
function moveIdBy( id, x, y)
{
    generic_move( id, x, y, true );
}

/*
 *
 * Function used when converting rgb format colors
 * from Navigator 6 to a hex format
 *
 */ 
function hex( n )
{
    var hexdigits = "0123456789abcdef";
    return ( hexdigits.charAt(n >> 4) + hexdigits.charAt(n & 0x0f) );
}

/*
 *
 * Retrieve background color for a given id.
 * The value returned will be in hex format (#rrggbb)
 *
 */ 
function getBackgroundColor( id )
{
    var color;

    if (isNav4)
    {
        color = document[id].bgColor;
    }
    else if (isNav6)
    {
        var parseExp = /rgb.(\d+),(\d+),(\d+)./;
        var rgbvals;
        color = getIdProperty( id, "backgroundColor" );
        if (color)
        {
            rgbvals = parseExp.exec( color );
            if (rgbvals)
            {
                color = "#" + hex( rgbvals[1] ) + hex( rgbvals[2] ) +
                    hex( rgbvals[3] );
            }
        }
        return color;
    }
    else if (isIE4)
    {
        return document.all[id].backgroundColor;
    }
    return "";
}

/*
 * Return a division's document
 */
function getDocument( divName )
{
    var doc;
    if (isNav4) doc = window.document[divName].document;
    else if (isNav6) doc = document;
    else if (isIE4) doc = document;
    return doc;
}



/** ONLOAD **/
window.onload=function(){
	//elem = is_element_in_document('p_material_html')
	//if (elem!=null){
	//	initEditor('p_material_html');
	//}
	if (hide_select){
		hide_all_form_select();
	}else{
		show_all_form_select();
	}
}

