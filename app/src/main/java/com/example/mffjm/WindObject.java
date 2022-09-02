package com.example.mffjm;

public class WindObject {
    private int altitude;		// Thousands of feet AGL
    private int direction;		// Always "true" (not grid or magnetic)
    private int velocity;		// Knots
    private boolean erroneous;
    private int dogLegNum;
    private boolean incompatible;

    public WindObject() {
        this.altitude = 0;
        this.direction = 0;
        this.velocity = 0;
    }

    /**
     * New WindData object
     *
     * @param alt		Altitude in thousands of feet AGL
     * @param dir		Direction (always entered in TRUE - not grid or magnetic)
     * @param vel		Velocity (knots)
     */
    public WindObject(int alt, int dir, int vel) {
        this.altitude = alt;
        this.direction = dir;
        this.velocity = vel;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public boolean isErroneous() {
        return erroneous;
    }

    public void setErroneous(boolean erroneous) {
        this.erroneous = erroneous;
    }

    public int getDogLegNum() {
        return dogLegNum;
    }

    public void setDogLegNum(int dogLegNum) {
        this.dogLegNum = dogLegNum;
    }

    public void setIncompatible(boolean incompatible) {
        this.incompatible = incompatible;
    }

    public boolean isIncompatible() {
        return incompatible;
    }

    @Override
    public String toString() {
        if (altitude == 0)
            return ("Surface\t\t" + direction + " T\t\t" + velocity + " knots");
        else
            return (altitude + ",000\t\t" + (direction) + " T\t\t" + velocity + " knots");
    }

}
