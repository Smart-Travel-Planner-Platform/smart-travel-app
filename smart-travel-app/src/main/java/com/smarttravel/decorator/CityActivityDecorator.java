package com.smarttravel.decorator;

/**
 * Base Decorator (Temel Sarmalayıcı):
 * SENG 324 Ders Notları: "Sınıflara alt sınıflar oluşturmaya gerek kalmadan
 * dinamik olarak ek sorumluluklar ekler."
 * Neden kullanıyoruz?: City sınıfını değiştirmeden (Open-Closed Prensibi),
 * içine aldığı (sarmaladığı)
 * CityVisit nesnesinin özelliklerini üstüne geçirir.
 */
public abstract class CityActivityDecorator implements CityVisit {
    protected CityVisit decoratedVisit; // İçine alıp sarmalayacağı asıl nesne (Mevcut plan)

    public CityActivityDecorator(CityVisit visit) {
        this.decoratedVisit = visit;
    }

    // Gelen istekleri önce sarmalanan nesneye iletiyoruz (Delegation)
    public String getDescription() {
        return decoratedVisit.getDescription();
    }

    public double getCost() {
        return decoratedVisit.getCost();
    }

    public double getDuration() {
        return decoratedVisit.getDuration();
    }
}