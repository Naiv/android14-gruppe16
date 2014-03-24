package com.gruppe16.bensinkalkulator;

/**
 * Created by Mikael on 24.03.14.
 */
public class Trip {
    private long id;
    private String trip;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    @Override
    public String toString() {
        return trip;
    }
}