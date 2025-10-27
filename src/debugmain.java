public class debugmain {
 
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   HashTablePAF Test Programı");
        System.out.println("========================================\n");
        
        // Hash table oluştur
        HashTablePAF hashTable = new HashTablePAF();
        
        // Test 1: Temel put ve get işlemleri
        System.out.println("TEST 1: Temel Put ve Get İşlemleri");
        System.out.println("-----------------------------------");
        hashTable.put("öğrenci1", "Ahmet Yılmaz");
        hashTable.put("öğrenci2", "Ayşe Demir");
        hashTable.put("öğrenci3", "Mehmet Kaya");
        hashTable.put("not1", 85);
        hashTable.put("not2", 92);
        
        System.out.println("öğrenci1: " + hashTable.get("öğrenci1"));
        System.out.println("öğrenci2: " + hashTable.get("öğrenci2"));
        System.out.println("not1: " + hashTable.get("not1"));
        System.out.println("Boyut: " + hashTable.size());
        System.out.println();
        
        // Test 2: containsKey kontrolü
        System.out.println("TEST 2: containsKey Kontrolü");
        System.out.println("----------------------------");
        System.out.println("'öğrenci1' var mı? " + hashTable.containsKey("öğrenci1"));
        System.out.println("'öğrenci5' var mı? " + hashTable.containsKey("öğrenci5"));
        System.out.println("'not1' var mı? " + hashTable.containsKey("not1"));
        System.out.println();
        
        // Test 3: isEmpty kontrolü
        System.out.println("TEST 3: isEmpty Kontrolü");
        System.out.println("------------------------");
        System.out.println("Tablo boş mu? " + hashTable.isEmpty());
        System.out.println("Mevcut boyut: " + hashTable.size());
        System.out.println();
        
        // Test 4: Çakışma testi (collision)
        System.out.println("TEST 4: Çakışma (Collision) Testi");
        System.out.println("----------------------------------");
        // Aynı hash değerine sahip olabilecek keyler ekleyelim
        hashTable.put("test1", "Değer 1");
        hashTable.put("test2", "Değer 2");
        hashTable.put("test3", "Değer 3");
        System.out.println("Çakışma testi tamamlandı.");
        System.out.println("Mevcut boyut: " + hashTable.size());
        System.out.println();
        
        // Test 5: Remove işlemi
        System.out.println("TEST 5: Remove İşlemi");
        System.out.println("---------------------");
        System.out.println("Silmeden önce boyut: " + hashTable.size());
        Object removedValue = hashTable.remove("öğrenci2");
        System.out.println("Silinen değer: " + removedValue);
        System.out.println("Silindikten sonra boyut: " + hashTable.size());
        System.out.println("'öğrenci2' hala var mı? " + hashTable.containsKey("öğrenci2"));
        System.out.println();
        
        // Test 6: Olmayan key'i silmeye çalışma
        System.out.println("TEST 6: Olmayan Key'i Silme");
        System.out.println("---------------------------");
        Object notFound = hashTable.remove("olmayan_key");
        System.out.println("Olmayan key için dönen değer: " + notFound);
        System.out.println();
        
        // Test 7: Null değer ekleme
        System.out.println("TEST 7: Null Değer Ekleme");
        System.out.println("-------------------------");
        hashTable.put("null_test", null);
        System.out.println("Null değer eklendi.");
        System.out.println("null_test key'i var mı? " + hashTable.containsKey("null_test"));
        System.out.println("null_test değeri: " + hashTable.get("null_test"));
        System.out.println();
        
        // Test 8: Aynı key ile güncelleme (collision senaryosu)
        System.out.println("TEST 8: Aynı Key ile Güncelleme (Collision)");
        System.out.println("-------------------------------------------");
        System.out.println("Eski değer: " + hashTable.get("öğrenci1"));
        hashTable.put("öğrenci1", "Ahmet Yılmaz - Güncellendi");
        System.out.println("Yeni değer: " + hashTable.get("öğrenci1"));
        System.out.println("NOT: PAF implementasyonunda collision varsa güncelleme yapılamaz!");
        System.out.println();
        
        // Test 9: Çok sayıda eleman ekleme
        System.out.println("TEST 9: Çok Sayıda Eleman Ekleme");
        System.out.println("--------------------------------");
        int initialSize = hashTable.size();
        for (int i = 1; i <= 20; i++) {
            hashTable.put("item" + i, "Değer " + i);
        }
        System.out.println("20 eleman eklendi.");
        System.out.println("Önceki boyut: " + initialSize);
        System.out.println("Yeni boyut: " + hashTable.size());
        System.out.println();
        
        // Test 10: Hash fonksiyonu testi
        System.out.println("TEST 10: Hash Fonksiyonu Testi");
        System.out.println("------------------------------");
        String[] testKeys = {"key1", "key2", "key3", "test", "hash"};
        for (String key : testKeys) {
            int hash = hashTable.hashFunction(key);
            System.out.println("Key: '" + key + "' -> Hash: " + hash);
        }
        System.out.println();
        
        // Test 11: Resize testi
        System.out.println("TEST 11: Resize Testi");
        System.out.println("--------------------");
        System.out.println("Resize öncesi boyut: " + hashTable.size());
        hashTable.resize();
        System.out.println("Resize sonrası boyut: " + hashTable.size());
        System.out.println("Resize sonrası 'öğrenci1' değeri: " + hashTable.get("öğrenci1"));
        System.out.println();
        
        // Test 12: Performans testi
        System.out.println("TEST 12: Performans Testi");
        System.out.println("-------------------------");
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            hashTable.put("perf_key_" + i, "Değer " + i);
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // millisaniye'ye çevir
        System.out.println("1000 eleman ekleme süresi: " + duration + " ms");
        System.out.println("Final boyut: " + hashTable.size());
        System.out.println();
        
        // Test özeti
        System.out.println("========================================");
        System.out.println("   Test Özeti");
        System.out.println("========================================");
        System.out.println("Toplam eleman sayısı: " + hashTable.size());
        System.out.println("Tablo boş mu? " + hashTable.isEmpty());
        System.out.println("\nTüm testler tamamlandı!");
    }
}
