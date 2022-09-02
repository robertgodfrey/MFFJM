package com.example.mffjm;

/** Takes distance and direction from calculations and determines the HARP and OP for a HALO/HAMO jump
 *
 * @author Rob Godfrey
 *
 */
import java.util.ArrayList;

public class TrigFunctionsHALO {
    private int opEasting;
    private int opNorthing;
    private int harpEasting;
    private int harpNorthing;
    private String harpString;
    private String opString;
    private double headingToDipGrid;
    private double distanceToDip;

    public TrigFunctionsHALO (int dipEasting, int dipNorthing, int jmpDispForThrMtrs, int aircraftHdgRvsGrid,
                              ArrayList<Double> ffDriftMeters, ArrayList<Double> ffAvgDirection, ArrayList<Double> canopyDriftKM,
                              ArrayList<Double> cdAvgDirection) {
        double angleRadians;	// the angle of the angle opposite the origin in radians
        this.harpEasting = dipEasting;
        this.harpNorthing = dipNorthing;

        // apply canopy drift calcs
        for (int i = 0; i < canopyDriftKM.size(); i++) {
            if (cdAvgDirection.get(i) > 0 && cdAvgDirection.get(i) <= 90) {
                angleRadians = Math.toRadians(90 - (cdAvgDirection.get(i)));
                harpEasting += canopyDriftKM.get(i) * 1000 * Math.cos(angleRadians);
                harpNorthing += canopyDriftKM.get(i) * 1000 * Math.sin(angleRadians);
            }
            else if (cdAvgDirection.get(i) > 90 && cdAvgDirection.get(i) <= 180) {
                angleRadians = Math.toRadians(90 - (cdAvgDirection.get(i) - 90));
                harpEasting += canopyDriftKM.get(i) * 1000 * Math.sin(angleRadians);
                harpNorthing -= canopyDriftKM.get(i) * 1000 * Math.cos(angleRadians);
            }
            else if (cdAvgDirection.get(i) > 180 && cdAvgDirection.get(i) <= 270) {
                angleRadians = Math.toRadians(90 - (cdAvgDirection.get(i) - 180));
                harpEasting -= canopyDriftKM.get(i) * 1000 * Math.cos(angleRadians);
                harpNorthing -= canopyDriftKM.get(i) * 1000 * Math.sin(angleRadians);
            }
            else if (cdAvgDirection.get(i) > 270 && cdAvgDirection.get(i) <= 360) {
                angleRadians = Math.toRadians(90 - (cdAvgDirection.get(i) - 270));
                harpEasting -= canopyDriftKM.get(i) * 1000 * Math.sin(angleRadians);
                harpNorthing += canopyDriftKM.get(i) * 1000 * Math.cos(angleRadians);
            }

        }
        this.opEasting = harpEasting;
        this.opNorthing = harpNorthing;
        opString = (String.format("%05d", opEasting) + "  " + String.format("%05d", opNorthing));

        // find distance and heading to dip
        int tempEasting = dipEasting - opEasting;
        int tempNorthing = dipNorthing - opNorthing;
        distanceToDip = Math.sqrt(Math.pow(tempEasting, 2) + Math.pow(tempNorthing, 2));
        if (tempEasting >= 0 && tempNorthing >= 0)
            headingToDipGrid = Math.toDegrees(Math.asin(tempNorthing/distanceToDip));
        else if (tempEasting >= 0)
            headingToDipGrid = Math.toDegrees(Math.asin(Math.abs(tempNorthing/distanceToDip))) + 90;
        else if (tempNorthing < 0)
            headingToDipGrid = Math.toDegrees(Math.asin(Math.abs(tempNorthing/distanceToDip))) + 180;
        else
            headingToDipGrid = Math.toDegrees(Math.asin(Math.abs(tempNorthing/distanceToDip))) + 270;

        // apply freefall drift calcs
        for (int i = 0; i < ffDriftMeters.size(); i++) {
            if (ffAvgDirection.get(i) > 0 && ffAvgDirection.get(i) <= 90) {
                angleRadians = Math.toRadians(90 - (ffAvgDirection.get(i)));
                harpEasting += ffDriftMeters.get(i) * Math.cos(angleRadians);
                harpNorthing += ffDriftMeters.get(i) * Math.sin(angleRadians);
            }
            else if (ffAvgDirection.get(i) > 90 && ffAvgDirection.get(i) <= 180) {
                angleRadians = Math.toRadians(90 - (ffAvgDirection.get(i) - 90));
                harpEasting += ffDriftMeters.get(i) * Math.sin(angleRadians);
                harpNorthing -= ffDriftMeters.get(i) * Math.cos(angleRadians);
            }
            else if (ffAvgDirection.get(i) > 180 && ffAvgDirection.get(i) <= 270) {
                angleRadians = Math.toRadians(90 - (ffAvgDirection.get(i) - 180));
                harpEasting -= ffDriftMeters.get(i) * Math.cos(angleRadians);
                harpNorthing -= ffDriftMeters.get(i) * Math.sin(angleRadians);
            }
            else if (ffAvgDirection.get(i) > 270 && ffAvgDirection.get(i) <= 360) {
                angleRadians = Math.toRadians(90 - (ffAvgDirection.get(i) - 270));
                harpEasting -= ffDriftMeters.get(i) * Math.sin(angleRadians);
                harpNorthing += ffDriftMeters.get(i) * Math.cos(angleRadians);
            }
        }

        if (aircraftHdgRvsGrid > 0 && aircraftHdgRvsGrid <= 90) {
            angleRadians = Math.toRadians(90 - aircraftHdgRvsGrid);
            harpEasting += jmpDispForThrMtrs * Math.cos(angleRadians);
            harpNorthing += jmpDispForThrMtrs * Math.sin(angleRadians);
        }
        else if (aircraftHdgRvsGrid > 90 && aircraftHdgRvsGrid <= 180) {
            angleRadians = Math.toRadians(90 - aircraftHdgRvsGrid - 90);
            harpEasting += jmpDispForThrMtrs * Math.sin(angleRadians);
            harpNorthing -= jmpDispForThrMtrs * Math.cos(angleRadians);
        }
        else if (aircraftHdgRvsGrid > 180 && aircraftHdgRvsGrid <= 270) {
            angleRadians = Math.toRadians(90 - aircraftHdgRvsGrid - 180);
            harpEasting -= jmpDispForThrMtrs * Math.cos(angleRadians);
            harpNorthing -= jmpDispForThrMtrs * Math.sin(angleRadians);
        }
        else if (aircraftHdgRvsGrid > 270 && aircraftHdgRvsGrid <= 360) {
            angleRadians = Math.toRadians(90 - aircraftHdgRvsGrid - 270);
            harpEasting -= jmpDispForThrMtrs * Math.sin(angleRadians);
            harpNorthing += jmpDispForThrMtrs * Math.cos(angleRadians);
        }
        harpString  = (String.format("%05d", harpEasting) + "  " + String.format("%05d", harpNorthing));

    }
    public int getOpNorthing() {
        return opNorthing;
    }
    public int getOpEasting() {
        return opEasting;
    }
    public double getHeadingToDipGrid() {
        return headingToDipGrid;
    }
    public double getDistanceToDip() {
        return distanceToDip;
    }
    public int getHarpNorthing() {
        return harpNorthing;
    }
    public int getHarpEasting() {
        return harpEasting;
    }
    public String getHarpString() {
        return harpString;
    }
    public String getOpString() {
        return opString;
    }
}
