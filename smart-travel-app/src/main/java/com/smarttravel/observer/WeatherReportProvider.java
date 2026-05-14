package com.smarttravel.observer;

import java.util.ArrayList;
import java.util.List;

public class WeatherReportProvider {
    private List<WeatherObserver> observers = new ArrayList<>();

    public void addObserver(WeatherObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(WeatherObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (WeatherObserver observer : observers) {
            observer.updateWeather();
        }
    }
}