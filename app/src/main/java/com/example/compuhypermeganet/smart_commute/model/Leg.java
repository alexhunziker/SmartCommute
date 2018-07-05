package com.example.compuhypermeganet.smart_commute.model;

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

    public void setDeparture(String date, String time) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.departure = sdf.parse(date + " " + time);
    }

    public Date getArrival() {
        return arrival;
    }

    public void setArrival(String date, String time) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.departure = sdf.parse(date + " " + time);
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
