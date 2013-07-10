package edu.xtec.qv.applet;

/*
 * SmallButton.java
 *
 * Created on 14 de septiembre de 2001, 11:41
 */

//
//Canviar-ho per un JButton normal ?
//


//package edu.xtec.util.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

/**
 *
 * @author  fbusquet
 * @version
 */
public class SmallButton extends JButton implements MouseListener
{
	
	//protected Border m_raised;
	//protected Border m_lowered;
	//protected Border m_inactive;
	
	SmallButtonBorder smb;
	
	public SmallButton(Action act, String tip) {
		super((javax.swing.Icon)act.getValue(javax.swing.Action.SMALL_ICON));
		//m_raised = new BevelBorder(BevelBorder.RAISED);
		//m_lowered = new BevelBorder(BevelBorder.LOWERED);
		//m_inactive = new javax.swing.border.EmptyBorder(2, 2, 2, 2);
		//setBorder(m_inactive);
		setMargin(new java.awt.Insets(1,1,1,1));
		setToolTipText(tip);
		addActionListener(act);
		addMouseListener(this);
		setRequestFocusEnabled(false);
		
		smb=new SmallButtonBorder(this.getBorder());
		setBorder(smb);
		
		//setBorderPainted(false);
	}
	
	public float getAlignmentY() { return 0.5f; }
	
	public void mousePressed(MouseEvent e) {
		//setBorder(m_lowered);
	}
	
	public void mouseReleased(MouseEvent e) {
		//setBorder(m_inactive);
	}
	
	public void mouseClicked(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {
		//setBorder(m_raised);
		//setBorderPainted(true);
		smb.borderOn=true;
		repaint();
	}
	
	public void mouseExited(MouseEvent e) {
		//setBorder(m_inactive);
		//setBorderPainted(false);
		smb.borderOn=false;
		repaint();
	}
	
	class SmallButtonBorder implements Border{
		boolean borderOn;
		Border m_border;
		
		public SmallButtonBorder(Border border){
			m_border=border;
			borderOn=false;
		}
		
		public void paintBorder(java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height) {
			if(borderOn)
				m_border.paintBorder(c, g, x, y, width, height);
		}                
		
		public java.awt.Insets getBorderInsets(java.awt.Component component) {
			return m_border.getBorderInsets(component);
		}
		
		public boolean isBorderOpaque() {
			return m_border.isBorderOpaque();
		}
	}    
}
