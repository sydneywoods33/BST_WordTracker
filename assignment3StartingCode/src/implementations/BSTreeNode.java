package implementations;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A node in a binary search tree (BST) that stores a value and metadata about file occurrences.
 *
 * @param <E> the type of elements held in this node
 */
public class BSTreeNode<E> implements Serializable {
    private static final long serialVersionUID = 1L;

    private E value;
    private BSTreeNode<E> left;
    private BSTreeNode<E> right;

    // Metadata: Map<FileName, Set<LineNumbers>>
    private Map<String, Set<Integer>> fileData;

    /**
     * Constructs a new BSTreeNode with the specified value.
     *
     * @param value the value to be stored in this node
     */
    public BSTreeNode(E value) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.fileData = new HashMap<>();
    }

    /**
     * Returns the value stored in this node.
     *
     * @return the value stored in this node
     */
    public E getElement() {
        return value;
    }

    /**
     * Sets the value stored in this node.
     *
     * @param value the value to be stored in this node
     */
    public void setValue(E value) {
        this.value = value;
    }

    /**
     * Returns the left child of this node.
     *
     * @return the left child of this node
     */
    public BSTreeNode<E> getLeft() {
        return left;
    }

    /**
     * Sets the left child of this node.
     *
     * @param left the node to be set as the left child
     */
    public void setLeft(BSTreeNode<E> left) {
        this.left = left;
    }

    /**
     * Returns the right child of this node.
     *
     * @return the right child of this node
     */
    public BSTreeNode<E> getRight() {
        return right;
    }

    /**
     * Sets the right child of this node.
     *
     * @param right the node to be set as the right child
     */
    public void setRight(BSTreeNode<E> right) {
        this.right = right;
    }

    /**
     * Returns the metadata about file occurrences.
     *
     * @return the metadata about file occurrences
     */
    public Map<String, Set<Integer>> getFileData() {
        return fileData;
    }

    /**
     * Adds a file name and line number to the metadata.
     *
     * @param fileName   the name of the file
     * @param lineNumber the line number in the file
     */
    public void addFileData(String fileName, int lineNumber) {
        fileData.putIfAbsent(fileName, new HashSet<>());
        fileData.get(fileName).add(lineNumber);
    }
}
