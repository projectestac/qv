package edu.xtec.qv.editor.ui.event;

public abstract class NodeActionListener implements java.awt.event.ActionListener{

	protected javax.swing.tree.DefaultMutableTreeNode node=null;
	
	public NodeActionListener(){
	}

	public NodeActionListener(javax.swing.tree.DefaultMutableTreeNode node){
		this.node=node;
	}
	
	public void setNode(javax.swing.tree.DefaultMutableTreeNode node){
		this.node=node;
	}
}
