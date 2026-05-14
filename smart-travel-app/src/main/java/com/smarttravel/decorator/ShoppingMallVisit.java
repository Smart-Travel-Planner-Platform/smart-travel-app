package com.smarttravel.decorator;

/**
 * Concrete Decorator: Alışveriş Merkezi (AVM) Ziyareti
 * Özellikleri: Ücretsiz (0$) ama çok zaman alır (4 Saat).
 */
public class ShoppingMallVisit extends CityActivityDecorator {
    public ShoppingMallVisit(CityVisit visit) {
        super(visit);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + AVM Ziyareti";
    }

    @Override
    public double getCost() {
        return super.getCost() + 0.0;
    } // Giriş ücretsiz

    @Override
    public double getDuration() {
        return super.getDuration() + 4.0;
    } // 4 saat sürer
}