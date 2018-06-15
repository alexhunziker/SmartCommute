package com.example.compuhypermeganet.smart_commute.model;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//
//SmartCommute
//Leg.java
//
//Alex Hunziker, Xinyuan Cai
//2018
//


public class Leg {
    private Station from;
    private Station to;
    private Date departure;
    private Date arrival;
    private String mode;
    private String line;
    private String direction;


    public Station getFrom() {
        return from;
    }

    public void setFrom(Station from) {
        this.from = from;
    }

    public Station getTo() {
        return to;
    }

    public void setTo(Station to) {
        this.to = to;
    }

    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        this.departure = sdf.parse(departure);
    }

    public Date getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        this.arrival = sdf.parse(arrival);
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }


    public Leg(Station from, Station to) {
        this.from = from;
        this.to = to;
    }

}
