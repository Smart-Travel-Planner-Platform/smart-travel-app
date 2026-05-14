package com.smarttravel.repository;

import com.smarttravel.model.City;
import com.smarttravel.model.WeatherState;
import java.util.ArrayList;
import java.util.List;

public class CityRepository {
    
    // 1. Kendi instance'ını static olarak tut (Singleton kuralı)
    private static CityRepository instance;
    private List<City> cities;

    // 2. Constructor private olmalı ki dışarıdan new ile oluşturulamasın
    private CityRepository() {
        cities = new ArrayList<>();
        
        // DİKKAT: JSON okuma işini yapana kadar arkadaşlarının test yapabilmesi için sahte veriler ekliyoruz.
        cities.add(new City("Ankara", 5317215, 25615.0, 20.5, WeatherState.SUNNY));
        cities.add(new City("Istanbul", 15715958, 5343.0, 18.0, WeatherState.CLOUDY));
        cities.add(new City("Erzurum", 758279, 25005.0, -5.5, WeatherState.SNOWY));
        cities.add(new City("Rize", 344016, 3922.0, 14.2, WeatherState.RAINY));
        cities.add(new City("Antalya", 2619832, 20177.0, 30.0, WeatherState.SUNNY));
    }

    // 3. Tekil objeye erişim metodu
    public static synchronized CityRepository getInstance() {
        if (instance == null) {
            instance = new CityRepository();
        }
        return instance;
    }

    // Şehir listesini döndüren metod
    public List<City> getCities() {
        return cities;
    }
}