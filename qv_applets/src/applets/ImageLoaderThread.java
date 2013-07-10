// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
//import java.awt.Canvas;
import javax.swing.JPanel;
import java.awt.MediaTracker;
import java.io.PrintStream;

public class ImageLoaderThread extends Thread
{

    //public ImageLoaderThread(MediaTracker mediatracker, Canvas canvas1)
    public ImageLoaderThread(MediaTracker mediatracker, JPanel canvas1)
    {
        mTracker = mediatracker;
        canvas = canvas1;
    }

    public void run()
    {
        waitForImages();
        canvas.repaint();
    }

    private void waitForImages()
    {
        try
        {
            if(mTracker != null)
                mTracker.waitForAll();
        }
        catch(Exception exception)
        {
            System.out.println("EXCEPCIO carregant imatges -> " + exception);
        }
    }

    MediaTracker mTracker;
    //Canvas canvas;
    JPanel canvas;
}






