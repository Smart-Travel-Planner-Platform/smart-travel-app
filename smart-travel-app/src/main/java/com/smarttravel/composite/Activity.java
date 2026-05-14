package com.smarttravel.composite;/*KARYA */
/*implements TravelComponent: Bu ifade,
 "Ben de bir seyahat bileşeniyim, dolayısıyla maliyet ve süre
  hesaplamayı biliyorum" demektir.  

  Burada Composite deseninin "Leaf" kısmını uyguladık.
Leaf, hiyerarşideki atomik (daha küçük parçaya bölünemeyen) iş birimidir.

Geliştirici 2 ile Bağlantı: Bu sınıf,
 Geliştirici 2'nin yazdığı Decorator'ları
  (örneğin müze ziyareti veya rehberli tur eklenmiş hali) sarmalar.
   Yani aslında saf bir aktiviteyi temsil eder.  


Hiyerarşideki Yeri: Altında çocuk (child) barındırmaz.
 Bir listesi yoktur. Sadece kendi verisini tutar ve döner. */

/**
 * Bu sınıf 'Leaf' (Yaprak) nesnesidir. 
 * Altında başka bileşenler barındırmaz, sadece kendi verisini tutar[cite: 3, 4].
 */


/*Butona Basıldığında Ne Olacak?
Kullanıcı arayüzden (GUI) "Müze Ziyareti Ekle"
 butonuna bastığında, arka planda bir Activity nesnesi oluşturulacak.  

Bu nesne, daha sonra yazacağımız ActivityPlan (Composite) sınıfının
 içindeki listeye eklenecek.  

Senin Command Manager yapın devreye girdiğinde,
 bu aktiviteyi ekleme işlemi bir "Komut" nesnesi olarak
  kaydedilecek ki kullanıcı "Geri Al" diyebilsin. */
public class Activity implements TravelComponent {
    private String name;
    private double cost;
    private double duration;

    // Constructor: Aktivitenin adını, fiyatını ve süresini alır.
    public Activity(String name, double cost, double duration) {
        this.name = name;
        this.cost = cost;
        this.duration = duration;
    }

    @Override
    public double getTotalCost() {
        // Yaprak düğüm olduğu için sadece kendi maliyetini döner.
        return this.cost;
    }

    @Override
    public double getTotalDuration() {
        // Sadece kendi süresini döner.
        return this.duration;
    }

    @Override
    public void displayPlan(int indentLevel) {
        // Girinti (indent) kadar boşluk bırakıp aktivite ismini yazdırır.
        String indent = "  ".repeat(indentLevel);
        System.out.println(indent + "- " + name + " (Cost: " + cost + ", Duration: " + duration + "h)");
    }
}