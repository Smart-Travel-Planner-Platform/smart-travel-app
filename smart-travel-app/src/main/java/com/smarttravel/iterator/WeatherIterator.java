package com.smarttravel.iterator;

import com.smarttravel.model.City;

public interface WeatherIterator {
    boolean hasNext();
    City next();
}