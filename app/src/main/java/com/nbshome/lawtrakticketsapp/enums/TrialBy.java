package com.nbshome.lawtrakticketsapp.enums;

/**
 * Created by Owner on 5/9/2017.
 */

public enum TrialBy
{
    JURY("Jury"),
    TRIAL_OFF("Trial Officer");


    private final String value;

    TrialBy(String v)
    {
        this.value = v;
    }

    public String toString()
    {
        return value;
    }
}
