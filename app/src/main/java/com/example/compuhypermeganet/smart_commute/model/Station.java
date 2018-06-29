package com.example.compuhypermeganet.smart_commute.model;

import org.w3c.dom.Element;

//
// SmartCommute
// Station.java
//
// Alex Hunziker, Xinyuan Cai
// 2018
//


public class Station {
    private String name;

    public Station(String id) {
        this.id = id;
    }


    private String id;
    private double x;
    private double y;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Station(String name, String id, double x, double y) {
        this.name = name;
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Station(Element station) {
        this.name = station.getAttribute("name");
        this.id = station.getAttribute("id");
        this.x = Double.parseDouble(station.getAttribute("lat"));
        this.y = Double.parseDouble(station.getAttribute("lon"));
//        Log.d("destination_id", this.id );
    }


}
