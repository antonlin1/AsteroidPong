package com.mygdx.game.view.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Controller.InputController;
import com.mygdx.game.PeerHelperInterface;
import com.mygdx.game.SpeechHelperInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.model.Ball;
import com.mygdx.game.model.Paddle;
import com.mygdx.game.view.MyGdxGame;
import com.mygdx.game.view.Particles;

import java.util.ArrayList;

import static com.mygdx.game.view.States.GameState.PaddleConstant.*;


public abstract class GameState
		extends com.mygdx.game.view.States.State {

		protected Paddle paddle1;
		protected Paddle paddle2;
		protected ArrayList<Ball> balls;

		protected float width, height;

		protected boolean wallCollision;
		protected boolean isDead;
		protected boolean paddleCollision;
		protected Particles particles;

		protected InputController input;

		protected Sound collisionSound;
		protected Sound gameOverSound;
		protected Texture cancel;
		protected Texture pause;






		//private WifiDirectInterface wifiDirect;
		/**
		 * Created by antonlin on 16-04-11.
		 */

		public enum PaddleConstant {
				HEIGHT(32), LENGTH(64 * 3), YPOS(128);

				public int value;

				PaddleConstant(int value) {
						this.value = value;
				}
		}

		public GameState(MyGdxGame game, StateManager stateManager, float width, float height,
						 PeerHelperInterface peerHelper, WifiDirectInterface wifiDirect,
						 StateManager.STATE_NAME state) {
				super(game, stateManager, state, wifiDirect, peerHelper);

				this.width = width;
				this.height = height;
				//this.wifiDirect = wifiDirect;

				balls = new ArrayList<Ball>();
				balls.add(new Ball(width / 2, height / 2, 32));

				paddle1 = new Paddle(width / 2 - 32, height - YPOS.value - 100, (float) LENGTH.value);
				paddle2 = new Paddle(width / 2 - 32, YPOS.value + 100, (float) LENGTH.value);

				particles = new Particles();
				collisionSound = Gdx.audio.newSound(Gdx.files.internal("bounce1.wav"));
				gameOverSound = Gdx.audio.newSound(Gdx.files.internal("missedBall2.wav"));


				cancel = new Texture("cancel2.png");
				pause = new Texture("paus1.png");




		}

		public void update() {
				/**
				 * WHY ?!
				 */
				wallCollision = false;
				paddleCollision = false;
				isDead = false;
		}

//    private void setFullState(boolean gameActive, boolean paddleCollision, boolean wallCollision, float paddleX, float ballX, float ballY,
//                              float ballXVelocity, float ballYVelocity, float ballVelocity) {
//
//        Ball ball = balls.get(0);
//
//        this.paddleCollision = paddleCollision;
//        this.wallCollision = wallCollision;
//
//        ball.setX(ballX);
//        ball.setY(ballY);
//
////        wallCollision = PhysicsHelper.wallCollision(width, height, balls);
////        paddleCollision = PhysicsHelper.paddleCollision(getPaddles(), balls);
//        isDead = PhysicsHelper.isDead(width, height, balls);
//        setPaddle2(paddleX);
//
//
//        //ball.move();
//    }

		public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {



				Ball ball = getBalls().get(0);
				particles.makeParticles(ball);
				particles.removeParticles();

				/**
				 *  THERE SHOULD BE NO GAME LOGIC HERE
				 */
				if (isDead()) {
						//gameOverSound.play();
						//Gdx.input.vibrate(50);
						killBall();
						randomizePos();
//
				}
//
				Paddle paddle1 = getPaddles()[0];
				Paddle paddle2 = getPaddles()[1];

				if (isPaddleCollision()) {
						collisionSound.play();
						//Gdx.input.vibrate(50);
				}
				if (isWallCollision()) {
						collisionSound.play();
				}

			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
				particles.drawParticles(shapeRenderer);
				shapeRenderer.setColor(Color.WHITE);
				shapeRenderer.circle(ball.getX(), ball.getY(), ball.getRadius());
				shapeRenderer.setColor(Color.WHITE);
				shapeRenderer.rect(paddle1.getX(), paddle1.getY(), paddle1.getLength(), paddle1.getHeight());
				shapeRenderer.rect(paddle2.getX(), paddle2.getY(), paddle2.getLength(), paddle2.getHeight());
				shapeRenderer.end();

				spriteBatch.begin();
				spriteBatch.draw(pause, Gdx.graphics.getWidth() - cancel.getWidth() - 20, 20);
				spriteBatch.end();
		}

		public void handleTouchInput() {
				if (Gdx.input.justTouched()) {
						float x = Gdx.input.getX();
						float y = Gdx.input.getY();

						float x1 = Gdx.graphics.getWidth() - pause.getWidth() - 20;
						float x2 = Gdx.graphics.getWidth() + pause.getWidth() - 20;
						float y1 = 20;
						float y2 = pause.getHeight() + 20;

						if (x > x1 && x < x2 && y > y1 && y < y2) {
								System.out.println("Button pressed");
								game.getSpeechHelper().setPaused(true);
					//			stateManager.push(new GamePausedState(game, stateManager, wifiDirect, peerHelperInterface));
						}
				}
		}

		public void handleSpeechInput() {
			if(game.getSpeechHelper().isPaused() == true) {
				stateManager.push(new GamePausedState(game, stateManager, wifiDirect, peerHelperInterface));
			}
		}

		public boolean isDead() {
				return isDead;
		}

		//stops ball from moving

		public void killBall() {
				//   balls.get(0).kill();
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


		public void randomizePos() {
				Ball ball = balls.get(0);
				ball.randomizePos(width, height);
		}

		// OpponentPadle, will mirror xPos
		public void setPaddle2X(float xPos, float width) {
				paddle2.setX(width - (xPos + LENGTH.value));
		}

		public void setPaddle2Y(float yPos, float height) {
				paddle2.setY(height - yPos);
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

		@Override
		public boolean isActive() {
				return (this.stateManager.getActiveStateName().equals(this.stateName));
		}

		protected boolean isStarted = false;
		protected boolean isPaused = false;

		public void start() {
				isStarted = true;
				isPaused = false;
		}

		public void pause() {
				isPaused = true;
		}

		public void stop() {
				isStarted = false;
				isPaused = false;
		}

		//	public boolean newBall() {
		//			return PhysicsHelper.isDead(width, height, balls);
		//	}


}
