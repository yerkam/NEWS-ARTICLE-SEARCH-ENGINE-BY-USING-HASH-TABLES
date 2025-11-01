public class HashTableSSF<K, V> extends Collision implements HashTableInterface<K, V> {
	
    private int hashSize = 1009; // Asal sayı boyutu
    private HashEntry<K, V>[] table;
    private boolean collision;
    private int collisionCount = 0;

    @SuppressWarnings("unchecked")
    public HashTableSSF(boolean collisionChoice) {
          table = (HashEntry<K, V>[])new HashEntry[hashSize];
          for (int i = 0; i < hashSize; i++)
                table[i] = null;
          this.collision = collisionChoice; // false -> LP, true -> DH
    }

    @Override
    public V get(K key) {
        int hash = hashFunction(key);
        if (table[hash] == null){
            System.out.println("Key not found: " + key);
            return null;
        } else
            return table[hash].getValue();  // Bu artık V tipinde dönecek
    }

    @SuppressWarnings("unchecked")
    @Override
    public void put(K key, V value) {
      if(isFull()) resize(); // If is it full, resize the table
      int index = hashFunction(key);
      if (table[index] != null) {
        collisionCount++;
        if(collision) {
          index = linearProbing(key, index, hashSize, table);
        } else{
          index = doubleHashing(key, index, hashSize, table, getPreviousPrime(hashSize));
        }
      }
      table[index] = new HashEntry(key, value);
      //System.out.println("Inserted key: " + key + " at index: " + index);
    }

    @Override
    public int size() { 
      int count = 0;
      for (HashEntry entry : table) {
          if (entry != null) {
              count++;
          }
      }
      return count;
    }

    @Override
    public boolean isEmpty() {
      return size() == 0;
    }

    @Override
    public boolean isFull(){
        return size() == hashSize;
    }

    @Override
    public boolean containsKey(K key) {
      int hash = hashFunction(key);
      return table[hash] != null && table[hash].getKey().equals(key);
    }

    @Override
    public V remove(K key) {
      int hash = hashFunction(key);
      if (table[hash] != null && table[hash].getKey().equals(key)){
          V value = table[hash].getValue();
          table[hash] = null; // Sil
          return value;
      } else {
          System.out.println("Key not found: " + key);
          return null; // Key bulunamadı
      }
    }

    @Override
    public int hashFunction(K key) {
        String keyStr = key.toString(); // K'yi String'e çevir
        int hash = 0;
        
        for (int i = 0; i < keyStr.length(); i++) {
            hash += keyStr.charAt(i);
        }
        
        return Math.abs(hash) % hashSize;
    }

    @Override
    public void resize() {
        HashEntry<K, V>[] oldTable = table;  // Generic ekleyin
        int oldCapacity = hashSize;
        
        hashSize = getNextPrime(hashSize * 2);
        table = (HashEntry<K, V>[]) new HashEntry[hashSize];  // Cast
        
        for (int i = 0; i < oldCapacity; i++) {
            HashEntry<K, V> entry = oldTable[i];  // Generic ekleyin
            if (entry != null && !entry.isDeleted()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void clear(){
        for (int i = 0; i < hashSize; i++) 
                table[i] = null;
    }

    private int getNextPrime(int n) {
        while (!isPrime(n)) {
            n++;
        }
        return n;
    }

    private int getPreviousPrime(int n) {
        n--;
        while (n > 1 && !isPrime(n)) {
            n--;
        }
        return n;
    }
    
    private boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0)
                return false;
        }
        return true;
    }

    @Override
    public int getCollisionCount(){
    return collisionCount;
  }
}
