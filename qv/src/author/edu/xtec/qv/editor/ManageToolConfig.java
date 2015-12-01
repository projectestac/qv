package edu.xtec.qv.editor;


public class ManageToolConfig{

	public static java.util.Properties prop=null;
	
	public static void iniConfiguration(){
		try{
			if (prop==null){
	  		prop=new java.util.Properties();
			prop.load(ClassLoader.getSystemResource("edu/xtec/resources/properties/ManageToolConfig.properties").openStream());//new java.io.FileInputStream("ManageToolConfig.properties"));
    	}
    }
    catch (Exception e){
    	System.out.println("Excepció en carregar ManageToolConfig.properties:"+e);
    	prop=null;
    }
	}
	
	public static String getProperty(String sPropertyName){
            //System.out.println("Demanada property:"+sPropertyName);
		if (prop==null) iniConfiguration();
		if (prop!=null) return prop.getProperty(sPropertyName);
		else return null;
	}
        
}
