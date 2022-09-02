package com.example.mffjm;

/* Calculates canopy direction and drift   D = ((A - SF) * (V + FS)) / K
 *
 * Altitude (A): Altitude in thousands of feet
 * Safety Factor (SF): Minimum value of 2 (if dogs legs are encountered, split evenly between upper two legs)
 * Velocity (V): Average velocity under canopy, rounded to the nearest whole number
 * Forward Speed (FS): 22.6 for RA-1, 20.8 for MT2-XX, MJ, MC-4, MMPS, TP400, MP360, HG380 in all modes
 * K factor (K): Constant drift - 36 for RA-1, 48 for MTXX or MC-4/5, MI1, 46 for MMPS 360 and TP 400
 *      31 for MMPS HG-380 (High-glide mode), 39 for MMPS HG-380 (Parachute mode)
 */

import com.example.mffjm.WindObject;
import java.util.ArrayList;

public class CanopyDrift {

    private ArrayList<Double> canopyDriftKM = new ArrayList<Double>();
    private ArrayList<Double> cdAvgDirection = new ArrayList<Double>();
    private String cdDirectionCalc;
    private String cdVelocityCalc;
    private String cdDriftCalc;
    private String cdString;

    public CanopyDrift(int highestAltLowerWindsIndex, ArrayList<WindObject> winds, double cdForwardSpeed, int cdKFactor,
                       int safetyFactor, int gridConvergence) {
        int dogLegIndex = 0;
        ArrayList<Integer> totalVelocity = new ArrayList<Integer>();
        ArrayList<Integer> elevationValues = new ArrayList<Integer>(); 	// How many elevation values are calculated
        ArrayList<Integer> totalDirection = new ArrayList<Integer>();
        ArrayList<Integer> erroneousElevation = new ArrayList<Integer>();
        ArrayList<Integer> highAlts = new ArrayList<Integer>(); 		// used to calculate canopy drift above 10k ft
        totalVelocity.add(dogLegIndex, 0);
        elevationValues.add(dogLegIndex, 0);
        totalDirection.add(0, 0);
        erroneousElevation.add(0, 0);
        highAlts.add(0, 0);

        // calculate total velocity and direction
        for (int elevationIndex = highestAltLowerWindsIndex; elevationIndex >= 0; elevationIndex--) {
            if (!winds.get(elevationIndex).isErroneous()) {
                if (!winds.get(elevationIndex).isIncompatible())
                    totalDirection.set(dogLegIndex, totalDirection.get(dogLegIndex)
                            + winds.get(elevationIndex).getDirection());
                else {
                    if (winds.get(elevationIndex).getDirection() > 270) {
                        totalDirection.set(dogLegIndex, totalDirection.get(dogLegIndex)
                                + winds.get(elevationIndex).getDirection() - 360);
                    }
                    else if (winds.get(elevationIndex).getDirection() < 90) {
                        totalDirection.set(dogLegIndex, totalDirection.get(dogLegIndex)
                                + winds.get(elevationIndex).getDirection() + 360);
                    }
                }
                totalVelocity.set(dogLegIndex, (totalVelocity.get(dogLegIndex)
                        + winds.get(elevationIndex).getVelocity()));
                elevationValues.set(dogLegIndex, elevationValues.get(dogLegIndex) + 1);
                if (elevationIndex > 9)
                    highAlts.set(dogLegIndex, highAlts.get(dogLegIndex) + 1);
                if (elevationIndex != 0) {
                    if (winds.get(elevationIndex).getDogLegNum() < winds.get(elevationIndex - 1).getDogLegNum()) {
                        dogLegIndex++;
                        totalVelocity.add(dogLegIndex, 0);
                        totalDirection.add(dogLegIndex, 0);
                        elevationValues.add(dogLegIndex, 0);
                        erroneousElevation.add(dogLegIndex, 0);
                        highAlts.add(dogLegIndex, 0);
                    }
                }
            }
            if (winds.get(elevationIndex).isErroneous())
                erroneousElevation.set(dogLegIndex, erroneousElevation.get(dogLegIndex) + 1);
        }
        // Calculate average velocity and direction
        ArrayList<Double> cdAvgVelocity = new ArrayList<Double>();
        for (int i = 0; i < elevationValues.size(); i++) {
            cdAvgVelocity.add(i, 0.0);
            cdAvgDirection.add(i, 0.0);
            cdAvgVelocity.set(i, (double)Math.round((double)totalVelocity.get(i) / elevationValues.get(i)));
            cdAvgDirection.set(i, ((double)Math.round((double)totalDirection.get(i) / elevationValues.get(i)))
                    + gridConvergence);
            if (cdAvgDirection.get(i) > 360)
                cdAvgDirection.set(i, cdAvgDirection.get(i) - 360);
            else if (cdAvgDirection.get(i) < 0)
                cdAvgDirection.set(i, cdAvgDirection.get(i) + 360);
        }

        // Get drift in nautical miles. Convert to KM, truncate to 1 decimal place
        if (elevationValues.size() > 1)
            safetyFactor = safetyFactor / 2;
        ArrayList<Double> canopyDriftNM = new ArrayList<Double>();
        for (int i = 0; i < elevationValues.size(); i++) {
            if (i == 0 || i == 1) {
                canopyDriftNM.add(i, ((((elevationValues.get(i) + erroneousElevation.get(i) + highAlts.get(i)) - safetyFactor)
                        * (cdAvgVelocity.get(i) + cdForwardSpeed)) / cdKFactor));
                canopyDriftNM.set(i, Math.floor(canopyDriftNM.get(i) * 10) / 10);
                canopyDriftKM.add(i, canopyDriftNM.get(i) * 1.85);
                canopyDriftKM.set(i, Math.floor(canopyDriftKM.get(i) * 10) / 10);
            }
            else {
                canopyDriftNM.add(i, ((((elevationValues.get(i) + erroneousElevation.get(i) + highAlts.get(i)))
                        * (cdAvgVelocity.get(i) + cdForwardSpeed)) / cdKFactor));
                canopyDriftNM.add(i, Math.floor(canopyDriftNM.get(i) * 10) / 10);
                canopyDriftKM.add(i, canopyDriftNM.get(i) * 1.85);
                canopyDriftKM.set(i, Math.floor(canopyDriftKM.get(i) * 10) / 10);
            }
        }
        String topLeg = "";
        if (elevationValues.size() > 1)
            topLeg += " Top Leg: ";
        String posNeg;
        if (gridConvergence > 0)
            posNeg = " + ";
        else
            posNeg = " - ";
        this.cdDirectionCalc = "CD Direction" + topLeg + totalDirection.get(0) + " / "
                + elevationValues.get(0) + " = " + (cdAvgDirection.get(0) + gridConvergence) + posNeg
                + gridConvergence + " = " + cdAvgDirection.get(0).intValue() + "\u00B0 (grid, rounded)";
        this.cdVelocityCalc = "CD Velocity" + topLeg + totalVelocity.get(0) + " / " + elevationValues.get(0)
                + " = " + cdAvgVelocity.get(0) + " knots (rounded)";
        this.cdDriftCalc = "\nCanopy Drift" + topLeg + "((" + (elevationValues.get(0) + erroneousElevation.get(0) + highAlts.get(0))
                + " - " + safetyFactor + ") * (" + cdAvgVelocity.get(0) + " + " + cdForwardSpeed + ")) / "
                + cdKFactor + " = " + canopyDriftKM.get(0) + " kilometers @ " + cdAvgDirection.get(0)
                + "\u00B0 grid";
        this.cdString = topLeg + canopyDriftKM.get(0) + " kilometers @ " + cdAvgDirection.get(0).intValue() + "\u00B0 grid";

        if (elevationValues.size() > 1) {
            for (int i = 1; i < elevationValues.size(); i++) {
                this.cdDirectionCalc += "\nCD Direction Leg " + (i + 1) + " = " + totalDirection.get(i) + " / "
                        + elevationValues.get(i) + " = " + (cdAvgDirection.get(i) + gridConvergence) + posNeg
                        + gridConvergence + " = " + cdAvgDirection.get(i).intValue() + "\u00B0 (grid, rounded)";
                this.cdVelocityCalc += "\nCD Velocity Leg " + (i + 1) + " = " + totalVelocity.get(i) + " / "
                        + elevationValues.get(i) + " = " + cdAvgVelocity.get(i) + " knots (rounded)";
                if (i == 1)
                    this.cdDriftCalc += "\n\nLeg " + (i + 1) + ": ((" + (elevationValues.get(i)
                            + erroneousElevation.get(i) + highAlts.get(i)) + " - " + safetyFactor + ") * ("+ cdAvgVelocity.get(i)
                            + " + " + cdForwardSpeed + ")) / " + cdKFactor + " = " + canopyDriftKM.get(i)
                            + " kilometers @ " + cdAvgDirection.get(i) + "\u00B0 grid";
                else
                    this.cdDriftCalc += "\nCanopy Drift Leg " + (i + 1) + " = ((" + elevationValues.get(i)
                            + ") * (" + cdAvgVelocity.get(i) + " + " + cdForwardSpeed
                            + ")) / " + cdKFactor + " = " + canopyDriftKM.get(i) + " kilometers @ "
                            + cdAvgDirection.get(i) + "\u00B0 grid";
                this.cdString += "\n\nLeg " + (i + 1) + " - " + canopyDriftKM.get(i)
                        + " kilometers @ " + cdAvgDirection.get(i).intValue() + "\u00B0 grid";
            }
        }
    }
    public ArrayList<Double> getCanopyDriftKM() {
        return canopyDriftKM;
    }
    public ArrayList<Double> getcdAvgDirection() {
        return cdAvgDirection;
    }
    public String getcdDirectionCalc() {
        return cdDirectionCalc;
    }
    public String getcdVelocityCalc() {
        return cdVelocityCalc;
    }
    public String getcdDriftCalc() {
        return cdDriftCalc;
    }
    @Override
    public String toString() {
        return cdString;
    }
}
