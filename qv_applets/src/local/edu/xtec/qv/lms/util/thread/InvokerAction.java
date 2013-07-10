package edu.xtec.qv.lms.util.thread;

public abstract class InvokerAction implements InvokerThreadObserver{

	private boolean bFinished = false;
	private Object result = null;
	
	public InvokerAction(){
	}

	void run (){ //Package visibility
		setActionFinished(false);
		try {
			result = runAction();
		} catch (Exception ex){
			ex.printStackTrace(System.err);
		} finally {
			setActionFinished(true);
		}
	}
	
	public abstract Object runAction();
	
	public Object getResult(){
		return result;
	}
	
	public boolean isActionFinished(){
		return bFinished;
	}
	
	public void setActionFinished(boolean bFinished){
		this.bFinished = bFinished;
	}

}