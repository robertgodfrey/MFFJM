/**
 * Stores operational data input by user.
 */

package com.example.mffjm;

public class OperationalData {
    private String gzd;
    private int dipEasting;
    private int dipNorthing;
    private int dzElevation;
    private int exitAltAGL;
    private int pullAltAGL;
    private ParachuteType parachute;
    private boolean isHighPerformance;
    private int aircraftHeadingMagnetic;
    private int numOfJumpers;
    private int safetyFactor;
    private int gmAngle;
    private int gridConvergence;

    public OperationalData() {
    }

    public OperationalData(String gzd, int dipEasting, int dipNorthing, int dzElevation, int exitAltAGL, int pullAltAGL, ParachuteType parachute,
                           boolean isHighPerformance, int aircraftHeadingMagnetic, int numOfJumpers, int safetyFactor, int gmAngle, int gridConvergence) {
        this.gzd = gzd;
        this.dzElevation = dzElevation;
        this.pullAltAGL = pullAltAGL;
        this.exitAltAGL = exitAltAGL;
        this.dipEasting = dipEasting;
        this.dipNorthing = dipNorthing;
        this.isHighPerformance = isHighPerformance;
        this.aircraftHeadingMagnetic = aircraftHeadingMagnetic;
        this.numOfJumpers = numOfJumpers;
        this.parachute = parachute;
        this.safetyFactor = safetyFactor;
        this.gmAngle = gmAngle;
        this.gridConvergence = gridConvergence;
    }

    public void setGzd(String gzd) {
        this.gzd = gzd;
    }

    public String getGzd() {
        return gzd;
    }

    public int getDzElevation() {
        return dzElevation;
    }

    public void setDzElevation(int dzElevation) {
        this.dzElevation = dzElevation;
    }

    public int getPullAltAGL() {
        return pullAltAGL;
    }

    public void setPullAltAGL(int pullAltAGL) {
        this.pullAltAGL = pullAltAGL;
    }

    public int getExitAltAGL() {
        return exitAltAGL;
    }

    public void setExitAltAGL(int exitAltAGL) {
        this.exitAltAGL = exitAltAGL;
    }

    public int getDipEasting() {
        return dipEasting;
    }

    public void setDipEasting(int dipEasting) {
        this.dipEasting = dipEasting;
    }

    public int getDipNorthing() {
        return dipNorthing;
    }

    public void setDipNorthing(int dipNorthing) {
        this.dipNorthing = dipNorthing;
    }

    public boolean isHighPerformance() {
        return isHighPerformance;
    }

    public void setHighPerformance(boolean isHighPerformance) {
        this.isHighPerformance = isHighPerformance;
    }

    public int getAircraftHeadingMagnetic() {
        return aircraftHeadingMagnetic;
    }

    public void setAircraftHeadingMagnetic(int aircraftHeadingMagnetic) {
        this.aircraftHeadingMagnetic = aircraftHeadingMagnetic;
    }

    public int getNumOfJumpers() {
        return numOfJumpers;
    }

    public void setNumOfJumpers(int numOfJumpers) {
        this.numOfJumpers = numOfJumpers;
    }

    public ParachuteType getParachuteType() {
        return parachute;
    }

    public void setParachuteType(ParachuteType parachute) {
        this.parachute = parachute;
    }

    public int getSafetyFactor() {
        return safetyFactor;
    }

    public void setSafetyFactor(int safetyFactor) {
        this.safetyFactor = safetyFactor;
    }

    public int getGmAngle() {
        return gmAngle;
    }

    public void setGmAngle(int gmAngle) {
        this.gmAngle = gmAngle;
    }

    public int getGridConvergence() {
        return gridConvergence;
    }

    public void setGridConvergence() {
        this.gridConvergence = gridConvergence;
    }
}