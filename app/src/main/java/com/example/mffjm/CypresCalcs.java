package com.example.mffjm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CypresCalcs extends AppCompatActivity {

    private EditText dzElevationText;
    private EditText aircraftAltimeterSettingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cypres_calcs2);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));

        aircraftAltimeterSettingText = (EditText) findViewById(R.id.altimeterSettingEditText);
        dzElevationText = (EditText) findViewById(R.id.dzElevationEditText);
        aircraftAltimeterSettingText.addTextChangedListener(textWatcher);
        dzElevationText.addTextChangedListener(textWatcher);

        checkFieldsForEmptyValues();

        Button cypresCalcButton = (Button) findViewById(R.id.cypresCalculateButton);

        cypresCalcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView settingTextView = (TextView) findViewById(R.id.cypresSettingTextView);
                TextView resultTextView = (TextView) findViewById(R.id.cypresResultTextView);
                double aircraftAltimeterSetting = Double.parseDouble(aircraftAltimeterSettingText.getText().toString());
                int dzElevation = Integer.parseInt(dzElevationText.getText().toString());
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                AlertDialog.Builder inputOutOfBounds = new AlertDialog.Builder(CypresCalcs.this);

                if (!((aircraftAltimeterSetting >= 28) && (aircraftAltimeterSetting <= 32)
                        || (aircraftAltimeterSetting >= 950) && (aircraftAltimeterSetting <= 1085))) {
                    inputOutOfBounds.setCancelable(true);
                    inputOutOfBounds.setTitle("Input out of bounds");
                    inputOutOfBounds.setMessage("Altimeter setting must be 28 to 32 inHg or 950 to 1085 mbar.");
                    inputOutOfBounds.show();
                }

                else {
                    // if altitude setting is inHg, convert to mbar
                    if (aircraftAltimeterSetting > 50)
                        aircraftAltimeterSetting = aircraftAltimeterSetting / 33.8612;

                    int result = (int) Math.round((Math.pow(((44.331 - ((dzElevation + (-((Math.pow((aircraftAltimeterSetting / 29.92),
                            (1 / 5.25585)) * 44331) - 44331) / 0.30479)) * 0.001 / 3.2809)) / 44.331), 5.25585) * 101.325) * 10);

                    settingTextView.setText("CYPRES Setting (mbar):");
                    resultTextView.setText(result + "");

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            checkFieldsForEmptyValues();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    private void checkFieldsForEmptyValues() {
        Button cypressBtn = (Button) findViewById(R.id.cypresCalculateButton);
        String dzEle = dzElevationText.getText().toString();
        String airAl = aircraftAltimeterSettingText.getText().toString();

        if (dzEle.trim().isEmpty() || airAl.trim().isEmpty())
            cypressBtn.setEnabled(false);
        else
            cypressBtn.setEnabled(true);

    }


}
