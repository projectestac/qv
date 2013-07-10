/*
 * qvRenderSliderApplet.java
 *
 * Created on 12 de agosto de 2002, 11:39
 */

/**
 *
 * @author  allastar
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class qvRenderSliderApplet extends qvApplet{

    private int iLowerBound;
    private int iUpperBound;
    private int iStep;
    private boolean bStepLabel;
    private int iStartVal;
    private String sOrientation;

    private JTextField tfValue;
    private JSlider slValue;    
    
    /** Creates a new instance of qvRenderSliderApplet */
    public qvRenderSliderApplet() {
        //initComponents();
        setVisible(true);
    }
    
    public void initComponents(){
	//System.out.println("initComponents");
        Container c=getContentPane();
        ///c.setLayout(new GridLayout(2,1));
	  c.setLayout(new BorderLayout());
        JPanel jp1=new JPanel();
        jp1.setLayout(new FlowLayout(FlowLayout.CENTER)); //per defecte ja es Flow, però no surt enmig!
        jp1.setBackground(Color.white);
        tfValue=new JTextField();
	  tfValue.setBackground(new Color(0,255,0));
	  tfValue.setForeground(Color.black);
        tfValue.setEditable(false);
        tfValue.setHorizontalAlignment(JTextField.CENTER);
        tfValue.setPreferredSize(new Dimension(50,20));
        tfValue.setMinimumSize(new Dimension(50,20));
        //tfValue.setMaximumSize(new Dimension(50,20));
        jp1.add(tfValue);
        ///c.add(jp1);
	  c.add(jp1,BorderLayout.NORTH);
	  JPanel jp2=new JPanel();
	  jp2.setBackground(Color.white);
        slValue=new JSlider();
	  slValue.setBackground(Color.white);
	  //slValue.setForeground(Color.blue);
        slValue.setMinimum(iLowerBound);
        slValue.setMaximum(iUpperBound);
        slValue.setMinorTickSpacing(iStep);
        slValue.setMajorTickSpacing(iStep);
        slValue.setPaintLabels(bStepLabel);
        slValue.setPaintTicks(bStepLabel);
        slValue.setValue(iStartVal);
        tfValue.setText(""+slValue.getValue());
        if (sOrientation.toLowerCase().equals("horizontal")){
        	slValue.setOrientation(javax.swing.SwingConstants.HORIZONTAL);
        	slValue.setPreferredSize(new Dimension(165,50));
        }
        else{
        	slValue.setOrientation(javax.swing.SwingConstants.VERTICAL);
        	slValue.setPreferredSize(new Dimension(50,165));
        }
        /*slValue.setPaintTrack(true);
        slValue.setExtent(2);*/
        slValue.setSnapToTicks(true);
        slValue.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e){
                tfValue.setText(""+slValue.getValue());
            }
        });
	  jp2.add(slValue);
        ///c.add(jp2);
	  c.add(jp2,BorderLayout.CENTER);
        //c.setForeground(Color.white);
    }
    
/*    public void start(){
	  System.out.println("start");
	  getParams();
        initComponents();
    }*/

    public void getParams(){
        iLowerBound=getIntegerParameter("lowerBound",0); //required
        iUpperBound=getIntegerParameter("upperBound",0); //required
        iStep=getIntegerParameter("step",1);
        bStepLabel=getBooleanParameter("stepLabel",true);
        iStartVal=getIntegerParameter("startVal",iLowerBound);
        sOrientation=getStringParameter("orientation","Horizontal");
    }
    
    public int getIntegerParameter(String paramName, int defaultValue){
        int i=defaultValue;
        String param=getParameter(paramName);
        if (param!=null){
            try{
                i=Integer.parseInt(param);
            }
            catch (Exception e){
                System.out.println("Excepció: "+e);
            }
        }
        return i;
    }
    
    public String getStringParameter(String paramName, String defaultValue){
        String param=getParameter(paramName);
        if (param!=null) return param;
        else return defaultValue;
    }
    
    public boolean getBooleanParameter(String paramName, boolean defaultValue){
        String param=getParameter(paramName);
        if (param!=null){
            if (param.trim().toLowerCase().equals("yes")) return true;
            else return false;
        }
        else return defaultValue;
    }
    
    public String getStringRepresentation() {
           return tfValue.getText();
    }
    
    public boolean initFromParam(String param) {
	   //System.out.println("init");
	   getParams();
           initComponents();

	   if (param!=null){
		   try{
			slValue.setValue(Integer.parseInt(param));
		   }
		   catch(Exception e){
			System.out.println("Excepció:"+e);
			return false;
		   }
		   tfValue.setText(param);
	   }
           return true;
    }
    
    public static void main(String[] args){
        new qvRenderSliderApplet();
    }
}