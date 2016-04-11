package com.mygdx.game.model;

/**
 * Created by antonlin on 16-04-11.
 */
public class Player {

    //Points out bottom left corner(?)
    private float xPos;
    private float yPos;
    private float size;

    //Rounds won
    private int score;

    public Player(float xPos, float yPos, float size) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.size = size;
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

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
