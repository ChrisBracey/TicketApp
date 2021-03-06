package com.nbshome.lawtrakticketsapp.objects;

import com.nbshome.lawtrakticketsapp.enums.State;
import com.nbshome.lawtrakticketsapp.enums.VehicleType;

import java.io.Serializable;


public class Vehicle implements Serializable
{
    private String make;
    private String year;
    private String plateNum;
    private String plateState;
    private String type;

    public Vehicle() {
    }

    public Vehicle(String make, String year, String plateNum, String plateState, String type) {
        this.make = make;
        this.year = year;
        this.plateNum = plateNum;
        this.plateState = plateState;
        this.type = type;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getPlateState() {
        return plateState;
    }

    public void setPlateState(String plateState) {
        this.plateState = plateState;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
