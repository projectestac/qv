/*
 * QTIItemOrderingResponsesPanel.java
 *
 * Created on 19 / setembre / 2003, 08:22
 */

package edu.xtec.qv.editor.ui;

import edu.xtec.qv.qti.QTIItemSelectionResponses;

/**
 *
 * @author  allastar
 */
public class QTIItemOrderingResponsesPanel extends QTIItemSelectionResponsesPanel{
    
    /** Creates a new instance of QTIItemOrderingResponsesPanel */
    public QTIItemOrderingResponsesPanel(QTIItemSelectionResponses qtiItemSelResp, java.util.Vector vSelectionResponses) {
        super(qtiItemSelResp, vSelectionResponses);
    }
    
    protected void initComponents2(java.util.Vector vInitialResponses){
        super.initComponents2(vInitialResponses);
        remove(cbDisplayList);
        remove(cbMultiple);
    }
}
