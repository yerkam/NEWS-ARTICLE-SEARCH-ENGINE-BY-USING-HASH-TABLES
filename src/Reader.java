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

    public void indexFileLines(String fileLocation, HashTableInterface articleCache) throws IOException { 
        // Search engine için dosyadaki satırları indeksleme
        try (BufferedReader reader = new BufferedReader(new FileReader(fileLocation))) {
            String line;
            while ((line = reader.readLine()) != null) {
                articleCache.put(line, null); // Sadece anahtar olarak satırı ekle
            }
        }
    }

    public void countWordFrequencies(String searchFileLocation, String stopWordsFileLocation, HashTableInterface indexMap) throws IOException {
        // Dosyadaki kelimelerin frekanslarını sayma ve indeksleme
        try (BufferedReader reader = new BufferedReader(new FileReader(searchFileLocation))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(DELIMITERS);
                String ID = line.substring(0, 9);
                for (String word : words) {
                    if(stopWordController(word, stopWordsFileLocation)) continue; // Stop word ise atla
                    word = word.toLowerCase().trim();
                    if (!word.isEmpty()) {
                        
                        // Index map value icinde bulunan hash tablelara ekleme yapma

                    }
                }
            }
        }
    }

    public boolean stopWordController(String word, String stopWordsFileLocation){
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