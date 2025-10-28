import java.io.IOException;
import java.util.List;

abstract class MainFunctionalities {
    
    public List<String> findArticleWithID(String ID,double loadFactor, boolean hashTableChoice, boolean collisionChoice) throws IOException { // Hızlı arama için hash table kullan
        HashTableInterface<String, List<String>> articleCache;
        Reader Reader = new Reader();
        if(hashTableChoice){
            articleCache = new HashTableSSF<>(collisionChoice); // false -> LP, true -> DH
        }else{
            articleCache = new HashTablePAF<>(collisionChoice); // false -> LP, true -> DH
        }
        Reader.loadArticles("articles.csv", loadFactor, articleCache); // Makaleleri yükle
        List<String> value = articleCache.get(ID);
        return value;
    }

    public List<String> findArticleWithSearchEngine(double loadFactor, boolean hashTableChoice, boolean collisionChoice) throws IOException{
        HashTableInterface<String, HashTableInterface<String, Integer>> indexMap;
        Reader Reader = new Reader();
        if(hashTableChoice){
            indexMap = new HashTableSSF<>(collisionChoice); // false -> LP, true -> DH
        }else{
            indexMap = new HashTablePAF<>(collisionChoice); // false -> LP, true -> DH
        }
        Reader.indexFileLines("search.txt", indexMap); // İndeks haritasını yükle
        


        return null;
    }
}