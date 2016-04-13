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


    public Star(float xPos, float yPos, long timeStamp) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.timeStamp = timeStamp;

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

    public void updateTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;

    }





}
