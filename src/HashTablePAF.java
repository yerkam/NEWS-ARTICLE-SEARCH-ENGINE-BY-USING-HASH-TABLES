import java.util.LinkedList;

public class HashTablePAF<K, V> extends Collision<K, V> implements HashTableInterface<K, V> {
  private int hashSize = 1009; // Asal sayı boyutu
  private static final int Z = 33; // PAF için polinom katsayısı çarpan
  private boolean collision; // collision handling yöntemi
  private HashEntry<K, V>[] table; // Hash tablosu
  private int collisionCount = 0; // çarpışma sayacı
  private double loadFactor; // yük faktörü
  private int currentSize = 0; // mevcut eleman sayısı

  @SuppressWarnings("unchecked")
  public HashTablePAF(boolean collisionChoice, double loadFactor) {
    table = (HashEntry<K, V>[]) new HashEntry[hashSize];
    for (int i = 0; i < hashSize; i++)
      table[i] = null;
    this.collision = collisionChoice; // false -> LP, true -> DH
    this.loadFactor = loadFactor;
  }

  @Override
  public V get(K key) {
    int hash = hashFunction(key);
    int startIndex = hash; // başangıç indeksini sakla

    // İlk pozisyonu kontrol et
    if (table[hash] != null && table[hash].getKey().equals(key)) {
      return table[hash].getValue();
    }

    // Collision varsa probing ile ara
    if (collision) {
      // Double Hashing
      int q = getPreviousPrime(hashSize);
      int d = q - (startIndex % q); // İkincil hash fonksiyonu
      hash = (startIndex + d) % hashSize; // adım adım ilerle

      while (hash != startIndex && table[hash] != null) {
        if (table[hash].getKey().equals(key)) {
          return table[hash].getValue();
        }
        hash = (hash + d) % hashSize;
      }
    } else {
      // Linear Probing
      hash = (hash + 1) % hashSize;
      while (hash != startIndex && table[hash] != null) {
        if (table[hash].getKey().equals(key)) {
          return table[hash].getValue();
        }
        hash = (hash + 1) % hashSize;
      
      }
    }
    return null;
  }

  @Override
  public void put(K key, V value) {


    if (size() >= hashSize * loadFactor)
      resize(); // load faktörü dolunca resize et


   

    int index = hashFunction(key);
    if (table[index] != null && !table[index].getKey().equals(key)) {
      collisionCount++;
      if (collision) { // Linear Probing veya Double Hashing ile yeni pozisyon ara
        index = doubleHashing(key, index, hashSize, table, getPreviousPrime(hashSize));
      } else {
        index = linearProbing(key, index, hashSize, table);
      }
    } else if (table[index] != null && table[index].getKey().equals(key)) {
      // Key zaten varsa, değeri güncelle
      table[index].setValue(value);
      return;
    }
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
  public boolean isFull() {
    return size() == hashSize;
  }

  @Override
  public boolean containsKey(K key) {
    int hash = hashFunction(key);
    int startIndex = hash;

    // İlk pozisyonu kontrol et
    if (table[hash] != null && table[hash].getKey().equals(key)) {
      return true;
    }

    // Collision varsa probing ile ara
    if (collision) {
      // Double Hashing
      int q = getPreviousPrime(hashSize);
      int d = q - (startIndex % q);
      hash = (startIndex + d) % hashSize;

      while (hash != startIndex && table[hash] != null) {
        if (table[hash].getKey().equals(key)) {
          return true;
        }
        hash = (hash + d) % hashSize;
      }
    } else {
      // Linear Probing
      hash = (hash + 1) % hashSize;
      while (hash != startIndex && table[hash] != null) {
        if (table[hash].getKey().equals(key)) {
          return true;
        }
        hash = (hash + 1) % hashSize;
      
      }
    }
    return false;
  }

  @Override
  public V remove(K key) {
    int hash = hashFunction(key);
    int startIndex = hash;

    // İlk pozisyonu kontrol et
    if (table[hash] != null && table[hash].getKey().equals(key)) {
      V value = table[hash].getValue();
      table[hash] = null;
      return value;
    }

    // Collision varsa probing ile ara
    if (collision) {
      // Double Hashing
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
     
    } else {
       // Linear Probing
      hash = (hash + 1) % hashSize;
      while (hash != startIndex && table[hash] != null) {
        if (table[hash].getKey().equals(key)) {
          V value = table[hash].getValue();
          table[hash] = null;
          return value;
        }
        hash = (hash + 1) % hashSize;
      }
      
    }

    System.out.println("Key not found: " + key);
    return null;
  }

  @Override
  public int hashFunction(K key) {
    
    // Horner's rule kullanarak overflow'u önleme
    // h(s) = ch0*z^(n-1) + ch1*z^(n-2) + ... + ch(n-1)*z^0 hash paf fonksiyonu

    String keyStr = key.toString();

    if (keyStr == null || keyStr.isEmpty()) {
      return 0;
    }

    keyStr = keyStr.toLowerCase(); // Case insensitive
    int hash = 0;

    // Horner's rule: her adımda modulus al
    for (int i = 0; i < keyStr.length(); i++) {
      char c = keyStr.charAt(i);
      int charValue = getCharValue(c);

      // Horner's rule: hash = (hash * Z + charValue) % hashSize
      hash = (hash * Z + charValue) % hashSize;

      // Negatif değerleri önle
      if (hash < 0) {
        hash = (hash + hashSize) % hashSize;
      }
    }

    return hash;
  }

  /**
   * Karakteri 1-26 arasında sayıya çevirir (a=1, b=2, ..., z=26)
   * Harf değilse ASCII değerini kullanır
   */
  private int getCharValue(char c) {
    if (c >= 'a' && c <= 'z') {
      return c - 'a' + 1; // a=1, b=2, ..., z=26
    } else if (c >= 'A' && c <= 'Z') {
      return c - 'A' + 1; // A=1, B=2, ..., Z=26
    } else {
      // Harf değilse ASCII değerini kullan
      return (int) c;
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void resize() {
    // Eski tabloyu sakla
    HashEntry<K, V>[] oldTable = table;
    int oldCapacity = hashSize;

    // Yeni boyutu hesapla (2 katı + asal sayı)
    hashSize = getNextPrime(hashSize * 2);
    table = (HashEntry<K, V>[]) new HashEntry[hashSize];
    currentSize = 0;

    // Eski elementleri yeni tabloya ekle (rehashing)
    for (int i = 0; i < oldCapacity; i++) {
      HashEntry<K, V> entry = oldTable[i];
      if (entry != null) {
        put(entry.getKey(), entry.getValue()); // Yeni hash ile ekle
      }
    }
  }

  @Override

  public void clear(){
      for (int i = 0; i < hashSize; i++) 
              table[i] = null;

      currentSize = 0;

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

  private boolean isPrime(int n) {// Asal sayı kontrolü (6k±1 optimizasyonu)
    if (n <= 1)
      return false;
    if (n <= 3)
      return true;
    if (n % 2 == 0 || n % 3 == 0)
      return false;

    for (int i = 5; i * i <= n; i += 6) {
      if (n % i == 0 || n % (i + 2) == 0)
        return false;
    }
    return true;
  }

  @Override
  public int getCollisionCount() {
    return collisionCount;
  }

  @Override
  public LinkedList<K> keySet() {
    LinkedList<K> keys = new LinkedList<K>();// Tablodaki tüm key'leri topla
    for (HashEntry<K, V> entry : table) {
      if (entry != null) {
        keys.push(entry.getKey());
      }
    }
    return keys;
  }
}