import javax.swing.JApplet;

abstract class qvApplet extends JApplet{

	public static String INIT_PARAM="INITPARAM";

	public abstract String getStringRepresentation();

	public abstract boolean initFromParam(String param);

	public void init(){
		super.init();
		String param=getParameter(INIT_PARAM);
		//if (param!=null){
			initFromParam(param);
		//}
	}

}