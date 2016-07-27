package com.speedsumm.bu.gba2l3;


public class Marker {
    double cellLat;
    double cellLon;
    int cellID;
    int id;
    int cellLac;
    int cellMcc;
    int cellMnc;

    public Marker(int cellMcc, int cellMnc, int cellLac, int cellID, double cellLat, double cellLon) {
        this.cellLat = cellLat;
        this.cellLon = cellLon;
        this.cellID = cellID;
        this.cellLac = cellLac;
        this.cellMcc = cellMcc;
        this.cellMnc = cellMnc;
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

    public int getCellLac() {

        return cellLac;
    }

    public void setCellLac(int cellLac) {
        this.cellLac = cellLac;
    }

    public int getCellMcc() {
        return cellMcc;
    }

    public void setCellMcc(int cellMcc) {
        this.cellMcc = cellMcc;
    }

    public int getCellMnc() {
        return cellMnc;
    }

    public void setCellMnc(int cellMnc) {
        this.cellMnc = cellMnc;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Marker)) return false;

        Marker marker = (Marker) o;

        if (cellID != marker.cellID) return false;
        if (cellLac != marker.cellLac) return false;
        if (cellMcc != marker.cellMcc) return false;
        return cellMnc == marker.cellMnc;

    }

    @Override
    public int hashCode() {
        int result = cellID;
        result = 31 * result + cellLac;
        result = 31 * result + cellMcc;
        result = 31 * result + cellMnc;
        return result;
    }
}
