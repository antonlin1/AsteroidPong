package com.mygdx.game.view.States;

import com.badlogic.gdx.Gdx;
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

		public MultiplayerClient(MyGdxGame game, StateManager stateManager, float width, float height,
								 PeerHelperInterface peerHelper, WifiDirectInterface wifiDirect) {
				super(game, stateManager, width, height, peerHelper, wifiDirect,
						StateManager.STATE_NAME.MULTIPLAYER_CLIENT_STATE);
		}

		@Override
		public void update() {
				super.update();
				Ball ball = balls.get(0);
				handleTouchInput();
				double w = Gdx.graphics.getWidth();
				double h = Gdx.graphics.getHeight();
				wifiDirect.getNetworkComponent().setClientToServerData(
						(float) (paddle1.getX() / w), (float) (paddle1.getY() / h));

				if (wifiDirect.getNetworkComponent().isClientUpdated()) {
						System.out.println("CLIENT UUUUUPDATE");
						ServerToClientMessage state = wifiDirect.getNetworkComponent().getServerData();

						long currentNanoTime = state.getNanoTime();

						if(currentNanoTime > lastNanoTimeUpdate) {
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
				} else {
						clientRegularUpdate(ball);
				}
		}

		private void clientRegularUpdate(Ball ball) {
				wallCollision = PhysicsHelper.wallCollision(width, height, balls);
				paddleCollision = PhysicsHelper.paddleCollision(getPaddles(), balls);
				isDead = PhysicsHelper.isDead(width, height, balls);
				ball.move();
		}
}
