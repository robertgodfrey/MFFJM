package com.example.mffjm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class WindInput extends AppCompatActivity {

    private static final String TAG = "WindInput";
    protected static ArrayList<WindObject> windsArray;
    private OperationalData opData = PaperCalcs.opData;
    private ToggleButton sortAltsButton;
    private static String result = "Failure. Please try again. ";
    private static String winds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wind_input);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));

        initWindsArray();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        WindRecyclerViewAdapter adapter = new WindRecyclerViewAdapter(windsArray, this);

        RecyclerView windsRecyclerView = findViewById(R.id.windsRecyclerView);
        windsRecyclerView.setLayoutManager(layoutManager);
        windsRecyclerView.setAdapter(adapter);


        sortAltsButton = (ToggleButton) findViewById(R.id.sortAltitudeButton);
        sortAltsButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layoutManager.setReverseLayout(false);
                    layoutManager.setStackFromEnd(false);
                } else {
                    layoutManager.setReverseLayout(true);
                    layoutManager.setStackFromEnd(true);
                }
            }
        });
        final View parent = (View) sortAltsButton.getParent();
        parent.post(new Runnable() {
            @Override
            public void run() {
                final Rect rect = new Rect();
                sortAltsButton.getHitRect(rect);
                rect.top -= 100;    // increase top hit area
                rect.left -= 100;   // increase left hit area
                rect.bottom += 100; // increase bottom hit area
                rect.right += 100;  // increase right hit area
                parent.setTouchDelegate( new TouchDelegate( rect , sortAltsButton));
            }
        });


    }

    private void initWindsArray() {
        int pullAltAGL = opData.getPullAltAGL();
        int exitAltAGL = opData.getExitAltAGL();
        int lowestAltUpperWinds;
        windsArray = new ArrayList<WindObject>();

        if (pullAltAGL <= 10000) {
            // Initialize values for winds from 1,000 ft to pull altitude, every 1,000 ft
            for (int i = 1; i <= (pullAltAGL / 1000); i++)
                windsArray.add(new WindObject(i, 0, 0));
            // Initialize values for winds from pull altitude to exit altitude, every 2000 ft (even numbers only)
            lowestAltUpperWinds = pullAltAGL / 1000 + 2;             // = The lowest altitude of the upper section of winds.
            if (lowestAltUpperWinds % 2 != 0)                        // - Ensures upper section of winds includes only
                lowestAltUpperWinds = pullAltAGL / 1000 + 1;         //   even numbers.
            for (int i = lowestAltUpperWinds; i <= ((exitAltAGL) / 1000); i += 2)
                windsArray.add(new WindObject(i, 0, 0));
        } else {
            // Initialize values for winds from 1,000 to 10,000 ft, every 1,000 ft
            for (int i = 1; i <= 10; i++)
                windsArray.add(new WindObject(i, 0, 0));
            // Initialize values for winds from 10,000 ft to exit altitude, every 2000 ft (even numbers only)
            lowestAltUpperWinds = 12;
            for (int i = lowestAltUpperWinds, a = lowestAltUpperWinds - 1; i <= ((exitAltAGL) / 1000); i += 2, a++)
                windsArray.add(new WindObject(i, 0, 0));
        }
    }

    public void backToOpData(View view) {
        onBackPressed();
    }

    public void calculateBtn(View view) {
        final Intent calculatePaper = new Intent(getApplicationContext(), PaperCalcResults.class);
        // my stupid way to see if any of the wind directions are invalid. i'm sure there is a better way to do this
        int sum = 0;
        for (int i = 0; i < windsArray.size(); i++) {
            if (windsArray.get(i).getDirection() > 360 || windsArray.get(i).getDirection() < 0)
                sum += 2;
            else
                sum++;
        }
        int sum2 = 0;
        for (int i = 0; i < windsArray.size(); i++) {
            if (windsArray.get(i).getDirection() == 0 && windsArray.get(i).getVelocity() == 0)
                sum2 += 2;
            else
                sum2++;
        }
        if (sum > windsArray.size()) {
            AlertDialog.Builder invalidWinds = new AlertDialog.Builder(WindInput.this);
            invalidWinds.setCancelable(true);
            invalidWinds.setTitle("Invalid Wind Direction");
            invalidWinds.setMessage("One or more of your wind directions is out of bounds " +
                    "(0 - 360). Please double check your input before continuing");
            invalidWinds.show();
        } else if (sum2 > windsArray.size()) {
                AlertDialog.Builder zeroValues = new AlertDialog.Builder(WindInput.this);
                zeroValues.setCancelable(true);
                zeroValues.setTitle("Zero Values Alert");
                zeroValues.setMessage("One or more wind inputs has zero values for both " +
                        "direction and velocity. If this is correct, click continue. If not, click return.");
                zeroValues.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(calculatePaper);
                    }
                });
                zeroValues.setNegativeButton("Return", null);
                zeroValues.show();
        } else
            startActivity(calculatePaper);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void windsAloft(View view) throws Exception {
        AlertDialog.Builder markyMark = new AlertDialog.Builder(WindInput.this);
        markyMark.setCancelable(true);
        markyMark.setTitle("Import winds?");
        markyMark.setMessage("Winds will be imported from www.markschulze.net (not an authorized weather source IAW" +
                " AFI 15-157 and AR 115-10)");
        markyMark.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new ImportWinds(PaperCalcs.opData.getGzd() + PaperCalcs.opData.getDipEasting() + PaperCalcs.opData.getDipNorthing())).start();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                AlertDialog.Builder result = new AlertDialog.Builder(WindInput.this);
                result.setCancelable(true);
                result.setMessage(WindInput.result + WindInput.winds);
                result.show();
            }
        });
        markyMark.setNegativeButton("Return", null);
        markyMark.show();


    }

    class ImportWinds implements Runnable {

        private String result;
        private String test;
        private String fullGrid;

        ImportWinds(String fullGrid) {
            this.fullGrid = fullGrid;
        }

        public void run() {
            String windsaloftURL = "https://www.markschulze.net/winds/";
            try {
                Connection.Response response = Jsoup.connect(windsaloftURL)
                                .timeout(10 * 1000)
                                .data("mgrs", fullGrid)
                                .method(Connection.Method.POST)
                                .followRedirects(true)
                                .execute();

                //parse the document from response
                Document doc = response.parse();
                Element windsAloft = doc.getElementById("windsaloft");
                test = windsAloft.text();
                result = "Winds successfully imported.";
            } catch (IOException e) {
                result = "Unable to connect. Check your internet connection and try again.";
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WindInput.result = result;
                    WindInput.winds = test;
                }
            });

        }

    }

}

