package com.mygdx.game.model;



import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Created by antonlin on 16-04-11.
 */
public class PhysicsHelper {


    public static boolean wallCollision(float width, float height, ArrayList<Ball> balls) {
        Ball ball = balls.get(0);
        boolean collision = false;
        //Collision with sides
        if(ball.xPos>= width - ball.radius|| ball.xPos <= ball.radius) {
            ball.xVelocity *= -1;
            collision = true;
        }

        //Collision with top
        if( ball.yPos <= ball.radius) {
            ball.yVelocity *= -1;
            collision = true;
        }
        //Death
        if(ball.yPos >= height - ball.radius) {

           // ball.yVelocity *= -1;
           // ball.setY(1000);
           // ball.setX(ball.getRadius() + 1);



        }

        //Top paddle
       // if(ball.getX() + ball.getRadius() > paddle2.getX() && ball.getX() - ball.getRadius() < paddle2.getX() + GameState.PaddleConstant.LENGTH.value
       //         && ball.getY() - ball.getRadius() < paddle2.getY() + GameState.PaddleConstant.HEIGHT.value){
       //     ball.yVelocity *= -1;
       //     GameState.bounces ++;
       //     if(GameState.bounces % 5 == 0) {
       //         //PhysicsHelper.increaseSpeed(balls);
     //       }
   //     }

        return collision;

    }

    public static boolean isDead(float width, float height, ArrayList<Ball> balls) {
        Ball ball = balls.get(0);
        if(ball.yPos >= height + ball.radius) {
            //ball.yVelocity *= -1;
            return true;
        }
        return false;

    }

    public static boolean paddleCollision(Paddle[] paddles, ArrayList<Ball> balls) {
        Paddle paddle1 = paddles[0];
        Paddle paddle2 = paddles[1];
        Ball ball = balls.get(0);
        boolean collision = false;

        if(ball.getX() + ball.getRadius() > paddle1.getX() && ball.getX() - ball.getRadius() < paddle1.getX() + GameState.PaddleConstant.LENGTH.value
                && ball.getY() + ball.getRadius() > paddle1.getY()){
            ball.yVelocity *= -1;
            GameState.bounces ++;
            //GameState.paddleCollision = true;
            collision = true;
        //    if(GameState.bounces % 5 == 0) {
        //        PhysicsHelper.increaseSpeed(balls);
        //    }
        }
        return collision;
    }

    public static void increaseSpeed(ArrayList<Ball> balls){
        if(GameState.bounces < 5 * 10) {
            for(Ball ball : balls){
                ball.xVelocity *= 1.1;
                ball.yVelocity *= 1.1;
            }
        }
    }
}
