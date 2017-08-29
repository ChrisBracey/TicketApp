package com.nbshome.lawtrakticketsapp.objects;


import java.io.Serializable;

public class ArrestOfficer implements Serializable
{

    private String offName;
    private String offNum;
    private String bailDeposited;
    private String arrestDate;
    private String bondAmtReq;

    public ArrestOfficer() {
    }

    public ArrestOfficer(String offName, String offNum, String bailDeposited, String arrestDate, String bondAmtReq) {
        this.offName = offName;
        this.offNum = offNum;
        this.bailDeposited = bailDeposited;
        this.arrestDate = arrestDate;
        this.bondAmtReq = bondAmtReq;
    }

    public String getOffName() {
        return offName;
    }

    public void setOffName(String offName) {
        this.offName = offName;
    }

    public String getOffNum() {
        return offNum;
    }

    public void setOffNum(String offNum) {
        this.offNum = offNum;
    }

    public String getBailDeposited() {
        return bailDeposited;
    }

    public void setBailDeposited(String bailDeposited) {
        this.bailDeposited = bailDeposited;
    }

    public String getArrestDate() {
        return arrestDate;
    }

    public void setArrestDate(String arrestDate) {
        this.arrestDate = arrestDate;
    }

    public String getBondAmtReq() {
        return bondAmtReq;
    }

    public void setBoundAmtReq(String boundAmtReq) {
        this.bondAmtReq = bondAmtReq;
    }
}
