package com.smarttravel.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smarttravel.model.City;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CityRepository {
    
    private static CityRepository instance;
    private List<City> cities;

    private CityRepository() {
        cities = new ArrayList<>();
        loadCitiesFromJson(); // Constructor çağrıldığında JSON'u oku
    }

    private void loadCitiesFromJson() {
        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("cities.json")))) {
            
            Gson gson = new Gson();
            Type cityListType = new TypeToken<ArrayList<City>>(){}.getType();
            cities = gson.fromJson(reader, cityListType);
            System.out.println("JSON başarıyla okundu. Şehir sayısı: " + cities.size());
            
        } catch (Exception e) {
            System.err.println("JSON okunurken hata oluştu: " + e.getMessage());
        }
    }

    public static synchronized CityRepository getInstance() {
        if (instance == null) {
            instance = new CityRepository();
        }
        return instance;
    }

    public List<City> getCities() {
        return cities;
    }
}