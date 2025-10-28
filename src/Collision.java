abstract class Collision<K, V> {
    public int linearProbing(K key, int index, int hashSize, HashEntry[] table){
        // İlk çağrıda: linearProbing(key, hashFunction(key), hashSize, table)
        if (table[index] == null) {
            return index;
        }
        return linearProbing(key, (index + 1) % hashSize, hashSize, table);
    }
  
    public int doubleHashing(K key, int initialHash, int hashSize, HashEntry[] table, int PreviousPrime) {
        // İlk çağrıda: doubleHashing(key, hashFunction(key), hashSize, table, getPreviousPrime(hashSize))
        int q = PreviousPrime;
        int h = initialHash;
        int d = q - (initialHash % q);
        
        while(table[h] != null){
        h = (h + d) % hashSize;
        }
        return h;
    } 
}
