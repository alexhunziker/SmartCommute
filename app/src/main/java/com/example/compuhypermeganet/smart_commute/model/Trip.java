package com.example.compuhypermeganet.smart_commute.model;

import android.util.Log;

import com.example.compuhypermeganet.smart_commute.API.rmv;

import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

//
// SmartCommute
// Trip.java
//
// Alex Hunziker, Xinyuan Cai
// 2018
//

public class Trip {
    private ArrayList<Leg> legs;
    private Date searchTime;
    private double duration;
    private Date departureTime;//origin departure time
    private Date arrivalTime;//origin arrival time
    private int transfers;
    private BikeTrip bikeTrip;

    public BikeTrip getBikeTrip() {
        return bikeTrip;
    }

    public boolean bikeTripexist() {
        if (this.bikeTrip == null || "".equals(this.bikeTrip)) {
            return false;
        }
        return true;

    }

    public double getDuration() {
        return duration;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }


    public ArrayList<Leg> getLegs() {
        return legs;
    }

    public Date getSearchTime() {
        return searchTime;
    }

    public int getTransfers() {
        return transfers;
    }

    public Trip() {                            // Minimal constructor for testing only
        this.legs = new ArrayList<Leg>();
        this.searchTime = new Date();
        this.transfers = 0;
    }

    public Trip(Station origin, Station destination, Date time) {
        this.searchTime = time;
        this.legs = new ArrayList<Leg>();
        this.bikeTrip = null;

        // Get Trip Information from RMV
        Document xmlTrip = rmv.getTrip(origin, destination, time);

        // Adding the Legs to the trip
        try {
            NodeList trips = xmlTrip.getElementsByTagName("Trip");
            Element trip_raw, leg_raw, origin_raw, destination_raw;
            Station origin_leg, destination_leg;
            Leg leg;
            NodeList legs;
            String type;
            for (int i = 0; i < trips.getLength(); i++) {
                trip_raw = (Element) trips.item(i);
                legs = trip_raw.getElementsByTagName("Leg");
                for (int j = 0; j < legs.getLength(); j++) {
                    leg_raw = (Element) legs.item(j);
                    origin_raw = (Element) leg_raw.getElementsByTagName("Origin").item(0);
                    destination_raw = (Element) leg_raw.getElementsByTagName("Destination").item(0);
                    origin_leg = new Station(origin_raw);
                    destination_leg = new Station(destination_raw);
                    leg = new Leg(origin_leg, destination_leg);
                    leg.setArrival(destination_raw.getAttribute("time"));
                    leg.setDeparture(origin_raw.getAttribute("time"));
                    Log.d("datetime", leg.getArrival().toString());
                    type = leg_raw.getAttribute("type");
                    if (!type.equals("JNY")) {
                        leg.setMode(type.trim());
                        System.out.print("1." + leg.getMode());
                    } else {
                        leg.setMode(leg_raw.getAttribute("category").trim());
                        leg.setLine(leg_raw.getAttribute("name").trim());
                        leg.setDirection(leg_raw.getAttribute("direction").trim());
                        System.out.print("2." + leg.getMode());
                        System.out.print("3." + leg.getLine());
                        System.out.print("4." + leg.getDirection());
                    }
                    if (!leg.getMode().equals("KISS")) {
                        this.addLeg(leg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Determine Departure and Arrival Time
        this.departureTime = this.legs.get(0).getDeparture();
        this.arrivalTime = this.legs.get(this.legs.size() - 1).getArrival();
        this.duration = (this.arrivalTime.getTime() - this.departureTime.getTime()) / 60_000; // stored in minutes

        // Find Bike option
        BikeStation potEnd = BikeStation.findCloseBikeStation(this.legs.get(this.legs.size() - 1).getTo());
        BikeTrip potBikeTrip;
        double remCommuteTime;
        //Look for Bike Station at destination
        if (potEnd == null) return;
        // Check for Each Intermediate Station & Compare Times
        for (int i = 0; i < this.legs.size(); i++) {
            System.out.println("Activity: Searching bike option for: " + this.legs.get(i).getFrom().getName() + " at " + this.legs.get(i).getFrom().getX() + "," + this.legs.get(i).getFrom().getY() + " to " + potEnd.getLat() + "," + potEnd.getLon());
            potBikeTrip = new BikeTrip(this.legs.get(i).getFrom(), potEnd);
            if (potBikeTrip == null)
                continue;        // Always False, because Object will be constructed anyways. Should give correct results anyways though. If not this needs to be fixed.
            remCommuteTime = (this.getArrivalTime().getTime() - legs.get(i).getDeparture().getTime()) / 60_000;
            potBikeTrip.setTimeSaving(remCommuteTime - potBikeTrip.getDuration());
            System.out.println("Info: Bike option " + potBikeTrip.getDuration() + "vs remaining commute" + remCommuteTime);
            if (potBikeTrip.getTimeSaving() > 0) {
                System.out.print("Info: Bike Option found");
                // If availability and time Saving found -> break. (Maybe better: look for maximum)
                if (potBikeTrip.getAvailability() > 0) {
                    if (this.bikeTrip == null || potBikeTrip.getTimeSaving() > this.bikeTrip.getTimeSaving())
                        this.bikeTrip = potBikeTrip;
                    else System.out.print("...But less attractive that existing one");
                    System.out.println(".");
                } else System.out.println("...But apparantly no availability of bikes.");
            }
        }
    }

    public void addLeg(Leg newLeg) {
        legs.add(newLeg);
        this.transfers += 1;
    }

}
