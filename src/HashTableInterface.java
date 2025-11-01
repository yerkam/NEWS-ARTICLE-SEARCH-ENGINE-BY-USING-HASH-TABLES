public interface HashTableInterface<K, V> {
    /**
     * Verilen key ile değer ekler
     * @param key anahtar
     * @param value değer
     */
    void put(K key, V value);
    
    /**
     * Verilen key ile değeri getirir
     * @param key anahtar
     * @return bulunan değer, yoksa null
     */
    V get(K key);

    /**
     * Hash table'ın boyutunu döndürür
     * @return tablodaki eleman sayısı
     */
    int size();

    /**
     * Hash table'ın boş olup olmadığını kontrol eder
     * @return tablo boşsa true
     */
    boolean isEmpty();

    /**
     * Hash table'ın dolu olup olmadığını kontrol eder
     * @return tablo doluysa true
     */
    boolean isFull();

    /**
     * Verilen key'in var olup olmadığını kontrol eder
     * @param key kontrol edilecek anahtar
     * @return key varsa true, yoksa false
     */
    boolean containsKey(K key);

    /**
     * Verilen key'i hash table'dan siler
     * @param key silinecek anahtar
     * @return silinen değer, key yoksa null
     */
    V remove(K key);

    /**
     * Hash fonksiyonu
     * @param key anahtar
     * @return hash değeri
     */
    int hashFunction(K key);

    /**
     * Tabloyu yeniden boyutlandırır
     * @return Yeniden boyutlandırılmış tablo
     */
    void resize();

    /**
     * Hash'i komple temizler
     * @return Temizlenmiş Hash
     */
    void clear();

    /**
     * @return kac tane collision olduğunu döndürür
     */
    int getCollisionCount();
}