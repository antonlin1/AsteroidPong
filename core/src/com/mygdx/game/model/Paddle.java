package com.mygdx.game.model;

/**
 * Created by antonlin on 16-04-11.
 */
public class Paddle {

    //Points out bottom left corner(?)
    protected float xPos;
    protected float yPos;
    protected float length;

    protected float height;

    //Rounds won
    protected int score;

    public Paddle(float xPos, float yPos, float length) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.length = length;
        this.height = GameState.PaddleConstant.HEIGHT.value;
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

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
