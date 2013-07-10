package edu.xtec.qv.applet;

import java.util.ArrayList;

public class UndoRedoer{
	
	ArrayList history = null;
	ArrayList alShapes = null;
	
	protected int maxHistory = 10;
	protected int currentSize;
	protected int currentElement;
	
	public UndoRedoer(){
		this(null);
	}
	
	public UndoRedoer(ArrayList alShapes){
		reset(alShapes);
	}
	
	public void reset(ArrayList alShapes){
		currentSize = 0;
		currentElement = 0;
		history = new ArrayList();
		setNewAlShapes(alShapes);
	}
	
	public boolean canUndo(){
		return (currentElement>1);
	}
	
	public boolean canRedo(){
		return (currentElement<currentSize);
	}
	
	public void setNewAlShapes(ArrayList newList){
		if (newList!=null){
			if (currentElement!=currentSize){ //Encara podia fer redo's
				for (int i=history.size();i>currentElement;i--){
					history.remove(i-1);
					currentSize--;
				}
			}
			
			if (currentSize>=maxHistory){
				currentSize--;
				currentElement--;
				history.remove(0);
			}
			history.add(newList.clone());
			currentSize++;
			currentElement++;
		}
	}
	
	public ArrayList undo(){
		ArrayList backList = null;
		if (currentElement>1){
			backList = (ArrayList)history.get(currentElement-2);
			currentElement--;
		}
		return (ArrayList)(backList.clone());
	}
	
	public ArrayList redo(){
		ArrayList nextList = null;
		if (currentElement<currentSize){
			nextList = (ArrayList)history.get(currentElement);
			currentElement++;
		}
		return (ArrayList)(nextList.clone());
	}
	
	public void removeCurrentScene(){
		history.remove(currentElement-1);
		currentElement--;
		currentSize--;
	}
	
}