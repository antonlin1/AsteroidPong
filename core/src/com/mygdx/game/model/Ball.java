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

        //Somewhat randomize ball start direction
        this.xVelocity = (Math.random() < 0.5) ? 10f : -10f;
        this.yVelocity = (Math.random() < 0.5) ? 10f : -10f;;
    }

    public float getX() {
        return xPos;
    }

    public void setX(float xPos) {
        this.xPos = xPos;
    }

    public float getY() {
        return yPos;
    }

    public float getXVel() {
        return xVelocity;
    }

    public float getYVel() {
        return yVelocity;
    }


    public void setY(float yPos) {
        this.yPos = yPos;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

}
