import java.io.*;
import java.util.*;
public class Reader {
    
    public static final String DELIMITERS = "[-+=" +
                                                    " " +       // space
                                                    "\r\n " +    //carriage return line fit
                                                    "1234567890" + //numbers
                                                    "’'\"" +       // apostrophe
                                                    "(){}<>\\[\\]" + // brackets
                                                    ":" +        // colon
                                                    "," +        // comma
                                                    "‒–—―" +     // dashes
                                                    "…" +        // ellipsis
                                                    "!" +        // exclamation mark
                                                    "«»" +       // guillemets
                                                    "-‐" +       // hyphen
                                                    "?" +        // question mark
                                                    "‘’“”" +     // quotation marks
                                                    ";" +        // semicolon
                                                    "/" +        // slash/stroke
                                                    "⁄" +        // solidus
                                                    "␠" +        // space?   
                                                    "·" +        // interpunct
                                                    "&" +        // ampersand
                                                    "@" +        // at sign
                                                    "*" +        // asterisk
                                                    "\\" +       // backslash
                                                    "•" +        // bullet
                                                    "^" +        // caret
                                                    "¤¢$€£¥₩₪" + // currency
                                                    "†‡" +       // dagger
                                                    "°" +        // degree
                                                    "¡" +        // inverted exclamation point
                                                    "¿" +        // inverted question mark
                                                    "¬" +        // negation
                                                    "#" +        // number sign (hashtag)
                                                    "№" +        // numero sign ()
                                                    "%‰‱" +      // percent and related signs
                                                    "¶" +        // pilcrow
                                                    "′" +        // prime
                                                    "§" +        // section sign
                                                    "~" +        // tilde/swung dash
                                                    "¨" +        // umlaut/diaeresis
                                                    "_" +        // underscore/understrike
                                                    "|¦" +       // vertical/pipe/broken bar
                                                    "⁂" +        // asterism
                                                    "☞" +        // index/fist
                                                    "∴" +        // therefore sign
                                                    "‽" +        // interrobang
                                                    "※" +          // reference mark
                                                    "]";
    
    public void loadArticles(String fileLocation, double loadFactor, HashTableInterface articleCache) throws IOException {
        List<String> allLines = new ArrayList<>(); // Tüm satırları tutacak liste, daha sonra belirli bir oranda işleyeceğiz
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fileLocation))) {
            String line;
            reader.readLine(); // Ilk satırı  atla (başlıklar)
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
            }
        }
        int totalRows = allLines.size();
        int rowsToProcess = (int) (totalRows * loadFactor);
        
        System.out.println("Total rows: " + totalRows);
        System.out.println("Processing: " + rowsToProcess + " rows (" + (loadFactor*100) + "%)");
        
        // Belirlenen sayıda satırı işle
        for (int i = 0; i < rowsToProcess; i++) {
            String line = allLines.get(i);
            String[] parts = line.split(",");

            
            if (parts.length > 0) { // Boş satırları atla
                String articleId = parts[0].trim(); parts[0] = ""; // ID'yi al ve array'den temizle
                
                // KELIMELERIN TEMIZLENMESI VE NORMALLESTIRILMESI KISMI EKSIK

                articleCache.put(articleId, new ArrayList<>(Arrays.asList(parts))); // HashTable'a ekle
            }
            System.out.println("Loaded " + (i + 1) + " / " + rowsToProcess + " articles."); // Loading progress
        }
    }

    /**
     * This method fills the keys <String, Hash<String, Integer>> of the given hash with the words to be searched. 
     * Fills the inner hash with <Article's ID, how many times the word found in the key of the outer hash is in the Article text>.
     * @param indexMap Hash that returns data
     * @param loadFactor How much of the total data given will be used (0 <= F <= 1)
     * @param loadFileLocation  Path to the file to search
     * @param searchWordsFileLocation  Path to the file containing the words to search for
     * @param stopWordsFileLocation  The path to the file containing the words to skip
     * @param hashTableChoice A variables required for the hash table.
     * @param collisionChoice  A variables required for the hash table.
     * @throws IOException 
     */
    public void computeWordFrequencyTable(HashTableInterface<String, HashTableInterface<String, Integer>> indexMap,
                                    double loadFactor,
                                    String loadFileLocation,
                                    String searchWordsFileLocation,
                                    String stopWordsFileLocation,
                                    boolean hashTableChoice,
                                    boolean collisionChoice) throws IOException {
        try (BufferedReader searchWordsReader = new BufferedReader(new FileReader(searchWordsFileLocation))){ 
            String wordToSearch;
            while ((wordToSearch = searchWordsReader.readLine()) != null){ 
                HashTableInterface<String, Integer> wordCountMap; // We initialize a separate hash to be placed in the indexMap's value.
                if(hashTableChoice) wordCountMap = new HashTableSSF<>(collisionChoice);
                else wordCountMap = new HashTablePAF<>(collisionChoice);
                
                List<String> allLines = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader(loadFileLocation))) { 
                    String line;
                    reader.readLine(); // Ilk satırı  atla (başlıklar)
                    while ((line = reader.readLine()) != null) {
                        allLines.add(line);
                    }
                }

                int rowsToProcess = (int)(allLines.size()*loadFactor);

                for (int i = 0; i < rowsToProcess; i++) {
                    String line = allLines.get(i);
                    String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                    String[] words = parts[10].split(" "); // Article textin kelimeleri
                    int count = 0;
                    for(String word : words){
                        
                        if(word.equalsIgnoreCase(wordToSearch) && !stopWordController(word, stopWordsFileLocation)){ // Satirdaki kelime aranan kelimeye esitse && stop word degilse
                            count++;
                        }
                    }
                    wordCountMap.put(parts[0], count); // parts[0]: ID
                }
                indexMap.put(wordToSearch, wordCountMap);
            }
        }
    }

    private boolean stopWordController(String word, String stopWordsFileLocation){
        // Kelimenin stop word olup olmadığını kontrol etme
        try (BufferedReader reader = new BufferedReader(new FileReader(stopWordsFileLocation))) {
            String stopWord;
            while ((stopWord = reader.readLine()) != null) {
                if (word.equalsIgnoreCase(stopWord.trim())) {
                    return true; // Kelime bir stop word ise
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}