package edu.xtec.qv.servlet.util;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class QTISymtab extends HashMap{

	public static final String STRING_TYPE="String";
	public static final String DECIMAL_TYPE="Decimal";
	public static final String SCIENTIFIC_TYPE="Scientific";
	public static final String BOOLEAN_TYPE="Boolean";
	public static final String INTEGER_TYPE="Integer";
	public static final String ENUMERATED_TYPE="Enumerated";
	public static final String DEFAULT_TYPE="Integer";

	public static final String DEFAULT_VARNAME="SCORE";

	private static boolean verbose=true;
	protected static Logger logger = Logger.getRootLogger();

	private HashMap hmMapInput,hmMapOutput;

	public QTISymtab(){
		hmMapInput=new HashMap();
		hmMapOutput=new HashMap();
	}

        public void addVar(String name, String type, String value, boolean isSubvar){
            /* isSubvar serveix per indicar que la variable que s'està declarant és subvariable i per tant,
             no cal crear les "subvariables" min, max i normalized*/
            //System.out.println("Declarant variable :"+name+" value="+value);
		try{
			if (type==null) type=DEFAULT_TYPE;
			if (value==null) value="0";
			QTISymtabElement e=new QTISymtabElement(type);
			if (type.equals(STRING_TYPE)) e.setValue(value);
			else if (type.equals(DECIMAL_TYPE)) e.setValue(new Float(value));
			else if (type.equals(SCIENTIFIC_TYPE)) e.setValue(new Double(value));
			else if (type.equals(BOOLEAN_TYPE)) e.setValue(new Boolean(value));
			else if (type.equals(INTEGER_TYPE)) e.setValue(new Integer(value));
			else if (type.equals(ENUMERATED_TYPE)) e.setValue(value);
			else e.setValue(new Integer(value));
                        if (isSubvar) name=getVarNameApplyingMaps(name); //Es declara una subVariable, la superVariable podria estar mapejada. Cal comprovar el nom.
                        
			put(name,e);
                        if (!isSubvar && (type.equals(INTEGER_TYPE)||type.equals(DECIMAL_TYPE)||type.equals(SCIENTIFIC_TYPE))){
                            // A les declaracions de tipus numeric a QTI queda implícita la declaració de les "subvariables" min, max i normalized                            
                            put(name+".min",new QTISymtabElement(type));
                            put(name+".max",new QTISymtabElement(type));
                            put(name+".normalized",new QTISymtabElement(DECIMAL_TYPE));
                        }
		}
		catch (Exception e){
			logger.debug("EXCEPTION: "+e);
			//System.out.println("Excepció:"+e);
			if (verbose) e.printStackTrace(System.out);
		}
        }
        
	public void addVar(String name, String type, String value){
            addVar(name,type,value,false); //!isSubvar
	}

	public void mapInput(String fromVarName, String toVarName){
		/* Els items que conté un assessment o section han de tenir
		declarades unes variables amb un nom concret per tal de calcular
		les puntuacions segons l'algorisme establert a l'outcomes_processing.
		Amb mapInput fem que aquest nom pugui ser el que vulguem i que quan
		es demani el que hauria de tenir es contesti amb el que té. */
		if (containsKey(toVarName) && !hmMapInput.containsKey(fromVarName))
			hmMapInput.put(fromVarName,toVarName);
		//L'especificació del QTI indica que només es tindrà en compte el primer mapeig
		//i que el mapeig s'aplicarà a variables declarades. IMS Question&Test Interoperability: ASI Outcomes Processing Final Specification Version 1.2 p.26.
	}

	public void mapOutput(String fromVarName, String toVarName){
		/* Els algorismes per calcular les puntuacions tenen un nom per defecte
		per les variables de sortida (resultats). Amb mapOutput fem que
		quan es calculin aquests es pugui fer servir un nom diferent.*/
		if (containsKey(toVarName) && !hmMapOutput.containsKey(fromVarName))
			hmMapOutput.put(fromVarName,toVarName);
                mapInput(fromVarName,toVarName); /* També s'afegeix al mapInput perquè les puntuacions són d'INOUT */
		//L'especificació del QTI indica que només es tindrà en compte el primer mapeig
		//i que el mapeig s'aplicarà a variables declarades. IMS Question&Test Interoperability: ASI Outcomes Processing Final Specification Version 1.2 p.26.
	}

	public void setVar(String varName, Object oValue){
		QTISymtabElement e=getVarApplyingMaps(varName,hmMapOutput);
		if (e!=null){
                    String type=e.getType();
                    if ((oValue instanceof String) && !type.equals(STRING_TYPE)){
                        String value=(String)oValue;
       			if (type.equals(DECIMAL_TYPE)) e.setValue(new Float(value));
			else if (type.equals(SCIENTIFIC_TYPE)) e.setValue(new Double(value));
			else if (type.equals(BOOLEAN_TYPE)) e.setValue(new Boolean(value));
			else if (type.equals(INTEGER_TYPE)) e.setValue(new Integer(value));
			else if (type.equals(ENUMERATED_TYPE)) e.setValue(value);
			else e.setValue(new Integer(value));
                    }
                    else e.setValue(oValue);
                    //put(varName,e); No cal fer un public doncs ja s'ha modificat el valor sobre el QTISymtabElement. A més, varName pot no ser el nom de la variable (mapeigs).
		}
		//else System.out.println("Excepció: Variable "+varName+" no declarada.");
	}
        
        public void changeType(String sVarName, String sType){
            /* Per canviar el tipus amb el que s'ha declarat una variable. El valor passarà a null.*/
            QTISymtabElement e=getVarApplyingMaps(sVarName,hmMapOutput);
            if (e!=null) e.changeType(sType);
            //else System.out.println("Excepció: Variable "+sVarName+" no declarada.");                
        }

	private QTISymtabElement getVarApplyingMaps(String varName, HashMap hm){
		//QTISymtabElement e=null;
                String sRealVarName=getVarNameApplyingMaps(varName,hm);
                return ((QTISymtabElement)get(sRealVarName));
		/*int i=varName.indexOf('.');
		try{
			if (i>=0){ //És una subvariable. Només es fa el mapeig del nom de la "supervariable".
				String sSuperVarName=varName.substring(0,i);
				String sSubVarName=varName.substring(i+1);
				if (hm.containsKey(sSuperVarName)) sSuperVarName=hm.get(sSuperVarName).toString();
				e=(QTISymtabElement)get(sSuperVarName+"."+sSubVarName);
			}
			else{
				if (hm.containsKey(varName)) varName=hm.get(varName).toString();
				e=(QTISymtabElement)get(varName);
			}
		}
		catch (Exception ex){
			if (verbose) System.out.println("Error: El nom d'una variable no pot acabar en punt ("+varName+").");
		}*/
		//return e;
	}
        
        private String getVarNameApplyingMaps(String sVarName){
            /* Es rep un nom de variable (o subVariable) i retorna el nom que té un cop mapejada (si ho està). En el cas que
             es demani el nom d'una subvariable no declarada el resultat serà el que tindria si la supervariable està declarada.*/
            String s=getVarNameApplyingMaps(sVarName,hmMapInput);
            if (s.equals(sVarName)) return getVarNameApplyingMaps(sVarName,hmMapOutput);
            else return s;
        }
        
        private String getVarNameApplyingMaps(String sVarName,HashMap hm){
                QTISymtabElement e=null;
		int i=sVarName.indexOf('.');
		try{
			if (i>=0){ //És una subvariable. Només es fa el mapeig del nom de la "supervariable".
				String sSuperVarName=sVarName.substring(0,i);
				String sSubVarName=sVarName.substring(i+1);
				if (hm.containsKey(sSuperVarName)) sSuperVarName=hm.get(sSuperVarName).toString();
				sVarName=(sSuperVarName+"."+sSubVarName);
			}
			else{
				if (hm.containsKey(sVarName)) sVarName=hm.get(sVarName).toString();
				e=(QTISymtabElement)get(sVarName);
			}
		}
		catch (Exception ex){
			if (verbose) System.out.println("Error: El nom d'una variable no pot acabar en punt ("+sVarName+").");
		}
                return sVarName;
        }

	private QTISymtabElement getVar(String varName){
		/* No s'ha de fer directament get,put,contains sobre el HashMap perquè poden haber-hi mapeigs.*/
                return getVarApplyingMaps(varName,hmMapInput);
	}

	public String getType(String varName){
		QTISymtabElement e=getVar(varName);
		if (e!=null){
			return e.getType();
		}
		else{
			//System.out.println("Error getType: Variable "+varName+" no declarada.");
			return "";
		}
	}

	public Object getValue(String varName){
		QTISymtabElement e=getVar(varName);
		if (e!=null){
			return e.getValue();
		}
		else{
			//System.out.println("Error getValue: Variable "+varName+" no declarada.");
			return null;
		}
	}

	public boolean contains(String varName){
		QTISymtabElement e=null;
		int i=varName.indexOf('.');
		try{
			if (i>=0){ //És una subvariable. Només es fa el mapeig del nom de la "supervariable".
				String sSuperVarName=varName.substring(0,i);
				String sSubVarName=varName.substring(i+1);
                                //System.out.println("sSuperVarName="+sSuperVarName);
                                //System.out.println("sSubVarName="+sSubVarName);
                                
				if (hmMapInput.containsKey(sSuperVarName)){
					sSuperVarName=hmMapInput.get(sSuperVarName).toString();
                                        //System.out.println("mapInput contains "+sSuperVarName+" containsKey("+sSuperVarName+"."+sSubVarName+")?"+hmMapInput.containsKey(sSuperVarName+"."+sSubVarName));
					return containsKey(sSuperVarName+"."+sSubVarName);
				}
				else if (hmMapOutput.containsKey(sSuperVarName)){
					sSuperVarName=hmMapOutput.get(sSuperVarName).toString();
                                        //System.out.println("mapOutput contains "+sSuperVarName+" containsKey("+sSuperVarName+"."+sSubVarName+")?"+hmMapOutput.containsKey(sSuperVarName+"."+sSubVarName));
					return containsKey(sSuperVarName+"."+sSubVarName);
				}
				else return containsKey(varName);
			}
			else{
				return (containsKey(varName)||hmMapInput.containsKey(varName)
				||hmMapOutput.containsKey(varName));
			}
		}
		catch (Exception ex){
			if (verbose) System.out.println("Error: El nom d'una variable no pot acabar en punt ("+varName+").");
			return false;
		}	
	}
        
        public void addToVar(String varName, Object operand){
            if (contains(varName)){
                //System.out.println("addToVar:contains("+varName+")");
                String sType=getType(varName);
                Object operand2=getValue(varName);
                Object result=add(sType,operand,operand2);
                setVar(varName,result);
                //System.out.println("setVar("+varName+","+result+") fet");
            }
            else{
                //System.out.println("addToVar");
                //System.out.println("Excepció: Variable "+varName+" no declarada.");
            }
        }
        
        public void subtractToVar(String varName, Object operand){
            if (contains(varName)){
                String sType=getType(varName);
                Object operand2=getValue(varName);
                Object result=subtract(sType,operand2,operand);
                setVar(varName,result);
            }
            else{
                //System.out.println("Excepció: Variable "+varName+" no declarada.");
            }
        }

        public void multVar(String varName, Object operand){
            if (contains(varName)){
                String sType=getType(varName);
                Object operand2=getValue(varName);
                Object result=mult(sType,operand2,operand);
                setVar(varName,result);
            }
            else{
                //System.out.println("Excepció: Variable "+varName+" no declarada.");
            }
        }
        
        public void divVar(String varName, Object operand){
            if (contains(varName)){
                String sType=getType(varName);
                Object operand2=getValue(varName);
                Object result=div(sType,operand2,operand);
                setVar(varName,result);
            }
            else{
                //System.out.println("Excepció: Variable "+varName+" no declarada.");
            }
        }

        public static Object add(String sType, Object operand1, Object operand2){
            Object result=null;
            if (operand1==null || operand2==null) return null;
            String sOperand1=operand1.toString();
            String sOperand2=operand2.toString();            
            if (sType.equals(QTISymtab.INTEGER_TYPE)){
                int op1=Integer.parseInt(sOperand1);
                int op2=Integer.parseInt(sOperand2);
                result=new Integer(op1+op2);
            }
            else if (sType.equals(QTISymtab.DECIMAL_TYPE)){
                float op1=Float.parseFloat(sOperand1);
                float op2=Float.parseFloat(sOperand2);
                result=new Float(op1+op2);
            }
            else if (sType.equals(QTISymtab.SCIENTIFIC_TYPE)){
                double op1=Double.parseDouble(sOperand1);
                double op2=Double.parseDouble(sOperand2);
                result=new Double(op1+op2);
            }
            else{
                System.out.println("Error: add només suma valors numerics ("+sOperand1+"+"+sOperand2+")."); 
            }
            return result;
        }

        public static Object subtract(String sType, Object operand1, Object operand2){
            Object result=null;
            if (operand1==null || operand2==null) return null;
            String sOperand1=operand1.toString();
            String sOperand2=operand2.toString();            
            if (sType.equals(QTISymtab.INTEGER_TYPE)){
                int op1=Integer.parseInt(sOperand1);
                int op2=Integer.parseInt(sOperand2);
                result=new Integer(op1-op2);
            }
            else if (sType.equals(QTISymtab.DECIMAL_TYPE)){
                float op1=Float.parseFloat(sOperand1);
                float op2=Float.parseFloat(sOperand2);
                result=new Float(op1-op2);
            }
            else if (sType.equals(QTISymtab.SCIENTIFIC_TYPE)){
                double op1=Double.parseDouble(sOperand1);
                double op2=Double.parseDouble(sOperand2);
                result=new Double(op1-op2);
            }
            else{
                System.out.println("Error: sustract només resta valors numerics ("+sOperand1+"-"+sOperand2+")."); 
            }
            return result;
        }
                
        public static Object mult(String sType, Object operand1, Object operand2){
            Object result=null;
            if (operand1==null || operand2==null) return null;
            String sOperand1=operand1.toString();
            String sOperand2=operand2.toString();            
            if (sType.equals(QTISymtab.INTEGER_TYPE)){
                int op1=Integer.parseInt(sOperand1);
                int op2=Integer.parseInt(sOperand2);
                result=new Integer(op1*op2);
            }
            else if (sType.equals(QTISymtab.DECIMAL_TYPE)){
                float op1=Float.parseFloat(sOperand1);
                float op2=Float.parseFloat(sOperand2);
                result=new Float(op1*op2);
            }
            else if (sType.equals(QTISymtab.SCIENTIFIC_TYPE)){
                double op1=Double.parseDouble(sOperand1);
                double op2=Double.parseDouble(sOperand2);
                result=new Double(op1*op2);
            }
            else{
                System.out.println("Error: mult només multiplica valors numerics ("+sOperand1+"*"+sOperand2+")."); 
            }
            return result;
        }
        
        public static Object div(String sType, Object operand1, Object operand2){
            Object result=null;
            if (operand1==null || operand2==null) return null;
            String sOperand1=operand1.toString();
            String sOperand2=operand2.toString();            
            if (sType.equals(QTISymtab.INTEGER_TYPE)){
                int op1=Integer.parseInt(sOperand1);
                int op2=Integer.parseInt(sOperand2);
                result=new Double(op1/op2);
            }
            else if (sType.equals(QTISymtab.DECIMAL_TYPE)){
                float op1=Float.parseFloat(sOperand1);
                float op2=Float.parseFloat(sOperand2);
                result=new Double(op1/op2);
            }
            else if (sType.equals(QTISymtab.SCIENTIFIC_TYPE)){
                double op1=Double.parseDouble(sOperand1);
                double op2=Double.parseDouble(sOperand2);
                result=new Double(op1/op2);
            }
            else{
                System.out.println("Error: div només divideix valors numerics ("+sOperand1+"/"+sOperand2+")."); 
            }
            return result;
        }

        public Object getScore(){
            if (getVar(DEFAULT_VARNAME)!=null) return getVar(DEFAULT_VARNAME).getValue();
            else if (getVar("COUNT")!=null) return getVar("COUNT").getValue();
            else{
                java.util.Iterator it=keySet().iterator();
                while (it.hasNext()){ 
                    Object oCurrent=it.next();
                    if (oCurrent.toString().indexOf('.')<0) return ((QTISymtabElement)get(oCurrent)).getValue(); // No és subvariable
                }
                if (!it.hasNext()){ //Si arriba aquí, la condició serà sempre true (no arribarà).
                    System.out.println("Excepció: No hi ha resultat."); // No pot passar mai perquè el dtd força a que es declari com a mínim una variable                    
                }
            }
            return null;
        }

	public static void cleanMapInput(java.util.Vector vSymtabs){
		java.util.Enumeration e=vSymtabs.elements();
		while (e.hasMoreElements()){
			QTISymtab st=(QTISymtab)e.nextElement();
			st.hmMapInput.clear();
		}
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

	public class QTISymtabElement{
		private String type;
		private Object value;
		
		public QTISymtabElement(String type, Object value){
			this.type=type;
			this.value=value;
		}

		public QTISymtabElement(String type){
			this(type,null);
		}

		public void setValue(Object value){
			this.value=value;
		}

		public String getType(){
			return type;
		}
                
                public void changeType(String newType){
                    type=newType;
                    value=null;
                }

		public Object getValue(){
			return value;
		}
                
                public String toString(){
                    if (value!=null) return value.toString();
                    else return("null");
                }
	}
}
