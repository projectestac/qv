/*
 * ReportServerEvent.java
 *
 * Created on 6 de septiembre de 2002, 13:19
 */

package edu.xtec.qv.editor.server;

import java.net.Socket;

/**
 *
 * @author Francesc Busquets (fbusquets@pie.xtec.es)
 */
public class QVServerEvent extends Object{
    
    public static final int SYSTEM=0, SOCKET=1, DB=2;
    public static final int CONNECT=0, DISCONNECT=1, START=2, STOP=3, MSG=4, ERROR=5, CONNECTION=6;
    public int type;
    public int action;
    public String msg;
    public Socket socket;
    
    public QVServerEvent(int type, String msg, Socket socket, int action){
        this.type=type;
        this.msg=msg;
        this.socket=socket;
        this.action=action;
    }
        
    public String toString(){
        StringBuffer sb=new StringBuffer(100);        
        sb.append(type==SYSTEM ? "SYSTEM " : type==SOCKET ? "" : "DB ");
        String sAct="";
        switch(action){
            case CONNECT: sAct="CONNECT"; break;
            case DISCONNECT: sAct="DISCONNECT"; break;
            case START: sAct="START"; break;
            case STOP: sAct="STOP"; break;
            case ERROR: sAct="ERROR"; break;
            case CONNECTION: sAct=msg==null ? "CONNECTION" : ""; break;
        }
        sb.append(sAct).append(" ");
        if(socket!=null) sb.append(socket.getInetAddress().getHostAddress()).append(":").append(socket.getPort()).append(" ");
        if(msg!=null) sb.append(msg);
        return sb.substring(0);
    }
}
