import java.io.*;
import java.util.*;
public class ExcelReader {
    
    public static final String DELIMITERS = "[-+=" +
                                                    " " +        //space
                                                    "\r\n " +    //carriage return line fit
                                                    "1234567890" + //numbers
                                                    "’'\"" +       // apostrophe
                                                    "(){}<>\\[\\]" + // brackets
                                                    ":" +        // colon
                                                    "," +        // comma
                                                    "‒–—―" +     // dashes
                                                    "…" +        // ellipsis
                                                    "!" +        // exclamation mark
                                                    "." +        // full stop/period
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

    public List<String> findArticleWithID(String fileLocation, String ID) throws IOException {
    
        long startTime = System.nanoTime();
        int rowCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileLocation))) {
            String line;
            reader.readLine(); // Header satırını atla
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0)
                {   rowCount++;
                    if(parts[0].equals(ID)){
                        // Timer and statistics
                        long endTime = System.nanoTime();
                        long duration = (endTime - startTime) / 1_000_000;
                        double seconds = duration / 1000.0;
                        System.out.println("Toplam satır: " + rowCount);
                        System.out.println("Süre: " + duration + " ms (" + seconds + " saniye)");
                        System.out.println("Saniyede okunan satır: " + (rowCount / seconds));
                        return new ArrayList<>(Arrays.asList(parts));
                    }
                }
            
            }
        }

        // Timer and statistics
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        double seconds = duration / 1000.0;
        System.out.println("Toplam satır: " + rowCount);
        System.out.println("Süre: " + duration + " ms (" + seconds + " saniye)");
        System.out.println("Saniyede okunan satır: " + (rowCount / seconds));

        return null;
    }
}
