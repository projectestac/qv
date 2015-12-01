package edu.xtec.qv.applet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class QVColorToolBar {
	
	private QVDrawer qvDrawer;
	private JToolBar toolBar;
	private Color initialColor;
	private int numColors;
	
	public QVColorToolBar(QVDrawer qvDrawer, JToolBar toolBar, Color initialColor, int numColors){
		this.qvDrawer=qvDrawer;
		this.toolBar=toolBar;
		this.initialColor=initialColor;
		this.numColors=numColors;
		initColors(initialColor);
	}
	
	private void initColors(Color initialColor){
		bgColor = new ButtonGroup();
		final Color[] colors=new Color[]{initialColor, Color.red, Color.blue, Color.green, Color.white, Color.yellow, Color.orange, Color.magenta};
		btColors = new FrequencyColor[Math.min(colors.length,numColors)];
		for (int i=0;i<colors.length && i<numColors;i++){
			btColors[i] = new FrequencyColor(colors[i]);
			bgColor.add(btColors[i]);
			btColors[i].addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					Color c = ((FrequencyColor)evt.getSource()).getColor();
					setColor(c);
				}
			});
			toolBar.add(btColors[i]);
		}
		btColorDummy = new JToggleButton();
		bgColor.add(btColorDummy);
		Action actionColor = new AbstractAction("color", new ImageIcon(qvDrawer.getResourceURL("icons/plus.gif"))) {
			public void actionPerformed(ActionEvent evt) {
				JColorChooser jcc = new JColorChooser(qvDrawer.getCurrentAspect().getColor());
				JDialog jd = JColorChooser.createDialog(qvDrawer, "Selecciona un color", true, jcc, null, null); 
				jd.show();
				btColorDummy.setSelected(true);
				setColor(jcc.getColor());
			}
		};
		btColor = new SmallButton(actionColor, "Més colors");
		toolBar.add(btColor);
		
	}
	
	private void setColor(Color c){
		FrequencyColor fc = getCurrentButtonWithColor(c);
		if (fc!=null){
			fc.setSelected(true);
			fc.incFrequency();
		} else {
			fc = getLessFrequentColor();
			fc.setColor(c);
			fc.setSelected(true);
		} 
		qvDrawer.setColor(c);
	}
	
	private FrequencyColor getCurrentButtonWithColor(Color c){
		FrequencyColor fc = null;
		boolean bFound = false;
		for (int i=0;!bFound && i<btColors.length;i++){
			if (btColors[i].getColor().equals(c)){
				bFound = true;
				fc = btColors[i];
			}
		}
		return fc;
	}
	
	private FrequencyColor getLessFrequentColor(){
		FrequencyColor fc = null;
		int minFrequency = 100000;
		for (int i=0;i<btColors.length;i++){
			FrequencyColor current=btColors[i];
			if (current.getFrequency()<=minFrequency){
				minFrequency=current.getFrequency();
				fc = current;
			}
		}
		return fc;
	}
	
	class FrequencyColor extends SmallColorToggleButton{
		int frequency;
		
		public FrequencyColor(Color c){
			super(c.equals(initialColor), null, null, "Pinta amb aquest color", c);
			this.frequency=(c.equals(initialColor))?1:0;
		}
		
		public void setColor(Color c){
			if (!getColor().equals(c))
				frequency=1;
			super.setColor(c);
		}
		
		public void incFrequency(){
			frequency++;
		}
		
		public int getFrequency(){
			return frequency;
		}
	}
	
	ButtonGroup bgColor;
	FrequencyColor[] btColors;
	JToggleButton btColorDummy;
	SmallButton btColor;
	
}