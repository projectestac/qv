package edu.xtec.qv.lms.util.thread;

import java.util.ArrayList;

public class InvokerThread extends Thread {

	ArrayList alInvokerActions;
	boolean bFinish = false;
	
	public InvokerThread(){
		alInvokerActions = new ArrayList();
	}
	
	public void invokeMyAction(InvokerAction ia){
		//System.out.println("invokeMyAction bFinish?"+bFinish);
		alInvokerActions.add(ia);
	}
	
	public void run(){
		//System.out.println("InvokerThread.start()!!!!!!!!!!!!!!!!!!!!!!");
		while (!bFinish){
			//System.out.println("InvokerThread espero");
			try{
				if (alInvokerActions.size()>0){
					//System.out.println("Hi ha accions per invocar");
					for (int i=0;i<alInvokerActions.size();i++){
						InvokerAction ia = (InvokerAction)alInvokerActions.get(i);
						//System.out.println("InvokerThread: Invoco l'acció");
						ia.run();
						//System.out.println("Invocada");
						alInvokerActions.remove(ia);
						//System.out.println("Tret de la llista");
					}
				}
				Thread.currentThread().sleep(500);//A
			} catch (Exception ex){
				System.out.println("InvokerThread "+ex.toString());
			}
		}
		//System.out.println("*********************************Acaba InvokerThread");
	}
	
	public void finish(){
		bFinish = true;
	}
	
}