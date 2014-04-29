package com.gruppe16.bensinkalkulator;

//klasse som tar vare på all data fra én tur.
public class Trip {
    private long id;
    private String datetime;
    private long cost;
    private long distance;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }


    @Override
    public String toString() {
        return datetime+" - "+Long.toString(distance)+"km "+Long.toString(cost)+",-";
    }

}