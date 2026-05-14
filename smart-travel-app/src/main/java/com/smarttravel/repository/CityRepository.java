package com.smarttravel.repository;

import com.smarttravel.model.City;
import java.util.ArrayList;
import java.util.List;

public class CityRepository {
    private static CityRepository instance;
    private List<City> cities;

    private CityRepository() {
        this.cities = new ArrayList<>();
        // TODO: Read from cities.json
    }

    public static CityRepository getInstance() {
        if (instance == null) {
            instance = new CityRepository();
        }
        return instance;
    }

    public List<City> getCities() {
        return cities;
    }
}