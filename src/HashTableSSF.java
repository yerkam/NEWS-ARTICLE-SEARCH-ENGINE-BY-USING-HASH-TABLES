import java.util.*;

public class HashTableSSF<K, V> extends Collision<K, V> implements HashTableInterface<K, V> {
	
    private int hashSize = 1009; // Asal sayı boyutu
    private HashEntry<K, V>[] table;
    private boolean collision;
    private int collisionCount = 0;
    private double loadFactor;
    private int currentSize = 0;

    @SuppressWarnings("unchecked")
    public HashTableSSF(boolean collisionChoice, double loadFactor) {
          table = (HashEntry<K, V>[])new HashEntry[hashSize];
          for (int i = 0; i < hashSize; i++)
                table[i] = null;
          this.collision = collisionChoice; // false -> LP, true -> DH
          this.loadFactor = loadFactor;
    }

    @Override
    public V get(K key) {
        int hash = hashFunction(key);
        int startIndex = hash; // Başlangıç indeksini sakla
        
        // İlk pozisyonu kontrol et
        if (table[hash] != null && table[hash].getKey().equals(key)) {
            return table[hash].getValue();
        } // Başlangıç pozisyonunda değilse

        // Collision varsa probing ile ara
        if (!collision) {
            // Linear Probing ile ara
            hash = (hash + 1) % hashSize;
            while (hash != startIndex && table[hash] != null) {
                if (table[hash].getKey().equals(key)) {
                    return table[hash].getValue();
                }
                hash = (hash + 1) % hashSize;
            }
        } else {
            // Double Hashing ile ara
            int q = getPreviousPrime(hashSize);
            int d = q - (startIndex % q);
            hash = (startIndex + d) % hashSize;
            
            while (hash != startIndex && table[hash] != null) {
                if (table[hash].getKey().equals(key)) {
                    return table[hash].getValue();
                }
                hash = (hash + d) % hashSize;
            }
        }
        return null; // Key bulunamadı
    }

    @Override
    public void put(K key, V value) {
      if(size() >= hashSize * loadFactor) resize(); // load factor'a göre resize et 
      int index = hashFunction(key);
      if (table[index] != null && !table[index].getKey().equals(key)) { // Collision durumu ile yeni pozisyon ara
        collisionCount++;
        if(!collision) {
          index = linearProbing(key, index, hashSize, table);
        } else{
          index = doubleHashing(key, index, hashSize, table, getPreviousPrime(hashSize));
        }
      }else if (table[index] != null && table[index].getKey().equals(key)) { // Key zaten varsa, değeri güncelle ve methodu bitir
          table[index].setValue(value);
          return;
      }
      // Bulunan boş pozisyona ekle
      table[index] = new HashEntry<>(key, value);
      currentSize++;
    }

    @Override
    public int size() { 
      return currentSize;
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
        int startIndex = hash; // Başlangıç indeksini sakla
        
        // İlk pozisyonu kontrol et
        if (table[hash] != null && table[hash].getKey().equals(key)) {
            return true;
        } // Başlangıç pozisyonunda değilse
        
        // Collision varsa probing ile ara
        if (!collision) {
            // Linear Probing ile ara
            hash = (hash + 1) % hashSize;
            while (hash != startIndex && table[hash] != null) {
                if (table[hash].getKey().equals(key)) {
                    return true;
                }
                hash = (hash + 1) % hashSize;
            }
        } else {
            // Double Hashing ile ara
            int q = getPreviousPrime(hashSize);
            int d = q - (startIndex % q);
            hash = (startIndex + d) % hashSize;
            
            while (hash != startIndex && table[hash] != null) {
                if (table[hash].getKey().equals(key)) {
                    return true;
                }
                hash = (hash + d) % hashSize;
            }
        }
        return false; // Key bulunamadı
    }

    @Override
    public V remove(K key) {
        int hash = hashFunction(key);
        int startIndex = hash; // Başlangıç indeksini sakla
        
        // İlk pozisyonu kontrol et
        if (table[hash] != null && table[hash].getKey().equals(key)) {
            V value = table[hash].getValue();
            table[hash] = null;
            return value;
        } // Başlangıç pozisyonunda değilse
        
        // Collision varsa probing ile ara
        if (!collision) {
            // Linear Probing ile ara
            hash = (hash + 1) % hashSize;
            while (hash != startIndex && table[hash] != null) {
                if (table[hash].getKey().equals(key)) {
                    V value = table[hash].getValue();
                    table[hash] = null;
                    return value;
                }
                hash = (hash + 1) % hashSize;
            }
        } else {
            // Double Hashing ile ara
            int q = getPreviousPrime(hashSize);
            int d = q - (startIndex % q);
            hash = (startIndex + d) % hashSize;
            
            while (hash != startIndex && table[hash] != null) {
                if (table[hash].getKey().equals(key)) {
                    V value = table[hash].getValue();
                    table[hash] = null;
                    return value;
                }
                hash = (hash + d) % hashSize;
            }
        }
        
        System.out.println("Key not found: " + key);
        return null; // Key bulunamadı
    }

    @Override
    public int hashFunction(K key) {
        String keyStr = key.toString(); // K'yi String'e çevir
        int hash = 0; // index
        
        for (int i = 0; i < keyStr.length(); i++) {
            hash += keyStr.charAt(i); // ASCII değerlerini topla
        }
        
        return Math.abs(hash) % hashSize; 
    }

    @Override
    @SuppressWarnings("unchecked")
    public void resize() {
        HashEntry<K, V>[] oldTable = table;  // Eski tabloyu sakla
        int oldCapacity = hashSize;
        
        hashSize = getNextPrime(hashSize * 2); // Yeni boyutu belirle
        table = (HashEntry<K, V>[]) new HashEntry[hashSize];  // Cast
        currentSize = 0; // Yeni tablo için eleman sayısını sıfırla
        
        for (int i = 0; i < oldCapacity; i++) { // Eski tablodaki elemanları yeni tabloya yeni indexleriyle ekle
            HashEntry<K, V> entry = oldTable[i];  
            if (entry != null) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void clear(){
        for (int i = 0; i < hashSize; i++) 
                table[i] = null;
    }

    @Override
    public int getCollisionCount(){
    return collisionCount;
  }

    @Override
    public LinkedList<K> keySet() {
        LinkedList<K> keys = new LinkedList<K>();
        for (HashEntry<K, V> entry : table) {
            if (entry != null) {
                keys.push(entry.getKey());
            }
        }
        return keys;
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
    
    private boolean isPrime(int n) { // Took from GeeksforGeeks (https://www.geeksforgeeks.org/java/java-prime-number-program/)
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        
        for (int i = 5; i <= Math.sqrt(n); i = i + 6) {
            if (n % i == 0 || n % (i + 2) == 0)
                return false;
        }
        return true;
    }
}
