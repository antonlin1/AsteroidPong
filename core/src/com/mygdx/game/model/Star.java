package com.mygdx.game.model;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

/**
 * Created by johanmansson on 16-04-12.
 */
public class Star {

    private float xPos;
    private float yPos;
    private float size;


    public Star(float xPos, float yPos, float size) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.size = size;

    }

    public float getxPos() {
        return xPos;
    }
    public float getyPos() {
        return yPos;
    }

    public float getSize() {
        return size;
    }





}
