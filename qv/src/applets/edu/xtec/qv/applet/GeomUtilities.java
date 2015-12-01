package edu.xtec.qv.applet;

import java.awt.Shape;
import java.awt.Point;
import java.awt.geom.*;
import java.util.ArrayList;

public class GeomUtilities
{
	
	public GeomUtilities(){
	}
	
	public static QVShape mergeShapes(QVShape s1, QVShape s2){
		/* Retorna el QVShape (Pencil) resultant de juntar les dues figures per la/les cantonada/des que tenen en comú */
		QVShape s = null;
		if (s1!=null && s2!=null && s1.canMergeTo(s2)){
			Point[] corners1 = s1.getCorners();
			Point[] corners2 = s2.getCorners();
			GeneralPath gp = null;
			if (corners1[1].equals(corners2[0])){
				//System.out.println("cas 1");
				// s2 darrere de s1
				gp = mergePaths(s1.getShape().getPathIterator(null), s2.getShape().getPathIterator(null));
			} else if (corners2[1].equals(corners1[0])){
				//System.out.println("cas 2");
				// s1 darrere de s2
				gp = mergePaths(s2.getShape().getPathIterator(null), s1.getShape().getPathIterator(null));
			} else if (corners1[0].equals(corners2[0])){
				//System.out.println("cas 3");
				// s1 darrere de la inversa de s2
				gp = mergePaths(getReversePathIterator(s2.getShape().getPathIterator(null)), s1.getShape().getPathIterator(null));
			} else if (corners1[1].equals(corners2[1])){
				//System.out.println("cas 4");
				// la inversa de s2 darrere de s1
				gp = mergePaths(s1.getShape().getPathIterator(null), getReversePathIterator(s2.getShape().getPathIterator(null)));
				//gp = mergePaths(getReversePathIterator(s1.getShape().getPathIterator(null)), s2.getShape().getPathIterator(null));
			} else {
				System.out.println("ERROR!!!!! cap dels casos!!!!");
			}
			
			if (gp!=null){ //Sempre
				s = new QVPencil(false);
				s.setShape(gp);
				s.setAspect(s1.getAspect());
			} else {
				System.out.println("ERROR!!!!! gp=null!!!!");
			}
		}
		return s;
	}
	
	private static GeneralPath mergePaths(PathIterator pi1, PathIterator pi2){
		GeneralPath gp = new GeneralPath();
		gp.append(pi1, false);
		//addPathIteratorToGeneralPath(pi1, gp);
		addPathIteratorToGeneralPath(pi2, gp);
		return gp;
	}
	
	private static void addPathIteratorToGeneralPath(PathIterator pi, GeneralPath gp){
		float aF[] = new float[6];
		for(int linies=0;!pi.isDone(); pi.next()){
			int i = pi.currentSegment(aF);
			switch (i){
				case PathIterator.SEG_MOVETO:
					//Estem juntant 2 figures i només ho fem quan una segueix a l'altre
					if (linies>0)
						gp.moveTo(aF[0],aF[1]);
				break;
				case PathIterator.SEG_LINETO:
					gp.lineTo(aF[0],aF[1]);
				break;
				case PathIterator.SEG_QUADTO:
					gp.quadTo(aF[0],aF[1],aF[2],aF[3]);
				break;
				case PathIterator.SEG_CUBICTO:
					gp.curveTo(aF[0],aF[1],aF[2],aF[3],aF[4],aF[5]);
				break;
			}
			linies++;
		}
	}
	
	private static PathIterator getReversePathIterator(PathIterator pi){
		GeneralPath gp = new GeneralPath();
		gp = getReversePathIterator(gp, pi, -1, -1);
		return gp.getPathIterator(null);
	}
	
	private static GeneralPath getReversePathIterator(GeneralPath gp, PathIterator pi, float x, float y){
		if (!pi.isDone()){
			float aF[] = new float[6];
			int i = pi.currentSegment(aF);
			pi.next();
			switch (i){
				case PathIterator.SEG_MOVETO:
					gp = getReversePathIterator(gp, pi, aF[0], aF[1]);
				//gp.moveTo(aF[0],aF[1]);
				break;
				case PathIterator.SEG_LINETO:
					gp = getReversePathIterator(gp, pi, aF[0], aF[1]);
				gp.lineTo(x, y);
				break;
				case PathIterator.SEG_QUADTO:
					gp = getReversePathIterator(gp, pi, aF[2], aF[3]);
				gp.quadTo(aF[0],aF[1], x, y);
				break;
				case PathIterator.SEG_CUBICTO:
					gp = getReversePathIterator(gp, pi, aF[4], aF[5]);
				gp.curveTo(aF[2],aF[3],aF[0],aF[1], x, y);
				break;
			}
			
		} else {
			gp.moveTo(x, y);
		}
		return gp;
	}
	
	/** Retorna un nou camí amb rectes que uneixen els punts 
	 mes rellevants dins del camí que es passa com a paràmetre. */
	public static GeneralPath simplifica(GeneralPath gpIni){
		//Primer obtenim els punts finals de cada segment de la figura
		ArrayList alPuntsDelPath = getPoints(gpIni);
		
		// Ens quedem amb els punts rellevants
		ArrayList lPuntsSimplif = simplifica(alPuntsDelPath, 2D);
		
		//Generem un nou camí amb rectes que uneixen els punts principals
		GeneralPath gp = new GeneralPath();
		Point2D pInicial = (Point2D)lPuntsSimplif.get(0);
		gp.moveTo((float)pInicial.getX(), (float)pInicial.getY());
		for(int j=1; j<lPuntsSimplif.size()-1; j++){
			Point2D pActual = (Point2D)lPuntsSimplif.get(j);
			gp.lineTo((float)pActual.getX(), (float)pActual.getY());
		}
		// Si el punt final és l'inicial, tanquem la figura
		Point2D pFinal = (Point2D)lPuntsSimplif.get(lPuntsSimplif.size()-1);
		if(pFinal.equals(pInicial))
			gp.closePath();
		else
			gp.lineTo((float)pFinal.getX(), (float)pFinal.getY());
		
		return gp;
	}
	
	/** Retorna els punts finals de cada segment de la figura */
	private static java.util.ArrayList getPoints(GeneralPath gp){
		ArrayList alPuntsDelPath = new ArrayList();
		PathIterator pi = gp.getPathIterator(null);
		float af[] = new float[6];
		for(; !pi.isDone(); pi.next()){
			int i = pi.currentSegment(af);
			if(i == PathIterator.SEG_MOVETO){
				alPuntsDelPath.add(new java.awt.geom.Point2D.Float(af[0], af[1]));
			} else if(i == PathIterator.SEG_LINETO){
				alPuntsDelPath.add(new java.awt.geom.Point2D.Float(af[0], af[1]));
			} else if(i == PathIterator.SEG_QUADTO){
				alPuntsDelPath.add(new java.awt.geom.Point2D.Float(af[2], af[3]));
			} else if(i == PathIterator.SEG_CUBICTO){
				alPuntsDelPath.add(new java.awt.geom.Point2D.Float(af[4], af[5]));
			} else if(i == PathIterator.SEG_CLOSE){
				Point2D primerPunt = (Point2D)alPuntsDelPath.get(0);
				alPuntsDelPath.add(primerPunt.clone());
			}
		}
		return alPuntsDelPath;
	}
	
	public static GeneralPath convertToCurve(GeneralPath gpIni){
		GeneralPath gp = new GeneralPath();
		
		ArrayList alPuntsDelPath = getPoints(gpIni);
		PathIterator pi = gpIni.getPathIterator(null);
		float af[] = new float[6];
		int j = 0;
		for(; !pi.isDone(); pi.next()){
			int i = pi.currentSegment(af);
			if(i == PathIterator.SEG_MOVETO){
				gp.moveTo(af[0], af[1]);
				j++;
			} else if(i == PathIterator.SEG_LINETO){
				if (j+3<alPuntsDelPath.size()){
					gp.curveTo((float)((Point2D.Float)alPuntsDelPath.get(j)).getX(), (float)((Point2D.Float)alPuntsDelPath.get(j)).getY(),
							(float)((Point2D.Float)alPuntsDelPath.get(j+1)).getX(), (float)((Point2D.Float)alPuntsDelPath.get(j+1)).getY(),
							(float)((Point2D.Float)alPuntsDelPath.get(j+2)).getX(), (float)((Point2D.Float)alPuntsDelPath.get(j+2)).getY());
					j+=3;
					pi.next();
					pi.next();
				} else{
					gp.lineTo(af[0], af[1]);
					j++;
				}
			} else if(i == PathIterator.SEG_LINETO){
				if (j+2<alPuntsDelPath.size()){
					gp.quadTo((float)((Point2D.Float)alPuntsDelPath.get(j)).getX(), (float)((Point2D.Float)alPuntsDelPath.get(j)).getY(),
							(float)((Point2D.Float)alPuntsDelPath.get(j+1)).getX(), (float)((Point2D.Float)alPuntsDelPath.get(j+1)).getY());
					j+=2;
					pi.next();
				} else{
					gp.lineTo(af[0], af[1]);
					j++;
				}
			} else if(i == PathIterator.SEG_CLOSE){
				gp.closePath();
			}
		}
		return gp;
	}
	
	private static java.util.ArrayList simplifica(java.util.List lPunts, double distanciaAdmesa){
		java.util.ArrayList lPuntsSimplif = new java.util.ArrayList();
		boolean aInclouPunts[] = new boolean[lPunts.size()];
		for(int k = 0; k < aInclouPunts.length; k++)
			aInclouPunts[k] = false;
		
		aInclouPunts[0] = true;
		aInclouPunts[lPunts.size() - 1] = true;
		simplificaProx(lPunts, 0, lPunts.size() - 1, aInclouPunts, distanciaAdmesa*distanciaAdmesa);
		for(int i=0; i<lPunts.size(); i++){
			if(aInclouPunts[i])
				lPuntsSimplif.add(lPunts.get(i));
		}
		return lPuntsSimplif;
	}
	
	private static void simplificaProx(java.util.List lPunts, int inicial, int darrer, boolean aInclouPunts[], double distanciaAdmesa){
		if(darrer>(inicial+1)){
			double distanciaReal = 0.0D;
			Point2D pInicial = (Point2D)lPunts.get(inicial);
			Point2D pFinal = (Point2D)lPunts.get(darrer);
			Point2D direccio = direccio(pInicial, pFinal);
			// L'arrel quadrada del producte escalar d'un vector per si mateix és el mòdul
			double distanciaTotal = prodEscalar(direccio, direccio);
			int millorPunt = inicial;
			for(int actual=inicial+1; actual<darrer; actual++){
				Point2D pActual = (Point2D)lPunts.get(actual);
				Point2D direccioActual = direccio(pInicial, pActual);
				// El producte escalar de dos vectors és igual al mòdul d'un d'ells per la projecció
				// de l'altre sobre el primer
				double esc = prodEscalar(direccioActual, direccio);
				double distanciaActual;
				if(esc <= 0.0D) //Direccions contràries
					distanciaActual = pActual.distanceSq(pInicial);
				else if(esc > distanciaTotal){
					distanciaActual = pActual.distanceSq(pFinal);
				} else{
					double modul=(distanciaTotal!=0.0D)?(esc/distanciaTotal):0.0D;
					distanciaActual = pActual.distanceSq(pInicial.getX()+modul*direccio.getX(), pInicial.getY()+modul*direccio.getY());
				}
				if(distanciaActual > distanciaReal){
					millorPunt = actual;
					distanciaReal = distanciaActual;
				}
			}
			if(distanciaReal > distanciaAdmesa){
				aInclouPunts[millorPunt] = true;
				simplificaProx(lPunts, inicial, millorPunt, aInclouPunts, distanciaAdmesa);
				simplificaProx(lPunts, millorPunt, darrer, aInclouPunts, distanciaAdmesa);
			}
		}
	}
	
	private static double prodEscalar(Point2D p1, Point2D p2){
		double d = p1.getX() * p2.getX() + p1.getY() * p2.getY();
		return d;
	}
	
	private static Point2D direccio(Point2D p1, Point2D p2){
		/**/
		double dx = p2.getX() - p1.getX();
		double dy = p2.getY() - p1.getY();
		return new java.awt.geom.Point2D.Double(dx, dy);
	}
	
}
