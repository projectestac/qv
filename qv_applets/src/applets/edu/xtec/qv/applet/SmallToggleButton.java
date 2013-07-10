package edu.xtec.qv.applet;

/*
 * SmallToggleButton.java
 *
 * Created on 14 de septiembre de 2001, 11:55
 */

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;

/**
 *
 * @author  fbusquet
 * @version
 */
public class SmallToggleButton extends JToggleButton
{
	private static final Dimension d = new Dimension(24,24);
	
	public SmallToggleButton(boolean selected, ImageIcon imgUnselected, ImageIcon imgSelected, String tip) {
		super(imgUnselected, selected);
		setHorizontalAlignment(CENTER);
		setMargin(new java.awt.Insets(1,1,1,1));
		setToolTipText(tip);
		setRequestFocusEnabled(false);
		if(imgSelected!=null) setSelectedIcon(imgSelected);
		
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
	}
	
	public float getAlignmentY() { return 0.5f; }
	
}
