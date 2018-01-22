package com.game.spshe.randomgame;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Canvas;

/**
 * Created by spshe on 12/28/2016.
 */

public class ChangingBall extends MovingBall{

    private int originalRed = 255;
    private int originalGreen = 255;
    private int originalBlue = 255;
    private int totalTime = 3900;
    private int count = 0;

    private double incrementingRedFactor = 0;
    private double incrementingGreenFactor = 0;
    private double incrementingBlueFactor = 0;

    public ChangingBall(Context context, int numberOfObjects, double heightRatio, double widthRatio)
    {
        super(context, numberOfObjects, heightRatio, widthRatio);

    }
    public void resetIncrement()
    {
        incrementingBlueFactor = 0;
        incrementingGreenFactor = 0;
        incrementingRedFactor = 0;
    }

    protected void onDraw (Canvas canvas) //Overwriting onDraw because color also needs to be updated
    {
        ballBounds.set(ballX-ballRadius, ballY-ballRadius, ballX+ballRadius, ballY+ballRadius); //figure this out

        update();

        incrementingRedFactor += (originalRed - redValue)/(totalTime/30);
        incrementingGreenFactor += (originalGreen - greenValue)/(totalTime/30);
        incrementingBlueFactor += (originalBlue - blueValue)/(totalTime/30);
        /*  colorValue - originalColor | the difference in RGB values
            count                      | the current increment number
            totalTime/30               | number of increments
        */
        paint.setColor(Color.rgb((int)(originalRed - incrementingRedFactor),
                (int)(originalGreen - incrementingGreenFactor),(int)(originalBlue - incrementingBlueFactor)));
       // paint.setColor(Color.rgb((int)(originalRed - 1.5),
                //(int)(originalGreen - 1.5 ),(int)(originalBlue - 1.5)));
        canvas.drawOval(ballBounds, paint);



        try{
            Thread.sleep(30/delayFactor);
            timeRemaining -= 30;

        } catch(InterruptedException e){ }

        if(timeRemaining <= 0)
        {
            paint.setColor(Color.rgb(redValue, greenValue, blueValue));
            canvas.drawOval(ballBounds, paint);
            canBeSelected = true;
        }
        else
            invalidate();

    }
}
