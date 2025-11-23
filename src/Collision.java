abstract class Collision<K, V> {
    /**
     * Linear Probing collision çözümleme yöntemi.
     * @param key // Aranacak anahtar
     * @param index // Başlangıç indeksi
     * @param hashSize // Hash tablosunun boyutu
     * @param table // Hash tablosu
     * @return // Anahtarın yerleştirilebileceği boş indeks
     */
    public int linearProbing(K key, int index, int hashSize, HashEntry<K, V>[] table){
        // İlk çağrıda: linearProbing(key, hashFunction(key), hashSize, table)
        while (table[index] != null) {
            index = (index + 1) % hashSize;
        }
        return index;
    }
  
    /**
     * Double Hashing collision çözümleme yöntemi.
     * @param key // Aranacak anahtar
     * @param initialHash // Başlangıç indeksi
     * @param hashSize // Hash tablosunun boyutu
     * @param table // Hash tablosu
     * @param PreviousPrime // Hash tablosunun boyutundan küçük en büyük asal sayı
     * @return // Anahtarın yerleştirilebileceği boş indeks
     */
    public int doubleHashing(K key, int initialHash, int hashSize, HashEntry<K, V>[] table, int PreviousPrime) {
        // İlk çağrıda: doubleHashing(key, hashFunction(key), hashSize, table, getPreviousPrime(hashSize))
        int h = initialHash;
        int d = PreviousPrime - (initialHash % PreviousPrime);
        
        if(d == 0) d = 1; // initialHash % q == q olursa
        
        while(table[h] != null){
            h = (h + d) % hashSize;
        }
        return h;
    } 
}
