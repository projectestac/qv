package edu.xtec.qv.editor;

import edu.xtec.qv.editor.ui.event.NodeActionListener;
import edu.xtec.qv.editor.util.ActionInfo;
import edu.xtec.qv.editor.util.Icons;
import edu.xtec.qv.qti.QTIItem;
import edu.xtec.qv.qti.QTIPresentationElement;
import edu.xtec.qv.qti.QTIXMLElement;
import edu.xtec.qv.qti.util.QTITester;
import edu.xtec.qv.util.Locator;
import edu.xtec.util.Messages;

public class ManageTreePresentationElement extends ManageTreeGrup{
    
    protected QTIPresentationElement qtiPresElem;
    
    public ManageTreePresentationElement(GrupTreeElement manager, int id, QTIItem qtiItem, String titol, int permis){
        super(manager,id,titol,permis);
        setPresentationElement(new QTIPresentationElement(qtiItem));
    }
    
    public ManageTreePresentationElement(GrupTreeElement manager, QTIPresentationElement qtiPresElem, int id, String titol, int permis){
        super(manager,id,titol,permis);
        setPresentationElement(qtiPresElem);
        //System.out.println("Creat ManageTreePresentationElement amb id:"+id+" qtiPresElem==null?"+(qtiPresElem==null));
    }
    
    public void setPresentationElement(QTIPresentationElement qtiPresElem){
        this.qtiPresElem=qtiPresElem;
        qtiPresElem.setManager(this);
    }
    
    public QTIPresentationElement getPresentationElement(){
        return qtiPresElem;
    }
    
    public Object[] expand() {
        Object[] oA=null;
        return oA;
    }
    
    /*public void popup(javax.swing.tree.DefaultMutableTreeNode node, java.awt.Component c, int x, int y) {
        javax.swing.JPopupMenu pm=new javax.swing.JPopupMenu();
        final javax.swing.tree.DefaultMutableTreeNode nod=node;
     
        javax.swing.JMenuItem mi1=new javax.swing.JMenuItem(util.Messages.getLocalizedString("Delete"));
        mi1.addActionListener(alDeleteItem);
        pm.add(mi1);
     
        pm.show(c,x,y);
    }*/
    
    public void select(javax.swing.tree.DefaultMutableTreeNode node){
        manager.setInfoPanel(qtiPresElem.getPanel());
        manager.setActionInfo(getActionInfo(node));
        qtiPresElem.getItem().getSection().getAssessment().getLocator().setToCurrentLocation();
    }
    
    public java.util.Vector getActionInfo(javax.swing.tree.DefaultMutableTreeNode node){
        java.util.Vector vActionInfo=new java.util.Vector();
        ActionInfo ai=new ActionInfo(alShowXMLElems,Messages.getLocalizedString("ShowXML"),Icons.getIcon("ShowXML"),node);
        vActionInfo.add(ai);
        ai=new ActionInfo(alTestItem,Messages.getLocalizedString("Test"),Icons.getIcon("Test"),node);
        vActionInfo.add(ai);
        ai=new ActionInfo(alMoveUp,"Pujar",Icons.getIcon("Up"),node,canMoveUp());
        vActionInfo.add(ai);
        ai=new ActionInfo(alMoveDown,"Baixar",Icons.getIcon("Down"),node,canMoveDown());
        vActionInfo.add(ai);            
        ai=new ActionInfo(alDeleteQuestion,Messages.getLocalizedString("Delete"),Icons.getIcon("Delete"),node);
        vActionInfo.add(ai);
        return vActionInfo;
    }

    protected NodeActionListener alShowXMLElems=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            java.util.Vector vXML=qtiPresElem.getXML();
            if (vXML.size()>0){
                org.jdom.Element eItem=new org.jdom.Element("item");
                java.util.Enumeration e=vXML.elements();
                while (e.hasMoreElements())
                    eItem.addContent((org.jdom.Element)e.nextElement());
                QTIXMLElement.showElementFrame(eItem);
            }
        }
    };
    

    protected NodeActionListener alTestItem=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            boolean bOk=false;
            if (qtiPresElem.getItem()!=null){
                java.util.Vector vXML=qtiPresElem.getItem().getXML();
                if (vXML.size()>0){
                    org.jdom.Element eItem=(org.jdom.Element)vXML.firstElement();
                    org.jdom.Element eQuestestinterop=new org.jdom.Element("questestinterop");
                    org.jdom.Element eAssessment=new org.jdom.Element("assessment");
                    eAssessment.setAttribute("ident","ident");
                    org.jdom.Element eSection=new org.jdom.Element("section");
                    eSection.setAttribute("ident","ident");
                    eSection.setAttribute("title",qtiPresElem.getItem().getName());
                    eQuestestinterop.addContent(eAssessment);
                    eAssessment.addContent(eSection);
                    eSection.addContent(eItem);
                    String sXML=QTIXMLElement.showElement(eQuestestinterop);
                    //bOk=QTITester.testQTI(qtiPresElem.getItem().getSection().getAssessment().getLocator().getLocation(),sXML);
					bOk=QTITester.testQTI(Locator.getLocation(),sXML);
                }
            }
        }
    };

    protected NodeActionListener alMoveUp=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            moveUp();
            manager.updateTreeUI();
            manager.selectNodeInTree(getNode());
            qtiPresElem.moveUp();
        }
    };

    protected NodeActionListener alMoveDown=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            moveDown();
            manager.updateTreeUI();
            manager.selectNodeInTree(getNode());
            qtiPresElem.moveDown();
        }
    };
    
    protected NodeActionListener alDeleteQuestion=new NodeActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent ev){
            int iOpcio=javax.swing.JOptionPane.showConfirmDialog(manager.getMF(),Messages.getLocalizedString("SureRemoveQuestion"),Messages.getLocalizedString("Attention"),javax.swing.JOptionPane.INFORMATION_MESSAGE);
            if (iOpcio==javax.swing.JOptionPane.YES_OPTION){
                qtiPresElem.deleteFromItem();
                manager.deleteNode(node);
            }
        }
    };
    
    public void setDivision(javax.swing.tree.DefaultMutableTreeNode node, java.util.HashMap hmNousAlumnes, String sChildName, java.util.HashMap hmChildElems) {}
    public void modifyDivision(javax.swing.tree.DefaultMutableTreeNode parentNode, int iIdChild, java.util.HashMap hmNousElems, java.util.HashMap hmChildElems) {}
    public void addNewElems(javax.swing.tree.DefaultMutableTreeNode node, java.util.HashMap hmNewChildElems) {}
    
}