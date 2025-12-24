# News Article Search Engine using Hash Tables

## Project Overview
This project implements a high-performance, in-memory search engine designed to index and query news articles efficiently. It serves as a practical application of **Hash Table** data structures, demonstrating custom implementations of hashing algorithms and collision resolution strategies to achieve near-instantaneous search results.

The system processes raw text data (CNN Articles), filters out common stop words, and builds an inverted index that maps keywords to specific news articles, allowing users to find relevant content based on search queries.

## Features

### 1. Custom Hash Table Implementations
The project does not rely on Java's built-in `HashMap`. Instead, it features two custom implementations to compare performance:
* **Simple Summation Function (SSF):** A basic hashing algorithm that sums the ASCII values of characters.
* **Polynomial Accumulation Function (PAF):** A more advanced hashing algorithm (Horner's Rule) designed to minimize collisions by using a polynomial constant (z=33).

### 2. Collision Resolution Strategies
To handle hash collisions, the system implements and compares two Open Addressing techniques:
* **Linear Probing (LP):** Searches for the next available slot sequentially.
* **Double Hashing (DH):** Uses a secondary hash function to calculate the step size, reducing clustering.

### 3. Search Engine Capabilities
* **Relevance Ranking:** Scores articles based on keyword frequency and displays the top 5 most relevant results.
* **Stop Word Filtering:** Automatically removes common English words (e.g., "the", "and") using a loaded `stop_words_en.txt` list to improve search quality.
* **Text Preprocessing:** Cleans raw text by removing punctuation, converting to lowercase, and handling delimiters.

### 4. Performance Analysis
* **Benchmarking:** Includes a built-in performance matrix that measures:
    * Indexing Time
    * Average Search Time
    * Collision Counts
* **Comparison:** Allows testing different combinations of Load Factors (0.5, 0.8), Hash Functions (SSF vs. PAF), and Collision Strategies (LP vs. DH).

## Project Structure

### Core Data Structures
* **`HashTableInterface.java`**: Defines the contract for hash table operations (`put`, `get`, `resize`, etc.).
* **`HashEntry.java`**: Represents a key-value pair stored in the hash table.
* **`Collision.java`**: Abstract class containing logic for **Linear Probing** and **Double Hashing**.
* **`HashTableSSF.java`**: Implementation using Simple Summation Function.
* **`HashTablePAF.java`**: Implementation using Polynomial Accumulation Function.

### Application Logic
* **`Main.java`**: The entry point. Handles the CLI menu, user inputs, and triggers the performance matrix.
* **`MainFunctionalities.java`**: Abstract class managing high-level operations like loading articles and computing word frequencies.
* **`Reader.java`**: Handles File I/O. Parses `CNN_Articels.csv`, cleans text, and loads data into hash tables.
* **`articleScore.java`**: Helper class to store and sort articles based on relevance frequency.

## Getting Started

### Prerequisites
* Java Development Kit (JDK) 8 or higher.
* Input files in the root directory:
    * `CNN_Articels.csv` (Dataset)
    * `stop_words_en.txt` (Stop words list)
    * `search.txt` (Optional, for specific performance tests)

### Compilation & Execution

1.  **Compile the source code:**
    ```bash
    javac *.java
    ```

2.  **Run the application:**
    ```bash
    java Main
    ```

### Usage Guide
Upon running the program, the system will index the articles (this may take a few seconds). You will then be presented with a menu:

1.  **Search:** Enter a sentence or keyword. The system will rank articles by relevance and show the top results.
2.  **Lookup by ID:** Enter a specific Article ID to retrieve its full content.
3.  **Performance Matrix:** Generates a detailed report comparing SSF/PAF and LP/DH performance metrics.
9.  **Exit:** Closes the application.

## Algorithm Details

### Indexing Process
1.  **Load Stop Words:** Reads `stop_words_en.txt` into a Hash Table for $O(1)$ lookup.
2.  **Read Articles:** Parses `CNN_Articels.csv`.
3.  **Tokenization & Cleaning:** Splits text by delimiters, removes punctuation, and converts to lowercase.
4.  **Frequency Counting:** For every meaningful word, updates an inverted index:
    `Word -> { ArticleID -> Frequency }`

### Hashing Functions
* **SSF:** $h(key) = (\sum charcode) \mod TableSize$
* **PAF:** $h(key) = (c_0 \cdot z^{n-1} + c_1 \cdot z^{n-2} + ... + c_{n-1}) \mod TableSize$ (where $z=33$)

## License
This project is open-source and available under the [MIT License](LICENSE).
