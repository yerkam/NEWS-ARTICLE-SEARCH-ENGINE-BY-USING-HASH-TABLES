public class HashEntry {
    private String key;
    private Object value;
    private boolean isDeleted;  // Linear Probing için silinme durumu

    HashEntry(String key, Object value) {
          this.key = key;
          this.value = value;
          this.isDeleted = false;
    }     

    public String getKey() {
          return key;
    }

    public Object getValue() {
          return value;
    }

    public boolean isDeleted() {
          return isDeleted;
    }

}
