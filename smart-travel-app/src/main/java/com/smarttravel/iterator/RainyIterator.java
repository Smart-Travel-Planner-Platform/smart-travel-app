package com.smarttravel.iterator;

import com.smarttravel.model.City;
import com.smarttravel.model.WeatherState;
import java.util.List;

/**
 * Concrete Iterator: Sadece Yağmurlu (RAINY) şehirleri getiren filtre.
 */
public class RainyIterator implements WeatherIterator {
    private List<City> cities;
    private int position = 0;

    public RainyIterator(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public boolean hasNext() {
        while (position < cities.size()) {
            if (cities.get(position).getCurrentWeatherState() == WeatherState.RAINY)
                return true;
            position++;
        }
        return false;
    }

    @Override
    public City next() {
        return cities.get(position++);
    }
}