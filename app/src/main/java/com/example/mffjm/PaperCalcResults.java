package com.example.mffjm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class PaperCalcResults extends AppCompatActivity {

    private ArrayList<WindObject> windsArray = WindInput.windsArray;
    private OperationalData opData = PaperCalcs.opData;
    private int highestAltLowerWindsForInput = opData.getPullAltAGL() / 1000;
    private int highestAltLowerWindsIndex;
    private int pullAltAGL = opData.getPullAltAGL();
    private ParachuteType parachute = opData.getParachuteType();
    private String jumperDispersionAndForwardThrowString;
    private CanopyDrift cd;
    private FreefallDrift ffd;
    private TrigFunctionsHALO halo;
    private TrigFunctionsHAHO haho;
    private boolean isHALO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_calc_results);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));

        dogLegsErroneous();
        calculate();

        TextView opSlashPrpResult = (TextView) findViewById(R.id.opSlashPrpResultTextView);
        TextView opSlashPrpTitle = (TextView) findViewById(R.id.opSlashPrpTitleTextView);
        TextView dipResultTextView = (TextView) findViewById(R.id.harpResultTextView);
        TextView distanceHeadingResultTextView = (TextView) findViewById(R.id.distanceHeadingResultTextView);
        String distanceHeadingToDip;
        double distanceToDip;

        /*
        TextView jumperDisAndForThrTextView = (TextView) findViewById(R.id.jumperDisAndForResultTextView);
        jumperDisAndForThrTextView.setText(jumperDispersionAndForwardThrowString);

        TextView canopyDriftResultTextView = (TextView) findViewById(R.id.canopyDriftResultTextView);
        canopyDriftResultTextView.setText(cd.toString());

        TextView freefallDriftResultTextView = (TextView) findViewById(R.id.freefallDriftResultTextView);
         */


        NumberFormat oneDecimal = new DecimalFormat("#0.0");
        if (isHALO) {
           // freefallDriftResultTextView.setText(ffd.toString());
            opSlashPrpTitle.setText("OP:");
            String op = opData.getGzd() + "   " + halo.getOpString();
            String harp = opData.getGzd() + "   " + halo.getHarpString();
            opSlashPrpResult.setText(op);
            dipResultTextView.setText(harp);
            distanceToDip = Math.round(halo.getDistanceToDip() / 100.0) / 10.0;
            distanceHeadingToDip = oneDecimal.format(distanceToDip) + " kilometers at " + (int)Math.round(halo.getHeadingToDipGrid()) + "\u00B0 grid";
        } else {
           // freefallDriftResultTextView.setText("N/A");
            opSlashPrpTitle.setText("PRP:");
            String prp = opData.getGzd() + "   " + haho.getOpString();
            String harp = opData.getGzd() + "   " + haho.getHarpString();
            opSlashPrpResult.setText(prp);
            dipResultTextView.setText(harp);
            distanceToDip = Math.round(haho.getDistanceToDip() / 100.0) / 10.0;
            distanceHeadingToDip = oneDecimal.format(distanceToDip) + " kilometers at " + (int)Math.round(haho.getHeadingToDipGrid()) + "\u00B0 grid";
        }
        distanceHeadingResultTextView.setText(distanceHeadingToDip);

        Button backToWinds = (Button) findViewById(R.id.backToWindsButton);
        backToWinds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button viewCalcs = (Button) findViewById(R.id.viewCalculationsBtn);
        viewCalcs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String freefallDrift = "Freefall drift: N/A";
                if (isHALO)
                    freefallDrift = ffd.getFreefallDriftCalcs();
                AlertDialog.Builder calculationsView = new AlertDialog.Builder(PaperCalcResults.this);
                calculationsView.setCancelable(true);
                calculationsView.setTitle("Calculations:");
                calculationsView.setMessage(freefallDrift
                        + "\n\nCanopy Drift" + cd.getcdDriftCalc()
                        + "\nForward Throw: " + jumperDispersionAndForwardThrowString);
                calculationsView.show();
            }
        });

        Button backToMain = (Button) findViewById(R.id.exitToMainButton);
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder areYouSure = new AlertDialog.Builder(PaperCalcResults.this);
                areYouSure.setCancelable(true);
                areYouSure.setTitle("Confirm Exit");
                areYouSure.setMessage("Are you sure you want to exit to the main menu? Your data will not be saved.");
                areYouSure.setPositiveButton("Exit to Main", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent startMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(startMainActivity);
                    }
                });
                areYouSure.setNegativeButton("Return to Results", null);
                areYouSure.show();

            }
        });

    }

    private void dogLegsErroneous() {
        // determine the index number for the highest altitude of the lower section of winds
        if (highestAltLowerWindsForInput > 10) {
            this.highestAltLowerWindsIndex = ((highestAltLowerWindsForInput - 10) / 2) + 9;
            windsArray.get(highestAltLowerWindsIndex).setErroneous(false);
        }
        else {
            this.highestAltLowerWindsIndex = highestAltLowerWindsForInput - 1;
            windsArray.get(highestAltLowerWindsIndex).setErroneous(false);
        }
        windsArray.get(windsArray.size() - 1).setErroneous(false);
        int dogLegNum = 1;
        // Find any erroneous winds or dog legs, going from the highest altitude to the lowest
        // upper section of winds:
        for (int i = windsArray.size() - 1; i > highestAltLowerWindsIndex + 1; i--) {
            int thisDir = windsArray.get(i).getDirection();
            int nextDir = windsArray.get(i - 1).getDirection();
            if (i == windsArray.size() - 1 && (thisDir - nextDir >= 90 || thisDir - nextDir <= -90)
                    && (thisDir - windsArray.get(i - 2).getDirection() >= 90 ||
                    (thisDir - windsArray.get(i - 2).getDirection() <= -90))) {
                if ((thisDir < 90 || thisDir > 270) && (nextDir < 90 || nextDir > 270)) {
                    if (thisDir < 90 && nextDir > 270) {
                        thisDir += 360;
                        if (thisDir - nextDir <= 90) {
                            windsArray.get(i).setErroneous(false);
                            windsArray.get(i).setDogLegNum(dogLegNum);
                            windsArray.get(i - 1).setIncompatible(true);
                        }
                    }
                    else if (thisDir > 270 && nextDir < 90) {
                        nextDir += 360;
                        if (thisDir - nextDir >= -90) {
                            windsArray.get(i).setErroneous(false);
                            windsArray.get(i).setDogLegNum(dogLegNum);
                            windsArray.get(i - 1).setIncompatible(true);
                        }
                    }
                }
                else {
                    windsArray.get(i).setErroneous(true);
                    windsArray.get(i).setDogLegNum(dogLegNum);
                }
            }
            else if (!windsArray.get(i).isErroneous() && (thisDir - nextDir >= 90 || thisDir - nextDir <= -90)) {
                if ((thisDir < 90 || thisDir > 270) && (nextDir < 90 || nextDir > 270)) {
                    if (thisDir < 90 && nextDir > 270) {
                        thisDir += 360;
                        if (thisDir - nextDir <= 90) {
                            windsArray.get(i).setErroneous(false);
                            windsArray.get(i).setDogLegNum(dogLegNum);
                            windsArray.get(i - 1).setIncompatible(true);
                        }
                    }
                    else if (thisDir > 270 && nextDir < 90) {
                        nextDir += 360;
                        if (thisDir - nextDir >= -90) {
                            windsArray.get(i).setErroneous(false);
                            windsArray.get(i).setDogLegNum(dogLegNum);
                            windsArray.get(i - 1).setIncompatible(true);
                        }
                    }
                }
                else {
                    windsArray.get(i - 1).setErroneous(true);
                    windsArray.get(i - 1).setDogLegNum(dogLegNum);
                }
            }
            else if ((windsArray.get(i).isErroneous()) && ((thisDir - nextDir < 90 && thisDir - nextDir >= 0)
                    || ((thisDir - nextDir > -90) && thisDir - nextDir <= 0))) {
                windsArray.get(i - 1).setErroneous(false);
                windsArray.get(i - 1).setDogLegNum(dogLegNum + 1);
                windsArray.get(i).setErroneous(false);
                windsArray.get(i).setDogLegNum(dogLegNum + 1);
                dogLegNum++;
            }
            else if (windsArray.get(i).isErroneous())
                windsArray.get(i).setDogLegNum(dogLegNum);
            else {
                windsArray.get(i).setErroneous(false);
                windsArray.get(i).setDogLegNum(dogLegNum);
                windsArray.get(i - 1).setErroneous(false);
                windsArray.get(i - 1).setDogLegNum(dogLegNum);
            }
            if (i < windsArray.size() - 1) {
                if (windsArray.get(i + 1).isIncompatible() && windsArray.get(i).getDogLegNum()
                        == windsArray.get(i + 1).getDogLegNum()) {
                    windsArray.get(i).setIncompatible(true);
                }
            }
        }
        // lower section of winds:
        for (int i = highestAltLowerWindsIndex; i > 0; i--) {
            int thisDir = windsArray.get(i).getDirection();
            int nextDir = windsArray.get(i - 1).getDirection();
            if (i == highestAltLowerWindsIndex && (thisDir - nextDir >= 90 || thisDir - nextDir <= -90)
                    && (thisDir - windsArray.get(i - 2).getDirection() >= 90 ||
                    (thisDir - windsArray.get(i - 2).getDirection() <= -90))) {
                if ((thisDir < 90 || thisDir > 270) && (nextDir < 90 || nextDir > 270)) {
                    if (thisDir < 90 && nextDir > 270) {
                        thisDir += 360;
                        if (thisDir - nextDir <= 90) {
                            windsArray.get(i).setErroneous(false);
                            windsArray.get(i).setDogLegNum(dogLegNum);
                            windsArray.get(i - 1).setIncompatible(true);
                        }
                    }
                    else if (thisDir > 270 && nextDir < 90) {
                        nextDir += 360;
                        if (thisDir - nextDir >= -90) {
                            windsArray.get(i).setErroneous(false);
                            windsArray.get(i).setDogLegNum(dogLegNum);
                            windsArray.get(i - 1).setIncompatible(true);
                        }
                    }
                }
                else {
                    windsArray.get(i).setErroneous(true);
                    windsArray.get(i).setDogLegNum(dogLegNum);
                }
            }
            else if (!windsArray.get(i).isErroneous() && (thisDir - nextDir >= 90 || thisDir - nextDir <= -90)) {
                if ((thisDir < 90 || thisDir > 270) && (nextDir < 90 || nextDir > 270)) {
                    if (thisDir < 90 && nextDir > 270) {
                        thisDir += 360;
                        if (thisDir - nextDir <= 90) {
                            windsArray.get(i).setErroneous(false);
                            windsArray.get(i).setDogLegNum(dogLegNum);
                            windsArray.get(i - 1).setIncompatible(true);
                        }
                    }
                    else if (thisDir > 270 && nextDir < 90) {
                        nextDir += 360;
                        if (thisDir - nextDir >= -90) {
                            windsArray.get(i).setErroneous(false);
                            windsArray.get(i).setDogLegNum(dogLegNum);
                            windsArray.get(i - 1).setIncompatible(true);
                        }
                    }
                }
                else {
                    windsArray.get(i - 1).setErroneous(true);
                    windsArray.get(i - 1).setDogLegNum(dogLegNum);
                }
            }
            else if ((windsArray.get(i).isErroneous()) && ((thisDir - nextDir < 90 && thisDir - nextDir >= 0)
                    || ((thisDir - nextDir > -90) && thisDir - nextDir <= 0))) {
                windsArray.get(i - 1).setErroneous(false);
                windsArray.get(i - 1).setDogLegNum(dogLegNum + 1);
                windsArray.get(i).setErroneous(false);
                windsArray.get(i).setDogLegNum(dogLegNum + 1);
                dogLegNum++;
            }
            else if (windsArray.get(i).isErroneous())
                windsArray.get(i).setErroneous(true);
            else {
                windsArray.get(i).setErroneous(false);
                windsArray.get(i).setDogLegNum(dogLegNum);
                windsArray.get(i - 1).setErroneous(false);
                windsArray.get(i - 1).setDogLegNum(dogLegNum);
            }
            if (i < windsArray.size() - 1) {
                if (windsArray.get(i + 1).isIncompatible() && windsArray.get(i).getDogLegNum()
                        == windsArray.get(i + 1).getDogLegNum()) {
                    windsArray.get(i).setIncompatible(true);
                }
            }
            if (i == 1 && windsArray.get(1).isIncompatible() && windsArray.get(1).getDogLegNum()
                    == windsArray.get(0).getDogLegNum() && !windsArray.get(0).isErroneous())
                windsArray.get(0).setIncompatible(true);
        }
    }

    private void calculate() {
        int forwardThrow;
        int jumperDispersionAndForwardThrow;
        int aircraftHeadingReverseGrid;

        // Calculate jumper dispersion and forward throw
        if (opData.isHighPerformance())
            forwardThrow = 300;
        else
            forwardThrow = 150;
        jumperDispersionAndForwardThrow = (opData.getNumOfJumpers() / 2 * 50) + forwardThrow;
        if (opData.getAircraftHeadingMagnetic() < 180)
            aircraftHeadingReverseGrid = opData.getAircraftHeadingMagnetic() + 180 - opData.getGmAngle();
        else
            aircraftHeadingReverseGrid = opData.getAircraftHeadingMagnetic() - 180 - opData.getGmAngle();

        this.jumperDispersionAndForwardThrowString = jumperDispersionAndForwardThrow + " meters @ " + aircraftHeadingReverseGrid + "\u00B0 grid";

        // HALO or HAMO
        if (opData.getExitAltAGL() - pullAltAGL > 1000) {
            this.isHALO = true;
            this.ffd = new FreefallDrift(pullAltAGL, windsArray, opData.getGridConvergence());
            System.out.println(ffd.toString());

            this.cd = new com.example.mffjm.CanopyDrift(highestAltLowerWindsIndex, windsArray,
                    parachute.getForwardSpeed(), parachute.getkFactor(), opData.getSafetyFactor(), opData.getGridConvergence());

            this.halo = new TrigFunctionsHALO(opData.getDipEasting(), opData.getDipNorthing(),
                    jumperDispersionAndForwardThrow, aircraftHeadingReverseGrid, ffd.getFfDriftMeters(), ffd.getFfAvgDirection(),
                    cd.getCanopyDriftKM(), cd.getcdAvgDirection());
        }

        // HAHO (disregards freefall drift)
        else {
            this.isHALO = false;
            this.cd = new com.example.mffjm.CanopyDrift(highestAltLowerWindsIndex, windsArray,
                    parachute.getForwardSpeed(), parachute.getkFactor(), opData.getSafetyFactor(), opData.getGridConvergence());

            this.haho = new TrigFunctionsHAHO(opData.getDipEasting(), opData.getDipNorthing(),
                    jumperDispersionAndForwardThrow, aircraftHeadingReverseGrid, cd.getCanopyDriftKM(),
                    cd.getcdAvgDirection());
        }
    }
}
