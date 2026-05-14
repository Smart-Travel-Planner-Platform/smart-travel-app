package com.smarttravel.strategy;

import com.smarttravel.model.City;
import java.util.List;
import java.util.Comparator;

public class SortByName implements SortStrategy {
    @Override
    public void sort(List<City> cities) {
        cities.sort(Comparator.comparing(City::getName));
    }
}