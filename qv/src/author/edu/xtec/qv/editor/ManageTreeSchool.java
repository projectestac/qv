package edu.xtec.qv.editor;

import edu.xtec.qv.editor.ui.QVAdminManagePresentationPanel;
import edu.xtec.util.Messages;

/*
 * ManageTreeSchool.java
 *
 * Created on 3 de septiembre de 2002, 20:31
 */


/**
 *
 * @author  Albert
 * @version 
 */
public class ManageTreeSchool extends AbstractManageTreeObject{

    public static javax.swing.tree.DefaultMutableTreeNode rootNode=null;
    private int iUserId=-1;
    public ManageTreeQuaderns mtq=null;

    protected QVAdminManagePresentationPanel descriptionPanel;
    
    /** Creates new ManageTreeSchool */
    public ManageTreeSchool(GrupTreeElement manager, int iUserId, String nom,int permis){
        super(manager,-1,nom,permis);
        this.iUserId=iUserId;
    }

    public static void setRootNode(javax.swing.tree.DefaultMutableTreeNode node){
	rootNode=node;
    }

    public Object[] expand(){
        Object[] oA=null;
        //System.out.println(this);
        
        java.util.Vector vManageTreeObjects=new java.util.Vector();
        //vManageTreeObjects.add(new ManageTreeGrupAlumnes(manager,-1,util.Messages.getLocalizedString("StudentManage"),AbstractManageTreeObject.NO));
        //if (manager.isAdmin()) vManageTreeObjects.add(new ManageTreeGrupDocents(manager,-1,util.Messages.getLocalizedString("TeacherManage"),AbstractManageTreeObject.NO));
        //if (manager.isDocent()) vManageTreeObjects.add(new ManageTreeAssignacionsQuaderns(manager,-1,util.Messages.getLocalizedString("NotebookAssignManage"),null,AbstractManageTreeObject.NO));
        mtq=new ManageTreeQuaderns(manager,-1,Messages.getLocalizedString("NotebookManage"),AbstractManageTreeObject.NO);
        //manager.setRoot(mtq);////
        vManageTreeObjects.add(mtq);
        oA=getObjectArrayFromVector(vManageTreeObjects);
        return oA;
    }

    public void popup(javax.swing.tree.DefaultMutableTreeNode node,java.awt.Component c,int x,int y){
        javax.swing.JPopupMenu pm=new javax.swing.JPopupMenu();            
    }

     public void select(javax.swing.tree.DefaultMutableTreeNode node){
        /* S'ha seleccionat aquest objecte. Caldrà construir tota la informació associada.*/
        if (descriptionPanel==null) descriptionPanel=new QVAdminManagePresentationPanel();
        manager.setInfoPanel(descriptionPanel);     
    }

    public Object[] getObjectArrayFromVector (java.util.Vector v){
        Object [] oA=null;
        if (v!=null){
            oA=new Object[v.size()];
            java.util.Enumeration e=v.elements();
            for (int i=0;e.hasMoreElements();i++)
                oA[i]=e.nextElement();
        }
        return oA;
    }
}