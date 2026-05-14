package com.smarttravel.strategy;

import com.smarttravel.model.City;
import java.util.Comparator;
import java.util.List;

/**
 * Concrete Strategy: Yüzölçümüne Göre Sıralama
 * Neden kullanıyoruz?: Kodu if-else bloklarına boğmadan, sadece alana göre
 * sıralama
 * sorumluluğunu bu sınıfa veriyoruz (Single Responsibility).
 */
public class SortByArea implements SortStrategy {
    @Override
    public void sort(List<City> cities) {
        cities.sort(Comparator.comparing(City::getArea).reversed());
    }
}