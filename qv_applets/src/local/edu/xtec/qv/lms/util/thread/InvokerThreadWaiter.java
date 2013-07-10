package edu.xtec.qv.lms.util.thread;

public class InvokerThreadWaiter {

	private InvokerThread it;
	private InvokerAction ia;
	
	public InvokerThreadWaiter (InvokerThread it, InvokerAction ia){
		this.it = it;
		this.ia = ia;
	}

	public void start(){
		ia.setActionFinished(false);
		//System.out.println("abans invocar it.invokeMyAction");
		it.invokeMyAction(ia);
		//System.out.println("invocat it.invokeMyAction");
		while (!ia.isActionFinished()){
			//System.out.println("Encara no ha acabat");
			try{
				Thread.currentThread().sleep(500);
			} catch (Exception ex){
				ex.printStackTrace(System.err);
			}
		}
	}

}