package com.smarttravel.decorator;

import com.smarttravel.model.City;

/**
 * Concrete Component: Çekirdek (Base) Sınıf
 * Hocaya Not: "Hocam, Decorator deseninde sarmalayacağımız asıl bir 'Çekirdek'
 * nesneye ihtiyacımız var.
 * Şehir nesnemizi (City) bozmamak için onu bu sınıfın içine koyduk.
 * Başlangıçta ekstra bir aktivite olmadığı için maliyet ve süre 0'dır."
 */
public class BaseCityVisit implements CityVisit {
    private City city;

    public BaseCityVisit(City city) {
        this.city = city;
    }

    @Override
    public String getDescription() {
        return city.getName() + " Gezisi (Temel)";
    }

    @Override
    public double getCost() {
        return 0.0;
    } // Başlangıç maliyeti yok

    @Override
    public double getDuration() {
        return 0.0;
    } // Başlangıç süresi yok
}