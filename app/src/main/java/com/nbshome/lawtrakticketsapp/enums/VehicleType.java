package com.nbshome.lawtrakticketsapp.enums;

/**
 * Created by Owner on 5/9/2017.
 */

public enum VehicleType
{
    AUTO("Auto"),
    VEH16PSG("16 Passenger Vehicle"),
    BICYCLE("Bicycle"),
    COMB("Combo"),
    COMMVEH("Commercial Vehicle"),
    HAZMAT("Hazardous Material"),
    MOPED("Moped"),
    MTRCYCL("Motorcycle"),
    PEDESTRIAN("Pedestrian"),
    OTHER("Other");

    private final String value;

    VehicleType(String v)
    {
        this.value = v;
    }

    public String toString()
    {
        return value;
    }
}
