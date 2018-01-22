package com.game.spshe.randomgame;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final int numberOfStaticBalls = 1;
    final int numberOfChangingBalls = 1;
    final int totalNumber = numberOfChangingBalls + numberOfStaticBalls;
    static boolean endOfGame = false;
    static int increment = 0;
    double widthRatio;
    double heightRatio;


    final List<MovingBall> staticBalls = new ArrayList<>(); //Balls that DO NOT CHANGE COLOR
    final List<ChangingBall> changingBalls = new ArrayList<>(); //Balls that CHANGE COLOR

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display d = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        double width = d.getWidth();
        double height = d.getHeight();
        widthRatio = width/1080;
        heightRatio = height/1920;
        mainMenu();
    }

    public void mainMenu() //loads main menu
    {
        setContentView(R.layout.activity_main); //initializing starting board
        Button start = (Button) findViewById(R.id.start);
        //start.setX(400);
        start.setY(0);
        //start.setGravity(Gravity.CENTER_HORIZONTAL);

//        Button howToPlay = (Button) findViewById(R.id.howToPlay);
//        //howToPlay.setX(350);
//        howToPlay.setY(200);

        start.setOnClickListener(new View.OnClickListener() { // start button click
            @Override
            public void onClick(View view) {
                startGame();
            }
        });

//        howToPlay.setOnClickListener(new View.OnClickListener() { // start button click
//            @Override
//            public void onClick(View view) {
//                startGame();
//            }
//        });
    }

    public void endGame()
    {
        setContentView(R.layout.end_game); //using end_game xml file
        RelativeLayout endwrap = (RelativeLayout) findViewById(R.id.end_game); //endwrap adds buttons and textviews onto screen
        final List<Boolean> staticChecked = new ArrayList<>(); //keeps track of static buttons selected
        final List<Boolean> changingChecked = new ArrayList<>(); //keeps track of changing buttons selected

        TextView levelLabel = (TextView) findViewById(R.id.levelLabel); //formatting
        levelLabel.setText("LEVEL " + (increment + 1));
        levelLabel.setX((int)(400*widthRatio));
        levelLabel.setY((int)(1400*heightRatio));

//        final TextView submitMessage = new TextView(this); //formatting
//        submitMessage.setX(0);
//        submitMessage.setY((int)(1450*heightRatio));
//        submitMessage.setText("Select the balls that were changing or remained the same \n" +
//                "CHANGING means the ball was changing color \n" +
//                "SAME means that the ball remained the same" ); //label that instructs users
//        endwrap.addView(submitMessage);

        Button submitButton = (Button) findViewById(R.id.submit); //formatting
        submitButton.setX((int)(800*widthRatio));
        submitButton.setY((int)(1600*heightRatio));
        submitButton.setOnClickListener(new View.OnClickListener() { //processes data once user decides to submit
            @Override
            public void onClick(View view) {
                boolean allCorrect = true;
                for(int i = 0; i < staticChecked.size(); i++) //ALL items in staticChecked must be true (all static balls found)
                {
                    if(staticChecked.get(i) == false)
                        allCorrect = false;
                    //Log.d("Static" + i, String.valueOf(staticChecked.get(i)) );
                }
                for(int i = 0; i < changingChecked.size(); i++) //ALL items in changingChecked must be false (all changing balls found)
                {
                    if(changingChecked.get(i) == true)
                        allCorrect = false;
                    //Log.d("Changing" + i, String.valueOf(changingChecked.get(i)) );
                }
                if(allCorrect) //LEVEL UP
                {
                    increment ++;
                    startGame();
                }
                else //GAME ENDS
                    gameOver();
            }
        });

        for (int i = 0; i < numberOfStaticBalls + increment; i++) //creates toggle button for static balls
        {
            staticChecked.add(false); //initializes all selections as false
            final ToggleButton btn = new ToggleButton(this);
            btn.setText("Changing"); //text formatting depending on what user selects
            btn.setTextOn("Same");
            btn.setTextOff("Changing");
            btn.setId(i);
            endwrap.addView(btn); //adds button to RelativeLayout
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CharSequence checkState = btn.getText();
                    if(checkState.equals("Same")) //user thinks button is static
                        staticChecked.set(btn.getId(), true);
                    else
                        staticChecked.set(btn.getId(), false);
                }
            });
            btn.setX(staticBalls.get(i).getBallX() - (int)(120*widthRatio));
            btn.setY(staticBalls.get(i).getBallY() - (int)(80*heightRatio));
        }
        for (int i = 0; i < numberOfChangingBalls + increment; i++) //creates toggle button for changing balls
        {
            changingChecked.add(false);
            final ToggleButton btn = new ToggleButton(this);
            btn.setText("Changing");
            btn.setTextOn("Same");
            btn.setTextOff("Changing");
            btn.setId(i);

            endwrap.addView(btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CharSequence checkState = btn.getText();
                    if(checkState.equals("Same")) //determines if user thinks button is static
                        changingChecked.set(btn.getId(), true); //user thinks button is static
                    else
                        changingChecked.set(btn.getId(), false); //user thinks button is changing
                }
            });
            btn.setX(changingBalls.get(i).getBallX() - (int)(120*widthRatio)); //formatting
            btn.setY(changingBalls.get(i).getBallY() - (int)(80*heightRatio));
        }
    }

    public void startGame()
    {
        //BACKGROUND FORMATION
        staticBalls.clear(); //clears arraylists because new objects will be loaded
        changingBalls.clear();
        for(int i = 0; i < numberOfStaticBalls + increment; i++) //adds number of static balls based on scaling
        {
            final MovingBall sample = new MovingBall(this, totalNumber + increment*2, heightRatio, widthRatio);
            sample.undoCanBeSelected();
            staticBalls.add(sample);
        }
        for(int i = 0; i < numberOfChangingBalls + increment; i++) //adds number of changing balls based on scaling
        {
            final ChangingBall sample = new ChangingBall(this, totalNumber + increment*2, heightRatio, widthRatio); //increment doubled because based on total balls instead of individual
            sample.undoCanBeSelected();
            sample.resetIncrement();
            changingBalls.add(sample);
        }

        //ACTUAL USER INTERFACE
        setContentView(R.layout.in_game);
        TextView levelLabel = (TextView) findViewById(R.id.levelLabel); //formatting level label
        levelLabel.setText("LEVEL " + (increment + 1)); //level scaling
        levelLabel.setX((int)(400*widthRatio));
        levelLabel.setY((int)(1400*heightRatio));
        for (int i = 0; i < numberOfStaticBalls + increment; i++)
        {
            addContentView(staticBalls.get(i), new ViewGroup.LayoutParams((int)(1000*widthRatio),(int)(1600*heightRatio)));
        }
        for (int i = 0; i < numberOfChangingBalls + increment; i++)
        {
            addContentView(changingBalls.get(i), new ViewGroup.LayoutParams((int)(1000*widthRatio),(int)(1600*heightRatio)));
        }
        new CountDownTimer(6000, 1000) { //timer (should be 5 seconds)

            public void onTick(long millisUntilFinished) {
                //mTextField.setText("Seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                endGame();
            }

        }.start();
    }

    public void gameOver() //loads when player loses
    {
        setContentView(R.layout.game_over);
        Button restartButton = (Button) findViewById(R.id.restart); //formatting restart button
        //restartButton.setX(380);
        restartButton.setY((int)(200*heightRatio));

        Button menuButton = (Button) findViewById(R.id.mainmenu); //formatting restart button
        //menuButton.setX(380);
        menuButton.setY((int)(300*heightRatio));
        TextView restartText = (TextView) findViewById(R.id.restartText); //formatting label
        //restartText.setX(400);
        restartText.setY((int)(400*heightRatio));

        restartButton.setOnClickListener(new View.OnClickListener() { //restarts game
            @Override
            public void onClick(View view) {
                increment = 0; //restarts level
                startGame();
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() { //restarts game
            @Override
            public void onClick(View view) {
                increment = 0;
                mainMenu();
            }
        });

    }













}
