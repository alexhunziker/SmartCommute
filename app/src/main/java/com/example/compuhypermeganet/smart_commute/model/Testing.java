package com.example.compuhypermeganet.smart_commute.model;

import com.example.compuhypermeganet.smart_commute.API.OpenMap;
import com.example.compuhypermeganet.smart_commute.API.Flinkster;
import com.example.compuhypermeganet.smart_commute.API.rmv;

import java.util.ArrayList;
import java.util.Date;

//
// SmartCommute
// UnitTesting.java
//
// Alex Hunziker, Xinyuan Cai
// 2018
//

public class Testing {

    public static void main(String[] args) throws Exception {

        System.out.print("Get Bike Travel Time...");
        double duration = OpenMap.getDuration(50.108120, 8.631881, 50.116953, 8.649144);
        if (duration == 9.466666666666667) System.out.println("OK");
        else System.out.println("Got unexpected result: " + duration);

        System.out.print("Get nearest station...");
        Station s1 = rmv.get_matching_stops("Europagarten").get(0);
        if (s1.getName().equals("Frankfurt (Main) Europagarten/Messe West"))
            System.out.println("OK");
        else System.out.println("Got unexpected result: " + s1.getName());

        System.out.print("Get nearest CallABike station...");
        BikeStation bs = Flinkster.getNearbyBikeStation(50.108120, 8.631881);
        if (bs == null) System.out.println("Returns null");
        else if (bs.getLat() == 50.108518 && bs.getLon() == 8.629235) System.out.println("OK");
        else System.out.println("Unexpected Location " + bs.getLat() + "," + bs.getLon());

        System.out.print("Try to book a bike near 50.128835, 8.666168 (Campus Westend)...");
        BikeTrip mytrip = new BikeTrip();
        BikeStation startStation = Flinkster.getAvailableBike(50.128835, 8.666168, mytrip, 1000);
        if (mytrip.getAvailability() == 0) System.out.println("No availability detected ");
        else
            System.out.println("Found Bike " + startStation.getReservationLink() + " at " + startStation.getLat() + "," + startStation.getLon());

        System.out.print("Try to book a bike near 50.108120, 8.631881 (Europagarten)...");
        BikeTrip mytrip2 = new BikeTrip();
        BikeStation startStation2 = Flinkster.getAvailableBike(50.108120, 8.631881, mytrip2, 1000);
        if (mytrip.getAvailability() == 0) System.out.println("No availability detected ");
        else
            System.out.println("Found Bike " + startStation2.getReservationLink() + " at " + startStation2.getLat() + "," + startStation2.getLon());

        Station s2 = rmv.get_matching_stops("bockenheimer warte,frankfurt").get(0);
        Trip t = new Trip(s1, s2, new Date());

        System.out.println("--------------------------------------------------------------");
        System.out.println("Trip Information: Search at " + t.getSearchTime() + ", Duration " + t.getDuration() + ", Arrival Time " + t.getArrivalTime() + ", Transfers " + t.getTransfers());
        System.out.println("--------------------------------------------------------------");
        ArrayList<Leg> legs = t.getLegs();
        for (int i = 0; i < legs.size(); i++) {
            Leg cl = legs.get(i);
            System.out.println("At " + cl.getDeparture().getTime() + " by " + cl.getMode() + " " + cl.getLine() + " arrive at " + cl.getArrival().getTime());
            Station from = cl.getFrom();
            Station to = cl.getTo();
            System.out.println("From: " + from.getName() + " at " + from.getX() + "," + from.getY());
            System.out.println("To: " + to.getName() + " at " + to.getX() + "," + to.getY());
            System.out.println("");
        }
        System.out.println("--------------------------------------------------------------");
        BikeTrip bt = t.getBikeTrip();
        if (bt != null) {
            System.out.println("Bike trip with: Availability " + bt.getAvailability() + ", Duration " + bt.getDuration() + ", Distance " + bt.getDistance() + ", timeSaving " + bt.getTimeSaving() + ", Transfer Station " + bt.getTransferStation().getName());
            BikeStation from = bt.getFrom();
            BikeStation to = bt.getTo();
            System.out.println("From: " + from.getAddress() + " at " + from.getLat() + "," + from.getLon() + " with availability " + from.getAvailability() + " requires Walking " + from.getDistToDest() + " Reservation " + from.getReservationLink());
            System.out.println("To: " + to.getAddress() + " at " + to.getLat() + "," + to.getLon() + " requires Walking " + from.getDistToDest());
        } else {
            System.out.println("No Bike Trip in this trip");
        }
        System.out.println("--------------------------------------------------------------");
        System.out.println("--------------------end        -------------------------------");

    }

}
