package edu.xtec.qv.editor;

import edu.xtec.qv.editor.util.ActionInfo;


public abstract class AbstractManageTreeObject extends ManageTreeObject{
    protected int id;
    protected String nom;
    protected GrupTreeElement manager;
    protected javax.swing.JPanel jpInfo=null;
    protected boolean bExpanded=false;
    public int permis=NO;
    public static final int NO=0;
    public static final int MODIF_ONLY=1;
    public static final int ERASE_ONLY=2;
    public static final int ALL=3;

    public AbstractManageTreeObject(GrupTreeElement manager, int id, String nom){
        this.manager=manager;
        this.id=id;
        this.nom=nom;
    }

    public AbstractManageTreeObject(GrupTreeElement manager, int id, String nom, int permis){
	this(manager,id,nom);
	this.permis=permis;
    }

	/*public static ArrayManageObject[] createArrayManageObjects(java.util.HashMap hm, int permis){
	      ArrayManageObject[] oA=null;
      	if (hm!=null){
            	oA=new ArrayManageObject[hm.size()];
	            java.util.Iterator it=hm.keySet().iterator();
      	      for (int i=0;it.hasNext();i++){
            	    Object oKey=it.next();
	                Object oValue=hm.get(oKey);
      	          ManageTreeObject go=createObject(oKey,oValue,permis);
            	    oA[i]=go;
	            }
      	}
		return oA;		
	}*/

	//public static abstract ArrayManageObject createObject(Object id, Object o,int permis);

    public void setId(int iId){
        this.id=iId;
    }
    
    public void setNom(String sNom){
        if (sNom!=null && !sNom.equals(this.nom)){
            this.nom=sNom;
            // S'actualitza a l'arbre també
            ManageFrame.frame.mt.updateTreeUI();
            ManageFrame.frame.mt.selectNodeInTree(getNode());
        }
    }
    
    public int getId(){
        return id;
    }
            
    public String getNom(){
        return nom;
    }
        
    public boolean isRoot(){
        return id==-1;
    }
    
    public int getPermis(){
	return permis;
    }

    public boolean isModificable(){
	return (permis==MODIF_ONLY || permis==ALL);
    }

    public boolean isEraseable(){
	return (permis==ERASE_ONLY || permis==ALL);
    }

    public Object[] concat(Object[] oA1,Object[] oA2){ //A UTILITY
        Object[] oA;
	if (oA1!=null && oA2!=null){
	    oA=new Object[oA1.length+oA2.length];
            System.arraycopy(oA1,0,oA,0,oA1.length);
	    System.arraycopy(oA2,0,oA,oA1.length,oA2.length);		
      	}
        else if (oA1==null && oA2==null) oA=null;
      	else if (oA1==null) oA=oA2;
	else oA=oA1; //oA2==null
	return oA;
    }
    
    public java.util.Vector getActionInfo(javax.swing.tree.DefaultMutableTreeNode node){
        return null;
    }
    
    
    public void popup(javax.swing.tree.DefaultMutableTreeNode node, java.awt.Component c, int x, int y) {
        javax.swing.JPopupMenu pm=new javax.swing.JPopupMenu();
        java.util.Vector vActionInfo=getActionInfo(node);
      	if (vActionInfo!=null){
            java.util.Enumeration e=vActionInfo.elements();
            while (e.hasMoreElements()){
    		ActionInfo ai=(ActionInfo)e.nextElement();
                javax.swing.JMenuItem mi1=new javax.swing.JMenuItem(ai.getName(),ai.getIcon());
                mi1.setEnabled(ai.isEnabled());
                mi1.addActionListener(ai.getAction());
                pm.add(mi1);
            }
        }
        pm.show(c,x,y);
    }
        
    public void setExpanded(boolean b){
	bExpanded=b;
    }

    public boolean isExpanded(){
	return bExpanded;
    }
    
    public javax.swing.JPanel getInfoPanel(){
        return jpInfo;
    }
    
    public void setInfoPanel(javax.swing.JPanel jp){
        jpInfo=jp;
    }
    
    public boolean hasCreatedInfoPanel(){
        return (jpInfo!=null);
    }
    
    public String toString(){
      	return nom;
    }
    
    public boolean equals(Object obj){
        if (obj instanceof AbstractManageTreeObject){
            AbstractManageTreeObject o=(AbstractManageTreeObject)obj;
            if (o!=null && o.getId()==getId() && o.getNom().equals(getNom())) return true;
            else return false;
        }
        else return false;
    }
}