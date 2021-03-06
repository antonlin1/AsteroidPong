package com.mygdx.game.view.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.PeerHelperInterface;
import com.mygdx.game.WifiDirectInterface;
import com.mygdx.game.model.Ball;
import com.mygdx.game.model.PhysicsHelper;
import com.mygdx.game.model.ServerToClientMessage;
import com.mygdx.game.view.MyGdxGame;

/**
 * Created by hampusballdin on 2016-05-08.
 */
public class MultiplayerClient extends Multiplayer {
		private long lastNanoTimeUpdate = 0l;
		protected Texture[] planetDown, planetUp;
		//	protected int scoreDown, scoreUp;
		protected boolean isDeadUp, isDeadDown;

		private boolean otherActive = false;

		public MultiplayerClient(MyGdxGame game, StateManager stateManager, float width, float height, PeerHelperInterface peerHelper, WifiDirectInterface wifiDirect) {

				super(game, stateManager, width, height, peerHelper, wifiDirect,
						StateManager.STATE_NAME.MULTIPLAYER_CLIENT_STATE);

				planetDown = new Texture[4];
				planetDown[0] = new Texture("MoonLifeDown25.png");
				planetDown[1] = new Texture("MoonLifeDown50.png");
				planetDown[2] = new Texture("MoonLifeDown75.png");
				planetDown[3] = new Texture("MoonLifeDown100.png");


				planetUp = new Texture[4];
				planetUp[0] = new Texture("EarthLifeUp25.png");
				planetUp[1] = new Texture("EarthLifeUp50.png");
				planetUp[2] = new Texture("EarthLifeUp75.png");
				planetUp[3] = new Texture("EarthLifeUp100.png");
		}

		@Override
		public void update() {
				super.update();
				Ball ball = balls.get(0);
				handleTouchInput();
				handleSpeechInput();
				double w = Gdx.graphics.getWidth();
				double h = Gdx.graphics.getHeight();
				ServerToClientMessage state = wifiDirect.getNetworkComponent().getServerData();


				if (otherActive && isActive()) {
						countDown();
				} else {
						drawWaitingForOtherPlayer();
				}

				if (wifiDirect.getNetworkComponent().isClientUpdated()) {
						otherActive = state.isGameActive();
						System.out.println("CLIENT UUUUUPDATE");
						gameOverEvent = state.getGameOverEvent();
						if (countDownDone && otherActive && isActive()) {
								long currentNanoTime = state.getNanoTime();
								playerUp.setHp(state.getHpServer());
								playerDown.setHp(state.getHpClient());

								if (currentNanoTime > lastNanoTimeUpdate) {
										isDead = false;
										ball.setxVelocity(-state.getBallXVelocity());
										ball.setyVelocity(-state.getBallYVelocity());

										ball.setX((float) (w - (state.getBallX()) * w));
										ball.setY((float) (h - (state.getBallY()) * h));

										setPaddle2X((float) (state.getPaddleX() * w), (float) w);
										setPaddle2Y((float) (state.getPaddleY() * h), (float) h);
										lastNanoTimeUpdate = currentNanoTime;
								} else {
										clientRegularUpdate(ball);
								}
						}

				} else {
						if (countDownDone && state.isGameActive() && isActive()) {
								clientRegularUpdate(ball);
						}
				}

				if (gameOverEvent.value != GameOverEvent.NOT_OVER.value && countDownDone) {
						resetCountDown();
						resetPlayerHp();
					if(gameOverEvent.value == GameOverEvent.SERVER_WON.value) {
						Gdx.input.vibrate(1000);
					}
					otherActive = false;
						stateManager.push(new GameOverState(game, stateManager, wifiDirect, peerHelperInterface,
								(gameOverEvent.value == GameOverEvent.CLIENT_WON.value)));
				}

				wifiDirect.getNetworkComponent().setClientToServerData(this.isActive(), this.isPaused,
						(float) (paddle1.getX() / w), (float) (paddle1.getY() / h));
		}

		private void clientRegularUpdate(Ball ball) {
				wallCollision = PhysicsHelper.wallCollision(width, height, balls);
				paddleCollision = PhysicsHelper.paddleCollision(getPaddles(), balls);
				isDead = PhysicsHelper.isDead(width, height, balls);
				ball.move();
		}


		@Override
		public void render(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
				super.render(spriteBatch, shapeRenderer);

			System.out.println("MULTIPLAYER CLIENT RENDER!!!");
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

