package com.mygdx.game.model;

import java.util.ArrayList;


/**
 * Created by antonlin on 16-04-11.
 */
public class GameState {

		private Paddle paddle1;
		private Paddle paddle2;
		private ArrayList<Ball> balls;

		protected static int roundsPlayed = 0;
		protected static int bounces = 0;
		//  protected static boolean paddleCollision = false;
		//Size of game window
		private float width, height;

		private boolean wallCollision;
		private boolean isDead;
		private boolean paddleCollision;


		public enum PaddleConstant {
				HEIGHT(32), LENGTH(64 * 3), YPOS(128);

				public int value;

				private PaddleConstant(int value) {
						this.value = value;
				}
		}

		public GameState(float width, float height) {
				this.width = width;
				this.height = height;

				balls = new ArrayList<Ball>();
				balls.add(new Ball(width / 2, height / 2, 32));


				paddle1 = new Paddle(width / 2 - 32, height - PaddleConstant.YPOS.value, (float) PaddleConstant.LENGTH.value);
				//  paddle2 = new Paddle(width / 2 - 32, PaddleConstant.YPOS.value -20, (float)PaddleConstant.LENGTH.value);


		}

		public boolean isDead() {
				return isDead;
		}

		//stops ball from moving

		public void killBall() {
				balls.get(0).kill();
		}

		//move ball object to random position



		public Paddle[] getPaddles() {
				Paddle[] paddles = {paddle1, paddle2};
				return paddles;
		}


		//So that several balls can be implemented
		public ArrayList<Ball> getBalls() {
				return balls;
		}

		public void update() {
				Ball ball = balls.get(0);

				wallCollision = false;
				paddleCollision = false;
				isDead = false;
				wallCollision = PhysicsHelper.wallCollision(width, height, balls);
				paddleCollision = PhysicsHelper.paddleCollision(getPaddles(), balls);
				isDead = PhysicsHelper.isDead(width, height, balls);

				if(isDead) {
						isDead = false;
						ball.randomizePos(width, height);
				}

				ball.move();
		}

		public float getWidth() {
				return width;
		}

		public float getHeight() {
				return height;
		}

		public boolean isWallCollision() {
				return wallCollision;
		}

		public boolean isPaddleCollision() {
				return paddleCollision;
		}

	//	public boolean newBall() {
	//			return PhysicsHelper.isDead(width, height, balls);
	//	}


}
