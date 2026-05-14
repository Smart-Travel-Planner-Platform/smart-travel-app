package com.smarttravel.decorator;

/**
 * Concrete Decorator (Somut Sarmalayıcı): Müze Ziyareti Aktivitesi
 * Neden kullanıyoruz?: Kullanıcı geziye "Müze" eklediğinde, mevcut gezi planını
 * bu sınıfla sarmalıyoruz.
 * Bu sınıf asıl gezi maliyetinin üstüne kendi maliyetini ve süresini ekliyor.
 */
public class MuseumVisit extends CityActivityDecorator {

    // Kurucu metod: Üzerine ekleneceğimiz mevcut geziyi alıp üst sınıfa (Base
    // Decorator) göndeririz.
    public MuseumVisit(CityVisit visit) {
        super(visit);
    }

    @Override
    public String getDescription() {
        // Önceki aktivitelerin açıklamasına kendimizi ekliyoruz
        return super.getDescription() + " + Müze Ziyareti";
    }

    @Override
    public double getCost() {
        // Önceki aktivitelerin toplam maliyetine Müze giriş ücretini (Örn: 25.0)
        // ekliyoruz
        return super.getCost() + 25.0;
    }

    @Override
    public double getDuration() {
        // Önceki aktivitelerin toplam süresine Müze gezme süresini (Örn: 3.0 saat)
        // ekliyoruz
        return super.getDuration() + 3.0;
    }
}