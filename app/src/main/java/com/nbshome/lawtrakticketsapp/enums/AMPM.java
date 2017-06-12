package com.nbshome.lawtrakticketsapp.enums;


public enum AMPM
{
    AM("AM"), PM("PM");

    private final String value;

    AMPM(String v)
    {
        this.value = v;
    }

    public String toString()
    {
        return value;
    }
}
