package com.game.spshe.randomgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by spshe on 12/28/2016.
 */

public class MovingBall extends View {

    //Scales pixels with screen size


    double heightRatioObj;
    double widthRatioObj;;

    //Board size
    private int xMin = 0;
    private int xMax;
    private int yMin = 0;
    private int yMax;

    //Ball size
    protected float ballRadius = 30;

    //Ball color
    protected int redValue = 255;
    protected int blueValue = 0;
    protected int greenValue = 0;

    //Ball initial position
    protected float ballX = (float)(Math.random() * 1000);
    protected float ballY = (float)(Math.random() * 1600);

    //Ball speed
    private float ballSpeedX = (float)(Math.random() * 50);
    private float ballSpeedY = 50 - ballSpeedX;

    //Random initializing stuff
    protected RectF ballBounds; // canvas oval
    protected Paint paint;

    //Time
    protected int timeRemaining = 5000;
    protected int delayFactor = 1; /* Delay factor is based on the number of active balls in the program. Since the program repaints
     itself for each object, the delay factor would need to be higher as there are more objects running.
     Ex: two objects will take twice as long to run compared to only one object if the delay factor is not changed*/

    //Selecting balls
    protected static boolean canBeSelected = false;
    private static boolean isSelected = false;

    protected int sampleCount = 0;



    public MovingBall(Context context, int numberOfObjects, double heightRatio, double widthRatio)
    {
        super(context);
        ballBounds = new RectF();
        paint = new Paint();
        delayFactor = numberOfObjects;

        heightRatioObj = heightRatio;
        widthRatioObj = widthRatio;
        xMax = (int)(1000 * widthRatioObj);
        yMax = (int)(1300 * heightRatioObj);
        ballRadius = (int)(60*heightRatio);
        ballSpeedX = (float)(Math.random() * 50 *widthRatio);
        ballSpeedY = (int)(50*heightRatio) - ballSpeedX;

    }


    protected void onDraw (Canvas canvas)
    {
        if (canBeSelected == false)
        {

            ballBounds.set(ballX-ballRadius, ballY-ballRadius, ballX+ballRadius, ballY+ballRadius); //figure this out
            paint.setColor(Color.rgb(redValue,greenValue,blueValue)); // MIGHT NEED TO USE RGB
            canvas.drawOval(ballBounds, paint); // actually draws the shape
            update(); // moves the ball


            try{
                Thread.sleep(30/delayFactor);
                timeRemaining -= 30;
            } catch(InterruptedException e){ }
            //invalidate();
        }
        else
        {
            if(getIsSelcted())
            {
                paint.setColor(Color.BLACK);
                canvas.drawCircle(ballX, ballY, ballRadius + 10, paint);
            }

            ballBounds.set(ballX-ballRadius, ballY-ballRadius, ballX+ballRadius, ballY+ballRadius); //figure this out
            paint.setColor(Color.rgb(redValue,greenValue,blueValue)); // MIGHT NEED TO USE RGB
            canvas.drawOval(ballBounds, paint); // actually draws the shape
            //Log.d("isSelected", String.valueOf(isSelected));

//            paint.setColor(Color.BLACK);
//            canvas.drawCircle(ballX, ballY, ballRadius + 10, paint);
        }

        if(timeRemaining <= 0)
            canBeSelected = true;

        invalidate();

    }

    public void undoCanBeSelected()
    {
        canBeSelected = false;
    }

    public boolean getCanBeSelected()
    {
        return canBeSelected;
    }

    public float getBallX()
    {
        return ballX;
    }
    public float getBallY()
    {
        return ballY;
    }

    protected void update()
    {
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        if (ballX + ballRadius > xMax) // hits far right of board
        {
            ballSpeedX = -ballSpeedX;
            ballX = xMax - ballRadius;
        }
        else if (ballX - ballRadius < xMin) // hits far left of board
        {
            ballSpeedX = -ballSpeedX;
            ballX = xMin + ballRadius;
        }

        if (ballY + ballRadius > yMax) // hits top of board
        {
            ballSpeedY = -ballSpeedY;
            ballY = yMax - ballRadius;
        }
        else if (ballY - ballRadius < yMin) // hits bottom of board
        {
            ballSpeedY = -ballSpeedY;
            ballY = yMin + ballRadius;
        }

    }

    public void select() //User wants to select the ball
    {
        isSelected = !isSelected;
        //return isSelected;
    }

    public boolean getIsSelcted()
    {
        return isSelected;
    }
    public float getRadius()
    {
        return ballRadius;
    }
}
