package com.cs449.dbklein.umpirebuddy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class UmpireActivity extends AppCompatActivity {

    private int balls;
    private int strikes;
    private final String filename = "outs";

    private TextView ballsText;
    private TextView strikesText;
    private int outs;
    private TextView outsText;

    private AlertDialog.Builder walkAlert;
    private AlertDialog.Builder strikeOutAlert;

    private ActionMode mActionMode;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.activity_umpire_count, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.balls_button_menu:
                    addBall();
                    return true;
                case R.id.strikes_button_menu:
                    addStrike();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_umpire, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.reset_button:
                updateCountText(0, 0);
                return true;
            case R.id.about_button:
                gotoAboutActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateCountText(int balls, int strikes) {
        ballsText.setText(getString(R.string.balls_label_text, balls));
        strikesText.setText(getString(R.string.strikes_label_text, strikes));

        this.balls = balls;
        this.strikes = strikes;
    }

    private void gotoAboutActivity() {
        startActivity(new Intent(UmpireActivity.this, AboutActivity.class));
    }

    private void updateOuts(int outs) {
        outsText.setText(getString(R.string.outs_label_text, outs));

        this.outs = outs;

        try {
            FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            System.out.println("Writing '" + outs + "'");
            outputStream.write(Integer.toString(outs).getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umpire);

        balls = 0;
        strikes = 0;
        outs = 0;

        ballsText = findViewById(R.id.balls_text);
        strikesText = findViewById(R.id.strikes_text);
        outsText = findViewById(R.id.outs_text);

        Button ballsButton = findViewById(R.id.balls_button);
        Button strikesButton = findViewById(R.id.strikes_button);

        updateCountText(balls, strikes);


        try {
            FileInputStream inputStream = openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            System.out.println("Reading in '" + sb.toString() + "'");
            outs = Integer.parseInt(sb.toString());
            updateOuts(outs);

        } catch (IOException e) {
            e.printStackTrace();
        }

        walkAlert = new AlertDialog.Builder(UmpireActivity.this)
                .setMessage("Take your base.")
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

        strikeOutAlert = new AlertDialog.Builder(UmpireActivity.this)
                .setMessage("You're Out!")
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

        ballsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBall();
            }
        });

        strikesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStrike();
            }
        });

        View umpireView = findViewById(R.id.background);

        umpireView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mActionMode != null) {
                    return false;
                }

                mActionMode = startSupportActionMode(mActionModeCallback);
                return true;
            }
        });
    }

    private void addBall() {
        // Every click increment the ball counter
        ++balls;

        // If it is the fourth ball, reset the count and show an alert
        if (balls == 4) {
            balls = 0;
            strikes = 0;

            walkAlert.show();
        }

        // Display the changes
        updateCountText(balls, strikes);
    }

    private void addStrike() {
        // Every click increment the strike counter
        ++strikes;

        // If it is the third strike, reset the count and show an alert
        if (strikes == 3) {
            balls = 0;
            strikes = 0;

            strikeOutAlert.show();

            ++outs;
            updateOuts(outs);
        }

        // Display the changes
        updateCountText(balls, strikes);
    }
}
