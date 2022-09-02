package com.example.mffjm;

/** Calculates free fall direction and drift (FFD): D = KAV
 * K factor (K): Constant of 3 in freefall
 * Altitude (A): Altitude in thousands of feet in freefall
 * Velocity (V): Average velocity during freefall, rounded to the nearest whole number */

import java.util.ArrayList;

public class FreefallDrift {

    private ArrayList<Double> ffDriftMeters = new ArrayList<Double>();
    private ArrayList<Double> ffAvgDirection = new ArrayList<Double>();
    private String ffDirectionCalc;
    private String ffVelocityCalc;
    private String ffDriftCalc;
    private String ffDriftString;

    /**
     * @param pullAltAGL		Pull altitude in ft AGL		// exit altitude not needed, obtained from wind data
     * @param winds				Load wind array
     */
    public FreefallDrift(int pullAltAGL, ArrayList<WindObject> winds, int gridConvergence) {

        int dogLegIndex = 0;
        ArrayList<Integer> totalVelocity = new ArrayList<Integer>();
        ArrayList<Integer> elevationValues = new ArrayList<Integer>(); 	// How many elevation values are calculated
        ArrayList<Integer> totalDirection = new ArrayList<Integer>();
        ArrayList<Integer> erroneousElevation = new ArrayList<Integer>();
        totalVelocity.add(dogLegIndex, 0);
        elevationValues.add(dogLegIndex, 0);
        totalDirection.add(0, 0);
        erroneousElevation.add(0, 0);

        // calculate total velocity and direction
        for (int elevationIndex = winds.size() - 1; elevationIndex > pullAltAGL / 1000 - 1; elevationIndex--) {
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
                if (winds.get(elevationIndex).getDogLegNum() < winds.get(elevationIndex - 1).getDogLegNum()) {
                    dogLegIndex++;
                    totalVelocity.add(dogLegIndex, 0);
                    totalDirection.add(dogLegIndex, 0);
                    elevationValues.add(dogLegIndex, 0);
                    erroneousElevation.add(dogLegIndex, 0);
                }
            }
            if (winds.get(elevationIndex).isErroneous())
                erroneousElevation.set(dogLegIndex, erroneousElevation.get(dogLegIndex) + 1);
        }
        // Calculate average velocity and direction
        ArrayList<Double> ffAvgVelocity = new ArrayList<Double>();
        for (int i = 0; i < elevationValues.size(); i++) {
            ffAvgVelocity.add(i, 0.0);
            ffAvgDirection.add(i, 0.0);
            ffAvgVelocity.set(i, (double)Math.round((double)totalVelocity.get(i) / elevationValues.get(i)));
            ffAvgDirection.set(i, (double)Math.round((double)totalDirection.get(i) / elevationValues.get(i))
                    + gridConvergence);
            if (ffAvgDirection.get(i) > 359)
                ffAvgDirection.set(i, ffAvgDirection.get(i) - 360);
        }
        int ffKFactor = 3;
        for (int i = 0; i < elevationValues.size(); i++)
            ffDriftMeters.add(i, (ffKFactor * ((elevationValues.get(i) + erroneousElevation.get(i)) * 2)
                    * ffAvgVelocity.get(i)));

        String topLeg = ": ";
        if (elevationValues.size() > 1)
            topLeg = " Top Leg: ";
        String posNeg;
        if (gridConvergence > 0)
            posNeg = " + ";
        else
            posNeg = " - ";
        ffDirectionCalc = ("FF Direction" + topLeg + totalDirection.get(0) + " / "
                + elevationValues.get(0) + " = " + (ffAvgDirection.get(0).intValue() + gridConvergence) + posNeg
                + gridConvergence + " = " + ffAvgDirection.get(0).intValue() + "\u00B0 (grid, rounded)" );
        ffVelocityCalc = ("FF Velocity" + topLeg + totalVelocity.get(0) + " / "
                + elevationValues.get(0) + " = " + ffAvgVelocity.get(0) + " knots (rounded)");
        ffDriftCalc = ("Freefall Drift" + topLeg + ffKFactor + " * " + ((elevationValues.get(0)
                + erroneousElevation.get(0)) * 2) + " * " + ffAvgVelocity.get(0) + " = "
                + ffDriftMeters.get(0).intValue() + " meters @ " + ffAvgDirection.get(0).intValue() + "\u00B0 grid");
        ffDriftString = (topLeg + ffDriftMeters.get(0).intValue() + " meters @ "
                + ffAvgDirection.get(0).intValue() + "\u00B0 grid");

        if (elevationValues.size() > 1) {
            for (int i = 1; i < elevationValues.size(); i++) {
                ffDirectionCalc += ("\nFF Direction Leg " + (i + 1) +  ": " + totalDirection.get(i) + " / "
                        + elevationValues.get(i) + " = " + (ffAvgDirection.get(i).intValue() + gridConvergence) + posNeg
                        + gridConvergence + " = " + ffAvgDirection.get(i).intValue() + "\u00B0 (grid, rounded)");
                ffVelocityCalc += ("\nFF Velocity Leg " + (i + 1) + ": " + totalVelocity.get(i) + " / "
                        + elevationValues.get(i) + " = " + ffAvgVelocity.get(i) + " knots (rounded)");
                ffDriftCalc += ("\nFreefall Drift Leg " + (i + 1) + ": " + ffKFactor + " * "
                        + ((elevationValues.get(i) + erroneousElevation.get(i)) * 2) + " * "
                        + ffAvgVelocity.get(i) + " = " + ffDriftMeters.get(i) + " meters @ "
                        + ffAvgDirection.get(i).intValue() + "\u00B0 grid");
                ffDriftString += ("\n\nLeg " + (i + 1) + ": " + ffDriftMeters.get(i).intValue()
                        + " meters @ " + ffAvgDirection.get(i).intValue() + "\u00B0 grid");
            }
        }
    }

    public ArrayList<Double> getFfDriftMeters() {
        return ffDriftMeters;
    }

    public ArrayList<Double> getFfAvgDirection() {
        return ffAvgDirection;
    }

    public String getAverageDirectionCalcs() {
        return ffDirectionCalc;
    }

    public String getAverageVelocityCalcs() {
        return ffVelocityCalc;
    }

    public String getFreefallDriftCalcs() {
        return ffDriftCalc;
    }

    @Override
    public String toString() {
        return ffDriftString;
    }
}
