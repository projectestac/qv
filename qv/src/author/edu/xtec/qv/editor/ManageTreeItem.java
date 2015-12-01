package edu.xtec.qv.editor;

import edu.xtec.qv.editor.ui.event.NodeActionListener;
import edu.xtec.qv.editor.util.ActionInfo;
import edu.xtec.qv.editor.util.Icons;
import edu.xtec.qv.qti.QTIClipboard;
import edu.xtec.qv.qti.QTIItem;
import edu.xtec.qv.qti.QTIPresentationElement;
import edu.xtec.qv.qti.QTISection;
import edu.xtec.qv.qti.QTIXMLElement;
import edu.xtec.qv.qti.util.QTITester;
import edu.xtec.qv.util.Locator;
import edu.xtec.util.Messages;

public class ManageTreeItem extends ManageTreeGrup{
    
    protected QTIItem qtiItem;
    
    public ManageTreeItem(GrupTreeElement manager, int id, QTISection qtiSection, String titol, int permis){
        super(manager,id,titol,permis);
        setItem(new QTIItem(qtiSection,titol));
        //System.out.println("Creat ManageTreeItem amb id:"+id);
    }
    
    public ManageTreeItem(GrupTreeElement manager, QTIItem qtiItem, int id, String titol, int permis){
        super(manager,id,titol,permis);
        setItem(qtiItem);
        //System.out.println("Creat ManageTreeItem amb id:"+id);
    }
    
    public void setItem(QTIItem qtiItem){
        this.qtiItem=qtiItem;
        qtiItem.setManager(this);
    }
    
    public QTIItem getItem(){
        return qtiItem;
    }
    
    public Object[] expand() {
        Object[] oA=null;
        java.util.Vector vPresentationElements=qtiItem.getPresentationElements();
        if (vPresentationElements!=null){
            //System.out.println("vPresentationElements.size()="+vPresentationElements.size());
            oA=new ManageTreePresentationElement[vPresentationElements.size()];
            java.util.Enumeration e=vPresentationElements.elements();
            for (int i=0;e.hasMoreElements();i++){
                QTIPresentationElement qtiPresElem=(QTIPresentationElement)e.nextElement();
                ManageTreePresentationElement mtpe=new ManageTreePresentationElement(manager, qtiPresElem, -1, Messages.getLocalizedString("Question")/*qtiPresElem.getName()*/, permis);
                oA[i]=mtpe;
            }
        }
        return oA;
    }
    
    /*public void popup(javax.swing.tree.DefaultMutableTreeNode node, java.awt.Component c, int x, int y) {
        javax.swing.JPopupMenu pm=new javax.swing.JPopupMenu();
        final javax.swing.tree.DefaultMutableTreeNode nod=node;
     
                                javax.swing.JMenuItem mi1=new javax.swing.JMenuItem(Messages.getLocalizedString("AddResponse"));
        mi1.addActionListener(alCreateResponseElems);
        pm.add(mi1);
     
        mi1=new javax.swing.JMenuItem(Messages.getLocalizedString("Cut"));
        mi1.addActionListener(alCut);
        pm.add(mi1);
     
        mi1=new javax.swing.JMenuItem(Messages.getLocalizedString("Copy"));
        mi1.addActionListener(alCopy);
        pm.add(mi1);
     
        //mi1=new javax.swing.JMenuItem("Enganxar");
        //mi1.addActionListener(alPaste);
        //mi1.setEnabled(creator.QTIClipboard.canPaste(creator.QTIClipboard.ITEM));
        //pm.add(mi1);
     
                                mi1=new javax.swing.JMenuItem(Messages.getLocalizedString("ShowXML"));
        mi1.addActionListener(alShowXMLElems);
        pm.add(mi1);
     
        mi1=new javax.swing.JMenuItem(Messages.getLocalizedString("Delete"));
        mi1.addActionListener(alDeleteItem);
        pm.add(mi1);
     
        pm.show(c,x,y);
    }*/
    
    public void select(javax.swing.tree.DefaultMutableTreeNode node){
        manager.setInfoPanel(qtiItem.getPanel());
        
        qtiItem.getSection().getAssessment().getLocator().setToCurrentLocation();
        manager.setActionInfo(getActionInfo(node));
    }
    
    public java.util.Vector getActionInfo(javax.swing.tree.DefaultMutableTreeNode node){
        java.util.Vector vActionInfo=new java.util.Vector();
        ActionInfo ai=new ActionInfo(alCreateResponseElems,Messages.getLocalizedString("AddQuestion"),Icons.getIcon("AddQuestion"),node);
        vActionInfo.add(ai);
        ai=new ActionInfo(alCut,Messages.getLocalizedString("Cut"),Icons.getIcon("Cut"),node);
        vActionInfo.add(ai);
        ai=new ActionInfo(alCopy,Messages.getLocalizedString("Copy"),Icons.getIcon("Copy"),node);
        vActionInfo.add(ai);
        ai=new ActionInfo(alShowXMLElems,Messages.getLocalizedString("ShowXML"),Icons.getIcon("ShowXML"),node);
        vActionInfo.add(ai);
        ai=new ActionInfo(alTestItem,Messages.getLocalizedString("Test"),Icons.getIcon("Test"),node);
        vActionInfo.add(ai);
        ai=new ActionInfo(alMoveUp,"Pujar",Icons.getIcon("Up"),node,canMoveUp());
        vActionInfo.add(ai);            
        ai=new ActionInfo(alMoveDown,"Baixar",Icons.getIcon("Down"),node,canMoveDown());
        vActionInfo.add(ai);            
        ai=new ActionInfo(alDeleteItem,Messages.getLocalizedString("Delete"),Icons.getIcon("Delete"),node);
        vActionInfo.add(ai);
        return vActionInfo;
    }
    
    protected NodeActionListener alMoveUp=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            moveUp();
            manager.updateTreeUI();
            manager.selectNodeInTree(getNode());
            qtiItem.moveUp();
        }
    };

    protected NodeActionListener alMoveDown=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            moveDown();
            manager.updateTreeUI();
            manager.selectNodeInTree(getNode());
            qtiItem.moveDown();
        }
    };

    protected NodeActionListener alCreateResponseElems=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            ManageTreePresentationElement mtpe=new ManageTreePresentationElement(manager,1,qtiItem,Messages.getLocalizedString("Question"),permis);
            javax.swing.tree.DefaultMutableTreeNode nPresentElement=mtpe.createNode();//new javax.swing.tree.DefaultMutableTreeNode(mtpe);
            qtiItem.addPresentationElement(mtpe.getPresentationElement());
            node.add(nPresentElement);
            manager.expandNode(nPresentElement);
            manager.expandSelectedPath();
            manager.select(nPresentElement);//
            manager.updateTreeUI();
            manager.selectNodeInTree(nPresentElement);
        }
    };
    
    protected NodeActionListener alShowXMLElems=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            java.util.Vector vXML=qtiItem.getXML();
            if (vXML.size()>0){
                org.jdom.Element eItem=(org.jdom.Element)vXML.firstElement();
                QTIXMLElement.showElementFrame(eItem);
            }
        }
    };
    
    protected NodeActionListener alTestItem=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            boolean bOk=false;
            if (qtiItem!=null){
                java.util.Vector vXML=qtiItem.getXML();
                if (vXML.size()>0){
                    org.jdom.Element eItem=(org.jdom.Element)vXML.firstElement();
                    org.jdom.Element eQuestestinterop=new org.jdom.Element("questestinterop");
                    org.jdom.Element eAssessment=new org.jdom.Element("assessment");
                    eAssessment.setAttribute("ident","ident");
                    org.jdom.Element eSection=new org.jdom.Element("section");
                    eSection.setAttribute("ident","ident");
                    eSection.setAttribute("title",qtiItem.getName());
                    eQuestestinterop.addContent(eAssessment);
                    eAssessment.addContent(eSection);
                    eSection.addContent(eItem);
                    String sXML=QTIXMLElement.showElement(eQuestestinterop);
                    //bOk=QTITester.testQTI(qtiItem.getSection().getAssessment().getLocator().getLocation(),sXML);
					bOk=QTITester.testQTI(Locator.getLocation(),sXML);
                }
            }
        }
    };

    protected NodeActionListener alDeleteItem=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            int iOpcio=javax.swing.JOptionPane.showConfirmDialog(manager.getMF(),Messages.getLocalizedString("SureRemoveItem"),Messages.getLocalizedString("Attention"),javax.swing.JOptionPane.INFORMATION_MESSAGE);
            if (iOpcio==javax.swing.JOptionPane.YES_OPTION){
                qtiItem.deleteFromSection();
                manager.deleteNode(node);
            }
        }
    };
    
    protected NodeActionListener alCopy=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            QTIClipboard.setCopied(QTIClipboard.ITEM,qtiItem.getXML());
        }
    };
    
    protected NodeActionListener alPaste=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            // qtiItem
            // De moment no es pot enganxar res donat que seria una pregunta dins l'item (exercici).
        }
    };
    
    protected NodeActionListener alCut=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            QTIClipboard.setCopied(QTIClipboard.ITEM,qtiItem.getXML());
            qtiItem.deleteFromSection();
            node.removeFromParent();
            manager.updateTreeUI();
        }
    };
    
    
    
    public void setDivision(javax.swing.tree.DefaultMutableTreeNode node, java.util.HashMap hmNousAlumnes, String sChildName, java.util.HashMap hmChildElems) {}
    public void modifyDivision(javax.swing.tree.DefaultMutableTreeNode parentNode, int iIdChild, java.util.HashMap hmNousElems, java.util.HashMap hmChildElems) {}
    public void addNewElems(javax.swing.tree.DefaultMutableTreeNode node, java.util.HashMap hmNewChildElems) {}
    
}