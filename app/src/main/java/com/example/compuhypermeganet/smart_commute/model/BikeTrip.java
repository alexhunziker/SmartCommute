package com.example.compuhypermeganet.smart_commute.model;

//
// SmartCommute
// BikeTrip.java
//
// Alex Hunziker, Xinyuan Cai
// 2018
//


import com.example.compuhypermeganet.smart_commute.API.OpenMap;

import java.net.URL;
import java.util.Date;

public class BikeTrip {
    private BikeStation from;
    private BikeStation to;
    private Station transferStation;
    private double duration;
    private double distance;
    private int availability;
    private double timeSaving;
    private Date arrivalTime;
    private Date departureTime;

    public Station getTransferStation() {
        return transferStation;
    }

    public double getDistance() {
        return distance;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTimeSaving() {
        return timeSaving;
    }

    public void setTimeSaving(double mins) {
        this.timeSaving = mins;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public BikeStation getFrom() {
        return from;
    }

    public BikeStation getTo() {
        return to;
    }

    public double getDuration() {
        return duration;
    }

    public BikeTrip() {
        this.availability = 0;
    }

    public BikeTrip(Station fromStation, BikeStation to) {
        BikeStation from = BikeStation.findFreeBikes(fromStation, this);
        // If no free Bikes at start, check availability of Stations
        if (from == null) {
            System.out.println("Info: No free Bookingproposals received...");
            System.out.println("Activity: Now searching station close to " + fromStation.getX() + "," + fromStation.getY());
            from = BikeStation.findCloseBikeStation(fromStation);
            if (from == null) return;
            System.out.println("Info: But Bike Station at " + from.getLat() + "," + from.getLon());
            this.availability = 0;
        }
        this.from = from;
        this.to = to;
        OpenMap.setDurationDistance(from.getLat(), from.getLon(), to.getLat(), to.getLon(), this);
        this.transferStation = fromStation;
    }

    public void setTimes(Date departureTime) {
        this.departureTime = departureTime;
        int totalBikeDuration = (int) (60000 * this.duration + this.to.getWalkingTime() + this.from.getWalkingTime());
        this.arrivalTime = new Date(departureTime.getTime() + totalBikeDuration);
        // System.out.println("Bike departure time = " + this.departureTime + " arriving at " + this.arrivalTime);
    }

    public URL getGoogleMapsLink() throws Exception {
        return new URL("https://www.google.com/maps/dir/'" + this.from.getLat() + "," +
                    this.from.getLon() + "'/'" + this.to.getLat() + "," + this.to.getLon() + "'/");
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }
}
