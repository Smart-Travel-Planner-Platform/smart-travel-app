package com.smarttravel.strategy;

import com.smarttravel.model.City;
import java.util.Comparator;
import java.util.List;

/**
 * Concrete Strategy (Somut Strateji): Alfabetik Sıralama
 * Neden kullanıyoruz?: Bu sınıf sadece isme göre sıralama algoritmasından
 * sorumludur (Single Responsibility).
 * Kullanıcı arayüzde "İsme Göre Sırala"yı seçtiğinde Context (GUI) bu sınıfın
 * sort metodunu tetikler.
 */
public class SortByName implements SortStrategy {
    @Override
    public void sort(List<City> cities) {
        // Java'nın yerleşik Comparator yapısını kullanarak City nesnelerinin getName()
        // değerlerine göre listeyi sıralıyoruz.
        cities.sort(Comparator.comparing(City::getName));
    }
}