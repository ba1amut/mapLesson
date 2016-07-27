package com.speedsumm.bu.gba2l3;

/**
 * Created by bu on 26.07.2016.
 */
public class Marker {
    double cellLat;
    double cellLon;
    int cellID;
    int id;

    public Marker(double cellLat, double cellLon, int cellID) {
        this.cellLat = cellLat;
        this.cellLon = cellLon;
        this.cellID = cellID;
    }

    public Marker() {

    }

    public double getCellLat() {
        return cellLat;
    }

    public void setCellLat(double cellLat) {
        this.cellLat = cellLat;
    }

    public double getCellLon() {
        return cellLon;
    }

    public void setCellLon(double cellLon) {
        this.cellLon = cellLon;
    }

    public int getCellID() {
        return cellID;
    }

    public void setCellID(int cellID) {
        this.cellID = cellID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
