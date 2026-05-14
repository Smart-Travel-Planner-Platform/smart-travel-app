package com.smarttravel.composite;/*KARYA */
 /*Composite (Bileşik) Tasarım Deseni kullanılıyor.
Çünkü kullanıcının bazen tek bir müze ziyareti (Activity/Leaf), bazen de içinde
 birden fazla müze ve şehir barındıran kompleks bir gezi planı (ActivityPlan/Composite)
  oluşturmasına izin vermek istiyoruz. Bu desen sayesinde bilgisayar, tekil objeyle grup
   objeyi birbirinden ayırmadan aynı metotlarla (getTotalCost gibi) yönetebilir.

*/

/**
 * Bu bir Interface (Arayüz). 
 * Nesnelerin "ne yapabileceğini" tanımlar ama "nasıl yapacağını" sınıflara bırakır. 
 */
/*"Hesapla" Butonu: Kullanıcı tıkladığında, senin MainAppWindow sınıfın gidip
en üstteki ActivityPlan nesnesinin getTotalCost() metodunu çağıracak.

"Ekle" Butonu: Yeni bir aktivite eklendiğinde, arka planda bu maliyetler
 otomatik olarak güncellenecek. */
public interface TravelComponent {

    /**
     * Bileşenin (aktivite veya planın) toplam maliyetini hesaplar. 
     * Eğer bu bir 'Activity' ise kendi fiyatını döner.
     * Eğer bu bir 'ActivityPlan' ise içindeki her şeyin toplamını döner. 
     */
    double getTotalCost(); /**Kullanıcı sepete veya plana bir şey 
    eklediğinde, tüm yapıyı tek tek gezmek zorunda kalmazsın.
     Sadece en üstteki planın bu metodunu çağırırsın,
      o da kendi içindekilere sorar (Özyineli/Recursive işlem). */

    /**
     * Bileşenin toplam ne kadar süreceğini saat cinsinden hesaplar. 
     * Alt bileşenlerin sürelerini toplayarak ilerler. 
     */
    double getTotalDuration();/*Gezinin ne kadar vaktini alacağını 
    hesaplar. Özellikle Geliştirici 1'in hava durumu verileriyle
     birleştiğinde "Bu yağmurlu havada bu kadar süre dışarıda kalınır mı?"
      sorusuna yanıt üretmeni sağlar. */

    /**
     * Planı hiyerarşik bir yapıda (ağaç gibi) ekrana/konsola yazdırır. 
     * @param indentLevel: Girinti seviyesi. İç içe yapılarda ne kadar sağa kaydırılacağını belirler.
     */
    void displayPlan(int indentLevel);/*Planı görselleştirirken kullanılır */
}
