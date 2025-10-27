import java.io.*;
import java.util.*;
public class ExcelReader {
    
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
    
    public HashMap<String, List<String>> articleCache = new HashMap<>(); // Will be used own hash table implementation later
    
    public void loadArticles(String fileLocation, double loadFactor) throws IOException {
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

    public List<String> findArticleWithID(String ID) { // Hızlı arama için hash table kullan
        List<String> result = articleCache.get(ID);
        return result;
    }
}