package com.smarttravel.composite;

public class Activity implements TravelComponent {
    private String name;

    public Activity(String name) {
        this.name = name;
    }

    @Override
    public void showDetails() {
        System.out.println("Activity: " + name);
    }
}