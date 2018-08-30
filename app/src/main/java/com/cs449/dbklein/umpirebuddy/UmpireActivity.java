package com.cs449.dbklein.umpirebuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        updateCountText(balls, strikes);

        Button ballsButton = findViewById(R.id.balls_button);
        Button strikesButton = findViewById(R.id.strikes_button);

        ballsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++balls;

                if (balls == 4) {
                    balls = 0;
                    strikes = 0;
                }

                updateCountText(balls, strikes);
            }
        });

        strikesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++strikes;

                if (strikes == 3) {
                    balls = 0;
                    strikes = 0;
                }

                updateCountText(balls, strikes);
            }
        });
    }

    private void updateCountText(int balls, int strikes) {
        ballsText.setText(getString(R.string.balls_label_text, balls));
        strikesText.setText(getString(R.string.strikes_label_text, strikes));
    }
}
