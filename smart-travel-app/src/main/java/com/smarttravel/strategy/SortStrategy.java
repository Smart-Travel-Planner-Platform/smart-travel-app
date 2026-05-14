package com.smarttravel.strategy;

import com.smarttravel.model.City;
import java.util.List;

public interface SortStrategy {
    void sort(List<City> cities);
}