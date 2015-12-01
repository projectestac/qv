package edu.xtec.qv.servlet.rp.compact;
/*
 * PrepareQVCompact.java
 *
 * Created on 12 / maig / 2003, 12:54
 */

import edu.xtec.qv.db.QVDataManage;
import edu.xtec.qv.qti.util.DataAdmin;
import edu.xtec.qv.qti.util.QVSession;
import edu.xtec.qv.servlet.rp.PrepareQV;

/**
 *
 * @author  allastar
 */
public class PrepareQVCompact extends PrepareQV{
    
    static DataAdmin db;
    
    /** Creates a new instance of PrepareQVCompact */
    public PrepareQVCompact() {
        super(null);
        QVSession.clean();
    }
    
    public QVDataManage createQVDataManage() {
        if (db==null) db=DataAdmin.getDefaultInstance();
        DataAdmin.clean();
        return db;
    }
    
    public void freeConnection() {
    }
    
    public String getRemoteAddr() {
        return "127.0.0.1";
    }
    
}
