import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


//package applets;

public class ImageLoader extends Thread{
    
    MediaTracker tracker;
    java.awt.Image i;
    ImageObserver io;
    String imgURL;
    public boolean finish=false;
    Image oImage;
    
    public ImageLoader(java.awt.Image i, ImageObserver io){
        this.i=i;
        this.io=io;
		System.out.println("Creada instancia ImageLoader");
    }
    
	public ImageLoader(java.awt.Image i, ImageObserver io, String imgURL){
		this(i, io);
		this.imgURL = imgURL;
		System.out.println("Creada instancia ImageLoader");
	}
    
    public void run(){
        System.out.println("Intentant carregar imatge '"+imgURL);
        while ((i==null || i.getWidth(null)<=0) && !finish){
            try{
                //if (i.getWidth(null)<=0) sleep(50);                
				if (i.getWidth(null)<=0) Thread.currentThread().sleep(50);
            }
            catch (Exception e){
                System.out.println("Excepció en carregar imatge ('"+imgURL+"')--> "+e);
            }
        }
		System.out.println("Imatge CARREGADA ('"+imgURL+"')");
        //io.imageLoaded();
    }
    
/*	public void run(){
		try{
			System.out.println("Intentant carregar imatge '"+imgURL+"'");
			i = prepareAndGetImage(imgURL);
			System.out.println("Imatge carregada ('"+imgURL+"')");
		}
		catch (Exception e){
			System.out.println("Excepció en carregar imatge ('"+imgURL+"')--> "+e);
		}
		//io.imageLoaded();
	}
    */
    
	private Image prepareAndGetImage(String sImgURL) throws Exception{
		Image img=getImage();
		if(img==null){
			if(prepareImage(sImgURL))
				img=getImage();
		}
		return img;        
	}
    
	public boolean prepareImage(String sImgURL) throws Exception{
		boolean result=false;
		if(oImage==null){
			setImage(getImageFile(sImgURL));
		}
		System.out.println("Imatge preparada '"+sImgURL+"' -> oImage="+oImage);
		result=true;
		return result;
	}
    
	private static int imgReadyFlag=(ImageObserver.WIDTH | ImageObserver.HEIGHT);
	private Image getImage() throws Exception{
		int imgStatus=0;
		if(oImage==null){
			return null;
		}
		while(true){
			imgStatus=Toolkit.getDefaultToolkit().checkImage(oImage, -1, -1, null);
			System.out.println("imgStatus="+imgStatus+"  imgReady="+imgReadyFlag);
			if((imgStatus & (ImageObserver.ERROR | ImageObserver.ABORT))!=0){
				oImage=null;
				break;
			}
			else 
				break;
			//else if((imgStatus & imgReadyFlag)==imgReadyFlag)
			//	break;
			//Thread.currentThread().sleep(50);
		}
        return oImage;
	}
	
	private void setImage(Image oImage){
		this.oImage = oImage;
	}
    
	private Image getImageFile(String sImgURL) throws Exception {
		return Toolkit.getDefaultToolkit().createImage(getBytes(sImgURL));
	}
    

	private byte[] getBytes(String fileName) throws IOException {
		byte[] bFile = null;
		URL url = new URL(fileName);
		InputStream in = url.openStream();
		return inputStreamToByteArray(in, 0);
	}
    
	private static byte[] inputStreamToByteArray(InputStream in, int bufSize) throws IOException {
	   ByteArrayOutputStream baos = null;
	   if (bufSize == 0) {
		 baos = new ByteArrayOutputStream();
	   }
	   else {
		 baos = new ByteArrayOutputStream(bufSize);
	   }
	   final int BUF_SIZE = 4096;
	   byte[] buf = new byte[BUF_SIZE];
	   int bytesRead = -1;
	   while ((bytesRead = in.read(buf, 0, BUF_SIZE)) > -1) {
		 baos.write(buf, 0, bytesRead);
	   }
	   byte[] allBytes = baos.toByteArray();
	   return allBytes;
	 }    
}
