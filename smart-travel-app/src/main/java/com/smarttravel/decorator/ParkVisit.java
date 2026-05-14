package com.smarttravel.decorator;

public class ParkVisit extends CityActivityDecorator {
    @Override
    public String getDescription() {
        return "Park Visit";
    }
}