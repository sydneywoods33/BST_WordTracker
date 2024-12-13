package implementations;

import utilities.BSTreeADT;
import utilities.Iterator;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;

/**
 * A binary search tree implementation of the BSTreeADT interface.
 *
 * @param <E> the type of elements maintained by this tree
 */
@SuppressWarnings("serial")
public class BSTree<E extends Comparable<E>> implements BSTreeADT<E>
{
	private BSTreeNode<E> root;
	private int size;

	/**
	 * Constructs an empty binary search tree.
	 */
	public BSTree()
	{
		root = null;
		size = 0;
	}

	/**
	 * Returns the root of the tree.
	 *
	 * @return the root of the tree
	 * @throws NullPointerException if the tree is empty
	 */
	@Override
	public BSTreeNode<E> getRoot() throws NullPointerException
	{
		if (root == null)
		{
			throw new NullPointerException("Tree is empty.");
		}
		return root;
	}

	/**
	 * Returns the height of the tree.
	 *
	 * @return the height of the tree
	 */
	@Override
	public int getHeight()
	{
		return calculateHeight(root);
	}

	private int calculateHeight(BSTreeNode<E> node)
	{
		if (node == null)
		{
			return 0; // An empty tree has height 0
		}
		return 1 + Math.max(calculateHeight(node.getLeft()), calculateHeight(node.getRight()));
	}

	/**
	 * Returns the number of elements in the tree.
	 *
	 * @return the number of elements in the tree
	 */
	@Override
	public int size()
	{
		return size;
	}

	/**
	 * Returns true if the tree contains no elements.
	 *
	 * @return true if the tree contains no elements
	 */
	@Override
	public boolean isEmpty()
	{
		return size == 0;
	}

	/**
	 * Removes all of the elements from the tree.
	 */
	@Override
	public void clear()
	{
		root = null;
		size = 0;
	}

	/**
	 * Searches for the specified element in the tree.
	 *
	 * @param entry the element to search for
	 * @return the node containing the element, or null if not found
	 * @throws NullPointerException if the specified element is null
	 */
	@Override
	public BSTreeNode<E> search(E entry) throws NullPointerException
	{
		if (entry == null)
		{
			throw new NullPointerException("Cannot search for null.");
		}
		return searchRec(root, entry);
	}

	private BSTreeNode<E> searchRec(BSTreeNode<E> node, E entry)
	{
		if (node == null || entry.equals(node.getElement()))
		{
			return node;
		}
		if (entry.compareTo(node.getElement()) < 0)
		{
			return searchRec(node.getLeft(), entry);
		}
		return searchRec(node.getRight(), entry);
	}

	/**
	 * Adds the specified element to the tree.
	 *
	 * @param newEntry the element to add
	 * @return true if the element was added, false if it was already present
	 * @throws NullPointerException if the specified element is null
	 */
	@Override
	public boolean add(E newEntry) throws NullPointerException
	{
		if (newEntry == null)
		{
			throw new NullPointerException("Cannot add null.");
		}
		if (root == null)
		{
			root = new BSTreeNode<>(newEntry); // Set root if tree is empty
			size++;
			return true;
		}
		if (contains(newEntry))
		{
			return false; // Duplicates are not allowed
		}
		root = insertRec(root, newEntry);
		size++;
		return true;
	}

	private BSTreeNode<E> insertRec(BSTreeNode<E> node, E value)
	{
		if (node == null)
		{
			return new BSTreeNode<>(value);
		}
		if (value.compareTo(node.getElement()) < 0)
		{
			node.setLeft(insertRec(node.getLeft(), value));
		} else if (value.compareTo(node.getElement()) > 0)
		{
			node.setRight(insertRec(node.getRight(), value));
		}
		return node;
	}

	/**
	 * Removes and returns the smallest element in the tree.
	 *
	 * @return the node containing the smallest element, or null if the tree is empty
	 */
	@Override
	public BSTreeNode<E> removeMin()
	{
		if (isEmpty())
		{
			return null;
		}
		BSTreeNode<E> minNode = getMin(root);
		root = removeMinRec(root);
		size--;
		return minNode;
	}

	private BSTreeNode<E> getMin(BSTreeNode<E> node)
	{
		while (node.getLeft() != null)
		{
			node = node.getLeft();
		}
		return node;
	}

	private BSTreeNode<E> removeMinRec(BSTreeNode<E> node)
	{
		if (node.getLeft() == null)
		{
			return node.getRight();
		}
		node.setLeft(removeMinRec(node.getLeft()));
		return node;
	}

	/**
	 * Removes and returns the largest element in the tree.
	 *
	 * @return the node containing the largest element, or null if the tree is empty
	 */
	@Override
	public BSTreeNode<E> removeMax()
	{
		if (isEmpty())
		{
			return null;
		}
		BSTreeNode<E> maxNode = getMax(root);
		root = removeMaxRec(root);
		size--;
		return maxNode;
	}

	private BSTreeNode<E> getMax(BSTreeNode<E> node)
	{
		while (node.getRight() != null)
		{
			node = node.getRight();
		}
		return node;
	}

	private BSTreeNode<E> removeMaxRec(BSTreeNode<E> node)
	{
		if (node.getRight() == null)
		{
			return node.getLeft();
		}
		node.setRight(removeMaxRec(node.getRight()));
		return node;
	}

	/**
	 * Returns an iterator over the elements in the tree in inorder sequence.
	 *
	 * @return an iterator over the elements in the tree in inorder sequence
	 */
	@Override
	public Iterator<E> inorderIterator()
	{
		return new Iterator<E>() {
			private Stack<BSTreeNode<E>> stack = new Stack<>();
			private BSTreeNode<E> current = root;

			{
				pushLeft(current);
			}

			private void pushLeft(BSTreeNode<E> node)
			{
				while (node != null)
				{
					stack.push(node);
					node = node.getLeft();
				}
			}

			@Override
			public boolean hasNext()
			{
				return !stack.isEmpty();
			}

			@Override
			public E next()
			{
				if (!hasNext())
				{
					throw new NoSuchElementException();
				}
				BSTreeNode<E> node = stack.pop();
				E result = node.getElement();
				pushLeft(node.getRight());
				return result;
			}
		};
	}

	/**
	 * Returns an iterator over the elements in the tree in preorder sequence.
	 *
	 * @return an iterator over the elements in the tree in preorder sequence
	 */
	@Override
	public Iterator<E> preorderIterator()
	{
		return new Iterator<E>() {
			private Stack<BSTreeNode<E>> stack = new Stack<>();

			{
				if (root != null)
					stack.push(root);
			}

			@Override
			public boolean hasNext()
			{
				return !stack.isEmpty();
			}

			@Override
			public E next()
			{
				if (!hasNext())
				{
					throw new NoSuchElementException();
				}
				BSTreeNode<E> node = stack.pop();
				E result = node.getElement();
				if (node.getRight() != null)
				{
					stack.push(node.getRight());
				}
				if (node.getLeft() != null)
				{
					stack.push(node.getLeft());
				}
				return result;
			}
		};
	}

	/**
	 * Returns an iterator over the elements in the tree in postorder sequence.
	 *
	 * @return an iterator over the elements in the tree in postorder sequence
	 */
	@Override
	public Iterator<E> postorderIterator()
	{
		return new Iterator<E>() {
			private Stack<BSTreeNode<E>> stack1 = new Stack<>();
			private Stack<BSTreeNode<E>> stack2 = new Stack<>();

			{
				if (root != null)
				{
					stack1.push(root);
					while (!stack1.isEmpty())
					{
						BSTreeNode<E> node = stack1.pop();
						stack2.push(node);
						if (node.getLeft() != null)
						{
							stack1.push(node.getLeft());
						}
						if (node.getRight() != null)
						{
							stack1.push(node.getRight());
						}
					}
				}
			}

			@Override
			public boolean hasNext()
			{
				return !stack2.isEmpty();
			}

			@Override
			public E next()
			{
				if (!hasNext())
				{
					throw new NoSuchElementException();
				}
				return stack2.pop().getElement();
			}
		};
	}

	/**
	 * Returns true if the tree contains the specified element.
	 *
	 * @param entry the element to search for
	 * @return true if the tree contains the specified element
	 * @throws NullPointerException if the specified element is null
	 */
	@Override
	public boolean contains(E entry) throws NullPointerException
	{
		if (entry == null)
		{
			throw new NullPointerException("Cannot search for null.");
		}
		return search(entry) != null;
	}
}
