package com.nbshome.lawtrakticketsapp.enums;

/**
 * Created by Owner on 5/9/2017.
 */

public enum Hair
{
    BAL("Bald"),
    BLK("Black"),
    BLN("Blonde or Strawberry"),
    BLU("Blue"),
    BRO("Brown"),
    GRN("Green"),
    GRY("Gray"),
    ONG("Orange"),
    PLE("Purple"),
    PNK("Pink"),
    RED("Red or Auburn"),
    SDY("Sandy"),
    WHI("White"),
    XXX("Unknown");


    private final String value;

    Hair(String v)
    {
        this.value = v;
    }

    public String toString()
    {
        return value;
    }
}
