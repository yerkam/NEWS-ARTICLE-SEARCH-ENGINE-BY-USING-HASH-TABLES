public class HashTableSSF extends Collision implements HashTableInterface<String, Object>{
	
    private int hashSize = 10007; // Asal sayı boyutu
    HashEntry[] table;
    boolean collision;

    public HashTableSSF(boolean collisionChoice) {
          table = new HashEntry[hashSize];
          for (int i = 0; i < hashSize; i++)
                table[i] = null;
          this.collision = collisionChoice; // false -> LP, true -> DH
    }

    @Override
    public Object get(String key) {
    	int hash = hashFunction(key);
        if (table[hash] == null){
            System.out.println("Key not found: " + key);
            return null;
        } else
            return table[hash].getValue();
    }

    @Override
    public void put(String key, Object value) {
      int index = hashFunction(key);
      if (table[index] != null) {
        if(collision) {
          index = linearProbing(key, index, hashSize, table);
        } else{
          index = doubleHashing(key, index, hashSize, table, getPreviousPrime(hashSize));
        }
      }
      table[index] = new HashEntry(key, value);
      System.out.println("Inserted key: " + key + " at index: " + index);
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
    public boolean containsKey(String key) {
      int hash = hashFunction(key);
      return table[hash] != null && table[hash].getKey().equals(key);
    }

    @Override
    public Object remove(String key) {
      int hash = hashFunction(key);
      if (table[hash] != null && table[hash].getKey().equals(key)){
          Object value = table[hash].getValue();
          table[hash] = null; // Sil
          return value;
      } else {
          System.out.println("Key not found: " + key);
          return null; // Key bulunamadı
      }
    }

    @Override
    public int hashFunction(String key) {
      int hash = 0;
      char[] charArray = key.toCharArray();
      for (char c : charArray) {
          hash += (int) c;
      }
      return hash % hashSize; 
    }

    @Override
    public void resize() {
      // Eski tabloyu sakla
      int capacity = hashSize;
      
        HashEntry[] oldTable = table;
        int oldCapacity = capacity;
        
        // Yeni boyutu hesapla (2 katı + asal sayı)
        capacity = getNextPrime(capacity * 2);
        
        table = new HashEntry[capacity];
        
        // Eski elementleri yeni tabloya ekle (rehashing)
        for (int i = 0; i < oldCapacity; i++) {
            HashEntry entry = oldTable[i];
            if (entry != null && !entry.isDeleted()) {  // LP/DH için
                put(entry.getKey(), entry.getValue());  // Yeni hash ile ekle
            }
        }
        hashSize = capacity;
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

    
}
