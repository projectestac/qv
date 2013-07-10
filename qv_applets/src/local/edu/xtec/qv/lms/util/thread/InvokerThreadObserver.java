package edu.xtec.qv.lms.util.thread;

public interface InvokerThreadObserver {
	
	public boolean isActionFinished();
	public void setActionFinished(boolean finished);
	public Object getResult();
	
}