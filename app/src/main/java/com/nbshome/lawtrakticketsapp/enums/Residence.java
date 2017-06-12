package com.nbshome.lawtrakticketsapp.enums;

/**
 * Created by Owner on 5/9/2017.
 */

public enum Residence
{
    J("Jurisdiction"),
    O("Out of State"),
    S("State"),
    U("Unknown");


    private final String value;

    Residence(String v)
    {
        this.value = v;
    }

    public String toString()
    {
        return value;
    }
}
