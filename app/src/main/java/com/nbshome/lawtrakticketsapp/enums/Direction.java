package com.nbshome.lawtrakticketsapp.enums;

/**
 * Created by Owner on 5/9/2017.
 */

public enum Direction
{
    E("East"),
    N("North"),
    NE("North-East"),
    NW("North-West"),
    S("South"),
    SE("South-East"),
    SW("South-West"),
    W("West");

    private final String value;

    Direction(String v)
    {
        this.value = v;
    }

    public String toString()
    {
        return value;
    }
}
