package com.nbshome.lawtrakticketsapp.enums;


public enum StreetSuffixOne
{
    AVE("Avenue"),
    BLVD("Boulevard"),
    BYP("Bypass"),
    CR("Circle"),
    CT("Court"),
    DR("Drive"),
    EST("Estates"),
    HGTS("Heights"),
    HWY("Highway"),
    LN("Lane"),
    LOOP("Loop"),
    PKWY("Parkway"),
    PLACE("Place"),
    RD("Road"),
    SQUARE("Square"),
    ST("Street"),
    TERR("Terrace"),
    TR("Trail"),
    WAY("Way");


    private final String value;

    StreetSuffixOne(String v)
    {
        this.value = v;
    }

    public String toString()
    {
        return value;
    }
}
