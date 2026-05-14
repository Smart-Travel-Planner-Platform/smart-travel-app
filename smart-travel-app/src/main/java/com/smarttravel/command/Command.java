package com.smarttravel.command;/*KARYA  */

/**
 * Command Pattern'in kalbidir.
 * Uygulamadaki her işlem (Ekleme, Silme, Taşıma) bu arayüzü giyer.
 */

/*Bu Arayüz Ne İşe Yarar? (Arayüzle Bağlantısı)
Kapsülleme: Kullanıcı "Şehir Ekle" butonuna bastığında,
 arayüz doğrudan listeye bir şey eklemez. Bunun yerine
  bir AddCityCommand nesnesi oluşturur. Bu nesne, içinde "ne yapılacağını"
   ve "nasıl geri alınacağını" paketlenmiş olarak tutar.

Geri Alabilme Özelliği: Eğer sadece execute() olsaydı,
 işlemi geri alamazdık. undo() metodu sayesinde sistem,
  az önce yapılanın tam tersini (zıt işlemini) yapmayı bilir.

Neden Kullanıyoruz? (Design Pattern)
Burada Command (Komut) Tasarım Deseni kullanılıyor.

Arayüzdeki Karşılığı: Senin tasarladığın MainAppWindow
 üzerinde bir "Undo" (Geri Al) butonu olacak. Bu butona 
 basıldığında, hangi işlemin yapıldığını bilmesine gerek
  kalmadan sadece en son komutun .undo() metodunu çağıracak. */
public interface Command {
    
    /**
     * İşlemi gerçekleştirir. 
     * Örneğin: Listeye bir şehir ekler.
     */
    void execute();

    /**
     * Yapılan işlemi tam tersine çevirir. 
     * Örneğin: Az önce eklenen şehri listeden geri siler.
     */
    void undo();
}