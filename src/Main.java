import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

public class Main extends MainFunctionalities {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scn = new Scanner(System.in);

        // Kullanıcıya performans matrisi gösterilsin mi sor
        int performansChoice = -1;
        while (performansChoice != 0 && performansChoice != 1) {
            System.out.print("Do you want to see the performance matrix? (1 -> Yes, 0 -> No): ");
            String performansInput = scn.next();
            // Boş string kontrolü ile birlikte sadece sayısal giriş kabul et
            if (performansInput.isEmpty() || !performansInput.matches("\\d+")) {
                System.out.println("Please enter a number!");
                continue;
            }
            performansChoice = Integer.parseInt(performansInput);
            if (performansChoice != 0 || performansChoice != 1) { // Geçersiz giriş kontrolü ya 1 ya da 0 olmalı
                System.out.println("Invalid input, please enter a valid number.");
            }
        }
        if (performansChoice == 1)
            performanceMatrix();

        double loadFactor = 0.5;
        boolean hashTableChoice = false; // true -> SSF, false -> PAF
        boolean collisionChoice = true; // true -> DH, false -> LP

        // Zaman hesaplama
        long startTime = System.currentTimeMillis();

        HashTableInterface<String, HashTableInterface<String, Integer>> wordArticleCountMap;
        HashTableInterface<String, LinkedList<String>> IDhashTable;
        HashTableInterface<String, Boolean> stopWordsHashTable;
        if (hashTableChoice) {
            IDhashTable = new HashTableSSF<>(collisionChoice, loadFactor);
            wordArticleCountMap = new HashTableSSF<>(collisionChoice, loadFactor);
            stopWordsHashTable = new HashTableSSF<>(collisionChoice, loadFactor);
        } else {
            IDhashTable = new HashTablePAF<>(collisionChoice, loadFactor);
            wordArticleCountMap = new HashTablePAF<>(collisionChoice, loadFactor);
            stopWordsHashTable = new HashTablePAF<>(collisionChoice, loadFactor);
        }
        System.out.println("Files are loading, please wait...");
        IDhashTable = loadArticles(loadFactor, hashTableChoice, collisionChoice);
        wordArticleCountMap = findArticleWithSearchEngine(loadFactor, hashTableChoice, collisionChoice);
        stopWordsHashTable = loadStopWords("stop_words_en.txt", loadFactor, hashTableChoice, collisionChoice);
        System.out.println("Files loaded successfully.");

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("The total time of indexing in general search motor is " + totalTime / 1000 + " seconds.");
        System.out.println();

        Thread.sleep(1000);

        String input = "";
        // Kullanıcı arayüzü
        while (!input.equals("9")) {
            System.out.println("1 - Enter a sentence or word and find out which article is most relevant.");
            System.out.println("2 - Find Article by Article ID number.");

            System.out.println("9 - Exit the program.");
            System.out.print("Enter the number of the transaction you want to perform: ");
            input = scn.next();
            System.out.println();
            input = input.toLowerCase();
            if (input.equals("1")) {
                System.out.print("Write the sentence / word you want to search: "); // Aranacak kelime isteniyor
                scn.nextLine(); // consume leftover newline/token from previous next()
                String wordInput = scn.nextLine().toLowerCase(); // read the full sentence
                System.out.println();
                wordInput = wordInput.trim(); // Önce baş/son boşlukları temizle
                wordInput = wordInput.replaceAll("\\s+", " "); // Birden fazla boşluğu tek boşluk yap
                wordInput = wordInput.replaceAll("[^a-zA-Z0-9\\s]", ""); // Noktalama işaretlerini kaldır (boşlukları
                                                                         // koru)
                String[] words = wordInput.split(" "); // Kelimelere ayır
                int size = words.length;
                for (int i = 0; i < words.length; i++) {
                    String word = words[i];
                    if (stopWordsHashTable.containsKey(word)) {
                        words[i] = ""; // Stop kelimeleri boş yap
                        size--;
                    }
                } // Şu an burada words arrayinde elemanlardan stop kelimeler boş ve aranacak
                  // kelimeler duruyor
                if (size == 0) { // Tüm kelimeler stop kelime ise
                    System.out.println("No valid words to search after removing stop words.");
                    continue;
                } else if (size == 1) { // Sadece bir kelime aranacak
                    HashTableInterface<String, Integer> tempHashTable = null; // Makale frekans tablosu
                    for (int i = 0; i < words.length; i++) {
                        if (!words[i].equals("")) {
                            tempHashTable = wordArticleCountMap.get(words[i]);
                        }
                    }
                    if (tempHashTable == null) { // Kelime makalelerde yoksa atla
                        System.out.println("No valid articles found matching the search term.");
                        continue;
                    }

                    // Tüm makaleleri frekanslarına göre sırala ve en yüksek frekanslı ilk 5'ini göster
                    LinkedList<String> keySet = tempHashTable.keySet(); // Makale ID'lerini al
                    LinkedList<articleScore> allArticleScores = new LinkedList<>(); // Makale ve frekans listesi

                    for (String key : keySet) { // Her makale ID'si için
                        int frequency = tempHashTable.get(key); // Makaledeki kelime frekansını al
                        allArticleScores.add(new articleScore(key, frequency)); // Listeye ekle
                    }

                    // Listeyi frekansa göre azalan düzende sırala (en yüksek frekanslı önce)
                    allArticleScores.sort(Comparator.comparing(articleScore::getFrequency).reversed());

                    // En üstteki 5 makaleyi (veya daha az makale varsa hepsini) al
                    int topN = 5;
                    System.out.println("Found " + allArticleScores.size() + " article(s) matching the search term.");
                    System.out.println(
                            "Displaying top " + Math.min(topN, allArticleScores.size()) + " most relevant article(s):");

                    // İlk N makaleyi yazdır
                    for (int i = 0; i < Math.min(topN, allArticleScores.size()); i++) {
                        articleScore articleScore = allArticleScores.get(i);
                        String relevantArticleID = articleScore.getId();
                        int frequency = articleScore.getFrequency();

                        // Frekans ve makale içeriğini yazdır
                        LinkedList<String> Article = IDhashTable.get(relevantArticleID); // Makaleyi al
                        Article.pop(); // ID'yi kaldır
                        Article.addFirst(relevantArticleID); // ID'yi tekrardan başa ekle

                        System.out.println("---");
                        System.out.println((i + 1) + ". Article ID: " + relevantArticleID + ", Frequency: " + frequency);
                        System.out.println(Article);
                        System.out.println();
                    }
                } else {
                    // Aranacak birden fazla kelime var
                    HashTableInterface<String, Integer> aggregateFrequencyTable;
                    if (hashTableChoice) {
                        aggregateFrequencyTable = new HashTableSSF<>(collisionChoice, loadFactor);
                    } else {
                        aggregateFrequencyTable = new HashTablePAF<>(collisionChoice, loadFactor);
                    }
                    for (int i = 0; i < words.length; i++) {
                        String word = words[i];
                        if (word.equals(""))
                            continue; // Stop kelime ise atla
                        HashTableInterface<String, Integer> tempHashTable = wordArticleCountMap.get(word);
                        if (tempHashTable == null)
                            continue; // Kelime makalelerde yoksa atla
                        for (String articleID : tempHashTable.keySet()) { // Her makale ID'si için
                            int freq = tempHashTable.get(articleID); // Kelimenin makaledeki frekansını al
                            if (aggregateFrequencyTable.containsKey(articleID)) { // Zaten eklenmişse frekansı topla
                                int existingFreq = aggregateFrequencyTable.get(articleID); // Mevcut frekansı al
                                aggregateFrequencyTable.put(articleID, existingFreq + freq); // Yeni frekansı güncelle
                            } else { // Yoksa direkt ekle
                                aggregateFrequencyTable.put(articleID, freq);
                            }
                        }
                    }
                    // En alakalı makaleleri bul
                    LinkedList<String> keySet = aggregateFrequencyTable.keySet(); // Makale ID'lerini al
                    if (keySet.size() == 0) { // Hiç makale bulunamadıysa
                        System.out.println("No articles found matching the search terms.");
                        continue;
                    }

                    // Tüm makale skorlarını tutacak bir liste oluştur
                    LinkedList<articleScore> allArticleScores = new LinkedList<>();

                    for (String key : keySet) { // Her makale ID'si için
                        int frequency = aggregateFrequencyTable.get(key); // Makaledeki toplam kelime frekansını al
                        allArticleScores.add(new articleScore(key, frequency));
                    }

                    // Listeyi frekansa göre azalan düzende sırala
                    // Aynı frekansa sahip makaleler için sıralama tutarlı olabilir, ancak bu
                    // senaryoda önemli değildir.
                    allArticleScores.sort(Comparator.comparing(articleScore::getFrequency).reversed());

                    // En üstteki 5 makaleyi (veya daha az makale varsa hepsini) al
                    int topN = 5;
                    System.out.println("Found " + allArticleScores.size() + " article(s) matching the search terms.");
                    System.out.println(
                            "Displaying top " + Math.min(topN, allArticleScores.size()) + " most relevant article(s):");

                    // İlk N makaleyi yazdır
                    for (int i = 0; i < Math.min(topN, allArticleScores.size()); i++) {
                        articleScore articleScore = allArticleScores.get(i);
                        String relevantArticleID = articleScore.getId();
                        int frequency = articleScore.getFrequency();

                        // Frekans ve makale içeriğini yazdır
                        LinkedList<String> Article = IDhashTable.get(relevantArticleID); // Makaleyi al
                        Article.pop(); // ID'yi kaldır
                        Article.addFirst(relevantArticleID); // ID'yi tekrardan başa ekle

                        System.out.println("---");
                        System.out.println((i + 1) + ". Article ID: " + relevantArticleID + ", Frequency: " + frequency);
                        System.out.println(Article);
                        System.out.println();
                    }
                }
            } else if (input.equals("2")) {
                System.out.print("Enter the 10-digit article ID number: "); // Makale ID'si isteniyor
                input = scn.next();
                System.out.println();
                if (IDhashTable.containsKey(input)) { // Makale ID'si var mı kontrol et
                    // Varsa makaleyi yazdır
                    LinkedList<String> Article = new LinkedList<>();
                    Article = IDhashTable.get(input);
                    Article.poll();
                    System.out.println(Article);
                } else { // Yoksa uyarı ver
                    System.out.println("The Article ID you entered could not be found.");
                }
            } else if (input.equals("9")) {
                System.out.println("Exiting the program...");
            } else {
                System.out.println("An unknown input was entered. Please enter it again.");
            }
            System.out.println();
        }
        scn.close();
    }// end main

    static public void performanceMatrix() {
        System.out.print(
                "Load Factor   Hash Function   Collision Handling   Collision Count   Indexing Time(s)   Average Searching Time(ns)\n");
        System.out.print(
                "-----------   -------------   ------------------   ---------------   ----------------   --------------------------\n");
        double[] loadFactors = { 0.5, 0.8 };
        boolean[] hashTableChoices = { true, false }; // true -> SSF, false -> PAF
        boolean[] collisionChoices = { false, true }; // true -> DH, false -> LP
        for (double loadFactor : loadFactors) {
            for (boolean hashTableChoice : hashTableChoices) {
                for (boolean collisionChoice : collisionChoices) {
                    try {
                        // Indexing süresi ölçümü
                        long startTime = System.currentTimeMillis();
                        HashTableInterface<String, HashTableInterface<String, Integer>> wordArticleCountMap = findArticleWithSearchEngineForPerformanceMatrix(
                                loadFactor, hashTableChoice, collisionChoice);
                        long endTime = System.currentTimeMillis();
                        long indexingTime = endTime - startTime;

                        // Ortalama arama süresi ölçümü
                        startTime = System.nanoTime();
                        for (String word : wordArticleCountMap.keySet()) {
                            HashTableInterface<String, Integer> tempHashTable = wordArticleCountMap.get(word);
                        }
                        endTime = System.nanoTime();
                        long searchingTime = endTime - startTime;
                        searchingTime /= wordArticleCountMap.size(); // Ortalama arama süresi

                        // Collision sayısı ölçümü
                        int collisionCount = wordArticleCountMap.getCollisionCount(); // Ana tablo çarpışma sayısı
                        for (String word : wordArticleCountMap.keySet()) { // Alt tabloların çarpışma sayısını ekle
                            HashTableInterface<String, Integer> tempHashTable = wordArticleCountMap.get(word);
                            collisionCount += tempHashTable.getCollisionCount();
                        }

                        // Sonuçları yazdır
                        System.out.printf(
                                "   %.1f             %s                %s                %d             %.2f                   %d\n",
                                loadFactor,
                                hashTableChoice ? "SSF" : "PAF",
                                collisionChoice ? "DH" : "LP",
                                collisionCount,
                                indexingTime / 1000.0,
                                searchingTime);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } // for collisionChoices
            } // for hashTableChoices
        } // for loadFactors
    }// end performanceMatrix

}// end class