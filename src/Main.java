import java.io.IOException;

public class Main extends MainFunctionalities {
    public static void main(String[] args) throws IOException, InterruptedException{
        long startTime = System.currentTimeMillis();
        double loadFactor = 0.05;
        boolean hashTableChoice = true; // false -> PAF, true -> SSF
        boolean collisionChoice = false; // false -> LP, true -> DH
        HashTableInterface<String, HashTableInterface<String, Integer>> HashTable;
        if(hashTableChoice){
            HashTable = new HashTableSSF<>(collisionChoice);
        }else{
            HashTable = new HashTablePAF<>(collisionChoice); 
        }
        
        HashTable = findArticleWithSearchEngine(loadFactor, hashTableChoice, collisionChoice);

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime/1000);
        System.out.print(HashTable.getCollisionCount());
        
    }// end main
}// end class 