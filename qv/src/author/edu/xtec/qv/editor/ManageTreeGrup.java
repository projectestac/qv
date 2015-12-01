package edu.xtec.qv.editor;

/*
 * ManageTreeGrup.java
 *
 * Created on 5 de septiembre de 2002, 9:38
 */

/**
 *
 * @author  allastar
 */
public abstract class ManageTreeGrup extends AbstractManageTreeObject{
    
    /** Creates a new instance of ManageTreeGrup */
    public ManageTreeGrup(GrupTreeElement manager, int id, String nom, int permis) {
        super(manager,id,nom,permis);
    }

    public abstract void setDivision(javax.swing.tree.DefaultMutableTreeNode node,java.util.HashMap hmNousAlumnes, String sChildName, java.util.HashMap hmChildElems);
    public abstract void modifyDivision(javax.swing.tree.DefaultMutableTreeNode parentNode,int iIdChild,java.util.HashMap hmNousElems, java.util.HashMap hmChildElems);
    public abstract void addNewElems(javax.swing.tree.DefaultMutableTreeNode node,java.util.HashMap hmNewChildElems);

}
