package com.mygdx.game.model;

import java.util.ArrayList;

/**
 * Created by antonlin on 16-04-11.
 */
public class GameState {

    private Player player1;
    private Player player2;
    private ArrayList<Ball> balls;

    private int roundsPlayed = 0;

    //Size of game window
    private float width, height;

    public GameState(float width, float height) {
        this.width = width;
        this.height = height;

        balls = new ArrayList<Ball>();
        balls.add(new Ball(width / 2, height / 2, 32));
    }

    public Player[] getPlayers() {
        Player[] players = {player1, player2};
        return players;
    }

    //So that several balls can be implemented
    public ArrayList<Ball> getBalls() {
        return  balls;
    }

    public void update() {

        Ball ball = balls.get(0);

        PhysicsHelper.ballCollision(width,height, getPlayers(), balls);

        ball.xPos += ball.xVelocity;
        ball.yPos += ball.yVelocity;
    }
}
