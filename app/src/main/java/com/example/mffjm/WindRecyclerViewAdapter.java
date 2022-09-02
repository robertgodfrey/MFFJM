package com.example.mffjm;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class WindRecyclerViewAdapter extends RecyclerView.Adapter<WindRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "WindRecyclerViewAdapter";
    private LayoutInflater inflater;

    public WindRecyclerViewAdapter(ArrayList<WindObject> windsArray, Context context) {
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.winds_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        String altitudeString = WindInput.windsArray.get(position).getAltitude() + ",000";
        holder.altitude.setText(altitudeString);
    }

    @Override
    public int getItemCount() {
        return WindInput.windsArray.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView altitude;
        EditText direction;
        EditText velocity;
        ConstraintLayout parentLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            altitude = itemView.findViewById(R.id.windsAltitudeTextView);
            direction = itemView.findViewById(R.id.windsDirectionEditText);
            velocity = itemView.findViewById(R.id.windsVelocityEditText);
            parentLayout = itemView.findViewById(R.id.winds_list);

            // if next button on soft keyboard is pushed, focus will go to velocity (default went down to next direction)
            direction.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        direction.focusSearch(View.FOCUS_RIGHT).requestFocus();
                        return true;
                    }
                    return false;
                }
            });

            // after three characters are entered, focus will go to velocity. if direction is an incorrect value, color changes to red
            direction.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!direction.getText().toString().isEmpty()) {
                        WindInput.windsArray.get(getAdapterPosition()).setDirection(Integer.parseInt(direction.getText().toString()));
                        if (Integer.parseInt(direction.getText().toString()) > 360 || Integer.parseInt(direction.getText().toString()) < 0) {
                            direction.setBackgroundColor(Color.RED);
                        }
                        if (Integer.parseInt(direction.getText().toString()) <= 360 && Integer.parseInt(direction.getText().toString()) >= 0) {
                            direction.setBackgroundColor(Color.TRANSPARENT);
                        }
                    }
                    if (direction.getText().toString().length() > 2) {
                        View next = direction.focusSearch(View.FOCUS_RIGHT);
                        if (next != null)
                            next.requestFocus();
                    }

                }


                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            // next button focuses on next direction
            velocity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        direction.focusSearch(View.FOCUS_DOWN).requestFocus();
                        return true;
                    }
                    return false;
                }
            });

            // focus changes to next direction after two characters
            velocity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!velocity.getText().toString().isEmpty())
                        WindInput.windsArray.get(getAdapterPosition()).setVelocity(Integer.parseInt(velocity.getText().toString()));

                    if (velocity.getText().toString().length() > 1) {
                        View down = velocity.focusSearch(View.FOCUS_DOWN);
                        if (down != null) {
                            direction.focusSearch(View.FOCUS_DOWN).requestFocus();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

    }


}
