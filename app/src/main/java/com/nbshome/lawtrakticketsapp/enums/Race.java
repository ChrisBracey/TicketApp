package com.nbshome.lawtrakticketsapp.enums;

public enum Race
{
    A("Asian"),
    B("Black / African American"),
    I("American Indian / Alaskan Native"),
    P("Native Hawaiian / Other Pacific Islander"),
    U("Unknown"),
    W("White");


    private final String value;

    Race(String v)
    {
        this.value = v;
    }

    public String toString()
    {
        return value;
    }
}
