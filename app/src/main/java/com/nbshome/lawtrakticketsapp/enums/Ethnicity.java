package com.nbshome.lawtrakticketsapp.enums;


public enum Ethnicity
{
    H("Hispanic or Latino"),
    N("Non-Hispanic or Latino"),
    U("Unknown");


    private final String value;

    Ethnicity(String v)
    {
        this.value = v;
    }

    public String toString()
    {
        return value;
    }
}
