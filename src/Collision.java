abstract class Collision<K, V> {
    public int linearProbing(K key, int index, int hashSize, HashEntry[] table){
        // İlk çağrıda: linearProbing(key, hashFunction(key), hashSize, table)
        while (table[index] != null) {
            index = (index + 1) % hashSize;
        }
        return index;
    }
  
    public int doubleHashing(K key, int initialHash, int hashSize, HashEntry[] table, int PreviousPrime) {
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
