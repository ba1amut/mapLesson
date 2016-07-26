package com.speedsumm.bu.gba2l3;

/**
 * Created by bu on 26.07.2016.
 */
public class Cell {
    int _mcc;
    int _mnc;
    int _cellid;
    int _lac;
    double _cellLat;
    double _cellLon;

    public Cell(int _mcc, int _mnc, int _cellid, int _lac, double _cellLat, double _cellLon) {
        this._mcc = _mcc;
        this._mnc = _mnc;
        this._cellid = _cellid;
        this._lac = _lac;
        this._cellLat = _cellLat;
        this._cellLon = _cellLon;
    }

    public Cell(int _mcc) {
    }

    public int get_mcc() {
        return _mcc;
    }

    public void set_mcc(int _mcc) {
        this._mcc = _mcc;
    }

    public int get_mnc() {
        return _mnc;
    }

    public void set_mnc(int _mnc) {
        this._mnc = _mnc;
    }

    public int get_cellid() {
        return _cellid;
    }

    public void set_cellid(int _cellid) {
        this._cellid = _cellid;
    }

    public int get_lac() {
        return _lac;
    }

    public void set_lac(int _lac) {
        this._lac = _lac;
    }

    public double get_cellLat() {
        return _cellLat;
    }

    public void set_cellLat(double _cellLat) {
        this._cellLat = _cellLat;
    }

    public double get_cellLon() {
        return _cellLon;
    }

    public void set_cellLon(double _cellLon) {
        this._cellLon = _cellLon;
    }
}
