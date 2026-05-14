package com.smarttravel.composite;

import java.util.ArrayList;
import java.util.List;

public class ActivityPlan implements TravelComponent {
    private List<TravelComponent> components = new ArrayList<>();

    public void addComponent(TravelComponent component) {
        components.add(component);
    }

    public void removeComponent(TravelComponent component) {
        components.remove(component);
    }

    @Override
    public void showDetails() {
        for (TravelComponent component : components) {
            component.showDetails();
        }
    }
}