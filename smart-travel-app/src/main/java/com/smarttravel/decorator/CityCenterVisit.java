package com.smarttravel.decorator;

/**
 * Concrete Decorator: Tarihi Şehir Merkezi Ziyareti
 */
public class CityCenterVisit extends CityActivityDecorator {
    public CityCenterVisit(CityVisit visit) {
        super(visit);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Şehir Merkezi Turu";
    }

    @Override
    public double getCost() {
        return super.getCost() + 10.0;
    } // Rehberlik vs. ücreti

    @Override
    public double getDuration() {
        return super.getDuration() + 3.5;
    } // 3.5 saat sürer
}