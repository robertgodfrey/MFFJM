package com.example.mffjm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class RA1Sustained01 extends AppCompatActivity {

    private int page = 1;
    float downX, upX;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_a1_sustained01);

        /* Figure this out later...
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView3);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:{
                        downX = event.getX();}
                    case MotionEvent.ACTION_UP:{
                        upX = event.getX();

                        float deltaX = downX - upX;

                        if(Math.abs(deltaX)>0){
                            if(deltaX>=0){
                                nextPage(v);
                                return true;
                            }else{
                                previousPage(v);
                                return true;
                            }
                        }
                    }
                }

                return false;
            }
        });

         */
    }
    public void nextPage(View view) {
        switch (page) {
            case 1:
                setContentView(R.layout.activity_r_a1_sustained02);
                page++;
                break;
            case 2:
                setContentView(R.layout.activity_r_a1_sustained03);
                page++;
                break;
            case 3:
                setContentView(R.layout.activity_r_a1_sustained04);
                page++;
                break;
            case 4:
                setContentView(R.layout.activity_r_a1_sustained05);
                page++;
                break;

        }
    }
    public void previousPage(View view) {
        switch (page) {
            case 2:
                setContentView(R.layout.activity_r_a1_sustained01);
                page--;
                break;
            case 3:
                setContentView(R.layout.activity_r_a1_sustained02);
                page--;
                break;
            case 4:
                setContentView(R.layout.activity_r_a1_sustained03);
                page--;
                break;
            case 5:
                setContentView(R.layout.activity_r_a1_sustained04);
                page--;
                break;
        }
    }
    public void returnToMenu(View view) {
        AlertDialog.Builder areYouSure = new AlertDialog.Builder(RA1Sustained01.this);
        areYouSure.setCancelable(true);
        areYouSure.setTitle("Confirm Exit");
        areYouSure.setMessage("Are you sure you want to exit to the main menu?");
        areYouSure.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent startMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startMainActivity);
            }
        });
        areYouSure.setNegativeButton("Return to SAT", null);
        areYouSure.show();
    }

}