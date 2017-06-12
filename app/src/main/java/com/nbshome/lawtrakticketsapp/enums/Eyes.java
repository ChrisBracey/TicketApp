package com.nbshome.lawtrakticketsapp.enums;

public enum Eyes
{
    BLK("Black"),
    BLU("Blue"),
    BRO("Brown"),
    GRN("Green"),
    GRY("Gray"),
    HAZ("Hazel"),
    MAR("Maroon"),
    MUL("Multi-Colored"),
    PNK("Pink"),
    XXX("Unknown");

    private final String value;

    Eyes(String v)
    {
        this.value = v;
    }

    public String toString()
    {
        return value;
    }
}
