package edu.xtec.qv.editor;

import edu.xtec.qv.editor.ui.AssessmentManagePresentationPanel;
import edu.xtec.qv.editor.ui.QTIFileChooserDialog;
import edu.xtec.qv.editor.ui.event.NodeActionListener;
import edu.xtec.qv.editor.util.ActionInfo;
import edu.xtec.qv.editor.util.FileHistory;
import edu.xtec.qv.editor.util.Icons;
import edu.xtec.qv.qti.QTIAssessment;
import edu.xtec.qv.qti.QTIClipboard;
import edu.xtec.qv.qti.QTISection;
import edu.xtec.qv.qti.QTIXMLElement;
import edu.xtec.qv.qti.util.Opener;
import edu.xtec.qv.qti.util.QTITester;
import edu.xtec.qv.qti.util.Saver;
import edu.xtec.qv.util.Locator;
import edu.xtec.util.Messages;

public class ManageTreeQuaderns extends ManageTreeGrup implements Opener, Saver{
    
    protected QTIAssessment qtiAssessment;
    protected String sLocalTestPath,sUserAssignments;
    protected static AssessmentManagePresentationPanel descriptionPanel=null;
    
    
    public ManageTreeQuaderns(GrupTreeElement manager, int id, String titol, int permis){
        super(manager,id,titol,permis);
        sLocalTestPath=ManageToolConfig.getProperty("localTestPath"); //"D:";
        sUserAssignments=ManageToolConfig.getProperty("userAssignmentsUrl"); //http://localhost:8080/qv/getAssignacionsUsuari
        //System.out.println("Creat ManageQuaderns amb id:"+id);
    }
    
    public void setAssessment(QTIAssessment qtiAssessment){
        this.qtiAssessment=qtiAssessment;
        qtiAssessment.setManager(this);
    }
    
    public Object[] expand() {
        Object[] oA=null;
        if (!isRoot()){ //És un quadern
            java.util.Vector vSections=qtiAssessment.getSections();
            if (vSections!=null){
                //System.out.println("vSections.size()="+vSections.size());
                oA=new ManageTreeSection[vSections.size()];
                java.util.Enumeration e=vSections.elements();
                for (int i=0;e.hasMoreElements();i++){
                    QTISection qtiSection=(QTISection)e.nextElement();
                    ManageTreeSection mts=new ManageTreeSection(manager, qtiSection, -1, qtiSection.getName(), permis);
                    oA[i]=mts;
                }
            }
        }
        return oA;
    }
    
    public boolean canSave(){
        return true;
        /*if (qtiAssessment!=null){
            int iLocation=qtiAssessment.getLocation();
            if (iLocation!=qtiAssessment.NO_LOCATION) return true;
        }
        return false;*/
    }
    
    public boolean canSaveAs(){
        return qtiAssessment!=null;
    }
    
    protected boolean testQTI(){
        boolean bOk=false;
        if (qtiAssessment!=null){
            java.util.Vector vXML=qtiAssessment.getXML();
            if (vXML.size()>0){
                org.jdom.Element eAssessment=(org.jdom.Element)vXML.firstElement();
                String sXML=QTIXMLElement.showElement(eAssessment);
                //bOk=QTITester.testQTI(qtiAssessment.getLocator().getLocation(),sXML);
				bOk=QTITester.testQTI(Locator.getLocation(),sXML);
            }
        }
        return bOk;
    }
            
    protected void doSaveAs(){
        String sId;
        sId=(qtiAssessment!=null && qtiAssessment.getIdent().trim().length()>0)?qtiAssessment.getIdent().trim():javax.swing.JOptionPane.showInputDialog(Messages.getLocalizedString("IntroNewIdent"));
        if (sId!=null && qtiAssessment!=null){
            qtiAssessment.doSaveAs(sId);
        }
    }
    
    protected void doSave(){
        qtiAssessment.doSave();
    }
    
    public void open(String sPathQuadern){
        open(sPathQuadern,null);
    }
    
    public void open(String sPathQuadern, Locator l){
        try{
            String sNom="";
            org.jdom.Document d=openQTIDocument(sPathQuadern);
            //org.jdom.Element eAssessment=creator.QTIXMLElement.firstChild(d.getRootElement());
            org.jdom.Element eAssessment=d.getRootElement().getChild("assessment");
            QTIAssessment qtiA=(QTIAssessment)QTIAssessment.createFromXML(QTIXMLElement.createVector(eAssessment),this,l);
            
            if (qtiA!=null){
                qtiA.setLocation(sPathQuadern);
                qtiA.setLocation(QTIAssessment.FILE);
                if (qtiA!=null){
                    ManageTreeQuaderns mtq=new ManageTreeQuaderns(manager,1,qtiA.getName(),permis);
                    mtq.setAssessment(qtiA);
                    javax.swing.tree.DefaultMutableTreeNode nQuadern=mtq.createNode(); //new javax.swing.tree.DefaultMutableTreeNode(mtq);
                    nod.add(nQuadern);
                    manager.expandNode(nQuadern);
                    manager.expandSelectedPath();
                    manager.updateTreeUI();
                }
            }
            FileHistory.addRecentFile(sPathQuadern,sPathQuadern);
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            e.printStackTrace(System.out);
            javax.swing.JOptionPane.showMessageDialog(manager.getMF(),"Error: Memòria insuficient.","Error",javax.swing.JOptionPane.ERROR_MESSAGE);
            //e.printStackTrace(System.out);
            try{
                java.io.FileOutputStream fos=new java.io.FileOutputStream("C:/Albert/error.txt");
                java.io.PrintWriter pw=new java.io.PrintWriter(fos);
                e.printStackTrace(pw);
                pw.flush();
                fos.flush();
                fos.close();
            }
            catch (Exception e2){
                System.out.println("Excepció:"+e2);
            }
        }
    }
    
    public void open(java.io.File f){
        //System.out.println("Vaig a crear el locator");
        Locator l=new Locator(f);
        //System.out.println("creat");
        open(f.getAbsolutePath(),l);
    }
    
    protected org.jdom.Document openQTIDocument(String sPath) throws Exception{
        org.jdom.Document d=QTIXMLElement.getXMLDocument(new java.io.FileInputStream(sPath));
        return d;
    }
    
    public void select(javax.swing.tree.DefaultMutableTreeNode node){
        if (node==null) node=getNode();
        nod=node;
        if (!isRoot()){
            manager.setInfoPanel(qtiAssessment.getPanel());
            qtiAssessment.getLocator().setToCurrentLocation();
        }
        else { //isRoot
            if (descriptionPanel==null) descriptionPanel=new AssessmentManagePresentationPanel();
            manager.setInfoPanel(descriptionPanel);
        }
        manager.setActionInfo(getActionInfo(node));
    }
    
    public java.util.Vector getActionInfo(javax.swing.tree.DefaultMutableTreeNode node){
        java.util.Vector vActionInfo=new java.util.Vector();
        if (isRoot()){
            ActionInfo ai=new ActionInfo(alCreateQuadernElems,Messages.getLocalizedString("CreateNotebook"),Icons.getIcon("CreateNotebook"),node);
            vActionInfo.add(ai);
            ai=new ActionInfo(alOpenQuadernElems,Messages.getLocalizedString("OpenNotebook"),Icons.getIcon("OpenNotebook"),node);
            vActionInfo.add(ai);
            //ai=new ActionInfo(alSearchQuadernElems,Messages.getLocalizedString("NotebookSearch"),Icons.getIcon("NotebookSearch"),node);
            //vActionInfo.add(ai);
        }
        else{
            ActionInfo ai=new ActionInfo(alAddSectionElems,Messages.getLocalizedString("AddPaper"),Icons.getIcon("AddPaper"),node);
            vActionInfo.add(ai);
            ai=new ActionInfo(alPaste,Messages.getLocalizedString("Paste"),Icons.getIcon("Paste"),node,QTIClipboard.canPaste(QTIClipboard.ASSESSMENT));
            vActionInfo.add(ai);
            if (qtiAssessment!=null){
                boolean enabled=canSave();
                /*int iLocation=qtiAssessment.getLocation();
                if (iLocation!=qtiAssessment.NO_LOCATION) enabled=true;
                else enabled=false;*/
                ai=new ActionInfo(alSaveQuadernElems,Messages.getLocalizedString("Save"),Icons.getIcon("Save"),node,enabled);
                vActionInfo.add(ai);
            }
            ai=new ActionInfo(alSaveAsQuadernElems,Messages.getLocalizedString("SaveAs"),Icons.getIcon("SaveAs"),node);
            vActionInfo.add(ai);
            ai=new ActionInfo(alShowXMLElems,Messages.getLocalizedString("ShowXML"),Icons.getIcon("ShowXML"),node);
            vActionInfo.add(ai);
            ai=new ActionInfo(alTestQuadernElems,Messages.getLocalizedString("Test"),Icons.getIcon("Test"),node);
            vActionInfo.add(ai);
            ai=new ActionInfo(alCloseQuadern,Messages.getLocalizedString("Close"),Icons.getIcon("Close"),node);
            vActionInfo.add(ai);
        }
        return vActionInfo;
    }
    
    public void setDivision(javax.swing.tree.DefaultMutableTreeNode node, java.util.HashMap hmNousAlumnes, String sChildName, java.util.HashMap hmChildElems) {}
    public void modifyDivision(javax.swing.tree.DefaultMutableTreeNode parentNode, int iIdChild, java.util.HashMap hmNousElems, java.util.HashMap hmChildElems) {}
    public void addNewElems(javax.swing.tree.DefaultMutableTreeNode node, java.util.HashMap hmNewChildElems) {}
    
    public int saveAssessment(QTIAssessment qtiAssessment, int idDBAssessment) {
        int iAssessmentId=idDBAssessment;
        try{
            //LearningDatabaseAdmin qtiDb=manager.getDb();
            org.jdom.Element eAssessment=(org.jdom.Element)qtiAssessment.getXML().firstElement();
            String sAssessment=QTIXMLElement.showElement(eAssessment);
            if (idDBAssessment<0){ //Encara no es troba a la BD
/*                iAssessmentId=qtiDb.createQuadern(qtiAssessment.getIdent(), qtiAssessment.getTitle(), "",
                qtiAssessment.getMateriaId(), qtiAssessment.getMateria(), qtiAssessment.getSubMateriaId(),
                qtiAssessment.getSubMateria(), qtiAssessment.getLevel(), qtiAssessment.getSubLevel(),
                qtiAssessment.getLanguage(), qtiAssessment.getDifficulty(), qtiAssessment.getKeyWords(),
                qtiAssessment.isAccessibleNoVisual(), qtiAssessment.isAccessibleNoAuditive(),
                qtiAssessment.isAccessibleNoMotrive(), sAssessment, Messages.getLanguageCode());
*/                
                //System.out.println("create quadern:"+iAssessmentId+"----------------------------------------------------------");
            }
            else{
                System.out.println("update quadern:"+idDBAssessment+"----------------------------------------------------------");
                boolean bOk=true;
/*                bOk=qtiDb.updateQuadern(idDBAssessment, qtiAssessment.getIdent(), qtiAssessment.getTitle(), "",
                qtiAssessment.getMateriaId(), qtiAssessment.getMateria(), qtiAssessment.getSubMateriaId(),
                qtiAssessment.getSubMateria(), qtiAssessment.getLevel(), qtiAssessment.getSubLevel(),
                qtiAssessment.getLanguage(), qtiAssessment.getDifficulty(), qtiAssessment.getKeyWords(),
                qtiAssessment.isAccessibleNoVisual(), qtiAssessment.isAccessibleNoAuditive(),
                qtiAssessment.isAccessibleNoMotrive(), sAssessment, Messages.getLanguageCode());
*/
                if (bOk) javax.swing.JOptionPane.showMessageDialog(manager.getMF(),"Quadern desat correctament.","Informació",javax.swing.JOptionPane.INFORMATION_MESSAGE);
                else javax.swing.JOptionPane.showMessageDialog(manager.getMF(),"Error: No s'ha pogut desar el quadern a la Base de dades.","Error",javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            javax.swing.JOptionPane.showMessageDialog(manager.getMF(),"Error: No s'ha pogut desar el quadern a la Base de dades.","Error",javax.swing.JOptionPane.ERROR_MESSAGE);
            //javax.swing.JOptionPane.showMessageDialog(manager,"Error: Memòria insuficient.","Error",javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        return iAssessmentId;
    }
    
    public boolean openDB(int iAssessmentId) {
        //AQUESTA OPERACIÓ NO ES FA SERVIR. NO L'ELIMINO PERQUÈ IMPLEMENTA UNA INTERFACE QUE OBLIGA A TENIR-LA. CAL MODIFICAR LA INTERFACE.
        /*boolean bOk=false;
        long t1=System.currentTimeMillis();
        try{
            java.io.InputStream is=search.Searcher.getDb().getQuadernData(iAssessmentId);
            org.jdom.Document doc=creator.QTIXMLElement.getXMLDocument(is);
            org.jdom.Element eAssessment=creator.QTIXMLElement.firstChild(doc.getRootElement());
            creator.QTIAssessment qtiA=(creator.QTIAssessment)creator.QTIAssessment.createFromXML(creator.QTIXMLElement.createVector(eAssessment),this);
            if (qtiA!=null){
                qtiA.setLocation(creator.QTIAssessment.DB);
                qtiA.setDBAssessmentId(iAssessmentId);
                ManageTreeQuaderns mtq=new ManageTreeQuaderns(manager,1,qtiA.getName(),permis);
                mtq.setAssessment(qtiA);
                javax.swing.tree.DefaultMutableTreeNode nQuadern=mtq.createNode();//new javax.swing.tree.DefaultMutableTreeNode(mtq);
                //System.out.println("nod==null?"+(nod==null));
                nod.add(nQuadern);
                manager.expandNode(nQuadern);
                manager.expandSelectedPath();
                manager.updateTreeUI();
            }
            bOk=true;
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            javax.swing.JOptionPane.showMessageDialog(manager.getMF(),"Error: Memòria insuficient.","Error",javax.swing.JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(System.out);
            bOk=false;
        }
        long t2=System.currentTimeMillis();
        System.out.println("Quadern obert en "+((t2-t1)/1000)+" segons. OK?"+bOk);
        return bOk;*/
        return false;
    }
    
    /*public void open(String sPath) {
    }*/
    
    protected NodeActionListener alCreateQuadernElems=new NodeActionListener(null){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            QTIFileChooserDialog qfc=new QTIFileChooserDialog(manager.getMF(),true);
            java.io.File f=qfc.showSaveDialog(manager.getMF());
            if (f!=null){
                String sNom=f.getName();
                ManageTreeQuaderns mtq=new ManageTreeQuaderns(manager,1,sNom,permis);
                QTIAssessment qtiA=new QTIAssessment(sNom,ManageTreeQuaderns.this);
                Locator l=new Locator(f);
                qtiA.setLocator(l);
                qtiA.setLocation(QTIAssessment.FILE);
                qtiA.setLocation(f.getAbsolutePath());
                mtq.setAssessment(qtiA);
                
                //System.out.println("a node==null?"+(node==null));
                javax.swing.tree.DefaultMutableTreeNode nQuadern=mtq.createNode();//new javax.swing.tree.DefaultMutableTreeNode(mtq);
                //System.out.println("b");
                //nod.add(nQuadern);
                node.add(nQuadern);
                //System.out.println("c");
                manager.expandNode(nQuadern);
                manager.expandSelectedPath();
                manager.select(nQuadern);
                manager.updateTreeUI();
            }
/*            String sNom=javax.swing.JOptionPane.showInputDialog(Messages.getLocalizedString("NotebookName"));
            if (sNom!=null){
                ManageTreeQuaderns mtq=new ManageTreeQuaderns(manager,1,sNom,permis);
                creator.QTIAssessment qtiA=new creator.QTIAssessment(sNom,ManageTreeQuaderns.this);
                mtq.setAssessment(qtiA);
                //System.out.println("a node==null?"+(node==null));
                javax.swing.tree.DefaultMutableTreeNode nQuadern=new javax.swing.tree.DefaultMutableTreeNode(mtq);
                //System.out.println("b");
                //nod.add(nQuadern);
                node.add(nQuadern);
                //System.out.println("c");
                manager.expandNode(nQuadern);
                manager.expandSelectedPath();
                manager.select(nQuadern);
                manager.updateTreeUI();
            }*/
        }
    };
    
    protected NodeActionListener alAddSectionElems=new NodeActionListener(null){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            String sNom=javax.swing.JOptionPane.showInputDialog(Messages.getLocalizedString("PaperName"));
            if (sNom!=null){
                ManageTreeSection mts=new ManageTreeSection(manager,-1,qtiAssessment,sNom,permis);
                javax.swing.tree.DefaultMutableTreeNode nSection=mts.createNode(); //new javax.swing.tree.DefaultMutableTreeNode(mts);
                node.add(nSection);
                qtiAssessment.addSection(mts.getSection());
                manager.expandNode(nSection);
                manager.expandSelectedPath();
                manager.select(nSection);//
                manager.updateTreeUI();
                manager.selectNodeInTree(nSection);
            }
        }
    };
    
    protected NodeActionListener alShowXMLElems=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            java.util.Vector vXML=qtiAssessment.getXML();
            if (vXML.size()>0){
                org.jdom.Element eAssessment=(org.jdom.Element)vXML.firstElement();
                QTIXMLElement.showElementFrame(eAssessment);
            }
        }
    };
    
    protected NodeActionListener alTestQuadernElems=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            testQTI();
        }
    };
    
    protected NodeActionListener alSaveQuadernElems=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            if (qtiAssessment!=null) doSave();
        }
    };
    
    protected NodeActionListener alSaveAsQuadernElems=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            if (qtiAssessment!=null) doSaveAs();
        }
    };
    
    protected NodeActionListener alOpenQuadernElems=new NodeActionListener(null){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            ManageTreeQuaderns.this.nod=node;
            QTIFileChooserDialog qfcd=new QTIFileChooserDialog(manager.getMF(),true);
            java.io.File f=qfcd.showOpenDialog(manager.getMF());
            if (f!=null){
                open(f);
            }
            /*ManageTreeQuaderns.this.nod=node;//nod
            OpenAssessmentDialog oad=new OpenAssessmentDialog(ManageTreeQuaderns.this,new javax.swing.JFrame(),true);
            oad.show();*/
        }
    };
    
    protected NodeActionListener alPaste=new NodeActionListener(null){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            try{
                java.util.Vector vXML=QTIClipboard.getCopied();
                if (vXML!=null && vXML.size()>0){
                    QTISection qtiSection=(QTISection)QTISection.createFromXML(vXML,qtiAssessment);
                    org.jdom.Element eSection=QTIXMLElement.getElement(vXML);
                    String sNomSection=eSection.getAttributeValue("title");
                    sNomSection=(sNomSection!=null && sNomSection.trim().length()>0)?sNomSection:QTIXMLElement.getRandomIdent();
                    
                    ManageTreeSection mts=new ManageTreeSection(manager,qtiSection,-1,sNomSection,permis);
                    javax.swing.tree.DefaultMutableTreeNode nSection=mts.createNode();//new javax.swing.tree.DefaultMutableTreeNode(mts);
                    qtiAssessment.addSection(qtiSection);
                    node.add(nSection);
                    manager.expandNode(nSection);
                    manager.expandSelectedPath();
                    manager.updateTreeUI();
                }
            }
            catch (Exception e){
                System.out.println("Excepció en enganxar item:"+e);
                javax.swing.JOptionPane.showMessageDialog(manager.getMF(),"Error: Memòria insuficient.","Error",javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    };
    
    protected NodeActionListener alSearchQuadernElems=new NodeActionListener(null){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            //ManageTreeQuaderns.this.nod=nod;
            //new search.Searcher(ManageTreeQuaderns.this);
        }
    };
    
    protected NodeActionListener alCloseQuadern=new NodeActionListener(null){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            //System.out.println("Mem. lliure abans:"+Runtime.getRuntime().freeMemory());
            int iOpcio=javax.swing.JOptionPane.showConfirmDialog(manager.getMF(),Messages.getLocalizedString("SureCloseNotebook"),Messages.getLocalizedString("Attention"),javax.swing.JOptionPane.INFORMATION_MESSAGE);
            if (iOpcio==javax.swing.JOptionPane.YES_OPTION){
                qtiAssessment=null;
                node.removeFromParent();
                manager.updateTreeUI();
                System.gc();
            }
            //System.out.println("Mem. lliure després:"+Runtime.getRuntime().freeMemory());
        }
    };
    
    private javax.swing.tree.DefaultMutableTreeNode nod=null;
    private javax.swing.JMenuItem miSave;
}