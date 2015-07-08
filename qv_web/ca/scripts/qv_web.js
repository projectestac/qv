function open_popup(page, title, width, height) {
	var features = "toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width="+width+",height="+height;
	var popup = window.open(page,title, features);
	popup.focus();
    return popup;
}
