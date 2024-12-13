package appDomain;

import implementations.BSTree;
import implementations.BSTreeNode;
import java.io.*;
import java.util.Map;
import java.util.Set;

/**
 * The WordTracker class processes text files to track the occurrence of words,
 * including the files and line numbers where they appear. It uses a binary search tree (BST)
 * to store this information.
 */
public class WordTracker implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final String REPOSITORY_FILE = "repository.ser";
    private static BSTree<String> wordTree;

    /**
     * Constructs a WordTracker object, loading the repository if it exists,
     * or creating a new tree if it does not.
     */
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

    /**
     * The main method to run the WordTracker program.
     * 
     * @param args Command-line arguments
     */
    public static void main(String[] args)
    {
        // Validate command-line arguments
        if (!validateArguments(args))
        {
            System.out.println("Usage: java appDomain.WordTracker <file_path> -pf/-pl/-po [-f <output.txt>]");
            return;
        }

        // command line variables
        String input = "";
        String order = "";
        String output = "";

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
        tracker.inorder(order, output);
        tracker.saveRepository();
    }

    /**
     * Processes a file, adding words to the tree with the file and line information.
     * 
     * @param filePath The path to the file to be processed
     */
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

    /**
     * Adds a word to the tree, updating existing entries or adding new ones.
     * 
     * @param word The word to add
     * @param filePath The file where the word was found
     * @param lineNumber The line number where the word was found
     */
    private void addWordToTree(String word, String filePath, int lineNumber)
    {
        BSTreeNode<String> node = wordTree.search(word);
        if (node == null)
        {
            wordTree.add(word);
            node = wordTree.search(word);
        }
        node.addFileData(filePath, lineNumber);
    }

    /**
     * Generates a report based on the tree and writes it to a file or prints it to the console.
     * 
     * @param printOption The print option (-pf, -pl, -po)
     * @param filePath The path to the output file
     */
    public void inorder(String printOption, String filePath)
    {
        StringBuilder printReport = new StringBuilder();
        printReport.append(inorderRec(wordTree.getRoot(), printOption));
        if (filePath.isEmpty())
        {
            System.out.println("File not exported.");
            System.out.print(printReport);
        } else
        {
            File file = new File(filePath);
            try
            {
                if (!file.exists())
                {
                    file.createNewFile();
                }

                FileWriter myWriter = new FileWriter(filePath);
                myWriter.append(printReport);
                myWriter.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            System.out.println("File exported to: " + filePath);
        }
    }

    /**
     * Recursively traverses the tree to generate the report.
     * 
     * @param root The root node of the tree
     * @param printOption The print option (-pf, -pl, -po)
     * @return The generated report as a StringBuilder
     */
    private StringBuilder inorderRec(BSTreeNode<String> root, String printOption)
    {
        StringBuilder printReport = new StringBuilder();
        if (root != null)
        {
            // get left
            printReport.append(inorderRec(root.getLeft(), printOption));
            // get root
            Map<String, Set<Integer>> Data = root.getFileData();
            switch (printOption)
            {
            case "f":
                // Alphabetical order, file names
                printReport.append("== " + root.getElement() + " ==");
                // get each file name
                for (String file : Data.keySet())
                {
                    printReport.append(" *found in: " + file + " ");
                }
                printReport.append("\n");
                break;

            case "l":
                // Alphabetical order, file names, line numbers
                printReport.append("== " + root.getElement() + " ==");
                for (String file : Data.keySet())
                {
                    printReport.append(" *found in: " + file);
                    {
                        printReport.append(" on lines: ");
                        // get each line
                        for (Integer line : Data.get(file))
                        {
                            printReport.append(line + " ");
                        }
                    }
                }

                printReport.append("\n");
                break;
            case "o":
                Integer entries = 0;
                for (Set<Integer> value : Data.values())
                {
                    entries += value.size();
                }
                // Alphabetical order, file names, line numbers, frequency
                printReport.append("== " + root.getElement() + " ==" + " number of entries: " + entries);
                // get each file name
                for (String file : Data.keySet())
                {
                    printReport.append(" *found in: " + file);
                    {
                        printReport.append(" on lines: ");
                        // get each line
                        for (Integer line : Data.get(file))
                        {
                            printReport.append(line + " ");
                        }
                    }
                }

                printReport.append("\n");
                break;
            default:
                // Alphabetical
                printReport.append(root.getElement() + "\n");
                break;
            }
            // get right
            printReport.append(inorderRec(root.getRight(), printOption));
        }
        return printReport;
    }

    /**
     * Loads the word tree from the repository file.
     */
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

    /**
     * Saves the word tree to the repository file.
     */
    private void saveRepository()
    {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(REPOSITORY_FILE)))
        {
            oos.writeObject(wordTree);
            System.out.println("Repository saved successfully.");
        } catch (IOException e)
        {
            System.out.println("Error saving repository: " + e.getMessage());
        }
    }

    /**
     * Validates the command-line arguments.
     * 
     * @param args The command-line arguments
     * @return true if the arguments are valid, false otherwise
     */
    private static boolean validateArguments(String[] args)
    {
        if (args.length < 2)
        {
            return false;
        }
        String order = args[1];
        if (!order.equals("-pf") && !order.equals("-pl") && !order.equals("-po"))
        {
            return false;
        }
        return true;
    }
}
