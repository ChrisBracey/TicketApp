package com.nbshome.lawtrakticketsapp.objects;

import com.nbshome.lawtrakticketsapp.enums.State;

/**
 * Created by Owner on 5/9/2017.
 */

public class TrialCourt
{
    private String court;
    private String courtAddr;
    private String courtCity;
    private String courtZip;
    private String courtDate;
    private String courtTime;
    private String courtState;



    public TrialCourt() {
    }

    public TrialCourt(String court, String courtAddr, String courtCity, String courtZip,
                      String courtDate, String courtTime, String courtState)
    {
        this.court = court;
        this.courtAddr = courtAddr;
        this.courtCity = courtCity;
        this.courtZip = courtZip;
        this.courtDate = courtDate;
        this.courtTime = courtTime;
        this.courtState = courtState;
    }


    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public String getCourtAddr() {
        return courtAddr;
    }

    public void setCourtAddr(String courtAddr) {
        this.courtAddr = courtAddr;
    }

    public String getCourtCity() {
        return courtCity;
    }

    public void setCourtCity(String courtCity) {
        this.courtCity = courtCity;
    }

    public String getCourtZip() {
        return courtZip;
    }

    public void setCourtZip(String courtZip) {
        this.courtZip = courtZip;
    }

    public String getCourtDate() {
        return courtDate;
    }

    public void setCourtDate(String courtDate) {
        this.courtDate = courtDate;
    }

    public String getCourtTime() {
        return courtTime;
    }

    public void setCourtTime(String courtTime) {
        this.courtTime = courtTime;
    }

    public String getCourtState() {
        return courtState;
    }

    public void setCourtState(String courtState) {
        this.courtState = courtState;
    }
}
