package com.smarttravel.decorator;

public class MuseumVisit extends CityActivityDecorator {
    @Override
    public String getDescription() {
        return "Museum Visit";
    }
}