package com.nbshome.lawtrakticketsapp.objects;

import java.io.Serializable;

public class Ticket implements Serializable
{
    private Person owner;
    private TrialCourt court;
    private Violation violation;
    private ArrestOfficer officer;

    public ArrestOfficer getOfficer() {
        return officer;
    }

    public void setOfficer(ArrestOfficer officer) {
        this.officer = officer;
    }

    public Violation getViolation() {
        return violation;
    }

    public void setViolation(Violation violation) {
        this.violation = violation;
    }


    public TrialCourt getCourt() {
        return court;
    }

    public void setCourt(TrialCourt court) {
        this.court = court;
    }


    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

}
