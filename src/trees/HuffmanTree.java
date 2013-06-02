package trees;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * HuffmanTree.java
 * @author John Zavidniak
 * A Huffman Tree data structure
 * @param <T> the type of sata stored in the huffman tree
 */

public class HuffmanTree<T>
{
    
    /**
     * The class which holds the data in each leaf node
     * of the huffman tree
     */
    private class Data
    {
        
        private double weight;
        private T value;
        
        /**
         * Create a new Data object with the given weight and value
         * @param weight the weight of the new Data object
         * @param value the value of the new Data object
         */
        public Data(double weight, T value)
        {
            this.weight = weight;
            this.value = value;
        }
        
    }
    
    /**
     * The class used to compare two binary trees with each other
     */
    private class CompareHuffmanTrees implements Comparator<BinaryTree<Data>>
    {

        @Override
        public int compare(BinaryTree<Data> treeA, BinaryTree<Data> treeB)
        {
            double treeAWeight = treeA.getData().weight;
            double treeBWeight = treeB.getData().weight;
            return Double.compare(treeAWeight, treeBWeight);
        }
        
    }
    
    private BinaryTree<Data> tree;
    
    /**
     * Create a new empty huffman tree
     */
    public HuffmanTree()
    {
        this.tree = null;
    }
    
    /**
     * Build a huffman tree with the given data
     * @param data the data to build the huffman tree with
     */
    public void buildTree(T[] data)
    {
        HashMap<T, Integer> freqTable = new HashMap<T, Integer>();
        for (T d : data)
        {
            if (freqTable.containsKey(d))
            {
                freqTable.put(d, freqTable.get(d) + 1);
                continue;
            }
            freqTable.put(d, 1);
        }
        List<Data> dataList = new LinkedList<Data>();
        for(T key : freqTable.keySet())
        {
            System.out.println(key + ": " + freqTable.get(key));
            dataList.add(new Data(freqTable.get(key), key));
        }
        buildTree(dataList);
    }
    
    /**
     * Build a huffman tree with the given data packaged in the Data class
     * @param data the data to build the huffman tree with
     */
    public void buildTree(List<Data> data)
    {
        Queue<BinaryTree<Data>> queue = new PriorityQueue<BinaryTree<Data>>(data.size(), new CompareHuffmanTrees());
        for (Data d : data)
        {
            BinaryTree<Data> bt = new BinaryTree<Data>(d, null, null);
            queue.offer(bt);
        }
        
        while (queue.size() > 1)
        {
            BinaryTree<Data> left = queue.poll();
            BinaryTree<Data> right = queue.poll();
            double weightLeft = left.getData().weight;
            double weightRight = right.getData().weight;
            Data sum = new Data(weightLeft + weightRight, null);
            BinaryTree<Data> sumTree = new BinaryTree<Data>(sum, left, right);
            queue.offer(sumTree);
        }
        
        tree = queue.poll();
    }
    
    /**
     * Use this huffman tree to encode the given value
     * @param tree the tree to use
     * @param bits the bits read up until the current node
     * @param value the value to encode
     * @return a String of bits representing the encoded value
     */
    private String encode(BinaryTree<Data> tree, StringBuilder bits, T value)
    {
        Data rootData = tree.getData();
        if (rootData.value != null)
        {
            if (rootData.value.equals(value))
            {
                return bits.toString();
            }
            else
            {
                return null;
            }
        }
        else
        {
            String result = encode(tree.getLeftTree(), new StringBuilder(bits.toString() + "0"), value);
            if (result == null)
            {
                result = encode(tree.getRightTree(), new StringBuilder(bits.toString() + "1"), value);
            }
            return result;
        }
    }
    
    /**
     * Get the bits representing the encoded values of the data given
     * @param values the values to encode
     * @return a String of bits representing the encoded values
     * of the data given
     */
    public String getBits(T[] values)
    {
        StringBuilder bits = new StringBuilder();
        for (T value : values)
        {
            bits.append(encode(tree, new StringBuilder(), value));
        }
        return bits.toString();
    }
    
    /**
     * Decode the given bits back to the original values
     * @param bits the bits to decode
     * @return a List of the original values
     */
    public List<T> decode(String bits)
    {
        List<T> result = new LinkedList<T>();
        BinaryTree<Data> currentTree = tree;
        for (int i = 0; i < bits.length(); i++)
        {
            if (bits.charAt(i) == '1')
            {
                currentTree = currentTree.getRightTree();
            }
            else
            {
                currentTree = currentTree.getLeftTree();
            }
            if (currentTree.isLeaf())
            {
                Data data = currentTree.getData();
                result.add(data.value);
                currentTree = tree;
            }
        }
        return result;
    }
    
}