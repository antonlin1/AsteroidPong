package com.mygdx.game.model;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

/**
 * Created by johanmansson on 16-04-12.
 */
public class Star {

    private float xPos;
    private float yPos;
    private long timeStamp;
    private Random rand;

    public Star(float xPos, float yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        rand = new Random();
        timeStamp = (long) rand.nextInt(500);

    }

    public float getxPos() {
        return xPos;
    }
    public float getyPos() {
        return yPos;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void updateTimeStamp() {
        if(timeStamp > 500) {
            timeStamp = (long) rand.nextInt(500);

        }

    }


}
