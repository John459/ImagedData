import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import trees.HuffmanTree;

/**
 * ImagedTree.java
 * @author John Zavidniak
 * Converts a Huffman Tree to a list of pixels, which
 * may then be converted to a BufferedImage
 * @param <T> the type of data in the Huffman Tree
 */

public class ImagedTree<T>
{
    
    
    /**
     * The class used for storing the data of each pixel
     */
    private class Pixel
    {
        
        private static final int PADDED_VALUE = 0xFFFFFF;
        
        private String rBits;
        private String bBits;
        private String gBits;
        
        /**
         * Initialie the object with an rgb value of 0
         */
        public Pixel()
        {
            rBits = "00000000";
            bBits = "00000000";
            gBits = "00000000";
        }
        
        /**
         * Convert the three rgb values to a single integer
         * @return the integer representing the rgb value
         */
        public int getRgb()
        {
            int rVal = Integer.parseInt(rBits, 2);
            int gVal = Integer.parseInt(gBits, 2);
            int bVal = Integer.parseInt(bBits, 2);
            return (1 << 24) + (rVal << 16) + (gVal << 8) + bVal;
        }
        
        /**
         * Write the given bits into the rgb values of the pixel
         * @param bits the bits to use
         * @return true if the bits fit into the pixel, false if not
         */
        public boolean write(String bits)
        {
            if (bits.length() <= 8)
            {
                rBits = bits;
                return true;
            }
            else if (bits.length() <= 16)
            {
                rBits = bits.substring(0, 8);
                gBits = bits.substring(8);
                return true;
            }
            else if (bits.length() <= 23)
            {
                rBits = bits.substring(0, 8);
                gBits = bits.substring(8, 16);
                bBits = bits.substring(16);
                return true;
            }
            return false;
        }
    }
    
    private List<Pixel> pixels;
    
    /**
     * Create the object with an empty list of pixels
     */
    public ImagedTree(T[] data)
    {
        pixels = new LinkedList<Pixel>();
    }
    
    /**
     * Create the object with the given tree
     * @param tree the huffman tree to convert to an image
     */
    public ImagedTree(HuffmanTree<T> tree, T[] data)
    {
        pixels = new LinkedList<Pixel>();
        String bits = tree.getBits(data);
        bitsToPixels(bits);
        
    }
    
    /**
     * Convert the given bits to a list of pixels
     * @param bits the bits to convert to a list of pixels
     */
    private void bitsToPixels(String bits)
    {
        Pixel p;
        while (bits.length() > 23)
        {
            p = new Pixel();
            p.write(bits.substring(0, 23));
            pixels.add(p);
            bits = bits.substring(23);
        }
        p = new Pixel();
        p.write(bits);
        pixels.add(p);
    }
    
    /**
     * Convert the list of pixels to a BufferedImage
     * @return the buffered image representing the list of pixels
     */
    public BufferedImage toBufferedImage()
    {
        int width = (int) Math.ceil(Math.sqrt(pixels.size()));
        int height = width;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                if (i*height + j >= pixels.size())
                {
                    bi.setRGB(i, j, Pixel.PADDED_VALUE);
                    continue;
                }
                bi.setRGB(i, j, pixels.get(i*height + j).getRgb());
            }
        }
        return bi;
    }
    
}