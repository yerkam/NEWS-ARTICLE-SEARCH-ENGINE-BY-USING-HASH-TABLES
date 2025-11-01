import java.io.IOException;

public class Main extends MainFunctionalities {
    public static void main(String[] args) throws IOException, InterruptedException{
        double loadFactor = 0.5;
        boolean hashTableChoice = true; // false -> PAF, true -> SSF
        boolean collisionChoice = false; // false -> LP, true -> DH
        HashTableInterface<String, HashTableInterface<String, Integer>> HashTable;
        if(hashTableChoice){
            HashTable = new HashTableSSF<>(collisionChoice);
        }else{
            HashTable = new HashTablePAF<>(collisionChoice); 
        }
        
        HashTable = findArticleWithSearchEngine(loadFactor, hashTableChoice, collisionChoice);

        System.out.print(HashTable.getCollisionCount());
        
    }// end main
}// end class 