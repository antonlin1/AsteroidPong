package com.mygdx.game.model;

import java.util.ArrayList;

/**
 * Created by antonlin on 16-04-11.
 */
public class PhysicsHelper {

    public static void ballCollision(float width, float height ,Paddle[] paddles, ArrayList<Ball> balls) {
        Ball ball = balls.get(0);
        Paddle paddle1 = paddles[0];
        Paddle paddle2 = paddles[1];

        //Collision with sides
        if(ball.xPos>= width - ball.radius|| ball.xPos <= ball.radius) {
            ball.xVelocity *= -1;
        }

        //Death(TO BE IMPLEMENTED)
        if(ball.yPos >= height - ball.radius || ball.yPos <= ball.radius) {
            ball.yVelocity *= -1;
        }

        //Top paddle
        if(ball.getxPos() + ball.getRadius() > paddle2.getxPos() && ball.getxPos() - ball.getRadius() < paddle2.getxPos() + GameState.PaddleConstant.LENGTH.value
                && ball.getyPos() - ball.getRadius() < paddle2.getyPos() + GameState.PaddleConstant.HEIGHT.value){
            ball.yVelocity *= -1;
            GameState.bounces ++;
            if(GameState.bounces % 5 == 0) {
                PhysicsHelper.increaseSpeed(balls);
            }
        }

        //Bottom paddle
        if(ball.getxPos() + ball.getRadius() > paddle1.getxPos() && ball.getxPos() - ball.getRadius() < paddle1.getxPos() + GameState.PaddleConstant.LENGTH.value
                && ball.getyPos() + ball.getRadius() > paddle1.getyPos()){
            ball.yVelocity *= -1;
            GameState.bounces ++;

            if(GameState.bounces % 5 == 0) {
                PhysicsHelper.increaseSpeed(balls);
            }
        }
    }

    public static void increaseSpeed(ArrayList<Ball> balls){
        for(Ball ball : balls){
            ball.xVelocity *= 1.1;
            ball.yVelocity *= 1.1;
        }
    }
}
