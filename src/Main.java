import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main extends MainFunctionalities {
    public static void main(String[] args) throws IOException, InterruptedException{
        long startTime = System.currentTimeMillis();
        double loadFactor = 0.5;
        boolean hashTableChoice = true; // false -> PAF, true -> SSF
        boolean collisionChoice = false; // false -> LP, true -> DH
        HashTableInterface<String, HashTableInterface<String, Integer>> wordArticleCountMap;
        HashTableInterface<String, LinkedList<String>> IDhashTable;
        if(hashTableChoice){
            IDhashTable = new HashTableSSF<>(collisionChoice, loadFactor);
            wordArticleCountMap = new HashTableSSF<>(collisionChoice, loadFactor);
        }else{
            IDhashTable = new HashTablePAF<>(collisionChoice, loadFactor);
            wordArticleCountMap = new HashTablePAF<>(collisionChoice, loadFactor); 
        }
        
        IDhashTable = loadArticles(loadFactor, hashTableChoice, collisionChoice);
        wordArticleCountMap = findArticleWithSearchEngine(loadFactor, hashTableChoice, collisionChoice);

        // Measure time
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime/1000);
        System.out.print(wordArticleCountMap.getCollisionCount());
        System.out.println();
        
        String input = "";
        Scanner scn = new Scanner(System.in);
        // User Interface
        while(!input.equals("exit")){
            System.out.println("1 - Choose one of the searched words and find out which article is most relevant.");
            System.out.println("2 - Find Article by News ID number.");
            
            System.out.println("Type 'exit' to quit.");
            System.out.print("Enter the number of the transaction you want to perform: ");
            System.out.println();
            input = scn.next();
            input = input.toLowerCase();
            if(input.equals("1")){
                System.out.print("Write the word you want to search: ");
                input = scn.next();
                System.out.println();
                if(!wordArticleCountMap.containsKey(input)) System.out.println("No word was selected from the search word list.");
                else{
                    HashTableInterface<String, Integer> tempHashTable;

                    if(hashTableChoice) tempHashTable = new HashTableSSF<>(collisionChoice, loadFactor);
                    else tempHashTable = new HashTablePAF<>(collisionChoice, loadFactor); 

                    // En alakali makaleyi bul
                    // Kelimeye ait makale frekans tablosunu al
                    tempHashTable = wordArticleCountMap.get(input);
                    LinkedList<String> keySet = tempHashTable.keySet();
                    String relevantArticleID = "";
                    int maxFrequency = -1;
                    for(String key : keySet){
                        int frequency = tempHashTable.get(key);
                        if(frequency > maxFrequency){
                            maxFrequency = frequency;
                            relevantArticleID = key;
                        }
                    }
                    // En alakali makaleyi yazdir
                    LinkedList<String> Article = new LinkedList<>();
                    Article = IDhashTable.get(relevantArticleID);
                    Article.poll();
                    System.out.println(Article);
                }
            }else if (input.equals("2")){ 
                System.out.print("Enter the 10-digit article ID number: "); // Makale ID'si isteniyor
                input = scn.next();
                System.out.println();
                if(IDhashTable.containsKey(input)){ // Makale ID'si var mi kontrol et
                    // Varsa makaleyi yazdir
                    LinkedList<String> Article = new LinkedList<>();
                    Article = IDhashTable.get(input);
                    Article.poll();
                    System.out.println(Article);
                }else{
                    System.out.println("The Article ID you entered could not be found.");
                }
            }else{
                System.out.println("An unknown input was entered. Please enter it again.");
            }
            System.out.println();
        }
        scn.close();
    }// end main
}// end class 