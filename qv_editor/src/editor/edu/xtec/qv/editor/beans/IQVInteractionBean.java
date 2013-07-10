/*
 * IQVInteractionBean.java
 * 
 * Created on 11/agost/2004
 */
package edu.xtec.qv.editor.beans;

/**
 * @author sarjona
 */
public interface IQVInteractionBean {

	public final String P_INTERACTIONSWITCH = "interactionswitch";
	
	/**
	 * @return true si s'ha de mostrar el camp d'intervencio; false en cas contrari
	 */
	public boolean isInteractionswitch();
	
	/**
	 * @return clau del fitxer de missatges que identifica el text que s'ha de mostrar al checkbox
	 */
	public String getInteractionText();
	
	/**
	 * @return clau del fitxer de missatges que identifica el tooltip del checkbox
	 */
	public String getInteractionTip();
}
