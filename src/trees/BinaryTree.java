package trees;

/**
 * BinaryTree.java
 * @author John Zavidniak
 * A Binary Tree data structure
 * @param <T> the type of data stored in the binary tree
 */

public class BinaryTree<T>
{
    
    /**
     * The node class which holds the data
     * of each node in the tree
     */
    private class Node
    {
        
        private T data;
        private Node left;
        private Node right;

        
        /**
         * Create a new node with the given data
         * @param data the data to store in the node
         */
        public Node(T data)
        {
            this.data = data;
            this.left = null;
            this.right = null;
        }
        
    }
    
    private Node root;
    
    /**
     * Create a new empty binary tree
     */
    public BinaryTree()
    {
        this.root = null;
    }
    
    /**
     * Create a new binary tree with the given root
     * @param root the root of this binary tree
     */
    public BinaryTree(Node root)
    {
        this.root = root;
    }
    
    /**
     * Create a new binary tree with the given data and subtrees
     * @param data the data of the root node of this binary tree
     * @param lTree the left subtree of the root node
     * @param rTree the right subtree of the root node
     */
    public BinaryTree(T data, BinaryTree<T> lTree, BinaryTree<T> rTree)
    {
        root = new Node(data);
        root.left = (lTree == null) ? null : lTree.root;
        root.right = (rTree == null) ? null : rTree.root;
    }
    
    /**
     * Get the left subtree of the root node
     * @return a binary tree representing the left
     * subtree of the root node, or null if either
     * the root is null, or the root of the left
     * subtree is null
     */
    public BinaryTree<T> getLeftTree()
    {
        if (root != null && root.left != null)
        {
            return new BinaryTree<T>(root.left);
        }
        return null;
    }
    
    /**
     * Get the right subtree of the root node
     * @return a binary tree representing the right
     * subtree of the root node, or null if either
     * the root is null, or the root of the right
     * subtree is null
     */
    public BinaryTree<T> getRightTree()
    {
        if (root != null && root.right != null)
        {
            return new BinaryTree<T>(root.right);
        }
        return null;
    }
    
    /**
     * Determine whether or not the root of this tree is a leaf node
     * @return true if it is, false if not
     */
    public boolean isLeaf()
    {
        return root == null || (root.left == null && root.right == null);
    }
    
    /**
     * Get the data stored in the root node of this tree
     * @return the data stored in the root node of this tree
     */
    public T getData()
    {
        return root.data;
    }
    
}