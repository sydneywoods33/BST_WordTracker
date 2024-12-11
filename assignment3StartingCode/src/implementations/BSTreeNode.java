package implementations;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BSTreeNode<E> implements Serializable {
    private static final long serialVersionUID = 1L;

    private E value;
    private BSTreeNode<E> left;
    private BSTreeNode<E> right;

    // Metadata: Map<FileName, Set<LineNumbers>>
    private Map<String, Set<Integer>> fileData;

    public BSTreeNode(E value) {
        this.value = value;
        this.left = null;
        this.right = null;
        this.fileData = new HashMap<>();
    }

    // named to match test
    public E getElement() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }

    public BSTreeNode<E> getLeft() {
        return left;
    }

    public void setLeft(BSTreeNode<E> left) {
        this.left = left;
    }

    public BSTreeNode<E> getRight() {
        return right;
    }

    public void setRight(BSTreeNode<E> right) {
        this.right = right;
    }

    public Map<String, Set<Integer>> getFileData() {
        return fileData;
    }

    public void addFileData(String fileName, int lineNumber) {
        fileData.putIfAbsent(fileName, new HashSet<>());
        fileData.get(fileName).add(lineNumber);
    }
}
