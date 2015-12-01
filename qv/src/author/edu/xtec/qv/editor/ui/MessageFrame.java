package edu.xtec.qv.editor.ui;

import edu.xtec.qv.editor.ManageFrame;
import edu.xtec.util.Messages;

public class MessageFrame extends /*javax.swing.JInternal*/javax.swing.JFrame{

	public MessageFrame(String sMessage){
            //super(manage.ManageFrame.frame.getGraphicsConfiguration());
            super(Messages.getLocalizedString("ShowXML"));
            setIconImage(ManageFrame.frame.getIconImage());
		javax.swing.JScrollPane jsp=new javax.swing.JScrollPane();
		javax.swing.JTextArea jta=new javax.swing.JTextArea(sMessage);
		jsp.setViewportView(jta);
		getContentPane().add(jsp);
		addWindowListener(new java.awt.event.WindowAdapter(){
			public void windowClosing(java.awt.event.WindowEvent e){
				setVisible(false);
				dispose();
			}
		});
                /*addInternalFrameListener(new javax.swing.event.InternalFrameAdapter(){
                    public void internalFrameClosing(javax.swing.event.InternalFrameEvent e){
                        setVisible(false);
			dispose();
                    }
                });*/
		setSize(600,400);
		setVisible(true);
	}

}