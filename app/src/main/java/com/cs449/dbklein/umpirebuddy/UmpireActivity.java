package com.cs449.dbklein.umpirebuddy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UmpireActivity extends AppCompatActivity {

    private int balls;
    private int strikes;

    private TextView ballsText;
    private TextView strikesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umpire);

        balls = 0;
        strikes = 0;

        ballsText = findViewById(R.id.balls_text);
        strikesText = findViewById(R.id.strikes_text);

        Button ballsButton = findViewById(R.id.balls_button);
        Button strikesButton = findViewById(R.id.strikes_button);

        final AlertDialog.Builder walkAlert = new AlertDialog.Builder(UmpireActivity.this)
                .setMessage("Take your base.")
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        final AlertDialog.Builder strikeOutAlert = new AlertDialog.Builder(UmpireActivity.this)
                .setMessage("You're Out!")
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

        updateCountText(balls, strikes);


        ballsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        strikesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Every click increment the strike counter
                ++strikes;

                // If it is the third strike, reset the count and show an alert
                if (strikes == 3) {
                    balls = 0;
                    strikes = 0;

                    strikeOutAlert.show();
                }

                // Display the changes
                updateCountText(balls, strikes);
            }
        });
    }

    private void updateCountText(int balls, int strikes) {
        ballsText.setText(getString(R.string.balls_label_text, balls));
        strikesText.setText(getString(R.string.strikes_label_text, strikes));
    }
}
