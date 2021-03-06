package com.mygdx.game.view.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.PeerHelperInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.model.Ball;
import com.mygdx.game.model.ClientToServerMessage;
import com.mygdx.game.model.PhysicsHelper;
import com.mygdx.game.view.MyGdxGame;

/**
 * Created by hampusballdin on 2016-05-08.
 */
public class MultiplayerServer extends Multiplayer {

		protected Texture[] planetDown, planetUp;
		protected boolean isDeadUp, isDeadDown;
		private boolean otherActive = false;

		public MultiplayerServer(MyGdxGame game, StateManager stateManager, float width, float height,
								 PeerHelperInterface peerHelper, WifiDirectInterface wifiDirect) {

				super(game, stateManager, width, height, peerHelper, wifiDirect, StateManager.STATE_NAME.MULTIPLAYER_SERVER_STATE);

				planetDown = new Texture[4];
				planetDown[0] = new Texture("EarthLifeDown25.png");
				planetDown[1] = new Texture("EarthLifeDown50.png");
				planetDown[2] = new Texture("EarthLifeDown75.png");
				planetDown[3] = new Texture("EarthLifeDown100.png");


				planetUp = new Texture[4];
				planetUp[0] = new Texture("MoonLifeUp25.png");
				planetUp[1] = new Texture("MoonLifeUp50.png");
				planetUp[2] = new Texture("MoonLifeUp75.png");
				planetUp[3] = new Texture("MoonLifeUp100.png");
		}

		@Override
		public void update() {
				super.update();
				Ball ball = balls.get(0);
				double w = Gdx.graphics.getWidth();
				double h = Gdx.graphics.getHeight();
				ClientToServerMessage clientMessage = wifiDirect.getNetworkComponent().getClientData();
				otherActive = clientMessage.isGameActive();

				if(!countDownDone) {
					gameOverEvent = GameOverEvent.NOT_OVER;
				}

				if (otherActive && isActive()) {
						countDown();
				} else {
						drawWaitingForOtherPlayer();
				}

				if (countDownDone && isActive() && otherActive) {
						wallCollision = PhysicsHelper.wallCollision(width, height, balls);
						paddleCollision = PhysicsHelper.paddleCollision(getPaddles(), balls);
						isDead = PhysicsHelper.isDead(width, height, balls);
						setPaddle2X(wifiDirect.getNetworkComponent().getOpponentPaddleX() *
								Gdx.graphics.getWidth(), (float) Gdx.graphics.getWidth());

						setPaddle2Y(wifiDirect.getNetworkComponent().getOpponentPaddleY() *
								Gdx.graphics.getHeight(), (float) Gdx.graphics.getHeight());

						ball.move();
						handleTouchInput();
						handleSpeechInput();

				}
				//Must happen in following order:

				//1
				isDeadDown = PhysicsHelper.isDeadDown(width, height, balls);
				isDeadUp = PhysicsHelper.isDeadUp(width, height, balls);

				//2 calculate gameOverState
				if(isDead() && isDeadDown) {
					playerDown.decreaseHp();
					gameOverEvent = (playerDown.isDead()) ? GameOverEvent.CLIENT_WON : GameOverEvent.NOT_OVER;
				} else if(isDead() && isDeadUp) {
					playerUp.decreaseHp();
					gameOverEvent = (playerUp.isDead()) ? GameOverEvent.SERVER_WON : GameOverEvent.NOT_OVER;
				}


				//3 send gameOverStateToClient
				wifiDirect.getNetworkComponent().setServerToClientData(this.isActive(), this.isPaused,
					paddleCollision, wallCollision, (float) (paddle1.getX() / w),
					(float) (paddle1.getY() / h), (float) (ball.getX() / w),
					(float) (ball.getY() / h), ball.getXVel(), ball.getYVel(), ball.getVelocity(), w, h, playerUp.getHp(), playerDown.getHp(), gameOverEvent);

				//4 transition to gameOverState
				if(gameOverEvent.value == GameOverEvent.SERVER_WON.value){
					resetCountDown();
					resetPlayerHp();
					stateManager.push(new GameOverState(game, stateManager, wifiDirect, peerHelperInterface, true));
				} else if(gameOverEvent.value == GameOverEvent.CLIENT_WON.value) {
					resetCountDown();
					resetPlayerHp();
					Gdx.input.vibrate(1000);
					stateManager.push(new GameOverState(game, stateManager, wifiDirect, peerHelperInterface, false));
				}
		}


		@Override
		public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
				super.render(spriteBatch, shapeRenderer);

			System.out.println("MULTIPLAYER SERVER RENDER!!!");

				spriteBatch.begin();
				spriteBatch.draw(planetDown[playerDown.getHp() - 1], 0, Gdx.graphics.getHeight() - planetDown[playerDown.getHp() - 1].getHeight());
				spriteBatch.draw(planetUp[playerUp.getHp() - 1], 0, 0);
				spriteBatch.end();
				stage.draw();
		}

		@Override
		public boolean isOtherActive() {
				return otherActive;
		}

}
