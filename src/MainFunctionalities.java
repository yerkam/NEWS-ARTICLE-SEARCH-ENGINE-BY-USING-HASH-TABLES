import java.io.IOException;
import java.util.LinkedList;

abstract class MainFunctionalities {
    
    /**
     * Makaleleri yükle
     * @param loadFactor // yükleme faktörü
     * @param hashTableChoice // true -> SSF, false -> PAF
     * @param collisionChoice // true -> DH, false -> LP
     * @return // Makalelerin ID'si anahtarda ve makale metinlerinin bağlı listesi değerde olan hash tablosu
     * @throws IOException
     */
    static public HashTableInterface<String, LinkedList<String>> loadArticles(double loadFactor, boolean hashTableChoice, boolean collisionChoice)throws IOException{
        HashTableInterface<String, LinkedList<String>> articleCache; // Makaleleri saklamak için hash tablosu
        Reader Reader = new Reader();
        if(hashTableChoice){
            articleCache = new HashTableSSF<>(collisionChoice, loadFactor); // true -> DH, false -> LP
        }else{
            articleCache = new HashTablePAF<>(collisionChoice, loadFactor); // true -> DH, false -> LP
        }
        Reader.loadArticles("CNN_Articels.csv", articleCache); // Makaleleri yükle
        return articleCache;
    }

    /**
     * Arama motoru ile makaleleri bul
     * @param loadFactor // yükleme faktörü
     * @param hashTableChoice // true -> SSF, false -> PAF
     * @param collisionChoice // true -> DH, false -> LP
     * @return // Kelimeleri anahtarda ve her kelime için makale ID'leri ve frekanslarının bulunduğu hash tablosu değerde olan hash tablosu
     * @throws IOException
     * @throws InterruptedException
     */
    static public HashTableInterface<String, HashTableInterface<String, Integer>> findArticleWithSearchEngine(double loadFactor, boolean hashTableChoice, boolean collisionChoice) throws IOException, InterruptedException{
        HashTableInterface<String, HashTableInterface<String, Integer>> indexMap; // Kelimeleri ve makale frekanslarını saklamak için hash tablosu
        Reader Reader = new Reader();
        if(hashTableChoice){
            indexMap = new HashTableSSF<>(collisionChoice, loadFactor); // true -> DH, false -> LP
        }else{
            indexMap = new HashTablePAF<>(collisionChoice, loadFactor); // true -> DH, false -> LP
        }
        Reader.computeWordFrequencyTable(indexMap, loadFactor, "CNN_Articels.csv", "stop_words_en.txt", hashTableChoice, collisionChoice); // Kelime frekans tablosunu hesapla
        return indexMap;
    }

    // Aynı işlevi yerine getirir ancak belli dosyada bulunan kelimeler için arama yapar (performans matrisi için)
    static public HashTableInterface<String, HashTableInterface<String, Integer>> findArticleWithSearchEngineForPerformanceMatrix(double loadFactor, boolean hashTableChoice, boolean collisionChoice) throws IOException, InterruptedException{
        HashTableInterface<String, HashTableInterface<String, Integer>> indexMap; // Kelimeleri ve makale frekanslarını saklamak için hash tablosu
        Reader Reader = new Reader();
        if(hashTableChoice){
            indexMap = new HashTableSSF<>(collisionChoice, loadFactor); // true -> DH, false -> LP
        }else{
            indexMap = new HashTablePAF<>(collisionChoice, loadFactor); // true -> DH, false -> LP
        }
        Reader.computeWordFrequencyTable(indexMap, loadFactor, "CNN_Articels.csv","search.txt", "stop_words_en.txt", hashTableChoice, collisionChoice); // Kelime frekans tablosunu hesapla
        return indexMap;
    }
}