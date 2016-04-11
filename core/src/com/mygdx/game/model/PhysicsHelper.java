package com.mygdx.game.model;

import java.util.ArrayList;

/**
 * Created by antonlin on 16-04-11.
 */
public class PhysicsHelper {

    public static void ballCollision(float width, float height ,Player[] players, ArrayList<Ball> balls) {
        Ball ball = balls.get(0);
//        //DEATH!
//        if(_ballY > _screenHeight || _ballY < 0)
//        {_ballX = 100; 	_ballY = 100;}
//
//        //Collisions with the sides
//        if(_ballX > _screenWidth || _ballX < 0)
//            _ballVelocityX *= -1;
//        //Collisions with the bats
//        if(_ballX > _topBatX && _ballX < _topBatX+_batLength && _ballY < _topBatY)
//            _ballVelocityY *= -1;
//        //Collisions with the bats
//        if(_ballX > _bottomBatX && _ballX < _bottomBatX+_batLength
//                && _ballY > _bottomBatY)
//            _ballVelocityY *= -1;

        if(ball.xPos>= width - ball.radius|| ball.xPos <= ball.radius) {
            ball.xVelocity *= -1;
        }

        if(ball.yPos >= height - ball.radius || ball.yPos <= ball.radius) {
            ball.yVelocity *= -1;
        }

    }
}
