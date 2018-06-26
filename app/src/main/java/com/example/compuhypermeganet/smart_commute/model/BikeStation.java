
package com.example.compuhypermeganet.smart_commute.model;

//
// SmartCommute
// BikeStation.java
//
// Alex Hunziker, Xinyuan Cai
// 2018
//

import com.example.compuhypermeganet.smart_commute.API.Flinkster;

public class BikeStation {
    private String address;
    //private String id;	Not used currently
    private double x;
    private double y;
    private double distToDest;
    private int availability;
    private String reservationLink;

    public String getReservationLink() {
        return reservationLink;
    }

    public void setReservationLink(String reservationLink) {
        this.reservationLink = reservationLink;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDistToDest() {
        return distToDest;
    }

    public void setDistToDest(double distToDest) {
        this.distToDest = distToDest;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public double getLon() {
        return x;
    }

    public double getLat() {
        return y;
    }

    // Very rough estimate based on air distance and approximate conversion of lat/lon distance to meters
    public double getWalkingTime() {
        return distToDest/0.071319629;
    }

    public BikeStation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static BikeStation findCloseBikeStation(Station stat) {
        return Flinkster.getNearbyBikeStation(stat.getX(), stat.getY());
    }

    public static BikeStation findFreeBikes(Station stat, BikeTrip bt) {
        return Flinkster.getAvailableBike(stat.getX(), stat.getY(), bt);
    }


}
