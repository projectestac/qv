var agt=navigator.userAgent.toLowerCase();
var is_ie     = ((agt.indexOf("msie") != -1) && (agt.indexOf("opera") == -1));

function xinha_init(xinha_editors){
  xinha_editors = xinha_editors ? xinha_editors : xinha_editors;
  xinha_config = xinha_config ? xinha_config : new Xinha.Config();
  xinha_config.statusBar = false;
  xinha_config.getHtmlMethod = 'TransformInnerHTML';
  xinha_config.toolbar =
  [
    ["popupeditor","htmlmode","print"],
    ["separator","fontsize","bold","italic","underline","strikethrough"],
    ["linebreak","separator","justifyleft","justifycenter","justifyright","justifyfull"],
    ["separator","forecolor","hilitecolor","textindicator"],
    ["separator","undo","redo","separator","subscript","superscript"],
    ["separator","insertorderedlist","insertunorderedlist","outdent","indent"],
    ["separator","inserthorizontalrule","createlink","insertimage","inserttable","toggleborders"],
    ["linebreak","separator","clearfonts","selectall"],
    (Xinha.is_gecko ? [] : ["separator","cut","copy","paste"]), ["showhelp"]
  ];

  xinha_config.URIs['insert_image'] = '../components/xinha/modules/InsertImage/insert_image.jsp';
  if (specialReplacements) xinha_config.specialReplacements = specialReplacements;
  if (is_ie) {
  	xinha_config.sizeIncludesBars=false;
  	xinha_config.sizeIncludesPanels=false;
  }
  xinha_config.colorPickerCellSize = '8px';	
  xinha_config.colorPickerGranularity=15;
	
  xinha_editors = Xinha.makeEditors(xinha_editors, xinha_config, xinha_plugins);
  Xinha.startEditors(xinha_editors);
}

function addEditor(xinha_editors, editor){
	if (xinha_editors) {
		xinha_editors[xinha_editors.length]=editor;
	} else{
		xinha_editors=[ editor ];
	}
	return xinha_editors;
}