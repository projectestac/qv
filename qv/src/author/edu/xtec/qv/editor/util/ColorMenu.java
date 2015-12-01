package edu.xtec.qv.editor.util;

/*
 * ColorMenu.java
 *
 * Created on 14 de septiembre de 2001, 12:03
 */

//package edu.xtec.util.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.MenuSelectionManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import edu.xtec.qv.qti.util.CreatorProperties;
import edu.xtec.util.Messages;

public class ColorMenu extends JMenu{
    
    protected Border m_unselectedBorder;
    protected Border m_selectedBorder;
    protected Border m_activeBorder;
    
    protected Hashtable m_panes;
    protected ColorPane m_selected;
    protected Color extendedSelected=null;
    
    public ColorMenu(String name) {
        super(name);
        m_unselectedBorder = new CompoundBorder(
            new MatteBorder(1, 1, 1, 1, getBackground()),
            new BevelBorder(BevelBorder.LOWERED, Color.white, Color.gray));
        m_selectedBorder = new CompoundBorder(
            new MatteBorder(2, 2, 2, 2, Color.red),
            new MatteBorder(1, 1, 1, 1, getBackground()));
        m_activeBorder = new CompoundBorder(
            new MatteBorder(2, 2, 2, 2, Color.blue),
            new MatteBorder(1, 1, 1, 1, getBackground()));
                
        JPanel extendedPanel = new JPanel();
        extendedPanel.setLayout(new BorderLayout());
        JPanel p = new JPanel();
        p.setBorder(new EmptyBorder(5, 5, 5, 5));
        p.setLayout(new GridLayout(8, 8));
        m_panes = new Hashtable();
        
        int[] values = new int[] { 0, 128, 192, 255 };
        for (int r=0; r<values.length; r++) {
            for (int g=0; g<values.length; g++) {
                for (int b=0; b<values.length; b++) {
                    Color c = new Color(values[r], values[g], values[b]);
                    ColorPane pn = new ColorPane(c);
                    p.add(pn);
                    m_panes.put(c, pn);
                }
            }
        }
        JButton btMore=new JButton(Messages.getLocalizedString("MoreColors"));
        btMore.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		m_selected=null;
        		Color c=JColorChooser.showDialog(ColorMenu.this,Messages.getLocalizedString("ChooseColor"),new Color(0,0,0)); 
        		extendedSelected=c;
        		fireActionPerformed(new ActionEvent(ColorMenu.this,0,"ok"));
        		fireMenuCanceled();
        	}
        });
        extendedPanel.add(p,BorderLayout.CENTER);
        extendedPanel.add(btMore,BorderLayout.SOUTH);
        
        add(extendedPanel);
        if (CreatorProperties.getBackground()!=null || CreatorProperties.getComponentColor()!=null) setBackground(CreatorProperties.getBackground());
    }
    
    public void setColor(Color c) {
        Object obj = m_panes.get(c);
        if (obj == null) return;
        if (m_selected != null) m_selected.setSelected(false);
        m_selected = (ColorPane)obj;
        m_selected.setSelected(true);
    }
    
    public Color getColor() {
        if (m_selected == null){
        	if (extendedSelected!=null) return extendedSelected;
        	else return null;
        }
        else return m_selected.getColor();
    }
    
    public void doSelection() {
        fireActionPerformed(new ActionEvent(this,
        ActionEvent.ACTION_PERFORMED, getActionCommand()));
    }
    
    class ColorPane extends JPanel implements MouseListener {
        protected Color m_c;
        protected boolean m_selected;
        
        public ColorPane(Color c) {
            m_c = c;
            setBackground(c);
            setBorder(m_unselectedBorder);
            //String msg = "R "+c.getRed()+", G "+c.getGreen()+", B "+c.getBlue();
            //setToolTipText(msg);
            addMouseListener(this);
        }
        
        public Color getColor() { return m_c; }
        
        public Dimension getPreferredSize() {
            return new Dimension(15, 15);
        }
        public Dimension getMaximumSize() { return getPreferredSize(); }
        public Dimension getMinimumSize() { return getPreferredSize(); }
        
        public void setSelected(boolean selected) {
            m_selected = selected;
            if (m_selected)
                setBorder(m_selectedBorder);
            else
                setBorder(m_unselectedBorder);
        }
        
        public boolean isSelected() { return m_selected; }
        
        public void mousePressed(MouseEvent e) {}
        
        public void mouseClicked(MouseEvent e) {}
        
        public void mouseReleased(MouseEvent e) {
            setColor(m_c);
            MenuSelectionManager.defaultManager().clearSelectedPath();
            doSelection();
        }
        
        public void mouseEntered(MouseEvent e) {
            setBorder(m_activeBorder);
        }
        
        public void mouseExited(MouseEvent e) {
            setBorder(m_selected ? m_selectedBorder : m_unselectedBorder);
        }
    }    
}