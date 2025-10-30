public class HashTablePAF<K, V> extends Collision implements HashTableInterface<K, V> {
  private int hashSize = 1009; // Asal sayı boyutu
  private static final int Z = 33;
  boolean collision;
  HashEntry<K, V>[] table;

  @SuppressWarnings("unchecked")
  public HashTablePAF(boolean collisionChoice) {
    table = (HashEntry<K, V>[]) new HashEntry[hashSize];
    for (int i = 0; i < hashSize; i++)
      table[i] = null;
    this.collision = collisionChoice; // false -> LP, true -> DH
  }

  @Override
  public V get(K key) {
    int hash = hashFunction(key);
    if (table[hash] == null)
      return null;
    else
      return table[hash].getValue();
  }

  @Override
  public void put(K key, V value) {
    if(isFull()) resize(); // If is it full, resize the table
    int hash = hashFunction(key);
    if (table[hash] != null) {
      if (collision) {
        hash = linearProbing(key, hash, hashSize, table);
      } else {
        hash = doubleHashing(key, hash, hashSize, table, getPreviousPrime(hashSize));
      }
      System.out.println("There is a collision for key: " + key);
    }

    table[hash] = new HashEntry<>(key, value);
    System.out.println("Inserted key: " + key + " at index: " + hash);
  }

  @Override
  public int size() {
    int count = 0;
    for (HashEntry<K, V> entry : table) {
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
    if (table[hash] != null && table[hash].getKey().equals(key)) {
      return true;
    }
    return false;
  }

  @Override
  public V remove(K key) {
    int hash = hashFunction(key);
    if (table[hash] == null) {
      return null;
    }

    if (table[hash].getKey().equals(key)) {
      V value = table[hash].getValue();
      table[hash] = null;
      return value;
    }

    return null;
  }

  @Override
  public int hashFunction(K key) {
    // return Math.abs(key.hashCode()) % hashSize;
    // PAF (Polynomial Accumulation Function) implementasyonu
    // Horner's rule kullanarak overflow'u önleme
    // h(s) = ch0*z^(n-1) + ch1*z^(n-2) + ... + ch(n-1)*z^0
    
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

    // Eski elementleri yeni tabloya ekle (rehashing)
    for (int i = 0; i < oldCapacity; i++) {
      HashEntry<K, V> entry = oldTable[i];
      if (entry != null && !entry.isDeleted()) { // LP/DH için
        put(entry.getKey(), entry.getValue()); // Yeni hash ile ekle
      }
    }
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
}