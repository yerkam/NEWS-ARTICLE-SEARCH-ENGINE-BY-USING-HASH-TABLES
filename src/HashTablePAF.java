public class HashTablePAF implements HashTableInterface<String, Object>
{
    private int hashSize = 10007; // Asal sayı boyutu
    HashEntry[] table;

    public HashTablePAF() {
          table = new HashEntry[hashSize];
          for (int i = 0; i < hashSize; i++)
                table[i] = null;
    }

    @Override
    public Object get(String key) {
    	throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public void put(String key, Object value) {
      int index = hashFunction(key);
      table[index] = new HashEntry(key, value);
    }

    @Override
    public int size() {
      throw new UnsupportedOperationException("Unimplemented method 'size'");
    }

    @Override
    public boolean isEmpty() {
      throw new UnsupportedOperationException("Unimplemented method 'isEmpty'");
    }

    @Override
    public boolean containsKey(String key) {
      throw new UnsupportedOperationException("Unimplemented method 'containsKey'");
    }

    @Override
    public Object remove(String key) {
      throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public int hashFunction(String key) {
      throw new UnsupportedOperationException("Unimplemented method 'hashFunction'");
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
     }

     private int getNextPrime(int n) {
        while (!isPrime(n)) {
            n++;
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
