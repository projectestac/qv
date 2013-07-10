package edu.xtec.qv.cp;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jdom.CDATA;
import org.jdom.Namespace;

import edu.xtec.eun.EUNParsingException;
import edu.xtec.eun.EUNPreparingOutputException;
import edu.xtec.eun.LangString;
import edu.xtec.eun.celebrate.Classification;
import edu.xtec.eun.celebrate.Context;
import edu.xtec.eun.celebrate.Contribute;
import edu.xtec.eun.celebrate.CopyrightAndOtherRestrictions;
import edu.xtec.eun.celebrate.Date;
import edu.xtec.eun.celebrate.Description;
import edu.xtec.eun.celebrate.Educational;
import edu.xtec.eun.celebrate.General;
import edu.xtec.eun.celebrate.Identifier;
import edu.xtec.eun.celebrate.IntendedEndUserRole;
import edu.xtec.eun.celebrate.Keyword;
import edu.xtec.eun.celebrate.LOM;
import edu.xtec.eun.celebrate.LifeCycle;
import edu.xtec.eun.celebrate.Purpose;
import edu.xtec.eun.celebrate.Rights;
import edu.xtec.eun.celebrate.Role;
import edu.xtec.eun.celebrate.Technical;
import edu.xtec.eun.celebrate.Title;
import edu.xtec.eun.celebrate.parser.CelebrateParser;
import edu.xtec.eun.rights.Action;
import edu.xtec.eun.rights.CDR;
import edu.xtec.eun.rights.Permission;
import es.upf.ims.ParsingException;
import es.upf.ims.PreparingOutputException;
import es.upf.ims.cp.CPDocument;
import es.upf.ims.cp.FileRef;
import es.upf.ims.cp.Manifest;
import es.upf.ims.cp.Metadata;
import es.upf.ims.cp.Organizations;
import es.upf.ims.cp.Resource;
import es.upf.ims.cp.Resources;
import es.upf.ims.cp._LIBGlobal;

public class QVCPDocument extends CPDocument {
	
	private static final Logger logger = Logger.getRootLogger();

	/** The schema name */
    public static String QTI_SCHEMA_NAME= "IMS QTI";   

    /** The schema version */
    public static String QTI_SCHEMA_VERSION= "1.2";   

    /** The Celebrate namespace */
    public static Namespace CELEBRATE_NAMESPACE = Namespace.getNamespace("euncl", "http://celebrate.eun.org/xml/ns/celebrateLOM-0_2");
    	
    /** The Rights namespace */
    public static Namespace RIGHTS_NAMESPACE = Namespace.getNamespace("rights", "http://celebrate.eun.org/xml/ns/rights-0_1");

	/** The Question&Test Interoperability namespace */
    public static Namespace IMS_QTI_NAMESPACE = Namespace.getNamespace("imsqti", "http://www.imsglobal.org/xsd/imsqti_item_v1p2");

	/** The default language */
    public static String DEFAULT_LANGUAGE = "ca";
	
    /** Constant with the Content Packaging XML Schema url */
    public static String CP_XSD_URL = "http://clic.xtec.net/qv_web/dist/imscp_v1p1.xsd";   

	
	/* ----------------------- 
       ATTRIBUTE DECLARATION  
       ----------------------- */   

	/** The class name */
	public static final String CLASS_NAME = "QVCPDocument";
	/** The Manifest Element */
	public Manifest m_manifest = null;
	/** The LOM Element **/
	public LOM m_lom = null;
	/** The user attribute */
	private String m_user = null;
	/** The qv name attribute */
	private String m_qv = null;
	/** The qv directory attribute */
	private String m_qv_dir = null;
	/** The qv remote url attribute */
	private String m_qv_url = null;

	/** True if lom exists; false otherwise */
	private boolean m_lom_exists;
	
	private static final String RESOURCE_QV_TYPE = "imsqti_item_xmlv1p2";

	public QVCPDocument(String sUser, String sQV, String sQVDir, String sQVURL){
		this.m_user = sUser;
		this.m_qv = sQV;
		this.m_qv_dir = sQVDir;
		this.m_qv_url = sQVURL;
		
		// Get manifest
		
		Resource oRsrc = null;
		File fManifest = new File(sQVDir+File.separator+"imsmanifest.xml");
		if (fManifest.exists()){
			//logger.debug("Load existing manifest");
			m_lom_exists = true;
			try{
				loadFromXMLFile(fManifest);
				m_manifest = (Manifest)getRoot();
			}catch (ParsingException pe){
				pe.printStackTrace();
			}
		}else{
			//logger.debug("Create empty manifest");
			m_lom_exists = false;
			m_lom = null;
			m_manifest = createEmptyManifest();
			setRoot(m_manifest);
		}
		
		//Set manifest values
		m_manifest.setIdentifier("mf_"+createIdentifier());

		//Update Resource
/*		Resources oRsrcs = oManifest.getResources();
		if (!oRsrcs.getResourceElements().isEmpty()){
			oRsrc = (Resource)(oRsrcs.getResourceElements().get(0));					
		}
		updateResource(oRsrc);*/
	}
	
    /** Prepares the data encapsulated in the root to generate a
     * XML document using the JDOM API
     */
    public void prepare4XMLOutput() 
    throws PreparingOutputException {
		LOM oLOM = getLOM();
		if (oLOM!=null){
			try{
				oLOM.prepare4XMLOutput();
				setLOM(oLOM);
			}catch (EUNPreparingOutputException epe){
				epe.printRouteToException();
				throw new PreparingOutputException(epe.getMessage(), CLASS_NAME, "prepare4XMLOutput");
			}
		}
		super.prepare4XMLOutput();
        this.m_root.addNamespaceDeclaration(CELEBRATE_NAMESPACE);
        this.m_root.addNamespaceDeclaration(IMS_QTI_NAMESPACE);
		this.m_root.addNamespaceDeclaration(RIGHTS_NAMESPACE);
    }
	
	public void store2XMLFile() throws IOException, PreparingOutputException{
		if (m_qv_dir!=null){
			File fManifest = new File(m_qv_dir+File.separator+"imsmanifest.xml");
			store2XMLFile(fManifest);
		}
	}
	
	protected Manifest createEmptyManifest(){
		Manifest oManifest = new Manifest();
		
		Metadata oMetadata = new Metadata();
		oMetadata.setSchema(_LIBGlobal.CP_SCHEMA_NAME);
		oMetadata.setSchemaVersion(_LIBGlobal.CP_SCHEMA_VERSION);
		oManifest.setMetadata(oMetadata);
		
		Organizations oOrgs = new Organizations();
		oManifest.setOrganizations(oOrgs);
		
		Resources oRsrcs = new Resources();
		oManifest.setResources(oRsrcs);
		Resource oRsrc = new Resource(createIdentifier(), RESOURCE_QV_TYPE);
		oRsrc.setMetadata(createEmptyMetadata());
		oRsrcs.addResourceElement(oRsrc);
		return oManifest;
	}
		
	protected Metadata createEmptyMetadata(){
		Metadata oMetadata = new Metadata();
		oMetadata.setSchema(QTI_SCHEMA_NAME);
		oMetadata.setSchemaVersion(QTI_SCHEMA_VERSION);
		return oMetadata;
	}

	protected LOM createEmptyLOM(){
		LOM oLOM = new LOM();
		General oGeneral = new General();
		oGeneral.addIdentifierElement(new Identifier("XTEC", createIdentifier()));
		oGeneral.setTitle(new Title());
		oGeneral.addLanguageElement(DEFAULT_LANGUAGE);
		oGeneral.setDescription(new Description());
		oLOM.setGeneral(oGeneral);
		
		Technical oTechnical = new Technical();
		oTechnical.addLocationElement(m_qv_url);
		oLOM.setTechnical(oTechnical);
		
		Educational oEducational = new Educational();
		oEducational.addIntendedEndUserRoleElement(IntendedEndUserRole.AUTHOR);
		oEducational.setTypicalAgeRange("-1");
		oLOM.addEducationalElement(oEducational);
		
		Rights oRights = new Rights();
		oRights.setCopyrightAndOtherRestrictions(CopyrightAndOtherRestrictions.YES);
		oRights.setCDR(createEmptyCDR());
		oLOM.setRights(oRights);
		
		Classification oClassification = new Classification();
		oClassification.addKeywordElement(new Keyword());
		oLOM.addClassificationElement(oClassification);
		
		return oLOM;
	}
	
	protected CDR createEmptyCDR(){
		CDR oCDR = new CDR();
		Permission oPermission = new Permission();
		oPermission.setAction(Action.REMOTEPLAY);
		oCDR.addPermissionElement(oPermission);
		
		return oCDR;
	}	
	
	protected String createIdentifier(){
		return "qv_"+m_user+"_"+m_qv;
	}
		
	protected String[] getQVFiles(){
		File fManifest = new File(m_qv_dir);
		String[] sFiles = fManifest.list();
		return sFiles;
	}
	
	protected Resource getResource(){
		Resource oRsrc = null;
		if (this.m_manifest!=null){
			Resources oRsrcs = this.m_manifest.getResources();
			if (oRsrcs!=null && !oRsrcs.getResourceElements().isEmpty()){
				oRsrc = (Resource)(oRsrcs.getResourceElements().get(0));
			}
		}
		return oRsrc;
	}
	
	public boolean existsLOM(){
		return m_lom_exists;
	}
	public LOM getLOM(){
		if (m_lom==null){
			Resource oResource = getResource();
			if (oResource!=null){
				Metadata oMetadata = oResource.getMetadata();
				if (oMetadata!=null){
					if (oMetadata.getWildCard()==null){
						//logger.debug("create empty LOM");
						m_lom = createEmptyLOM();
						oMetadata.setWildCard(m_lom);
					}else{
						if (LOM.isLOMElement(oMetadata.getWildCard())){
							try{
								m_lom = CelebrateParser.parseElement(oMetadata.getWildCard());
							}catch (EUNParsingException eup){
								eup.printStackTrace();
							}
						}
					}						
				}			
			}
		}
		return m_lom;
	}	
	public void setLOM(LOM lom){
		Resource oResource = getResource();
		if (oResource!=null){
			Metadata oMetadata = oResource.getMetadata();
			if (oMetadata!=null){
				oMetadata.setWildCard(lom);
			}
		}
	}

	public void setFiles(){
		Resource oResource = getResource();
		if (oResource!=null){
			String[] list = getQVFiles();
			ArrayList al = new ArrayList();
			if (list!=null){
				int i = 0;
				while (i<list.length){
					try{
						al.add(i, new FileRef(new URI(list[i])));
						i++;
					}catch (Exception e){}
				}
			}
			oResource.setFileRefElements(al);
		}
	}

	public LifeCycle getLifeCycle(){
		LifeCycle oLifeCycle = null;
		LOM oLOM = getLOM();
		if (oLOM!=null){
			oLifeCycle = oLOM.getLifeCycle();
		}
		return oLifeCycle;
	}
	public General getGeneral(){
		General oGeneral = null;
		LOM oLOM = getLOM();
		if (oLOM!=null){
			oGeneral = oLOM.getGeneral();
		}
		return oGeneral;
	}
	public Rights getRights(){
		Rights oRights = null;
		LOM oLOM = getLOM();
		if (oLOM!=null){
			oRights = oLOM.getRights();
		}
		return oRights;
	}
	public Educational getEducational(){
		Educational oEducational = null;
		LOM oLOM = getLOM();
		if (oLOM!=null && oLOM.getEducationalElements()!=null && !oLOM.getEducationalElements().isEmpty()){
			oEducational = (Educational)oLOM.getEducationalElements().get(0);
		}
		return oEducational;
	}
	public Classification getEducationalLevelClassification(){
		return getClassification(Purpose.V_EDUCATIONAL_LEVEL);
	}

	public Classification getDisciplineClassification(){
		return getClassification(Purpose.V_DISCIPLINE);
	}

	protected Classification getClassification(String sPurpose){
		Classification oClassification = null;
		LOM oLOM = getLOM();
		if (oLOM!=null && sPurpose!=null){
			Iterator it = oLOM.getClassificationElements().iterator();
			while (it.hasNext()){
				Classification tmpClass = (Classification)it.next();
				if (tmpClass.getPurpose()!=null && sPurpose.equals(tmpClass.getPurpose().getValue())){
					oClassification=tmpClass;
					break;
				}
			}
			if (oClassification==null){
				oClassification = new Classification();
				oClassification.setPurpose(new Purpose(sPurpose));
				Keyword oKeyword = new Keyword();
				oKeyword.addLangStringElement(new LangString("", DEFAULT_LANGUAGE));
				oClassification.addKeywordElement(oKeyword);
				oLOM.addClassificationElement(oClassification);
			}
		}
		return oClassification;
	}

	public Vector getLOMEducationalLevels(){
		Vector vLevels = new Vector();
		Classification oClass = getEducationalLevelClassification();
		if (oClass!=null){			
			Iterator it = oClass.getKeywordElements().iterator();
			while (it.hasNext()){
				Keyword oKeyword = (Keyword)it.next();
				Iterator itLangString = oKeyword.getLangStringElements().iterator();
				while (itLangString.hasNext()){
					LangString ls = (LangString)itLangString.next();
					vLevels.add(ls.getText());
				}
			}
		}		
		return vLevels;
	}	

	public String getLOMAuthor(){
		String sAuthor = "";
		LifeCycle oLifeCycle = getLifeCycle();
		if (oLifeCycle!=null && oLifeCycle.getFirstContributeElement()!=null){
			Contribute oContribute = oLifeCycle.getFirstContributeElement();
			if (oContribute!=null){
				ArrayList aEntities = oContribute.getEntityElements();
				if (aEntities!=null){
					Iterator itEntities = aEntities.iterator();
					while (itEntities.hasNext()){
						String sEntity = (String)itEntities.next();
						if (sAuthor.length()>0) sAuthor= sAuthor.concat(", ");
						sAuthor = sAuthor.concat(sEntity);
					}
				}
			}
		}
		return sAuthor;
	}	
	public void setLOMAuthor(String sAuthor){
		if (getLOM()!=null && sAuthor!=null){
			LifeCycle oLifeCycle = getLOM().getLifeCycle();
			if (oLifeCycle==null){
				oLifeCycle = new LifeCycle();
				getLOM().setLifeCycle(oLifeCycle);
			}
			
			Contribute oContribute = oLifeCycle.getFirstContributeElement();
			if (oContribute==null){
				oContribute = new Contribute();
				oLifeCycle.addContributeElement(oContribute);				
			}
			Role oRole = oContribute.getRole();
			if (oRole==null){
				oRole = new Role();
				oContribute.setRole(oRole);
				Date oDate = new Date();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				oDate.setDateTime(df.format(new java.util.Date()));
				oContribute.setDate(oDate);
			}
			oRole.setValue(Role.V_AUTHOR);
			oContribute.deleteEntityElements();
			StringTokenizer st = new StringTokenizer(sAuthor, ",");
			while (st.hasMoreTokens()){
				String sEntity = st.nextToken();
				oContribute.addEntityElement(sEntity);					
			}
		}
	}
	
	public String getLOMTitle(String sLanguage){
		String sTitle = null;
		General oGeneral = getGeneral();
		if (oGeneral!=null && oGeneral.getTitle()!=null){
			LangString oLangString = oGeneral.getTitle().getLangStringElement(sLanguage);
			if (oLangString!=null){
				sTitle = oLangString.getText();
			}
		}
		return sTitle;
	}	
	public void addLOMTitle(String sTitle, String sLanguage){
		if (getLOM()!=null){
			Title oTitle = getLOM().getGeneral().getTitle();
			LangString ls = oTitle.getLangStringElement(sLanguage);
			if (ls!=null){
				ls.setText(sTitle);
			}else{
				ls = new LangString(sTitle, sLanguage);
				oTitle.addLangStringElement(ls);
			}
		}
	}
	
	public String getLOMDescription(String sLanguage){
		String sDescription = null;
		General oGeneral = getGeneral();
		if (oGeneral!=null && oGeneral.getTitle()!=null){
			LangString oLangString = oGeneral.getDescription().getLangStringElement(sLanguage);
			if (oLangString!=null){
				sDescription = oLangString.getText();
			}
		}
		return sDescription;
	}	
	public void addLOMDescription(String sDescription, String sLanguage){
		if (getLOM()!=null){
			Description oDescription = getLOM().getGeneral().getDescription();
			LangString ls = oDescription.getLangStringElement(sLanguage);
			if (ls!=null){
				ls.setText(sDescription);
			}else{
				ls = new LangString(sDescription, sLanguage);
				oDescription.addLangStringElement(ls);
			}
		}
	}
	
	public String getLOMLanguage() {
		String sLanguage = null;
		General oGeneral = getGeneral();
		if (oGeneral!=null && oGeneral.getLanguageElements()!=null && !oGeneral.getLanguageElements().isEmpty()){
			sLanguage = (String)oGeneral.getLanguageElements().get(0);
		}
		return sLanguage;
	}
	public void setLOMLanguage(String sLanguage){
		if (getLOM()!=null){
			getLOM().getGeneral().deleteLanguageElements();
			getLOM().getGeneral().addLanguageElement(sLanguage);
			/* 
			// Specification supports <omore than one language, but QV only one
			if (sLang==null){
				getLOM().getGeneral().addLanguageElement(sLanguage);
			}*/
		}
	}
	
	public String getLOMArea(){
		String sArea = "";
		Classification oClass = getDisciplineClassification();
		if (oClass!=null && oClass.getKeywordElements()!=null && !oClass.getKeywordElements().isEmpty()){
			Keyword oKeyword = (Keyword)oClass.getKeywordElements().get(0);
			Iterator itLangString = oKeyword.getLangStringElements().iterator();
			if (itLangString.hasNext()){
				LangString ls = (LangString)itLangString.next();
				 sArea = ls.getText();
			}
		}
		return sArea;
	}
	public void addLOMArea(String sArea){
		addLOMArea(sArea, true);
	}	
	public void addLOMArea(String sArea, boolean bDel){
		Classification oClass = getDisciplineClassification();
		if (oClass!=null){
			if (bDel) oClass.deleteKeywordElements();
			Keyword oKeyword = new Keyword();
			oKeyword.addLangStringElement(new LangString(sArea));
			oClass.addKeywordElement(oKeyword);
		}
	}
	
	public String getLOMCopyright(){
		String sCopyright = "";
		Rights oRights = getRights();
		if (oRights!=null && oRights.getCopyrightAndOtherRestrictions()!=null){
			sCopyright = oRights.getCopyrightAndOtherRestrictions().getValue();
		}
		return sCopyright;
	}
	public void setLOMCopyright(String sCopyright){
		Rights oRights = getRights();
		if (oRights!=null && sCopyright!=null && oRights.getCopyrightAndOtherRestrictions()!=null){
			oRights.getCopyrightAndOtherRestrictions().setValue(sCopyright);
		}
	}
	public String getLOMLicense(){
		String sLicense = "";
		Rights oRights = getRights();
		if (oRights!=null && oRights.getDescription()!=null){
			sLicense = oRights.getDescription().getFirstLangStringValue();
		}
		return sLicense;
	}
	public void setLOMLicense(String sLicense){
		Rights oRights = getRights();
		if (oRights!=null && sLicense!=null){
			Description oDescription = oRights.getDescription();
			if (oDescription==null){
				oDescription = new Description();
				oRights.setDescription(oDescription);
			}
			LangString ls = oDescription.getFirstLangStringElement();
			if (ls==null){
				ls = new LangString(sLicense);
				oDescription.addLangStringElement(ls);
			}
			CDATA cd = new CDATA(sLicense);
			ls.setContent(cd);
		}
	}

	public void addLOMLearningContext(String sContext){
		if (getLOM()!=null){
			Educational oEducational = getEducational();
			Context oContext = new Context(sContext);
			oEducational.addContextElement(oContext);
		}
	}
	
    public void copyXSDFile(String path){
		
    }
	
	public String getSchemaLocation(){
		return  _LIBGlobal.CP_NAMESPACE_KEY + " " + CP_XSD_URL;
	}
    
}
