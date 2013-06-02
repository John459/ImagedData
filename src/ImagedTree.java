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
            int bVal = Integer.parseInt(bBits, 2);
            int gVal = Integer.parseInt(gBits, 2);
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
                bBits = bits.substring(8);
                return true;
            }
            else if (bits.length() <= 24)
            {
                rBits = bits.substring(0, 8);
                bBits = bits.substring(8, 16);
                gBits = bits.substring(16);
                return true;
            }
            return false;
        }
    }
    
    private List<Pixel> pixels;
    
    /**
     * Create the object with an empty list of pixels
     */
    public ImagedTree()
    {
        pixels = new LinkedList<Pixel>();
    }
    
    /**
     * Create the object with the given tree
     * @param tree the huffman tree to convert to an image
     */
    public ImagedTree(HuffmanTree<T> tree)
    {
        pixels = new LinkedList<Pixel>();
        bitsToPixels(tree.getBits());
    }
    
    /**
     * Convert the given bits to a list of pixels
     * @param bits the bits to convert to a list of pixels
     */
    private void bitsToPixels(String bits)
    {
        Pixel p = new Pixel();
        if (p.write(bits))
        {
            pixels.add(p);
            return;
        }
        pixels.add(p);
        bitsToPixels(bits.substring(24));
    }
    
    /**
     * Convert the list of pixels to a BufferedImage
     * @return the buffered image representing the list of pixels
     */
    public BufferedImage toBufferedImage()
    {
        int width = (int) Math.sqrt(pixels.size());
        int height = (int) Math.ceil(pixels.size()/width);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                if (i + j*i >= pixels.size())
                {
                    bi.setRGB(i, j, 0);
                    continue;
                }
                bi.setRGB(i, j, pixels.get(i + j*i).getRgb());
            }
        }
        return bi;
    }
    
}