package com.example.mffjm;

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

public class AltimeterCalcs extends AppCompatActivity {

    private EditText dzElevationText;
    private EditText dafElevationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altimeter_calcs);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));

        dafElevationText = (EditText) findViewById(R.id.dafElevationEditText);
        dzElevationText = (EditText) findViewById(R.id.dzElevationEditTextAlt);
        dafElevationText.addTextChangedListener(textWatcher);
        dzElevationText.addTextChangedListener(textWatcher);

        checkFieldsForEmptyValues();

        Button altimeterCalcButton = (Button) findViewById(R.id.altimeterCalculateButton);
        altimeterCalcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView settingTextView = (TextView) findViewById(R.id.altimeterSettingTextViewAlt);
                TextView resultTextView = (TextView) findViewById(R.id.altimeterResultTextView);
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                int dafElevation = Integer.parseInt(dafElevationText.getText().toString());
                int dzElevation = Integer.parseInt(dzElevationText.getText().toString());
                int result = dafElevation - dzElevation;

                settingTextView.setText("Altimeter setting:");
                resultTextView.setText(result + "");

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
        Button altButton = (Button) findViewById(R.id.altimeterCalculateButton);
        String dzEle = dzElevationText.getText().toString();
        String dafEle = dafElevationText.getText().toString();

        if (dzEle.trim().isEmpty() || dafEle.trim().isEmpty())
            altButton.setEnabled(false);
        else
            altButton.setEnabled(true);

    }
}
