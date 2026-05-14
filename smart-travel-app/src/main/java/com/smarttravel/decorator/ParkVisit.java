package com.smarttravel.decorator;

/**
 * Concrete Decorator: Park Ziyareti
 */
public class ParkVisit extends CityActivityDecorator {
    public ParkVisit(CityVisit visit) {
        super(visit);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Park Yürüyüşü";
    }

    @Override
    public double getCost() {
        return super.getCost() + 5.0;
    } // Küçük bir ücret (Örn: Çay/Kahve)

    @Override
    public double getDuration() {
        return super.getDuration() + 2.0;
    } // 2 saat sürer
}