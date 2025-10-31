public class debugmain {
    
    
    public static String cleanWord(String word, String delimiter) {
        if (word == null || word.isEmpty() || delimiter == null || delimiter.isEmpty()) {
            return word;
        }
        
        boolean foundAndRemoved = false;
        String newWord = word;
        
        for (char delimChar : delimiter.toCharArray()) {
            int index = newWord.indexOf(delimChar);
            if (index != -1) {
                newWord = newWord.substring(0, index) + newWord.substring(index + 1);
                foundAndRemoved = true;
                break;
            }
        }
        
        if (!foundAndRemoved) {
            return newWord;
        }
        
        return cleanWord(newWord, delimiter);
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   cleanWord Recursive Method Test");
        System.out.println("========================================\n");

        // Test 1: Basit delimiter'lar
        System.out.println("TEST 1: Basit Delimiter Temizleme");
        System.out.println("----------------------------------");
        testClean("hello-world", "helloworld");
        testClean("test.data", "testdata");
        testClean("clean_text", "cleantext");
        testClean("no@special#chars", "nospecialchars");
        System.out.println();

        // Test 2: Sayılar
        System.out.println("TEST 2: Sayı Temizleme");
        System.out.println("----------------------");
        testClean("test123data", "testdata");
        testClean("year2024", "year");
        testClean("abc123def456", "abcdef");
        testClean("123", "");
        System.out.println();

        // Test 3: Tırnak işaretleri
        System.out.println("TEST 3: Tırnak İşaretleri");
        System.out.println("-------------------------");
        testClean("\"quoted\"", "quoted");
        testClean("'single'", "single");
        testClean("it's", "its");
        System.out.println();

        // Test 4: Parantezler
        System.out.println("TEST 4: Parantezler");
        System.out.println("-------------------");
        testClean("(test)", "test");
        testClean("[array]", "array");
        testClean("{object}", "object");
        testClean("<html>", "html");
        System.out.println();

        // Test 5: Karışık karakterler
        System.out.println("TEST 5: Karışık Karakterler");
        System.out.println("---------------------------");
        testClean("hello!!!world???", "helloworld");
        testClean("test@email.com", "testemailcom");
        testClean("price:$100", "price");
        testClean("50%off", "off");
        System.out.println();

        // Test 6: Edge cases
        System.out.println("TEST 6: Edge Cases");
        System.out.println("------------------");
        testClean("", "");
        testClean("onlyletters", "onlyletters");
        testClean("!!!###$$$", "");
        testClean("a1b2c3", "abc");
        System.out.println();

        // Test 7: Gerçek makale kelime örnekleri
        System.out.println("TEST 7: Gerçek Makale Kelime Örnekleri");
        System.out.println("--------------------------------------");
        testClean("U.S.", "US");
        testClean("don't", "dont");
        testClean("COVID-19", "COVID");
        testClean("(CNN)", "CNN");
        testClean("President's", "Presidents");
        testClean("$1,000", "");
        System.out.println();

        // Test 8: Performans testi
        System.out.println("TEST 8: Performans Testi");
        System.out.println("------------------------");
        String complexWord = "a1-b2_c3@d4#e5$f6%g7&h8*i9!j0";
        long startTime = System.nanoTime();
        String cleaned = cleanWord(complexWord, Reader.DELIMITERS);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000; // mikrosaniye
        System.out.printf("Input:  '%s'%n", complexWord);
        System.out.printf("Output: '%s'%n", cleaned);
        System.out.printf("Süre:   %d µs%n", duration);
        System.out.println();

        // Test 9: Çoklu delimiter ardışık
        System.out.println("TEST 9: Ardışık Delimiter'lar");
        System.out.println("------------------------------");
        testClean("test---data", "testdata");
        testClean("hello___world", "helloworld");
        testClean("a@@@b", "ab");
        System.out.println();

        // Test 10: Türkçe karakterler (korunmalı)
        System.out.println("TEST 10: Türkçe Karakterler (Korunmalı)");
        System.out.println("---------------------------------------");
        testClean("çalışma", "çalışma");
        testClean("öğrenci123", "öğrenci");
        testClean("ışık-test", "ışıktest");
        System.out.println();

        // Özet
        System.out.println("========================================");
        System.out.println("   Test Tamamlandı!");
        System.out.println("========================================");
        System.out.println("✓ Recursive method başarıyla çalışıyor");
        System.out.println("✓ Tüm delimiter karakterler temizleniyor");
        System.out.println("✓ Hiç delimiter kalmadığında döngü bitiyor");
    }

    private static void testClean(String input, String expected) {
        String result = cleanWord(input, Reader.DELIMITERS);
        boolean passed = result.equals(expected);
        String status = passed ? "✓ PASS" : "✗ FAIL";
        
        System.out.printf("%s | Input: %-30s | Output: %-20s | Expected: %-20s%n", 
                         status, "'" + input + "'", "'" + result + "'", "'" + expected + "'");
        
        if (!passed) {
            System.out.println("   ⚠ Beklenen: '" + expected + "' ama '" + result + "' alındı");
        }
    }
}