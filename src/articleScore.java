import java.util.Comparator;

/**
 * Makale Kimliği (ID) ve arama terimleriyle eşleşen toplam kelime frekansını 
 * (skorunu) tutar. Frekansa göre sıralamayı desteklemek için Comparator uygular.
 */
public class articleScore {
    
    private final String id;
    private final int frequency; // Makaledeki arama terimlerinin toplam frekansı

    public articleScore(String id, int frequency) {
        this.id = id;
        this.frequency = frequency;
    }

    public String getId() {
        return id;
    }

    public int getFrequency() {
        return frequency;
    }

    /**
     * Bu makale skorunu, diğer bir ArticleScore nesnesiyle karşılaştırmak için 
     * bir karşılaştırıcı (Comparator) döndürür.
     * * Frekansa göre azalan düzende (yani en yüksek frekans en başta) sıralama yapar.
     */
    public static Comparator<articleScore> getComparatorByFrequency() {
        // En yüksek frekans en önce gelmelidir (Azalan düzen)
        return Comparator.comparing(articleScore::getFrequency).reversed();
    }

    @Override
    public String toString() {
        return "ArticleScore{" +
                "id='" + id + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}