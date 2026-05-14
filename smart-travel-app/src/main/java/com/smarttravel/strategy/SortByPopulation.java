package com.smarttravel.strategy;

import com.smarttravel.model.City;
import java.util.Comparator;
import java.util.List;

/**
 * Concrete Strategy: Nüfusa Göre Sıralama
 * Neden kullanıyoruz?: Kullanıcı nüfusa göre sıralamak istediğinde bu algoritma
 * devreye girer.
 * reversed() kullanarak en kalabalık şehrin en üstte çıkmasını sağlıyoruz.
 */
public class SortByPopulation implements SortStrategy {
    @Override
    public void sort(List<City> cities) {
        cities.sort(Comparator.comparing(City::getPopulation).reversed());
    }
}