import java.io.IOException;
import java.util.LinkedList;

abstract class MainFunctionalities {
    
    static public HashTableInterface<String, LinkedList<String>> loadArticles(double loadFactor, boolean hashTableChoice, boolean collisionChoice)throws IOException{
        HashTableInterface<String, LinkedList<String>> articleCache;
        Reader Reader = new Reader();
        if(hashTableChoice){
            articleCache = new HashTableSSF<>(collisionChoice, loadFactor); // false -> LP, true -> DH
        }else{
            articleCache = new HashTablePAF<>(collisionChoice, loadFactor); // false -> LP, true -> DH
        }
        Reader.loadArticles("CNN_Articels.csv", articleCache); // Makaleleri yükle
        return articleCache;
    }

    static public HashTableInterface<String, HashTableInterface<String, Integer>> findArticleWithSearchEngine(double loadFactor, boolean hashTableChoice, boolean collisionChoice) throws IOException, InterruptedException{
        HashTableInterface<String, HashTableInterface<String, Integer>> indexMap;
        Reader Reader = new Reader();
        if(hashTableChoice){
            indexMap = new HashTableSSF<>(collisionChoice, loadFactor); // false -> LP, true -> DH
        }else{
            indexMap = new HashTablePAF<>(collisionChoice, loadFactor); // false -> LP, true -> DH
        }
        Reader.computeWordFrequencyTable(indexMap, loadFactor, "CNN_Articels.csv", "search.txt", "stop_words_en.txt", hashTableChoice, collisionChoice);
        return indexMap;
    }
}