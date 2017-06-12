package com.nbshome.lawtrakticketsapp.enums;

public enum RoadType {
    CTY("County"),
    I("Interstate"),
    O("Other"),
    PP("Private Property"),
    R("Ramp"),
    SC("SC Primary"),
    SEC("Secondary"),
    US("US Primary");


    private final String value;

    RoadType(String v)
    {
        this.value = v;
    }

    public String toString()
    {
        return value;
    }
}
