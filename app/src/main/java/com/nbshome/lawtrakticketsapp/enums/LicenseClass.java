package com.nbshome.lawtrakticketsapp.enums;


public enum LicenseClass
{

    A("Comb Veh > 26000 + Trailer - no Motorcycle"),
    AM("Class A + All Classes of Licenses + Motorcycle"),
    B("Single Unit Veh > 26000 or Tow < 10000 - no Motorcycle"),
    BM("Class B + Classes C D E F G + Motorcycle"),
    C("Single or Comb Veh 16+ Pass Placard HazMat"),
    CM("Class C + Classes D E F G + Motorcycle"),
    D("Veh < 26000 - no Motorcycle"),
    DM("Class D + Mopeds + Motorcycle"),
    E("Single Unit Veh > 26000 - no Motorcycle"),
    EM("Class E + Classes D G + Motorcycle"),
    F("Comb Veh > 26000 - no Motorcycle"),
    FM("Class F + Motorcycle"),
    G("Mopeds Only"),
    I("ID Card"),
    M("Motorcycle Only");


    private final String value;

    LicenseClass(String v)
    {
        this.value = v;
    }

   /* public String toString()
    {
        return value;
    }*/
}
