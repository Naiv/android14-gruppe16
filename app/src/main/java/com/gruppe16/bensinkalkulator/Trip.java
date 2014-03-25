package com.gruppe16.bensinkalkulator;

/**
 * Created by Mikael on 24.03.14.
 */
public class Trip {
    private long id;
    private String datetime;
    private String cost;
    private String distance;

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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


    @Override
    public String toString() {
        return datetime+" - "+distance+"km "+cost+",-";
    }

}