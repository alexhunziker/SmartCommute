package com.example.compuhypermeganet.smart_commute.model;

import com.example.compuhypermeganet.smart_commute.API.OpenMap;

import java.util.Date;

public class Car {
    private Date departure;
    private Date arrival;
    private String from;
    private String to;
    private double fromX;
    private double fromY;
    private double toX;
    private double toY;

    public double getFromX() {
        return fromX;
    }

    public double getFromY() {
        return fromY;
    }

    public double getToX() {
        return toX;
    }

    public double getToY() {
        return toY;
    }

    public Date getDeparture(){
        return this.departure;
    }

    public double getDuration(){
        return (this.arrival.getTime() - this.departure.getTime()) / 60_000;
    }

    public Date getArrival(){
        return this.arrival;
    }

    public String getFrom(){
        return this.from;
    }

    public String getTo(){
        return this.to;
    }

    public Car(Station from, Station to, Date departure){
        this.from = from.getName();
        this.to = to.getName();
        this.departure = departure;
        double duration = OpenMap.getCarDuration(from.getX(), from.getY(), to.getX(), to.getY());
        this.fromX = from.getX();
        this.fromY = from.getX();
        this.toX =  to.getX();
        this.toY = to.getY();
        long duration_ms = (long) (duration * 60_000);
        this.arrival = new Date(duration_ms + this.departure.getTime());
    }
}
