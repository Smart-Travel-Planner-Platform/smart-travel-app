package com.smarttravel.observer;

import com.smarttravel.model.City;
import com.smarttravel.model.WeatherState;
import com.smarttravel.repository.CityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class WeatherReportProvider implements Runnable {
    
    private List<WeatherObserver> observers;
    private Random random;
    private boolean running;

    public WeatherReportProvider() {
        observers = new ArrayList<>();
        random = new Random();
        running = true;
    }

   
    public void attach(WeatherObserver observer) {
        observers.add(observer);
    }

    
    public void detach(WeatherObserver observer) {
        observers.remove(observer);
    }

    
    public void notifyObservers() {
        for (WeatherObserver observer : observers) {
            observer.update();
        }
    }

   
    @Override
    public void run() {
        while (running) {
            try {
             
                Thread.sleep(3000); 
                
                updateWeatherRandomly();
                notifyObservers(); 
                
            } catch (InterruptedException e) {
                System.out.println("Hava durumu güncelleyici durduruldu.");
                running = false;
            }
        }
    }

    private void updateWeatherRandomly() {
        List<City> cities = CityRepository.getInstance().getCities();
        if (cities == null || cities.isEmpty()) return;

       
        City randomCity = cities.get(random.nextInt(cities.size()));
        
        
        double tempChange = -5.0 + (10.0 * random.nextDouble());
        randomCity.setCurrentTemperature(randomCity.getCurrentTemperature() + tempChange);


        WeatherState[] states = WeatherState.values();
        randomCity.setCurrentWeatherState(states[random.nextInt(states.length)]);
    }
}