/*
 * PointListener.java
 *
 * Created on 26 / febrer / 2002, 09:57
 */

/**
 *
 * @author  allastar
 * @version 
 */

package edu.xtec.jclic.shapers.utils;
 
public interface PointListener {
    public void pointMoved(java.awt.geom.Point2D p);
    public void shapeChanged();
}
