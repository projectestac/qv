package edu.xtec.qv.servlet.util;

import java.util.HashMap;

public class QTIUserResponsesSymtab extends HashMap{
    
    private static boolean verbose=true;
    
    public void addVar(String sNameSpace, String sName, String value){
		//System.out.println("QTIUserResponsesSymtab.addVar-> ns="+sNameSpace+" name="+sName+"  value="+value);
        try{
            if (value==null) value="0";
            HashMap hmNameSpace;
            if (containsKey(sNameSpace))
                hmNameSpace=(HashMap)get(sNameSpace);
            else{
                hmNameSpace=new HashMap();
                put(sNameSpace,hmNameSpace);
            }
            if (hmNameSpace.containsKey(sName)){
                QTIUserResponseElement e=(QTIUserResponseElement)(hmNameSpace.get(sName));
                e.setMultiple(true);
                e.addValue(value);
            }
            else{
                QTIUserResponseElement e=new QTIUserResponseElement(value);
                hmNameSpace.put(sName,e);
            }
        }
        catch (Exception e){
            System.out.println("Excepció:"+e);
            if (verbose) e.printStackTrace(System.out);
        }
    }
    
    public Object getValue(String sNameSpace,String sVarName){
                /* Si la variable té múltiples valors es retorna només el primer.
                Per fer una comparació amb un valor s'ha de fer servir la funció isEqual.*/
        if (containsKey(sNameSpace)){
            HashMap hmNameSpace=(HashMap)get(sNameSpace);
            QTIUserResponseElement e=(QTIUserResponseElement)(hmNameSpace.get(sVarName));
            if (e!=null){
                return e.getValue();
            }
            else{
                //System.out.println("Excepció: Variable "+sVarName+" no declarada.");
                return null;
            }
        }
        else{
            if (verbose) System.out.println("Excepció: Namespace "+sNameSpace+" no declarat.");
            return null;
        }
    }
    
    public boolean isEqual(String sNameSpace, String sVarName, String op1, boolean bCaseSensitive){
        return isEqual(sNameSpace,sVarName,op1,bCaseSensitive,0); //index==0 indica que no estem demanant una comparació d'una posició de llista
    }
    
	public boolean isEqual(String sNameSpace, String sVarName, String op1, boolean bCaseSensitive, int index){
		return isEqual(sNameSpace, sVarName, op1, bCaseSensitive, index, true);
	}
    public boolean isEqual(String sNameSpace, String sVarName, String op1, boolean bCaseSensitive, int index, boolean bExact){
		//System.out.println("QTIUserResponsesSymtab.isEqual-> name="+sNameSpace+"  var="+sVarName+" op1="+op1+"  index="+index);
        HashMap hmNameSpace=(HashMap)get(sNameSpace);
        if (hmNameSpace==null) return false;
        QTIUserResponseElement e=(QTIUserResponseElement)(hmNameSpace.get(sVarName));
        //System.out.println("QTIUserResponsesSymtab.isEqual-> e="+e);
        if (e!=null){
            return e.isEqual(op1,bCaseSensitive,index, bExact);
        }
        else{
            //System.out.println("Excepció: Variable "+sVarName+" no declarada.");
            return false;
        }
    }
    
    public boolean contains(String sNameSpace,String sVarName){
        if (containsKey(sNameSpace)){
            HashMap hmNameSpace=(HashMap)get(sNameSpace);
            return (hmNameSpace.containsKey(sVarName));
        }
        else return false;
    }
    
    public boolean hasAttemptedItem(String sItemName){
        //System.out.println("hasAttempted("+sItemName+")?");
        boolean b=containsKey(sItemName);
        if (b){
            //System.out.println("contains");
            Object o=get(sItemName);
            if (o!=null && o instanceof HashMap){
                b=((HashMap)o).size()>0;
                //System.out.println("instance. Size="+((HashMap)o).size()+" b="+b);
            }
        }
        return b;
    }
    
    public String toString(){
        String s="";
        java.util.Iterator it=keySet().iterator();
        while (it.hasNext()){
            Object oKeyName=it.next();
            s+=oKeyName+"="+get(oKeyName)+"\n";
        }
        return s;
    }
    
    public class QTIUserResponseElement{
        private java.util.Vector vValue;
        private boolean bMultiple=false;
        
        public QTIUserResponseElement(Object oValue){
            vValue=new java.util.Vector();
            if (oValue!=null) vValue.add(oValue);
        }
        
        public void setValue(Object oValue){
            vValue.clear();
            vValue.add(oValue);
        }
        
        public void addValue(Object value){
            vValue.add(value);
        }
        
        public void setMultiple(boolean b){
            bMultiple=b;
        }
        
        public Object getValue(){
            if (vValue.size()>0) return vValue.firstElement();
            else return null;
        }
        
        public boolean isMultiple(){
            return bMultiple;
        }
        
        public boolean isEqual(String op1, boolean bCaseSensitive){
            return isEqual(op1,bCaseSensitive,0);
        }
        
		public boolean isEqual(String op1, boolean bCaseSensitive, int index){
			return isEqual(op1, bCaseSensitive, index, true);
		}
        public boolean isEqual(String op1, boolean bCaseSensitive, int index, boolean bExact){
            if (index==0) index=-1; ///AFEGIT PER TAL QUE SI ÉS MÚLTIPLE SEMPRE HO BUSQUI
            boolean bEqual=false;
            //System.out.println("----------------------------------------------------------");
            //System.out.println("op1:"+op1+" value:"+vValue+" index="+index+"<--"+" multiple?"+isMultiple()+"  exact?"+bExact);
            if (isMultiple()){
				java.util.Enumeration e=vValue.elements();
				while (e.hasMoreElements() && !bEqual){
					String op2=e.nextElement().toString();
					//System.out.println("op2:"+op2+"<--");
					String sElement;
					if (index!=0){
						//System.out.println("index:"+index);
						java.util.StringTokenizer st=new java.util.StringTokenizer(op2,",");
						for (int i=1;st.hasMoreTokens() && !bEqual;i++){
							sElement=st.nextToken().trim();
							if (index==-1 || i==index) bEqual=isEqual(op1,sElement,bCaseSensitive);
							//System.out.println("sElement:"+sElement+"<-- index="+index+"<--");
						}
					}
					else bEqual=isEqual(op1,op2,bCaseSensitive);
				}
            }else{
            	if (bExact){
					bEqual = isEqual(op1, (String)getValue(), bCaseSensitive);
            	}else{
            		bEqual = ((String)getValue()).indexOf(op1)>=0;
            	}
            }
            //System.out.println("equal?"+bEqual);
            return bEqual;
        }
        
        private boolean isEqual(String op1, String sElement,boolean bCaseSensitive){
            boolean bEqual=false;
            try{
                double d1=Double.parseDouble(op1.trim());
                double d2=Double.parseDouble(sElement.trim());
                //System.out.println(d1+"=="+d2+"?");
                bEqual=(d1==d2);
            }
            catch (Exception ex){ //Si es produeix una excepció és que no son numeros (seran String)
                if (bCaseSensitive) bEqual=(op1.trim().equals(sElement.trim()));
				else bEqual=op1.trim().equalsIgnoreCase(sElement.trim());
                //else bEqual=((op1.trim().toLowerCase().equals(sElement.trim().toLowerCase())));
                //System.out.println(op1.trim().toLowerCase()+"=="+sElement.trim().toLowerCase()+"?"+bEqual);
            }
            return bEqual;
        }
        
        public String toString(){
            return vValue.toString()+" multiple?"+bMultiple;
        }
    }
}
