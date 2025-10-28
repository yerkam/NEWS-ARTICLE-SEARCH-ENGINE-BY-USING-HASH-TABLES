public class HashEntry<K, V> {
    private K key;
    private V value;
    private boolean isDeleted;  // Linear Probing için silinme durumu

    HashEntry(K key, V value) {
          this.key = key;
          this.value = value;
          this.isDeleted = false;
    }     

    public K getKey() {
          return key;
    }

    public V getValue() {
          return value;
    }

    public boolean isDeleted() {
          return isDeleted;
    }

}
