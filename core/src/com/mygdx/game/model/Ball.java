package com.mygdx.game.model;

/**
 * Created by antonlin on 16-04-11.
 */
public class Ball {

    protected float xPos;
    protected float yPos;
    protected float radius;

    protected float xVelocity;
    protected float yVelocity;

    public Ball(float xPos, float yPos, float radius) {

        this.xPos = xPos;
        this.yPos = yPos;
        this.radius = radius;

        this.xVelocity = 10f;
        this.yVelocity = 10f;
    }

    public float getxPos() {
        return xPos;
    }

    public void setxPos(float xPos) {
        this.xPos = xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public void setyPos(float yPos) {
        this.yPos = yPos;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

}
