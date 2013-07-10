package edu.xtec.qv.applet;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.CompositeShapePainter;
import org.apache.batik.gvt.FillShapePainter;
import org.apache.batik.gvt.GVTTreeWalker;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.ShapeNode;
import org.apache.batik.gvt.ShapePainter;
import org.apache.batik.gvt.StrokeShapePainter;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class SVGUtility{
	
	public static ArrayList loadShapesFromSVG(String sSVG){
		ArrayList shapes = new ArrayList();
		UserAgent      userAgent;
		DocumentLoader loader;
		BridgeContext  ctx;
		GVTBuilder     builder;
		Document doc = null;
		
		try {
			
			/*			String parser = XMLResourceDescriptor.getXMLParserClassName();
			 SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
			 String uri = "file:\\c:\\Albert\\out.svg";
			 doc = f.createDocument(uri);
			 */
			if (sSVG!=null && sSVG.trim().length()>0){
  			StringReader source = new StringReader(sSVG); 
  			String parser = XMLResourceDescriptor.getXMLParserClassName();
  			//System.out.println("parser:"+parser+" svg.length()="+sSVG.length());
  			SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
  			doc = f.createDocument(null,source);
  			/*System.out.println("doc0:"+doc);
  			 source = new StringReader(sSVG);
  			 f = new SAXSVGDocumentFactory(parser);
  			 doc = f.createDocument(null,source);
  			 source = new StringReader(sSVG);
  			 f = new SAXSVGDocumentFactory(parser);
  			 doc = f.createDocument(null,source);
  			 source = new StringReader(sSVG);
  			 f = new SAXSVGDocumentFactory(parser);
  			 doc = f.createDocument(null,source);*/
  			//System.out.println("doc:"+doc);
  		}
		} catch (Exception ex) {
			System.out.println("EXCEPCIO:"+ex.toString());
			ex.printStackTrace();
		}
		
		userAgent = new UserAgentAdapter();
		loader    = new DocumentLoader(userAgent);
		ctx       = new BridgeContext(userAgent, loader);
		ctx.setDynamicState(BridgeContext.DYNAMIC);
		builder   = new GVTBuilder();
		GraphicsNode graphicsNode =  builder.build(ctx, doc);
		GVTTreeWalker treeWalker = new GVTTreeWalker(graphicsNode);
		
		//int i = 0; //
		QVShape lastShape = null;
		while((graphicsNode = treeWalker.nextGraphicsNode()) != null) {
			//i = i + 1; //
			//System.out.println("graphicsNode:"+graphicsNode);
			if (graphicsNode instanceof ShapeNode){	
				Shape s = graphicsNode.getOutline();
				//System.out.println("Shape: "+i); //
				QVShapeAspect aspect = getQVShapeAspectGraphicsNode(graphicsNode);
				// Cal determinar quina és la forma del Shape si no és un GeneralPath
				QVPencil ll = new QVPencil(false); //!isNew
				ll.setShape(s);
				if (aspect!=null){
					//System.out.println("COLOR:"+c);
					ll.setAspect(aspect);
				}
				
				// Si el Shape és idéntic a l'anterior no n'afegim dos iguals, juntem l'aspecte
				if (lastShape!=null && shapeEquals(lastShape.getShape(),s)){
					QVShapeAspect lastAspect = lastShape.getAspect();
					if (lastAspect==null)
						lastAspect=new QVShapeAspect(null, null, null);
					if (aspect!=null){
						if (aspect.getStroke()!=null)
							lastAspect.setStroke(aspect.getStroke());
						if (aspect.getColor()!=null)
							lastAspect.setColor(aspect.getColor());
						if (aspect.getFillColor()!=null)
							lastAspect.setFillColor(aspect.getFillColor());
					}
					lastShape.setAspect(lastAspect);
				} else{
					shapes.add(ll);
				}	
				lastShape=ll;
			}
		}
		//System.out.println("TOTAL SHAPES LOADED: " + shapes.size());
		return shapes;
	}
	
	
	public static String createSVGElement(Paintable p){
		String s = null;
		
		DOMImplementation domImpl =
			GenericDOMImplementation.getDOMImplementation();
		
		// Create an instance of org.w3c.dom.Document
		Document document = domImpl.createDocument(null, "svg", null);
		
		// Create an instance of the SVG Generator
		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
		
		// Ask the test to render into the SVG Graphics2D implementation
		p.paint(svgGenerator, false); //No incloem la imatge de fons
		
		// Finally, stream out SVG to the standard output using UTF-8
		// character to byte encoding
		boolean useCSS = true; // we want to use CSS style attribute
		try{
			//FileOutputStream fw = new FileOutputStream("c:/Albert/out.svg");
			//OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fw, "UTF-8");
			//svgGenerator.stream(outputStreamWriter, useCSS);
			//outputStreamWriter.close();
			
			StringWriter sw = new StringWriter();
			svgGenerator.stream(sw, useCSS);
			sw.flush();
			sw.close();
			
			s= sw.toString();
			//shapes = SVGUtility.loadShapesFromSVG();
			
		}
		catch (Exception e){
			e.printStackTrace(System.out);
		}
		
		return s;
	}
	
	private static QVShapeAspect getQVShapeAspectGraphicsNode(GraphicsNode graphicsNode){
		//System.out.println("class:"+graphicsNode.getClass().getName());
		QVShapeAspect aspect = null;
		if (graphicsNode instanceof ShapeNode){
			ShapeNode sn=(ShapeNode)graphicsNode;
			ShapePainter sp=(ShapePainter)sn.getShapePainter();
			aspect=getQVShapeAspectShapePainter(sp);
		} else if (graphicsNode instanceof CompositeGraphicsNode){
			CompositeGraphicsNode cgn =(CompositeGraphicsNode)graphicsNode;
			for (int i=0;aspect==null && cgn!=null && i<cgn.size();i++){
				Object o=cgn.get(i);
				if (o instanceof GraphicsNode){
					GraphicsNode gn=(GraphicsNode)o;
					aspect=getQVShapeAspectGraphicsNode(gn);
				} 
			}			
			/*if (graphicsNode instanceof CanvasGraphicsNode){
			 Object o=((CanvasGraphicsNode)graphicsNode).getBackgroundPaint();
			 System.out.println("o="+o);
			 }*/
		} 
		return aspect;
	}
	
	private static QVShapeAspect getQVShapeAspectShapePainter(ShapePainter sp){
		QVShapeAspect aspect = null;
		//System.out.println("class:"+sp.getClass().getName());
		if (sp instanceof FillShapePainter){
			Color c = getColorFillShapePainter((FillShapePainter)sp);
			if (c!=null){
				//aspect = new QVShapeAspect(c, null);
				aspect = new QVShapeAspect(null, null, null);
				aspect.setFillColor(c);
			}
		} else if (sp instanceof StrokeShapePainter){
			StrokeShapePainter ssp=(StrokeShapePainter)sp;
			java.awt.Paint p=ssp.paint;
			java.awt.Stroke s=ssp.stroke;
			if (p!=null || s!=null){
				aspect = new QVShapeAspect(null, null, null);
				if (p!=null && p instanceof java.awt.Color)
					aspect.setColor((Color)p);
				if (s!=null && s instanceof java.awt.BasicStroke)
					aspect.setStroke((java.awt.BasicStroke)s);
			}
		} else if (sp instanceof CompositeShapePainter){
			CompositeShapePainter csp=(CompositeShapePainter)sp;
			ShapePainter[] sp2=csp.painters;
			for (int j=0;aspect==null && sp2!=null && j<sp2.length;j++){
				aspect = getQVShapeAspectShapePainter(sp2[j]);
			}
		}
		return aspect;
	}
	
	private static java.awt.Color getColorFillShapePainter(FillShapePainter fsp){
		java.awt.Paint p = fsp.paint;
		//System.out.println("getColor p="+p+" fsp:"+fsp.toString()+" class:"+fsp.getClass().getName());
		if (p instanceof Color)
			return (Color)p;
		else
			return null;
	}
	
	private static boolean shapeEquals(Shape s1, Shape s2){
		/* La classe Shape no té sobrecarregat el métode equals. Aquesta funció
		 retorna true quan dos Shapes són identics */
		boolean bEquals = false;
		if (s1!=null && s2!=null){
			PathIterator pi1 = s1.getPathIterator(null);
			PathIterator pi2 = s2.getPathIterator(null);
			double aD1[] = new double[6];
			double aD2[] = new double[6];
			for(bEquals=true ;bEquals && !pi1.isDone() && !pi2.isDone();){
				Point current = null;
				int i1 = pi1.currentSegment(aD1);
				int i2 = pi2.currentSegment(aD2);
				if (i1==i2 && aD1[0]==aD2[0] && aD1[1]==aD2[1] && aD1[2]==aD2[2] && aD1[3]==aD2[3] && aD1[4]==aD2[4] && aD1[5]==aD2[5])
					bEquals = true;
				else
					bEquals = false;
				pi1.next();
				pi2.next();
			}
			if (pi1.isDone()!=pi2.isDone())
				bEquals=false;
		}
		return bEquals;
	}
}
