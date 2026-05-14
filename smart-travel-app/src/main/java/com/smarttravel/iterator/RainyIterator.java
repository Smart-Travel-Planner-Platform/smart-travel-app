package com.smarttravel.iterator;

import com.smarttravel.model.City;

public class RainyIterator implements WeatherIterator {
    @Override
    public boolean hasNext() { return false; }

    @Override
    public City next() { return null; }
}