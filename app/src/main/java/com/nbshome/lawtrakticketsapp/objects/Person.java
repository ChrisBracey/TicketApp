package com.nbshome.lawtrakticketsapp.objects;


import com.nbshome.lawtrakticketsapp.enums.Country;
import com.nbshome.lawtrakticketsapp.enums.Ethnicity;

import java.util.ArrayList;

public class Person {
    private String fName;
    private String mName;
    private String lName;
    private String fullName;
    private String suffix;
    private String addr;
    private String city;
    private String zip;
    private String dlNum;
    private String dlClass;
    private String dob;
    private String height;
    private String weight;
    private String state, dlState;
    private String hair;
    private String eyes;
    private String sex;
    private boolean cdl;
    private String race;
    private Country country;
    private String phone;
    private String ssn;
    private Ethnicity ethnicity;
    private String residence;
    private Vehicle vehicle;
    private ArrayList<Ticket> tickets;


    public Person() {
        tickets = new ArrayList<Ticket>();
    }

    public Person(String fName, String mName, String lName, String suffix, String addr,
                  String city, String zip, String dlNum, String dlClass, String dob,
                  String height, String weight, String state, String dlState, String hair, String eyes,
                  String sex, boolean cdl, String race, Country country, String phone, String ssn,
                  Ethnicity ethnicity, String residence, ArrayList<Ticket> tickets)
    {
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.suffix = suffix;
        this.addr = addr;
        this.city = city;
        this.zip = zip;
        this.dlNum = dlNum;
        this.dlClass = dlClass;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.state = state;
        this.dlState = dlState;
        this.hair = hair;
        this.eyes = eyes;
        this.sex = sex;
        this.cdl = cdl;
        this.race = race;
        this.country = country;
        this.phone = phone;
        this.ssn = ssn;
        this.ethnicity = ethnicity;
        this.residence = residence;
        this.tickets = tickets;
    }

    public Person(String fullName, String addr,
                  String city, String zip, String state)
    {
        this.fullName = fullName;
        this.addr = addr;
        this.city = city;
        this.zip = zip;
        this.state = state;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Ethnicity getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(Ethnicity ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getfName() {

        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getDlNum() {
        return dlNum;
    }

    public void setDlNum(String dlNum) {
        this.dlNum = dlNum;
    }

    public String getDlClass() {
        return dlClass;
    }

    public void setDlClass(String dlClass) {
        this.dlClass = dlClass;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDlState() {
        return dlState;
    }

    public void setDlState(String dlState) {
        this.dlState = dlState;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getEyes() {
        return eyes;
    }

    public void setEyes(String eyes) {
        this.eyes = eyes;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public boolean isCdl() {
        return cdl;
    }

    public void setCdl(boolean cdl) {
        this.cdl = cdl;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }


}
