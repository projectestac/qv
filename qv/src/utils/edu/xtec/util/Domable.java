/*
 * Domable.java
 *
 * Created on 24 de marzo de 2003, 15:26
 */

package edu.xtec.util;

/**
 *
 * @author  fbusquet
 */
public interface Domable {
    public org.jdom.Element getJDomElement();
    public void setProperties(org.jdom.Element e, Object aux) throws Exception;
}
