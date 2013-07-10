package edu.xtec.qv.applet;

/*
 * ColorComboRenderer.java
 *
 * Created on 14 de septiembre de 2001, 12:22
 */

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 *
 * @author  fbusquet
 * @version
 */
public class ColorComboRenderer extends JPanel implements ListCellRenderer{
	
	protected Color m_color = Color.black;
	protected Color m_focusColor = (Color) UIManager.get("List.selectionBackground");
	protected Color m_nonFocusColor = Color.white;
	
	public Component getListCellRendererComponent(JList list, Object obj, int row, boolean sel, boolean hasFocus) {
		if (hasFocus || sel)
			setBorder(new CompoundBorder(
					new MatteBorder(2, 10, 2, 10, m_focusColor),
					new LineBorder(Color.black)));
		else
			setBorder(new CompoundBorder(
					new MatteBorder(2, 10, 2, 10, m_nonFocusColor),
					new LineBorder(Color.black)));
		
		if (obj instanceof Color)
			m_color = (Color) obj;
		return this;
	}
	
	public void paintComponent(Graphics g) {
		setBackground(m_color);
		super.paintComponent(g);
	}
	
}
