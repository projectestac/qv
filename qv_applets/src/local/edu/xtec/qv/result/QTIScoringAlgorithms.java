package edu.xtec.qv.result;

/*
 * QTIScoringAlgorithms.java
 *
 * Created on 26 de agosto de 2002, 8:30
 */

/**
 *
 * @author  allastar
 */
public class QTIScoringAlgorithms {
    
    public static String SUM_OF_SCORES="SumofScores";
    public static String SUM_OF_SCORES_ATTEMPTED="SumofScoresAttempted";
    public static String WEIGHTED_SUM_OF_SCORES="WeightedSumofScores";
    public static String WEIGHTED_SUM_OF_SCORES_ATTEMPTED="WeightedSumofScoresAttempted";
    public static String PARAMETER_WEIGHTED_SUM_OF_SCORES="ParameterWeightedSumofScores";
    public static String PARAMETER_WEIGHTED_SUM_OF_SCORES_ATTEMPTED="ParameterWeightedSumofScoresAttempted";
    public static String NUMBER_CORRECT="NumberCorrect";
    public static String NUMBER_CORRECT_ATTEMPTED="NumberCorrectAttempted";
    public static String WEIGHTED_NUMBER_CORRECT="WeightedNumberCorrect";
    public static String WEIGHTED_NUMBER_CORRECT_ATTEMPTED="WeightedNumberCorrectAttempted";
    public static String PARAMETER_WEIGHTED_NUMBER_CORRECT="ParameterWeightedNumberCorrect";
    public static String PARAMETER_WEIGHTED_NUMBER_CORRECT_ATTEMPTED="ParameterWeightedNumberCorrectAttempted";
    public static String GUESSING_PENALTY="GuessingPenalty";
    public static String WEIGHTED_GUESSING_PENALTY="WeightedGuessingPenalty";
    public static String BEST_K_FROM_N="BestKfromN";
    
    public static String DEFAULT_SCOREMODEL=SUM_OF_SCORES;
    
    public static String PARAM_PREFIX="param.";
    
    public static QTISymtab execScoringAlgorithm(String sScoringAlgorithmName, java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTIUserResponsesSymtab stUserResponses, QTISymtab symtab){
        ////System.out.println("execScoringAlgorithm "+sScoringAlgorithmName);
        ////System.out.println("taules inicials:");
        ////QTIResponseParser.showSymtabVector(vSelectedObjects);
        ////System.out.println("-fi taules inicials");
        ////System.out.println("ABANS: symtab:"+symtab);
        if (sScoringAlgorithmName.trim().equalsIgnoreCase(SUM_OF_SCORES))
            symtab=execSumOfScoresAlgorithm(vAllSymtabs,vSelectedObjects,symtab);
        else if (sScoringAlgorithmName.trim().equalsIgnoreCase(SUM_OF_SCORES_ATTEMPTED))
            symtab=execSumOfScoresAttemptedAlgorithm(vAllSymtabs,vSelectedObjects,stUserResponses,symtab);
        else if (sScoringAlgorithmName.trim().equalsIgnoreCase(WEIGHTED_SUM_OF_SCORES))
            symtab=execWeightedSumOfScoresAlgorithm(vAllSymtabs,vSelectedObjects,symtab);
        else if (sScoringAlgorithmName.trim().equalsIgnoreCase(WEIGHTED_SUM_OF_SCORES_ATTEMPTED))
            symtab=execWeightedSumOfScoresAttemptedAlgorithm(vAllSymtabs,vSelectedObjects,stUserResponses,symtab);
        else if (sScoringAlgorithmName.trim().equalsIgnoreCase(PARAMETER_WEIGHTED_SUM_OF_SCORES))
            symtab=execParameterWeightedSumOfScoresAlgorithm(vAllSymtabs,vSelectedObjects,symtab);
        else if (sScoringAlgorithmName.trim().equalsIgnoreCase(PARAMETER_WEIGHTED_SUM_OF_SCORES_ATTEMPTED))
            symtab=execParameterWeightedSumOfScoresAttemptedAlgorithm(vAllSymtabs,vSelectedObjects,stUserResponses,symtab);
        else if (sScoringAlgorithmName.trim().equalsIgnoreCase(NUMBER_CORRECT))
            symtab=execNumberCorrectAlgorithm(vAllSymtabs,vSelectedObjects,symtab);
        else if (sScoringAlgorithmName.trim().equalsIgnoreCase(NUMBER_CORRECT_ATTEMPTED))
            symtab=execNumberCorrectAttemptedAlgorithm(vAllSymtabs,vSelectedObjects,stUserResponses,symtab);
        else if (sScoringAlgorithmName.trim().equalsIgnoreCase(WEIGHTED_NUMBER_CORRECT))
            symtab=execWeightedNumberCorrectAlgorithm(vAllSymtabs,vSelectedObjects,symtab);
        else if (sScoringAlgorithmName.trim().equalsIgnoreCase(WEIGHTED_NUMBER_CORRECT_ATTEMPTED))
            symtab=execWeightedNumberCorrectAttemptedAlgorithm(vAllSymtabs,vSelectedObjects,stUserResponses,symtab);
        else if (sScoringAlgorithmName.trim().equalsIgnoreCase(PARAMETER_WEIGHTED_NUMBER_CORRECT))
            symtab=execParameterWeightedNumberCorrectAlgorithm(vAllSymtabs,vSelectedObjects,symtab);
        else if (sScoringAlgorithmName.trim().equalsIgnoreCase(PARAMETER_WEIGHTED_NUMBER_CORRECT_ATTEMPTED))
            symtab=execParameterWeightedNumberCorrectAttemptedAlgorithm(vAllSymtabs,vSelectedObjects,stUserResponses,symtab);
        else if (sScoringAlgorithmName.trim().equalsIgnoreCase(GUESSING_PENALTY))
            symtab=execGuessingPenaltyAlgorithm(vAllSymtabs,vSelectedObjects,stUserResponses,symtab);
        else if (sScoringAlgorithmName.trim().equalsIgnoreCase(WEIGHTED_GUESSING_PENALTY))
            symtab=execWeightedGuessingPenaltyAlgorithm(vAllSymtabs,vSelectedObjects,stUserResponses,symtab);
        else if (sScoringAlgorithmName.trim().equalsIgnoreCase(BEST_K_FROM_N))
            symtab=execBestKFromNAlgorithm(vAllSymtabs,vSelectedObjects,stUserResponses,symtab);
        else{
            System.out.println("L'Scoring Algorithm "+sScoringAlgorithmName+" no està suportat.");
        }
        //System.out.println("DESPRÉS: symtab:"+symtab);
        //System.out.println("El resultat és:");
        //System.out.println(symtab);
        //System.out.println("---------------");
        return symtab;
    }
    
    private static QTISymtab execSumOfScoresAlgorithm(java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTISymtab symtab){
        return execGenericSumOfScoresAlgorithm(false,false,false,vAllSymtabs,vSelectedObjects,null,symtab); //!bParameter,!weighting,!OnlyAttempted
    }
    
    private static QTISymtab execSumOfScoresAttemptedAlgorithm(java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTIUserResponsesSymtab stUserResponses, QTISymtab symtab){
        return execGenericSumOfScoresAlgorithm(false,false,true,vAllSymtabs,vSelectedObjects,stUserResponses,symtab); //!bParameter,!weighting,OnlyAttempted
    }
    
    private static QTISymtab execWeightedSumOfScoresAlgorithm(java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTISymtab symtab){
        return execGenericSumOfScoresAlgorithm(false,true,false,vAllSymtabs,vSelectedObjects,null,symtab); //!bParameter,weighting,!OnlyAttempted
    }
    
    private static QTISymtab execWeightedSumOfScoresAttemptedAlgorithm(java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTIUserResponsesSymtab stUserResponses, QTISymtab symtab){
        return execGenericSumOfScoresAlgorithm(false,true,true,vAllSymtabs,vSelectedObjects,stUserResponses,symtab); //!bParameter,weighting,OnlyAttempted
    }
    
    private static QTISymtab execParameterWeightedSumOfScoresAlgorithm(java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTISymtab symtab){
        return execGenericSumOfScoresAlgorithm(true,true,false,vAllSymtabs,vSelectedObjects,null,symtab); //bParameter,weighting,!OnlyAttempted
    }
    
    private static QTISymtab execParameterWeightedSumOfScoresAttemptedAlgorithm(java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTIUserResponsesSymtab stUserResponses, QTISymtab symtab){
        return execGenericSumOfScoresAlgorithm(true,true,true,vAllSymtabs,vSelectedObjects,stUserResponses,symtab); //bParameter,weighting,OnlyAttempted
    }
    
    private static QTISymtab execGenericSumOfScoresAlgorithm(boolean bParameter, boolean bWeighting, boolean bOnlyAttempted, java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTIUserResponsesSymtab stUserResponses, QTISymtab symtab){
        if (symtab.contains("SCORE")){
            Object oWeighting;
            symtab.setVar("SCORE","0"); //Es podria passar new Integer(0) enlloc de "0" però d'aquesta manera es dona suport a que pugui ser Float en un futur.
            symtab.setVar("SCORE.min","0");
            symtab.setVar("SCORE.max","0");
            symtab.setVar("SCORE.normalized","0.0");
            //System.out.println("#TS a tractar:"+vSelectedObjects.size());
            java.util.Enumeration e=vSelectedObjects.elements();
            while (e.hasMoreElements()){ // for ALL_SELECTED(childobjects) do
                QTISymtab st=(QTISymtab)e.nextElement();
                if (!bOnlyAttempted || hasAttempted(st,stUserResponses)){
                    //System.out.println("st.contains(SCORE)?"+st.contains("SCORE"));
                    if (!bWeighting) oWeighting=new Integer(1);
                    else oWeighting=getWeighting(st,bParameter);
                    String sType=st.getType("SCORE");
                    if (st.contains("SCORE")){ // if childobject(HAS_VARIABLE(SCORE)) = True
                        symtab.addToVar("SCORE.min",QTISymtab.mult(sType,st.getValue("SCORE.minvalue"),oWeighting));
                        symtab.addToVar("SCORE.max",QTISymtab.mult(sType,st.getValue("SCORE.maxvalue"),oWeighting));
                        symtab.addToVar("SCORE",QTISymtab.mult(sType,st.getValue("SCORE"),oWeighting));
                    }
                }
            }
            // parentobject.SCORE.normalized=(parentobject.SCORE-parentobject.SCORE.min)/(parentobject.SCORE.max-parentobject.SCORE.min);
            Object op1=QTISymtab.subtract(symtab.getType("SCORE"),symtab.getValue("SCORE"),symtab.getValue("SCORE.min"));
            Object op2=QTISymtab.subtract(symtab.getType("SCORE.max"),symtab.getValue("SCORE.max"),symtab.getValue("SCORE.min"));
            Object result=QTISymtab.div(QTISymtab.DECIMAL_TYPE,op1,op2); //La divisió per 0 provocarà un NaN (not a number)
            symtab.setVar("SCORE.normalized",result);
        }
        else{
            System.out.println("Error: La variable SCORE hauria d'estar declarada a l'outcomes o bé s'hauria de fer un map_output.");
        }
        return symtab;
    }
    
    private static QTISymtab execNumberCorrectAlgorithm(java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTISymtab symtab){
        return execGenericNumberCorrectAlgorithm(false,false,false,vAllSymtabs,vSelectedObjects,null,symtab); //!parameter,!weighted,!OnlyAttempted
    }
    
    private static QTISymtab execNumberCorrectAttemptedAlgorithm(java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTIUserResponsesSymtab stUserResponses, QTISymtab symtab){
        return execGenericNumberCorrectAlgorithm(false,false,true,vAllSymtabs,vSelectedObjects,stUserResponses,symtab); //!parameter,!weighted,OnlyAttempted
    }
    
    private static QTISymtab execWeightedNumberCorrectAlgorithm(java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTISymtab symtab){
        return execGenericNumberCorrectAlgorithm(false,true,false,vAllSymtabs,vSelectedObjects,null,symtab); //!parameter,weighted,!OnlyAttempted
    }
    
    private static QTISymtab execWeightedNumberCorrectAttemptedAlgorithm(java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTIUserResponsesSymtab stUserResponses, QTISymtab symtab){
        return execGenericNumberCorrectAlgorithm(false,true,true,vAllSymtabs,vSelectedObjects,stUserResponses,symtab); //!parameter,weighted,OnlyAttempted
    }
    
    private static QTISymtab execParameterWeightedNumberCorrectAlgorithm(java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTISymtab symtab){
        return execGenericNumberCorrectAlgorithm(true,true,false,vAllSymtabs,vSelectedObjects,null,symtab); //parameter,weighted,!OnlyAttempted
    }
    
    private static QTISymtab execParameterWeightedNumberCorrectAttemptedAlgorithm(java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTIUserResponsesSymtab stUserResponses, QTISymtab symtab){
        return execGenericNumberCorrectAlgorithm(true,true,true,vAllSymtabs,vSelectedObjects,stUserResponses,symtab); //parameter,weighted,OnlyAttempted
    }
    
    private static QTISymtab execGenericNumberCorrectAlgorithm(boolean bParameter, boolean bWeighting, boolean bOnlyAttempted, java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTIUserResponsesSymtab stUserResponses, QTISymtab symtab){
        if (symtab.contains("COUNT")){
            Object oWeighting;
            symtab.setVar("COUNT","0"); //Es podria passar new Integer(0) enlloc de "0" però d'aquesta manera es dona suport a que pugui ser Float en un futur.
            symtab.setVar("COUNT.min","0");
            symtab.setVar("COUNT.max","0");
            symtab.setVar("COUNT.normalized","0.0");
            //System.out.println("#TS a tractar:"+vSelectedObjects.size());
            java.util.Enumeration e=vSelectedObjects.elements();
            while (e.hasMoreElements()){
                QTISymtab st=(QTISymtab)e.nextElement();
                if (!bOnlyAttempted || hasAttempted(st,stUserResponses)){
                    //System.out.println("st.contains(SCORE)?"+st.contains("SCORE"));
                    if (!bWeighting) oWeighting=new Integer(1);
                    else oWeighting=getWeighting(st,bParameter);
                    
                    if (st.contains("CORRECT")){
                        symtab.addToVar("COUNT.max",oWeighting);
                        boolean bCorrect=false;
                        try{
                            bCorrect=((Boolean)st.getValue("CORRECT")).booleanValue();
                            if (bCorrect) symtab.addToVar("COUNT",oWeighting);
                        }
                        catch (Exception ex){
                            System.out.println("Error: constant booleana no vàlida.");
                        }
                    }
                }
                //else System.out.println("!hasAttempted");
            }
            Object result=QTISymtab.div(QTISymtab.DECIMAL_TYPE,symtab.getValue("COUNT"),symtab.getValue("COUNT.max")); //La divisió per 0 provocarà un NaN (not a number)
            symtab.setVar("COUNT.normalized",result);
        }
        else{
            System.out.println("Error: La variable COUNT hauria d'estar declarada a l'outcomes o bé s'hauria de fer un map_output.");
        }
        return symtab;
    }
    
    private static QTISymtab execGuessingPenaltyAlgorithm(java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTIUserResponsesSymtab stUserResponses, QTISymtab symtab){
        return execGenericGuessingPenaltyAlgorithm(false,vAllSymtabs,vSelectedObjects,stUserResponses,symtab);
    }
    
    private static QTISymtab execWeightedGuessingPenaltyAlgorithm(java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTIUserResponsesSymtab stUserResponses, QTISymtab symtab){
        return execGenericGuessingPenaltyAlgorithm(true,vAllSymtabs,vSelectedObjects,stUserResponses,symtab);
    }
    
    private static QTISymtab execGenericGuessingPenaltyAlgorithm(boolean bWeighting, java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTIUserResponsesSymtab stUserResponses, QTISymtab symtab){
        if (symtab.contains("COUNT")){
            Object oWeighting;
            
            symtab.changeType("COUNT",QTISymtab.DECIMAL_TYPE);
            symtab.setVar("COUNT",new Float(0));
            /*El tipus de la vaiable COUNT és per defecte enter, però la penalització podria ser real. Per tant, s'admet que COUNT sigui
             real i en el cas es compleixi que les penalitzacions són enters el resultat de count també serà enter.*/
            
            symtab.addVar("COUNT.correct",QTISymtab.INTEGER_TYPE,"0",true); //isSubVar
            symtab.addVar("COUNT.incorrect",QTISymtab.INTEGER_TYPE,"0",true); //isSubVar
            symtab.addVar("COUNT.unattempted",QTISymtab.INTEGER_TYPE,"0",true); //isSubVar
            /* Es podria eliminar de la TS les "subvariables" que es creen per defecte: COUNT.max, .min i .normalized */
            //System.out.println("#TS a tractar:"+vSelectedObjects.size());
            java.util.Enumeration e=vSelectedObjects.elements();
            while (e.hasMoreElements()){
                QTISymtab st=(QTISymtab)e.nextElement();
                if (st.contains("CORRECT")){ //HAS_VARIABLE(CORRECT)
                    if (hasAttempted(st,stUserResponses)){ //childobject(ATTEMPTED)=True
                        try{
                            if (!bWeighting) oWeighting=new Integer(1);
                            else oWeighting=getWeighting(st,true); //default=1
                            ////else oWeighting=getWeighting(st,false); //default=1 DECLARACIO_MODIF
                            
                            boolean bCorrect=((Boolean)st.getValue("CORRECT")).booleanValue();
                            if (bCorrect){ //childobject.CORRECT=True
                                symtab.addToVar("COUNT.correct",new Integer(1));
                                //System.out.println("Sumo a Count "+oWeighting);
                                symtab.addToVar("COUNT",oWeighting);
                            }
                            else{
                                Object oPenaltyValue=getPenaltyValue(st,symtab); //default=1
                                symtab.addToVar("COUNT.incorrect",new Integer(1));
                                symtab.subtractToVar("COUNT",QTISymtab.mult(symtab.getType("COUNT"),oWeighting,oPenaltyValue)); // COUNT és Integer, però en un futur podria ser Float i així ja es dona suport.
                                //System.out.println("Resto a Count "+symtab.mult(symtab.getType("COUNT"),oWeighting,oPenaltyValue)+" que és "+oWeighting+"*"+oPenaltyValue);
                            }
                        }
                        catch (Exception ex){
                            System.out.println("Error: constant booleana no vàlida.");
                            ex.printStackTrace(System.out);
                        }
                    } // !hasAttempted
                    else symtab.addToVar("COUNT.unattempted",new Integer(1));
                }
            }
        }
        else{
            System.out.println("Error: La variable COUNT hauria d'estar declarada a l'outcomes o bé s'hauria de fer un map_output.");
        }
        return symtab;
    }
    
    private static QTISymtab execBestKFromNAlgorithm(java.util.Vector vAllSymtabs, java.util.Vector vSelectedObjects, QTIUserResponsesSymtab stUserResponses, QTISymtab symtab){
        if (symtab.contains("SCORE")){
            symtab.setVar("SCORE","0"); //Es podria passar new Integer(0) enlloc de "0" però d'aquesta manera es dona suport a que pugui ser Float en un futur.
            symtab.setVar("SCORE.normalized","0.0");
            
            //System.out.println("#TS a tractar:"+vSelectedObjects.size());
            
            int k=getK(symtab,vSelectedObjects,stUserResponses);
            if (vSelectedObjects.size()>0){
                QTISymtab st=(QTISymtab)vSelectedObjects.firstElement();
                Object oMaxValue=st.contains("SCORE.maxvalue")?st.getValue("SCORE.maxvalue"):new Integer(1); //Per utilitzar l'algorisme cal que l'indiquin, però...
                Object oMinValue=st.contains("SCORE.minvalue")?st.getValue("SCORE.minvalue"):new Integer(0); //Per utilitzar l'algorisme cal que l'indiquin, però...
                symtab.setVar("SCORE.max",QTISymtab.mult(st.getType("SCORE.max"),oMaxValue,new Integer(k)));
                symtab.setVar("SCORE.min",QTISymtab.mult(st.getType("SCORE.min"),oMinValue,new Integer(k)));
            }
            else{
                symtab.setVar("SCORE.max","0");
                symtab.setVar("SCORE.min","0");
            }
            
            float[] scores=new float[vSelectedObjects.size()];
            int numWithScore=0; //Num de items que contenen la variable SCORE (haurien de ser tots)
            java.util.Enumeration e=vSelectedObjects.elements();
            while (e.hasMoreElements()){ // for ALL_SELECTED(childobjects) do
                QTISymtab st=(QTISymtab)e.nextElement();
                if (st.contains("SCORE")){
                    Object oScore=st.getValue("SCORE");
                    try{
                        float f=new Float(oScore.toString()).floatValue();
                        scores[numWithScore]=-f; //Poso el valor negatiu a l'array perquè utilitzaré Arrays.sort per ordenar i només ordena en ordre creixent
                        numWithScore++;
                    }
                    catch (Exception ex){
                        System.out.println("Error: el valor de la variable SCORE ("+oScore+") no és correcte.");
                    }
                }
            }
            java.util.Arrays.sort(scores,0,numWithScore);
            float score=0;
            for (int i=0;i<numWithScore && i<k;i++){
                //System.out.println("Sumo a SCORE:"+scores[i]);
                score+=(-scores[i]); //Amb el valor negatiu desfaig el canvi fet anteriorment
            }
            symtab.addToVar("SCORE",new Integer((int)score));
            Object op1=QTISymtab.subtract(symtab.getType("SCORE"),symtab.getValue("SCORE"),symtab.getValue("SCORE.min"));
            Object op2=QTISymtab.subtract(symtab.getType("SCORE.max"),symtab.getValue("SCORE.max"),symtab.getValue("SCORE.min"));
            Object result=QTISymtab.div(QTISymtab.DECIMAL_TYPE,op1,op2); //La divisió per 0 provocarà un NaN (not a number)
            symtab.setVar("SCORE.normalized",result);
        }
        else{
            System.out.println("Error: La variable SCORE hauria d'estar declarada a l'outcomes o bé s'hauria de fer un map_output.");
        }
        return symtab;
    }
    
    private static boolean hasAttempted(QTISymtab st,QTIUserResponsesSymtab stUserResponses){
        System.out.println("att");
        if (st.contains(".IDENT") && stUserResponses.hasAttemptedItem(st.getValue(".IDENT").toString())) return true;
        else return false;
    }
    
    private static Object getWeighting(QTISymtab st, boolean isParam){
        String sVarName=isParam?PARAM_PREFIX+"qmd_weighting":"qmd_weighting";
        if (st.contains(sVarName))
            return (st.getValue(sVarName));
        else{
            if (isParam){
                System.out.println("Error: falta definir qmd_weighting a l'objects_parameter.");
                return new Integer(1);
            }
            else{
                System.out.println("Error: falta definir qmd_weighting al metadata de l'item.");
                return new Integer(1);
            }
        }
    }
    
    private static Object getPenaltyValue(QTISymtab stItem,QTISymtab stAlgorithm){
        if (stItem.contains("qmd_penaltyvalue")) return stItem.getValue("qmd_penaltyvalue");
        else{
                /*Segons l'especificació de l'Outcomes (IMS Question & Test Interoperability: ASI Outcomes Processing
                 , Final Specification Version 1.2 plana 113) el qmd_penaltyvalue si apareix ho ha de fer al metadata
                 dels items. De tota manera, a l'exemple l'utilitza com a paràmetre de l'algorisme GuessingParameter.
                 Aquesta manera resulta més cómode de cara a assignar la mateixa penalització a tots els items. Per
                 aquest motiu s'accepta que en el cas que l'item no contigui aquest valor, si l'algorisme el té com
                 a paràmetre es triarà aquest i en cas contrari serà el valor per defecte (1).*/
            if (stAlgorithm.contains("qmd_penaltyvalue")) return stAlgorithm.getValue("qmd_penaltyvalue");
            else return new Integer(1);
        }
    }
    
    private static int getK(QTISymtab symtab,java.util.Vector vSelectedObjects, QTIUserResponsesSymtab stUserResponses){
        if (symtab.contains("K")) return (Integer.parseInt(symtab.getValue("K").toString()));
        else if (symtab.contains("k")) return (Integer.parseInt(symtab.getValue("k").toString()));
        else{ //En el cas que el paràmetre K no estigui definit, pren per valor el nombre d'items que s'ha intentat.
            int iNumAttempted=0;
            java.util.Enumeration e=vSelectedObjects.elements();
            while (e.hasMoreElements()){
                QTISymtab st=(QTISymtab)e.nextElement();
                if (hasAttempted(st,stUserResponses)) iNumAttempted++;
            }
            return iNumAttempted;
        }
    }
}
