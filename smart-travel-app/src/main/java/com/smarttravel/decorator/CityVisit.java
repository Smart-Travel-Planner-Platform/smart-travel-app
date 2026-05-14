package com.smarttravel.decorator;

/**
 * Component (Bileşen) Arayüzü:
 * Neden kullanıyoruz?: Hem temel şehir ziyaretinin hem de üzerine eklenecek
 * ekstra
 * aktivitelerin (Müze, AVM) ortak özelliklerini tanımlar.
 */
public interface CityVisit {
    String getDescription(); // Ziyaretin açıklaması

    double getCost(); // Ziyaretin maliyeti

    double getDuration(); // Ziyaretin süresi (saat)
}