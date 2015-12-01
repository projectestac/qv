/*
 * ReportServerEventMaker.java
 *
 * Created on 6 de septiembre de 2002, 13:25
 */

package edu.xtec.qv.editor.server;

import java.util.Vector;
import java.net.Socket;

/**
 *
 * @author Francesc Busquets (fbusquets@pie.xtec.es)
 */
public class QVServerEventMaker {
    
    protected Vector listeners=new Vector(1);
    
    public interface Listener {
        public void reportEventPerformed(QVServerEvent ev);
    }
            
    /** Creates a new instance of ReportServerEventMaker */
    public QVServerEventMaker() {
    }
    
    public void addListener(Listener ls){
        listeners.add(ls);
    }
    
    public void removeListener(Listener ls){
        listeners.remove(ls);
    }
    
    public void fireReportServerSystemEvent(String s){
        fireReportServerSystemEvent(s, QVServerEvent.MSG);
    }
    
    public void fireReportServerSystemEvent(String s, int action){
        fireReportServerEvent(QVServerEvent.SYSTEM, s, null, action);
    }
    
    public void fireReportServerSocketEvent(Socket socket, String msg){
        fireReportServerSocketEvent(socket, msg, QVServerEvent.MSG);
    }
    
    public void fireReportServerSocketEvent(Socket socket, String msg, int action){
        fireReportServerEvent(QVServerEvent.SOCKET, msg, socket, action);
    }
    
    public void fireReportServerEvent(int type, String msg, Socket socket, int action){
        QVServerEvent ev=new QVServerEvent(type, msg, socket, action);
        for(int i=0; i<listeners.size(); i++){
            ((Listener)listeners.get(i)).reportEventPerformed(ev);
        }
    }    
}
