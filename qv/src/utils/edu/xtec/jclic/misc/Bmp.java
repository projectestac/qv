/*
 * Bmp.java
 *
 * Created on 16 de octubre de 2001, 9:58
 */

/**
 * Adapted from:
 *
 * BMP - Wrapper Class  for the loading & saving of uncompressed BMP files.
 *
 * Known problems/issues:
 *     Only 24 bit BMP files are output, images get converted to 24-bit
 *     which corresponds to Java's default colour model, output from
 *     PixelGrabber in Java 1.1.x.
 *
 * see PCBinaryInputStream, PCBinaryOutputStream
 *
 * author Richard J.Osbaldeston
 * version 1.1 02/08/98
 * copyright Richard J.Osbaldeston (http://www.osbald.co.uk)
 * (http://home.freeuk.com/osbald/)
 */


package edu.xtec.jclic.misc;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Francesc Busquets (fbusquets@pie.xtec.es)
 * @version 1.0
 */
public class Bmp {
    
    private java.awt.Image image;
    private BmpFileheader bmp_fileheader = new BmpFileheader();
    private BmpInfoHeader bmp_infoheader = new BmpInfoHeader();
    private BmpPalette bmp_palette;
    private int width;
    private int height;
    
    public Bmp(InputStream is) throws Exception{
        read(new DataInputStream(new BufferedInputStream(is)));
    }
    
    public Bmp(BufferedInputStream is) throws Exception{
        read(new DataInputStream(is));
    }
    
    public Bmp(DataInputStream is) throws Exception{
        read(is);
    }
    
    public Image getImage(){
        return image;
    }
    
    void read(DataInputStream is) throws Exception{
        int coloursUsed = 0;
        int scanlineSize = 0;
        int bitplaneSize = 0;
        byte [] rawData = null;
        
        bmp_fileheader.read(is);
        bmp_infoheader.read(is);
        
        if (bmp_infoheader.biClrUsed != 0)
            coloursUsed = bmp_infoheader.biClrUsed;
        else if (bmp_infoheader.biBitCount < 16)
            coloursUsed = 1 << bmp_infoheader.biBitCount;
        
        bmp_palette = new BmpPalette(coloursUsed);
        bmp_palette.read(is);
        
        long skip =  bmp_fileheader.bfOffBits -
        (bmp_fileheader.getSize()+
        bmp_infoheader.getSize()+
        bmp_palette.getSize());
        
        if (skip > 0)
            is.skip(skip);
        
        scanlineSize = ((bmp_infoheader.biWidth*bmp_infoheader.biBitCount+31)/32)*4;
        
        if (bmp_infoheader.biSizeImage != 0)
            bitplaneSize = bmp_infoheader.biSizeImage;
        else
            bitplaneSize = scanlineSize * bmp_infoheader.biHeight;
        
        rawData = new byte[bitplaneSize];
        readBytePCArray(is, rawData);
        is.close();
        
        
        if (rawData != null) {
            if (bmp_infoheader.biBitCount > 8)
                image = unpack24(rawData, scanlineSize);
            else
                image = unpack08(rawData, scanlineSize);
        }
        rawData = null;
    }
    
    public int readIntPC(DataInputStream is) throws IOException{
        int i=is.readInt();
        return ((i<<24)|((i&0x0000FF00)<<8)|((i&0x00FF0000)>>>8)|(i>>>24));
    }
    
    public short readShortPC(DataInputStream is) throws IOException{
        int i=is.readUnsignedShort();
        return (short)((i<<8)|(i>>>8));
    }
    
    public byte readBytePC(DataInputStream is) throws IOException{
        return (byte)is.readUnsignedByte();
    }
    
    public void readBytePCArray(DataInputStream is, byte b[]) throws IOException{
        is.readFully(b);
    }
        
    Image unpack24(byte [] rawData, int scanlineSize) throws Exception{
        int b=0, k=0, x=0, y=0;
        int [] data = new int[bmp_infoheader.biWidth * bmp_infoheader.biHeight];
        for (y=0; y < bmp_infoheader.biHeight; y++) {
            b=(bmp_infoheader.biHeight-1-y)*bmp_infoheader.biWidth;
            k=y*scanlineSize;
            for (x=0; x < bmp_infoheader.biWidth; x++) {
                data[x+b] = 0xFF000000 |
                (((int)(rawData[k++])) & 0xFF) |
                (((int)(rawData[k++])) & 0xFF) << 8 |
                (((int)(rawData[k++])) & 0xFF) << 16;
            }
        }
        return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(bmp_infoheader.biWidth, bmp_infoheader.biHeight, ColorModel.getRGBdefault(), data, 0, bmp_infoheader.biWidth));
    }
    
    Image unpack08(byte [] rawData, int scanlineSize) throws Exception {
        int b=0, k=0, i=0, x=0, y=0;
        byte [] data = new byte[bmp_infoheader.biWidth * bmp_infoheader.biHeight];
        if (bmp_infoheader.biBitCount == 1) {
            for (y=0; y < bmp_infoheader.biHeight; y++) {
                b=(bmp_infoheader.biHeight-1-y)*bmp_infoheader.biWidth;
                k=y*scanlineSize;
                for (x=0; x < (bmp_infoheader.biWidth-8); x+=8) {
                    data[b+x+7] = (byte)((rawData[k]) & 1);
                    data[b+x+6] = (byte)((rawData[k] >>> 1) & 1);
                    data[b+x+5] = (byte)((rawData[k] >>> 2) & 1);
                    data[b+x+4] = (byte)((rawData[k] >>> 3) & 1);
                    data[b+x+3] = (byte)((rawData[k] >>> 4) & 1);
                    data[b+x+2] = (byte)((rawData[k] >>> 5) & 1);
                    data[b+x+1] = (byte)((rawData[k] >>> 6) & 1);
                    data[b+x]   = (byte)((rawData[k] >>> 7) & 1);
                    k++;
                }
                for (i=7; i>=0 ; i--) {
                    if ((i+x)< bmp_infoheader.biWidth) {
                        data[b+x+i] = (byte)((rawData[k] >>> (7-i)) & 1);
                    }
                }
            }
        } else if (bmp_infoheader.biBitCount == 4) {
            for (y=0; y < bmp_infoheader.biHeight; y++) {
                b=(bmp_infoheader.biHeight-1-y)*bmp_infoheader.biWidth;
                k=y*scanlineSize;
                for (x=0; x < (bmp_infoheader.biWidth-2); x+=2) {
                    data[b+x]   = (byte)((rawData[k]>>4) & 0x0F);
                    data[b+x+1] = (byte)((rawData[k] & 0x0F));
                    k+=1;
                }
                for (i=1; i>=0 ; i--) {
                    if ((i+x)< bmp_infoheader.biWidth) {
                        data[b+x+i] = (byte)((rawData[k] >>> ((1-i)<<2)) & 0x0F);
                    }
                }
            }
        } else {
            for (y=0; y < bmp_infoheader.biHeight; y++) {
                b=(bmp_infoheader.biHeight-1-y)*bmp_infoheader.biWidth;
                k=y*scanlineSize;
                for (x=0; x < bmp_infoheader.biWidth; x++) {
                    data[x+b] = (byte)(rawData[k++] & 0xFF);
                }
            }
        }
        ColorModel colourModel = new IndexColorModel(bmp_infoheader.biBitCount, bmp_palette.length, bmp_palette.r, bmp_palette.g, bmp_palette.b);
        return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(bmp_infoheader.biWidth, bmp_infoheader.biHeight, colourModel, data, 0, bmp_infoheader.biWidth));
    }
    
    /**
     * .BMP InfoHeader
     */
    
    class BmpInfoHeader {
        int   biSize = 40;                  /* InfoHeader Offset*/
        int   biWidth;                      /* Width */
        int   biHeight;                     /* Height */
        short biPlanes = 1;                 /* BitPlanes on Target Device */
        short biBitCount;                   /* Bits per Pixel */
        int   biCompression;                /* Bitmap Compression */
        int   biSizeImage;                  /* Bitmap Image Size */
        int   biXPelsPerMeter;              /* Horiz Pixels Per Meter */
        int   biYPelsPerMeter;              /* Vert Pixels Per Meter */
        int   biClrUsed;                    /* Number of ColorMap Entries */
        int   biClrImportant;               /* Number of important colours */
        
        int getSize() {
            return biSize;
        }
        
        void read(DataInputStream is) throws Exception{
            biSize = readIntPC(is);
            if (biSize == 12) {
                biWidth = readShortPC(is);
                biHeight = readShortPC(is);
                biPlanes = readShortPC(is);
                biBitCount = readShortPC(is);
            }
            else {
                biWidth = readIntPC(is);
                biHeight = readIntPC(is);
                biPlanes = readShortPC(is);
                biBitCount = readShortPC(is);
                biCompression = readIntPC(is);
                biSizeImage = readIntPC(is);
                biXPelsPerMeter = readIntPC(is);
                biYPelsPerMeter = readIntPC(is);
                biClrUsed = readIntPC(is);
                biClrImportant = readIntPC(is);
            }
            
            if (biSizeImage == 0)
                biSizeImage = (((biWidth*biBitCount+31)>>5)<<2)*biHeight;
            
            if (biClrUsed == 0 && biBitCount < 16)
                biClrUsed = 1 << biBitCount;
        }
        
    }
    
    /**
     * .BMP FileHeader
     */
    
    class BmpFileheader {
        byte    bfType[] = {'B','M'};       /* Type */
        int     bfSize;                     /* File Size */
        short   bfReserved1=0;              /* Reserved 1 */
        short   bfReserved2=0;              /* Reserved 2 */
        int     bfOffBits;                  /* Offset to Data */
        
        int getSize() {
            return 14;
        }
        
        void read(DataInputStream is) throws Exception{
            bfType[0] = readBytePC(is);
            bfType[1] = readBytePC(is);
            if (bfType[0] != 'B' && bfType[1] != 'M')
                throw new IOException("Invalid BMP 3.0 File.");
            bfSize = readIntPC(is);
            bfReserved1 = readShortPC(is);
            bfReserved2 = readShortPC(is);
            bfOffBits = readIntPC(is);
        }        
    }
    
    /**
     * .BMP Palette
     */
    
    class BmpPalette {
        int length;
        byte r[];
        byte g[];
        byte b[];
        
        public BmpPalette(int length) {
            this.length = length;
            r = new byte[length];
            g = new byte[length];
            b = new byte[length];
        }
        
        int getSize() {
            return length*4;
        }
        
        void read(DataInputStream is) throws Exception{
            if (length > 0) {
                byte reserved;
                for (int i=0; i < length; i++) {
                    b[i] = readBytePC(is);     // blue
                    g[i] = readBytePC(is);     // green
                    r[i] = readBytePC(is);     // red
                    reserved = readBytePC(is); // reserved
                }
            }
        }
    }
}
