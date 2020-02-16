package com.pl.connect3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    LinearLayout layout;

    // 0 == green, 1 == red
    int activePlayer = 0;
    boolean gameIsActiv = true;

    int[] gameState = {2,2,2,2,2,2,2,2,2};

    int[][] winningPositions = {{0,1,2}, {0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};

    public void dropIn(View view) {
        ImageView counter = (ImageView) view;

        System.out.println(counter.getTag().toString());

        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (gameState[tappedCounter] == 2 && gameIsActiv) {
            gameState[tappedCounter] = activePlayer;

            counter.setTranslationY(-1000f);

            if (activePlayer == 0) {
                counter.setImageResource(R.drawable.green);

                activePlayer = 1;
            } else {
                counter.setImageResource(R.drawable.red);

                activePlayer = 0;
            }

            counter.animate().translationYBy(1000f).rotation(360).setDuration(300);

            for (int[] winningPosition : winningPositions)
            {
                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]] != 2)
                {

                    gameIsActiv = false;

                    String winner = "Red";
                    if(gameState[winningPosition[0]] == 0)
                    {
                        winner = "Green";
                    }
                    TextView winnerMessage = findViewById(R.id.winner_message);
                    winnerMessage.setText(winner + " has Won!");
                    LinearLayout layout = findViewById(R.id.play_again_layout);
                    layout.setVisibility(View.VISIBLE);
                }
            }

                if (gameIsActiv) {

                    boolean gameIsOver = true;
                    for (int counterState : gameState) {
                        if (counterState == 2) gameIsOver = false;
                    }
                    if (gameIsOver) {
                        TextView winnerMessage = findViewById(R.id.winner_message);
                        winnerMessage.setText("It's a Draw");
                        LinearLayout layout = findViewById(R.id.play_again_layout);
                        layout.setVisibility(View.VISIBLE);
                    }
                }

        }
    }

    public void playAgain(View view)
    {
        gameIsActiv = true;
        layout = findViewById(R.id.play_again_layout);
        layout.setVisibility(View.INVISIBLE);

        activePlayer = 0;


        for (int i = 0; i < gameState.length; i++)
        {
            gameState[i] = 2;
        }
        GridLayout gridLayout = findViewById(R.id.grid_layout);

        for (int i = 0; i < gridLayout.getChildCount(); i++)
        {
            ((ImageView) gridLayout.getChildAt(i)).setImageResource(0);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
