/**Class of data for each parachute system used. Holds maximum jump altitudes/minimum pull altitudes,
 * forward speed, and K factor. */

package com.example.mffjm;
import java.util.ArrayList;

public class ParachuteType {
    private String name;
    private int maxJumpAlt;
    private int minJumpAlt;
    private int maxPullAlt;
    private int minPullAlt;
    private double forwardSpeed;
    private int kFactor;
    private static ArrayList<ParachuteType> parachuteData = new ArrayList<ParachuteType>(4);

    public ParachuteType() {
        this.name = "Error Parachute Type";
        this.maxJumpAlt = 0;
        this.minJumpAlt = 0;
        this.setMaxPullAlt(0);
        this.minPullAlt = 0;
        this.forwardSpeed = 0;
        this.kFactor = 0;
    }
    /**
     * Creates a new parachute object
     *
     * @param name				Name of chute
     * @param maxJumpAlt		Maximum jump altitude allowed by regulation
     * @param minJumpAlt		Minimum jump altitude allowed by regulation
     * @param maxPullAlt		Maximum pull altitude allowed by regulation
     * @param minPullAlt		Minimum pull altitude allowed by regulation
     * @param forwardSpeed		Forward speed of parachute
     * @param kFactor			K factor of parachute
     */
    public ParachuteType(String name, int maxJumpAlt, int minJumpAlt, int maxPullAlt, int minPullAlt,
                         double forwardSpeed, int kFactor) {
        this.name = name;
        this.maxJumpAlt = maxJumpAlt;
        this.minJumpAlt = minJumpAlt;
        this.setMaxPullAlt(maxPullAlt);
        this.minPullAlt = minPullAlt;
        this.forwardSpeed = forwardSpeed;
        this.kFactor = kFactor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxJumpAlt() {
        return maxJumpAlt;
    }

    public void setMaxJumpAlt (int maxJumpAlt) {
        this.maxJumpAlt = maxJumpAlt;
    }

    public int getMinJumpAlt() {
        return minJumpAlt;
    }

    public void setMinJumpAlt (int minJumpAlt) {
        this.minJumpAlt = minJumpAlt;
    }

    public int getMaxPullAlt() {
        return maxPullAlt;
    }

    public void setMaxPullAlt(int maxPullAlt) {
        this.maxPullAlt = maxPullAlt;
    }

    public int getMinPullAlt() {
        return minPullAlt;
    }

    public void setMinPullAlt(int minPullAlt) {
        this.minPullAlt = minPullAlt;
    }

    public double getForwardSpeed() {
        return forwardSpeed;
    }

    public void setForwardSpeed(double forwardSpeed) {
        this.forwardSpeed = forwardSpeed;
    }

    public int getkFactor() {
        return kFactor;
    }

    public void setkFactor(int kFactor) {
        this.kFactor = kFactor;
    }

    public static ParachuteType getParachuteData(int index) {
        return parachuteData.get(index);
    }

    public static int getNumberOfParachuteTypes() {
        return parachuteData.size();
    }

    public static void loadChutes() {
        // Load parachute data
        parachuteData.add(new ParachuteType("RA-1", 35000, 5000, 30000, 4500, 22.6, 36));		// need to double check
        parachuteData.add(new ParachuteType("MC-4", 30000, 5000, 30000, 4500, 20.8, 48));		// max/min values for all
        parachuteData.add(new ParachuteType("MMPS 360", 40000, 5000, 30000, 3500, 20.8, 46));	// parachutes
        //add HG other mode
        parachuteData.add(new ParachuteType("HG-380 (Parachute Mode)", 35000, 5000, 30000, 3500, 20.8, 39));
    }

    @Override
    public String toString() {
        return "Name: " + name
                + "\nMaximum jump altitude: " + maxJumpAlt
                + "\nMinimum pull altitude: " + minPullAlt
                + "\nForward Speed: " + forwardSpeed
                + "\nK Factor: " + kFactor;
    }
}
