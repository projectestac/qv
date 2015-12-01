package edu.xtec.qv.editor;

/*
 * ManageTree.java
 *
 * Created on 30 de agosto de 2002, 9:17
 */

/**
 *
 * @author  allastar
 */

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeExpansionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import manage.ui.EstatAssignacioQuadernPanel;

public /*abstract*/ class ManageTree extends javax.swing.JPanel{
    
    private JTree tree;
    private boolean bProcessMouseEvents=true;
    protected boolean bOptimizeExpand=true;
    protected ManageFrame mf;
    
    /** Creates new ManageTree */
    public ManageTree(ManageFrame mf) {
        this.mf=mf;
        tree = new JTree();
        tree.putClientProperty("JTree.lineStyle","Angled");
        //tree.setEditable(true);
        tree.setEditable(false);
        //tree.setRootVisible(false);//
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new ManageQVTreeCellRenderer());
        
        final ManageFrame mf2=mf;
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                //if (node!=null) System.out.println("Ha seleccionat node que te "+node.getChildCount()+" fills.");
                if (node!=null && node.getUserObject()!=null && node.getUserObject() instanceof ManageTreeObject)
                    ((ManageTreeObject)node.getUserObject()).select(node);
                /*if (node == null) return;
                expandSubNodes(node);
                //if (node.isLeaf()) {*/
                if (node!=null && node.getUserObject()!=null){
                    Object selectedObject=node.getUserObject();
                    if (selectedObject instanceof ManageTreeQuaderns){
                        mf2.getMenuManager().setSaveEnabled(((ManageTreeQuaderns)selectedObject).canSave());
                        mf2.getMenuManager().setSaveAsEnabled(((ManageTreeQuaderns)selectedObject).canSaveAs());
                    }
                    else{
                        mf2.getMenuManager().setSaveEnabled(false);
                        mf2.getMenuManager().setSaveAsEnabled(false);
                    }
                }
            }
        });
        tree.addTreeExpansionListener(new TreeExpansionListener() {
            public void treeExpanded(TreeExpansionEvent e) {
                //System.out.println("TreeExpanded!!!!!!!!!!!!!!!");
                tree.setSelectionPath(e.getPath());
                //System.out.println("expansió");
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                if (node == null) return;
                expandSubNodes(node);
            }
            public void treeCollapsed(TreeExpansionEvent e) {
            }
        });
        
        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (!bProcessMouseEvents) return;
                if (e.getModifiers()==MouseEvent.BUTTON3_MASK){
                    int selRow = tree.getRowForLocation(e.getX(), e.getY());
                    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                    if (selPath!=null){
                        tree.setSelectionPath(selPath);
                        Object o=selPath.getLastPathComponent();
                        if (o instanceof javax.swing.tree.DefaultMutableTreeNode){
                            Object userObject=((javax.swing.tree.DefaultMutableTreeNode)o).getUserObject();
                            if (userObject instanceof ManageTreeObject)
                                ((ManageTreeObject)userObject).popup((javax.swing.tree.DefaultMutableTreeNode)o,tree,e.getX(),e.getY());
                        }
                    }
                }
            }
        };
        tree.addMouseListener(ml);
        
        //tree.setShowsRootHandles(true);
        /*java.awt.Container c=getContentPane();
        c.add(tree);*/
        setSize(300,375);
        setLayout(new java.awt.BorderLayout());
        add(tree,java.awt.BorderLayout.CENTER);
    }
    
    public void setRoot(Object oRoot){
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(oRoot);
        //if (oRoot instanceof ManageTreeGrupAlumnes) ManageTreeGrupAlumnes.setRootNode(rootNode);
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode){
            protected  void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices, Object[] children)  {
                super.fireTreeNodesInserted(source,path,childIndices,children);
                //System.out.println("Siiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
            }
        };
        treeModel.addTreeModelListener(new javax.swing.event.TreeModelListener(){
            
            public void treeNodesChanged(javax.swing.event.TreeModelEvent e) {}
            public void treeNodesInserted(javax.swing.event.TreeModelEvent e) {
                //System.out.println("nodes insertats!!!!!!!!!!!!!!!!!!!!!!!!");
                lastInserted=e.getTreePath();
            }
            public void treeNodesRemoved(javax.swing.event.TreeModelEvent e) {}
            public void treeStructureChanged(javax.swing.event.TreeModelEvent e) {}
            
            
        });
        
        tree.setModel(treeModel);
        expandNode(rootNode);
        tree.expandRow(0);
    }
    
    public void addChildNodeToParent(javax.swing.tree.DefaultMutableTreeNode nChild){
        Object oRoot=tree.getModel().getRoot();
        //System.out.println(oRoot);
        if (oRoot instanceof javax.swing.tree.DefaultMutableTreeNode){
            javax.swing.tree.DefaultMutableTreeNode nRoot=(javax.swing.tree.DefaultMutableTreeNode)oRoot;
            nRoot.add(nChild);
            expandNode(nChild);
            expandSubNodes(nChild);
            updateTreeUI();
        }
    }
    
    public javax.swing.tree.DefaultMutableTreeNode getRootNode(){
        return (javax.swing.tree.DefaultMutableTreeNode)tree.getModel().getRoot();
    }
    
    public void setRootNode(javax.swing.tree.DefaultMutableTreeNode rootNode){
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        treeModel.addTreeModelListener(new javax.swing.event.TreeModelListener(){
            
            public void treeNodesChanged(javax.swing.event.TreeModelEvent e) {}
            public void treeNodesInserted(javax.swing.event.TreeModelEvent e) {
                //System.out.println("nodes insertats!!!!!!!!!!!!!!!!!!!!!!!!");
                lastInserted=e.getTreePath();
            }
            public void treeNodesRemoved(javax.swing.event.TreeModelEvent e) {}
            public void treeStructureChanged(javax.swing.event.TreeModelEvent e) {}
            
            
        });
        
        tree.setModel(treeModel);
        expandNode(rootNode);
        //tree.expandPath(tree.getEditingPath());
        //tree.expandRow(1);
    }
    
    public void expandSubNodes(DefaultMutableTreeNode node){
        //System.out.println("expandSubNodes("+node.getUserObject().toString()+")");
        java.util.Enumeration e=node.children();
        while (e.hasMoreElements()){
            DefaultMutableTreeNode subNode=(DefaultMutableTreeNode)e.nextElement();
            expandNode(subNode);
        }
    }
    
    public void expandNode(DefaultMutableTreeNode node){
        //System.out.println("ExpandeixoNode");
        Object userObject = node.getUserObject();
        if (userObject instanceof ManageTreeObject && (!bOptimizeExpand || !((ManageTreeObject)userObject).isExpanded())){
            if (bOptimizeExpand) ((ManageTreeObject)userObject).setExpanded(true);
            Object[] oA=((ManageTreeObject)userObject).expand();
            if (oA!=null){
                for (int i=0;i<oA.length;i++){
                    DefaultMutableTreeNode tn;
                    if (oA[i] instanceof ManageTreeObject) tn=((ManageTreeObject)oA[i]).createNode();
                    else tn=new DefaultMutableTreeNode(oA[i]);
                    node.add(tn);
                    if (node.getUserObject() instanceof ManageTreeSchool){
                        //if (oA[i] instanceof ManageTreeGrupAlumnes)
                            //ManageTreeGrupAlumnes.setRootNode(tn);
                    }
                }
            }
        }
    }
    
    public void expandSelectedPath(){
        TreePath tp=tree.getSelectionPath();
        if (tp!=null) tree.expandPath(tp);
    }
    
    public void setInfoPanel(javax.swing.JPanel jpInfo){
        if (jpInfo!=null) mf.setInfoPanel(jpInfo);
        //////////////////////////!!!!!!!!!!!!!!!! S'haurà de mostrar
        //if (jpInfo!=null && jpInfo instanceof EstatAssignacioQuadernPanel){
            //System.out.println("setInfoPanel:");
            //System.out.println(jpInfo);
        //}
        //else System.out.println("No és EstatAssignacioQuadernPanel");
    }
    
    public void setActionInfo(java.util.Vector vActionInfo){
        if (vActionInfo!=null) mf.setActionInfo(vActionInfo);
    }
    
    public void updateTreeUI(){
        tree.updateUI();
    }
    
    public Object getSelectedObject(){
        DefaultMutableTreeNode selectedNode=getSelectedNode();
        if (selectedNode!=null) return selectedNode.getUserObject();
        else return null;
    }
    
    public javax.swing.tree.DefaultMutableTreeNode getSelectedNode(){
        DefaultMutableTreeNode selectedNode=null;
        if (tree.getLastSelectedPathComponent()!=null)
            selectedNode=(DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        return selectedNode;
    }
    
    public void select(javax.swing.tree.DefaultMutableTreeNode node){
        if (node!=null && node.getUserObject()!=null && node.getUserObject() instanceof ManageTreeObject)
            ((ManageTreeObject)node.getUserObject()).select(node);
    }
    
    public void selectNodeInTree(javax.swing.tree.DefaultMutableTreeNode node){
        javax.swing.tree.TreePath tp=new javax.swing.tree.TreePath(node.getPath());
        tree.setSelectionPath(tp);
    }
    
    public void processMouseEvents(boolean b){
        /* En el cas que es vulgui que no es notifiqui als objectes que s'interacciona amb el mouse amb ells b=false */
        bProcessMouseEvents=b;
    }
    
    public void setRootVisible(boolean b){
        tree.setRootVisible(b);
    }
    
    public void setOptimizeExpand(boolean b){
        bOptimizeExpand=b;
    }
    
    public void expandRoot(){
        tree.expandRow(0);
    }
    
    public boolean containsObject(Object o){
        /* Retorna true si el node root conté a un fill que tingui 'o' com a userObject (equals) */
        boolean bTrobat=false;
        javax.swing.tree.DefaultMutableTreeNode node=(javax.swing.tree.DefaultMutableTreeNode)tree.getModel().getRoot();
        java.util.Enumeration e=node.children();
        while (e.hasMoreElements()){
            javax.swing.tree.DefaultMutableTreeNode nChild=(javax.swing.tree.DefaultMutableTreeNode)e.nextElement();
            Object oChild=nChild.getUserObject();
            if (oChild!=null && oChild.equals(o)) bTrobat=true;
        }
        return bTrobat;
    }
    
    public javax.swing.JTree getTree(){
        return tree;
    }
    
    public ManageFrame getMF(){
        return mf;
    }
    
    public void treeDidChange(){
        tree.treeDidChange();
    }
    
    protected javax.swing.tree.TreePath lastInserted=null;
    
}