import java.io.*;
import java.util.*;
public class Reader {
    
    public static final String DELIMITERS = "[-+=" +
                                                    " " + 
                                                    "." +       // space
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
    
    public void loadArticles(String fileLocation, HashTableInterface<String, LinkedList<String>> articleCache) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileLocation))) {
            String line;
            reader.readLine(); // Ilk satırı  atla (başlıklar)
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // Satiri partlara ayir
                if (parts.length > 0) { // Boş satırları atla
                    String articleId = parts[0].trim(); parts[0] = ""; // ID'yi al ve array'den temizle
                    articleCache.put(articleId, new LinkedList<>(Arrays.asList(parts))); // HashTable'a ekle
                }
            }
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
     * @throws InterruptedException 
     */
    public void computeWordFrequencyTable(HashTableInterface<String, HashTableInterface<String, Integer>> indexMap, // For performance matrix
                                    double loadFactor,
                                    String loadFileLocation,
                                    String searchWordsFileLocation,
                                    String stopWordsFileLocation,
                                    boolean hashTableChoice,
                                    boolean collisionChoice) throws IOException, InterruptedException { 
                                        
        HashTableInterface<String, Boolean> stopWords; 
        HashTableInterface<String, Boolean> searchWords;
        if(hashTableChoice){ 
            stopWords = new HashTableSSF<>(collisionChoice, loadFactor);
            searchWords = new HashTableSSF<>(collisionChoice, loadFactor);
        }
        else {
            stopWords = new HashTablePAF<>(collisionChoice, loadFactor);
            searchWords = new HashTablePAF<>(collisionChoice, loadFactor);
        }  

        /**
         *  Butun Article'lari okuyup temizle ve bunlari kaydet
        */
        // Stop wordlari yükle
        loadStopWords(stopWords, stopWordsFileLocation);

       


        try (BufferedReader reader = new BufferedReader(new FileReader(searchWordsFileLocation))){
            String wordToSearch;

            while ((wordToSearch = reader.readLine()) != null){ 
                searchWords.put(wordToSearch, false);
            }

            // Artik arama kelimeleri set olarak var, simdi tum article'lari okuyup gerekli kelimeleri ekle
        }

        int wordDone = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(loadFileLocation))) {
            String line;
            reader.readLine(); // başlığı atla
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                String articleId = parts[0].trim();

                String[] rawWords = parts[10].split(" ");
                for (String raw : rawWords) {
                    String cleaned = cleanWord(raw.toLowerCase(), DELIMITERS);
                    if (cleaned == null || cleaned.isEmpty()) continue;
                    if (stopWords.containsKey(cleaned)) continue;
                    if (!searchWords.containsKey(cleaned)) continue;

                    // indexMap.get(cleaned) veya oluştur
                    HashTableInterface<String, Integer> inner = indexMap.get(cleaned);
                    if (inner == null) {
                        if (hashTableChoice) inner = new HashTableSSF<>(collisionChoice, loadFactor);
                        else inner = new HashTablePAF<>(collisionChoice, loadFactor);
                        indexMap.put(cleaned, inner);
                    }

                    Integer prev = inner.get(articleId);
                    if (prev == null) inner.put(articleId, 1);
                    else inner.put(articleId, prev + 1);
                } // for rawWords
                wordDone++;
                if (wordDone % 1000 == 0) {
                    System.out.println("Articles processed: " + wordDone);
                }
            }
        }
    }

    public void computeWordFrequencyTable(HashTableInterface<String, HashTableInterface<String, Integer>> indexMap, // General search engine
                                    double loadFactor,
                                    String loadFileLocation,
                                    String stopWordsFileLocation,
                                    boolean hashTableChoice,
                                    boolean collisionChoice) throws IOException, InterruptedException {
                                        
        HashTableInterface<String, Boolean> stopWords;
        if(hashTableChoice){ 
            stopWords = new HashTableSSF<>(collisionChoice, loadFactor);
        }
        else {
            stopWords = new HashTablePAF<>(collisionChoice, loadFactor);
        }  

        /**
         *  Butun Article'lari okuyup temizle ve bunlari kaydet
        */
        // Stop wordlari yükle
        loadStopWords(stopWords, stopWordsFileLocation);

       

        int wordDone = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(loadFileLocation))) {
            String line;
            reader.readLine(); // başlığı atla
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                String articleId = parts[0].trim();

                String[] rawWords = parts[10].split(" ");
                for (String raw : rawWords) {
                    String cleaned = cleanWord(raw.toLowerCase(), DELIMITERS);
                    if (cleaned == null || cleaned.isEmpty()) continue;
                    if (stopWords.containsKey(cleaned)) continue;

                    // indexMap.get(cleaned) veya oluştur
                    HashTableInterface<String, Integer> inner = indexMap.get(cleaned);
                    if (inner == null) {
                        if (hashTableChoice) inner = new HashTableSSF<>(collisionChoice, loadFactor);
                        else inner = new HashTablePAF<>(collisionChoice, loadFactor);
                        indexMap.put(cleaned, inner);
                    }

                    Integer prev = inner.get(articleId);
                    if (prev == null) inner.put(articleId, 1);
                    else inner.put(articleId, prev + 1);
                } // for rawWords
                wordDone++;
                if (wordDone % 1000 == 0) {
                    System.out.println("Articles processed: " + wordDone);
                }
            }
        }
    }

    private void loadStopWords(HashTableInterface<String, Boolean> temp, String stopWordsFileLocation){
        // Stop wordlari bir liste kaydet
        try (BufferedReader reader = new BufferedReader(new FileReader(stopWordsFileLocation))) {
            String stopWord;
            while ((stopWord = reader.readLine()) != null) {
                if(!stopWord.isEmpty())
                    temp.put(stopWord, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String cleanrecursive(String word, String delimiter) {
        // Base case: word boşsa veya delimiter boşsa
        if (word == null || word.isEmpty() || delimiter == null || delimiter.isEmpty()) {
            return word;
        }

        boolean foundAndRemoved = false;
        String newWord = word;

        // Delimiter'daki her karakteri kontrol et
        for (char delimChar : delimiter.toCharArray()) {
            int index = newWord.indexOf(delimChar);
            if (index != -1) {
                // İlk bulduğun karakteri sil
                newWord = newWord.substring(0, index) + newWord.substring(index + 1);
                foundAndRemoved = true;
                break; // Bir karakter sildik, recursive çağrı yapacağız
            }
        }

        // Eğer hiçbir karakter silinmediyse, döngüden çık
        if (!foundAndRemoved) {
            return newWord;
        }

        // Recursive olarak devam et
        return cleanWord(newWord, delimiter);
    }

    private String cleanrecursivestringbuilder(String word, String delimiter) {
        // Base case: word boşsa veya delimiter boşsa
        if (word == null || word.isEmpty() || delimiter == null || delimiter.isEmpty()) {
            return word;
        }

        boolean foundAndRemoved = false;
        StringBuilder currentWord = new StringBuilder(word);

        // Delimiter'daki her karakteri kontrol et
        for (char delimChar : delimiter.toCharArray()) {
            for (int i = 0; i < currentWord.length(); i++) {
                if (currentWord.charAt(i) == delimChar) {
                    // İlk bulduğun karakteri sil
                    currentWord.deleteCharAt(i);
                    foundAndRemoved = true;
                    break; // Bir karakter sildik, recursive çağrı yapacağız
                }
            }
            if (foundAndRemoved)
                break;
        }

        // Eğer hiçbir karakter silinmediyse, döngüden çık
        if (!foundAndRemoved) {
            return currentWord.toString();
        }

        // Recursive olarak devam et
        return cleanWord(currentWord.toString(), delimiter);
    }

    private String cleanWord(String word, String delimiter) {
        // 1. Basit Durum Kontrolleri
        if (word == null || word.isEmpty() || delimiter == null || delimiter.isEmpty()) {
            return word;
        }

        String result = word;
        int start = 0;
        int end = result.length() - 1;

        // 2. Baştan Temizleme: Kelimenin başındaki delimiter karakterlerini atla
        // Karakteri delimiter'da aramak için indexOf() kullanılır
        while (start <= end && delimiter.indexOf(result.charAt(start)) != -1) {
            start++;
        }

        // 3. Sondan Temizleme: Kelimenin sonundaki delimiter karakterlerini atla
        while (end >= start && delimiter.indexOf(result.charAt(end)) != -1) {
            end--;
        }

        // 4. Temizlenmiş Alt Dizeyi Döndürme
        // Eğer start > end ise kelimenin tamamı temizlenmiştir, boş string döndür
        if (start > end) {
            return "";
        }

        // start ve end, temizlenmiş kelimenin sınırlarını belirler
        return result.substring(start, end + 1);
    }

    private String cleanWordregex(String word, String delimiter) {

        return word.replaceAll(delimiter, "");
    }
    /*
     * Performans Karşılaştırması
     * YaklaşımZamanAlanStack RiskMevcut (Recursive)O(n²×m)O(n×d)✗ VarStringBuilder
     * (İteratif)O(n+m)O(n)✓ YokRegexO(n×m)O(n)✓ Yok
     * Önerim: İlk yaklaşımı (StringBuilder + HashSet) kullan. En hızlı ve en
     * güvenli çözüm bu. Özellikle büyük stringlerde recursive yaklaşım stack
     * overflow'a neden olabilir.
     * Hangi yaklaşımı tercih etmek istersin? Senaryona göre detaylandırabilirim.
     */
}