package com.smarttravel.strategy;

import com.smarttravel.model.City;
import java.util.List;

/**
 * Strategy (Strateji) Arayüzü:
 * SENG 324 Ders Notları: "Bir algoritma ailesi tanımlar, her birini kapsüller
 * ve birbirlerinin yerine kullanılabilir hale getirir."
 * Neden kullanıyoruz?: Arayüzdeki (GUI) ComboBox'tan seçilen sıralama kriterine
 * göre (İsim, Nüfus, Alan)
 * if/else blokları yazmak yerine, işlemi bu arayüzü uygulayan sınıflara
 * devrediyoruz.
 */
public interface SortStrategy {
    void sort(List<City> cities);
}