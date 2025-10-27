abstract class Collision {
    public int linearProbing(String key, int index, int hashSize, HashEntry[] table){
        // İlk çağrıda: linearProbing(key, hashFunction(key))
        if (table[index] == null) {
            return index;
        }
        return linearProbing(key, (index + 1) % hashSize, hashSize, table);
    }
  
    public int doubleHashing(String key, int initialHash, int hashSize, HashEntry[] table, int PreviousPrime) {
        // İlk çağrıda: doubleHashing(key, hashFunction(key))
        int q = PreviousPrime;
        int h = initialHash;
        int d = q - (initialHash % q);
        
        while(table[h] != null){
        h = (h + d) % hashSize;
        }
        return h;
    } 
}
