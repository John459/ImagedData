import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.imageio.ImageIO;

import trees.HuffmanTree;



public class ImagedData<T>
{
    
    private HuffmanTree<T> hTree;
    private T[] data;
    
    /**
     * Create a new ImagedData object with the given array of data
     * @param data the data to use with this object
     */
    public ImagedData(T[] data)
    {
        hTree = new HuffmanTree<T>();
        hTree.buildTree(data);
        this.data = data;
    }
    
    /**
     * Formats the bits so that they may be properly processed
     * @param bits the bits to format
     * @param maxLen the maximum length the bit String is allowed to be
     * @return the newly formatted bit String
     */
    private String formatBits(String bits, int maxLen)
    {
        if (bits.equals("00000000"))
        {
            return "";
        }
        if (bits.equals("0"))
        {
            return "0";
        }
        while (bits.length() < maxLen)
        {
            bits = "0" + bits;
        }
        return bits.substring(0, maxLen);
    }
    
    /**
     * Converts a hex value to the bits representing an rgb value
     * @param hex the hex value to convert
     * @return a String of bits representing the rgb value
     */
    private String hexToRgb(int hex)
    {
        int r = (hex >> 16) & 255;
        int g = (hex >> 8) & 255;
        int b = hex & 255;
        
        if (r == 255 && g == 255 && b == 255)
        {
            return "";
        }
        
        String rBits = Integer.toBinaryString(r);
        rBits = formatBits(rBits, 8);
        if (rBits.equals("0"))
        {
            return rBits;
        }
        
        String gBits = Integer.toBinaryString(g);
        gBits = formatBits(gBits, 8);
        if (gBits.equals("0"))
        {
            return rBits + gBits;
        }
        
        String bBits = Integer.toBinaryString(b);
        bBits = formatBits(bBits, 7);
        
        return rBits + gBits + bBits;
    }
    
    /**
     * Decompress data from the png image located at the given path
     * @param path the path to the png image
     * @return a list containing the decompressed data
     */
    public List<T> decompressFromPNG(String path)
    {
        StringBuilder bits = new StringBuilder();
        File image = new File(path);
        BufferedImage bi;
        try
        {
            bi = ImageIO.read(image);
        }
        catch (IOException e)
        {
            System.out.println("Failed to read image");
            return null;
        }
        for (int i = 0; i < bi.getWidth(); i++)
        {
            for (int j = 0; j < bi.getHeight(); j++)
            {
                bits.append(hexToRgb(bi.getRGB(i, j)));
            }
        }
        System.out.println("decoded: " + bits);
        List<T> result = hTree.decode(bits.toString());
        return result;
    }
    
    /**
     * Compress the data to a png image that will be saved to the
     * given path
     * @param path the path to save the png image to
     */
    public void compressToPNG(String path)
    {
        ImagedTree<T> it = new ImagedTree<T>(hTree, data);
        BufferedImage bi = it.toBufferedImage();
        File image = new File(path);
        try
        {
            ImageIO.write(bi, "png", image);
        }
        catch (IOException e)
        {
            System.out.println("Failed to make image");
            return;
        }
    }
    
    /**
     * Convert a String to an array of Characters
     * @param s the String to convert
     * @return an array of Characters representing the String
     */
    public static Character[] stringToCharacterArray(String s)
    {
        Character[] c = new Character[s.length()];
        for (int i = 0; i < s.length(); i++)
        {
            c[i] = s.charAt(i);
        }
        return c;
    }
    
    /**
     * Read characters from a file into a String
     * @param file the file to read
     * @return a String containing all the characters from the file
     * @throws IOException if the file cannot be found or read
     */
    private static String getChars(String file) throws IOException
    {
        StringBuilder result = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String str;
        while ((str = in.readLine()) != null)
        {
            result.append(str + "\n");
        }
        in.close();
        return result.toString();
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        Character[] chars;
        try
        {
            chars = stringToCharacterArray(getChars("words"));
        }
        catch (IOException e)
        {
            return;
        }
        
        ImagedData<Character> id = new ImagedData<Character>(chars);
        id.compressToPNG("image.png");
        StringBuilder s = new StringBuilder();
        for (Character c : id.decompressFromPNG("image.png"))
        {
            s.append(c);
        }
        System.out.println(s);
    }
    
}