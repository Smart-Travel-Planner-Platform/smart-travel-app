package com.smarttravel.iterator;

import com.smarttravel.model.City;
import com.smarttravel.model.WeatherState;
import java.util.List;

/**
 * Concrete Iterator: Sadece Karlı (SNOWY) şehirleri getiren filtre.
 */
public class SnowyIterator implements WeatherIterator {
    private List<City> cities;
    private int position = 0;

    public SnowyIterator(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public boolean hasNext() {
        while (position < cities.size()) {
            if (cities.get(position).getCurrentWeatherState() == WeatherState.SNOWY)
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