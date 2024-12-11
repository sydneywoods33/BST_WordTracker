package appDomain;

import implementations.BSTree;
import implementations.BSTreeNode;
import java.io.*;

public class WordTracker implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static final String REPOSITORY_FILE = "res/repository.ser";
	private static BSTree<String> wordTree;

	// constructs a wordtracker object, loads repository or creates a new tree
	public WordTracker()
	{
		File file = new File(REPOSITORY_FILE);
		if (file.exists())
		{
			loadRepository();
		} else
		{
			wordTree = new BSTree<>();
		}
	}

	public static void main(String[] args)
	{
		// command line variables
		String input = "";
		String order = "";
		String output = "";

		// check if arguments given
		if (args.length < 1)
		{
			System.out.println("Usage: java appDomain.WordTracker <file_path>");
			return;
		}

		// parse arguments
		for (String arg : args)
		{
			if (arg.startsWith("-f"))
			{
				output = arg.substring(2);
			} else if (arg.startsWith("-p"))
			{
				order = arg.substring(2);
			} else
			{
				input = arg;
			}

		}

		WordTracker tracker = new WordTracker();
		tracker.processFile(input);
		wordTree.inorder(order);
		if (!output.isEmpty())
		{
			tracker.saveRepository(output);
		} else
		{
			tracker.saveRepository();
		}

	}

	// processes file, adds words to the tree with the file + line info
	private void processFile(String filePath)
	{
		File file = new File(filePath);
		if (!file.exists() || !file.isFile())
		{
			System.out.println("File not found. Please provide a valid file path.");
			return;
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(file)))
		{
			String line;
			int lineNumber = 1;

			while ((line = reader.readLine()) != null)
			{
				String[] words = line.split("\\W+");
				for (String word : words)
				{
					if (!word.isEmpty())
					{
						addWordToTree(word.toLowerCase(), file.getName(), lineNumber);
					}
				}
				lineNumber++;
			}

			System.out.println("File processed successfully.");

		} catch (IOException e)
		{
			System.out.println("Error reading file: " + e.getMessage());
		}
	}

	// adds word to the tree, updating existing entries or adding new ones
	private void addWordToTree(String word, String fileName, int lineNumber)
	{
		BSTreeNode<String> node = wordTree.search(word);
		if (node == null)
		{
			wordTree.add(word);
			node = wordTree.search(word);
		}
		node.addFileData(fileName, lineNumber);
	}

	// loads the word tree from repo file
	@SuppressWarnings("unchecked")
	private void loadRepository()
	{
		File file = new File(REPOSITORY_FILE);
		if (!file.exists())
		{
			wordTree = new BSTree<>();
			return;
		}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file)))
		{
			wordTree = (BSTree<String>) ois.readObject();
			System.out.println("Repository loaded successfully.");
		} catch (IOException | ClassNotFoundException e)
		{
			System.out.println("Error loading repository. Starting with a new tree.");
			wordTree = new BSTree<>();
		}
	}

	// saves the word tree to repo file
	private void saveRepository()
	{
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(REPOSITORY_FILE)))
		{
			oos.writeObject(wordTree);
			System.out.println("Repository saved successfully.");
			System.out.println("File not exported.");
		} catch (IOException e)
		{
			System.out.println("Error saving repository: " + e.getMessage());
		}
	}

	// saves the word tree to another file
	private void saveRepository(String filePath)
	{
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath)))
		{
			oos.writeObject(wordTree);
			System.out.println("Repository saved successfully.");
			System.out.println("Exported to: " + filePath);
		} catch (IOException e)
		{
			System.out.println("Error saving repository: " + e.getMessage());
		}
	}
}
