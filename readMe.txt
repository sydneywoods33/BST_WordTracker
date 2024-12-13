# WordTracker

## Overview
WordTracker is a Java application that constructs a binary search tree (BST) from words in a text file. It records the line numbers and file names where each word appears and stores this information in a serialized repository file.

## Installation

### Prerequisites
- Java Development Kit (JDK) 1.8 or higher
- An IDE such as Eclipse or IntelliJ IDEA (optional, but recommended)

### Steps
1. **Clone or Download the Repository**
   - Clone the repository using Git:
     ```sh
     git clone <repository-url>
     ```
   - Or download the ZIP file from the repository and extract it.

2. **Open the Project in Your IDE**
   - Open your preferred IDE.
   - Import the project as an existing project.
   - Ensure that the JDK 1.8 or higher is set as the project's JDK.

3. **Build the Project**
   - If using an IDE, build the project using the build tools provided by the IDE.
   - If using the command line, navigate to the project root directory and compile the project:
     ```sh
     javac -d bin -sourcepath src src/appDomain/WordTracker.java
     ```

4. **Create the Executable JAR**
   - If using an IDE, export the project as a runnable JAR file.
   - If using the command line, create the JAR file:
     ```sh
     jar cfe WordTracker.jar appDomain.WordTracker -C bin .
     ```

## Usage

### Running the Application
To run the WordTracker application from the command line, use the following command:

```sh
java -jar WordTracker.jar <input.txt> -pf/-pl/-po [-f <output.txt>]
