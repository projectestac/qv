package edu.xtec.qv.result;

import java.util.StringTokenizer;

import org.jdom.Element;

/*
 * QTIResponseParser.java
 *
 * Created on 13 de agosto de 2002, 18:17
 */


/**
 *
 * @author  Albert
 * @version
 */


public class QTIResponseParser extends Object {
    
    public static final String SET_ACTION="Set";
    public static final String ADD_ACTION="Add";
    public static final String SUBTRACT_ACTION="Subtract";
    public static final String MULTIPLY_ACTION="Multiply";
    public static final String DIVIDE_ACTION="Divide";
    public static final String DEFAULT_ACTION=SET_ACTION;
    public static final String RESPONSE_DELIMITATORS="#";
    public static final String ITEM_NAMESPACE_DELIM="-->"; //Les respostes de l'usuari arribaran en el format ITEM_IDENT-->RESPONSE_IDENT=RESPONSE_VALUE
    
    private static boolean verbose=true;
    
    private QTIUserResponsesSymtab userRespSymtab=null;
    private java.io.InputStream xmlSource=null;
    private static org.jdom.Document doc=null;
    private java.util.HashMap hmFeedback=null;
    private QualificationSymtabs qs=null;
    
    /** Creates new QTIResponseParser */
    public QTIResponseParser(java.io.InputStream xmlSource, java.util.HashMap hmFeedback){
        this.xmlSource=xmlSource;
        this.hmFeedback=hmFeedback;
        doc=null;
    }
    
    public void setFeedbackHashMap(java.util.HashMap hmFeedback){
        this.hmFeedback=hmFeedback;
    }
    
	public String getAssessmentName(){
		String sAssessmentName = null;
		try{
        
			if (doc==null) doc=getXMLDocument(xmlSource);
			if (doc!=null){
				Element eQuestestinterop=doc.getRootElement();
				if (eQuestestinterop!=null){
					Element eAssessment=eQuestestinterop.getChild("assessment", eQuestestinterop.getNamespace());
					if (eAssessment!=null){
						sAssessmentName = eAssessment.getAttributeValue("title");
					}
				}
			}
		}
		catch (Exception e){
			System.out.println("Excepció obtenint nom del quadern:"+e);
			if (verbose) e.printStackTrace(System.out);
		}
		return sAssessmentName;
	}

	public java.util.Hashtable getAssessmentMetadata(){
		java.util.Hashtable hMetadata=null;
		try{
        
			if (doc==null) doc=getXMLDocument(xmlSource);
			org.jdom.Element eQuestestinterop=doc.getRootElement();
			org.jdom.Element eAssessment=eQuestestinterop.getChild("assessment", eQuestestinterop.getNamespace());
			if (eAssessment!=null){
				org.jdom.Element eMetadata=eAssessment.getChild("qtimetadata", eQuestestinterop.getNamespace());
				if (eMetadata!=null){
					hMetadata = new java.util.Hashtable();
					java.util.Iterator it=eMetadata.getChildren("qtimetadatafield", eQuestestinterop.getNamespace()).iterator();
					while (it.hasNext()){
						org.jdom.Element eMetadataField = (org.jdom.Element)it.next();
						String sLabel = eMetadataField.getChildText("fieldlabel");
						String sEntry = eMetadataField.getChildText("fieldentry");
						//System.out.println("QTIResponseParser-> label="+sLabel+" entry="+sEntry);
						if (sLabel!=null && sEntry!=null)
							hMetadata.put(sLabel, sEntry);
					}
				}
			}
		}
		catch (Exception e){
			System.out.println("Excepció obtenint metadata del quadern:"+e);
			if (verbose) e.printStackTrace(System.out);
		}
		return hMetadata;
	}
    
    public java.util.Vector getSectionNames(){
        java.util.Vector vSectionNames=null;
        try{
        
            if (doc==null) doc=getXMLDocument(xmlSource);
            org.jdom.Element eQuestestinterop=doc.getRootElement();
            org.jdom.Element eAssessment=eQuestestinterop.getChild("assessment", eQuestestinterop.getNamespace());
            java.util.Iterator it=null;
            if (eAssessment!=null) it=eAssessment.getChildren("section", eQuestestinterop.getNamespace()).iterator();
            else it=eQuestestinterop.getChildren("section", eQuestestinterop.getNamespace()).iterator();
            vSectionNames=getSectionNames(it);
        }
        catch (Exception e){
			System.out.println("EXCEPTION: "+e);
            if (verbose) e.printStackTrace(System.out);
        }
        return vSectionNames;
    }
    
    private java.util.Vector getSectionNames(java.util.Iterator itSections){
        java.util.Vector vSectionNames=new java.util.Vector();
        if (itSections!=null){
            int iNumFull=1;
            while(itSections.hasNext()){
                org.jdom.Element eSection=(org.jdom.Element)itSections.next();
                Object oTitle=eSection.getAttributeValue("title");
                String sTitle=(oTitle!=null)?oTitle.toString():"Full "+iNumFull;
                vSectionNames.add(sTitle);
                iNumFull++;
            }
        }
        return vSectionNames;
    }
    
    public int getSectionNumber(){
        ////////////////////////Això s'ha d'optimitzar
        return getSectionNames().size();
    }
        
    public QTISymtab getFullPuntuation(int iNumFull, java.util.Vector vUserResponses){
        return getQuadernPuntuation(iNumFull,vUserResponses);
    }
    
    public QTISymtab getFullPuntuation(int iNumFull, String sUserResponses, QualificationSymtabs qs){
        this.qs=qs;
        return getQuadernPuntuation(iNumFull,sUserResponses);
    }
    
    public QTISymtab getQuadernPuntuation(java.util.Vector vUserResponses){
        return getQuadernPuntuation(-1,vUserResponses);
    }
    
    public QTISymtab getQuadernPuntuation(java.util.Vector vUserResponses,QualificationSymtabs qs){
        this.qs=qs;
        return getQuadernPuntuation(vUserResponses);
    }
    
    public QTISymtab getQuadernPuntuation(String sUserResponses){
        return getQuadernPuntuation(-1,sUserResponses);
    }
    
    protected java.util.Vector filterUserResponses(java.util.Vector vUserResponses){
        /* Eliminaré qualsevol final de resposta id-->preg=?????*????:????# doncs lo que va darrere són coordenades d'utilitat en els hotspot*/
        java.util.Vector vFiltered=new java.util.Vector();
        java.util.Enumeration e=vUserResponses.elements();
        while (e.hasMoreElements()){
            String s=e.nextElement().toString();
            vFiltered.add(filterUserResponses(s));
        }
        return vFiltered;
    }
    
    protected static String filterUserResponses(String sUserResponses){
        /* Eliminaré qualsevol final de resposta id-->preg=?????*????:????# doncs lo que va darrere són coordenades d'utilitat en els hotspot*/
        boolean bFinish=false;
        int index=0;
        while (!bFinish && index>=0){
            if (sUserResponses!=null && sUserResponses.indexOf('*',index)>0){
                int ast=sUserResponses.indexOf('*',index);
                int ig=sUserResponses.indexOf(':',ast);
                int par=sUserResponses.indexOf('#',ast);
                if (par>ig && ig>ast){
                    sUserResponses=sUserResponses.substring(0,ast)+sUserResponses.substring(par);
                    index=ig;
                }
                else index=par;
            }
            else bFinish=true;
        }
        return sUserResponses;
    }
    
    public QTISymtab getQuadernPuntuation(int iNumFull, java.util.Vector vUserResponses){
            /*System.out.println("user responses:");
            Utility.showVector(vUserResponses);*/
        //System.out.println("---------------");
        String sUserResponses="";
        if (vUserResponses!=null){
            java.util.Enumeration e=vUserResponses.elements();
            while (e.hasMoreElements()) {
                Object o=e.nextElement();
                sUserResponses+=(o!=null)?o.toString():"";
            }
        }
        return getQuadernPuntuation(iNumFull,sUserResponses);
    }
    
    public QTISymtab getQuadernPuntuation(int iNumFull, String sUserResponses){
        //int iPuntuation=0;
        sUserResponses=filterUserResponses(sUserResponses);
        QTISymtab symtab=null;
        try{
            if (doc==null) doc=getXMLDocument(xmlSource);
            org.jdom.Element root=doc.getRootElement();
            createUserRespSymtab(sUserResponses);
            symtab=parseQuestestinterop(iNumFull,root);
            //System.out.println("getQuadernPuntuation-> "+symtab);
        }
        catch (Exception e){
			System.out.println("EXCEPTION: "+e);
            if (verbose) e.printStackTrace(System.out);
        }
        //return iPuntuation;
        return symtab;
    }
    
    private QTISymtab parseQuestestinterop(int iNumFull, org.jdom.Element eQuestestinterop){
        float fPuntuation=0;
        QTISymtab symtab=null;
        try{
            ////java.util.HashMap hmFeedback=new java.util.HashMap();
            org.jdom.Element eAssessment=eQuestestinterop.getChild("assessment", eQuestestinterop.getNamespace());
            //iPuntuation=parseAssessment(eAssessment,eQuestestinterop,hmFeedback);
            java.util.Vector vOutcomesSymtabs=parseAssessment(iNumFull,eAssessment,eQuestestinterop,hmFeedback);
            //-> Millor que lo que ve ara ho controli l'assessment pq sap si hi ha outcomes processing, etc , però de moment ho faig aquí
            java.util.Enumeration e=vOutcomesSymtabs.elements();
            ////System.out.println(showSymtabVector("ASSESSMENT SYMTAB",vOutcomesSymtabs));
            while (e.hasMoreElements()){
                symtab=(QTISymtab)e.nextElement();
				if (symtab.getScore() instanceof Integer) fPuntuation+=((Integer)symtab.getScore()).intValue();
				else if (symtab.getScore() instanceof Float) fPuntuation+=((Float)symtab.getScore()).floatValue();
                //System.out.println("ASSESSMENT SYMTAB");
                //System.out.println(symtab.toString());
                //System.out.println("-----------------");
                
                            /*Object score=symtab.getScore();
                            System.out.println("final TS:\n"+symtab);
                            if (score!=null){
                                System.out.println("TOTAL SCORE:"+score.toString());
                                if (score instanceof Integer) iPuntuation=((Integer)score).intValue(); //POTSER NO ÉS INT!!!
                                else if (score instanceof Float) iPuntuation=(int)(((Float)score).floatValue());
                                //else iPuntuation=0;
                            }*/
                //else iPuntuation=0;
            }
            symtab.addVar("SCORE", QTISymtab.DECIMAL_TYPE, String.valueOf(fPuntuation));
            //else iPuntuation=0;
            //<-
            ////showSymtabVector(vOutcomesSymtabs);
            //showHashMap("feedback:",hmFeedback);
        }
        catch (Exception e){
			System.out.println("EXCEPTION: "+e);
            if (verbose) e.printStackTrace(System.out);
        }
        //System.out.println("Punts:"+iPuntuation);
        return symtab;
    }
    
    private java.util.Vector parseAssessment(int iNumFull, org.jdom.Element eAssessment, org.jdom.Element eQuestestinterop, java.util.HashMap hmFeedback){
        /* eAssessment pot ser null*/
        int iPuntuation=0;
        java.util.Vector vOutcomesSymtabs=new java.util.Vector();
        try{
            java.util.Vector vAllSymtabs=new java.util.Vector();
            
            if (eAssessment!=null){
                String sAssessmentName=eAssessment.getAttributeValue("ident").toString(); //mandatory
                
                java.util.Iterator it=eAssessment.getChildren("section", eQuestestinterop.getNamespace()).iterator();
                int iCurrentSection=1;
                while(it.hasNext()){ /*Ha d'haber-hi un a no ser que sigui sectionref...*/
                    org.jdom.Element eSection=(org.jdom.Element)it.next();
                    if (iNumFull==-1 || iCurrentSection==iNumFull){
                        ////System.out.println("Tracto el full "+iNumFull);
                        //iPuntuation+=parseSection(eSection,hmFeedback);
                        java.util.Vector vOutcomesSection=parseSection(eSection,hmFeedback);
                        ////System.out.println("vOutcomesSection.size()="+vOutcomesSection.size());
                        vAllSymtabs.addAll(vOutcomesSection);
                    }
                    iCurrentSection++;
                }
                ////System.out.println(showSymtabVector("SECTION SYMTAB",vAllSymtabs));
                if (it.hasNext()){
	                it=eAssessment.getChildren("outcomes_processing", eQuestestinterop.getNamespace()).iterator();
	                while(it.hasNext()){
	                    org.jdom.Element eOutcomesProcessing=(org.jdom.Element)it.next();
	                    QTISymtab symtab=parseOutcomesProcessing(eOutcomesProcessing,vAllSymtabs,sAssessmentName);
	                    vOutcomesSymtabs.add(symtab);
	                }
				} else{
					vOutcomesSymtabs = vAllSymtabs;
				}
                //System.out.println("parseAssessment-> "+vOutcomesSymtabs);
            }
            else{ //NO hi ha assessment
                java.util.Iterator it=eQuestestinterop.getChildren("section", eQuestestinterop.getNamespace()).iterator();
                int iCurrentSection=1;
                while(it.hasNext()){
                    org.jdom.Element eSection=(org.jdom.Element)it.next();
                    if (iNumFull==-1 || iCurrentSection==iNumFull){
                        ////System.out.println("Tracto el full "+iNumFull);
                        //iPuntuation+=parseSection(eSection,hmFeedback);
                        java.util.Vector vOutcomesSection=parseSection(eSection,hmFeedback);
                        vOutcomesSymtabs.addAll(vOutcomesSection);
                        
                    }
                    iCurrentSection++;
                }
                it=eQuestestinterop.getChildren("item", eQuestestinterop.getNamespace()).iterator();
                while(it.hasNext()){
                    org.jdom.Element eItem=(org.jdom.Element)it.next();
                    //iPuntuation+=parseItem(eItem,hmFeedback);
                    QTISymtab symtab=parseItem(eItem,hmFeedback);
                    vOutcomesSymtabs.add(symtab);
                }
            }
        }
        catch (Exception e){
			System.out.println("EXCEPTION: "+e);
            if (verbose) e.printStackTrace(System.out);
        }
        //return iPuntuation;
        return vOutcomesSymtabs;
    }
    
    private java.util.Vector parseSection(org.jdom.Element eSection, java.util.HashMap hmFeedback){
        int iPuntuation=0;
        java.util.Vector vOutcomesSymtabs=new java.util.Vector();
        try{
            java.util.Vector vAllSymtabs=new java.util.Vector();
            String sSectionName=eSection.getAttributeValue("ident").toString(); //mandatory
			//System.out.println("parseSection-> "+sSectionName);
            java.util.Iterator it=eSection.getChildren("item", eSection.getNamespace()).iterator();
            while(it.hasNext()){
                org.jdom.Element eItem=(org.jdom.Element)it.next();
                //iPuntuation+=parseItem(eItem,hmFeedback);
                QTISymtab symtab=parseItem(eItem,hmFeedback);
                vAllSymtabs.add(symtab);
            }
            if (qs!=null) qs.addSectionSymtab(sSectionName,vAllSymtabs);
            ////System.out.println(showSymtabVector("ITEM SYMTABS",vAllSymtabs));
            it=eSection.getChildren("outcomes_processing", eSection.getNamespace()).iterator();
            if (it.hasNext()){
                while(it.hasNext()){
                    org.jdom.Element eOutcomesProcessing=(org.jdom.Element)it.next();
                    QTISymtab symtab=parseOutcomesProcessing(eOutcomesProcessing,vAllSymtabs,sSectionName);
                    if (qs!=null) qs.addAssessmentSymtab(sSectionName,symtab);
                    vOutcomesSymtabs.add(symtab);
                }
            }
            else vOutcomesSymtabs=vAllSymtabs;
			//System.out.println("parseSection-> "+vOutcomesSymtabs);
        }
        catch (Exception e){
			System.out.println("EXCEPTION: "+e);
            if (verbose) e.printStackTrace(System.out);
        }
        //return iPuntuation;
        return vOutcomesSymtabs;
    }
    
    private QTISymtab parseOutcomesProcessing(org.jdom.Element eOutcomesProcessing, java.util.Vector vAllSymtabs, String sSectionName){
        QTISymtab symtab=new QTISymtab();
        //System.out.println("SYMTABS:"+vAllSymtabs.size()+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        try{
            QTISymtab.cleanMapInput(vAllSymtabs); /* Els Maps només afecten a l'outcomes_processing on
                        estan continguts. Per motius d'eficiència les taules de símbols obtingudes en fer el processat
                        de la resposta de l'usuari només es calculen un cop i surt mes a compte esborrar els
                        mapeigs (Cost O(1)) que duplicar cadascuna de les taules de símbols. */
            
            String sScoreModel=eOutcomesProcessing.getAttributeValue("scoremodel",QTIScoringAlgorithms.DEFAULT_SCOREMODEL);
            Object oOutcomes=eOutcomesProcessing.getChild("outcomes", eOutcomesProcessing.getNamespace());
            if (oOutcomes!=null) //és obligatori, però...
                parseOutcomes((org.jdom.Element)oOutcomes,symtab);
            
            java.util.Vector vSelectedObjects;
            java.util.Iterator it=eOutcomesProcessing.getChildren("objects_condition", eOutcomesProcessing.getNamespace()).iterator();
            if (it.hasNext()){
                vSelectedObjects=new java.util.Vector();
                while(it.hasNext()){
                    org.jdom.Element eObjectsCondition=(org.jdom.Element)it.next();
                    vSelectedObjects.addAll(parseObjectsCondition(eObjectsCondition,vAllSymtabs));
                }
            }
            else vSelectedObjects=vAllSymtabs;
            
            it=eOutcomesProcessing.getChildren("processing_parameter", eOutcomesProcessing.getNamespace()).iterator();
            while(it.hasNext()){
                org.jdom.Element eProcessingParameter=(org.jdom.Element)it.next();
                /*symtab=*/parseProcessingParameter(eProcessingParameter,symtab);
            }
            
            it=eOutcomesProcessing.getChildren("map_output", eOutcomesProcessing.getNamespace()).iterator();
            while(it.hasNext()){
                org.jdom.Element eMapOutput=(org.jdom.Element)it.next();
                /*symtab=*/parseMapOutput(eMapOutput,symtab);
            }

            symtab=QTIScoringAlgorithms.execScoringAlgorithm(sScoreModel,vAllSymtabs,vSelectedObjects,userRespSymtab,symtab);
            
            it=eOutcomesProcessing.getChildren("outcomes_feedback_test", eOutcomesProcessing.getNamespace()).iterator();
            while(it.hasNext()){
                org.jdom.Element eOutcomesFeedbackTest=(org.jdom.Element)it.next();
                /*symtab=*/parseOutComesFeedbackTest(eOutcomesFeedbackTest,symtab,sSectionName);
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
        return symtab;
    }
    
    private java.util.Vector parseObjectsCondition(org.jdom.Element eObjectsCondition, java.util.Vector vAllSymtabs){
        java.util.Vector vSelectedObjects=new java.util.Vector();
        try{
            // Només pot tenir un fill entre outcomes_metadata, and_objects, or_objects i not_objects
            if (eObjectsCondition.getChild("outcomes_metadata", eObjectsCondition.getNamespace())!=null) vSelectedObjects=parseBooleanObject((org.jdom.Element)eObjectsCondition.getChild("outcomes_metadata"),vAllSymtabs);
            else if (eObjectsCondition.getChild("and_objects", eObjectsCondition.getNamespace())!=null) vSelectedObjects=parseBooleanObject((org.jdom.Element)eObjectsCondition.getChild("and_objects"),vAllSymtabs);
            else if (eObjectsCondition.getChild("or_objects", eObjectsCondition.getNamespace())!=null) vSelectedObjects=parseBooleanObject((org.jdom.Element)eObjectsCondition.getChild("or_objects"),vAllSymtabs);
            else if (eObjectsCondition.getChild("not_objects", eObjectsCondition.getNamespace())!=null) vSelectedObjects=parseBooleanObject((org.jdom.Element)eObjectsCondition.getChild("not_objects"),vAllSymtabs);
            else vSelectedObjects=vAllSymtabs;
            
            java.util.Iterator it=eObjectsCondition.getChildren("objects_parameter", eObjectsCondition.getNamespace()).iterator();
            while(it.hasNext()){
                org.jdom.Element eObjectsParameter=(org.jdom.Element)it.next();
                parseObjectsParameter(eObjectsParameter,vSelectedObjects);
            }
            
            it=eObjectsCondition.getChildren("map_input", eObjectsCondition.getNamespace()).iterator();
            while(it.hasNext()){
                org.jdom.Element eMapInput=(org.jdom.Element)it.next();
                parseMapInput(eMapInput,vSelectedObjects);
            }
            
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
        return vSelectedObjects;
    }
    
    private java.util.Vector parseOutcomesMetadata(org.jdom.Element eOutcomesMetadata, java.util.Vector vAllSymtabs){
        java.util.Vector vSelectedObjects=new java.util.Vector();
        String sOperator=eOutcomesMetadata.getAttributeValue("mdoperator",""); //mandatory
        String sOperand2=eOutcomesMetadata.getText();
        String sVarName=eOutcomesMetadata.getAttributeValue("mdname",""); //mandatory
        if (sOperand2!=null) sOperand2=sOperand2.trim();
        java.util.Enumeration e=vAllSymtabs.elements();
        while (e.hasMoreElements()){
            QTISymtab st=(QTISymtab)e.nextElement();
            if (st.contains(sVarName)){
                String sOperand1=st.getValue(sVarName).toString();
                boolean bValue=parseBooleanTest(sOperator,sOperand1,sOperand2);
                if (bValue) vSelectedObjects.add(st);
            } // Si no el té, no l'afegim.
        }
        return vSelectedObjects;
    }
    
    private boolean parseBooleanTest(String sOperator,String sOperand1,String sOperand2){
        boolean bValue=false;
        if (sOperator.equals("EQ")) bValue=isEqualNoMultivalue(sOperand1,sOperand2);
        else if (sOperator.equals("NEQ")) bValue=!isEqualNoMultivalue(sOperand1,sOperand2);
        else if (sOperator.equals("LT")) bValue=isLeast(sOperand1,sOperand2);
        else if (sOperator.equals("LTE")) bValue=isLeast(sOperand1,sOperand2)||isEqualNoMultivalue(sOperand1,sOperand2);
        else if (sOperator.equals("GT")) bValue=!isLeast(sOperand1,sOperand2) && !isEqualNoMultivalue(sOperand1,sOperand2);
        else if (sOperator.equals("GTE")) bValue=!isLeast(sOperand1,sOperand2);
        else{
            System.out.println("Error: El comparador "+sOperator+" no està admés.");
        }
        return bValue;
    }
    
    private java.util.Vector parseBooleanObject(org.jdom.Element eBooleanObject, java.util.Vector vSymtabs){
        java.util.HashSet hsSelectedSymtabs=null;
        String sBooleanOp=eBooleanObject.getName();
        try{
            if (sBooleanOp.equals("outcomes_metadata")) return parseOutcomesMetadata(eBooleanObject,vSymtabs);
            else{
                java.util.Iterator it=eBooleanObject.getChildren().iterator();
                if (sBooleanOp.equals("and_objects")){
                    hsSelectedSymtabs=new java.util.HashSet(vSymtabs);
                    while(it.hasNext()){
                        org.jdom.Element eBooleanObject2=(org.jdom.Element)it.next();
                        java.util.Vector v2=parseBooleanObject(eBooleanObject2,vSymtabs);
                        hsSelectedSymtabs.retainAll(v2); // A hsSS es queda la intersecció entre v i v2.
                    }//S'ha de fer la intersecció entre els elements del Vector anterior (all) amb els dels nodes que conté
                }
                else if (sBooleanOp.equals("or_objects")){
                    hsSelectedSymtabs=new java.util.HashSet();
                    while(it.hasNext()){
                        org.jdom.Element eBooleanObject2=(org.jdom.Element)it.next();
                        java.util.Vector v2=parseBooleanObject(eBooleanObject2,vSymtabs);
                        hsSelectedSymtabs.addAll(v2); // A hsSS es queda la unió entre vSelectedSymtabs i v2.
                    }
                }
                else if (sBooleanOp.equals("not_objects")){
                    hsSelectedSymtabs=new java.util.HashSet(vSymtabs);
                    while(it.hasNext()){
                        org.jdom.Element eBooleanObject2=(org.jdom.Element)it.next();
                        java.util.Vector v2=parseBooleanObject(eBooleanObject2,vSymtabs);
                        hsSelectedSymtabs.removeAll(v2); // A hsSS es queden els elements que no són a v2.
                    }
                }
                else{
                    System.out.println("Error: Operador booleà "+sBooleanOp+" no acceptat.");
                }
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
        return new java.util.Vector(hsSelectedSymtabs);
    }
    
    private void parseObjectsParameter(org.jdom.Element eObjectsParameter,java.util.Vector vSelectedObjects){
        String sParamName=eObjectsParameter.getAttributeValue("pname","");
        String sValue=eObjectsParameter.getText();
        if (sValue!=null) sValue=sValue.trim();
        java.util.Enumeration e=vSelectedObjects.elements();
        //System.out.println("pname:"+sParamName+" value:"+sValue+".");
        //System.out.println("Selected:"+vSelectedObjects.size());
        while (e.hasMoreElements()){
            QTISymtab st=(QTISymtab)e.nextElement();
            st.addVar(QTIScoringAlgorithms.PARAM_PREFIX+sParamName,QTISymtab.STRING_TYPE,sValue,true);
                        /*No és imprescindible posar param. davant del nom, però evitarà
                        ambigüetats en el cas que l'objecte tingui també metadata amb el mateix
                        nom. En el cas que pasés prevaldria l'objects parameter, però millor indicar-ho explicitament.*/
        }
    }
    
    private void parseProcessingParameter(org.jdom.Element eProcessingParameter, QTISymtab processingParameterSymtab){
        String sParamName=eProcessingParameter.getAttributeValue("pname",""); //mandatory
        String sValue=eProcessingParameter.getText();
        //System.out.println("pname:"+sParamName+" value:"+sValue+".");
        if (sValue!=null) sValue=sValue.trim();
        processingParameterSymtab.addVar(sParamName,QTISymtab.STRING_TYPE,sValue,true);
    }
    
    private void parseMapInput(org.jdom.Element eMapInput, java.util.Vector vSelectedObjects){
        String sVarNameFrom=eMapInput.getAttributeValue("varname",QTISymtab.DEFAULT_VARNAME); //optional
        String sVarNameTo=eMapInput.getText();
        
        java.util.Enumeration e=vSelectedObjects.elements();
        while (e.hasMoreElements()){
            QTISymtab st=(QTISymtab)e.nextElement();
            st.mapInput(sVarNameFrom,sVarNameTo);
        }
    }
    
    private void parseMapOutput(org.jdom.Element eMapOutput, QTISymtab symtab){
        String sVarNameFrom=eMapOutput.getAttributeValue("varname",QTISymtab.DEFAULT_VARNAME).trim(); //optional
        String sVarNameTo=eMapOutput.getText().trim();
        //System.out.println("Map output from:"+sVarNameFrom+" to:"+sVarNameTo+"<--");
        symtab.mapOutput(sVarNameFrom,sVarNameTo);
    }
    
    private void parseOutComesFeedbackTest(org.jdom.Element eOutcomesFeedbackTest, QTISymtab symtab, String sSectionName){
        try{
            org.jdom.Element eTestVariable=eOutcomesFeedbackTest.getChild("test_variable", eOutcomesFeedbackTest.getNamespace()); //mandatory
            boolean bValue;
            if (eTestVariable!=null) bValue=parseTestVariable(eTestVariable,symtab); //sempre
            else bValue=false;
            
            if (bValue){
                java.util.Iterator it=eOutcomesFeedbackTest.getChildren("displayfeedback", eOutcomesFeedbackTest.getNamespace()).iterator();
                while(it.hasNext()){
                    org.jdom.Element eDisplayfeedback=(org.jdom.Element)it.next();
                    parseDisplayfeedback(eDisplayfeedback,hmFeedback,sSectionName);
                }
            }
            
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
    }
    
    private boolean parseTestVariable(org.jdom.Element eTestVariable, QTISymtab symtab){
        boolean bValue;
        try{
            if (eTestVariable.getChild("variable_test", eTestVariable.getNamespace())!=null) bValue=parseBooleanTestVariable(eTestVariable.getChild("variable_test", eTestVariable.getNamespace()),symtab);
            else if (eTestVariable.getChild("and_test", eTestVariable.getNamespace())!=null) bValue=parseBooleanTestVariable(eTestVariable.getChild("and_test", eTestVariable.getNamespace()),symtab);
            else if (eTestVariable.getChild("or_test", eTestVariable.getNamespace())!=null) bValue=parseBooleanTestVariable(eTestVariable.getChild("or_test", eTestVariable.getNamespace()),symtab);
            else if (eTestVariable.getChild("not_test", eTestVariable.getNamespace())!=null) bValue=parseBooleanTestVariable(eTestVariable.getChild("not_test", eTestVariable.getNamespace()),symtab);
            else bValue=true; //No és possible
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
            bValue=false;
        }
        return bValue;
    }
    
    private boolean parseBooleanTestVariable(org.jdom.Element eBooleanTestVariable, QTISymtab symtab){
        boolean bValue=false;
        String sBooleanOp=eBooleanTestVariable.getName();
        try{
            if (sBooleanOp.equals("variable_test")) return parseVariableTest(eBooleanTestVariable,symtab);
            else{
                java.util.Iterator it=eBooleanTestVariable.getChildren().iterator();
                if (sBooleanOp.equals("and_test")){
                    bValue=true;
                    while(it.hasNext() && bValue){
                        org.jdom.Element eBooleanTestVariable2=(org.jdom.Element)it.next();
                        bValue=/*bValue&&*/parseBooleanTestVariable(eBooleanTestVariable2,symtab);
                    }
                }
                else if (sBooleanOp.equals("or_test")){
                    bValue=false;
                    while(it.hasNext() && !bValue){
                        org.jdom.Element eBooleanTestVariable2=(org.jdom.Element)it.next();
                        bValue=bValue||parseBooleanTestVariable(eBooleanTestVariable2,symtab);
                    }
                }
                else if (sBooleanOp.equals("not_test")){
                    bValue=true;
                    while(it.hasNext() && bValue){ //En el cas que tingui diversos fills faig el not de l'and d'aquests.
                        org.jdom.Element eBooleanTestVariable2=(org.jdom.Element)it.next();
                        bValue=bValue&&parseBooleanTestVariable(eBooleanTestVariable2,symtab);
                    }
                    bValue=!bValue;
                }
                else{
                    System.out.println("Error: Operador booleà "+sBooleanOp+" no acceptat.");
                }
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
        return bValue;
    }
    
    private boolean parseVariableTest(org.jdom.Element eBooleanTestVariable, QTISymtab symtab){
        boolean bValue;
        String sOperator=eBooleanTestVariable.getAttributeValue("testoperator",""); //mandatory
        String sOperand2=eBooleanTestVariable.getText();
        String sVarName=eBooleanTestVariable.getAttributeValue("varname",QTISymtab.DEFAULT_VARNAME); //optional
        if (symtab.contains(sVarName)){
            String sOperand1=symtab.getValue(sVarName).toString();
            bValue=parseBooleanTest(sOperator,sOperand1,sOperand2);
        }
        else{
            //System.out.println("Error: Variable "+sVarName+" no declarada.");
            bValue=false;
        }
        return bValue;
    }
    
    private QTISymtab parseItem(org.jdom.Element eItem, java.util.HashMap hmFeedback){
        //System.out.println("$$$ parseItem $$$");
        int iPuntuation=0;
        QTISymtab symtab=new QTISymtab();
        try{
            Object oItemMetadata=eItem.getChild("itemmetadata", eItem.getNamespace()); //optional
            if (oItemMetadata!=null){
                parseItemMetadata((org.jdom.Element)oItemMetadata,symtab);
            }
            String sItemName=eItem.getAttributeValue("ident"); //required
			//System.out.println("QTIResponseParser.parseItem()-> "+sItemName);
            //HAIG D'ESBORRAR LA SEGÜENT LINIA
            symtab.addVar(".IDENT",QTISymtab.STRING_TYPE,sItemName);
            
            
        	java.util.Iterator it=eItem.getChildren("resprocessing", eItem.getNamespace()).iterator();
        	while(it.hasNext()){
        		org.jdom.Element eResprocessing=(org.jdom.Element)it.next();
        		//iPuntuation+=parseResprocessing(eResprocessing,hmFeedback,sItemName,symtab);
        		//System.out.println("QTIResponseParser.parseItem.parseResprocessing()-> "+sItemName);
        		symtab=parseResprocessing(eResprocessing,hmFeedback,sItemName,symtab);
        	}

        	if (userRespSymtab.contains(sItemName, sItemName+"_MANUALSCORE")){//Albert
            	//S'ha establert la puntuació de forma manual
            	Object manualScore = userRespSymtab.getValue(sItemName, sItemName+"_MANUALSCORE");
            	if (manualScore!=null){
            		symtab.setVar("SCORE",manualScore.toString().trim());
            		//System.out.println("symtab.setVar(\"SCORE\","+manualScore.toString().trim()+");");
    				//"Correct", "True"
            	}
            } 
        	if (userRespSymtab.contains(sItemName, sItemName+"_MANUALCORRECT")){//Albert
            	//S'ha establert la correcció de forma manual
            	Object manualCorrect = userRespSymtab.getValue(sItemName, sItemName+"_MANUALCORRECT");
            	if (manualCorrect!=null){
            		symtab.setVar("CORRECT",manualCorrect.toString().trim());
            		//System.out.println("symtab.setVar(\"SCORE\","+manualScore.toString().trim()+");");
    				//"Correct", "True"
            	}
            } 
            //System.out.println("parseItem-> SYMTAB"+symtab+"  \nFEEDBACK="+hmFeedback);
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
        //return iPuntuation;
        return symtab;
    }
    
    private QTISymtab parseResprocessing(org.jdom.Element eResprocessing,java.util.HashMap hmFeedback, String sItemName, QTISymtab symtab){
        //System.out.println("resprocessing "+sItemName);
        int iPuntuation=0;
        try{
            org.jdom.Element eOutcomes=eResprocessing.getChild("outcomes", eResprocessing.getNamespace());
            /*symtab=*/parseOutcomes(eOutcomes,symtab);
            
            java.util.Iterator it=eResprocessing.getChildren("respcondition", eResprocessing.getNamespace()).iterator();
            boolean bOneConditionDone=false; //Indica si ja s'ha assolit alguna de les condicions. Serveix per a poder mostrar les "other".
            boolean bContinue=true;
            while(it.hasNext() && bContinue){
                org.jdom.Element eRespcondition=(org.jdom.Element)it.next();
                boolean bValue=parseRespcondition(eRespcondition,symtab,hmFeedback,sItemName,bOneConditionDone); // hereta la taula de simbols sintetitzada per outcomes, i la sintetiza per al següent respcondition
                bOneConditionDone=bOneConditionDone||bValue;
                String sContinue=eRespcondition.getAttributeValue("continue"); //optional
                if (sContinue==null) sContinue="No";
                boolean bWantContinue=sContinue.trim().toLowerCase().equals("yes");
                bContinue=!(bValue && !bWantContinue);
            }
            //System.out.println("parseResprocessing-> "+symtab);
            //->
            Object score=symtab.getScore();
            if (score!=null){
                //System.out.println("SCORE:"+score.toString());
                if (score instanceof Integer) iPuntuation=((Integer)score).intValue(); //POTSER NO ÉS INT!!!
                else iPuntuation=0;
            }
            else iPuntuation=0;
            //<-
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
            //return 0;
        }
        //return iPuntuation;
        return symtab;
    }
    
    private void parseOutcomes(org.jdom.Element eOutcomes, QTISymtab symtab){
        /* otcomes hereta la taula de simbols i també la sintetitza*/
        try{
            java.util.Iterator it=eOutcomes.getChildren("decvar", eOutcomes.getNamespace()).iterator();
            while(it.hasNext()){ //Afegim totes les variables a la taula de simbols.
                org.jdom.Element eDecvar=(org.jdom.Element)it.next();
                parseDecvar(eDecvar,symtab);
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
    }
    
    private void parseDecvar(org.jdom.Element eDecvar, QTISymtab symtab){
        try{
            String sVarname=eDecvar.getAttributeValue("varname"); //optional
            if (sVarname==null) sVarname=QTISymtab.DEFAULT_VARNAME;
            String sType=eDecvar.getAttributeValue("vartype"); //required
            if (sType==null) sType=QTISymtab.INTEGER_TYPE;
            String sDefaultval=eDecvar.getAttributeValue("defaultval"); //optional
            //System.out.println("parseDecvar-> "+sVarname+"="+sDefaultval);
            symtab.addVar(sVarname,sType,sDefaultval);
            /*Per cadascun dels atributs que conté decvar s'ha de declarar una variable p.ex SCORE.maxvalue */
            if (sType.equalsIgnoreCase(QTISymtab.INTEGER_TYPE) || sType.equalsIgnoreCase(QTISymtab.DECIMAL_TYPE) || sType.equalsIgnoreCase(QTISymtab.SCIENTIFIC_TYPE)){
                java.util.Iterator it=eDecvar.getAttributes().iterator();
                while(it.hasNext()){ //Afegim totes les variables a la taula de simbols.
                    org.jdom.Attribute aSubvar=(org.jdom.Attribute)it.next();
                    String sSubvarName=aSubvar.getName();
                    if (!sSubvarName.equals("varname") && !sSubvarName.equals("vartype")){
                        symtab.addVar(sVarname+"."+sSubvarName,sType,aSubvar.getValue(),true); //isSubvar
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
    }
    
    private boolean parseRespcondition(org.jdom.Element eRespcondition, QTISymtab symtab, java.util.HashMap hmFeedback, String sItemName, boolean bOneConditionDone){
        /* respcondition hereta la taula de simbols i també la sintetitza per al següent respcondition*/
        /* retorna el valor de ConditionVar, és a dir, si es cumpleixen les condicions.*/
        boolean bValue=false;
        try{
            //System.out.println("$$$ parseRespcondition $$$");
            org.jdom.Element eConditionvar=eRespcondition.getChild("conditionvar", eRespcondition.getNamespace());
            bValue=parseConditionvar(eConditionvar,symtab,sItemName,bOneConditionDone);
            boolean bDeclared=userRespSymtab.containsKey(sItemName);
			//logger.debug("bValue?"+bValue+" declared?"+bDeclared+" item="+sItemName);
            if (bValue && bDeclared){
                java.util.Iterator it=eRespcondition.getChildren("setvar", eRespcondition.getNamespace()).iterator();
                while(it.hasNext()){
                    org.jdom.Element eSetvar=(org.jdom.Element)it.next();
                    parseSetvar(eSetvar,symtab);
                }
                it=eRespcondition.getChildren("displayfeedback", eRespcondition.getNamespace()).iterator();
                while(it.hasNext()){
                    org.jdom.Element eDisplayfeedback=(org.jdom.Element)it.next();
                    parseDisplayfeedback(eDisplayfeedback,hmFeedback,sItemName);
                }
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
        return bValue;
    }
    
    private boolean parseConditionvar(org.jdom.Element eConditionvar, QTISymtab symtab, String sItemName, boolean bOneConditionDone){
            /* Cal l'ItemName perquè l'identificador de les respostes de l'usuari només és únic dintre del node item, és a dir,
             podeden existir identificadors de resposta identics en items diferents. */
        //System.out.println("$$$ parseConditionVar $$$");
        boolean bValue=true;
        try{
            java.util.Iterator it=eConditionvar.getChildren().iterator();
            while(it.hasNext() && bValue){
                org.jdom.Element eBooleanOp=(org.jdom.Element)it.next();
                bValue=(bValue&&parseBooleanOp(eBooleanOp,symtab,sItemName,bOneConditionDone));
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
        return bValue;
    }
    
    private boolean parseBooleanOp(org.jdom.Element eBooleanOp, QTISymtab symtab, String sItemName, boolean bOneConditionDone){
        boolean bValue=false;
        try{
            String nodeName=eBooleanOp.getName();
            //System.out.println("$$$ nodeName:"+nodeName+"  itemName:"+sItemName+" <--");
            if (nodeName.equals("not")) bValue=parseNot(eBooleanOp,symtab,sItemName,bOneConditionDone);
            else if (nodeName.equals("and")) bValue=parseAnd(eBooleanOp,symtab,sItemName,bOneConditionDone);
            else if (nodeName.equals("or")) bValue=parseOr(eBooleanOp,symtab,sItemName,bOneConditionDone);
            else if (nodeName.equals("other")) {
                if (!bOneConditionDone) bValue=true;
            }
            else{
                String sRespident=eBooleanOp.getAttributeValue("respident"); //required
                String sIndex=eBooleanOp.getAttributeValue("index"); //optional: podria ser null
                int iIndex=-1;
                try{
                    if (sIndex!=null) iIndex=Integer.parseInt(sIndex);
                }
                catch(Exception ex){
                    System.out.println("Error en el format de l'index:"+ex);
                }
                String sOperand=eBooleanOp.getText();
                boolean bDeclared=userRespSymtab.contains(sItemName,sRespident) || (userRespSymtab.containsKey(sItemName) && nodeName.equals("varsubset"));
				//logger.debug("declared?"+bDeclared+"  item="+sItemName+" resp="+sRespident);
                /////if (!bDeclared) System.out.println("!Declared "+sItemName+" "+sRespident+"<--");
                //else //
				if (bDeclared){
	                String sUserRespValue=bDeclared?getUserRespValue(sRespident,sItemName,sIndex):"";
					//logger.debug("userResp="+sUserRespValue+" operand="+sOperand);
	                if (nodeName.equals("varequal")) {if (bDeclared) bValue=isEqual(sItemName,sRespident,sOperand,eBooleanOp.getAttributeValue("case"));}
	                else if (nodeName.equals("varlt")) {if (bDeclared) bValue=isLeast(sUserRespValue,sOperand);}
	                else if (nodeName.equals("varlte")) {if (bDeclared) bValue=isLeast(sUserRespValue,sOperand)||isEqual(sItemName,sRespident,sOperand);}
	                else if (nodeName.equals("vargt")) {if (bDeclared) bValue=!isLeast(sUserRespValue,sOperand) && !isEqual(sItemName,sRespident,sOperand);}
	                else if (nodeName.equals("vargte")) {if (bDeclared) bValue=!isLeast(sUserRespValue,sOperand);}
	                else if (nodeName.equals("varsubset")) {
	                    //System.out.println("varsubset1");
	                    //////////if (bDeclared) bValue=isSubset(sItemName,sRespident,sOperand,iIndex);
	                    /*if (bDeclared) */
	                    //bValue=isSubset(sItemName,sRespident,sOperand,iIndex);
	                    //System.out.println("varsubset2");
	                    boolean bExact = true;
	                    if (eBooleanOp.getAttributeValue("setmatch")!=null){
	                    	bExact = eBooleanOp.getAttributeValue("setmatch").equalsIgnoreCase("Exact");
	                    }
						bValue=isSubset(sItemName,sRespident,sOperand,iIndex, bExact);
	                }
	                else if (nodeName.equals("varsubstring")) {if (bDeclared) bValue=isSubstring(sUserRespValue,sOperand,eBooleanOp.getAttributeValue("case"));}
	                ////else if (nodeName.equals("unanswered")) bValue=!bDeclared;
	                else if (nodeName.equals("unanswered")){
	                    bValue=(!bDeclared || sUserRespValue==null || sUserRespValue.trim().length()==0);
	                    //System.out.println("unanswered: bDecl="+bDeclared+" Resp="+sUserRespValue+" bValue:"+bValue+"<--");
	                }
	                else{
	                    System.out.println("Excepció: conditionvar "+nodeName+" no suportada.");
	                }
					//System.out.println("nodeName:"+nodeName+" resp:"+sUserRespValue+" op:"+sOperand+" bDeclared:"+bDeclared);
				}
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
        return bValue;
    }
    
    private boolean parseNot(org.jdom.Element eNot, QTISymtab symtab, String sItemName, boolean bOneConditionDone){
        boolean bValue=false;
        try{
            java.util.Iterator it=eNot.getChildren().iterator();
            if(it.hasNext()){
                org.jdom.Element eBooleanOp=(org.jdom.Element)it.next();
                bValue=!parseBooleanOp(eBooleanOp,symtab,sItemName,bOneConditionDone);
            }
            if(it.hasNext()){
                System.out.println("Excepció: Not de mes d'una operació booleana sense connectiva.");
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
        return bValue;
    }
    
    private boolean parseAnd(org.jdom.Element eAnd, QTISymtab symtab, String sItemName, boolean bOneConditionDone){
        boolean bValue=true;
        try{
            java.util.Iterator it=eAnd.getChildren().iterator();
            while (it.hasNext() && bValue){ // Quan es trobi el primer node que avalui a fals ja no s'avalua la resta
                org.jdom.Element eBooleanOp=(org.jdom.Element)it.next();
                bValue=parseBooleanOp(eBooleanOp,symtab,sItemName,bOneConditionDone);
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
        return bValue;
    }
    
    private boolean parseOr(org.jdom.Element eOr, QTISymtab symtab, String sItemName, boolean bOneConditionDone){
        boolean bValue=false;
        try{
            java.util.Iterator it=eOr.getChildren().iterator();
            while (it.hasNext() && !bValue){ // Quan es trobi el primer node que avalui a cert ja no s'avalua la resta
                org.jdom.Element eBooleanOp=(org.jdom.Element)it.next();
                bValue=parseBooleanOp(eBooleanOp,symtab,sItemName,bOneConditionDone);
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
        return bValue;
    }
    
    private void parseSetvar(org.jdom.Element eSetvar, QTISymtab symtab){
        try{
            String sVarname=eSetvar.getAttributeValue("varname"); //optional. Default:SCORE
            if (sVarname==null) sVarname=QTISymtab.DEFAULT_VARNAME;
            String sAction=eSetvar.getAttributeValue("action"); //optional. Default:Set
            if (sAction==null) sAction=DEFAULT_ACTION;
            String sOperand=eSetvar.getText().trim();
            
            if (sAction.equals(SET_ACTION)) symtab.setVar(sVarname,sOperand);
            else{ /* Tota la resta d'accions són operacions aritmétiques: no s'admet String, bool */
                Object currentValue=symtab.getValue(sVarname);
                String sCurrentValue=currentValue.toString();
                if (currentValue!=null){
                    Object newValue=null;
                    String sType=symtab.getType(sVarname);
                    //System.out.println("SETVAR name="+sVarname+"  action="+sAction+"  operand="+sOperand+"  type="+sType);
                    if (sType.equals(QTISymtab.INTEGER_TYPE)){
                        int op1=Integer.parseInt(sCurrentValue);
                        int op2=Integer.parseInt(sOperand);
                        if (sAction.equals(ADD_ACTION))
                            newValue=new Integer(op1+op2);
                        else if (sAction.equals(SUBTRACT_ACTION))
                            newValue=new Integer(op1-op2);
                        else if (sAction.equals(MULTIPLY_ACTION))
                            newValue=new Integer(op1*op2);
                        else if (sAction.equals(DIVIDE_ACTION))
                            newValue=new Integer(op1/op2);
                        else System.out.println("Excepció: Acció "+sAction+" no suportada.");
                    }
                    else if (sType.equals(QTISymtab.DECIMAL_TYPE)){
                        float op1=Float.parseFloat(sCurrentValue);
                        float op2=Float.parseFloat(sOperand);
                        if (sAction.equals(ADD_ACTION))
                            newValue=new Float(op1+op2);
                        else if (sAction.equals(SUBTRACT_ACTION))
                            newValue=new Float(op1-op2);
                        else if (sAction.equals(MULTIPLY_ACTION))
                            newValue=new Float(op1*op2);
                        else if (sAction.equals(DIVIDE_ACTION))
                            newValue=new Float(op1/op2);
                        else System.out.println("Excepció: Acció "+sAction+" no suportada.");
                    }
                    else if (sType.equals(QTISymtab.SCIENTIFIC_TYPE)){
                        double op1=Double.parseDouble(sCurrentValue);
                        double op2=Double.parseDouble(sOperand);
                        if (sAction.equals(ADD_ACTION))
                            newValue=new Double(op1+op2);
                        else if (sAction.equals(SUBTRACT_ACTION))
                            newValue=new Double(op1-op2);
                        else if (sAction.equals(MULTIPLY_ACTION))
                            newValue=new Double(op1*op2);
                        else if (sAction.equals(DIVIDE_ACTION))
                            newValue=new Double(op1/op2);
                        else System.out.println("Excepció: Acció "+sAction+" no suportada.");
                    }
                    else System.out.println("Excepció: Operació "+sAction+" no suportada per tipus "+sType);
                    symtab.setVar(sVarname,newValue);
                }
            }
        }
        catch (Exception e){
			System.out.println("EXCEPTION: "+e);
            if (verbose) e.printStackTrace(System.out);
        }
    }
    
    private void parseDisplayfeedback(org.jdom.Element eDisplayfeedback, java.util.HashMap hmFeedback, String sItemName){
        try{
            String sLinkrefid=eDisplayfeedback.getAttributeValue("linkrefid"); //required
            //System.out.println("FEEDBACK:"+sItemName+" link:"+sLinkrefid);////////
            //////////hmFeedback.put(sItemName,sLinkrefid);
			//logger.debug("parseDisplayFeedback-> item="+sItemName+" link="+sLinkrefid);
            hmFeedback.put(sItemName+"*"+sLinkrefid,sLinkrefid);
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
    }
    
    private void parseItemMetadata(org.jdom.Element eItemMetadata, QTISymtab symtab){
        try{
            java.util.Iterator it=eItemMetadata.getChildren().iterator();
            while (it.hasNext()){
                org.jdom.Element eMetadata=(org.jdom.Element)it.next();
                String sName=eMetadata.getName();
                if (sName.equals("qtimetadata"))
                    parseQtiMetadata(eMetadata,symtab);
                else{ //és un qmd (weighting, levelofdifficulty...) Es pot fer servir per calcular la puntuació a l'scoring algorith.
                    String sValue=eMetadata.getText();
                    symtab.addVar(sName,QTISymtab.STRING_TYPE,sValue);
                }
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
    }
    
    private void parseQtiMetadata(org.jdom.Element eQtiMetadata, QTISymtab symtab){
        try{
            java.util.Iterator it=eQtiMetadata.getChildren("qtimetadatafield", eQtiMetadata.getNamespace()).iterator();
            while (it.hasNext()){
                org.jdom.Element eQtimetadatafield=(org.jdom.Element)it.next();
                parseQtiMetadataField(eQtimetadatafield,symtab);
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
    }
    
    private void parseQtiMetadataField(org.jdom.Element eQtimetadatafield, QTISymtab symtab){
        try{
			String sFieldlabel=eQtimetadatafield.getAttributeValue("fieldlabel");
			String sFieldentry=eQtimetadatafield.getAttributeValue("fieldentry");
            symtab.addVar(sFieldlabel,QTISymtab.STRING_TYPE,sFieldentry);
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
    }
    
    private boolean isEqual(String sItemName, String sRespident, String op, String sCaseSensitive){
        return isEqual(sItemName,sRespident,op,sCaseSensitive,0);
    }
    
	private boolean isEqual(String sItemName, String sRespident, String op, String sCaseSensitive,int index){
		return isEqual(sItemName, sRespident, op, sCaseSensitive, index, true);
	}
    private boolean isEqual(String sItemName, String sRespident, String op, String sCaseSensitive,int index, boolean bExact){
        boolean bCaseSensitive;
        if (sCaseSensitive!=null && sCaseSensitive.trim().toLowerCase().equals("yes")) bCaseSensitive=true;
        else bCaseSensitive=false;
        //System.out.println("!!! Name:"+sItemName+" Resp:"+sRespident+" op:"+op+" case:"+bCaseSensitive+" ind:"+index);
        return userRespSymtab.isEqual(sItemName,sRespident,op,bCaseSensitive,index, bExact);
                /* isEqual està fet a la TS perquè l'identificador pot tenir
                diversos valors si es permet resposta múltiple*/
    }
    
    private boolean isEqual(String sItemName, String sRespident, String op){
        return isEqual(sItemName,sRespident,op,0); //index==0 indica que no estem demanant una comparació d'una posició de llista
    }
    
    private boolean isEqual(String sItemName, String sRespident, String op, int index){
        return isEqual(sItemName,sRespident,op,null,index);
    }
    
	private boolean isEqual(String sItemName, String sRespident, String op, int index, boolean bExact){
		return isEqual(sItemName,sRespident,op,null,index, bExact);
	}
    
    private boolean isLeast(String op1, String op2){
        try{
            double d1=Double.parseDouble(op1);
            double d2=Double.parseDouble(op2);
            //System.out.println(d1+"<"+d2+"?"+(d1<d2)+" ("+op1+"<"+op2+")?");
            return (d1<d2);
        }
        catch (Exception e){
            //System.out.println(op1+"<"+op2+"?"+(op1.compareTo(op2)<0));
            return (op1.compareTo(op2)<0);
        }
    }
    
    private boolean isEqualNoMultivalue(String op1, String op2){
                /* isEqualNoMultivalue compara op1 amb op2 i retorna si són iguals.
                En el cas que ambdos operands puguin ser interpretats com a reals es
                comparen d'aquesta forma de manera que 4==4.00 p.ex. */
        try{
            double d1=Double.parseDouble(op1);
            double d2=Double.parseDouble(op2);
            return (d1==d2);
        }
        catch (Exception e){
            return (op1.compareTo(op2)==0);
        }
    }
    
	private boolean isSubset(String sItemName, String sUserRespName,String sOperand, boolean bExact){
		return isSubset(sItemName,sUserRespName,sOperand,-1, bExact);
	}
    
    private boolean isSubset(String sItemName, String sUserRespName,String sOperand){
        return isSubset(sItemName,sUserRespName,sOperand,-1);
    }
    
	private boolean isSubset(String sItemName, String sUserRespName,String sOperand, int index){
		return isSubset(sItemName, sUserRespName, sOperand, index, true);
	}
    private boolean isSubset(String sItemName, String sUserRespName,String sOperand, int index, boolean bExact){
        /* si index==-1 no importa la posició dintre de la llista */
        sOperand=sOperand.trim();
        boolean isSubset=false;
        //java.util.StringTokenizer st=new java.util.StringTokenizer(sOperand,",");
        //for (int i=1;st.hasMoreTokens() && !isSubset;i++){
        //	String sElement=st.nextToken().trim();
        /*	if (index==-1 || i==index) */
        //System.out.println("item="+sItemName+" userResp="+sUserRespName+" operand="+sOperand);
        if (sOperand!=null){
            int i=sOperand.indexOf(',');
            if (i>0){ //DragDrop
                String sSource=sOperand.substring(0,i).trim();
                String sTarget=sOperand.substring(i+1).trim();
                //System.out.println("QTIResponseParser.isSubset-> sItemName="+sItemName);
                isSubset=isEqual(sItemName,sUserRespName+"_"+sSource,sTarget,index, bExact);
                //isSubset=true;
                System.out.println("QTIResponseParser.isSubset [dragdrop]("+sUserRespName+"_"+sSource+","+sTarget+")?"+isSubset);
            }
            else {
            	isSubset=isEqual(sItemName,sUserRespName,sOperand,index, bExact);
            }    
        }
        //	System.out.println("element["+i+"]"+sElement);
        //}
        //System.out.println(sUserRespName+"["+index+"]=="+sOperand+"?"+isSubset+" exact?"+bExact);
        return isSubset;
    }
    
    private boolean isSubstring(String sUserRespValue,String sOperand,String sCaseSensitive){
        boolean bCaseSensitive;
        if (sCaseSensitive!=null && sCaseSensitive.trim().toLowerCase().equals("yes")) bCaseSensitive=true;
        else bCaseSensitive=false;
        if (bCaseSensitive) return (sOperand.indexOf(sUserRespValue)>=0);
        else return (sOperand.toLowerCase().indexOf(sUserRespValue.toLowerCase())>=0);
    }
    
    private String getUserRespValue(String sRespident,String sItemName,String sIndex){
        /* Retorna un String que representa la resposta que l'usuari ha fet a la response amb ident=sRespident */
        Object oValue=null;
        if (sIndex==null) //No ens referim a una posició dintre de les respostes
            if (userRespSymtab.contains(sItemName,sRespident))
                oValue=userRespSymtab.getValue(sItemName,sRespident);
            else{ //S'HA DE FER!!!!!!
            }
        if (oValue!=null) return oValue.toString();
        else{
            ////System.out.println("No es té valor per l'identificador "+sRespident+" del namespace "+sItemName+".");
            return "";
        }
    }
    
    private void createUserRespSymtab(String sUserResponses){
        userRespSymtab=new QTIUserResponsesSymtab();
		//logger.debug("QTIResponseParser.createUserRespSymtab-> responses="+sUserResponses);
        if (sUserResponses==null) return;
        
        java.util.StringTokenizer st=new java.util.StringTokenizer(sUserResponses,RESPONSE_DELIMITATORS);
        try{
            while (st.hasMoreTokens()){
                String sCurrentResponse=st.nextToken();
                if (sCurrentResponse!=null && !sCurrentResponse.trim().equals("")){
                    java.util.StringTokenizer st2=new java.util.StringTokenizer(sCurrentResponse,"=");
                    if (st2.hasMoreTokens()){
                        String sIdNameWithItemIdent=st2.nextToken();
                        String sIdName,sIdNamespace;
                        int i=sIdNameWithItemIdent.indexOf(ITEM_NAMESPACE_DELIM);
                        if (i>0){
                            sIdNamespace=sIdNameWithItemIdent.substring(0,i);
                            sIdName=sIdNameWithItemIdent.substring(i+ITEM_NAMESPACE_DELIM.length());
                        }
                        else{ //No pot passar si no es manipula manualment.
                            sIdNamespace="";
                            sIdName=sIdNameWithItemIdent;
                        }
                        if (st2.hasMoreTokens()){
                            String sIdValue=st2.nextToken();
                            //System.out.println(sIdName+"="+sIdValue);
                            if (sIdName.endsWith("_manual_sco")){ //Albert: És una correcció manual
                            	sIdName = sIdName.substring(0, sIdName.indexOf("_manual_sco"));
                            	userRespSymtab.addVar(sIdName,sIdName+"_MANUALSCORE",sIdValue); //Marquem que l'item té una puntuació manual
                            	//System.out.println("userRespSymtab.addVar("+sIdName+", "+sIdName+"_MANUALSCORE"+","+sIdValue+");");
                            } else if (sIdName.endsWith("_manual_corr")){ //Albert: És una correcció manual
                            	sIdName = sIdName.substring(0, sIdName.indexOf("_manual_corr"));
                            	userRespSymtab.addVar(sIdName,sIdName+"_MANUALCORRECT",sIdValue); //Marquem que l'item té una puntuació manual
                            	//System.out.println("userRespSymtab.addVar("+sIdName+", "+sIdName+"_MANUALSCORE"+","+sIdValue+");");
                            }
                            else if (sIdValue.indexOf(':')>0){ //Resposta de drag_drop...
                                java.util.StringTokenizer stDrag=new java.util.StringTokenizer(sIdValue,",");
                                while (stDrag.hasMoreTokens()){
                                    String sDrag=stDrag.nextToken();
                                    StringTokenizer stSource = new StringTokenizer(sDrag, ":");
                                    if (stSource.hasMoreTokens()){
                                    	String sDragSource=stSource.nextToken();
                                    	String sX = stSource.nextToken();
                                    	String xY = stSource.nextToken();
                                    	String sRotate=stSource.nextToken();
                                    	String sRatio=stSource.nextToken();
                                    	if (stSource.hasMoreTokens()){
                                            String sDragTarget=stSource.nextToken();
                                            userRespSymtab.addVar(sIdNamespace,sIdName+"_"+sDragSource,sDragTarget);                                    		
                                            if (sRotate!=null){
                                                userRespSymtab.addVar(sIdNamespace,sIdName+"_"+sDragSource,"rotate="+sRotate);                                    		
                                            }
                                            if (sRatio!=null){
                                                userRespSymtab.addVar(sIdNamespace,sIdName+"_"+sDragSource,"ratio="+sRatio);                                    		
                                            }
                                    	}
                                    }
                                    
/*                                    int k=sDrag.indexOf(':');
                                    if (k>0){
                                        String sDragSource=sDrag.substring(0,k);
                                        String sDragTarget=sDrag.substring(k+1);
                                        userRespSymtab.addVar(sIdNamespace,sIdName+"_"+sDragSource,sDragTarget);
                                    }*/
                                }
                            }
                            else userRespSymtab.addVar(sIdNamespace,sIdName,sIdValue);
                            //else if (sIdValue.trim().length()==0) userRespSymtab.addVar(sIdNamespace,sIdName,sIdValue);
                        }else{
							int iIndex = sCurrentResponse.indexOf("=");
							String sResposta = iIndex>0?sCurrentResponse.substring(iIndex+1):"";
							userRespSymtab.addVar(sIdNamespace,sIdName, sResposta);
                        }
                        ////else userRespSymtab.addVar(sIdNamespace,sIdName,""); DECLARACIO_MODIF
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
        
        ////System.out.println("Symtab:"+userRespSymtab.toString()+"<--");
    }
    
    private static org.jdom.Document getXMLDocument(java.io.InputStream is) throws Exception{
        org.jdom.input.SAXBuilder sb=new org.jdom.input.SAXBuilder(false);
        sb.setValidation(false);
        sb.setExpandEntities(false);
        org.jdom.Document doc=sb.build(is);
        return doc;
    }
    
    public static void showHashMap(String name, java.util.HashMap hmFeedback){
        // AQUESTA CLASSE ÉS PER FER SEGUIMENT. S'HA D'ESBORRAR.
        //System.out.println(name);
        java.util.Iterator it=hmFeedback.keySet().iterator();
        while (it.hasNext()){
            Object id=it.next();
            //System.out.println(id.toString()+":"+hmFeedback.get(id).toString());
        }
    }
    
    public static String showSymtabVector(String header, java.util.Vector vSymtabs){
        StringBuffer sb=new StringBuffer();
        if (vSymtabs==null) return "";
        sb.append("-------"+header+"-------\n");
        java.util.Enumeration e=vSymtabs.elements();
        while (e.hasMoreElements()){
            QTISymtab symtab=(QTISymtab)e.nextElement();
            sb.append(symtab+"\n");
            sb.append("-----------\n");
        }
        sb.append("---------------------------\n");
        return sb.toString();
    }
    
    public String getFeedBackRepresentation(java.util.HashMap hmFeedback){
        String sRepresentation="";
        java.util.Iterator it=hmFeedback.keySet().iterator();
        while (it.hasNext()){
            Object id=it.next();
            //System.out.println("feed. id="+id);
            String sId=id.toString();
            int i=sId.indexOf("*");
            if (i>0) sId=sId.substring(0,i);
            sRepresentation+=(sId+""+hmFeedback.get(id).toString()+'#');
            // HAURIA D?ANAR UN = ENMIG, S'HA DE CANVIAR TB A L'XSL
        }
        return sRepresentation;
    }
    
    public static void main(String[] args){
        try{
            //QTIResponseParser qr=new QTIResponseParser(new java.io.FileInputStream("D:\\Albert\\Asignaturas\\PFC\\src\\075.xml"));
            QTIResponseParser qr=new QTIResponseParser(new java.io.FileInputStream("C:\\Albert\\PFC\\src\\075.xml"),new java.util.HashMap());
            float fPuntuation=0.0f;
            QTISymtab st=qr.getQuadernPuntuation("MC01=B#MC02=B#FIB01=36#");
            Object score=st.getScore();
            if (score instanceof Integer) fPuntuation=((Integer)score).intValue();
            else if (score instanceof Float) fPuntuation=((Float)score).floatValue();
            System.out.println("Puntuació:"+fPuntuation);
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
        }
    }
}
