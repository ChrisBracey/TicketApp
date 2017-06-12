package com.nbshome.lawtrakticketsapp.enums;

public enum Sex
{
    F("Female"),
    M("Male"),
    U("Unknown");


    private final String value;

    Sex(String v)
    {
        this.value = v;
    }


    public String toString()
    {
        return value;
    }
}
