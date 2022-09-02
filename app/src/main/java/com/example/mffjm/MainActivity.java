package com.example.mffjm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));

        final ImageButton paperCalcsBtn = (ImageButton) findViewById(R.id.paperCalcsBtn);
        ImageButton cypresCalcsBtn = (ImageButton) findViewById(R.id.cypresCalcsBtn);
        ImageButton altimeterCalcsBtn = (ImageButton) findViewById(R.id.altimeterBtn);
        ImageButton satBtn = (ImageButton) findViewById(R.id.satBtn);
        ImageButton jmpiBtn = (ImageButton) findViewById(R.id.jmpiBtn);

        // go to activity (same for all image buttons on main menu)
        paperCalcsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startPaperCalcs = new Intent(getApplicationContext(), PaperCalcs.class);
                startActivity(startPaperCalcs);
                ImageView view = (ImageView) v;
                view.getDrawable().setColorFilter(0x000000,PorterDuff.Mode.SRC_ATOP);
                view.invalidate();
            }
        });

        // set overlay if button is touched (same for all image buttons on main menu)
        paperCalcsBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ImageView view = (ImageView) v;
                    view.getDrawable().setColorFilter(0x60000000,PorterDuff.Mode.SRC_ATOP);
                    view.invalidate();
                    return false;
                }
                else if  (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    ImageView view = (ImageView) v;
                    view.getDrawable().setColorFilter(0x000000,PorterDuff.Mode.SRC_ATOP);
                    view.invalidate();
                    return false;
                }
                return false;
            }
        });

        cypresCalcsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startCypresCalcs = new Intent(getApplicationContext(), CypresCalcs.class);
                startActivity(startCypresCalcs);
                ImageView view = (ImageView) v;
                view.getDrawable().setColorFilter(0x000000,PorterDuff.Mode.SRC_ATOP);
                view.invalidate();
            }
        });

        cypresCalcsBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ImageView view = (ImageView) v;
                    view.getDrawable().setColorFilter(0x60000000,PorterDuff.Mode.SRC_ATOP);
                    view.invalidate();
                    return false;
                }
                else if  (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    ImageView view = (ImageView) v;
                    view.getDrawable().setColorFilter(0x000000,PorterDuff.Mode.SRC_ATOP);
                    view.invalidate();
                    return false;
                }
                return false;
            }
        });

        altimeterCalcsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startAltimterCalcs = new Intent(getApplicationContext(), AltimeterCalcs.class);
                startActivity(startAltimterCalcs);
                ImageView view = (ImageView) v;
                view.getDrawable().setColorFilter(0x000000,PorterDuff.Mode.SRC_ATOP);
                view.invalidate();
            }
        });

        altimeterCalcsBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ImageView view = (ImageView) v;
                    view.getDrawable().setColorFilter(0x60000000,PorterDuff.Mode.SRC_ATOP);
                    view.invalidate();
                    return false;
                }
                else if  (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    ImageView view = (ImageView) v;
                    view.getDrawable().setColorFilter(0x000000,PorterDuff.Mode.SRC_ATOP);
                    view.invalidate();
                    return false;
                }
                return false;
            }
        });

        satBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder satBtnMessage = new AlertDialog.Builder(MainActivity.this);
                satBtnMessage.setCancelable(true);
                satBtnMessage.setMessage("This app currently only supports the RA-1");
                satBtnMessage.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent startSat = new Intent(getApplicationContext(), RA1Sustained01.class);
                        startActivity(startSat);
                    }
                });
                satBtnMessage.setNegativeButton("Return", null);
                satBtnMessage.show();
                ImageView view = (ImageView) v;
                view.getDrawable().setColorFilter(0x000000,PorterDuff.Mode.SRC_ATOP);
                view.invalidate();

                           }
        });

        satBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ImageView view = (ImageView) v;
                    view.getDrawable().setColorFilter(0x60000000,PorterDuff.Mode.SRC_ATOP);
                    view.invalidate();
                    return false;
                }
                else if  (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    ImageView view = (ImageView) v;
                    view.getDrawable().setColorFilter(0x000000,PorterDuff.Mode.SRC_ATOP);
                    view.invalidate();
                    return false;
                }
                return false;
            }
        });

        jmpiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Intent startJmpi = new Intent(getApplicationContext(), Jmpi.class);
                startActivity(startSat);
                 */
                AlertDialog.Builder notYetComplete = new AlertDialog.Builder(MainActivity.this);
                notYetComplete.setCancelable(true);
                notYetComplete.setTitle("Coming soon!");
                notYetComplete.setMessage("Check back later for updates.");
                notYetComplete.show();
                ImageView view = (ImageView) v;
                view.getDrawable().setColorFilter(0x000000,PorterDuff.Mode.SRC_ATOP);
                view.invalidate();
            }
        });

        jmpiBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ImageView view = (ImageView) v;
                    view.getDrawable().setColorFilter(0x60000000,PorterDuff.Mode.SRC_ATOP);
                    view.invalidate();
                    return false;
                }
                else if  (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    ImageView view = (ImageView) v;
                    view.getDrawable().setColorFilter(0x000000,PorterDuff.Mode.SRC_ATOP);
                    view.invalidate();
                    return false;
                }
                return false;
            }
        });

    }

}
