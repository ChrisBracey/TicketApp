package com.nbshome.lawtrakticketsapp.enums;

/**
 * Created by Owner on 5/9/2017.
 */

public enum Day
{
    FR("Friday"),
    MO("Monday"),
    SA("Saturday"),
    SU("Sunday"),
    TH("Thursday"),
    TU("Tuesday"),
    WE("Wednesday");


    private final String value;

    Day(String v)
    {
        this.value = v;
    }

    public String toString()
    {
        return value;
    }
}
