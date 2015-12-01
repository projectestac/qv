package edu.xtec.qv.editor;

import edu.xtec.qv.editor.ui.event.NodeActionListener;
import edu.xtec.qv.editor.util.ActionInfo;
import edu.xtec.qv.editor.util.Icons;
import edu.xtec.qv.qti.QTIAssessment;
import edu.xtec.qv.qti.QTIClipboard;
import edu.xtec.qv.qti.QTIItem;
import edu.xtec.qv.qti.QTISection;
import edu.xtec.qv.qti.QTIXMLElement;
import edu.xtec.qv.qti.util.QTITester;
import edu.xtec.qv.util.Locator;
import edu.xtec.util.Messages;

public class ManageTreeSection extends ManageTreeGrup{
    
    protected QTISection qtiSection;
    
    public ManageTreeSection(GrupTreeElement manager, int id, QTIAssessment qtiAssessment,String titol, int permis){
        super(manager,id,titol,permis);
        setSection(new QTISection(qtiAssessment,titol));
        //System.out.println("Creat ManageTreeSection amb id:"+id);
    }
    
    public ManageTreeSection(GrupTreeElement manager, QTISection qtiSection, int id, String titol, int permis){
        super(manager,id,titol,permis);
        setSection(qtiSection);
        //System.out.println("Creat ManageTreeSection amb id:"+id);
    }
    
    public void setSection(QTISection qtiSection){
        this.qtiSection=qtiSection;
        qtiSection.setManager(this);
    }
    
    public QTISection getSection(){
        return qtiSection;
    }
    
    public Object[] expand() {
        Object[] oA=null;
        if (isRoot()){ //És un full
            java.util.Vector vItems=qtiSection.getItems();
            if (vItems!=null){
                //System.out.println("vItems.size()="+vItems.size());
                oA=new ManageTreeItem[vItems.size()];
                java.util.Enumeration e=vItems.elements();
                for (int i=0;e.hasMoreElements();i++){
                    QTIItem qtiItem=(QTIItem)e.nextElement();
                    ManageTreeItem mti=new ManageTreeItem(manager, qtiItem, -1, qtiItem.getName(), permis);
                    oA[i]=mti;
                }
            }
        }
        return oA;
    }
    
    /*public void popup(javax.swing.tree.DefaultMutableTreeNode node, java.awt.Component c, int x, int y) {
        javax.swing.JPopupMenu pm=new javax.swing.JPopupMenu();
        final javax.swing.tree.DefaultMutableTreeNode nod=node;
     
     
        if (isRoot()){
            javax.swing.JMenuItem mi1=new javax.swing.JMenuItem(util.Messages.getLocalizedString("AddQuestion"));
            mi1.addActionListener(alCreateItemElems);
            pm.add(mi1);
     
            mi1=new javax.swing.JMenuItem(util.Messages.getLocalizedString("Cut"));
            mi1.addActionListener(alCut);
            pm.add(mi1);
     
            mi1=new javax.swing.JMenuItem(util.Messages.getLocalizedString("Copy"));
            mi1.addActionListener(alCopy);
            pm.add(mi1);
     
            mi1=new javax.swing.JMenuItem(util.Messages.getLocalizedString("Paste"));
            mi1.addActionListener(alPaste);
            mi1.setEnabled(creator.QTIClipboard.canPaste(creator.QTIClipboard.SECTION));
            pm.add(mi1);
     
            mi1=new javax.swing.JMenuItem(util.Messages.getLocalizedString("ShowXML"));
            mi1.addActionListener(alShowXMLElems);
            pm.add(mi1);
     
            mi1=new javax.swing.JMenuItem(util.Messages.getLocalizedString("Delete"));
            mi1.addActionListener(alDeleteSection);
            pm.add(mi1);
        }
        //else{
        //    javax.swing.JMenuItem mi1=new javax.swing.JMenuItem("Afegir Resposta");
        //    mi1.addActionListener(alCreateResponseElems);
        //    pm.add(mi1);
        //}
        pm.show(c,x,y);
    }*/
    
    public void select(javax.swing.tree.DefaultMutableTreeNode node){
        //System.out.println("Select  de full!!!!!!!!"+qtiSection.getIdent()+" "+qtiSection.getName());
        if (!isRoot()){
        }
        else manager.setInfoPanel(qtiSection.getPanel());
        qtiSection.getAssessment().getLocator().setToCurrentLocation();
        manager.setActionInfo(getActionInfo(node));
    }
    
    public java.util.Vector getActionInfo(javax.swing.tree.DefaultMutableTreeNode node){
        java.util.Vector vActionInfo=new java.util.Vector();
        if (isRoot()){
            ActionInfo ai=new ActionInfo(alCreateItemElems,Messages.getLocalizedString("AddExercise"),Icons.getIcon("AddQuestion"),node);
            vActionInfo.add(ai);
            ai=new ActionInfo(alCut,Messages.getLocalizedString("Cut"),Icons.getIcon("Cut"),node);
            vActionInfo.add(ai);
            ai=new ActionInfo(alCopy,Messages.getLocalizedString("Copy"),Icons.getIcon("Copy"),node);
            vActionInfo.add(ai);
            ai=new ActionInfo(alPaste,Messages.getLocalizedString("Paste"),Icons.getIcon("Paste"),node,QTIClipboard.canPaste(QTIClipboard.SECTION));
            vActionInfo.add(ai);
            ai=new ActionInfo(alShowXMLElems,Messages.getLocalizedString("ShowXML"),Icons.getIcon("ShowXML"),node);
            vActionInfo.add(ai);
            ai=new ActionInfo(alTestSection,Messages.getLocalizedString("Test"),Icons.getIcon("Test"),node);
            vActionInfo.add(ai);            
            ai=new ActionInfo(alMoveUp,"Pujar",Icons.getIcon("Up"),node,canMoveUp());
            vActionInfo.add(ai);            
            ai=new ActionInfo(alMoveDown,"Baixar",Icons.getIcon("Down"),node,canMoveDown());
            vActionInfo.add(ai);            
            ai=new ActionInfo(alDeleteSection,Messages.getLocalizedString("Delete"),Icons.getIcon("Delete"),node);
            vActionInfo.add(ai);
        }
        return vActionInfo;
    }
    
    protected NodeActionListener alMoveUp=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            moveUp();
            manager.updateTreeUI();
            manager.selectNodeInTree(getNode());
            qtiSection.moveUp();
        }
    };

    protected NodeActionListener alMoveDown=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            moveDown();
            manager.updateTreeUI();
            manager.selectNodeInTree(getNode());
            qtiSection.moveDown();
        }
    };

    protected NodeActionListener alCreateItemElems=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            ManageTreeItem mti=new ManageTreeItem(manager,1,qtiSection,Messages.getLocalizedString("NewExercise"),permis);
            javax.swing.tree.DefaultMutableTreeNode nItem=mti.createNode();//new javax.swing.tree.DefaultMutableTreeNode(mti);
            qtiSection.addItem(mti.getItem());
            node.add(nItem);
            manager.expandNode(nItem);
            manager.expandSelectedPath();
            manager.select(nItem);
            manager.updateTreeUI();
            manager.selectNodeInTree(nItem);
        }
    };
    
    protected NodeActionListener alShowXMLElems=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            java.util.Vector vXML=qtiSection.getXML();
            if (vXML.size()>0){
                org.jdom.Element eSection=(org.jdom.Element)vXML.firstElement();
                QTIXMLElement.showElementFrame(eSection);
            }
        }
    };

    protected NodeActionListener alTestSection=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            boolean bOk=false;
            if (qtiSection!=null){
                java.util.Vector vXML=qtiSection.getXML();
                if (vXML.size()>0){
                    org.jdom.Element eSection=(org.jdom.Element)vXML.firstElement();
                    org.jdom.Element eQuestestinterop=new org.jdom.Element("questestinterop");
                    org.jdom.Element eAssessment=new org.jdom.Element("assessment");
                    eAssessment.setAttribute("ident","ident");
                    eQuestestinterop.addContent(eAssessment);
                    eAssessment.addContent(eSection);
                    String sXML=QTIXMLElement.showElement(eQuestestinterop);
                    //bOk=QTITester.testQTI(qtiSection.getAssessment().getLocator().getLocation(),sXML);
					bOk=QTITester.testQTI(Locator.getLocation(),sXML);
                }
            }
        }
    };

    protected NodeActionListener alDeleteSection=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            int iOpcio=javax.swing.JOptionPane.showConfirmDialog(manager.getMF(),Messages.getLocalizedString("SureRemoveSection"),Messages.getLocalizedString("Attention"),javax.swing.JOptionPane.INFORMATION_MESSAGE);
            if (iOpcio==javax.swing.JOptionPane.YES_OPTION){
                qtiSection.deleteFromAssessment();
                manager.deleteNode(node);
            }
        }
    };
    
    protected NodeActionListener alCopy=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            QTIClipboard.setCopied(QTIClipboard.SECTION,qtiSection.getXML());
        }
    };
    
    protected NodeActionListener alCut=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            QTIClipboard.setCopied(QTIClipboard.SECTION,qtiSection.getXML());
            qtiSection.deleteFromAssessment();
            node.removeFromParent();
            manager.updateTreeUI();
        }
    };
    
    protected NodeActionListener alPaste=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            try{
                java.util.Vector vXML=QTIClipboard.getCopied();
                if (vXML!=null && vXML.size()>0){
                    QTIItem qtiItem=(QTIItem)QTIItem.createFromXML(vXML,qtiSection);
                    org.jdom.Element eItem=QTIXMLElement.getElement(vXML);
                    String sNomItem=eItem.getAttributeValue("title");
                    sNomItem=(sNomItem!=null && sNomItem.trim().length()>0)?sNomItem:QTIXMLElement.getRandomIdent();
                    
                    ManageTreeItem mti=new ManageTreeItem(manager,qtiItem,1,sNomItem,permis);
                    javax.swing.tree.DefaultMutableTreeNode nItem=mti.createNode();//new javax.swing.tree.DefaultMutableTreeNode(mti);
                    qtiSection.addItem(qtiItem);
                    node.add(nItem);
                    manager.expandNode(nItem);
                    manager.expandSelectedPath();
                    manager.updateTreeUI();
                }
            }
            catch (Exception e){
                System.out.println("Excepció en enganxar item:"+e);
                e.printStackTrace(System.out);
            }
        }
    };
    
    
    public void setDivision(javax.swing.tree.DefaultMutableTreeNode node, java.util.HashMap hmNousAlumnes, String sChildName, java.util.HashMap hmChildElems) {}
    public void modifyDivision(javax.swing.tree.DefaultMutableTreeNode parentNode, int iIdChild, java.util.HashMap hmNousElems, java.util.HashMap hmChildElems) {}
    public void addNewElems(javax.swing.tree.DefaultMutableTreeNode node, java.util.HashMap hmNewChildElems) {}
    
}