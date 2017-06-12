package com.nbshome.lawtrakticketsapp.objects;


import com.nbshome.lawtrakticketsapp.enums.RoadType;

public class Violation
{
    private String violationSectionNo;
    private String violation;
    private String violationDate;
    private String violationTime;
    private String postActSpeed;
    private String points;
    private String BALevel;
    private String vioLocation;
    private String zone;
    private RoadType roadType;
    private String county;
    private String vioLat;
    private String vioLong;
    private String violationCity;
    private boolean courtAppearance;


    public Violation() {
    }

    public Violation(String violationSectionNo, String violation, String violationDate,
                     String violationTime, String postActSpeed, String points, String BALevel,
                     String violationLocation, String county, String vioLat, String vioLong,
                     String violationCity, boolean courtAppearance) {
        this.violationSectionNo = violationSectionNo;
        this.violation = violation;
        this.violationDate = violationDate;
        this.violationTime = violationTime;
        this.postActSpeed = postActSpeed;
        this.points = points;
        this.BALevel = BALevel;
        this.vioLocation = violationLocation;
        this.county = county;
        this.vioLat = vioLat;
        this.vioLong = vioLong;
        this.violationCity = violationCity;
        this.courtAppearance = courtAppearance;
    }

    public String getVioLocation() {
        return vioLocation;
    }

    public void setVioLocation(String vioLocation) {
        this.vioLocation = vioLocation;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public RoadType getRoadType() {
        return roadType;
    }

    public void setRoadType(RoadType roadType) {
        this.roadType = roadType;
    }

    public String getViolationSectionNo() {
        return violationSectionNo;
    }

    public void setViolationSectionNo(String violationSectionNo) {
        this.violationSectionNo = violationSectionNo;
    }

    public String getViolation() {
        return violation;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }

    public String getViolationDate() {
        return violationDate;
    }

    public void setViolationDate(String violationDate) {
        this.violationDate = violationDate;
    }

    public String getViolationTime() {
        return violationTime;
    }

    public void setViolationTime(String violationTime) {
        this.violationTime = violationTime;
    }

    public String getPostActSpeed() {
        return postActSpeed;
    }

    public void setPostActSpeed(String postActSpeed) {
        this.postActSpeed = postActSpeed;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getBALevel() {
        return BALevel;
    }

    public void setBALevel(String BALevel) {
        this.BALevel = BALevel;
    }

    public String getViolationLocation() {
        return vioLocation;
    }

    public void setViolationLocation(String violationLocation) {
        this.vioLocation = violationLocation;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getVioLat() {
        return vioLat;
    }

    public void setVioLat(String vioLat) {
        this.vioLat = vioLat;
    }

    public String getVioLong() {
        return vioLong;
    }

    public void setVioLong(String vioLong) {
        this.vioLong = vioLong;
    }

    public String getViolationCity() {
        return violationCity;
    }

    public void setViolationCity(String violationCity) {
        this.violationCity = violationCity;
    }

    public boolean isCourtAppearance() {
        return courtAppearance;
    }

    public void setCourtAppearance(boolean courtAppearance) {
        this.courtAppearance = courtAppearance;
    }
}
