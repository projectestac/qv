/*
 * QVQuoteBean.java
 * Created on 26/05/2008 
 * @author sarjona
 */
package edu.xtec.qv.admin.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;


public class QVQuoteBean extends QVAdminBean {

	public boolean start(){
		if (request.getParameter("p_save")!=null){
			String sUser = getUsername();
			long lQuote = 5000000;
			try{
				String sQuote = request.getParameter("p_quote");
				lQuote = parseFromMB(Long.parseLong(sQuote));
			}catch (Exception e){e.printStackTrace();}
			setUserQuote(sUser, lQuote);
		}
		return true;
	}
	
	public String getUsername(){
		String sName=getUsername(false);
		if (sName==null || "".equals(sName)){
			sName = getUserId();
		}
		return sName;
	}
	
	public long getUserQuote(){
		return getUserQuote(getUsername());
	}
	
	protected long getUserQuote(String sUser){
		long lQuote = 0;
		try{
			String sQuote = getUserSetting(sUser, "store.defaultQuota", "5000000");
			lQuote = Long.parseLong(sQuote);
		}catch (Exception e){
		}
		return lQuote;
	}

	protected void setUserQuote(String sUser, long lQuote){
		setUserSetting(sUser, "store.defaultQuota", String.valueOf(lQuote));
	}

	protected void setUserSetting(String sUser, String sProperty, String sValue){
		try{
			Properties pUser = getUserSettings(sUser);
			pUser.setProperty(sProperty, sValue);
			
			File f = getSettingsFile(sUser);
			FileOutputStream fOutput = new FileOutputStream(f);
			pUser.store(fOutput, "modified "+sProperty+" by"+getUserId());
		}catch (Exception e){
		}
	}

	
	public long getUserUsedSpace(){
		return getDiskSpace(getUserLocalURL(getUsername()));
	}
	
	public String getUserLocalURL(String sUserid){
		return getSetting("store.localURL")+sUserid;
	}
	
	public Properties getUserSettings(String sUser){		
		Properties pUser = new Properties();
		try{
			File f = getSettingsFile(sUser);
			if(f.exists()){
				FileInputStream is=new FileInputStream(f);
				pUser.load(is);
				is.close();
			}			
		} catch (Exception e){
			logger.error("EXCEPTION: problems loading settings file for user '"+sUser+"' --> "+e);
		}
		return pUser;
	}	
	
	protected String getUserSetting(String sUser, String sKey, String sDefaultValue){
		String sProperty = sDefaultValue;
		try{
			Properties pUser = getUserSettings(sUser);
			sProperty = pUser.getProperty(sKey, sDefaultValue);					
		} catch (Exception e){
			logger.error("EXCEPCIO: No s'ha pogut obtenir la propietat '"+sKey+"' --> "+e);
		}
		return sProperty;
	}
	
	protected File getSettingsFile(String sUser){
		//String sPath = "/windows/D/dev/qv/qv_editor/web/quaderns/"+sUser+"/";
		String sPath = getSetting("store.localURL")+sUser+"/";
		File f = new File(sPath+"qv_editor.properties");
		return f;
	}
	
	
}
