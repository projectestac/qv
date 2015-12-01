function crea_finestra2(url,ample,llarg)
  {
  carta=window.open(url, 'displayWindow', 'toolbar=0,location=0,directories=0,screenX=0,screenY=0,top=0,left=0,status=0,menubar=0,scrollbars=1,resizable=0,width=' + ample + ',height=' + llarg + '');
  carta.focus();
  }
  