package com.mygdx.game.model;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

import javax.print.attribute.standard.PDLOverrideSupported;

/**
 * Created by antonlin on 16-04-11.
 */
public class PhysicsHelper {


		public static boolean wallCollision(float width, float height, ArrayList<Ball> balls) {
				Ball ball = balls.get(0);
				boolean collision = false;
				//Collision with sides
				if (ball.getX() >= width - ball.getRadius() || ball.getX() <= ball.getRadius()) {
						ball.reverseXVelocity();
						collision = true;
				}

				//Collision with top
//				if (ball.getY() <= ball.getRadius()) {
//						ball.reverseYVelocity();
//						collision = true;
//				}
				//Death
//				if (ball.getY() >= height - ball.getRadius()) {
//
//				 ball.reverseYVelocity();
//				 ball.setY(1000);
//				 ball.setX(ball.getRadius() + 1);
//
//
//				}
//
//				//Top paddle
//				 if(ball.getX() + ball.getRadius() > paddle2.getX() && ball.getX() - ball.getRadius() < paddle2.getX() + GameState.PaddleConstant.LENGTH.value
//				         && ball.getY() - ball.getRadius() < paddle2.getY() + GameState.PaddleConstant.HEIGHT.value){
//				     ball.yVelocity *= -1;
//				     GameState.bounces ++;
//				     if(GameState.bounces % 5 == 0) {
//				         //PhysicsHelper.increaseSpeed(balls);
//				       }
//				     }

				return collision;

		}

		public static boolean isDead(float width, float height, ArrayList<Ball> balls) {
				Ball ball = balls.get(0);
				if (ball.getY() >= height + ball.getRadius() || ball.getY() <=  - ball.getRadius()) {
						//ball.yVelocity *= -1;
						return true;
				}
				return false;

		}

		static boolean isCollision = false;

		public static boolean paddleCollision(Paddle[] paddles, ArrayList<Ball> balls) {
				Paddle paddle1 = paddles[0];
				Paddle paddle2 = paddles[1];
				Ball ball = balls.get(0);

				boolean makeCollisionSound = false;

				if ((isCollision(ball, paddle1) || isCollision(ball, paddle2)) && !isCollision) {

						ball.reverseYVelocity();
						GameState.bounces++;
						//GameState.paddleCollision = true;
						isCollision = true;
						makeCollisionSound = true;
						//    if(GameState.bounces % 5 == 0) {
						//        PhysicsHelper.increaseSpeed(balls);
						//    }

				}

				if (!isCollision(ball, paddle1) && !isCollision(ball, paddle2)) {
						isCollision = false;
				}

				return makeCollisionSound;
		}

		public static boolean isCollision(Ball ball, Paddle paddle) {
				return ball.getX() + ball.getRadius() >= paddle.getX() &&
						ball.getX() - ball.getRadius() <= paddle.getX() + GameState.PaddleConstant.LENGTH.value
						&& ((ball.getY() + ball.getRadius() >= paddle.getY()
						&& ball.getY() - ball.getRadius() <= paddle.getY() - paddle.getHeight())
						|| (ball.getY() - ball.getRadius() <= paddle.getY()
						&& ball.getY() + ball.getRadius() >= paddle.getY() + paddle.getHeight()));
		}


		/*public static void increaseSpeed(ArrayList<Ball> balls) {
				if (GameState.bounces < 5 * 10) {
						for (Ball ball : balls) {
								ball.xVelocity *= 1.1;
								ball.yVelocity *= 1.1;
						}
				}
		}*/
}
