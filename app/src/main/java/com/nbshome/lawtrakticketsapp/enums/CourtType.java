package com.nbshome.lawtrakticketsapp.enums;

/**
 * Created by Owner on 5/9/2017.
 */

public enum CourtType
{
    CIRCUIT("Circuit"),
    FAMILY("Family"),
    FEDERAL("Federal"),
    MAGISTRATE("Magistrate"),
    MUNICIPAL("Municipal"),
    OTHER("Other");


    private final String value;

    CourtType(String v)
    {
        this.value = v;
    }

    public String toString()
    {
        return value;
    }
}
