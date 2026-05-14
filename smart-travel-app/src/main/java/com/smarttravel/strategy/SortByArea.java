package com.smarttravel.strategy;

import com.smarttravel.model.City;
import java.util.List;
import java.util.Comparator;

public class SortByArea implements SortStrategy {
    @Override
    public void sort(List<City> cities) {
        cities.sort(Comparator.comparingDouble(City::getArea).reversed());
    }
}