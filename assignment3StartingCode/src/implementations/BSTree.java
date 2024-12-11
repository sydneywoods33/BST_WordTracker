package implementations;

import utilities.BSTreeADT;
import utilities.Iterator;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;

@SuppressWarnings("serial")
public class BSTree<E extends Comparable<E>> implements BSTreeADT<E>
{
	private BSTreeNode<E> root;
	private int size;

	public BSTree()
	{
		root = null;
		size = 0;
	}

	@Override
	public BSTreeNode<E> getRoot() throws NullPointerException
	{
		if (root == null)
		{
			throw new NullPointerException("Tree is empty.");
		}
		return root;
	}

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

	@Override
	public int size()
	{
		return size;
	}

	@Override
	public boolean isEmpty()
	{
		return size == 0;
	}

	@Override
	public void clear()
	{
		root = null;
		size = 0;
	}

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
		if (node == null || entry.equals(node.getValue()))
		{
			return node;
		}
		if (entry.compareTo(node.getValue()) < 0)
		{
			return searchRec(node.getLeft(), entry);
		}
		return searchRec(node.getRight(), entry);
	}

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
		if (value.compareTo(node.getValue()) < 0)
		{
			node.setLeft(insertRec(node.getLeft(), value));
		} else if (value.compareTo(node.getValue()) > 0)
		{
			node.setRight(insertRec(node.getRight(), value));
		}
		return node;
	}

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
				E result = node.getValue();
				pushLeft(node.getRight());
				return result;
			}
		};
	}

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
				E result = node.getValue();
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
				return stack2.pop().getValue();
			}
		};
	}

	@Override
	public boolean contains(E entry) throws NullPointerException
	{
		if (entry == null)
		{
			throw new NullPointerException("Cannot search for null.");
		}
		return search(entry) != null;
	}

	// inorder traversal
	public void inorder(String printOption)
	{
		inorderRec(root, printOption);
		System.out.println("\n");
	}

	private void inorderRec(BSTreeNode<E> root, String printOption)
	{
		if (root != null)
		{
			// get left
			inorderRec(root.getLeft(), printOption);
			// get root
			Map<String, Set<Integer>> Data = root.getFileData();
			Collection<Set<Integer>> Lines;
			switch (printOption)
			{
			case "f":
				// Alphabetical order, file names
				System.out.print("== " + root.getValue() + " ==");
				// get each file name
				for (String file : Data.keySet())
				{
					System.out.print(" *found in: " + file + " ");
				}
				System.out.print("\n");
				break;

			case "l":
				// Alphabetical order, file names, line numbers
				System.out.print("== " + root.getValue() + " ==");
				// get each file name
				for (String file : Data.keySet())
				{
					System.out.print(" *found in: " + file);
					{
						System.out.print(" on lines: ");
						// get each line
						for (Integer line : Data.get(file)) {
						System.out.print(line + " ");}
					}
				}
				System.out.print("\n");
				break;
			case "o":
				// Alphabetical order, file names, line numbers, frequency
				System.out.print("== " + root.getValue() + " ==" + " number of entries: " + Data.values().size());
				// get each file name
				for (String file : Data.keySet())
				{
					System.out.print(" *found in: " + file);
					{
						System.out.print(" on lines: ");
						// get each line
						for (Integer line : Data.get(file)) {
						System.out.print(line + " ");}
					}
				}

				System.out.print("\n");
				break;
			default:
				// Alphabetical
				System.out.print(root.getValue() + "\n");
				break;
			}
			// get right
			inorderRec(root.getRight(), printOption);
		}
	}

}
