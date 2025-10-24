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
            if (line.trim().isEmpty()) continue;
            rowCount++;
            String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // CSV format için özel split
            if (parts.length > 0 && parts[0].equals(ID)) {
                return new ArrayList<>(Arrays.asList(parts));
            }
            System.out.println("Checked ID: " + parts[0]);
            
        }
    }

    long endTime = System.nanoTime();
    long duration = (endTime - startTime) / 1_000_000; // milliseconds
    double seconds = duration / 1000.0;
    System.out.println("Toplam satır: " + rowCount);
    System.out.println("Süre: " + duration + " ms (" + seconds + " saniye)");
    System.out.println("Saniyede okunan satır: " + (rowCount / seconds));

    return null;
    }
}
