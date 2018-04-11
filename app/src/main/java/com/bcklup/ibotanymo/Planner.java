package com.bcklup.ibotanymo;

/**
 * Created by gians on 12/04/2018.
 */

public class Planner{
    public int id;
    public int soil;
    public String date;

    public Planner(int id, String date) {
        this.id = id;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSoil() {
        return soil;
    }

    public void setSoil(int soil) {
        this.soil = soil;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}