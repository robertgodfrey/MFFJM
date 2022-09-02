package com.example.mffjm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PaperCalcs extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText gzdEditText;
    private EditText dipEastingEditText;
    private EditText dipNorthingEditText;
    private EditText dzElevation;
    private EditText exitAltAGL;
    private EditText pullAltAGL;
    private EditText aircraftHeadingMagnetic;
    private EditText numOfJumpers;
    private EditText safetyFactor;
    private EditText gmAngle;
    private EditText gridConvergence;
    protected static OperationalData opData;
    private boolean isHighPerformance;
    private int parachuteIndex;
    private static boolean firstTimeOpened = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_calcs);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));

        if (firstTimeOpened) {
            ParachuteType.loadChutes();
            firstTimeOpened = false;
        }
        String[] parachuteTypes = new String[ParachuteType.getNumberOfParachuteTypes()];
        Spinner parachuteTypeSpinner = (Spinner) findViewById(R.id.parachuteTypeSpinner);
        for (int i = 0; i < ParachuteType.getNumberOfParachuteTypes(); i++)
            parachuteTypes[i] = ParachuteType.getParachuteData(i).getName();
        ArrayAdapter<String> parachuteSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, parachuteTypes);
        parachuteTypeSpinner.setAdapter(parachuteSpinnerAdapter);
        parachuteTypeSpinner.setOnItemSelectedListener(this);

        Spinner aircraftTypeSpinner = findViewById(R.id.aircraftTypeSpinner);
        String[] aircraftTypes = new String[]{"High Performance", "Low Performance"};
        ArrayAdapter<String> aircraftTypeSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, aircraftTypes);
        aircraftTypeSpinner.setAdapter(aircraftTypeSpinnerAdapter);
        aircraftTypeSpinner.setOnItemSelectedListener(this);

        gzdEditText = (EditText) findViewById(R.id.gzdEditText);
        gzdEditText.addTextChangedListener(overFiveCharsRight);

        dipEastingEditText = (EditText) findViewById(R.id.dipEastingEditText);
        dipEastingEditText.addTextChangedListener(overFiveCharsRight);

        dipNorthingEditText = (EditText) findViewById(R.id.dipNorthingEditText) ;
        dipNorthingEditText.addTextChangedListener(overFiveChars);

        dzElevation = (EditText) findViewById(R.id.dzElevationEditText);
        dzElevation.addTextChangedListener(overFiveChars);

        exitAltAGL = (EditText) findViewById(R.id.exitAltitudeEditText);
        exitAltAGL.addTextChangedListener(overFiveChars);

        pullAltAGL = (EditText) findViewById(R.id.pullAltitudeEditText);
        pullAltAGL.addTextChangedListener(overFiveChars);

        aircraftHeadingMagnetic = (EditText) findViewById(R.id.aircraftHeadingMagneticEditText);
        aircraftHeadingMagnetic.addTextChangedListener(overThreeChars);

        numOfJumpers = (EditText) findViewById(R.id.numberOfJumpersEditText);
        numOfJumpers.addTextChangedListener(overTwoChars);

        safetyFactor = (EditText) findViewById(R.id.safetyFactorEditText);

        gmAngle = (EditText) findViewById(R.id.gmAngleEditText);

        gridConvergence = (EditText) findViewById(R.id.gridConvergenceEditText);

    }

    // Go to winds input activity
    public void windsInputButtonPush(View view) {

        if (gzdEditText.getText().toString().isEmpty())
            Toast.makeText(PaperCalcs.this, "Enter grid zone designator", Toast.LENGTH_LONG).show();

        else if (dipEastingEditText.getText().toString().length() != 5 || dipNorthingEditText.getText().toString().length() != 5)
            Toast.makeText(PaperCalcs.this, "Enter a 10 digit grid for the DIP", Toast.LENGTH_LONG).show();

        else if (dzElevation.getText().toString().isEmpty())
            Toast.makeText(PaperCalcs.this, "Enter an elevation for the DZ/VDZ", Toast.LENGTH_LONG).show();

        else if (exitAltAGL.getText().toString().isEmpty())
            Toast.makeText(PaperCalcs.this, "Enter exit altitude", Toast.LENGTH_LONG).show();

        else if (pullAltAGL.getText().toString().isEmpty())
            Toast.makeText(PaperCalcs.this, "Enter pull altitude", Toast.LENGTH_LONG).show();

        else if (aircraftHeadingMagnetic.getText().toString().isEmpty())
            Toast.makeText(PaperCalcs.this, "Enter aircraft heading", Toast.LENGTH_LONG).show();

        else if (numOfJumpers.getText().toString().isEmpty())
            Toast.makeText(PaperCalcs.this, "Enter number of jumpers", Toast.LENGTH_LONG).show();

        else if (safetyFactor.getText().toString().isEmpty())
            Toast.makeText(PaperCalcs.this, "Enter safety factor", Toast.LENGTH_LONG).show();

        else {
            ParachuteType parachute = ParachuteType.getParachuteData(parachuteIndex);
            Intent startWindInput = new Intent(getApplicationContext(), WindInput.class);
            startWindInput.setFlags(startWindInput.FLAG_ACTIVITY_SINGLE_TOP);
            opData = new OperationalData(gzdEditText.getText().toString(), Integer.parseInt(dipEastingEditText.getText().toString()),
                    Integer.parseInt(dipNorthingEditText.getText().toString()), Integer.parseInt(dzElevation.getText().toString()),
                    Integer.parseInt(exitAltAGL.getText().toString()), Integer.parseInt(pullAltAGL.getText().toString()),
                    parachute, isHighPerformance, Integer.parseInt(aircraftHeadingMagnetic.getText().toString()),
                    Integer.parseInt(numOfJumpers.getText().toString()), Integer.parseInt(safetyFactor.getText().toString()),
                    Integer.parseInt(gmAngle.getText().toString()), Integer.parseInt(gridConvergence.getText().toString()));
            startActivity(startWindInput);
            overridePendingTransition((R.anim.slide_in_right), (R.anim.slide_out_left));
        }
    }

    private TextWatcher overTwoChars = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            TextView text = (TextView) getCurrentFocus();

            if (text != null && text.length() > 1) {
                View next = text.focusSearch(View.FOCUS_DOWN);
                if (next != null)
                    next.requestFocus();

            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    };

    private TextWatcher overThreeChars = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            TextView text = (TextView) getCurrentFocus();

            if (text != null && text.length() > 2) {
                View next = text.focusSearch(View.FOCUS_DOWN);
                if (next != null)
                    next.requestFocus();

            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    };


    private TextWatcher overFiveChars = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            TextView text = (TextView) getCurrentFocus();

            if (text != null && text.length() > 4) {
                View next = text.focusSearch(View.FOCUS_DOWN);
                if (next != null)
                    next.requestFocus();

            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    };

    private TextWatcher overFiveCharsRight = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            TextView text = (TextView) getCurrentFocus();

            if (text != null && text.length() > 4) {
                View next = text.focusSearch(View.FOCUS_RIGHT);
                if (next != null)
                    next.requestFocus();

            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.parachuteTypeSpinner)
            this.parachuteIndex = position;
        else {
            if (position == 0)
                this.isHighPerformance = true;
            else
                this.isHighPerformance = false;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
