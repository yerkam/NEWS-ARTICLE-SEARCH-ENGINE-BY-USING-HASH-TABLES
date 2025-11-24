import java.io.*;
import java.util.*;
public class Reader {
    
    private String DELIMITERS = "[-+=" +
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
    
/**
 * Dosya yolu konumunda bulunan makaleleri okuyup verilen articleCache HashTable'ına yükler.
 * Makalelerin her biri, makale ID'si anahtarıyla ve makale verilerinin bir LinkedList<String> değeriyle saklanır.
 * @param fileLocation // Makale verilerinin bulunduğu dosyanın yolu
 * @param articleCache // Makaleleri saklamak için kullanılan HashTable
 * @throws IOException 
 */
    public void loadArticles(String fileLocation, HashTableInterface<String, LinkedList<String>> articleCache) throws IOException { 
        try (BufferedReader reader = new BufferedReader(new FileReader(fileLocation))) {
            String line;
            reader.readLine(); // İlk satırı  atla (başlıklar)
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // Satırı partlara ayır
                if (parts.length > 0) { // Boş satırları atla
                    String articleId = parts[0].trim(); parts[0] = ""; // ID'yi al ve array'den temizle
                    articleCache.put(articleId, new LinkedList<>(Arrays.asList(parts))); // HashTable'a ekle
                }
            }
        }
    }

/**
 * Bu yöntem, verilen HashMap<String, Hash<String, Integer>> anahtarlarını aranacak kelimelerle doldurur.
 * İç HashMap'i <Makalenin ID'si, dış hash anahtarında bulunan kelimenin Makale metninde kaç kez geçtiği> ile doldurur.
 * @param indexMap Verileri saklamak için kullanılan HashMap
 * @param loadFactor Verilen yük faktörü, HashMap'in yeniden boyutlandırılması için kullanılır
 * @param loadFileLocation  Makale verilerinin yükleneceği dosyanın yolu
 * @param searchWordsFileLocation  Makale metninde aranacak kelimelerin bulunduğu dosyanın yolu
 * @param stopWordsFileLocation  Makale metninde göz ardı edilecek durdurma kelimelerinin bulunduğu dosyanın yolu
 * @param hashTableChoice Hangi hash tablosunun kullanılacağını belirten değişken
 * @param collisionChoice  Hangi collision yöntemini belirten değişken
 * @throws IOException 
 * @throws InterruptedException 
 */
    public void computeWordFrequencyTable(HashTableInterface<String, HashTableInterface<String, Integer>> indexMap, // Performans matrixi arama motoru, belli başlı kelimeler için 
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

            // Artık arama kelimeleri set olarak var, şimdi tüm makaleleri okuyup gerekli kelimeleri ekle
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(loadFileLocation))) {
            String line;
            reader.readLine(); // başlığı atla
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // Satırı partlara ayır
                String articleId = parts[0].trim(); // Makale ID'si

                String[] rawWords = parts[10].split(" "); // Makale metnini kelimelere ayır
                for (String raw : rawWords) { // Makaledeki her kelime için
                    String cleaned = cleanWord(raw.toLowerCase(), DELIMITERS); // Kelimeyi temizle
                    if (cleaned == null || cleaned.isEmpty()) continue; // Boş kelimeleri atla
                    if (stopWords.containsKey(cleaned)) continue; // Stop wordleri atla
                    if (!searchWords.containsKey(cleaned)) continue; // Sadece arama kelimelerini işle (Belli başlı kelimeler için bu satır bulunur, genel arama motoru için bulunmaz)

                    // indexMap.get(cleaned) veya oluştur
                    HashTableInterface<String, Integer> inner = indexMap.get(cleaned); // İç hash tablosunu al
                    if (inner == null) { // Eğer yoksa yeni oluştur
                        if (hashTableChoice) inner = new HashTableSSF<>(collisionChoice, loadFactor);
                        else inner = new HashTablePAF<>(collisionChoice, loadFactor);
                        indexMap.put(cleaned, inner);
                    }

                    Integer prev = inner.get(articleId); // Mevcut sayımı al
                    if (prev == null) inner.put(articleId, 1); // Yoksa 1 olarak ayarla
                    else inner.put(articleId, prev + 1); // Varsa sayımı artır
                } // for rawWords
            }
        }
    }

    public void computeWordFrequencyTable(HashTableInterface<String, HashTableInterface<String, Integer>> indexMap, // Genel arama motoru
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
                String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // Satırı partlara ayır
                String articleId = parts[0].trim(); // Makale ID'si

                String[] rawWords = parts[10].split(" "); // Makale metnini kelimelere ayır
                for (String raw : rawWords) { // Makaledeki her kelime için
                    String cleaned = cleanWord(raw.toLowerCase(), DELIMITERS); // Kelimeyi temizle 
                    if (cleaned == null || cleaned.isEmpty()) continue; // Boş kelimeleri atla
                    if (stopWords.containsKey(cleaned)) continue; // Stop wordleri atla

                    // indexMap.get(cleaned) veya oluştur
                    HashTableInterface<String, Integer> inner = indexMap.get(cleaned); // İç hash tablosunu al
                    if (inner == null) { // Eğer yoksa yeni oluştur
                        if (hashTableChoice) inner = new HashTableSSF<>(collisionChoice, loadFactor);
                        else inner = new HashTablePAF<>(collisionChoice, loadFactor);
                        indexMap.put(cleaned, inner);
                    }

                    Integer prev = inner.get(articleId); // Mevcut sayımı al
                    if (prev == null) inner.put(articleId, 1); // Yoksa 1 olarak ayarla
                    else inner.put(articleId, prev + 1); // Varsa sayımı artır
                } // for rawWords
                wordDone++;
                if (wordDone % 1000 == 0) { // Her 1000 makalede bir ilerlemeyi yazdır
                    System.out.println("Articles processed: " + wordDone);
                }
            }
        }
    }

    public void loadStopWords(HashTableInterface<String, Boolean> temp, String stopWordsFileLocation){
        // Stop kelimelerini bir listeye kaydet
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