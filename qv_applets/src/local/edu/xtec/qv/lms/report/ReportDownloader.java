package edu.xtec.qv.lms.report;

import edu.xtec.qv.lms.util.thread.*;
import edu.xtec.qv.player.CorrectQVApplet;
import edu.xtec.qv.lms.report.qtidata.QTIAssessmentInfo;
import edu.xtec.qv.lms.data.QV;
import javax.swing.*;
import java.io.*;

public class ReportDownloader{

	//public static String downloadCSVResultReport(CorrectQVApplet main, QTIAssessmentInfo ai, QV qv, int groupId){
	public static String downloadCSVResultReport(CorrectQVApplet main, QTIAssessmentInfo ai, QV qv, String groupName){
		//String report = ReportGenerator.getCSVResultReport(main, ai, qv, groupId);
		String report = ReportGenerator.getCSVResultReport(main, ai, qv, groupName);
		return downloadReport(main, report);
	}

	private static String downloadReport(CorrectQVApplet main, String report){
		final CorrectQVApplet main2 = main;
		final String report2 = report;
		InvokerAction downloadReport = new InvokerAction(){
			public Object runAction(){
				String result = "";
				try{
					System.out.println("showFileChooser");
					JFileChooser chooser = new JFileChooser();
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					chooser.setDialogTitle("Tria on vols desar l'informe");
					boolean end = false;
					while (!end){
						int returnVal = chooser.showSaveDialog(main2);
						if(returnVal == JFileChooser.APPROVE_OPTION) {
							java.io.File fSelectedFile = chooser.getSelectedFile();
							boolean canWrite = fSelectedFile.getParentFile().canWrite();
							if (!canWrite){
								JOptionPane.showMessageDialog(main2, "No es té accés per escriure en aquesta carpeta. Tria'n una altre.", "Tria on vols desar l'informe", JOptionPane.ERROR_MESSAGE); 
							} else {
								try{
									FileWriter fw = new FileWriter(fSelectedFile);
									BufferedWriter bw = new BufferedWriter(fw);
									bw.write(report2);
									bw.flush();
									bw.close();
									result = "OK";
									JOptionPane.showMessageDialog(main2, "Informe desat al fitxer: "+fSelectedFile.getAbsolutePath(), "Tria on vols desar l'informe", JOptionPane.INFORMATION_MESSAGE);
									end = true;
								} catch (Exception ex){
									ex.printStackTrace(System.err);
									JOptionPane.showMessageDialog(main2, "No s'ha pogut escriure en aquest fitxer:"+fSelectedFile.getAbsolutePath()+". Tria'n una altre.", "Tria on vols desar l'informe", JOptionPane.ERROR_MESSAGE);
								}
							}
						} else {
							end = true;
							result = "CANCEL";
						}
					}
				} catch (Exception ex){
					ex.printStackTrace(System.err);
					result = ex.toString();
				}
				return result;
			}
		};
		InvokerThreadWaiter itw = new InvokerThreadWaiter(main.getInvokerThread(), downloadReport);
		itw.start();
		return (String)downloadReport.getResult();
	}
}