/*
 * ActionInfo.java
 *
 * Created on 10 / desembre / 2002, 13:52
 */

package edu.xtec.qv.editor.util;

import edu.xtec.qv.editor.ui.event.NodeActionListener;

/**
 *
 * @author  allastar
 */
public class ActionInfo {
    
    protected java.awt.event.ActionListener action;
    protected String sName;
    protected javax.swing.ImageIcon icon;
    protected boolean enabled=true;
    
    /** Creates a new instance of ActionInfo */
    public ActionInfo(java.awt.event.ActionListener action, String sName, javax.swing.ImageIcon icon) {
        this.action=action;
        this.sName=sName;
        this.icon=icon;
    }
    
    public ActionInfo(java.awt.event.ActionListener action, String sName, javax.swing.ImageIcon icon, javax.swing.tree.DefaultMutableTreeNode node) {
       this(action,sName,icon);
       if (action!=null && action instanceof NodeActionListener) ((NodeActionListener)action).setNode(node);
    }
    

    public ActionInfo(java.awt.event.ActionListener action, String sName, javax.swing.ImageIcon icon, javax.swing.tree.DefaultMutableTreeNode node, boolean enabled) {
        this(action,sName,icon);
        if (action!=null && action instanceof NodeActionListener) ((NodeActionListener)action).setNode(node);
        this.enabled=enabled;
    }

    public java.awt.event.ActionListener getAction(){
        return action;
    }
    
    public String getName(){
        return sName;
    }
    
    public javax.swing.ImageIcon getIcon(){
        return icon;
    }
    
    public boolean isEnabled(){
    	return enabled;
    }
}