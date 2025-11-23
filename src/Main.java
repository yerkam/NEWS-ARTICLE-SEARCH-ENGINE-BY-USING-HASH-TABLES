import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main extends MainFunctionalities {
    public static void main(String[] args) throws IOException, InterruptedException{
        
        performanceMatrix();
        
        /* 
        double loadFactor = 0.5;
        boolean hashTableChoice = false; // true -> SSF, false -> PAF
        boolean collisionChoice = false; // true -> DH, false -> LP
        
        // Zaman hesaplama
        long startTime = System.currentTimeMillis();
        
        
        
        HashTableInterface<String, HashTableInterface<String, Integer>> wordArticleCountMap;
        HashTableInterface<String, LinkedList<String>> IDhashTable;
        if(hashTableChoice){
            IDhashTable = new HashTableSSF<>(collisionChoice, loadFactor);
            wordArticleCountMap = new HashTableSSF<>(collisionChoice, loadFactor);
        }else{
            IDhashTable = new HashTablePAF<>(collisionChoice, loadFactor);
            wordArticleCountMap = new HashTablePAF<>(collisionChoice, loadFactor); 
        }
        System.out.println("Files are loading, please wait...");
        IDhashTable = loadArticles(loadFactor, hashTableChoice, collisionChoice);
        wordArticleCountMap = findArticleWithSearchEngine(loadFactor, hashTableChoice, collisionChoice);
        System.out.println("Files loaded successfully.");

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime/1000);
        System.out.println();


        Thread.sleep(1000); // Pause for 1 second


        String input = "";
        Scanner scn = new Scanner(System.in);
        // Kullanıcı arayüzü
        while(!input.equals("9")){
            System.out.println("1 - Choose one of the searched words and find out which article is most relevant.");
            System.out.println("2 - Find Article by Article ID number.");
            
            System.out.println("9 - Exit the program.");
            System.out.print("Enter the number of the transaction you want to perform: ");
            System.out.println();
            input = scn.next();
            input = input.toLowerCase();
            if(input.equals("1")){
                System.out.print("Write the word you want to search: "); // Aranacak kelime isteniyor
                input = scn.next();
                System.out.println();
                if(!wordArticleCountMap.containsKey(input)) System.out.println("No word was selected from the search word list."); // Kelime var mı kontrol et
                else{
                    HashTableInterface<String, Integer> tempHashTable; // Makale frekans tablosu

                    if(hashTableChoice) tempHashTable = new HashTableSSF<>(collisionChoice, loadFactor);
                    else tempHashTable = new HashTablePAF<>(collisionChoice, loadFactor); 

                    // En alakalı makaleyi bul
                    // Kelimeye ait makale frekans tablosunu al
                    tempHashTable = wordArticleCountMap.get(input); // Kelimeye ait makale frekans tablosunu al
                    LinkedList<String> keySet = tempHashTable.keySet(); // Makale ID'lerini al
                    String relevantArticleID = "";
                    int maxFrequency = -1;
                    for(String key : keySet){ // Her makale ID'si için
                        int frequency = tempHashTable.get(key); // Makaledeki kelime frekansını al
                        if(frequency > maxFrequency){ // En yüksek frekansı güncelle
                            maxFrequency = frequency; 
                            relevantArticleID = key; // En alakalı makale ID'sini güncelle
                        }else if(frequency == maxFrequency){
                            // Eger frekanslar eşit ise durumu kontrol et
                            // relevantArticleID ve key'deki makalelerin ID'lerine göre karşılaştırma yap
                        }
                    }
                    // En alakalı makaleyi yazdır
                    LinkedList<String> Article = new LinkedList<>(); 
                    Article = IDhashTable.get(relevantArticleID);
                    Article.pop(); 
                    Article.addFirst(relevantArticleID); 
                    System.out.println(Article);
                }
            }else if (input.equals("2")){ 
                System.out.print("Enter the 10-digit article ID number: "); // Makale ID'si isteniyor
                input = scn.next();
                System.out.println();
                if(IDhashTable.containsKey(input)){ // Makale ID'si var mı kontrol et
                    // Varsa makaleyi yazdır
                    LinkedList<String> Article = new LinkedList<>();
                    Article = IDhashTable.get(input);
                    Article.poll();
                    System.out.println(Article);
                }else{
                    System.out.println("The Article ID you entered could not be found.");
                }
            }else if(input.equals("9")){
                System.out.println("Exiting the program...");
            }else{
                System.out.println("An unknown input was entered. Please enter it again.");
            }
            System.out.println();
        }
        scn.close();*/
    }// end main
    
    static public void performanceMatrix(){
        System.out.print("Load Factor   Hash Function   Collision Handling   Collision Count   Indexing Time(s)   Average Searching Time(ns)\n");
        System.out.print("-----------   -------------   ------------------   ---------------   ----------------   --------------------------\n");
        double[] loadFactors = {0.5, 0.8};
        boolean[] hashTableChoices = {true, false}; // true -> SSF, false -> PAF
        boolean[] collisionChoices = {false, true}; // true -> DH, false -> LP
        for(double loadFactor : loadFactors){
            for(boolean hashTableChoice : hashTableChoices){
                for(boolean collisionChoice : collisionChoices){
                    try{
                        // Indexing süresi ölçümü
                        long startTime = System.currentTimeMillis();
                        HashTableInterface<String, HashTableInterface<String, Integer>> wordArticleCountMap = findArticleWithSearchEngineForPerformanceMatrix(loadFactor, hashTableChoice, collisionChoice);
                        long endTime = System.currentTimeMillis();
                        long indexingTime = endTime - startTime;

                        // Ortalama arama süresi ölçümü
                        startTime = System.nanoTime();
                        for(String word : wordArticleCountMap.keySet()){
                            HashTableInterface<String, Integer> tempHashTable = wordArticleCountMap.get(word);
                        }
                        endTime = System.nanoTime();
                        long searchingTime = endTime - startTime;
                        searchingTime /= wordArticleCountMap.size(); // Ortalama arama süresi

                        // Collision sayısı ölçümü
                        int collisionCount = wordArticleCountMap.getCollisionCount(); // Ana tablo çarpışma sayısı
                        for(String word : wordArticleCountMap.keySet()){ // Alt tabloların çarpışma sayısını ekle
                            HashTableInterface<String, Integer> tempHashTable = wordArticleCountMap.get(word);
                            collisionCount += tempHashTable.getCollisionCount();
                        }

                        // Sonuçları yazdır
                        System.out.printf("   %.1f             %s                %s                %d             %.2f                   %d\n",
                                loadFactor,
                                hashTableChoice ? "SSF" : "PAF",
                                collisionChoice ? "DH" : "LP",
                                collisionCount,
                                indexingTime / 1000.0,
                                searchingTime);
                    }catch(IOException | InterruptedException e){
                        e.printStackTrace();
                    }
                } // for collisionChoices
            } // for hashTableChoices
        } // for loadFactors
    }// end performanceMatrix
}// end class 