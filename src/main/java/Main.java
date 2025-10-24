import java.io.IOException;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        ExcelReader reader = new ExcelReader();
        try {
            String filePath = "CNN_Articels.csv";
            List<String> article = reader.findArticleWithID(filePath, "WN0M7728E6");
            if (article != null) {
                System.out.println("Article found: " + String.join(", ", article));
            } else {
                System.out.println("Article not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }// end main
}// end class 

//16902. satir eksik
//18345. satir eksik