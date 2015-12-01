package edu.xtec.qv.editor.ui.event;

public class ResponseIdentEvent{

	protected java.util.Vector vIdents;
	
	public ResponseIdentEvent(java.util.Vector vIdents){
		this.vIdents=vIdents;
	}
	
	public java.util.Vector getIdents(){
		return vIdents;
	}
	
}