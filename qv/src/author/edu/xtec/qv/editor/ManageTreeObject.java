package edu.xtec.qv.editor;

public abstract class ManageTreeObject{

    protected javax.swing.tree.DefaultMutableTreeNode node;

    public abstract Object[] expand();
    public abstract void popup(javax.swing.tree.DefaultMutableTreeNode node,java.awt.Component c,int x,int y);
    public abstract void select(javax.swing.tree.DefaultMutableTreeNode node); //Es crida quan es seleccioni aquest objecte
    public abstract boolean isExpanded();
    public abstract void setExpanded(boolean b);

    public javax.swing.tree.DefaultMutableTreeNode createNode(){
        node=new javax.swing.tree.DefaultMutableTreeNode(this);
        return node;
    }

    public javax.swing.tree.DefaultMutableTreeNode getNode(){
        return node;
    }
    
    public abstract void setNom(String s);
    
    private void move(int inc){
        javax.swing.tree.TreeNode tn=node.getParent();
        if (tn!=null && tn instanceof javax.swing.tree.DefaultMutableTreeNode){
            javax.swing.tree.DefaultMutableTreeNode dmtn=(javax.swing.tree.DefaultMutableTreeNode)tn;
            int i=dmtn.getIndex(node);
            dmtn.insert(node,i+inc);
        }
    }
    
    protected void moveUp(){
        if (canMove(-1)) move(-1);
    }
    
    protected void moveDown(){
        if (canMove(1)) move(1);
    }
    
    private boolean canMove(int inc){
        javax.swing.tree.TreeNode tn=node.getParent();
        if (tn!=null && tn instanceof javax.swing.tree.DefaultMutableTreeNode){
            javax.swing.tree.DefaultMutableTreeNode dmtn=(javax.swing.tree.DefaultMutableTreeNode)tn;
            int i=dmtn.getIndex(node);
            int size=dmtn.getChildCount();
            if ((i+inc)>=0 && (i+inc)<size) return true;
        }
        return false;
    }
    
    protected boolean canMoveUp(){
        return canMove(-1);
    }

    protected boolean canMoveDown(){
        return canMove(1);
    }

}

