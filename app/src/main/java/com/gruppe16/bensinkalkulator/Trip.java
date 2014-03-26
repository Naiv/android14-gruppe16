package com.gruppe16.bensinkalkulator;

/**
 * Created by Mikael on 24.03.14.
 */
public class Trip {
    private long id;
    private String datetime;
<<<<<<< HEAD
    private long cost;
    private long distance;
=======
    private String cost;
    private String distance;
>>>>>>> c5d27ba699e3df585172e9c4ba5ed865c712c3f5

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

<<<<<<< HEAD
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
=======
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
>>>>>>> c5d27ba699e3df585172e9c4ba5ed865c712c3f5
        this.distance = distance;
    }


    @Override
    public String toString() {
<<<<<<< HEAD
        return datetime+" - "+Long.toString(distance)+"km "+Long.toString(cost)+",-";
=======
        return datetime+" - "+distance+"km "+cost+",-";
>>>>>>> c5d27ba699e3df585172e9c4ba5ed865c712c3f5
    }

}