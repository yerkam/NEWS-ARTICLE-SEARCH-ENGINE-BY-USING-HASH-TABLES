import java.io.IOException;
import java.util.*;
public class Main extends MainFunctionalities {
    public static void main(String[] args) throws IOException {
        Reader reader = new Reader();
        HashTableSSF hash = new HashTableSSF<>(false);
        reader.loadArticles("CNN_Articels.csv", 1, hash);
        
        /*try {
            String filePath = "CNN_Articels.csv";
            List<String> article = reader.findArticleWithID(filePath, "x");
            if (article != null) {
                System.out.println("Article found: " + String.join(", ", article));
            } else {
                System.out.println("Article not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } */

        /*ExcelReader reader = new ExcelReader();
        try {
            reader.loadArticles("CNN_Articels.csv", 1);
        } catch (IOException e) {
            e.printStackTrace();
        } 
        List<String> article = reader.findArticleWithID("x");  // Hızlı arama yap
        System.out.println(article);
        System.out.println(reader.articleCache);*/
        
    }// end main
}// end class 

//16902. satir eksik
//18345. satir eksik
