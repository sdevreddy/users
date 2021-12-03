package com.example.bus_route.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
 
public class Route {
    private String ag, f, l, name;
    private float distance,sid, lU;
    private List<String> seq;
    private Boolean unActive,il,isComplete;
    private String di, poly, rr;

    public void setF(String f) {
        this.f = f;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getSid() {
        return sid;
    }

    public void setSid(float sid) {
        this.sid = sid;
    }

    public Boolean getIl() {
        return il;
    }

    public void setIl(Boolean il) {
        this.il = il;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }


    // \"rr\", \"di\", \"ag\", \"name\", \"l\", \"seq\", \"f\", \"poly\", \"unActive\", \"iu\"


    public Route(String ag, String f, String l, String name, float distance, float sid, List<String> seq, Boolean unActive, Boolean il, Boolean isComplete, String di, float lU, String poly, String rr) {
        this.ag = ag;
        this.f = f;
        this.l = l;
        this.name = name;
        this.distance = distance;
        this.sid = sid;
        this.seq = seq;
        this.unActive = unActive;
        this.il = il;
        this.isComplete = isComplete;
        this.di = di;
        this.lU = lU;
        this.poly = poly;
        this.rr = rr;
    }

    public Route() {

    }

    public String getAg() {
        return ag;
    }

    public void setAg(String ag) {
        this.ag = ag;
    }

    public String getF() {
        return f;
    }

    public void getF(String f) {
        this.f = f;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSeq() {
        return seq;
    }

    public void setSeq(List<String> seq) {
        this.seq = seq;
    }

    public Boolean getUnActive() {
        return unActive;
    }

    public void setUnActive(Boolean unActive) {
        this.unActive = unActive;
    }

    public String getDi() {
        return di;
    }

    public void setDi(String di) {
        this.di = di;
    }

    public float getIU() {
        return lU;
    }

    public void setIU(float lU) {
        this.lU = lU;
    }

    public String getPoly() {
        return poly;
    }

    public void setPoly(String poly) {
        this.poly = poly;
    }

    public String getRr() {
        return rr;
    }

    public void setRr(String rr) {
        this.rr = rr;
    }
}
