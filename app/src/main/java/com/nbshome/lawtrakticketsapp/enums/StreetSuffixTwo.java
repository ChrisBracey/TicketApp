package com.nbshome.lawtrakticketsapp.enums;

/**
 * Created by Owner on 5/9/2017.
 */

public enum StreetSuffixTwo
{
    E("East"),
    EXT("Extension"),
    N("North"),
    S("South"),
    W("West");

    private final String value;

    StreetSuffixTwo(String v)
    {
        this.value = v;
    }

    public String toString()
    {
        return value;
    }
}
