package com.smarttravel.iterator;

import com.smarttravel.model.City;
import com.smarttravel.model.WeatherState;
import java.util.List;

/**
 * Concrete Iterator (Somut Yineleyici): Güneşli Şehir Filtresi
 * Neden kullanıyoruz?: Bu sınıf listenin tamamını alır ama next() çağrıldığında
 * sadece durumu SUNNY olan şehirleri döndürür. Diğerlerini (Yağmurlu, Karlı)
 * sessizce atlar.
 */
public class SunnyIterator implements WeatherIterator {
    private List<City> cities;
    private int position = 0; // Listede nerede kaldığımızı tutan işaretçi (cursor)

    public SunnyIterator(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public boolean hasNext() {
        // İşaretçiden başlayarak listeyi tara, SUNNY olan bir şehir bulursan true dön.
        while (position < cities.size()) {
            if (cities.get(position).getCurrentWeatherState() == WeatherState.SUNNY) {
                return true;
            }
            position++; // SUNNY değilse bir sonraki şehre geç
        }
        return false; // Liste bitti, başka SUNNY şehir yok
    }

    @Override
    public City next() {
        // hasNext() zaten pozisyonu doğru şehre getirdiği için direkt o şehri döndürüp
        // işaretçiyi bir ilerletiyoruz.
        return cities.get(position++);
    }
}