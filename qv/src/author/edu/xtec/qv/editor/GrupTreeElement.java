package edu.xtec.qv.editor;


/*
 * GrupTreeElement.java
 *
 * Created on 30 de agosto de 2002, 9:57
 */

/**
 *
 * @author  allastar
 */
public class GrupTreeElement extends ManageTree{
        
    private int iIdUsuari;
    private int iIdEntitat;
    private boolean isAdmin=false;
    private boolean isDocent=false;
    public ManageTreeSchool mts=null;
    ////public ManageTreeQuaderns mtq=null;
	//private javax.swing.DefaultMutableTreeNode root;
    
    /** Creates a new instance of GrupTreeElement */
    public GrupTreeElement(ManageFrame mf,int iIdUsuari, int iIdEntitat) { //El iIdEntitat es podrà treure de la BD
        super(mf);
        //this.iIdUsuari=iIdUsuari;
        //this.iIdEntitat=iIdEntitat;
        this.iIdUsuari=iIdUsuari;        
        try{
            /*isAdmin=getDb().isAdminUser(iIdUsuari);
            isDocent=!isAdmin; ///// AIXÒ ESTÀ PER PROVAR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            String sNomUsuari=getDb().getNomUsuari(iIdUsuari);
            this.iIdEntitat=getDb().getIdEntitat(iIdUsuari);
            System.out.println("Entra l'usuari "+iIdUsuari+" de l'entitat "+iIdEntitat+" isAdmin?"+isAdmin);*/
            isAdmin=true;
            isDocent=true;
            String sNomUsuari="QV";
            this.iIdEntitat=1;
            mts=new ManageTreeSchool(this,iIdUsuari,sNomUsuari,AbstractManageTreeObject.NO);
            setRoot(mts);//root
            ////mtq=new ManageTreeQuaderns(this,-1,util.Messages.getLocalizedString("NotebookManage"),AbstractManageTreeObject.NO);
            ////setRoot(mtq);//root
            
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
        }
    }
    
    public boolean isAdmin(){
	return isAdmin;
    }

    public boolean isDocent(){
	return isDocent;
    }

    public int getIdDocent(){
    	return iIdUsuari;
    }
        
    public int getUserId(){
    	return iIdUsuari;
    }
        
    public int getIdEntitat(){
        return iIdEntitat;
    }

/*    public LearningDatabaseAdmin getDb(){
	return mf.getDb();
    }
*/
    public static void showHashMap(java.util.HashMap hm){
        java.util.Iterator it=hm.keySet().iterator();
	while (it.hasNext()){
            Object oKey=it.next();
            Object oValue=hm.get(oKey);			
            System.out.println(oKey.toString()+": "+oValue.toString());
	}
    }
    
    public void deleteNode(javax.swing.tree.DefaultMutableTreeNode node){
        javax.swing.tree.DefaultMutableTreeNode parent=(javax.swing.tree.DefaultMutableTreeNode)node.getParent();
        if (parent!=null){
            javax.swing.tree.DefaultMutableTreeNode next=(javax.swing.tree.DefaultMutableTreeNode)parent.getChildBefore(node);
            if (next==null){
                next=(javax.swing.tree.DefaultMutableTreeNode)parent.getChildAfter(node);
                if (next==null){
                    next=parent;
                    if (next==null) return;
                }
            }
            //System.out.println("NEXT!=NULL");
            node.removeFromParent();
            /*manager.*/updateTreeUI();
            select(next);
            selectNodeInTree(next);
        }
    }

    /*public static void main(String args[]) {
        new GrupTreeElement(1).show(); //entitat 1
    }*/
    
}